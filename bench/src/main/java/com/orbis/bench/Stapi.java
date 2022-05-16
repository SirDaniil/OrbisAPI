package com.orbis.bench;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class Stapi extends Thread
    {
        private final BlockingQueue<StapiData> queue = new LinkedBlockingQueue<>();
        private String region;
        private String token;

        public Stapi(String token, String region)
            {
                this.token = token;
                this.region = region;

                start();
            }

        public void add(StapiData data)
            {
                if (!queue.offer(data))
                    System.out.println("Failed to add to the queue");
            }

        @Override
        public void run()
            {
                while (true)
                    try
                        {
                            store(queue.take());
                        }
                    catch (InterruptedException | IOException e)
                        {
                            e.printStackTrace();
                        }
            }

        private void store(StapiData data) throws IOException
            {
                var str = data.toJSON(region);
                var url = new URL("https://bo.orbisfn.com/stapi/v2/tracking/add");
                var con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setRequestProperty("Content-Length", String.valueOf(str.length()));
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept-Encoding", "deflate, gzip");
                con.setRequestMethod("POST");
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                try (Writer out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8)))
                    {
                        out.write(str);
                        out.flush();
                    }

                try (var in = con.getInputStream())
                    {
                        byte[] buf = new byte[1024 * 65];
                        int size;

                        while ((size = in.read(buf)) > 0);
                    }
            }
    }
