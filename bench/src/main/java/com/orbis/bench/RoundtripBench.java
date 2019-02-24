package com.orbis.bench;

import java.io.*;
import java.nio.*;
import java.util.*;
import com.github.sd.*;
import com.mongodb.*;
import com.mongodb.client.model.*;
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

                var test = new RoundtripBench();
                test.props = props;
                test.ws = api.openWebSocket(test);
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

                new Thread(this::beginBench).start();
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
                System.out.println(object);

                if (object.containsField("u"))
                    {
                        BSONObject update = (BSONObject) object.get("u");
                        for (String symbol : update.keySet())
                            {
                                BSONObject fields = (BSONObject) update.get(symbol);

                                if (!fields.containsField("ts"))
                                    return;

                                Date date = (Date)fields.get("ts");
                                System.out.println("Roundtrip time: " + (now - date.getTime()) / 1000.0);
                            }
                    }
            }

        private void beginBench()
            {
                MongoClientURI uri = new MongoClientURI(props.getProperty("db"));
                System.out.println("(+) Connecting to [" + uri + "]");

                var client = new MongoClient(uri);
                var db = client.getDatabase(uri.getDatabase());
                System.out.println("(+) Connected");

                var col = db.getCollection("QuoteL1");
                col.deleteOne(eq("_id", markId));
                System.out.println("(*) Mark cleared");

                while (true)
                    try
                        {
                            //col.updateOne(eq("_id", markId), combine(set("QuoteTime", new Date()), set("LastPx", Math.random())), new UpdateOptions().upsert(true));
                            col.updateOne(eq("_id", markId), set("QuoteTime", new Date()), new UpdateOptions().upsert(true));
                            System.out.println("(*) Mark set on [" + new Date() + "]");
                            Thread.sleep(1000);
                        }
                    catch (Exception e)
                        {
                            e.printStackTrace();
                            System.exit(1);
                        }
            }
    }
