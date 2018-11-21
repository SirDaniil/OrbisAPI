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
        @SuppressWarnings("unused")
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
            PasswordChange("/auth/v1/password/change", JSONObject.class),
            DeviceRegistration("/user/device/register", JSONObject.class),
            UserInfo("/user/info", JSONObject.class),
            AdvisoryUsers("/v2/advisory/clients/list", JSONArray.class),
            AdvisoryUserAccounts("/v2/advisory/clients/accounts", JSONArray.class),
            AdvisoryUserNotes("/v2/advisory/clients/user/notes", JSONArray.class),
            AdvisoryUserNotesAdd("/v2/advisory/clients/user/notes/add", JSONObject.class),
            AdvisoryAccountStats("/v2/advisory/clients/accounts/stats", JSONObject.class),
            AdvisoryAccountNotes("/v2/advisory/clients/account/notes", JSONArray.class),
            AdvisoryAccountNotesAdd("/v2/advisory/clients/account/notes/add", JSONObject.class),
            AdvisoryModelUpdateComponent("/v2/advisory/model/component/update", JSONObject.class),

            AdvisoryModelAdjustments       ("/v2/advisory/model/adjustments/{modelId}", JSONArray.class),
            AdvisoryModelAdjustmentsModify ("/v2/advisory/model/adjustments/modify/{action}", JSONObject.class),
            AdvisoryModelAdjustmentPreview ("/v2/advisory/model/adjustments/preview/{adjustmentId}", JSONArray.class),
            AdvisoryModelAdjustmentSchedule("/v2/advisory/model/adjustments/preallocate", JSONObject.class),
            AdvisoryModelAdjustmentTrigger ("/v2/advisory/model/adjustments/allocation/trigger", JSONObject.class),
            AdvisoryModelAllocationCancel  ("/v2/advisory/model/adjustments/allocation/cancel", JSONObject.class),
            AdvisoryModelAllocate          ("/v2/advisory/model/orders/allocate", JSONObject.class),

            AdvisoryModelPlaceEquity("/orders/v2/advisory/equity/place", JSONObject.class),

            AdvisoryModelPerformance("/v2/advisory/analytics/model/performance/{modelId}/{range}", JSONArray.class),
            AdvisoryModelBalance("/v2/advisory/model/rtb/{modelId}", JSONObject.class),
            AdvisoryModelBalanceHistory("/v2/advisory/model/rtb/history/{modelId}", JSONArray.class),
            AdvisoryModels("/v2/advisory/models", JSONArray.class),
            AdvisoryModelOrders("/orders/model/{modelId}/{type}", JSONArray.class),
            AdvisoryModelAccountStats("/v2/advisory/model/accounts/stats/{modelId}", JSONObject.class),
            AdvisoryModelAccounts("/v2/advisory/model/accounts/{modelId}", JSONArray.class),
            AdvisoryModelArphans("/v2/advisory/model/accounts/orphaned", JSONArray.class),
            AdvisoryAllocation("/v2/advisory/allocations/{allocationRef}", JSONArray.class),
            UserBalancesHistory("/user/rtb/history", JSONArray.class),
            UserBuyingPower("/user/balance", JSONObject.class),
            UserBalance("/user/rtb", JSONObject.class),
            UserPortfolio("/user/portfolio", JSONArray.class),
            UserPreferences("/user/preferences", JSONObject.class),
            UserPreferencesSet("/user/preferences/set", JSONObject.class),
            UserPreferencesDelete("/user/preferences/delete", JSONObject.class),

            OrdersCost("/orders/cost", JSONArray.class),
            OrdersStatus("/orders/status/{orderRef}", JSONObject.class),
            ;
            private String path;
            private Class clazz;

            Endpoint(String path, Class clazz)
                {
                    this.path = path;
                    this.clazz = clazz;
                }
        }
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

                if (credentials.getReferer() != null)
                    con.setRequestProperty("Referer", credentials.getReferer());

                return read(endpoint, con);
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
                String path = endpoint.path;

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
                System.out.println("posting: " + data);
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
