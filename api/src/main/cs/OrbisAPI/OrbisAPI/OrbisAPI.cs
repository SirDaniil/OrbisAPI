using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace OrbisAPI
{
    public interface ICredentials
    {
        string GetToken();
        string GetScheme();
    }

    public class API
    {
        private string hostname;
        public ICredentials Credentials { get; set; }
        public string Uri { get; set; }
        public string Scheme { get; set; }
        public string Hostname
        {
            get
            {
                return hostname;
            }

            set
            {
                if (value.StartsWith("http://"))
                {
                    hostname = value.Substring("http://".Length);
                    Scheme = "http";
                }

                if (value.StartsWith("https://"))
                {
                    hostname = value.Substring("https://".Length);
                    Scheme = "https";
                }
            }
        }

        public API()
        {
            this.Uri = "/api";
            this.Scheme = "https";
        }

        public string Get(string endpoint, string key, object value)
        {
            Dictionary<string, object> args = new Dictionary<string, object>();
            args[key] = value;

            return Get(endpoint, args);
        }

        public string Get(string endpoint, Dictionary<string, object> args)
        {
            if (!endpoint.StartsWith("/"))
                endpoint = "/" + endpoint;

            string path = Uri + endpoint;
            if (args != null && args.Count > 0)
            {
                path += "?";
                foreach (KeyValuePair<string, object> pair in args)
                {
                    object value = pair.Value;
                    if (value == null)
                        value = "";

                    path += pair.Key + "=" + value + "&";
                }
            }

            var client = new HttpClient();
            client.BaseAddress = new Uri(Scheme + "://" + Hostname);

            var bytes = Encoding.ASCII.GetBytes(Credentials.GetToken());
            client.DefaultRequestHeaders.Add("Authorization", Credentials.GetScheme() + " " + Convert.ToBase64String(bytes));
            var response = client.GetAsync(path).Result;
            var content = response.Content.ReadAsStringAsync();

            return content.Result;
        }
    }
}
