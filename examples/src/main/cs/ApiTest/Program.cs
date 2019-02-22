using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OrbisAPI;

namespace ApiTest
{
    class Program
    {
        static void Main(string[] args)
        {
            API api = new API();
            api.Credentials = new PublicKey(System.Configuration.ConfigurationManager.AppSettings["AccessKey"] + ".pem");
            api.Hostname = "https://portal.orbisfn.com";

            Console.WriteLine(api.Get("/quotes/equity", "symbols", "msft"));
            Console.ReadLine();
        }
    }
}
