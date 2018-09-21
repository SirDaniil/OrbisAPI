package com.github.sd;

import java.text.*;
import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 9/21/2018 11:33 AM
 */
public class CorporateActionsSearch extends HashMap<String, Object>
    {
        private final DateFormat mmddyyyy = new SimpleDateFormat("MM/dd/yyyy");

        public CorporateActionsSearch symbol(String symbol)
            {
                if (symbol == null || (symbol = symbol.trim().toUpperCase()).length() == 0)
                    remove("symbol");
                else
                    put("symbol", symbol);

                return this;
            }

        public CorporateActionsSearch cusip(String cusip)
            {
                if (cusip == null || (cusip = cusip.trim().toUpperCase()).length() == 0)
                    remove("cusip");
                else
                    put("cusip", cusip);

                return this;
            }

        public CorporateActionsSearch from(Date date)
            {
                put("dateFrom", mmddyyyy.format(date));
                return this;
            }

        public CorporateActionsSearch to(Date date)
            {
                put("dateTo", mmddyyyy.format(date));
                return this;
            }

        public CorporateActionsSearch type(String type)
            {
                put("type", type);
                return this;
            }
    }
