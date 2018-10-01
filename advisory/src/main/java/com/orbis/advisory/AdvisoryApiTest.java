package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.sd.*;
import org.json.*;

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

                //print(api.get(OrbisAPI.Endpoint.AdvisoryUsers));
                print(api.get(OrbisAPI.Endpoint.AdvisoryUserAccounts, "uid", 59329));
                //System.out.println(api.getQuotes("goog,googl").toString(2));
                /*System.out.println(api.post(OrbisAPI.Endpoint.PasswordChange, () -> {

                    Scanner in = new Scanner(System.in);
                    System.out.print("Password: ");

                    JSONObject object = new JSONObject();
                    object.put("currentPassword", password);
                    object.put("newPassword", in.nextLine());

                    return object.toString();
                }).toString());*/
            }

        static void print(Object o)
            {
                if (o instanceof JSONObject)
                    System.out.println(((JSONObject)o).toString(2));
                else if (o instanceof JSONArray)
                    System.out.println(((JSONArray)o).toString(2));
                else
                    System.out.println(o);
            }
    }
