package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.*;
import com.github.sd.*;
import org.json.*;
import static com.orbis.advisory.AdvisoryEndpoints.*;

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

                print(api.get(UserInfo));

                /*print(api.get(UserPreferences));
                print(api.post(UserPreferencesSet, new JSONObject().put("MY_TEST_KEY", new Date())));
                print(api.get(UserPreferences));
                print(api.post(UserPreferencesDelete, new JSONObject().put("MY_TEST_KEY", "").put("API_KEY", "")));
                print(api.get(UserPreferences));*/
                //allocationTest(api);
                //directAllocationTest(api);

                //costOrdersTest(api);
                //print(api.get(AdvisoryModelArphans));
                //print(api.get(AdvisoryAllocation, "{allocationRef}", "CC19996899"));

                //adjustmentsModify(api);
                //previewAdjustments(api);
                //print(api.get(AdvisoryAccountStats));
                //print(api.get(AdvisoryModelAdjustments, "{modelId}", 1));
                //print(api.get(AdvisoryModels));
                //print(api.get(AdvisoryModelAccounts, "{modelId}", 1, "loadRtb", true, "loadRtbHistory", true));
                //print(api.get(UserBalance, "account", "TR001001"));
                //print(api.get(UserBuyingPower, "account", "TR001001"));
                //print(api.get(AdvisoryModelPerformance, "{modelId}", 1, "{range}", "1y"));
                //print(api.get(AdvisoryModelBalance, "{modelId}", 1));
                //componentUpdate(api);
                //accountNotes(api);
                //rtbHistory(api);
                //rtbModelHistory(api);
                //print(api.post(AdvisoryUserNotesAdd, new JSONObject().put("content", "Important notes aren't !important").put("userId", 59329)));
                //checkAllBalances(api);
                //System.out.println(api.getQuotes("c+a").toString(2));
                /*System.out.println(api.post(PasswordChange, () -> {

                    Scanner in = new Scanner(System.in);
                    System.out.print("Password: ");

                    JSONObject object = new JSONObject();
                    object.put("currentPassword", password);
                    object.put("newPassword", in.nextLine());

                    return object.toString();
                }).toString());*/
            }

        private static void directAllocationTest(OrbisAPI api) throws IOException
            {
                long modelId = 1;
                long ordeQty = 10000;

                EquityOrder order = new EquityOrder();
                order.setQuote(new Quote("ADBE"));
                order.setOrderType(OrderType.MARKET);
                order.setQuantity(ordeQty);
                order.setTransaction(Transaction.SELL);

                ModelEquityOrder request = new ModelEquityOrder();
                request.setModel(new PortfolioModel().setId(modelId));
                request.setOrder(order);

                JSONObject resp = api.post(AdvisoryModelPlaceEquity, new JSONObject(request));
                String orderRef = resp.getString("OrderRef");
                System.out.println("Order placed as: " + orderRef);

                boolean filled = false;
                while (!filled)
                    {
                        JSONArray list = api.get(AdvisoryModelOrders, "{modelId}", modelId, "{type}", "single", "orderRef", orderRef);
                        if (list.length() == 0)
                            throw new IllegalArgumentException("Order " + orderRef + " wasn't found");

                        JSONObject obj = list.getJSONObject(0);
                        String status = obj.getString("translatedStatus");
                        if (status == null)
                            throw new IllegalArgumentException("Missing status: " + obj);

                        switch (status)
                            {
                                case "R":
                                    throw new IOException("Order rejected");

                                case "E":
                                    System.out.println("Filled!");
                                    filled = true;
                                    break;
                            }
                    }

                JSONArray accounts = api.get(AdvisoryModelAccounts, "{modelId}", modelId);
                List<Order> targets = new ArrayList<>();
                List<Order> sources = new ArrayList<>();
                Order source = new Order();
                source.setOurRef(orderRef);
                source.setQuantity(ordeQty);
                sources.add(source);

                for (int i = 0; i < accounts.length(); i++)
                    {
                        JSONObject account = accounts.getJSONObject(i).getJSONObject("account");
                        targets.add(
                                new Order()
                                        .setQuantity(ordeQty / account.length())
                                        .setAccount(new UserAccount().setAccountNumber(account.getString("accountNumber")))
                        );
                    }

                Allocation allocation = new Allocation();
                allocation.setTargets(targets);
                allocation.setOrders(sources);

                resp = api.post(AdvisoryModelAllocate, new JSONObject(allocation));
                System.out.println("Allocated: " + resp);
            }

        private static void costOrdersTest(OrbisAPI api) throws IOException
            {
                String account = "TRCLIENT1";
                JSONArray portfolio = api.get(UserPortfolio, "account", account);
                for (int i = 0; i < portfolio.length(); i++)
                    {
                        JSONObject entry = portfolio.getJSONObject(i);
                        String symbol = entry.getString("symbol");

                        JSONArray orders = api.get(OrdersCost, "symbol", symbol, "account", account, "loadQuotes", true);
                        System.out.println("=== " + symbol + " ===");
                        for (int j = 0; j < orders.length(); j++)
                            {
                                JSONObject obj = orders.getJSONObject(j);

                                if (j == 0)
                                    {
                                        JSONObject quote = obj.getJSONObject("quote");
                                        System.out.println("\tLastPx: " + quote.getDouble("lastPrice"));
                                    }

                                System.out.println("\t" + obj.getString("ourRef") + " - " + obj.getDouble("execPx") + " (" + obj.get("execTime") + ")");
                            }
                    }
            }

        private static void allocationTest(OrbisAPI api) throws IOException
            {
                long modelId = 1;

                JSONArray adjustments = api.get(AdvisoryModelAdjustments, "{modelId}", modelId, "status", "Pending");
                for (int i = 0; i < adjustments.length(); i++)
                    {
                        JSONObject adj = adjustments.getJSONObject(i);
                        System.out.println("Cancelling: " + adj);

                        api.post(AdvisoryModelAdjustmentsModify, new JSONObject(new ModelAdjustment().setId(adj.getLong("id"))), "{action}", "Cancel");
                    }

                JSONArray accounts = api.get(AdvisoryModelAccounts, "{modelId}", modelId);
                JSONObject adj = api.post(AdvisoryModelAdjustmentsModify, createAdjustment(modelId), "{action}", "Create");
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

                /*System.out.println("Cancelling...");
                rsp = api.post(AdvisoryModelAllocationCancel, new JSONObject(new Allocation().setAllocationRef(rsp.getString("allocationRef"))));*/



                System.out.println("Triggering...");

                rsp = api.post(AdvisoryModelAdjustmentTrigger, new JSONObject(new Allocation().setAllocationRef(rsp.getString("allocationRef"))));
                System.out.println("Triggered: " + rsp.getString("allocationRef"));
                print(rsp);
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
                JSONArray users = api.get(AdvisoryUsers);
                for (int i = 0; i < users.length(); i++)
                    {
                        JSONObject user = users.getJSONObject(i);
                        print(api.get(AdvisoryUserAccounts, "uid", user.getLong("userId"), "loadRtb", true, "loadRtbHistory", true));
                    }
            }

        private static void adjustmentsModify(OrbisAPI api) throws IOException
            {
                JSONObject adj = api.post(AdvisoryModelAdjustmentsModify, new JSONObject().put("modelId", 1).put("targetPct", .1).put("symbol", "adbe"), "{action}", "Create");
                System.out.println("AdjustmentID: " + adj.getLong("id") + " (" + adj + ")");
                api.post(AdvisoryModelAdjustmentsModify, adj.put("targetPct", .9), "{action}", "Update");
                print(api.post(AdvisoryModelAdjustmentsModify, adj, "{action}", "Fetch"));
                api.post(AdvisoryModelAdjustmentsModify, adj, "{action}", "Cancel");
            }

        private static void previewAdjustments(OrbisAPI api) throws IOException
            {
                JSONArray list = api.get(AdvisoryModelAdjustments, "{modelId}", 1);
                for (int i = 0; i < list.length(); i++)
                    {
                        JSONObject adj = list.getJSONObject(i);
                        print("-----------------------------");
                        print(adj);

                        long adjustmentId = adj.getLong("id");
                        JSONArray orders = api.get(AdvisoryModelAdjustmentPreview, "{adjustmentId}", adjustmentId);
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

                print(api.post(AdvisoryModelUpdateComponent, obj));
            }

        static void rtbModelHistory(OrbisAPI api) throws IOException
            {
                Map<String, Object> args = new HashMap<>();
                args.put("{modelId}", "all");
                args.put("page", 0);
                args.put("count", 500);
                print(api.get(AdvisoryModelBalanceHistory, args));
            }

        static void rtbHistory(OrbisAPI api) throws IOException
            {
                Map<String, Object> args = new HashMap<>();
                args.put("account", "TRCLIENT3");
                args.put("page", 0);
                args.put("count", 500);
                args.put("dateTo", "10/01/2018");
                print(api.get(UserBalancesHistory, args));
            }

        static void accountNotes(OrbisAPI api) throws IOException
            {
                //print(api.get(AdvisoryAccountNotes, "aid", 92644));

                print(api.post(AdvisoryAccountNotesAdd, new JSONObject().put("content", "Another note: " + new Date()).put("accountId", 92644) ));
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
