package com.orbis.advisory;

import java.io.*;
import java.util.*;
import com.github.*;
import com.github.sd.*;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/21/2019 10:28 AM
 */
public class BasketOrderTest
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

                JSONObject account = new JSONObject();
                account.put("accountNumber", "TRKAMAL");

                JSONArray orders = new JSONArray();
                orders.put(createOrder("BUY", "F", 100, OrderType.MARKET, 0, 0));
                orders.put(createOrder("BUY", "TW", 100, OrderType.MARKET, 0, 0));
                orders.put(createOrder("SELL", "GOOG", 1, OrderType.LIMIT, 1200, 0));

                JSONObject basket = new JSONObject();
                basket.put("account", account);
                basket.put("orders", orders);

                AdvisoryApiTest.print( api.post(() -> "/orders/v2/basket/preview/equity", basket) );
                AdvisoryApiTest.print( api.post(() -> "/orders/v2/basket/place/equity", basket) );
            }

        private static JSONObject createOrder(String trx, String ticker, int quantity, OrderType orderType, double limitPx, double stopPx)
            {
                JSONObject quote = new JSONObject();
                quote.put("symbol", ticker);

                JSONObject obj = new JSONObject();
                obj.put("quote", quote);
                obj.put("transaction", trx);
                obj.put("quantity", quantity);
                obj.put("orderType", orderType.toString());

                switch (orderType)
                    {
                        case MARKET:
                            break;

                        case LIMIT:
                            obj.put("limitPrice", limitPx);
                            break;

                        case STOP:
                            obj.put("stopPrice", stopPx);
                            break;

                        case STOP_LIMIT:
                            obj.put("limitPrice", limitPx);
                            obj.put("stopPrice", stopPx);
                            break;
                    }

                return obj;
            }
    }
