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
                api.setHostname(domain);

                previewAdjustments(api);
                //print(api.get(OrbisAPI.Endpoint.AdvisoryAccountStats));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelAdjustments, "{modelId}", 1));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModels));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelPerformance, "{modelId}", 1, "{range}", "1y"));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelBalance, "{modelId}", 1));
                //componentUpdate(api);
                //accountNotes(api);
                //rtbHistory(api);
                //rtbModelHistory(api);
                //print(api.post(OrbisAPI.Endpoint.AdvisoryUserNotesAdd, new JSONObject().put("content", "Important notes aren't !important").put("userId", 59329)));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryUsers));
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

        private static void previewAdjustments(OrbisAPI api) throws IOException
            {
                JSONArray list = api.get(OrbisAPI.Endpoint.AdvisoryModelAdjustments, "{modelId}", 1);
                for (int i = 0; i < list.length(); i++)
                    {
                        JSONObject adj = list.getJSONObject(i);
                        print("-----------------------------");
                        print(adj);

                        long adjustmentId = adj.getLong("id");
                        JSONArray orders = api.get(OrbisAPI.Endpoint.AdvisoryModelAdjustmentPreview, "{adjustmentId}", adjustmentId);
                        for (int j = 0; j < orders.length(); j++)
                            {
                                JSONObject order = orders.getJSONObject(j);
                                JSONObject acct = order.getJSONObject("account");
                                JSONObject position = order.getJSONObject("position");
                                System.out.print(acct.getString("accountNumber") + " >> " + order.optString("transType") + " " + order.optDouble("quantity") + " shares of " + order.optString("symbol") + " @ " + order.optDouble("expectedPx") + " (error: " + order.optString("error") + ")");
                                System.out.println(" [" + position.optDouble("value") + " = " + position.optDouble("quantity") + " (" + order.optDouble("currentAllocationPct") + "%) ==> " + order.optDouble("targetAllocationValue") + "]");
                            }
                    }
            }

        private static void componentUpdate(OrbisAPI api) throws IOException
            {
                JSONObject obj = new JSONObject();
                obj.put("model", new JSONObject().put("id", 1));
                obj.put("symbol", "MSFT");
                obj.put("percentage", .2);

                print(api.post(OrbisAPI.Endpoint.AdvisoryModelUpdateComponent, obj));
            }

        static void rtbModelHistory(OrbisAPI api) throws IOException
            {
                Map<String, Object> args = new HashMap<>();
                args.put("{modelId}", "all");
                args.put("page", 0);
                args.put("count", 500);
                print(api.get(OrbisAPI.Endpoint.AdvisoryModelBalanceHistory, args));
            }

        static void rtbHistory(OrbisAPI api) throws IOException
            {
                Map<String, Object> args = new HashMap<>();
                args.put("account", "TRCLIENT3");
                args.put("page", 0);
                args.put("count", 500);
                args.put("dateTo", "10/01/2018");
                print(api.get(OrbisAPI.Endpoint.UserBalancesHistory, args));
            }

        static void accountNotes(OrbisAPI api) throws IOException
            {
                //print(api.get(OrbisAPI.Endpoint.AdvisoryAccountNotes, "aid", 92644));

                print(api.post(OrbisAPI.Endpoint.AdvisoryAccountNotesAdd, new JSONObject().put("content", "Another note: " + new Date()).put("accountId", 92644) ));
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
