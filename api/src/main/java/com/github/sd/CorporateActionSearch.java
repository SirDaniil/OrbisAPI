package com.github.sd;

import java.util.*;

/**
 * Created by Daniil Sosonkin
 * 7/25/2018 8:26 PM
 */
public class CorporateActionSearch extends HashMap<String, Object>
    {
        public static CorporateActionSearch Builder()
            {
                return new CorporateActionSearch();
            }

        private CorporateActionSearch()
            { }

        public CorporateActionSearch symbol(String symbol)
            {
                put("symbol", symbol);
                return this;
            }

        public CorporateActionSearch dateFrom(Date date)
            {
                put("dateFrom", date);
                return this;
            }

        public CorporateActionSearch dateFrom(String date)
            {
                put("dateFrom", date);
                return this;
            }

        public CorporateActionSearch dateTo(Date date)
            {
                put("dateTo", date);
                return this;
            }

        public CorporateActionSearch dateTo(String date)
            {
                put("dateTo", date);
                return this;
            }

        public CorporateActionSearch type(String type)
            {
                put("type", type);
                return this;
            }
    }
