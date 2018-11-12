package com.github;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 1/16/2018 1:36 PM
 */
public class ModelAdjustment
    {
        private long id;
        private long modelId;
        private double sourcePct;
        private double targetPct;
        private String ourRef;
        private String market;
        private AdjustmentStatus status;
        private Quote quote;
        private Date createdOn;
        private Date activatedOn;

        public ModelAdjustment setQuote(Quote quote)
            {
                this.quote = quote;
                return this;
            }

        public Quote getQuote()
            {
                return quote;
            }

        public String getSymbol()
            {
                return quote.getSymbol();
            }

        public boolean isOption()
            {
                return quote.isOption();
            }

        public long getId()
            {
                return id;
            }

        public ModelAdjustment setId(long id)
            {
                this.id = id;
                return this;
            }

        public double getSourcePct()
            {
                return sourcePct;
            }

        public ModelAdjustment setSourcePct(double sourcePct)
            {
                this.sourcePct = sourcePct;
                return this;
            }

        public double getTargetPct()
            {
                return targetPct;
            }

        public ModelAdjustment setTargetPct(double targetPct)
            {
                this.targetPct = targetPct;
                return this;
            }

        public String getOurRef()
            {
                return ourRef;
            }

        public ModelAdjustment setOurRef(String ourRef)
            {
                this.ourRef = ourRef;
                return this;
            }

        public String getMarket()
            {
                return market;
            }

        public ModelAdjustment setMarket(String market)
            {
                this.market = market;
                return this;
            }

        public AdjustmentStatus getStatus()
            {
                return status;
            }

        public ModelAdjustment setStatus(AdjustmentStatus status)
            {
                this.status = status;
                return this;
            }

        public Date getCreatedOn()
            {
                return createdOn;
            }

        public ModelAdjustment setCreatedOn(Date createdOn)
            {
                this.createdOn = createdOn;
                return this;
            }

        public Date getActivatedOn()
            {
                return activatedOn;
            }

        public ModelAdjustment setActivatedOn(Date activatedOn)
            {
                this.activatedOn = activatedOn;
                return this;
            }

        public long getModelId()
            {
                return modelId;
            }

        public ModelAdjustment setModelId(long modelId)
            {
                this.modelId = modelId;
                return this;
            }

    }
