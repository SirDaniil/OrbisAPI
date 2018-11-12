package com.github;

/**
 * User: Daniil Sosonkin
 * Date: 11/12/2018 3:19 PM
 */
public class AllocationRequest
    {
        private ModelAdjustment adjustment;
        private Allocation allocation;

        public ModelAdjustment getAdjustment()
            {
                return adjustment;
            }

        public AllocationRequest setAdjustment(ModelAdjustment adjustment)
            {
                this.adjustment = adjustment;
                return this;
            }

        public Allocation getAllocation()
            {
                return allocation;
            }

        public AllocationRequest setAllocation(Allocation allocation)
            {
                this.allocation = allocation;
                return this;
            }
    }

