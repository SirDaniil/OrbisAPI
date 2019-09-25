package com.orbis.advisory;

import java.io.*;
import com.github.*;
import com.github.sd.*;
import org.json.*;
import static com.orbis.advisory.AdvisoryEndpoints.*;

/**
 * User: Daniil Sosonkin
 * Date: 7/22/2019 9:16 AM
 */
public class AllocationsTest extends Advisory
    {
        public static void main(String[] args) throws IOException
            {
                OrbisAPI api = createAPI();

                // schedule the allocation
                JSONObject trclient1 = new JSONObject();
                trclient1.put("account", new JSONObject().put("accountNumber", "TRCLIENT1"));
                trclient1.put("quantity", 500);

                JSONObject trclient2 = new JSONObject();
                trclient2.put("account", new JSONObject().put("accountNumber", "TRCLIENT2"));
                trclient2.put("quantity", 500);

                JSONObject allocation = new JSONObject();
                allocation.put("quote", new JSONObject().put("symbol", "F"));
                allocation.put("transaction", Transaction.SELL);
                allocation.put("targets", new JSONArray().put(trclient1).put(trclient2));

                var request = new JSONObject().put("allocation", allocation);
                print(request);
                JSONObject allocRef = api.post(AllocationPreallocate, request);
                System.out.println("Preallocated: " + allocRef);

                var validation = api.post(AllocationValidate, allocRef);
                System.out.println("Validated: " + validation);


                var trigger = api.post(AllocationTrigger, allocRef);
                System.out.println("Triggered: " + trigger);

                /*var cancellation = api.post(AllocationCancel, allocRef);
                System.out.println("Cancelled: " + cancellation);*/
            }
    }
