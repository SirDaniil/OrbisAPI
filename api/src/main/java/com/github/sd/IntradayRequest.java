package com.github.sd;

import java.text.*;
import java.util.*;

public class IntradayRequest extends HashMap<String, Object>
    {
        private IntradayRequest()
            { }

        public static IntradayRequest Create()
            {
                return new IntradayRequest();
            }

        public IntradayRequest symbol(String symbol)
            {
                put("symbol", symbol);
                return this;
            }

        public IntradayRequest from(Date date)
            {
                return from(new SimpleDateFormat("MM/dd/yyyy").format(date));
            }

        public IntradayRequest from(String date)
            {
                put("from", date);
                return this;
            }

        public IntradayRequest to(Date date)
            {
                return to(new SimpleDateFormat("MM/dd/yyyy").format(date));
            }

        public IntradayRequest to(String date)
            {
                put("to", date);
                return this;
            }
    }
