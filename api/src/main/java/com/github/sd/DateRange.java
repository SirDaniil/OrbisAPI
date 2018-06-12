package com.github.sd;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/30/2018 2:20 PM
 */
public class DateRange
    {
        private Date lower;
        private Date upper;

        public DateRange()
            {
                this.lower = this.upper = new Date();
            }

        public Date getLower()
            {
                return lower;
            }

        public DateRange setLower(Date lower)
            {
                this.lower = lower;
                return this;
            }

        public Date getUpper()
            {
                return upper;
            }

        public DateRange setUpper(Date upper)
            {
                this.upper = upper;
                return this;
            }

        public DateRange lowerToday()
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(lower);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                lower = cal.getTime();

                return this;
            }

        public DateRange upperNextMonth()
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(upper);
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                upper.getTime();

                return this;
            }
    }
