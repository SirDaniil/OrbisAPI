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
        private final BasicBSONDecoder decoder = new BasicBSONDecoder();
        private final DateFormat hhmmssS = new SimpleDateFormat("HH:mm:ss.SSS");
        private final Map<String, UI> uis = new HashMap<>();
        private final LinkedList<LagEntry> lags = new LinkedList<>();
        private final LinkedList<BytesEntry> bytes = new LinkedList<>();
        private boolean ui;
        private String[] symbols;
        protected WebSocketClient ws;

        public static void main(String[] args) throws Exception
            {
                boolean ui = false;

                for (String arg : args)
                    if ("-ui".equalsIgnoreCase(arg))
                        ui = true;

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                OrbisAPI api = new OrbisAPI();
                api.setCredentials(new SessionCredentials());

                WebSocketTest test = new WebSocketTest();
                test.ui = ui;
                test.symbols = new String[] {"MSFT", "AAPL"};
                test.ws = api.openWebSocket(test);
            }

        public static void main1(String[] args) throws Exception
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
                //sub.put("mic", "XNGS");
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
                var now = System.currentTimeMillis();
                var array = bytes.array();
                var object = decoder.readObject(array);
                var timestamp = (Date) object.get("ts");
                long lag = (now - timestamp.getTime());

                addLastLag(new LagEntry(now, lag));
                addLastBytes(new BytesEntry(now, array.length));

                System.out.printf("(lag=%s (min=%s, max=%s); bytes=%s %s; messages=%s) %s%n", lag, longestLag(), shortestLag(), array.length, speed(), messages(), object);

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

        private Object messages()
            {
                return bytes.size() + "/s";
            }

        private Object speed()
            {
                double total = 0;
                for (var entry : bytes)
                    total += entry.bytes;

                return (total / 1024.0) + " KB/s";
            }

        private void addLastBytes(BytesEntry entry)
            {
                bytes.addLast(entry);

                BytesEntry first;
                while ((first = bytes.peek()) != null && entry.timestamp - first.timestamp > 1000)
                    bytes.removeFirst();
            }

        private void addLastLag(LagEntry entry)
            {
                lags.addLast(entry);

                LagEntry first;
                while ((first = lags.peek()) != null && entry.timestamp - first.timestamp > 10 * 1000)
                    lags.removeFirst();
            }

        private long longestLag()
            {
                var lag = 0L;
                for (var entry : lags)
                    if (entry.lag > lag)
                        lag = entry.lag;

                return lag;
            }

        private long shortestLag()
            {
                var lag = Long.MAX_VALUE;
                for (var entry : lags)
                    if (entry.lag < lag)
                        lag = entry.lag;

                return lag;
            }

        static class BytesEntry
            {
                long timestamp;
                int bytes;

                public BytesEntry(long timestamp, int bytes)
                    {
                        this.timestamp = timestamp;
                        this.bytes = bytes;
                    }
            }

        static class LagEntry
            {
                long timestamp;
                long lag;

                public LagEntry(long timestamp, long lag)
                    {
                        this.timestamp = timestamp;
                        this.lag = lag;
                    }
            }

        static class UI extends JFrame
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
