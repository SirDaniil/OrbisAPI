package com.github;

/**
 * User: Daniil Sosonkin
 * Date: 1/16/2018 9:56 AM
 */
public class AdvisoryOrder<T extends ASingleOrder>
    {
        private Boolean inventory;
        private PortfolioModel model;
        private T order;

        public PortfolioModel getModel()
            {
                return model;
            }

        public void setModel(PortfolioModel model)
            {
                this.model = model;
                this.inventory = null;
            }

        public T getOrder()
            {
                return order;
            }

        public void setOrder(T order)
            {
                this.order = order;
            }

        public Boolean getInventory()
            {
                return inventory;
            }

        public void setInventory(Boolean inventory)
            {
                this.inventory = inventory;
                this.model = null;
            }
    }
