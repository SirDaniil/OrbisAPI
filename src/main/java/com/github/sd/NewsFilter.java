package com.github.sd;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/24/2018 9:53 AM
 */
public class NewsFilter extends HashSet<FilterKey>
    {
        public static NewsFilter Builder()
            {
                return new NewsFilter();
            }

        private NewsFilter()
            { }

        public NewsFilter filter(FilterKey key)
            {
                add(key);
                return this;
            }

        public String toString()
            {
                StringBuilder buf = new StringBuilder();
                forEach(key -> {
                    Set<FilterValue> values = key.getValues();
                    if (values.isEmpty())
                        return;

                    buf.append(key);
                    buf.append(':');
                    buf.append(String.join(",", values));
                    buf.append(';');
                });

                return buf.toString();
            }
    }
