package com.orbis.bench;

import org.json.*;

public record StapiData(double seconds)
    {
        public String toJSON()
            {
                var obj = new JSONObject();
                obj.put("seconds", seconds);
                obj.put("tag", "audit-quotes");
                obj.put("group", "MUMBAY");
                obj.put("name", "ping");
                obj.put("timestamp", System.currentTimeMillis());

                return obj.toString();
            }
    }
