package com.github.sd;

import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 12/7/2018 3:47 PM
 */
public class Endpoints
    {
        private Endpoints()
            { }

        public static final Endpoint QuotesEquity = new Endpoint() {
                @Override
                public String getPath()
                    {
                        return "/quotes/equity";
                    }

                @Override
                public Class getDatatype()
                    {
                        return JSONArray.class;
                    }
        };

        public static final Endpoint QuotesSearch = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/quotes/search";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ChartsIntraday = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/quotes/equity/intraday";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ChartsHistorical = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/quotes/equity/historical";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint Research = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/{symbol}";
                }
        };

        public static final Endpoint ResearchAdrs = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/adrs";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ResearchAdrsTop10 = () -> "/research/adrs/top10";

        public static final Endpoint ResearchAdrsTop10Defaults = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/adrs/top10/defaults";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ResearchNews = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/news";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ResearchNewsBySymbol = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/news/ticker/{symbol}";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ResearchFundamentalTypes = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/fundamentals/types";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint ResearchFundamentals = () -> "/research/fundamentals/{type}/{symbol}";

        public static final Endpoint ResearchScreener = () -> "/research/screener";

        public static final Endpoint ResearchMarketDateLastOpen = () -> "/research/dates/lastOpen";

        public static final Endpoint ResearchMarketDateCheck = () -> "/research/dates/check";

        public static final Endpoint ResearchMarketDates = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/dates/markets";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint CorporateActionTypes = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/actions/types";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint CorporateActionSearch = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/actions/search";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint TipranksLivefeed = new Endpoint() {
            @Override
            public String getPath()
                {
                    return "/research/tipranks/livefeed";
                }

            @Override
            public Class getDatatype()
                {
                    return JSONArray.class;
                }
        };

        public static final Endpoint UserInfo = () -> "/user/info";
    }
