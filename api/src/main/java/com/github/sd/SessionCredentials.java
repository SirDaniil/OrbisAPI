package com.github.sd;

import com.github.*;
import com.nimbusds.jwt.*;
import org.json.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

public class SessionCredentials implements Credentials
    {
        private String hostname;
        private String username;
        private String password;

        @Override
        public URL provideUrl(String path) throws MalformedURLException
            {
                return new URL(hostname + path);
            }

        public void setHostname(String hostname)
            {
                this.hostname = hostname;
            }

        public void setUsername(String username)
            {
                this.username = username;
            }

        public void setPassword(String password)
            {
                this.password = password;
            }

        @Override
        public boolean base64()
            {
                return false;
            }

        @Override
        public String getToken()
            {
                return getSessionId();
            }

        @Override
        public String getScheme()
            {
                return "SessionId";
            }

        @Override
        public void expired()
            {

            }

        private String getSessionId()
            {
                try
                    {
                        return getSessionId1();
                    }
                catch (ParseException | IOException e)
                    {
                        e.printStackTrace();
                    }

                return null;
            }

        private String getSessionId1() throws ParseException, IOException
            {
                String sessionId = null;
                var dir = System.getProperty("user.home");
                var filename = dir + File.separator + ".sessionId-" + Utils.md5(hostname) + ".txt";
                var file = new File(filename);

                // Check whether session id is in the file
                if (file.exists() && file.canRead())
                    try (InputStream in = new FileInputStream(file))
                        {
                            StringBuilder buf = new StringBuilder();
                            byte[] bytes = new byte[1024 * 65];
                            int size;

                            while ((size = in.read(bytes)) > 0)
                                buf.append(new String(bytes, 0, size));

                            sessionId = (buf.length() > 0 ? buf.toString() : null);
                        }
                    catch (IOException e)
                        {
                            System.out.println("[IGNORING] Failed to read session id from " + filename);
                            e.printStackTrace();
                        }

                if (hostname == null && username == null)
                    {
                        Data data = checkHiddenFile();
                        if (data != null) {
                            hostname = data.hostname;
                            username = data.username;
                            password = data.password;
                        }
                    }

                if (sessionId != null)
                    {
                        SignedJWT jwt = SignedJWT.parse(sessionId);
                        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
                        Date expiresAt = claimsSet.getExpirationTime();

                        if (expiresAt != null && expiresAt.getTime() < System.currentTimeMillis())
                            sessionId = null;
                    }

                if (sessionId != null) {
                    System.out.println("Using token: " + sessionId);
                    return sessionId;
                }

                JSONObject post = new JSONObject();
                post.put("username", username);
                post.put("password", password);

                String rsp = post("/auth/v2/login", post);
                JSONObject obj = new JSONObject(rsp);
                String stage = obj.optString("stage");
                String type = obj.optString("twoFactorType");
                sessionId = obj.getString("sessionId");

                if ("Pending2FA".equals(stage))
                    {
                        String code;
                        do {
                            if ("SMS".equals(type))
                                get("/auth/2fa/v1/resend", sessionId);

                            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                            System.out.print("2fa code (" + type + "): ");
                            code = in.readLine();
                        } while ("resend".equals(code));

                        post = new JSONObject();
                        post.put("code", code);

                        rsp = post("/auth/v2/2fa/verify", post, sessionId);
                        obj = new JSONObject(rsp);
                        stage = obj.getString("stage");

                        if (!"ESTABLISHED".equals(stage))
                            throw new IOException("2fa failed. Expecting `ESTABLISHED` but got: " + stage);
                    }
                else if ("RequiredPasswordChange".equals(stage))
                    {
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("New password: ");
                        String pwd = in.readLine();

                        post = new JSONObject();
                        post.put("currentPassword", password);
                        post.put("newPassword", pwd);

                        rsp = post("/auth/v1/password/set", post, sessionId);
                        obj = new JSONObject(rsp);
                        stage = obj.getString("stage");

                        if (!"ESTABLISHED".equals(stage))
                            throw new IOException("2fa failed. Expecting `ESTABLISHED` but got: " + stage);
                    }
                else if (!"ESTABLISHED".equals(stage))
                    {
                        System.out.println("Unhandled stage: " + stage);
                        System.exit(1);
                    }

                try (PrintWriter out = new PrintWriter(file))
                    {
                        out.print(sessionId);
                        out.flush();
                    }

                return sessionId;
            }

        private static Data checkHiddenFile()
            {
                String dir = System.getProperty("user.home");
                File file = new File(dir + File.separator + ".wp");

                if (!file.exists())
                    return null;

                try (InputStream in = new FileInputStream(file))
                    {
                        Properties props = new Properties();
                        props.load(in);

                        return new Data(props.getProperty("hostname"), props.getProperty("username"), props.getProperty("password"));
                    }
                catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                return null;
            }

        private String post(String endpoint, JSONObject post) throws IOException
            {
                return post(endpoint, post, null);
            }

        private String post(String endpoint, JSONObject post, String sessionId, Object... args) throws IOException
            {
                if (args != null)
                    for (int i = 0; i < args.length; i += 2)
                        {
                            String name = args[i].toString();
                            String value = (i + 1 < args.length ? args[i + 1] : "").toString();

                            if (!name.startsWith("{"))
                                throw new IOException("Only path arguments supported.");

                            endpoint = endpoint.replace(name, value);
                        }

                String data = post.toString();
                URL url = new URL(String.format("%s%s", hostname, endpoint));
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Content-Length", String.valueOf(data.length()));
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept-Encoding", "gzip");
                con.setRequestMethod("POST");
                con.setConnectTimeout(1000 * 30);
                con.setReadTimeout(1000 * 30);
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                if (sessionId != null)
                    con.setRequestProperty("Authorization", "SessionId " + sessionId);

                System.out.println("[" + url + "] Posting: " + data);
                try (Writer out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8)))
                    {
                        out.write(data);
                        out.flush();
                    }

                int code = con.getResponseCode();
                String content = con.getContentEncoding();
                System.out.println("Response code: " + code);

                try (InputStream stream = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream())
                    {
                        if (stream == null)
                            {
                                if (code == 201)
                                    throw new IOException("No data");

                                if (code == 401)
                                    throw new IOException("Not authorized");

                                throw new IOException("No content available. Code=" + code);
                            }

                        BufferedReader in = new BufferedReader(new InputStreamReader("gzip".equals(content) ? new GZIPInputStream(stream) : stream));
                        StringBuilder buf = new StringBuilder();
                        String line;

                        while ((line = in.readLine()) != null)
                            buf.append(line);

                        System.out.println("Response: " + buf);
                        if (code == 401)
                            {
                                JSONObject obj = new JSONObject(buf.toString());
                                String error = obj.getString("error");
                                String id = obj.optString("id");

                                throw new IOException(error + " (ErrorID: " + id + ")");
                            }

                        return buf.toString();
                    }
            }

        private String get(String endpoint, String sessionId, Object... args) throws IOException
            {
                if (args != null)
                    for (int i = 0, pos = 0; i < args.length; i += 2)
                        {
                            String name = args[i].toString();
                            String value = (i + 1 < args.length ? args[i + 1] : "").toString();

                            if (name.startsWith("{"))
                                endpoint = endpoint.replace(name, value);
                            else
                                endpoint += (pos++ == 0 ? "?" : "&") + encode(name) + "=" + encode(value);
                        }

                URL url = new URL(String.format("%s%s", hostname, endpoint));
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.addRequestProperty("Authorization", "SessionId " + sessionId);

                System.out.println("-> " + url + " <-");
                int code = con.getResponseCode();
                String content = con.getContentEncoding();
                System.out.println("Response code: " + code);

                try (InputStream stream = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream())
                    {
                        if (stream == null)
                            {
                                if (code == 201)
                                    throw new IOException("No data");

                                if (code == 401)
                                    throw new IOException("Not authorized");

                                throw new IOException("No content available. Code=" + code);
                            }

                        BufferedReader in = new BufferedReader(new InputStreamReader("gzip".equals(content) ? new GZIPInputStream(stream) : stream));
                        StringBuilder buf = new StringBuilder();
                        String line;

                        while ((line = in.readLine()) != null)
                            buf.append(line);

                        if (code == 401)
                            {
                                JSONObject obj = new JSONObject(buf.toString());
                                String error = obj.getString("error");
                                String id = obj.optString("id");

                                throw new IOException(error + " (ErrorID: " + id + ")");
                            }

                        System.out.println(buf);
                        return buf.toString();
                    }
            }

        private String encode(Object value)
            {
                if (value == null)
                    return "";

                return URLEncoder.encode(value.toString(), StandardCharsets.UTF_8);
            }

        private record Data(String hostname, String username, String password)
            { }
    }
