package com.github;

/**
 * Daniil Sosonkin
 * Jul 16, 2008 11:01:01 AM
 */
public class MutualFundOrder extends ASingleOrder
    {
        private MutualFundOrder substitution;
        private boolean full;
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

        public boolean isFull()
            {
                return full;
            }

        public void setFull(boolean full)
            {
                this.full = full;
            }

        public MutualFundOrder getSubstitution()
            {
                return substitution;
            }

        public void setSubstitution(MutualFundOrder substitution)
            {
                this.substitution = substitution;
            }
    }
