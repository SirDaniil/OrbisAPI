package com.github.sd;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/29/2018 9:52 AM
 */
public class AdrRequest extends HashMap<String, Object>
    {
        public static AdrRequest Builder()
            {
                return new AdrRequest();
            }

        private AdrRequest()
            { }

        public AdrRequest country(String country)
            {
                put("country", country);
                return this;
            }

        public AdrRequest loadQuotes()
            {
                put("loadQuotes", true);
                return this;
            }

        public AdrRequest loadEarningReleases()
            {
                put("loadEarningReleases", true);
                return this;
            }

        public AdrRequest loadUpgradesDowngrades()
            {
                put("loadUpgradesDowngrades", true);
                return this;
            }
    }
