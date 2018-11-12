package com.github;

import java.io.*;
import java.util.*;

/**
 * Daniil Sosonkin
 * Mar 26, 2008 10:22:29 AM
 */
public class Quote implements Serializable
    {
        private static final Set<String> reserved = new HashSet<String>() {{
            add("EU");
            add("IV");
            add("NV");
            add("SO");
            add("TC");
        }};
        public static final char PUT = 'P';
        public static final char CALL = 'C';
        public static final byte IN_THE_MONEY = 'i';
        public static final byte AT_THE_MONEY = 'a';
        public static final byte OUT_OF_MONEY = 'o';

        private String symbol;
        private String underlyingSymbol;
        private String marketCentre;
        private String companyName;
        private String nativeCompanyName;
        private String isin;
        private String cusip;
        private String sedol;
        private String newSymbol;
        private String rootSymbol;
        private String standardAndPoorsRating;
        private String gics;
        private String country;
        private String mic;
        private String currency = "USD";
        private String source;
        private String quoteId;
        private Date symbolChangeDate;
        private Date expirationDate;
        private Date updateTime;
        private Date tradeTime;
        private Date dividendPaymentDate;
        private Date dividendRecordDate;
        private Date dividendExDate;
        private Date calendarYearHighDate;
        private Date calendarYearLowDate;
        private Date week52HighDate;
        private Date week52LowDate;
        private Date haltedOn;
        private double vwap;
        private double vwap1;
        private double yesterdayClose;
        private double openingPrice;
        private double strikePrice;
        private double dayHigh;
        private double dayLow;
        private double ask;
        private double bid;
        private double askYield;
        private double bidYield;
        private double lastPrice;
        private double change;
        private double openInterest;
        private double high52week;
        private double low52week;
        private double priceEarningRatio;
        private double changePercent;
        private double marketCap;
        private double calendarYearHigh;
        private double calendarYearLow;
        private double dividendYield;
        private double dividendAmount;
        private double dividendRate;
        private double beta;
        private double totalCashAmount;
        private double estimatedCashAmount;
        private double intradayValue;
        private double nav;
        private long yesterdayVolume;
        private long askSize;
        private long bidSize;
        private long minAskSize;
        private long minBidSize;
        private long volume;
        private long contractSize;
        private long averageVolume30;
        private long imbalanceVolume;
        private long sharesOutstanding;
        private long increment;
        private char putOrCall;
        private int indicator;
        private int subMarket;
        private int bidTick;
        private int precision;
        private boolean delayed;
        private boolean notFound;
        private boolean option;
        private boolean notPermissioned;
        private boolean halted;
        private boolean paused;
        private boolean mini;
        private boolean corpAct;
        private boolean jumbo;
        private boolean mutualFund;
        private boolean bond;
        private Quote underlying;

        public Quote(String symbol)
            {
                this.symbol = symbol;
            }

        public Quote()
            { }

        public boolean isSymbolChange()
            {
                return (newSymbol != null && symbolChangeDate != null);
            }

        public boolean isMutualFund()
            {
                return mutualFund;
            }

        /**
         * Returns true when we're at the money.
         *
         * @return
         */
        public boolean isAtTheMoney()
            {
                return (underlying != null && underlying.getLastPrice() == lastPrice);
            }

        /**
         * Returns true when in the money.
         * 
         * @return
         */
        public boolean isInTheMoney()
            {
                if (underlying == null)
                    return false;

                if (isPut())
                    return (strikePrice > underlying.lastPrice);
                else
                    return (strikePrice < underlying.lastPrice);
            }

        /**
         * Returns true if this is Call symbol.
         *
         * @return
         */
        public boolean isCall()
            {
                return (putOrCall == CALL);
            }

        /**
         * Returns true of this is a Put symbol.
         *
         * @return
         */
        public boolean isPut()
            {
                return (putOrCall == PUT);
            }

        ////////////////////////////////////////////////////////////////////////////////////////////////////

        public boolean equals(Object o)
            {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Quote quote = (Quote)o;

                return option == quote.option && !(symbol != null ? !symbol.equals(quote.symbol) : quote.symbol != null);
            }

        public int hashCode()
            {
                int result;
                result = (symbol != null ? symbol.hashCode() : 0);
                result = 31 * result + (option ? 1 : 0);

                return result;
            }

        public String toString()
            {
                return symbol;
            }

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////// GETTERS AND SETTERS ARE BELOW
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        public double getTotalCashAmount()
            {
                return totalCashAmount;
            }

        public void setTotalCashAmount(double totalCashAmount)
            {
                this.totalCashAmount = totalCashAmount;
            }

        public double getEstimatedCashAmount()
            {
                return estimatedCashAmount;
            }

        public void setEstimatedCashAmount(double estimatedCashAmount)
            {
                this.estimatedCashAmount = estimatedCashAmount;
            }

        public double getIntradayValue()
            {
                return intradayValue;
            }

        public void setIntradayValue(double intradayValue)
            {
                this.intradayValue = intradayValue;
            }

        public double getNav()
            {
                return nav;
            }

        public void setNav(double nav)
            {
                this.nav = nav;
            }

        public long getSharesOutstanding()
            {
                return sharesOutstanding;
            }

        public void setSharesOutstanding(long sharesOutstanding)
            {
                this.sharesOutstanding = sharesOutstanding;
            }

        public Date getHaltedOn()
            {
                return haltedOn;
            }

        public void setHaltedOn(Date haltedOn)
            {
                this.haltedOn = haltedOn;
            }

        public boolean isHalted()
            {
                return halted;
            }

        public void setHalted(boolean halted)
            {
                this.halted = halted;
            }

        public boolean isPaused()
            {
                return paused;
            }

        public void setPaused(boolean paused)
            {
                this.paused = paused;
            }

        public boolean isNotPermissioned()
            {
                return notPermissioned;
            }

        public void setNotPermissioned(boolean notPermissioned)
            {
                this.notPermissioned = notPermissioned;
            }

        public String getCountry()
            {
                return country;
            }

        public void setCountry(String country)
            {
                this.country = country;
            }

        public char getPutOrCall()
            {
                return putOrCall;
            }

        public void setPutOrCall(char putOrCall)
            {
                this.putOrCall = putOrCall;
            }

        public String getRootSymbol()
            {
                return rootSymbol;
            }

        public void setRootSymbol(String rootSymbol)
            {
                this.rootSymbol = rootSymbol;
            }

        public String getNewSymbol()
            {
                return newSymbol;
            }

        public void setNewSymbol(String newSymbol)
            {
                this.newSymbol = newSymbol;
            }

        public Date getSymbolChangeDate()
            {
                return symbolChangeDate;
            }

        public void setSymbolChangeDate(Date symbolChangeDate)
            {
                this.symbolChangeDate = symbolChangeDate;
            }

        public double getCalendarYearHigh()
            {
                return calendarYearHigh;
            }

        public void setCalendarYearHigh(double calendarYearHigh)
            {
                this.calendarYearHigh = calendarYearHigh;
            }

        public double getCalendarYearLow()
            {
                return calendarYearLow;
            }

        public void setCalendarYearLow(double calendarYearLow)
            {
                this.calendarYearLow = calendarYearLow;
            }

        public double getDividendYield()
            {
                return dividendYield;
            }

        public void setDividendYield(double dividendYield)
            {
                this.dividendYield = dividendYield;
            }

        public double getDividendAmount()
            {
                return dividendAmount;
            }

        public void setDividendAmount(double dividendAmount)
            {
                this.dividendAmount = dividendAmount;
            }

        public double getDividendRate()
            {
                return dividendRate;
            }

        public void setDividendRate(double dividendRate)
            {
                this.dividendRate = dividendRate;
            }

        public Date getDividendRecordDate()
            {
                return dividendRecordDate;
            }

        public void setDividendRecordDate(Date dividendRecordDate)
            {
                this.dividendRecordDate = dividendRecordDate;
            }

        public Date getDividendPaymentDate()
            {
                return dividendPaymentDate;
            }

        public void setDividendPaymentDate(Date dividendPaymentDate)
            {
                this.dividendPaymentDate = dividendPaymentDate;
            }

        public Date getDividendExDate()
            {
                return dividendExDate;
            }

        public void setDividendExDate(Date dividendExDate)
            {
                this.dividendExDate = dividendExDate;
            }

        public long getYesterdayVolume()
            {
                return yesterdayVolume;
            }

        public void setYesterdayVolume(long yesterdayVolume)
            {
                this.yesterdayVolume = yesterdayVolume;
            }

        public double getMarketCap()
            {
                return marketCap;
            }

        public void setMarketCap(double marketCap)
            {
                this.marketCap = marketCap;
            }

        public String getSymbol()
            {
                return symbol;
            }

        public void setSymbol(String symbol)
            {
                this.symbol = symbol;
            }

        public String getUnderlyingSymbol()
            {
                return underlyingSymbol;
            }

        public void setUnderlyingSymbol(String underlyingSymbol)
            {
                this.underlyingSymbol = underlyingSymbol;
            }

        public String getMarketCentre()
            {
                return marketCentre;
            }

        public void setMarketCentre(String marketCentre)
            {
                this.marketCentre = marketCentre;
            }

        public String getCompanyName()
            {
                return companyName;
            }

        public void setCompanyName(String companyName)
            {
                this.companyName = companyName;
            }

        public String getIsin()
            {
                return isin;
            }

        public void setIsin(String isin)
            {
                this.isin = isin;
            }

        public String getCusip()
            {
                return cusip;
            }

        public void setCusip(String cusip)
            {
                this.cusip = cusip;
            }

        public Date getExpirationDate()
            {
                return expirationDate;
            }

        public void setExpirationDate(Date expirationDate)
            {
                this.expirationDate = expirationDate;
            }

        public Date getUpdateTime()
            {
                return updateTime;
            }

        public void setUpdateTime(Date updateTime)
            {
                this.updateTime = updateTime;
            }

        public Date getTradeTime()
            {
                return tradeTime;
            }

        public void setTradeTime(Date tradeTime)
            {
                this.tradeTime = tradeTime;
            }

        public double getYesterdayClose()
            {
                return yesterdayClose;
            }

        public void setYesterdayClose(double yesterdayClose)
            {
                this.yesterdayClose = yesterdayClose;
            }

        public double getOpeningPrice()
            {
                return openingPrice;
            }

        public void setOpeningPrice(double openingPrice)
            {
                this.openingPrice = openingPrice;
            }

        public double getStrikePrice()
            {
                return strikePrice;
            }

        public void setStrikePrice(double strikePrice)
            {
                this.strikePrice = strikePrice;
            }

        public double getDayHigh()
            {
                return dayHigh;
            }

        public void setDayHigh(double dayHigh)
            {
                this.dayHigh = dayHigh;
            }

        public double getDayLow()
            {
                return dayLow;
            }

        public void setDayLow(double dayLow)
            {
                this.dayLow = dayLow;
            }

        public double getAsk()
            {
                return ask;
            }

        public void setAsk(double ask)
            {
                this.ask = ask;
            }

        public double getBid()
            {
                return bid;
            }

        public void setBid(double bid)
            {
                this.bid = bid;
            }

        public double getLastPrice()
            {
                return lastPrice;
            }

        public void setLastPrice(double lastPrice)
            {
                this.lastPrice = lastPrice;
            }

        public double getChange()
            {
                return change;
            }

        public void setChange(double change)
            {
                this.change = change;
            }

        public double getOpenInterest()
            {
                return openInterest;
            }

        public void setOpenInterest(double openInterest)
            {
                this.openInterest = openInterest;
            }

        public double getHigh52week()
            {
                return high52week;
            }

        public void setHigh52week(double high52week)
            {
                this.high52week = high52week;
            }

        public double getLow52week()
            {
                return low52week;
            }

        public void setLow52week(double low52week)
            {
                this.low52week = low52week;
            }

        public double getPriceEarningRatio()
            {
                return priceEarningRatio;
            }

        public void setPriceEarningRatio(double priceEarningRatio)
            {
                this.priceEarningRatio = priceEarningRatio;
            }

        public long getAskSize()
            {
                return askSize;
            }

        public void setAskSize(long askSize)
            {
                this.askSize = askSize;
            }

        public long getBidSize()
            {
                return bidSize;
            }

        public void setBidSize(long bidSize)
            {
                this.bidSize = bidSize;
            }

        public long getVolume()
            {
                return volume;
            }

        public void setVolume(long volume)
            {
                this.volume = volume;
            }

        public long getContractSize()
            {
                return contractSize;
            }

        public void setContractSize(long contractSize)
            {
                this.contractSize = contractSize;
            }

        public long getAverageVolume30()
            {
                return averageVolume30;
            }

        public void setAverageVolume30(long averageVolume30)
            {
                this.averageVolume30 = averageVolume30;
            }

        public int getIndicator()
            {
                return indicator;
            }

        public void setIndicator(int indicator)
            {
                this.indicator = indicator;
            }

        public int getSubMarket()
            {
                return subMarket;
            }

        public void setSubMarket(int subMarket)
            {
                this.subMarket = subMarket;
            }

        public int getBidTick()
            {
                return bidTick;
            }

        public void setBidTick(int bidTick)
            {
                this.bidTick = bidTick;
            }

        public int getPrecision()
            {
                return precision;
            }

        public void setPrecision(int precision)
            {
                this.precision = precision;
            }

        public boolean isDelayed()
            {
                return delayed;
            }

        public void setDelayed(boolean delayed)
            {
                this.delayed = delayed;
            }

        public boolean isNotFound()
            {
                return notFound;
            }

        public void setNotFound(boolean notFound)
            {
                this.notFound = notFound;
            }

        public boolean isOption()
            {
                return option;
            }

        public void setOption(boolean option)
            {
                this.option = option;
            }

        public Quote getUnderlying()
            {
                return underlying;
            }

        public void setUnderlying(Quote underlying)
            {
                this.underlying = underlying;
            }

        public double getChangePercent()
            {
                return changePercent;
            }

        public void setChangePercent(double changePercent)
            {
                this.changePercent = changePercent;
            }

        public double getBeta()
            {
                return beta;
            }

        public void setBeta(double beta)
            {
                this.beta = beta;
            }

        public double getTrailing12MonthsEps()
            {
                return (priceEarningRatio == 0 ? 0 : lastPrice / priceEarningRatio);
            }

        public String getStandardAndPoorsRating()
            {
                return standardAndPoorsRating;
            }

        public void setStandardAndPoorsRating(String standardAndPoorsRating)
            {
                this.standardAndPoorsRating = standardAndPoorsRating;
            }

        public String getGics()
            {
                return gics;
            }

        public void setGics(String gics)
            {
                this.gics = gics;
            }

        public Date getCalendarYearHighDate()
            {
                return calendarYearHighDate;
            }

        public void setCalendarYearHighDate(Date calendarYearHighDate)
            {
                this.calendarYearHighDate = calendarYearHighDate;
            }

        public Date getCalendarYearLowDate()
            {
                return calendarYearLowDate;
            }

        public void setCalendarYearLowDate(Date calendarYearLowDate)
            {
                this.calendarYearLowDate = calendarYearLowDate;
            }

        public Date getWeek52HighDate()
            {
                return week52HighDate;
            }

        public void setWeek52HighDate(Date week52HighDate)
            {
                this.week52HighDate = week52HighDate;
            }

        public Date getWeek52LowDate()
            {
                return week52LowDate;
            }

        public void setWeek52LowDate(Date week52LowDate)
            {
                this.week52LowDate = week52LowDate;
            }

        public double getVwap()
            {
                return vwap;
            }

        public void setVwap(double vwap)
            {
                this.vwap = vwap;
            }

        public double getVwap1()
            {
                return vwap1;
            }

        public void setVwap1(double vwap1)
            {
                this.vwap1 = vwap1;
            }

        public long getImbalanceVolume()
            {
                return imbalanceVolume;
            }

        public void setImbalanceVolume(long imbalanceVolume)
            {
                this.imbalanceVolume = imbalanceVolume;
            }

        public String getMic()
            {
                return mic;
            }

        public void setMic(String mic)
            {
                this.mic = mic;
            }

        public boolean isMini()
            {
                return mini;
            }

        public void setMini(boolean mini)
            {
                this.mini = mini;
            }

        public boolean isCorpAct()
            {
                return corpAct;
            }

        public void setCorpAct(boolean corpAct)
            {
                this.corpAct = corpAct;
            }

        public boolean isJumbo()
            {
                return jumbo;
            }

        public void setJumbo(boolean jumbo)
            {
                this.jumbo = jumbo;
            }

        public String getCurrency()
            {
                return currency;
            }

        public void setCurrency(String currency)
            {
                this.currency = currency;
            }

        public String getSedol()
            {
                return sedol;
            }

        public void setSedol(String sedol)
            {
                this.sedol = sedol;
            }

        public String getSource()
            {
                return source;
            }

        public void setSource(String source)
            {
                this.source = source;
            }

        public void setMutualFund(boolean mutualFund)
            {
                this.mutualFund = mutualFund;
            }

        public boolean isBond()
            {
                return bond;
            }

        public void setBond(boolean bond)
            {
                this.bond = bond;
            }

        public String getNativeCompanyName() {
            return nativeCompanyName;
        }

        public void setNativeCompanyName(String nativeCompanyName) {
            this.nativeCompanyName = nativeCompanyName;
        }

        public double getAskYield()
            {
                return askYield;
            }

        public Quote setAskYield(double askYield)
            {
                this.askYield = askYield;
                return this;
            }

        public double getBidYield()
            {
                return bidYield;
            }

        public Quote setBidYield(double bidYield)
            {
                this.bidYield = bidYield;
                return this;
            }

        public long getMinAskSize()
            {
                return minAskSize;
            }

        public Quote setMinAskSize(long minAskSize)
            {
                this.minAskSize = minAskSize;
                return this;
            }

        public long getMinBidSize()
            {
                return minBidSize;
            }

        public Quote setMinBidSize(long minBidSize)
            {
                this.minBidSize = minBidSize;
                return this;
            }

        public String getQuoteId()
            {
                return quoteId;
            }

        public Quote setQuoteId(String quoteId)
            {
                this.quoteId = quoteId;
                return this;
            }

        public long getIncrement()
            {
                return increment;
            }

        public Quote setIncrement(long increment)
            {
                this.increment = increment;
                return this;
            }
    }
