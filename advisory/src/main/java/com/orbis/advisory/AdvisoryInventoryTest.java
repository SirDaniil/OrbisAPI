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

                /*EquityOrder order = new EquityOrder();
                order.setQuote(new Quote("IBM"));
                order.setOrderType(OrderType.LIMIT);
                order.setQuantity(2_000);
                order.setLimitPrice(127);
                order.setTransaction(Transaction.SELL);

                AdvisoryEquityOrder request = new AdvisoryEquityOrder();
                request.setInventory(true);
                request.setOrder(order);

                JSONObject resp = api.post(AdvisoryModelPlaceEquity, new JSONObject(request));
                String orderRef = resp.getString("OrderRef");
                System.out.println("Order placed as: " + orderRef);*/
            }
    }
