package com.github;

/**
 * Daniil Sosonkin
 * Jul 16, 2008 11:01:01 AM
 */
public class MutualFundOrder extends ASingleOrder
    {
        private double value;
        private double shares;

        public double getValue()
            {
                return value;
            }

        public void setValue(double value)
            {
                this.value = value;
            }

        public double getShares()
            {
                return shares;
            }

        public void setShares(double shares)
            {
                this.shares = shares;
            }
    }
