package com.github;

/**
 * User: Daniil Sosonkin
 * Date: 1/16/2018 9:56 AM
 */
public class ModelEquityOrder
    {
        private PortfolioModel model;
        private EquityOrder order;

        public PortfolioModel getModel()
            {
                return model;
            }

        public void setModel(PortfolioModel model)
            {
                this.model = model;
            }

        public EquityOrder getOrder()
            {
                return order;
            }

        public void setOrder(EquityOrder order)
            {
                this.order = order;
            }
    }
