package com.github.sd;

import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 12/7/2018 3:45 PM
 */
public interface Endpoint
    {
        String getPath();

        default Class<?> getDatatype() {
            return JSONObject.class;
        }

        default boolean isJson() {
            return true;
        }
    }
