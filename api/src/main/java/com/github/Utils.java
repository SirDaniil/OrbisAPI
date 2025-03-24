package com.github;

import java.security.*;

public class Utils
    {
        private static final String HEX_DIGITS = "0123456789abcdef";

        private Utils()
            { }

        public static String md5(String value)
            {
                return (value == null ? null : md5(value.getBytes()));
            }

        public static String md5(byte[] value)
            {
                if (value == null)
                    return null;

                try
                    {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(value);
                        byte[] md5 = md.digest();

                        return toHexString(md5).toString();
                    }
                catch (NoSuchAlgorithmException e)
                    {
                        return null;
                    }
            }

        @SuppressWarnings({"ForLoopReplaceableByForEach"})
        public static StringBuilder toHexString(byte[] v)
            {
                StringBuilder buf = new StringBuilder(v.length * 2);
                for (int i = 0; i < v.length; i++)
                    {
                        int b = v[i] & 0xFF;
                        buf.append(HEX_DIGITS.charAt(b >>> 4));
                        buf.append(HEX_DIGITS.charAt(b & 0xF));
                    }

                return buf;
            }
    }
