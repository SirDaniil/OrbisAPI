package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.sd.*;

/**
 * User: Daniil Sosonkin
 * Date: 9/21/2018 2:10 PM
 */
public class AdvisoryApiTest
    {
        public static void main(String[] args) throws IOException
            {
                Properties props = new Properties();
                try (InputStream in = new FileInputStream("advisory.conf"))
                    {
                        props.load(in);
                    }

                String domain = props.getProperty("domain");
                String platformId = props.getProperty("platform.id");
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                OrbisAPI api = new OrbisAPI();
                api.setCredentials(new AvisoryCredentials(domain, platformId, username, password));
                api.setHostname("http://" + domain);

                System.out.println(api.getQuotes("msft").toString(2));
                //System.out.println(api.getQuotes("goog,googl").toString(2));
            }
    }
