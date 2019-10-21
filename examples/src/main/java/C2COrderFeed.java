import java.io.*;
import java.util.*;
import com.github.sd.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

/**
 * Created by Daniil Sosonkin
 * 5/15/2019 9:30 PM
 */
public class C2COrderFeed implements OrbisApiClient
    {
        private WebSocketClient ws;

        public static void main(String[] args) throws Exception
            {
                var props = new Properties();
                try (var in = new FileInputStream("c2c.properties"))
                    {
                        props.load(in);
                    }

                var hostname = props.getProperty("api.hostname");
                var c2c = new C2C();
                c2c.setEntity(props.getProperty("c2c.entity"));
                c2c.setGroup(props.getProperty("c2c.group"));
                c2c.setSecret(props.getProperty("c2c.secret"));
                c2c.setHostname(hostname);

                OrbisAPI api = new OrbisAPI();
                api.setCredentials(c2c);
                api.setHostname(hostname);

                C2COrderFeed feed = new C2COrderFeed();
                feed.ws = api.openWebSocket(feed);
            }

        @Override
        public String getEndpoint()
            {
                return "/orders/feed";
            }

        @Override
        public void onOpen(ServerHandshake handshakedata)
            {
                System.out.println("(*) Connection established");
                ws.send("{action: 'replay', from: '10/21/2019 00:00:00 EST', to: '10/22/2019 00:00:00 EST'}");
            }

        @Override
        public void onMessage(String message)
            {
                System.out.println("(=) " + message);
            }

        @Override
        public void onClose(int code, String reason, boolean remote)
            {
                System.out.printf("(-) Closed. Code: %s; Reason: %s; Remote: %s\n", code, reason, remote);
            }

        @Override
        public void onError(Exception ex)
            {
                ex.printStackTrace();
            }
    }
