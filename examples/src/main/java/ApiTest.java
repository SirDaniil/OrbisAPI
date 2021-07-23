import java.io.*;
import java.util.*;
import com.github.sd.*;

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
                api.setCredentials(new PublicKeyTokenCredentials(props));
                var data = api.getChartIntraday( IntradayRequest.Create().symbol("NVDA").from("06/21/2021").to("07/21/2021") );
                for (int i = 0; i < data.length(); i++) {
                    var point = data.getJSONObject(i);
                    System.out.printf("%s - price: %,.2f%n", point.getString("date"), point.getDouble("price"));
                }

                /*api.getQuotes("amzn");
                api.getQuotes("amzn");
                api.getQuotes("amzn");
                api.getQuotes("amzn");
                api.getQuotes("amzn");
                api.getQuotes("amzn");
                api.getQuotes("amzn");*/
                //System.out.println((Object)api.get(() -> "/quotes/equity/shortability/{symbol}", "{symbol}", "MSFT"));
                //System.out.println(api.getQuotes("amzn").toString(2));
                //System.out.println(((JSONArray)api.get(ResearchMarketDates)).toString(2));
                //System.out.println(((JSONObject)api.get(ResearchMarketDateLastOpen)).toString(2));
                //System.out.println(((JSONObject)api.get(ResearchMarketDateCheck)).toString(2));
                //System.out.println(((JSONObject)api.get(OrbisAPI.Endpoint.Research, "{symbol}", "baba")).toString(2));
                //System.out.println(api.getCorporateActionTypes());
                //System.out.println(api.corporateActionSearch(CorporateActionSearch.Builder().symbol("msft,jnj").type("Dividend").dateFrom("2019-01-01").dateTo("2019-10-01")).toString(2));
                //System.out.println(api.news(NewsFilter.Builder().filter( new FilterKey(FilterKey.Provider).add(Providers.Sec).add(Providers.TenkWiz) )).toString(2));
                //System.out.println(api.getChartHistorical("AAPL", "1y").toString(2));
                //System.out.println(api.news("AAPL"));
                /*System.out.println(api.news(NewsFilter.Builder().filter(FilterKey.Provider.add(Providers.Sec).exclude())).toString(2));
                System.out.println(api.getFundamentals("IncomeStatement", "MSFT").toString(2));
                System.out.println(api.getFundamentalTypes().toString(2));
                System.out.println(api.getAdrsTop10Defaults().toString(2));
                System.out.println(api.getAdrsTop10(AdrRequest.Builder().country("CAN")).toString(2));
                System.out.println(api.getAdrs(AdrRequest.Builder().loadEarningReleases().loadUpgradesDowngrades().country("CHE")).toString(2));
                System.out.println(api.screener(Screener.Builder().is(Adr).gte(MarketCap, 10, new DateRange().lowerToday().upperNextMonth())).toString(2));*/

                /*final var count = new AtomicLong();
                for (int i = 0; i < 3; i++)
                    new Thread(() -> {
                        System.out.println("Starting...");
                        while (true)
                            try
                                {
                                    //api.getQuotes("msft");
                                    api.get(ResearchMarketDateLastOpen);
                                    count.incrementAndGet();
                                }
                            catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                    }).start();

                var fmt = DecimalFormat.getInstance();
                long start = System.currentTimeMillis();
                while (true) {
                    long now = System.currentTimeMillis();
                    long delta = now - start;
                    if (delta < 30_000)
                        continue;

                    long num = count.getAndSet(0);
                    double rps = num / (delta / 1000.0);
                    System.out.println(num + " requests in " + delta + " = " + fmt.format(rps) + "/s");
                    start = now;
                }*/
            }
    }
