package com.orbis.advisory;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.*;
import java.util.zip.*;
import com.github.sd.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import net.iharder.Base64;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 9/21/2018 2:07 PM
 */
public class AvisoryCredentials implements Credentials
    {
        private String domain;
        private String token;
        private String username;
        private String password;
        private String sessionId;

        AvisoryCredentials(String domain, String token, String username, String password)
            {
                int pos = domain.indexOf("//");
                if (pos != -1)
                    domain = domain.substring(pos + 2);

                this.username = username;
                this.password = password;
                this.domain = domain;
                this.token = token;
            }

        @Override
        public String getToken()
            {
                if (sessionId == null)
                    {
                        if (password == null || password.length() == 0)
                            {
                                Scanner in = new Scanner(System.in);
                                System.out.print("Password: ");
                                password = in.nextLine();
                            }

                        try
                            {
                                System.out.println("Logging in...");
                                JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                                        .issuer(token)
                                        .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                                        .notBeforeTime(new Date())
                                        .issueTime(new Date())
                                        .build();

                                SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.ES512).keyID(UUID.randomUUID().toString()).build(), claimSet);
                                PrivateKey privateKey = readPEM(domain + ".pem");
                                jwt.sign(new ECDSASigner((ECPrivateKey) privateKey));
                                String signature = jwt.serialize();

                                JSONObject login = new JSONObject();
                                login.put("username", username);
                                login.put("password", password);
                                login.put("signature", signature);

                                JSONObject resp = post(login);
                                if (resp == null)
                                    throw new IllegalArgumentException("No content");

                                String stage = resp.optString("stage");

                                if (!"ESTABLISHED".equals(stage))
                                    throw new IllegalArgumentException("Session couldn't be established: " + stage);

                                sessionId = resp.optString("sessionId");
                                System.out.println("Logged in as " + username);
                            }
                        catch (Exception e)
                            {
                                throw new IllegalArgumentException(e);
                            }
                    }

                return jwt(sessionId);
            }

        @Override
        public String getScheme()
            {
                return "Session";
            }

        @Override
        public String getReferer()
            {
                return domain;
            }

        private String jwt(String sessionId)
            {
                JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                        .subject(sessionId)
                        .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                        .notBeforeTime(new Date())
                        .issueTime(new Date())
                        .build();
                JWT jwt = new PlainJWT(claimSet);

                return jwt.serialize();
            }

        private PrivateKey readPEM(String filename) throws Exception
            {
                File file = new File(filename);
                try (BufferedReader in = new BufferedReader(new FileReader(file)))
                    {
                        StringBuilder buf = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null)
                            buf.append(line);

                        String privateKeyPem = buf.toString();
                        privateKeyPem = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "");
                        privateKeyPem = privateKeyPem.replace("-----END PRIVATE KEY-----", "");

                        byte[] encoded = Base64.decode(privateKeyPem);
                        KeySpec spec = new PKCS8EncodedKeySpec(encoded);
                        KeyFactory factory = KeyFactory.getInstance("EC");

                        return factory.generatePrivate(spec);
                    }
            }

        private JSONObject post(JSONObject request) throws IOException
            {
                String data = request.toString();
                URL url = new URL("http://" + domain + "/api/auth/v1/login");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Content-Length", String.valueOf(data.length()));
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept-Encoding", "gzip");
                con.setRequestProperty("Referer", domain);
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

                int code = con.getResponseCode();
                String content = con.getContentEncoding();

                try (InputStream stream = OrbisAPI.oks.contains(code) ? con.getInputStream() : con.getErrorStream())
                    {
                        if (stream == null)
                            {
                                if (code == 201)
                                    return null;

                                if (code == 401)
                                    throw new IOException("Not authorized");

                                throw new IOException("No content available");
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

                        return new JSONObject(buf.toString());
                    }
            }
    }
