package com.github;

import java.util.*;

/**
 * Daniil Sosonkin
 * Jul 16, 2008 10:47:13 AM
 */
public abstract class ASingleOrder
    {
        protected Quote quote;
        protected Transaction transaction;
        protected MarketTime marketTime = MarketTime.CORE_MARKET;
        protected String password;
        protected String subDestination;
        protected String orderRef;
        protected String marketCode = "U";
        protected String clientRef;
        protected String tradeCurrency;
        protected String accountCurrency;
        protected String trailer;
        protected Order underlyingOrder;
        protected Date receiveTime;
        protected Date expireTime;
        protected Date triggerTime;
        protected int index = 1;
        protected long quantity;
        protected long maxFloor = -1;
        protected double fee1;
        protected double fee2;
        protected double fee3;
        protected double markup;
        protected double commission = -1;
        protected boolean solicited;

        public String getPassword()
            {
                return password;
            }

        public void setPassword(String password)
            {
                this.password = password;
            }

        public Date getTriggerTime()
            {
                return triggerTime;
            }

        public void setTriggerTime(Date triggerTime)
            {
                this.triggerTime = triggerTime;
            }

        public double getMarkup()
            {
                return markup;
            }

        public void setMarkup(double markup)
            {
                this.markup = markup;
            }

        public double getFee1()
            {
                return fee1;
            }

        public void setFee1(double fee1)
            {
                this.fee1 = fee1;
            }

        public double getFee2()
            {
                return fee2;
            }

        public void setFee2(double fee2)
            {
                this.fee2 = fee2;
            }

        public double getFee3()
            {
                return fee3;
            }

        public void setFee3(double fee3)
            {
                this.fee3 = fee3;
            }

        public Date getExpireTime()
            {
                return expireTime;
            }

        public void setExpireTime(Date expireTime)
            {
                this.expireTime = expireTime;
            }

        public String getMarketCode()
            {
                return marketCode;
            }

        public void setMarketCode(String marketCode)
            {
                this.marketCode = marketCode;
            }

        public String getSubDestination()
            {
                return subDestination;
            }

        public void setSubDestination(String subDestination)
            {
                this.subDestination = subDestination;
            }

        public long getMaxFloor()
            {
                return maxFloor;
            }

        public void setMaxFloor(long maxFloor)
            {
                this.maxFloor = maxFloor;
            }

        public String getTradeCurrency()
            {
                return tradeCurrency;
            }

        public void setTradeCurrency(String tradeCurrency)
            {
                this.tradeCurrency = tradeCurrency;
            }

        public String getAccountCurrency()
            {
                return accountCurrency;
            }

        public void setAccountCurrency(String accountCurrency)
            {
                this.accountCurrency = accountCurrency;
            }

        public String getClientRef()
            {
                return clientRef;
            }

        public void setClientRef(String clientRef)
            {
                this.clientRef = clientRef;
            }

        public Order getUnderlyingOrder()
            {
                return underlyingOrder;
            }

        public void setUnderlyingOrder(Order underlyingOrder)
            {
                this.underlyingOrder = underlyingOrder;
            }

        public String getOrderRef()
            {
                return orderRef;
            }

        public void setOrderRef(String orderRef)
            {
                this.orderRef = orderRef;
            }

        public void setOrderIndex(int index)
            {
                this.index = index;
            }

        public int getOrderIndex()
            {
                return index;
            }

        public void setQuote(Quote quote)
            {
                this.quote = quote;
            }

        public Quote getQuote()
            {
                return quote;
            }

        public Transaction getTransaction()
            {
                return transaction;
            }

        public void setTransaction(Transaction transaction)
            {
                this.transaction = transaction;
            }

        public long getQuantity()
            {
                return quantity;
            }

        public void setQuantity(long quantity)
            {
                this.quantity = quantity;
            }

        public boolean isSolicited()
            {
                return solicited;
            }

        public void setSolicited(boolean solicited)
            {
                this.solicited = solicited;
            }

        public double getCommission()
            {
                return commission;
            }

        public void setCommission(double commission)
            {
                this.commission = commission;
            }

        public Date getReceiveTime()
            {
                return receiveTime;
            }

        public void setReceiveTime(Date receiveTime)
            {
                this.receiveTime = receiveTime;
            }

        public String getTrailer()
            {
                return trailer;
            }

        public void setTrailer(String trailer)
            {
                this.trailer = trailer;
            }

        public MarketTime getMarketTime()
            {
                return marketTime;
            }

        public void setMarketTime(MarketTime marketTime)
            {
                this.marketTime = marketTime;
            }
    }
