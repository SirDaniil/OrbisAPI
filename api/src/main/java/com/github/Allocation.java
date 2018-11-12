package com.github;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 5/9/2017 11:43 AM
 */
public class Allocation
    {
        private AllocationStatus status;
        private Transaction transaction;
        private List<Order> orders;
        private List<Order> targets;
        private Quote quote;
        private String allocationRef;
        private double avgPx;
        private double quantity;

        public AllocationStatus getStatus()
            {
                return status;
            }

        public Allocation setStatus(AllocationStatus status)
            {
                this.status = status;
                return this;
            }

        public Transaction getTransaction()
            {
                return transaction;
            }

        public Allocation setTransaction(Transaction transaction)
            {
                this.transaction = transaction;
                return this;
            }

        public List<Order> getOrders()
            {
                return orders;
            }

        public Allocation setOrders(List<Order> orders)
            {
                this.orders = orders;
                return this;
            }

        public List<Order> getTargets()
            {
                return targets;
            }

        public Allocation setTargets(List<Order> targets)
            {
                this.targets = targets;
                return this;
            }

        public Quote getQuote()
            {
                return quote;
            }

        public Allocation setQuote(Quote quote)
            {
                this.quote = quote;
                return this;
            }

        public String getAllocationRef()
            {
                return allocationRef;
            }

        public Allocation setAllocationRef(String allocationRef)
            {
                this.allocationRef = allocationRef;
                return this;
            }

        public double getAvgPx()
            {
                return avgPx;
            }

        public Allocation setAvgPx(double avgPx)
            {
                this.avgPx = avgPx;
                return this;
            }

        public double getQuantity()
            {
                return quantity;
            }

        public Allocation setQuantity(double quantity)
            {
                this.quantity = quantity;
                return this;
            }
    }
