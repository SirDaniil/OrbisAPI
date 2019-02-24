import java.awt.*;
import java.io.*;
import java.nio.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
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
        private DateFormat hhmmssS = new SimpleDateFormat("HH:mm:ss.SSS");
        private Map<String, UI> uis = new HashMap<>();
        private boolean ui;

        public static void main(String[] args) throws Exception
            {
                boolean ui = false;
                String settings = "settings.conf";

                for (String arg : args)
                    if ("-ui".equalsIgnoreCase(arg))
                        ui = true;
                    else if (arg.length() > 0 && arg.charAt(0) != '-')
                        settings = arg;

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


                Properties props = new Properties();
                try (InputStream in = new FileInputStream(settings))
                    {
                        props.load(in);
                    }

                OrbisAPI api = new OrbisAPI();
                api.setHostname(props.getProperty("hostname"));
                api.setCredentials(new PublicKeyCredentials(props.getProperty("key.file")));

                WebSocketTest test = new WebSocketTest();
                test.ui = ui;
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

                if (!ui)
                    return;

                if (object.containsField("u"))
                    {
                        BSONObject update = (BSONObject)object.get("u");
                        for (String symbol : update.keySet())
                            {
                                UI ui = uis.computeIfAbsent(symbol, UI::new);

                                BSONObject fields = (BSONObject) update.get(symbol);

                                if (object.containsField("ts"))
                                    ui.lbSentTimestamp.setText(hhmmssS.format(object.get("ts")));

                                if (fields.containsField("px"))
                                    ui.lbPrice.setText(fields.get("px").toString());

                                if (fields.containsField("bx"))
                                    ui.lbBid.setText(fields.get("bx").toString());

                                if (fields.containsField("ax"))
                                    ui.lbAsk.setText(fields.get("ax").toString());

                                if (fields.containsField("bz"))
                                    ui.lbBidSize.setText(fields.get("bz").toString());

                                if (fields.containsField("az"))
                                    ui.lbAskSize.setText(fields.get("az").toString());

                                if (fields.containsField("ts"))
                                    ui.lbQuoteTimestamp.setText(hhmmssS.format(fields.get("ts")));

                                if (fields.containsField("rts"))
                                    ui.lbReceivedTimestamp.setText(hhmmssS.format(fields.get("rts")));

                                if (fields.containsField("dts"))
                                    ui.lbDisptachedTimestamp.setText(hhmmssS.format(fields.get("dts")));

                                ui.lbNowTimestamp.setText(hhmmssS.format(new Date()));
                            }
                    }
            }

        class UI extends JFrame
            {
                private JLabel lbBid = new JLabel();
                private JLabel lbBidSize = new JLabel();
                private JLabel lbAsk = new JLabel();
                private JLabel lbAskSize = new JLabel();
                private JLabel lbPrice = new JLabel();
                private JLabel lbSentTimestamp = new JLabel();
                private JLabel lbReceivedTimestamp = new JLabel();
                private JLabel lbDisptachedTimestamp = new JLabel();
                private JLabel lbQuoteTimestamp = new JLabel();
                private JLabel lbNowTimestamp = new JLabel();

                UI(String symbol) throws HeadlessException
                    {
                        JPanel panel = new JPanel(new GridLayout(10, 2, 2, 2));
                        panel.setBorder(BorderFactory.createTitledBorder(" " + symbol + " "));
                        panel.add(new JLabel("Price:"));
                        panel.add(lbPrice);
                        panel.add(new JLabel("Bid:"));
                        panel.add(lbBid);
                        panel.add(new JLabel("BidSz:"));
                        panel.add(lbBidSize);
                        panel.add(new JLabel("Ask:"));
                        panel.add(lbAsk);
                        panel.add(new JLabel("AskSz:"));
                        panel.add(lbAskSize);
                        panel.add(new JLabel("Quoted on:"));
                        panel.add(lbQuoteTimestamp);
                        panel.add(new JLabel("Q received on:"));
                        panel.add(lbReceivedTimestamp);
                        panel.add(new JLabel("Q dispatched on:"));
                        panel.add(lbDisptachedTimestamp);
                        panel.add(new JLabel("WS sent on:"));
                        panel.add(lbSentTimestamp);
                        panel.add(new JLabel("Local:"));
                        panel.add(lbNowTimestamp);

                        JFrame frame = new JFrame();
                        frame.setTitle(symbol);
                        frame.add(panel);
                        frame.pack();
                        frame.setVisible(true);
                    }
            }
    }
