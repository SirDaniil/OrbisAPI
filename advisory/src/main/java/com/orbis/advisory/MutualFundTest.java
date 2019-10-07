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
public class MutualFundTest
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

                // Simple buy
                var order = new MutualFundOrder();
                order.setQuote(new Quote("FCPEX"));
                order.setValue(1000);
                order.setTransaction(Transaction.BUY);

                var request = new AccountOrder<MutualFundOrder>();
                request.setAccount(new UserAccount().setAccountNumber("TRCLIENT1"));
                request.setOrder(order);

                JSONObject resp = api.post(MutualFundsPlace, new JSONObject(request));
                String orderRef = resp.getString("OrderRef");
                System.out.println("Order placed as: " + orderRef);
                order.setOrderRef(orderRef);

                JSONObject jo = new JSONObject();
                jo.put("orderRef", order.getOrderRef());

                /*JSONObject cancel = new JSONObject();
                cancel.put("order", jo);
                System.out.println("Order cancellation: " + api.post(OrderCancel, cancel).toString());*/
            }
    }
