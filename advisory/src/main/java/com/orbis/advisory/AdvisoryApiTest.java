package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.*;
import com.github.sd.*;
import org.json.*;
import static com.github.sd.OrbisAPI.Endpoint.*;

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

                allocationTest(api);

                //print(api.get(AdvisoryModelArphans));
                //print(api.get(AdvisoryAllocation, "{allocationRef}", "CC19996899"));

                //adjustmentsModify(api);
                //previewAdjustments(api);
                //print(api.get(OrbisAPI.Endpoint.AdvisoryAccountStats));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelAdjustments, "{modelId}", 1));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModels));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelAccounts, "{modelId}", 1, "loadRtb", true, "loadRtbHistory", true));
                //print(api.get(OrbisAPI.Endpoint.UserBalance, "account", "TR001001"));
                //print(api.get(OrbisAPI.Endpoint.UserBuyingPower, "account", "TR001001"));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelPerformance, "{modelId}", 1, "{range}", "1y"));
                //print(api.get(OrbisAPI.Endpoint.AdvisoryModelBalance, "{modelId}", 1));
                //componentUpdate(api);
                //accountNotes(api);
                //rtbHistory(api);
                //rtbModelHistory(api);
                //print(api.post(OrbisAPI.Endpoint.AdvisoryUserNotesAdd, new JSONObject().put("content", "Important notes aren't !important").put("userId", 59329)));
                //checkAllBalances(api);
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

        private static void allocationTest(OrbisAPI api) throws IOException
            {
                long modelId = 1;

                JSONArray adjustments = api.get(OrbisAPI.Endpoint.AdvisoryModelAdjustments, "{modelId}", modelId, "status", "Pending");
                for (int i = 0; i < adjustments.length(); i++)
                    {
                        JSONObject adj = adjustments.getJSONObject(i);
                        System.out.println("Cancelling: " + adj);

                        api.post(OrbisAPI.Endpoint.AdvisoryModelAdjustmentsModify, new JSONObject(new ModelAdjustment().setId(adj.getLong("id"))), "{action}", "Cancel");
                    }

                JSONArray accounts = api.get(AdvisoryModelAccounts, "{modelId}", modelId);
                JSONObject adj = api.post(OrbisAPI.Endpoint.AdvisoryModelAdjustmentsModify, createAdjustment(modelId), "{action}", "Create");
                System.out.println("Created an adjustment: " + adj);

                List<Order> targets = new ArrayList<>();
                for (int i = 0; i < accounts.length(); i++)
                    {
                        JSONObject account = accounts.getJSONObject(i).getJSONObject("account");
                        targets.add(new Order().setQuantity(100).setAccount(new UserAccount().setAccountNumber(account.getString("accountNumber"))));
                    }

                AllocationRequest request = new AllocationRequest();
                request.setAdjustment(new ModelAdjustment().setId(adj.getLong("id")));
                request.setAllocation(new Allocation().setTargets(targets).setTransaction(Transaction.BUY));

                JSONObject rsp = api.post(AdvisoryModelAdjustmentSchedule, new JSONObject(request));
                System.out.println("Allocation scheduled: " + rsp.getString("allocationRef"));

                System.out.println("Cancelling...");
                rsp = api.post(AdvisoryModelAllocationCancel, new JSONObject(new Allocation().setAllocationRef(rsp.getString("allocationRef"))));



                /*System.out.println("Triggering...");

                rsp = api.post(AdvisoryModelAdjustmentTrigger, new JSONObject(new Allocation().setAllocationRef(rsp.getString("allocationRef"))));
                System.out.println("Triggered: " + rsp.getString("allocationRef"));
                print(rsp);*/
            }

        private static JSONObject createAdjustment(long modelId)
            {
                JSONObject quote = new JSONObject();
                quote.put("symbol", "adbe");

                JSONObject obj = new JSONObject();
                obj.put("modelId", modelId);
                obj.put("targetPct", .1);
                obj.put("quote", quote);

                return obj;
            }

        private static void checkAllBalances(OrbisAPI api) throws IOException
            {
                JSONArray users = api.get(OrbisAPI.Endpoint.AdvisoryUsers);
                for (int i = 0; i < users.length(); i++)
                    {
                        JSONObject user = users.getJSONObject(i);
                        print(api.get(OrbisAPI.Endpoint.AdvisoryUserAccounts, "uid", user.getLong("userId"), "loadRtb", true, "loadRtbHistory", true));
                    }
            }

        private static void adjustmentsModify(OrbisAPI api) throws IOException
            {
                JSONObject adj = api.post(OrbisAPI.Endpoint.AdvisoryModelAdjustmentsModify, new JSONObject().put("modelId", 1).put("targetPct", .1).put("symbol", "adbe"), "{action}", "Create");
                System.out.println("AdjustmentID: " + adj.getLong("id") + " (" + adj + ")");
                api.post(OrbisAPI.Endpoint.AdvisoryModelAdjustmentsModify, adj.put("targetPct", .9), "{action}", "Update");
                print(api.post(OrbisAPI.Endpoint.AdvisoryModelAdjustmentsModify, adj, "{action}", "Fetch"));
                api.post(OrbisAPI.Endpoint.AdvisoryModelAdjustmentsModify, adj, "{action}", "Cancel");
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
