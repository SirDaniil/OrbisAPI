package com.github.sd;

import java.io.*;
import java.net.*;
import java.util.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import net.minidev.json.*;

/**
 * Created by Daniil Sosonkin
 * 5/15/2019 9:17 PM
 */
public class C2C implements Credentials
    {
        private String token;
        private String hostname;
        private String secret;
        private String entity;
        private String group;

        public void setHostname(String hostname)
            {
                this.hostname = hostname;
            }

        public void setSecret(String secret)
            {
                this.secret = secret;
            }

        public void setEntity(String entity)
            {
                this.entity = entity;
            }

        public void setGroup(String group)
            {
                this.group = group;
            }

        @Override
        public String getToken()
            {
                if (token == null)
                    try
                        {
                            obtainToken();
                        }
                    catch (JOSEException | IOException e)
                        {
                            throw new IllegalArgumentException(e);
                        }

                return token;
            }

        @Override
        public String getScheme()
            {
                return "C2C";
            }

        private void obtainToken() throws JOSEException, IOException
            {
                JSONObject obj = new JSONObject();
                obj.put("entity", entity);
                obj.put("group", group);
                obj.put("iss", getClass().getName());
                obj.put("jti", UUID.randomUUID().toString());
                obj.put("iat", System.currentTimeMillis() / 1000);
                obj.put("exp", (System.currentTimeMillis() + 1000 * 60) / 1000);

                Payload payload = new Payload(obj);
                JWSSigner signer = new MACSigner(secret);
                JWSObject jws = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), payload);
                jws.sign(signer);

                String jwt = jws.serialize();
                URLConnection con = new URL(hostname + "/c2c/jws.action?jws=" + jwt).openConnection();
                con.setConnectTimeout(1000 * 60);
                con.setReadTimeout(1000 * 60);

                StringBuilder str = new StringBuilder();
                try (InputStream in = con.getInputStream())
                    {
                        byte[] buf = new byte[1024 * 1024];
                        int size;

                        while ((size = in.read(buf)) > 0)
                            str.append(new String(buf, 0, size));
                    }

                org.json.JSONObject response = new org.json.JSONObject(str.toString());
                String token = response.optString("token");
                String message = response.optString("message");
                boolean success = response.getBoolean("success");

                if (!success)
                    throw new IOException("Could not obtain the token: " + message);

                this.token = token;
            }
    }
