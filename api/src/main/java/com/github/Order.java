package com.github;

import java.util.*;

/**
 * Daniil Sosonkin
 * Apr 23, 2008 10:31:49 AM
 */
public class Order
    {
        private UserAccount account;
        private Quote quote;
        private OrderType orderType;
        private OrderValidity validity;
        private MarketTime marketTime = MarketTime.CORE_MARKET;
        private Date expireTime;
        private Date triggerTime;
        private String symbol;
        private String ourRef;
        private String status;
        private String transType;
        private String openClose;
        private String handlingRequest;
        private String pendingRequest;
        private String market;
        private String algoStrategy;
        private String tradeCurrency;
        private String accountCurrency;
        private String pegType;
        private long orderId;
        private long contraOid;
        private long accountId;
        private long userId;
        private long modelId;
        private int aon;
        private int margin;
        private int requestState;
        private int accountType;
        private int optionFlag;
        private int complexType;
        private int legRef;
        private int noLegs;
        private double quantity;
        private double execQty;
        private double remainingQty;
        private double unallocatedQty;
        private double limitPx;
        private double stopPx;
        private double execPx;
        private double expectedPx;
        private double volumeRate;
        private double pegDifference;
        private double commission;
        private double markup;
        private double fee1;
        private double fee2;
        private double fee3;
        private Double profitLoss;
        private Boolean replaceable;
        private Date createDate;
        private Date executedDate;
        private Date startTime;
        private Date endTime;

        public UserAccount getAccount()
            {
                return account;
            }

        public Order setAccount(UserAccount account)
            {
                this.account = account;
                return this;
            }

        public Quote getQuote()
            {
                return quote;
            }

        public Order setQuote(Quote quote)
            {
                this.quote = quote;
                return this;
            }

        public OrderType getOrderType()
            {
                return orderType;
            }

        public Order setOrderType(OrderType orderType)
            {
                this.orderType = orderType;
                return this;
            }

        public OrderValidity getValidity()
            {
                return validity;
            }

        public Order setValidity(OrderValidity validity)
            {
                this.validity = validity;
                return this;
            }

        public MarketTime getMarketTime()
            {
                return marketTime;
            }

        public Order setMarketTime(MarketTime marketTime)
            {
                this.marketTime = marketTime;
                return this;
            }

        public Date getExpireTime()
            {
                return expireTime;
            }

        public Order setExpireTime(Date expireTime)
            {
                this.expireTime = expireTime;
                return this;
            }

        public Date getTriggerTime()
            {
                return triggerTime;
            }

        public Order setTriggerTime(Date triggerTime)
            {
                this.triggerTime = triggerTime;
                return this;
            }

        public String getSymbol()
            {
                return symbol;
            }

        public Order setSymbol(String symbol)
            {
                this.symbol = symbol;
                return this;
            }

        public String getOurRef()
            {
                return ourRef;
            }

        public Order setOurRef(String ourRef)
            {
                this.ourRef = ourRef;
                return this;
            }

        public String getStatus()
            {
                return status;
            }

        public Order setStatus(String status)
            {
                this.status = status;
                return this;
            }

        public String getTransType()
            {
                return transType;
            }

        public Order setTransType(String transType)
            {
                this.transType = transType;
                return this;
            }

        public String getOpenClose()
            {
                return openClose;
            }

        public Order setOpenClose(String openClose)
            {
                this.openClose = openClose;
                return this;
            }

        public String getHandlingRequest()
            {
                return handlingRequest;
            }

        public Order setHandlingRequest(String handlingRequest)
            {
                this.handlingRequest = handlingRequest;
                return this;
            }

        public String getPendingRequest()
            {
                return pendingRequest;
            }

        public Order setPendingRequest(String pendingRequest)
            {
                this.pendingRequest = pendingRequest;
                return this;
            }

        public String getMarket()
            {
                return market;
            }

        public Order setMarket(String market)
            {
                this.market = market;
                return this;
            }

        public String getAlgoStrategy()
            {
                return algoStrategy;
            }

        public Order setAlgoStrategy(String algoStrategy)
            {
                this.algoStrategy = algoStrategy;
                return this;
            }

        public String getTradeCurrency()
            {
                return tradeCurrency;
            }

        public Order setTradeCurrency(String tradeCurrency)
            {
                this.tradeCurrency = tradeCurrency;
                return this;
            }

        public String getAccountCurrency()
            {
                return accountCurrency;
            }

        public Order setAccountCurrency(String accountCurrency)
            {
                this.accountCurrency = accountCurrency;
                return this;
            }

        public String getPegType()
            {
                return pegType;
            }

        public Order setPegType(String pegType)
            {
                this.pegType = pegType;
                return this;
            }

        public long getOrderId()
            {
                return orderId;
            }

        public Order setOrderId(long orderId)
            {
                this.orderId = orderId;
                return this;
            }

        public long getContraOid()
            {
                return contraOid;
            }

        public Order setContraOid(long contraOid)
            {
                this.contraOid = contraOid;
                return this;
            }

        public long getAccountId()
            {
                return accountId;
            }

        public Order setAccountId(long accountId)
            {
                this.accountId = accountId;
                return this;
            }

        public long getUserId()
            {
                return userId;
            }

        public Order setUserId(long userId)
            {
                this.userId = userId;
                return this;
            }

        public long getModelId()
            {
                return modelId;
            }

        public Order setModelId(long modelId)
            {
                this.modelId = modelId;
                return this;
            }

        public int getAon()
            {
                return aon;
            }

        public Order setAon(int aon)
            {
                this.aon = aon;
                return this;
            }

        public int getMargin()
            {
                return margin;
            }

        public Order setMargin(int margin)
            {
                this.margin = margin;
                return this;
            }

        public int getRequestState()
            {
                return requestState;
            }

        public Order setRequestState(int requestState)
            {
                this.requestState = requestState;
                return this;
            }

        public int getAccountType()
            {
                return accountType;
            }

        public Order setAccountType(int accountType)
            {
                this.accountType = accountType;
                return this;
            }

        public int getOptionFlag()
            {
                return optionFlag;
            }

        public Order setOptionFlag(int optionFlag)
            {
                this.optionFlag = optionFlag;
                return this;
            }

        public int getComplexType()
            {
                return complexType;
            }

        public Order setComplexType(int complexType)
            {
                this.complexType = complexType;
                return this;
            }

        public int getLegRef()
            {
                return legRef;
            }

        public Order setLegRef(int legRef)
            {
                this.legRef = legRef;
                return this;
            }

        public int getNoLegs()
            {
                return noLegs;
            }

        public Order setNoLegs(int noLegs)
            {
                this.noLegs = noLegs;
                return this;
            }

        public double getQuantity()
            {
                return quantity;
            }

        public Order setQuantity(double quantity)
            {
                this.quantity = quantity;
                return this;
            }

        public double getExecQty()
            {
                return execQty;
            }

        public Order setExecQty(double execQty)
            {
                this.execQty = execQty;
                return this;
            }

        public double getRemainingQty()
            {
                return remainingQty;
            }

        public Order setRemainingQty(double remainingQty)
            {
                this.remainingQty = remainingQty;
                return this;
            }

        public double getUnallocatedQty()
            {
                return unallocatedQty;
            }

        public Order setUnallocatedQty(double unallocatedQty)
            {
                this.unallocatedQty = unallocatedQty;
                return this;
            }

        public double getLimitPx()
            {
                return limitPx;
            }

        public Order setLimitPx(double limitPx)
            {
                this.limitPx = limitPx;
                return this;
            }

        public double getStopPx()
            {
                return stopPx;
            }

        public Order setStopPx(double stopPx)
            {
                this.stopPx = stopPx;
                return this;
            }

        public double getExecPx()
            {
                return execPx;
            }

        public Order setExecPx(double execPx)
            {
                this.execPx = execPx;
                return this;
            }

        public double getExpectedPx()
            {
                return expectedPx;
            }

        public Order setExpectedPx(double expectedPx)
            {
                this.expectedPx = expectedPx;
                return this;
            }

        public double getVolumeRate()
            {
                return volumeRate;
            }

        public Order setVolumeRate(double volumeRate)
            {
                this.volumeRate = volumeRate;
                return this;
            }

        public double getPegDifference()
            {
                return pegDifference;
            }

        public Order setPegDifference(double pegDifference)
            {
                this.pegDifference = pegDifference;
                return this;
            }

        public double getCommission()
            {
                return commission;
            }

        public Order setCommission(double commission)
            {
                this.commission = commission;
                return this;
            }

        public double getMarkup()
            {
                return markup;
            }

        public Order setMarkup(double markup)
            {
                this.markup = markup;
                return this;
            }

        public double getFee1()
            {
                return fee1;
            }

        public Order setFee1(double fee1)
            {
                this.fee1 = fee1;
                return this;
            }

        public double getFee2()
            {
                return fee2;
            }

        public Order setFee2(double fee2)
            {
                this.fee2 = fee2;
                return this;
            }

        public double getFee3()
            {
                return fee3;
            }

        public Order setFee3(double fee3)
            {
                this.fee3 = fee3;
                return this;
            }

        public Double getProfitLoss()
            {
                return profitLoss;
            }

        public Order setProfitLoss(Double profitLoss)
            {
                this.profitLoss = profitLoss;
                return this;
            }

        public Boolean getReplaceable()
            {
                return replaceable;
            }

        public Order setReplaceable(Boolean replaceable)
            {
                this.replaceable = replaceable;
                return this;
            }

        public Date getCreateDate()
            {
                return createDate;
            }

        public Order setCreateDate(Date createDate)
            {
                this.createDate = createDate;
                return this;
            }

        public Date getExecutedDate()
            {
                return executedDate;
            }

        public Order setExecutedDate(Date executedDate)
            {
                this.executedDate = executedDate;
                return this;
            }

        public Date getStartTime()
            {
                return startTime;
            }

        public Order setStartTime(Date startTime)
            {
                this.startTime = startTime;
                return this;
            }

        public Date getEndTime()
            {
                return endTime;
            }

        public Order setEndTime(Date endTime)
            {
                this.endTime = endTime;
                return this;
            }
    }
