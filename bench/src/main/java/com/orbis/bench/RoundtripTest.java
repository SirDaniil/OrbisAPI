package com.orbis.bench;

import java.io.*;
import java.util.*;
import com.github.sd.*;

public class RoundtripTest implements LogListener
    {
        private long responseDelta;
        private int responseCode;

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
                api.setListner(new RoundtripTest());

                while (true)
                    api.getQuotes("MSFT", "F", "AAPL", "GOOG");
            }

        @Override
        public void serverResponded(int code, long delta)
            {
                this.responseCode = code;
                this.responseDelta = delta;
            }

        @Override
        public void contentRead(boolean gzip, long delta, int read)
            {
                System.out.printf("[%s] Server response: %.3f; content read in: %.3f (gzip: %s; read: %.2fkb)%n", responseCode, responseDelta / 1000.0, delta / 1000.0, gzip, read / 1024.0);
            }
    }
