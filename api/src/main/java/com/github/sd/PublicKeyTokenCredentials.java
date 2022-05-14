package com.github.sd;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.*;
import java.util.zip.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import net.iharder.Base64;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 3:01 PM
 */
public class PublicKeyTokenCredentials implements Credentials
    {
        private String hostname;
        private String filename;
        private String token;

        public PublicKeyTokenCredentials(Properties props)
            {
                this.filename = props.getProperty("key.file");
                this.hostname = props.getProperty("hostname");
            }

        public PublicKeyTokenCredentials()
            { }

        public PublicKeyTokenCredentials setHostname(String hostname)
            {
                this.hostname = hostname;
                return this;
            }

        public PublicKeyTokenCredentials setFilename(String filename)
            {
                this.filename = filename;
                return this;
            }

        @Override
        public String getToken()
            {
                if (token != null)
                    return token;

                return (token = obtainToken());
            }

        @Override
        public String getScheme()
            {
                return "KeyToken";
            }

        @Override
        public void expired()
            {
                this.token = null;
            }

        private String obtainToken()
            {
                try
                    {
                        String keyId = filename.substring(0, filename.indexOf('.'));
                        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(getPublicKey(filename), RSAPublicKeySpec.class);
                        RSAPublicKey publicRsaKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

                        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                                .issuer("jAPI (" + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version") + " " + System.getProperty("java.version") + ")")
                                .expirationTime(new Date(System.currentTimeMillis() + 10 * 1000))
                                .notBeforeTime(new Date())
                                .issueTime(new Date())
                                .jwtID(UUID.randomUUID().toString())
                                .build();

                        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM)
                                .keyID(keyId)
                                .build();
                        EncryptedJWT jwt = new EncryptedJWT(header, claimSet);

                        RSAEncrypter encrypter = new RSAEncrypter(publicRsaKey);
                        jwt.encrypt(encrypter);

                        String requestToken = jwt.serialize();
                        URL url = new URL(hostname + "/api/auth/v1/token");
                        HttpURLConnection con = (HttpURLConnection)url.openConnection();
                        con.setRequestProperty("Authorization", "Key " + Base64.encodeBytes(requestToken.getBytes()));
                        con.setRequestProperty("Accept-Encoding", "gzip");
                        con.setUseCaches(false);
                        con.setConnectTimeout(1000 * 30);
                        con.setReadTimeout(1000 * 30);

                        int code = con.getResponseCode();
                        String content = con.getContentEncoding();

                        JSONObject response;
                        try (InputStream stream = (code == 200 ? con.getInputStream() : con.getErrorStream()))
                            {
                                BufferedReader in = new BufferedReader(new InputStreamReader("gzip".equals(content) ? new GZIPInputStream(stream) : stream));
                                JSONTokener tokener = new JSONTokener(in);
                                response = new JSONObject(tokener);
                            }

                        if (code != 200)
                            throw new IOException(response.toString());

                        return response.getString("payload");
                    }
                catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
            }

        private static PublicKey getPublicKey(String filename) throws Exception
            {
                if (filename.toLowerCase().endsWith(".pem"))
                    return readPEM(filename);

                File file = new File(filename);
                try (DataInputStream dis = new DataInputStream(new FileInputStream(file)))
                    {
                        byte[] keyBytes = new byte[(int) file.length()];
                        dis.readFully(keyBytes);

                        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                        KeyFactory kf = KeyFactory.getInstance("RSA");

                        return kf.generatePublic(spec);
                    }
            }

        private static PublicKey readPEM(String filename) throws Exception
            {
                File file = new File(filename);
                try (BufferedReader in = new BufferedReader(new FileReader(file)))
                    {
                        StringBuilder buf = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null)
                            buf.append(line);

                        String publicKeyPEM = buf.toString();
                        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
                        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

                        byte[] encoded = Base64.decode(publicKeyPEM);
                        KeyFactory kf = KeyFactory.getInstance("RSA");

                        return kf.generatePublic(new X509EncodedKeySpec(encoded));
                    }
            }

    }
