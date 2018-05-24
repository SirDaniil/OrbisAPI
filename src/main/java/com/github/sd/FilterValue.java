package com.github.sd;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/24/2018 9:55 AM
 */
public class FilterValue implements CharSequence
    {
        public interface Aggregators {
            FilterValue News = new FilterValue("NEWS____");
            FilterValue Uspr = new FilterValue("USPR____");
            FilterValue UsEquity = new FilterValue("USEQUITY");
            FilterValue Intraday = new FilterValue("INTRADAY");
            FilterValue Morning = new FilterValue("MORNING_");
            FilterValue Rumors = new FilterValue("RUMORS__");
            FilterValue Mainwire = new FilterValue("MAINWIRE");
            FilterValue FullFeed = new FilterValue("FULLFEED");
            FilterValue Edgar = new FilterValue("EDGAR___");
        }

        public interface Providers {
            FilterValue Benzinga = new FilterValue("BENZINGA");
            FilterValue Bizwire = new FilterValue("BIZWIRE_");
            FilterValue FlyOnWall = new FilterValue("FLYWALL_");
            FilterValue JagMedia = new FilterValue("JAGMEDIA");
            FilterValue Sec = new FilterValue("SEC_____");
            FilterValue PrNews = new FilterValue("PR_NEWS_");
        }
        private String value;

        FilterValue(String value)
            {
                this.value = value;
            }

        @Override
        public int length()
            {
                return value.length();
            }

        @Override
        public char charAt(int index)
            {
                return value.charAt(index);
            }

        @Override
        public CharSequence subSequence(int start, int end)
            {
                return value.subSequence(start, end);
            }

        @Override
        public String toString()
            {
                return value;
            }

        @Override
        public boolean equals(Object o)
            {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FilterValue that = (FilterValue) o;
                return Objects.equals(value, that.value);
            }

        @Override
        public int hashCode()
            {
                return Objects.hash(value);
            }
    }
