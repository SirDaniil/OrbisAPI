const jose = require('node-jose');
const { v4: uuidv4 } = require('uuid');
const axios = require('axios');
const fs = require('fs')

const {
    URL,
    ACCESS_KEY,
    FILE_KEY
} = process.env

const main = async() => {
    let keystore = jose.JWK.createKeyStore();
    let pemInput = fs.readFileSync(FILE_KEY);
    let key2use = await keystore.add(pemInput, "pem", {"kid": ACCESS_KEY});

    console.log(key2use);
    console.log(key2use.algorithms("wrap"));

    let content2encrypt= JSON.stringify({
        "iat": Math.floor(Date.now() / 1000),
        "exp": Math.floor(Date.now() / 1000) + 60,
        "nbf": Math.floor(Date.now() / 1000),
        "iss": 'Sample API v1.0',
        "jti": uuidv4(),
    });

    let encryptedContent = await jose.JWE.createEncrypt({
        format: 'compact',
        fields: {
            alg: 'RSA-OAEP-256',
            enc: 'A128GCM',
            kid: ACCESS_KEY
        }
    }, key2use)
        .update(content2encrypt)
        .final();

    let JWE = Buffer.from(encryptedContent).toString('base64')
    try {
        const resp = await axios.get(URL, {
            headers: {
                'Authorization': `Key ${JWE}`,
                'Accept-Encoding': 'gzip'
            }
        });
        console.log(resp)
    } catch(err) {
        console.log(err)
    }
};

main()