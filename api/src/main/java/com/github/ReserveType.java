package com.github;

/**
 * User: Daniil Sosonkin
 * Date: 11/27/2017 10:04 AM
 */
public enum ReserveType
    {
        DOLLAR('$'), PERCENTAGE('%');

        private final char indicator;

        ReserveType(char indicator)
            {
                this.indicator = indicator;
            }

        public char getIndicator()
            {
                return indicator;
            }

        public static ReserveType fromChar(char ch)
            {
                for (ReserveType type : values())
                    if (type.indicator == ch)
                        return type;

                return null;
            }
    }