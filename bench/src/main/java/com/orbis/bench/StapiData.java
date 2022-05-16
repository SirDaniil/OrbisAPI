package com.orbis.bench;

import org.json.*;

public class StapiData
    {
        private final long createdOn = System.currentTimeMillis();
        private final double seconds;

        public StapiData(double seconds)
            {
                this.seconds = seconds;
            }

        public String toJSON(String region)
            {
                var obj = new JSONObject();
                obj.put("seconds", seconds);
                obj.put("tag", "audit-quotes");
                obj.put("group", "audit-" + region);
                obj.put("name", "ping");
                obj.put("timestamp", createdOn);

                return obj.toString();
            }
    }
