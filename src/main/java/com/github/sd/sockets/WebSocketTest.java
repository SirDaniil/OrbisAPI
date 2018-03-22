package com.github.sd.sockets;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.bson.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.json.*;
import net.iharder.Base64;

/**
 * User: Daniil Sosonkin
 * Date: 3/20/2018 1:37 PM
 */
public class WebSocketTest extends WebSocketClient
    {
        private BasicBSONDecoder decoder = new BasicBSONDecoder();

        public static void main(String[] args) throws Exception
            {
                Properties props = new Properties();
                try (InputStream in = new FileInputStream("settings.conf"))
                    {
                        props.load(in);
                    }

                JSONObject sub = new JSONObject();
                sub.put("action", "sub");
                sub.put("symbols", new JSONArray(Arrays.asList(props.getProperty("symbols").split(","))));

                String filename = props.getProperty("key.file");
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
                String token = jwt.serialize();

                WebSocketTest ws = new WebSocketTest(new URI(props.getProperty("hostname") + "/stream?auth=" + URLEncoder.encode("Key " + Base64.encodeBytes(token.getBytes()), "ISO-8859-1")));
                ws.connectBlocking();
                ws.send(sub.toString());
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

        private WebSocketTest(URI serverUri)
            {
                super(serverUri);
            }

        @Override
        public void onOpen(ServerHandshake handshakedata)
            {
                System.out.println("(+) Connection opened");
            }

        @Override
        public void onMessage(String message)
            {
                System.out.println("(*) " + message);
            }

        @Override
        public void onClose(int code, String reason, boolean remote)
            {
                System.out.println("(-) Code: " + code + "; Reason: " + reason + "; Remote: " + remote);
            }

        @Override
        public void onError(Exception ex)
            {
                ex.printStackTrace();
            }

        @Override
        public void onMessage(ByteBuffer bytes)
            {
                BSONObject array = decoder.readObject(bytes.array());
                System.out.println(array);
            }
    }
