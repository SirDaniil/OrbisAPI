package com.github;

/**
 * User: Daniil Sosonkin
 * Date: 1/16/2018 9:56 AM
 */
public class AccountOrder<T extends ASingleOrder>
    {
        private UserAccount account;
        private T order;

        public T getOrder()
            {
                return order;
            }

        public void setOrder(T order)
            {
                this.order = order;
            }

        public UserAccount getAccount()
            {
                return account;
            }

        public void setAccount(UserAccount account)
            {
                this.account = account;
            }
    }
