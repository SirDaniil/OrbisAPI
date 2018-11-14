package com.github;

/**
 * Daniil Sosonkin
 * Jul 16, 2008 11:01:01 AM
 */
public class EquityOrder extends ASingleOrder
    {
        private EquityOrder oco;
        private OrderType orderType;
        private OrderFillType fillType;
        private OrderValidity validity;
        private String pegType;
        private int orderAccountType;
        private double expectedPrice;
        private double limitPrice;
        private double stopPrice;
        private double pegDifference;
        private double limitOffset;
        private boolean stockLocated;
        private boolean valid;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getPegType()
            {
                return pegType;
            }

        public void setPegType(String pegType)
            {
                this.pegType = pegType;
            }

        public double getPegDifference()
            {
                return pegDifference;
            }

        public void setPegDifference(double pegDifference)
            {
                this.pegDifference = pegDifference;
            }

        public boolean isStockLocated()
            {
                return stockLocated;
            }

        public void setStockLocated(boolean stockLocated)
            {
                this.stockLocated = stockLocated;
            }

        public double getExpectedPrice()
            {
                return expectedPrice;
            }

        public void setExpectedPrice(double expectedPrice)
            {
                this.expectedPrice = expectedPrice;
            }

        public void setOrderType(OrderType orderType)
            {
                this.orderType = orderType;
            }

        public void setFillType(OrderFillType fillType)
            {
                this.fillType = fillType;
            }

        public void setValidity(OrderValidity validity)
            {
                this.validity = validity;
            }

        public void setOrderAccountType(int orderAccountType)
            {
                this.orderAccountType = orderAccountType;
            }

        public void setLimitPrice(double limitPrice)
            {
                this.limitPrice = limitPrice;
            }

        public void setStopPrice(double stopPrice)
            {
                this.stopPrice = stopPrice;
            }

        public OrderType getOrderType()
            {
                return orderType;
            }

        public OrderFillType getFillType()
            {
                return fillType;
            }

        public OrderValidity getValidity()
            {
                return validity;
            }

        public int getOrderAccountType()
            {
                return orderAccountType;
            }

        public double getLimitPrice()
            {
                return limitPrice;
            }

        public double getStopPrice()
            {
                return stopPrice;
            }

        public EquityOrder getOco()
            {
                return oco;
            }

        public void setOco(EquityOrder oco)
            {
                this.oco = oco;
            }

        public double getLimitOffset()
            {
                return limitOffset;
            }

        public void setLimitOffset(double limitOffset)
            {
                this.limitOffset = limitOffset;
            }
    }
