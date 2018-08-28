import java.io.*;
import java.util.*;
import com.github.sd.*;
import static com.github.sd.Screener.Field.*;
import static com.github.sd.FilterValue.*;

/**
 * User: Daniil Sosonkin
 * Date: 4/4/2018 4:13 PM
 */
public class ApiTest
    {
        public static void main(String[] args) throws IOException
            {
                String settings = args.length > 0 ? args[0] : "settings.conf";
                Properties props = new Properties();
                try (InputStream in = new FileInputStream(settings))
                    {
                        props.load(in);
                    }

                OrbisAPI api = new OrbisAPI();
                api.setHostname(props.getProperty("hostname"));
                api.setCredentials(new PublicKeyCredentials(props.getProperty("key.file")));

                /*System.out.println(api.getCorporateActionTypes());
                System.out.println(api.corporateActionSearch(CorporateActionSearch.Builder().type("Dividend")).toString(2));*/
                System.out.println(api.news(NewsFilter.Builder().filter( new FilterKey(FilterKey.Provider).add(Providers.Sec).add(Providers.TenkWiz) )).toString(2));
                //System.out.println(api.getChartHistorical("AAPL", "1y").toString(2));
                //System.out.println(api.news("AAPL"));
                /*System.out.println(api.news(NewsFilter.Builder().filter(FilterKey.Provider.add(Providers.Sec).exclude())).toString(2));
                System.out.println(api.getFundamentals("IncomeStatement", "MSFT").toString(2));
                System.out.println(api.getQuotes("amzn").toString(2));
                System.out.println(api.getFundamentalTypes().toString(2));
                System.out.println(api.getAdrsTop10Defaults().toString(2));
                System.out.println(api.getAdrsTop10(AdrRequest.Builder().country("CAN")).toString(2));
                System.out.println(api.getAdrs(AdrRequest.Builder().loadEarningReleases().loadUpgradesDowngrades().country("CHE")).toString(2));
                System.out.println(api.screener(Screener.Builder().is(Adr).gte(MarketCap, 10, new DateRange().lowerToday().upperNextMonth())).toString(2));*/
            }
    }
