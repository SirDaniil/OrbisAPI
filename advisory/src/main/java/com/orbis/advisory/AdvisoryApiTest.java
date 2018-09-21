package com.orbis.advisory;

import java.io.*;
import com.github.sd.*;

/**
 * User: Daniil Sosonkin
 * Date: 9/21/2018 2:10 PM
 */
public class AdvisoryApiTest
    {
        public static void main(String[] args) throws IOException
            {
                String domain = "192.168.110.224";
                String platformId = "10660";
                String username = "daniil.s";

                OrbisAPI api = new OrbisAPI();
                api.setCredentials(new AvisoryCredentials(domain, platformId, username));
                api.setHostname("http://" + domain);

                System.out.println(api.getQuotes("msft").toString(2));
                System.out.println(api.getQuotes("goog,googl").toString(2));
            }
    }
