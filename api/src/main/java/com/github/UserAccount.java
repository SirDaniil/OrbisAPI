package com.github;

import java.util.*;

/**
 * Daniil Sosonkin
 * Nov 15, 2007 2:25:18 PM
 */
public class UserAccount
    {
        private long accountId;
        private String accountNumber;
        private String fullName;
        private String repCode;
        private String mpid;
        private String address;
        private String city;
        private String state;
        private String zip;
        private String country;
        private String phone1;
        private String phone2;
        private Date createdOn;
        private Date updatedOn;
        private int accountType;
        private int bpConfig;
        private int optionLevel;
        private char status;
        private boolean liquidationOnly;
        private boolean skipOptionsRisk;
        private boolean skipEquitiesRisk;
        private boolean allowNakedPuts;
        private boolean allowNakedCalls;

        public long getAccountId()
            {
                return accountId;
            }

        public UserAccount setAccountId(long accountId)
            {
                this.accountId = accountId;
                return this;
            }

        public String getAccountNumber()
            {
                return accountNumber;
            }

        public UserAccount setAccountNumber(String accountNumber)
            {
                this.accountNumber = accountNumber;
                return this;
            }

        public String getFullName()
            {
                return fullName;
            }

        public UserAccount setFullName(String fullName)
            {
                this.fullName = fullName;
                return this;
            }

        public String getRepCode()
            {
                return repCode;
            }

        public UserAccount setRepCode(String repCode)
            {
                this.repCode = repCode;
                return this;
            }

        public String getMpid()
            {
                return mpid;
            }

        public UserAccount setMpid(String mpid)
            {
                this.mpid = mpid;
                return this;
            }

        public String getAddress()
            {
                return address;
            }

        public UserAccount setAddress(String address)
            {
                this.address = address;
                return this;
            }

        public String getCity()
            {
                return city;
            }

        public UserAccount setCity(String city)
            {
                this.city = city;
                return this;
            }

        public String getState()
            {
                return state;
            }

        public UserAccount setState(String state)
            {
                this.state = state;
                return this;
            }

        public String getZip()
            {
                return zip;
            }

        public UserAccount setZip(String zip)
            {
                this.zip = zip;
                return this;
            }

        public String getCountry()
            {
                return country;
            }

        public UserAccount setCountry(String country)
            {
                this.country = country;
                return this;
            }

        public String getPhone1()
            {
                return phone1;
            }

        public UserAccount setPhone1(String phone1)
            {
                this.phone1 = phone1;
                return this;
            }

        public String getPhone2()
            {
                return phone2;
            }

        public UserAccount setPhone2(String phone2)
            {
                this.phone2 = phone2;
                return this;
            }

        public Date getCreatedOn()
            {
                return createdOn;
            }

        public UserAccount setCreatedOn(Date createdOn)
            {
                this.createdOn = createdOn;
                return this;
            }

        public Date getUpdatedOn()
            {
                return updatedOn;
            }

        public UserAccount setUpdatedOn(Date updatedOn)
            {
                this.updatedOn = updatedOn;
                return this;
            }

        public int getAccountType()
            {
                return accountType;
            }

        public UserAccount setAccountType(int accountType)
            {
                this.accountType = accountType;
                return this;
            }

        public int getBpConfig()
            {
                return bpConfig;
            }

        public UserAccount setBpConfig(int bpConfig)
            {
                this.bpConfig = bpConfig;
                return this;
            }

        public int getOptionLevel()
            {
                return optionLevel;
            }

        public UserAccount setOptionLevel(int optionLevel)
            {
                this.optionLevel = optionLevel;
                return this;
            }

        public char getStatus()
            {
                return status;
            }

        public UserAccount setStatus(char status)
            {
                this.status = status;
                return this;
            }

        public boolean isLiquidationOnly()
            {
                return liquidationOnly;
            }

        public UserAccount setLiquidationOnly(boolean liquidationOnly)
            {
                this.liquidationOnly = liquidationOnly;
                return this;
            }

        public boolean isSkipOptionsRisk()
            {
                return skipOptionsRisk;
            }

        public UserAccount setSkipOptionsRisk(boolean skipOptionsRisk)
            {
                this.skipOptionsRisk = skipOptionsRisk;
                return this;
            }

        public boolean isSkipEquitiesRisk()
            {
                return skipEquitiesRisk;
            }

        public UserAccount setSkipEquitiesRisk(boolean skipEquitiesRisk)
            {
                this.skipEquitiesRisk = skipEquitiesRisk;
                return this;
            }

        public boolean isAllowNakedPuts()
            {
                return allowNakedPuts;
            }

        public UserAccount setAllowNakedPuts(boolean allowNakedPuts)
            {
                this.allowNakedPuts = allowNakedPuts;
                return this;
            }

        public boolean isAllowNakedCalls()
            {
                return allowNakedCalls;
            }

        public UserAccount setAllowNakedCalls(boolean allowNakedCalls)
            {
                this.allowNakedCalls = allowNakedCalls;
                return this;
            }
    }
