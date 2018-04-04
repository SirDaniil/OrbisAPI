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
            }
    }
