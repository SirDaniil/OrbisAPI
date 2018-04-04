package com.github.sd;

import java.io.*;
import java.net.*;
import java.util.*;
import net.iharder.Base64;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 2:42 PM
 */
public class OrbisAPI
    {
        private static final Set<Integer> oks = new HashSet<Integer>() {{
            add(200);
            add(201);
        }};
        private Credentials credentials;
        private String hostname;
        private String api = "/api";
        private String scheme = "https";

        public OrbisAPI setCredentials(Credentials credentials)
            {
                this.credentials = credentials;
                return this;
            }

        public OrbisAPI setHostname(String hostname)
            {
                this.hostname = hostname;
                return this;
            }

        public OrbisAPI setApi(String api)
            {
                this.api = api;
                return this;
            }

        public JSONArray getQuotes(String... symbols) throws IOException
            {
                return new JSONArray(get("/quotes/equity", "symbols", String.join(",", symbols)));
            }

        public JSONArray quoteSearch(String criteria) throws IOException
            {
                return new JSONArray(get("/quotes/search", "criteria", criteria));
            }

        private String get(String path, String name, Object value) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put(name, value);

                return get(path, params);
            }

        private String get(String path, Map<String, Object> params) throws IOException
            {
                StringBuilder args = new StringBuilder();
                params.forEach((k, v) -> {
                    if (k == null)
                        k = "";

                    if (v == null)
                        v = "";

                    args.append(encode(k));
                    args.append('=');
                    args.append(encode(v));
                    args.append('&');
                });
                URL url = new URL(hostname + api + path + (args.length() > 0 ? "?" + args : ""));
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()));
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);

                StringBuilder response = new StringBuilder();
                byte[] buf = new byte[1024 * 1024];
                int size;
                int code = con.getResponseCode();

                try (InputStream in = (oks.contains(code) ? con.getInputStream() : con.getErrorStream()))
                    {
                        while ((size = in.read(buf)) > 0)
                            response.append(new String(buf, 0, size));
                    }

                return response.toString();
            }

        private String encode(Object o)
            {
                try
                    {
                        return URLEncoder.encode(o.toString(), "ISO-8859-1");
                    }
                catch (UnsupportedEncodingException e)
                    {
                        return o.toString();
                    }
            }

    }
