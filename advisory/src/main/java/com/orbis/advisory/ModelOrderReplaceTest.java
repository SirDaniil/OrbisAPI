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
public class ModelOrderReplaceTest extends Advisory
    {
        public static void main(String[] args) throws Exception
            {
                OrbisAPI api = createAPI();

                EquityOrder order = new EquityOrder();
                order.setQuote(new Quote("IBM"));
                order.setOrderType(OrderType.LIMIT);
                order.setQuantity(2_000);
                order.setLimitPrice(1);
                order.setTransaction(Transaction.BUY);

                // Submit the order
                AdvisoryEquityOrder request = new AdvisoryEquityOrder();
                request.setModel(new PortfolioModel().setId(1));
                request.setOrder(order);

                JSONObject resp = api.post(AdvisoryEquityPlace, new JSONObject(request));
                order.setOrderRef(resp.getString("OrderRef"));
                System.out.println("Order placed as: " + order.getOrderRef());

                waitForAccept(api, order.getOrderRef());

                // Replace the order
                order.setLimitPrice(1.01);

                JSONObject replace = new JSONObject();
                replace.put("order", new JSONObject(order));
                System.out.println(api.post(OrderReplace, replace).toString());
                waitForAccept(api, order.getOrderRef());

                // Cancel the order
                JSONObject cancel = new JSONObject();
                cancel.put("order", new JSONObject(order));
                System.out.println(api.post(OrderCancel, cancel).toString());

            }

        private static void waitForAccept(OrbisAPI api, String orderRef) throws IOException, InterruptedException
            {
                String status;
                do
                    {
                        Thread.sleep(1000);
                        JSONObject result = api.get(OrdersStatus, "{orderRef}", orderRef);
                        status = result.getString("translatedStatus");

                    } while (!"W".equals(status));
            }
    }
