package com.orbis.advisory;

import java.io.*;
import com.github.sd.*;
import org.json.*;
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
                JSONArray allocations = api.get(AdvisoryModelDrift, "{modelId}", 1);
                print(allocations);
                for (int i = 0; i < allocations.length(); i++)
                    {
                        JSONObject allocation = allocations.getJSONObject(i);
                        JSONObject quote = allocation.getJSONObject("quote");
                        JSONArray targets = allocation.getJSONArray("targets");

                        print("Quote: " + quote.getString("symbol"));
                        print("Transaction: " + allocation.getString("transaction"));
                        print("Quantity: " + allocation.getDouble("quantity"));
                        for (int j = 0; j < targets.length(); j++)
                            {
                                JSONObject target = targets.getJSONObject(j);
                                print("\tAccount: " + target.getJSONObject("account"));
                                print("\tCurrent: " + target.optDouble("currentPct") * 100 + "%");
                                print("\tExpected: " + target.optDouble("expectedPct") * 100 + "%");
                                print("\tBalance: " + target.optJSONObject("rtb"));
                                print("\tDrift: " + target.optDouble("drift") * 100 + "%");
                                print("\tQty: " + target.getDouble("quantity"));
                            }
                    }
            }
    }
