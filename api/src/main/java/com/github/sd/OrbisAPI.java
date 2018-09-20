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
        public enum Endpoint {
            QuotesEquity("/quotes/equity", JSONArray.class),
            QuotesSearch("/quotes/search", JSONArray.class),
            ChartsIntraday("/quotes/equity/intraday", JSONArray.class),
            ChartsHistorical("/quotes/equity/historical", JSONArray.class),
            Research("/research/{symbol}", JSONObject.class),
            ResearchAdrs("/research/adrs", JSONArray.class),
            ResearchAdrsTop10("/research/adrs/top10", JSONObject.class),
            ResearchAdrsTop10Defaults("/research/adrs/top10/defaults", JSONArray.class),
            ResearchNews("/research/news", JSONArray.class),
            ResearchNewsBySymbol("/research/news/ticker/{symbol}", JSONArray.class),
            ResearchFundamentalTypes("/research/fundamentals/types", JSONArray.class),
            ResearchFundamentals("/research/fundamentals/{type}/{symbol}", JSONObject.class),
            ResearchScreener("/research/screener", JSONObject.class),
            CorporateActionTypes("/research/actions/types", JSONArray.class),
            CorporateActionSearch("/research/actions/search", JSONArray.class),
            TipranksLivefeed("/research/tipranks/livefeed", JSONArray.class),

            OmsQuotes("/quotes/equity", JSONObject.class)
            ;
            private String path;
            private Class clazz;

            Endpoint(String path, Class clazz)
                {
                    this.path = path;
                    this.clazz = clazz;
                }
        }
        private static final Set<Integer> oks = new HashSet<>() {{
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
                return get(Endpoint.ChartsIntraday, "symbol", symbol);
            }

        public JSONArray getCorporateActionTypes() throws IOException
            {
                return get(Endpoint.CorporateActionTypes);
            }

        public JSONArray corporateActionSearch(CorporateActionSearch criteria) throws IOException
            {
                return get(Endpoint.CorporateActionSearch, criteria);
            }

        public JSONArray getChartHistorical(String symbol, String range) throws IOException
            {
                Map<String, Object> params = new HashMap<>();
                params.put("symbol", symbol);
                params.put("range", range);

                return get(Endpoint.ChartsHistorical, params);
            }

        public JSONObject screener(Screener screener) throws IOException
            {
                return post(Endpoint.ResearchScreener, screener);
            }

        public JSONArray getFundamentalTypes() throws IOException
            {
                return get(Endpoint.ResearchFundamentalTypes);
            }

        public JSONArray getAdrsTop10Defaults() throws IOException
            {
                return get(Endpoint.ResearchAdrsTop10Defaults);
            }

        public JSONArray getAdrs(AdrRequest request) throws IOException
            {
                return get(Endpoint.ResearchAdrs, request);
            }

        public JSONObject getAdrsTop10(AdrRequest request) throws IOException
            {
                return get(Endpoint.ResearchAdrsTop10, request);
            }

        public JSONObject getFundamentals(String type, String symbol) throws IOException
            {
                Map<String, Object> args = new HashMap<>();
                args.put("{type}", type);
                args.put("{symbol}", symbol);

                return get(Endpoint.ResearchFundamentals, args);
            }

        public JSONArray getQuotes(String... symbols) throws IOException
            {
                return get(Endpoint.QuotesEquity, "symbols", String.join(",", symbols));
            }

        public JSONArray quoteSearch(String criteria) throws IOException
            {
                return get(Endpoint.QuotesSearch, "criteria", criteria);
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

                return get(Endpoint.ResearchNews, params);
            }

        public JSONArray news(String symbol) throws IOException
            {
                return get(Endpoint.ResearchNewsBySymbol, "{symbol}", symbol);
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

        public <T> T get(Endpoint endpoint, Map<String, Object> params) throws IOException
            {
                StringBuilder args = new StringBuilder();
                String path = endpoint.path;

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

                URL url = new URL(scheme + "://" + hostname + api + path + (args.length() > 0 ? "?" + args : ""));
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", credentials.getScheme() + " " + Base64.encodeBytes(credentials.getToken().getBytes()));
                con.setRequestProperty("Accept-Encoding", "gzip");
                con.setUseCaches(false);
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);

                return read(endpoint, con);
            }

        private <T> T post(Endpoint endpoint, JsonConvertable obj) throws IOException
            {
                String data = obj.toJSON();
                URL url = new URL(scheme + "://" + hostname + api + endpoint.path);
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

                if (code == 401)
                    throw new IOException("Access denied");

                try (InputStream stream = (oks.contains(code) ? con.getInputStream() : con.getErrorStream()))
                    {
                        if (stream == null)
                            throw new IOException("Return code: " + code);

                        BufferedReader in = new BufferedReader(new InputStreamReader("gzip".equals(content) ? new GZIPInputStream(stream) : stream));
                        JSONTokener tokener = new JSONTokener(in);
                        try
                            {
                                response = (T)(oks.contains(code) ? endpoint.clazz.getConstructor(JSONTokener.class).newInstance(tokener) : new JSONObject(tokener));
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
