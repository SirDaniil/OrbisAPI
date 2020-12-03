import java.io.*;
import com.github.sd.*;

public class CorporateActionsTest
    {
        public static void main(String[] args) throws IOException
            {
                var api = new OrbisAPI();
                api.setHostname("https://portal.orbisfn.com");
                api.setCredentials(new PublicKeyTokenCredentials().setFilename("37151790-cd2d-4020-9b18-aba07766adce.pem").setHostname("https://portal.orbisfn.com"));
                var rsp = api.corporateActionSearch(CorporateActionSearch.Builder().type("Dividend").dateFrom("12/01/2020"));
                System.out.println(rsp.toString(2));
            }
    }
