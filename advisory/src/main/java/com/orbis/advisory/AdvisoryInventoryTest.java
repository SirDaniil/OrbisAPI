package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.*;
import com.github.sd.*;
import org.json.*;
import static com.orbis.advisory.AdvisoryEndpoints.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/29/2019 12:46 PM
 */
public class AdvisoryInventoryTest
    {
        public static void main(String[] args) throws Exception
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

                System.out.println(api.get(BranchInventories).toString());
                List<EquityOrder> orders = new ArrayList<>();

                for (int i = 0; i < 2; i++)
                    {
                        EquityOrder order = new EquityOrder();
                        order.setQuote(new Quote("F"));
                        order.setOrderType(OrderType.MARKET);
                        order.setQuantity(500 + i);
                        order.setTransaction(Transaction.BUY);

                        AdvisoryEquityOrder request = new AdvisoryEquityOrder();
                        request.setInventory(true);
                        request.setOrder(order);

                        JSONObject resp = api.post(AdvisoryModelPlaceEquity, new JSONObject(request));
                        String orderRef = resp.getString("OrderRef");
                        System.out.println("Order placed as: " + orderRef);
                        order.setOrderRef(orderRef);
                        orders.add(order);

                        waitForFill(api, orderRef);
                    }

                List<Order> targets = new ArrayList<>();
                List<Order> sources = new ArrayList<>();
                double totalQty = 0;

                for (EquityOrder order : orders)
                    {
                        Order source = new Order();
                        source.setOurRef(order.getOrderRef());
                        source.setQuantity(order.getQuantity());
                        sources.add(source);
                        totalQty += order.getQuantity();
                    }

                targets.add(new Order().setQuantity(totalQty / 2.0).setAccount(new UserAccount().setAccountNumber("TRCLIENT1")));
                targets.add(new Order().setQuantity(totalQty / 2.0).setAccount(new UserAccount().setAccountNumber("TRCLIENT2")));

                Allocation allocation = new Allocation();
                allocation.setTargets(targets);
                allocation.setOrders(sources);

                JSONObject resp = api.post(AdvisoryModelAllocate, new JSONObject(allocation));
                System.out.println("Allocated: " + resp);
            }

        private static void waitForFill(OrbisAPI api, String orderRef) throws IOException, InterruptedException
            {
                double remQty;
                String status;

                do
                    {
                        Thread.sleep(1000);
                        JSONObject result = api.get(OrdersStatus, "{orderRef}", orderRef);
                        remQty = result.getDouble("remainingQty");
                        status = result.getString("translatedStatus");

                    } while (remQty > 0 && !"E".equals(status));
            }
    }
