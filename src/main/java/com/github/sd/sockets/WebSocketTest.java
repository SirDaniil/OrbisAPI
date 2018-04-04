package com.github.sd.sockets;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import com.github.sd.*;
import net.iharder.Base64;
import org.bson.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 3/20/2018 1:37 PM
 */
public class WebSocketTest extends WebSocketClient
    {
        private BasicBSONDecoder decoder = new BasicBSONDecoder();
        private String[] symbols;

        public static void main(String[] args) throws Exception
            {
                String settings = args.length > 0 ? args[0] : "settings.conf";
                Properties props = new Properties();
                try (InputStream in = new FileInputStream(settings))
                    {
                        props.load(in);
                    }

                String filename = props.getProperty("key.file");
                Credentials credentials = new PublicKeyCredentials(filename);

                WebSocketTest ws = new WebSocketTest(new URI(props.getProperty("hostname") + "/stream?auth=" + URLEncoder.encode(credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()), "ISO-8859-1")));
                ws.symbols = props.getProperty("symbols").split(",");
                ws.connect();
            }

        private WebSocketTest(URI serverUri)
            {
                super(serverUri);
            }

        @Override
        public void onOpen(ServerHandshake handshakedata)
            {
                System.out.println("(+) Connection opened");

                JSONObject sub = new JSONObject();
                sub.put("action", "sub");
                sub.put("symbols", new JSONArray(Arrays.asList(symbols)));

                send(sub.toString());
            }

        @Override
        public void onMessage(String message)
            {
                System.out.println("(*) " + message);
            }

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
                BSONObject array = decoder.readObject(bytes.array());
                System.out.println(array);
            }
    }
