package com.github.sd;

import org.json.*;

/**
 * Created by Daniil Sosonkin
 * 6/15/2018 3:33 PM
 */
public enum Endpoint
    {
        QuotesEquity("/quotes/equity", JSONArray.class),
        QuotesSearch("/quotes/search", JSONArray.class),
        ResearchAdrs("/research/adrs", JSONArray.class),
        ResearchAdrsTop10("/research/adrs/top10", JSONObject.class),
        ResearchAdrsTop10Defaults("/research/adrs/top10/defaults", JSONArray.class),
        ResearchNews("/research/news", JSONArray.class),
        ResearchNewsBySymbol("/research/news/ticker/{symbol}", JSONArray.class),
        ResearchFundamentalTypes("/research/fundamentals/types", JSONArray.class),
        ResearchFundamentals("/research/fundamentals/{type}/{symbol}", JSONObject.class),
        ResearchScreener("/research/screener", JSONObject.class),
        UserInfo("/user/info", JSONObject.class),;
        private String path;
        private Class clazz;

        Endpoint(String path, Class clazz)
            {
                this.path = path;
                this.clazz = clazz;
            }

        public String getPath()
            {
                return path;
            }

        public Class getClazz()
            {
                return clazz;
            }
    }
