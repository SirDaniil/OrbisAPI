import java.io.*;
import java.nio.*;
import java.util.*;
import com.github.sd.*;
import org.bson.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.json.*;

/**
 * Created by Daniil Sosonkin
 * 4/4/2018 9:43 PM
 */
public class WebSocketTest implements OrbisApiClient
    {
        private BasicBSONDecoder decoder = new BasicBSONDecoder();
        private String[] symbols;
        private WebSocketClient ws;

        public static void main(String[] args) throws Exception
            {
                String settings = args.length > 0 ? args[0] : "settings.conf";
                Properties props = new Properties();
                try (InputStream in = new FileInputStream(settings))
                    {
                        props.load(in);
                    }

                OrbisAPI api = new OrbisAPI();
                api.setHostname(props.getProperty("hostname"));
                api.setCredentials(new PublicKeyCredentials(props.getProperty("key.file")));

                WebSocketTest test = new WebSocketTest();
                test.symbols = props.getProperty("symbols").split(",");
                test.ws = api.openWebSocket(test);
            }

        @Override
        public void onOpen(ServerHandshake handshakedata)
            {
                System.out.println("(+) Connection opened");

                JSONObject sub = new JSONObject();
                sub.put("action", "sub");
                sub.put("symbols", new JSONArray(Arrays.asList(symbols)));
                System.out.println("(+) Subscribing: " + sub);

                ws.send(sub.toString());
            }

        @Override
        public void onMessage(String message)
            { }

        @Override
        public void onClose(int code, String reason, boolean remote)
            {
                System.out.println("(-) Code: " + code + "; Reason: " + reason + "; Remote: " + remote);
            }

        @Override
        public void onError(Exception ex)
            {
                ex.printStackTrace();
            }

        @Override
        public void onMessage(ByteBuffer bytes)
            {
                BSONObject object = decoder.readObject(bytes.array());
                Date timestamp = (Date) object.get("ts");
                long lag = (System.currentTimeMillis() - timestamp.getTime());
                System.out.println("(" + lag + ") " + object);
            }
    }
