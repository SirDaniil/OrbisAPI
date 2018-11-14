package com.github;

/**
 * User: Daniil Sosonkin
 * Date: 10/12/2018 3:36 PM
 */
public enum FractionalSharePolicy
    {
        RoundDown('D'), RoundUp('U'), Fractional('~');

        private final char code;

        FractionalSharePolicy(char code)
            {
                this.code = code;
            }

        public static FractionalSharePolicy fromCode(char code)
            {
                for (FractionalSharePolicy policy : values())
                    if (policy.code == code)
                        return policy;

                return RoundDown;
            }
    }
