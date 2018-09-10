import java.io.*;
import com.github.sd.*;
import org.json.*;

/**
 * Created by Daniil Sosonkin
 * 6/15/2018 3:27 PM
 */
public class BasicTests implements Credentials
    {
        public static void main(String[] args) throws IOException
            {


                OrbisAPI api = new OrbisAPI();
                api.setHostname("https://wrh.orbisfn.net");
                api.setCredentials(new BasicTests());
            }

        @Override
        public String getToken()
            {
                return "";
            }

        @Override
        public String getScheme()
            {
                return "OAuth2";
            }
    }
