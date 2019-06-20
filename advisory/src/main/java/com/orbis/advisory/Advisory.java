package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.sd.*;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 6/20/2019 12:55 PM
 */
public class Advisory
    {
        private static OrbisAPI api;

        protected static OrbisAPI createAPI() throws IOException
            {
                if (api != null)
                    return api;

                Properties props = new Properties();
                try (InputStream in = new FileInputStream("advisory.conf"))
                    {
                        props.load(in);
                    }

                String domain = props.getProperty("domain");
                String platformId = props.getProperty("platform.id");
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                api = new OrbisAPI();
                api.setCredentials(new AvisoryCredentials(domain, platformId, username, password));
                api.setHostname(domain);

                return api;
            }

        protected static void print(Object obj)
            {
                if (obj instanceof JSONArray)
                    System.out.println(((JSONArray)obj).toString(2));
                else if (obj instanceof JSONObject)
                    System.out.println(((JSONObject)obj).toString(2));
                else
                    System.out.println(obj);
            }
    }
