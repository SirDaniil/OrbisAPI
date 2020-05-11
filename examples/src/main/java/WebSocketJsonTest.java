import java.io.*;
import java.nio.*;
import java.util.*;
import com.github.sd.*;
import org.java_websocket.handshake.*;
import org.json.*;

public class WebSocketJsonTest extends WebSocketTest
    {
        public static void main(String[] args) throws Exception
            {
                var settings = "settings.conf";
                var props = new Properties();
                try (InputStream in = new FileInputStream(settings))
                    {
                        props.load(in);
                    }

                var api = new OrbisAPI();
                api.setHostname(props.getProperty("hostname"));
                api.setCredentials(new PublicKeyCredentials(props.getProperty("key.file")));

                var test = new WebSocketJsonTest();
                test.ws = api.openWebSocket(test);
            }

        @Override
        public void onOpen(ServerHandshake handshakedata)
            {
                System.out.println("(+) Connection opened");

                JSONObject mode = new JSONObject();
                mode.put("action", "mode");
                mode.put("mode", "json");
                System.out.println("(+) Switching to JSON: " + mode);

                JSONObject sub = new JSONObject();
                sub.put("action", "sub");
                sub.put("mic", "XNGS");
                System.out.println("(+) Subscribing: " + sub);

                ws.send(mode.toString());
                ws.send(sub.toString());
            }

        @Override
        public void onMessage(ByteBuffer bytes)
            {
                var msg = new String(bytes.array());
                var obj = new JSONObject(msg);
                var timestamp = new Date(obj.getLong("ts"));
                var receivedOn = new Date(obj.getLong("rts"));
                long lag = (System.currentTimeMillis() - timestamp.getTime());
                long quoteLag = (System.currentTimeMillis() - receivedOn.getTime());

                System.out.printf("(lag=%s, quote lag=%s) %s", lag, quoteLag, msg);
            }
    }
