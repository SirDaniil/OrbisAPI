package com.github.sd;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/24/2018 9:54 AM
 */
public class FilterKey
    {
        public static final FilterKey Aggregator = new FilterKey("aggregator");
        public static final FilterKey Provider = new FilterKey("provider");
        private Set<FilterValue> values = new HashSet<>();
        private boolean exclude;
        private String value;

        private FilterKey(String value)
            {
                this.value = value;
            }

        public FilterKey add(String value)
            {
                values.add(new FilterValue(value));
                return this;
            }

        public FilterKey add(FilterValue value)
            {
                values.add(value);
                return this;
            }

        Set<FilterValue> getValues()
            {
                return values;
            }

        public boolean isExclude()
            {
                return exclude;
            }

        public FilterKey exclude()
            {
                this.exclude = true;
                return this;
            }

        public FilterKey include()
            {
                this.exclude = false;
                return this;
            }

        @Override
        public String toString()
            {
                return (exclude ? "-" : "") + value;
            }

        @Override
        public boolean equals(Object o)
            {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FilterKey filterKey = (FilterKey) o;
                return Objects.equals(value, filterKey.value);
            }

        @Override
        public int hashCode()
            {
                return Objects.hash(value);
            }
    }
