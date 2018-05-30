package com.github.sd;

import java.text.*;
import java.util.*;
import org.json.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/30/2018 10:44 AM
 */
public class Screener extends ArrayList<ScreenerQuery> implements JsonConvertable
    {
        enum Comparator {
            Equals,
            LessThan,
            GreaterThan,
            NotEquals
        }

        public enum Field {
            MarketCap("mcap"),
            LastPrice("price"),
            Beta("beta"),
            DividendAmount("dividend.amount"),
            DividendRate("dividend.rate"),
            DividendYield("dividend.yield"),
            DividendPayDate("dividend.paydate"),
            DividendExDate("dividend.exdate"),
            Exchange("exchange"),
            Gap("gap"),
            Adr("adr"),
            Etf("etf"),
            Industry("industry"),
            Volume("vol"),
            Volume30D("volAvg30"),
            PriceEarningRatio("pe_ratio");

            private String fieldName;

            Field(String fieldName)
                {
                    this.fieldName = fieldName;
                }

            public String getFieldName()
                {
                    return fieldName;
                }
        }

        public static Screener Builder()
            {
                return new Screener();
            }

        private Screener()
            { }

        public Screener is(Field field)
            {
                ScreenerQuery query = new ScreenerQuery();
                query.setComparator(Comparator.Equals);
                query.setField(field);
                query.setValue(true);
                add(query);

                return this;
            }

        public Screener eq(Field field, Object value)
            {
                ScreenerQuery query = new ScreenerQuery();
                query.setComparator(Comparator.Equals);
                query.setField(field);
                query.setValue(value);
                add(query);

                return this;
            }

        public Screener gte(Field field, double value)
            {
                ScreenerQuery query = new ScreenerQuery();
                query.setComparator(Comparator.GreaterThan);
                query.setField(field);
                query.setValue(value);
                add(query);

                return this;
            }

        public Screener gte(Field field, double value, DateRange range)
            {
                ScreenerQuery query = new ScreenerQuery();
                query.setComparator(Comparator.GreaterThan);
                query.setField(field);
                query.setValue(value);
                query.setRange(range);
                add(query);

                return this;
            }

        @Override
        public String toJSON()
            {
                DateFormat fmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
                JSONArray array = new JSONArray();
                forEach(query -> {
                    JSONObject obj = new JSONObject();
                    obj.put("field", query.getField().toString());
                    obj.put("comparator", query.getComparator().toString());
                    obj.put("value", query.getValue());

                    DateRange range = query.getRange();
                    if (range != null)
                        {
                            JSONObject robj = new JSONObject();
                            robj.put("lowerBound", fmt.format(range.getLower()));
                            robj.put("upperBound", fmt.format(range.getUpper()));
                            obj.put("dateRange", robj);
                        }

                    array.put(obj);
                });

                return array.toString();
            }
    }
