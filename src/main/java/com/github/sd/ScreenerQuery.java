package com.github.sd;

/**
 * User: Daniil Sosonkin
 * Date: 5/30/2018 11:09 AM
 */
public class ScreenerQuery
    {
        private Screener.Field field;
        private Screener.Comparator comparator;
        private DateRange range;
        private Object value;

        public Screener.Field getField()
            {
                return field;
            }

        public void setField(Screener.Field field)
            {
                this.field = field;
            }

        public Screener.Comparator getComparator()
            {
                return comparator;
            }

        public void setComparator(Screener.Comparator comparator)
            {
                this.comparator = comparator;
            }

        public Object getValue()
            {
                return value;
            }

        public void setValue(Object value)
            {
                this.value = value;
            }

        public DateRange getRange()
            {
                return range;
            }

        public void setRange(DateRange range)
            {
                this.range = range;
            }
    }
