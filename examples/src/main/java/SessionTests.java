import com.github.sd.*;

import java.io.*;

public class SessionTests implements LogListener
    {
        private long responseDelta;
        private long sendDelta;
        private int responseCode;

        public static void main(String[] args) throws IOException
            {
                var api = new OrbisAPI();
                api.setListner(new SessionTests());
                api.setCredentials(new SessionCredentials());
                System.out.println(api.getUserInfo());
            }

        @Override
        public void sent(long delta)
            {
                this.sendDelta = delta;
            }

        @Override
        public void serverResponded(int code, long delta)
            {
                this.responseCode = code;
                this.responseDelta = delta;
            }

        @Override
        public void contentRead(boolean compressed, long delta, int read, String encoding)
            {
                System.out.printf("[%.3f -> %s] Server response: %.3f; content read in: %.3f (compression: %s; read: %.2fkb)%n", sendDelta / 1000.0,  responseCode, responseDelta / 1000.0, delta / 1000.0, compressed, read / 1024.0);
            }
    }
