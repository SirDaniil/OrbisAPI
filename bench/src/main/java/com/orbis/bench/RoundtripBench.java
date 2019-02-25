package com.orbis.bench;

import java.io.*;
import java.nio.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import com.github.sd.*;
import com.mongodb.*;
import com.mongodb.client.model.*;
import com.orbis.util.*;
import org.bson.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.json.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

/**
 * Created by Daniil Sosonkin
 * 2/23/2019 9:31 PM
 */
public class RoundtripBench implements OrbisApiClient
    {
        private BasicBSONDecoder decoder = new BasicBSONDecoder();
        private WebSocketClient ws;
        private String markId = "ATEST";
        private Properties props;
        private AtomicLong lastMarkDelta = new AtomicLong();
        private NumberFormat df3 = DecimalFormat.getInstance();

        public static void main(String[] args) throws Exception
            {
                String settings = "bench.conf";

                for (String arg : args)
                    if (arg.length() > 0 && arg.charAt(0) != '-')
                        settings = arg;

                Properties props = new Properties();
                try (InputStream in = new FileInputStream(settings))
                    {
                        props.load(in);
                    }
                catch (IOException e)
                    {
                        System.out.println(new File(settings).getAbsolutePath());
                        throw e;
                    }

                OrbisAPI api = new OrbisAPI();
                api.setHostname(props.getProperty("hostname"));
                api.setCredentials(new PublicKeyCredentials(props.getProperty("key.file")));

                var bench = new RoundtripBench();
                bench.props = props;
                bench.ws = api.openWebSocket(bench);

                new Thread(bench::beginBench).start();
            }

        public RoundtripBench()
            {
                df3.setMinimumFractionDigits(3);
                df3.setMaximumFractionDigits(3);
            }

        @Override
        public void onOpen(ServerHandshake handshakedata)
            {
                System.out.println("(+) Connection opened");

                JSONObject sub = new JSONObject();
                sub.put("action", "sub");
                sub.put("symbols", new JSONArray(Arrays.asList(markId)));
                System.out.println("(+) Subscribing: " + sub);

                ws.send(sub.toString());
            }

        @Override
        public void onMessage(String message)
            {
                System.out.println("(*) Message: " + message);
            }

        @Override
        public void onClose(int code, String reason, boolean remote)
            {
                System.out.println("(-) Code: " + code + "; Reason: " + reason + "; Remote: " + remote);
            }

        @Override
        public void onError(Exception ex)
            { }

        @Override
        public void onMessage(ByteBuffer bytes)
            {
                var now = System.currentTimeMillis();
                var object = decoder.readObject(bytes.array());

                if (!object.containsField("u"))
                    return;

                BSONObject update = (BSONObject) object.get("u");
                BSONObject fields = (BSONObject) update.get(markId);
                if (fields == null)
                    return;

                if (!fields.containsField("ts"))
                    return;

                if (!fields.containsField("px"))
                    return;

                Date date = (Date) fields.get("ts");
                String price = ((Number)fields.get("px")).longValue() + "";
                if (!Verhoeff.validateVerhoeff(price))
                    return;

                System.out.println("(@) Roundtrip time: " + df3.format((now - date.getTime()) / 1000.0) + "; Mark set time: " + df3.format(lastMarkDelta.get() / 1000.0));
            }

        private void beginBench()
            {
                MongoClientURI uri = new MongoClientURI(props.getProperty("db"));
                System.out.println("(+) Connecting to [" + uri + "]");

                var client = new MongoClient(uri);
                var db = client.getDatabase(uri.getDatabase());
                System.out.println("(+) Connected");

                var col = db.getCollection("QuoteL1").withWriteConcern(WriteConcern.UNACKNOWLEDGED);
                col.deleteOne(eq("_id", markId));
                System.out.println("(*) Mark cleared");

                long index = 1;
                while (true)
                    try
                        {
                            index++;
                            long start = System.currentTimeMillis();
                            col.updateOne(
                                    eq("_id", markId),
                                    combine(
                                            set("QuoteTime", new Date()),
                                            set("LastPx", Long.parseLong(index + Verhoeff.generateVerhoeff(index)))
                                    ),
                                    new UpdateOptions().upsert(true)
                            );
                            lastMarkDelta.set(System.currentTimeMillis() - start);
                            Thread.sleep(1000);
                        }
                    catch (Exception e)
                        {
                            e.printStackTrace();
                            System.exit(1);
                        }
            }
    }
