package com.github.sd;

import net.iharder.*;
import net.iharder.Base64;
import org.java_websocket.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.json.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 2:42 PM
 */
public class OrbisAPI
    {
        private static final Set<Integer> oks = new HashSet<>() {{
            add(200);
            add(201);
        }};
        private LogListener listener = LogListener.Blank;
        private Credentials credentials;
        private String hostname;
        private String api = "/api";
        private String scheme = "https";

        public OrbisAPI setListner(LogListener listener)
            {
                this.listener = listener;
                return this;
            }

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
                var auth_scheme = credentials.getScheme();
                var auth_token = credentials.getToken();

                if (credentials.base64())
                    auth_token = Base64.encodeBytes(auth_token.getBytes(StandardCharsets.ISO_8859_1));

                var path = client.getEndpoint() + "?auth=" + URLEncoder.encode(auth_scheme + " " + auth_token, StandardCharsets.ISO_8859_1);
                var url = credentials.provideUrl(path);
                if (url == null)
                    url = new URL(scheme + "://" + hostname + api);

                var surl = url.toString();
                surl = surl.replace("http://", "ws://");
                surl = surl.replace("https://", "wss://");

                var uri = new URI(surl);
                System.out.println("Connecting to : " + uri);
                System.out.println("Connecting to host : " + uri.getHost());
                System.out.println("Connecting to port : " + uri.getPort());
                System.out.println("Raw path : " + uri.getRawPath());
                System.out.println("Raw query : " + uri.getRawQuery());
                System.out.println(".............................................");

                WebSocketClient ws = new WebSocketClient(uri) {
                    @Override
                    public void onWebsocketHandshakeSentAsClient(WebSocket conn, ClientHandshake request)
                        {
                            System.out.println("(*) Sending handshake: " + request.getResourceDescriptor());
                        }

                    @Override
                    public void onOpen(ServerHandshake handshakedata)
                        {
                            System.out.println("Connected to " + uri);
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

        public JSONObject getUserInfo() throws IOException
            {
                return get(Endpoints.UserInfo);
            }

        public JSONArray getChartIntraday(IntradayRequest request) throws IOException
            {
                return get(Endpoints.ChartsIntraday, request);
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
                        else if (value instanceof Collection<?> col)
                            {
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

                String auth_scheme = credentials.getScheme();
                String auth_token = credentials.getToken();

                if (credentials.base64())
                    auth_token = Base64.encodeBytes(auth_token.getBytes());

                var start = System.currentTimeMillis();
                var url = credentials.provideUrl(path + (args.length() > 0 ? "?" + args : ""));
                if (url == null)
                    url = new URL(scheme + "://" + hostname + api + path + (args.length() > 0 ? "?" + args : ""));

                var con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", auth_scheme + " " + auth_token);
                con.setRequestProperty("Accept-Encoding", "deflate, gzip");
                con.setUseCaches(false);
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);
                listener.sent(System.currentTimeMillis() - start);

                return read(con);
            }

        public <T> T post(Endpoint endpoint, JsonConvertable obj) throws IOException
            {
                String data = obj.toJSON();
                String auth_scheme = credentials.getScheme();
                String auth_token = credentials.getToken();

                if (credentials.base64())
                    auth_token = Base64.encodeBytes(auth_token.getBytes());

                var start = System.currentTimeMillis();
                var url = credentials.provideUrl(endpoint.getPath());
                if (url == null)
                    url = new URL(scheme + "://" + hostname + api + endpoint.getPath());

                var con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Authorization", auth_scheme + " " + auth_token);
                con.setRequestProperty("Content-Length", String.valueOf(data.length()));
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
                        out.write(data);
                        out.flush();
                    }
                finally
                    {
                        listener.sent(System.currentTimeMillis() - start);
                    }

                return read(con);
            }

        private String encode(Object o)
            {
                return URLEncoder.encode(o.toString(), StandardCharsets.ISO_8859_1);
            }

        @SuppressWarnings("unchecked")
        private <T> T read(HttpURLConnection con) throws IOException
            {
                T response = null;
                var start = System.currentTimeMillis();
                int code = con.getResponseCode();
                var encoding = con.getContentEncoding();
                var compressed = false;
                listener.serverResponded(code, System.currentTimeMillis() - start);

                if (code == 204)
                    return null;

                if (code == 401) {
                    credentials.expired();
                    return null;
                }

                start = System.currentTimeMillis();
                int read = 0;
                try (var stream = new CountingInputStream(oks.contains(code) ? con.getInputStream() : con.getErrorStream()))
                    {
                        InputStream in = stream;

                        if ("gzip".equals(encoding)) {
                            compressed = true;
                            in = new GZIPInputStream(in);
                        }

                        if ("deflate".equals(encoding)) {
                            compressed = true;
                            in = new DeflaterInputStream(in);
                        }

                        var reader = new BufferedReader(new InputStreamReader(in));
                        var tokener = new JSONTokener(reader);
                        char ch = tokener.nextClean();
                        tokener.back();

                        if (ch == '[')
                            response = (T)new JSONArray(tokener);
                        else if (ch == '{')
                            response = (T)new JSONObject(tokener);

                        read = stream.getBytes();
                    }
                finally
                    {
                        listener.contentRead(compressed, System.currentTimeMillis() - start, read, encoding);
                    }

                if (!oks.contains(code))
                    throw new IOException(response == null ? "Bad request." : response.toString());

                return response;
            }
    }
