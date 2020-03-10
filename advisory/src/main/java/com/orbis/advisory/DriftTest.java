package com.orbis.advisory;

import java.io.*;
import com.github.sd.*;
import static com.orbis.advisory.AdvisoryEndpoints.*;

/**
 * User: Daniil Sosonkin
 * Date: 3/10/2020 5:06 PM
 */
public class DriftTest extends Advisory
    {
        public static void main(String[] args) throws IOException
            {
                OrbisAPI api = createAPI();
                print(api.get(AdvisoryModelDrift, "{modelId}", 1));
            }
    }
