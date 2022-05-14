package com.github.sd;

import java.io.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import net.iharder.Base64;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 3:01 PM
 */
public class PublicKeyCredentials implements Credentials
    {
        private String filename;

        public PublicKeyCredentials(String filename)
            {
                this.filename = filename;
            }

        @Override
        public String getToken()
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

                        return jwt.serialize();
                    }
                catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
            }

        @Override
        public String getScheme()
            {
                return "Key";
            }

        @Override
        public void expired()
            { }

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
