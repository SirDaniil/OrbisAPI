package com.github;

import java.util.*;

/**
 * User: Daniil Sosonkin
 * Date: 2/8/2017 6:15 PM
 */
public class PortfolioModel
    {
        private String title;
        private String description;
        private ReserveType reserveType = ReserveType.DOLLAR;
        private FractionalSharePolicy fractionalSharePolicy = FractionalSharePolicy.RoundDown;
        private List<ModelComponent> components = new ArrayList<>();
        private double minimum;
        private double reserve;
        private long inventoryId;
        private long id;
        private boolean robo;
        private boolean roboActive;

        public static PortfolioModel Builder()
            {
                return new PortfolioModel();
            }

        public List<ModelComponent> getComponents()
            {
                return components;
            }

        public String getTitle()
            {
                return title;
            }

        public PortfolioModel setTitle(String title)
            {
                this.title = title;
                return this;
            }

        public String getDescription()
            {
                return description;
            }

        public PortfolioModel setDescription(String description)
            {
                this.description = description;
                return this;
            }

        public double getMinimum()
            {
                return minimum;
            }

        public PortfolioModel setMinimum(double minimum)
            {
                this.minimum = minimum;
                return this;
            }

        public double getReserve()
            {
                return reserve;
            }

        public PortfolioModel setReserve(double reserve)
            {
                this.reserve = reserve;
                return this;
            }

        public long getId()
            {
                return id;
            }

        public PortfolioModel setId(long id)
            {
                this.id = id;
                return this;
            }

        public long getInventoryId()
            {
                return inventoryId;
            }

        public PortfolioModel setInventoryId(long inventoryId)
            {
                this.inventoryId = inventoryId;
                return this;
            }

        public ReserveType getReserveType()
            {
                return reserveType;
            }

        public PortfolioModel setReserveType(ReserveType reserveType)
            {
                this.reserveType = reserveType;
                return this;
            }

        public PortfolioModel setRobo(boolean robo)
            {
                this.robo = robo;
                return this;
            }

        public boolean isRobo()
            {
                return robo;
            }

        public PortfolioModel setRoboActive(boolean roboActive)
            {
                this.roboActive = roboActive;
                return this;
            }

        public boolean isRoboActive()
            {
                return roboActive;
            }

        public FractionalSharePolicy getFractionalSharePolicy()
            {
                return fractionalSharePolicy;
            }

        public PortfolioModel setFractionalSharePolicy(FractionalSharePolicy fractionalSharePolicy)
            {
                this.fractionalSharePolicy = fractionalSharePolicy;
                return this;
            }
    }
