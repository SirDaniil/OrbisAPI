package com.github;

/**
 * Daniil Sosonkin
 * Jul 16, 2008 10:05:22 AM
 */
public enum Transaction
    {
        BUY(true, "B"),
        SELL("S"),
        SHORT_SELL(true, "SS"),
        BUY_TO_COVER("BC"),
        BUY_TO_OPEN(true, "BTO"),
        SELL_TO_CLOSE("BTC"),
        SELL_TO_OPEN(true, "STO"),
        BUY_TO_CLOSE("BTC");

        private boolean acquiring;
        private String abbreviation;

        Transaction(boolean acquiring, String abbreviation)
            {
                this.acquiring = acquiring;
                this.abbreviation = abbreviation;
            }

        Transaction(String abbreviation)
            {
                this.abbreviation = abbreviation;
            }

        public boolean isAcquiring()
            {
                return acquiring;
            }

        public boolean isLiquidating()
            {
                return !acquiring;
            }

        public boolean isSame(String transaction)
            {
                return abbreviation.equalsIgnoreCase(transaction);
            }

        public static Transaction fromCode(String code)
            {
                for (Transaction trx : values())
                    if (trx.abbreviation.equals(code))
                        return trx;

                return null;
            }
    }
