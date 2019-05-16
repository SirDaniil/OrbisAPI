import java.io.*;
import java.util.*;
import com.github.sd.*;

/**
 * Created by Daniil Sosonkin
 * 5/15/2019 9:30 PM
 */
public class C2CTest
    {
        public static void main(String[] args) throws IOException
            {
                var props = new Properties();
                try (var in = new FileInputStream("c2c.properties"))
                    {
                        props.load(in);
                    }

                var hostname = props.getProperty("api.hostname");
                var c2c = new C2C();
                c2c.setEntity(props.getProperty("c2c.entity"));
                c2c.setGroup(props.getProperty("c2c.group"));
                c2c.setSecret(props.getProperty("c2c.secret"));
                c2c.setHostname(hostname);

                OrbisAPI api = new OrbisAPI();
                api.setCredentials(c2c);
                api.setHostname(hostname);
                System.out.println(api.getQuotes("msft,goog").toString(2));
                System.out.println(api.getChartHistorical("googl", "1y").toString(2));
            }
    }
