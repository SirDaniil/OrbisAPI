package com.orbis.advisory;

import java.io.*;
import com.github.sd.*;

/**
 * User: Daniil Sosonkin
 * Date: 1/29/2020 1:11 PM
 */
public class AgreementsTest extends Advisory
    {
        public static void main(String[] args) throws IOException
            {
                OrbisAPI api = createAPI();
                /*print(api.get(AdvisoryEndpoints.UserAgreementsAvailable));
                print(api.get(AdvisoryEndpoints.UserAgreementsUnsigned));*/
                print(api.get(AdvisoryEndpoints.UserAgreement, "{version}", "v1", "{code}", "BATS"));
            }
    }
