package com.github.sd;

import java.net.*;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 2:43 PM
 */
public interface Credentials
    {
        default URL provideUrl(String path) throws MalformedURLException
            {
                return null;
            }

        default boolean base64()
            {
                return true;
            }

        String getToken();
        String getScheme();
        void expired();
    }
