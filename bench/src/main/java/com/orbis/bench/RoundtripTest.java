package com.orbis.bench;

import java.io.*;
import java.util.*;
import com.github.sd.*;

public class RoundtripTest implements LogListener
    {
        private Stapi stapi;
        private long responseDelta;
        private long sendDelta;
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
                api.setListner(new RoundtripTest(props));

                while (true)
                    api.getQuotes("MSFT", "F", "AAPL", "GOOG");
            }

        public RoundtripTest(Properties props)
            {
                var stitoken = props.getProperty("stapi");
                if (stitoken != null)
                    stapi = new Stapi(stitoken);
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
                if (stapi != null)
                    stapi.add(new StapiData(delta / 1000.0));

                System.out.printf("[%.3f -> %s] Server response: %.3f; content read in: %.3f (compression: %s; read: %.2fkb)%n", sendDelta / 1000.0,  responseCode, responseDelta / 1000.0, delta / 1000.0, compressed, read / 1024.0);
            }
    }
