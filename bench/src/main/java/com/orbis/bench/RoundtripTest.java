package com.orbis.bench;

import java.io.*;
import java.util.*;
import com.github.sd.*;

public class RoundtripTest
    {
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

                var start = System.currentTimeMillis();
                api.getQuotes("MSFT", "F", "AAPL", "GOOG");
                System.out.printf("Got quotes in %.3fs%n", (System.currentTimeMillis() - start) / 1000.0);
            }
    }
