import java.io.*;
import java.util.*;
import com.github.sd.*;
import com.github.sd.FilterValue.*;

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

                //System.out.println(api.news(NewsFilter.Builder().filter(FilterKey.Provider.add(Providers.Sec).exclude())).toString(2));
                //System.out.println(api.getFundamentals("IncomeStatement", "MSFT").toString(2));
                System.out.println(api.getQuotes("msft").toString(2));
            }
    }
