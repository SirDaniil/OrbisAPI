package com.github.sd;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;
import net.iharder.Base64;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 2:42 PM
 */
public class OrbisAPI
    {
        public static final Set<Integer> oks = new HashSet<>() {{
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
                if (hostname.startsWith("http://")) {
                    hostname = hostname.substring("http://".length());
                    scheme = "http";
                }

                if (hostname.startsWith("https://")) {
                    hostname = hostname.substring("https://".length());
                    scheme = "https";
                }

                this.hostname = hostname;
                return this;
            }

        public OrbisAPI setApi(String api)
            {
                this.api = api;
                return this;
            }

        public WebSocketClient openWebSocket(OrbisApiClient client) throws Exception
            {
                WebSocketClient ws = new WebSocketClient(new URI((scheme.equals("http") ? "ws" : "wss") + "://" + hostname + "/stream?auth=" + URLEncoder.encode(credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()), StandardCharsets.ISO_8859_1)))
                    {
                        @Override
                        public void onOpen(ServerHandshake handshakedata)
                            {
                                client.onOpen(handshakedata);
                            }

                        @Override
                        public void onMessage(String message)
                            {
                                client.onMessage(message);
                            }

                        @Override
                        public void onClose(int code, String reason, boolean remote)
                            {
                                client.onClose(code, reason, remote);
                            }

                        @Override
                        public void onError(Exception ex)
                            {
                                client.onError(ex);
                            }

                        @Override
                        public void onMessage(ByteBuffer bytes)
                            {
                                client.onMessage(bytes);
                            }
                    };
                ws.setTcpNoDelay(true);
                ws.connect();

                return ws;
            }

        public JSONArray getChartIntraday(String symbol) throws IOException
            {
                return get(Endpoints.ChartsIntraday, "symbol", symbol);
            }

        public JSONArray getCorporateActionTypes() throws IOException
            {
                return get(Endpoints.CorporateActionTypes);
            }

        public JSONArray corporateActionSearch(CorporateActionSearch criteria) throws IOException
            {
                return get(Endpoints.CorporateActionSearch, criteria);
            }

        public JSONArray getChartHistorical(String symbol, String range) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put("symbol", symbol);
                params.put("range", range);

                return get(Endpoints.ChartsHistorical, params);
            }

        public JSONObject screener(Screener screener) throws IOException
            {
                return post(Endpoints.ResearchScreener, screener);
            }

        public JSONArray getFundamentalTypes() throws IOException
            {
                return get(Endpoints.ResearchFundamentalTypes);
            }

        public JSONArray getAdrsTop10Defaults() throws IOException
            {
                return get(Endpoints.ResearchAdrsTop10Defaults);
            }

        public JSONArray getAdrs(AdrRequest request) throws IOException
            {
                return get(Endpoints.ResearchAdrs, request);
            }

        public JSONObject getAdrsTop10(AdrRequest request) throws IOException
            {
                return get(Endpoints.ResearchAdrsTop10, request);
            }

        public JSONObject getFundamentals(String type, String symbol) throws IOException
            {
                Map<String, Object> args = new HashMap<>();
                args.put("{type}", type);
                args.put("{symbol}", symbol);

                return get(Endpoints.ResearchFundamentals, args);
            }

        public JSONArray getQuotes(String... symbols) throws IOException
            {
                return get(Endpoints.QuotesEquity, "symbols", String.join(",", symbols));
            }

        public JSONArray quoteSearch(String criteria) throws IOException
            {
                return get(Endpoints.QuotesSearch, "criteria", criteria);
            }

        public JSONArray news(NewsFilter filter) throws IOException
            {
                return news(filter, 0);
            }

        public JSONArray news(NewsFilter filter, int start) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put("filter", filter);
                params.put("start", start);

                return get(Endpoints.ResearchNews, params);
            }

        public JSONArray news(String symbol) throws IOException
            {
                return get(Endpoints.ResearchNewsBySymbol, "{symbol}", symbol);
            }

        public <T> T get(Endpoint endpoint, String name, Object value, Object... others) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put(name, value);

                if (others != null)
                    {
                        if (others.length % 2 != 0)
                            throw new IllegalArgumentException("nnnnnope!");

                        for (int i = 0; i < others.length; i += 2)
                            params.put(others[i].toString(), others[i + 1]);
                    }

                return get(endpoint, params);
            }

        public <T> T get(Endpoint endpoint, String name, Object value) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put(name, value);

                return get(endpoint, params);
            }

        public <T> T get(Endpoint endpoint) throws IOException
            {
                return get(endpoint, new HashMap<>());
            }

        public InputStream getBinary(Endpoint endpoint, Map<String, Object> params) throws IOException
            {
                StringBuilder args = new StringBuilder();
                String path = endpoint.getPath();

                for (Map.Entry<String, Object> entry : params.entrySet())
                    {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        if (key == null)
                            key = "";

                        if (value == null)
                            value = "";

                        if (key.startsWith("{") && key.endsWith("}"))
                            path = path.replace(key, value.toString());
                        else if (value instanceof Collection)
                            {
                                Collection col = (Collection) value;
                                for (Object item : col)
                                    {
                                        args.append(encode(key));
                                        args.append('=');
                                        args.append(encode(item));
                                        args.append('&');
                                    }
                            }
                        else
                            {
                                args.append(encode(key));
                                args.append('=');
                                args.append(encode(value));
                                args.append('&');
                            }
                    }

                String targetUrl = scheme + "://" + hostname + api + path + (args.length() > 0 ? "?" + args : "");
                URL url = new URL(targetUrl);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()));
                con.setRequestProperty("Accept-Encoding", "gzip");
                con.setUseCaches(false);
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);

                if (credentials.getReferer() != null)
                    con.setRequestProperty("Referer", credentials.getReferer());

                int code = con.getResponseCode();
                String content = con.getContentEncoding();

                if (code == 204)
                    return null;

                InputStream stream = (oks.contains(code) ? con.getInputStream() : con.getErrorStream());

                return "gzip".equals(content) ? new GZIPInputStream(stream) : stream;
            }

        public <T> T get(Endpoint endpoint, Map<String, Object> params) throws IOException
            {
                StringBuilder args = new StringBuilder();
                String path = endpoint.getPath();

                for (Map.Entry<String, Object> entry : params.entrySet())
                    {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        if (key == null)
                            key = "";

                        if (value == null)
                            value = "";

                        if (key.startsWith("{") && key.endsWith("}"))
                            path = path.replace(key, value.toString());
                        else if (value instanceof Collection)
                            {
                                Collection col = (Collection) value;
                                for (Object item : col)
                                    {
                                        args.append(encode(key));
                                        args.append('=');
                                        args.append(encode(item));
                                        args.append('&');
                                    }
                            }
                        else
                            {
                                args.append(encode(key));
                                args.append('=');
                                args.append(encode(value));
                                args.append('&');
                            }
                    }

                String targetUrl = scheme + "://" + hostname + api + path + (args.length() > 0 ? "?" + args : "");
                URL url = new URL(targetUrl);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()));
                con.setRequestProperty("Accept-Encoding", "gzip");
                con.setUseCaches(false);
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);

                if (credentials.getReferer() != null)
                    con.setRequestProperty("Referer", credentials.getReferer());

                long start = System.currentTimeMillis();
                try
                    {
                        return read(endpoint, con);
                    }
                finally
                    {
                        System.out.println("Complete in " + (System.currentTimeMillis() - start) / 1000.0);
                    }
            }

        public <T> T post(Endpoint endpoint, JSONObject obj) throws IOException
            {
                return post(endpoint, obj::toString);
            }

        public <T> T post(Endpoint endpoint, JsonConvertable obj) throws IOException
            {
                return post(endpoint, null, obj);
            }

        public <T> T post(Endpoint endpoint, JSONObject obj, String name, Object value, Object... others) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put(name, value);

                if (others != null)
                    {
                        if (others.length % 2 != 0)
                            throw new IllegalArgumentException("?!");

                        for (int i = 0; i < others.length; i += 2)
                            params.put(others[i].toString(), others[i + 1]);
                    }

                return post(endpoint, params, obj::toString);
            }

        public <T> T post(Endpoint endpoint, Map<String, Object> pathParams, JsonConvertable obj) throws IOException
            {
                StringBuilder args = new StringBuilder();
                String path = endpoint.getPath();

                if (pathParams != null)
                    for (Map.Entry<String, Object> entry : pathParams.entrySet())
                        {
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            if (key == null)
                                key = "";

                            if (value == null)
                                value = "";

                            if (key.startsWith("{") && key.endsWith("}"))
                                path = path.replace(key, value.toString());
                        }

                String data = obj.toJSON();
                URL url = new URL(scheme + "://" + hostname + api + path);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()));
                con.setRequestProperty("Content-Length", String.valueOf(data.length()));
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept-Encoding", "gzip");
                con.setRequestMethod("POST");
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                if (credentials.getReferer() != null)
                    con.setRequestProperty("Referer", credentials.getReferer());

                try (Writer out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8)))
                    {
                        out.write(data);
                        out.flush();
                    }

                return read(endpoint, con);
            }

        private String encode(Object o)
            {
                return URLEncoder.encode(o.toString(), StandardCharsets.ISO_8859_1);
            }

        private <T> T read(Endpoint endpoint, HttpURLConnection con) throws IOException
            {
                T response;
                int code = con.getResponseCode();
                String content = con.getContentEncoding();

                if (code == 204)
                    return null;

                try (InputStream stream = (oks.contains(code) ? con.getInputStream() : con.getErrorStream()))
                    {
                        BufferedReader in = new BufferedReader(new InputStreamReader("gzip".equals(content) ? new GZIPInputStream(stream) : stream));
                        JSONTokener tokener = new JSONTokener(in);
                        try
                            {
                                response = (T)(oks.contains(code) ? endpoint.getDatatype().getConstructor(JSONTokener.class).newInstance(tokener) : new JSONObject(tokener));
                            }
                        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
                            {
                                throw new IOException(e);
                            }
                    }

                if (!oks.contains(code))
                    throw new IOException(response.toString());

                return response;
            }
    }
