package com.orbis.advisory;

import java.io.*;
import com.github.sd.*;
import org.json.*;

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

                /*print("## trading limit ##");
                print(api.get(AdvisoryEndpoints.UserBuyingPower, "account", account));*/

                /*print("");
                print("## RTB ##");
                print(api.get(AdvisoryEndpoints.UserBalance, "account", account));*/

                /*print("");
                print("## Branch RTB ##");
                print(api.get(AdvisoryEndpoints.BranchRtb));*/

                /*print("");
                print("## Branch RTBs ##");
                print(api.get(AdvisoryEndpoints.BranchRtbs));*/

                /*print("");
                print("## Branch RTBs total ##");
                print(api.get(AdvisoryEndpoints.BranchRtbsTotal));*/

                JSONArray models = api.get(AdvisoryEndpoints.AdvisoryModels);
                for (int i = 0; i < models.length(); i++)
                    {
                        JSONObject model = models.getJSONObject(i);
                        long modelId = model.getLong("id");

                        print("\n## Model: " + model.getString("title") + " ##");
                        print("-- RTB --");
                        print(api.get(AdvisoryEndpoints.AdvisoryModelRtbs, "{modelId}", modelId));
                    }
            }
    }
