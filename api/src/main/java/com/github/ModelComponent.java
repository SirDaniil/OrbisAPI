package com.github;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * 11/27/2017 10:30 AM
 */
public class ModelComponent
    {
        private ComponentType type;
        private PortfolioModel model;
        private Quote quote;
        private Date addedOn;
        private String symbol;
        private String tag;
        private String note;
        private boolean option;
        private double percentage;

        public ComponentType getType()
            {
                return type;
            }

        public void setType(ComponentType type)
            {
                this.type = type;
            }

        public PortfolioModel getModel()
            {
                return model;
            }

        public void setModel(PortfolioModel model)
            {
                this.model = model;
            }

        public Quote getQuote()
            {
                return quote;
            }

        public void setQuote(Quote quote)
            {
                this.quote = quote;
            }

        public Date getAddedOn()
            {
                return addedOn;
            }

        public void setAddedOn(Date addedOn)
            {
                this.addedOn = addedOn;
            }

        public String getSymbol()
            {
                return symbol;
            }

        public boolean isOption()
            {
                return option;
            }

        public void setSymbol(String symbol)
            {
                this.symbol = symbol;
            }

        public double getPercentage()
            {
                return percentage;
            }

        public void setPercentage(double percentage)
            {
                this.percentage = percentage;
            }

        public void setOption(boolean option)
            {
                this.option = option;
            }

        public String getNote()
            {
                return note;
            }

        public void setNote(String note)
            {
                this.note = note;
            }

        public String getTag()
            {
                return tag;
            }

        public void setTag(String tag)
            {
                this.tag = tag;
            }
    }
