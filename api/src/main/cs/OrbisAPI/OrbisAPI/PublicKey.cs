using BlackyPaw.Crypto;
using Jose;
using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;

namespace OrbisAPI
{
    public class PublicKey : ICredentials
    {
        public string Filename { get; set; }

        public PublicKey(string filename)
        {
            this.Filename = filename;
        }

        public string GetScheme()
        {
            return "Key";
        }

        public string GetToken()
        {
            var publicKey = File.ReadAllText(Filename);
            var pos = Filename.LastIndexOf('.');
            var accessKey = Filename.Substring(0, pos);
            var payload = new Dictionary<string, object>()
                {
                    { "iss" , "MyClient" },
                    { "exp", DateTimeOffset.Now.ToUnixTimeSeconds() + 30 },
                    { "iat" , DateTimeOffset.Now.ToUnixTimeSeconds() },
                    { "jti" , Guid.NewGuid().ToString() }
                };

            publicKey = publicKey.Replace("\n", "");
            publicKey = publicKey.Replace("\r", "");
            publicKey = publicKey.Replace("-----BEGIN PUBLIC KEY-----", "");
            publicKey = publicKey.Replace("-----END PUBLIC KEY-----", "");
            var publicKeyBytes = Convert.FromBase64String(publicKey);

            MemoryStream stream = new MemoryStream(publicKeyBytes);
            X509EncodedPublicKeyImporter importer = X509EncodedPublicKeyImporter.ImportFromDER(stream);
            CngKey rsaKey = ((RSAPublicKeyImporter)importer).ToCngKey();

            var extraHeaders = new Dictionary<string, object>();
            extraHeaders.Add("kid", accessKey);

            return JWT.Encode(payload, rsaKey, JweAlgorithm.RSA_OAEP_256, JweEncryption.A128GCM, extraHeaders: extraHeaders);
        }
    }
}
