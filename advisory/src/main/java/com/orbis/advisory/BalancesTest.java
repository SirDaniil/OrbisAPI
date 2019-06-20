package com.orbis.advisory;

import java.io.*;
import com.github.sd.*;

/**
 * User: Daniil Sosonkin
 * Date: 6/20/2019 12:55 PM
 */
public class BalancesTest extends Advisory
    {
        public static void main(String[] args) throws IOException
            {
                OrbisAPI api = createAPI();
                String account = "5VA05003";

                print("## trading limit ##");
                print(api.get(AdvisoryEndpoints.UserBuyingPower, "account", account));

                print("");
                print("## RTB ##");
                print(api.get(AdvisoryEndpoints.UserBalance, "account", account));
            }
    }
