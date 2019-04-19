const jws = require('jws');
const got = require('got');
const uuidv4 = require('uuid/v4');
const ini = require('ini');
const fs = require('fs');

const config = ini.parse(fs.readFileSync('c2c.properties', 'utf-8'));
const key = jws.sign({
    header: {
        alg: 'HS256'
    },
    payload: {
        jid: uuidv4(),
        iat: Math.floor(Date.now() / 1000),
        eat: Math.floor(Date.now() / 1000) + 60 * 1000,
        entity: config['c2c.entity'],
        group: config['c2c.group']
    },
    secret: config['c2c.secret']
});

got(config['api.hostname'] + '/c2c/jws.action?jws=' + key, {json: true}).then(resp => {
    let response = resp.body;
    if (response.success !== true)
        throw 'Failure to obtain a token: ' + response.message;

    let token = response.token;
    let options = {
        headers: {
            Authorization: 'C2C ' + Buffer.from(token).toString('base64')
        },
        json: true
    };
    got(config['api.hostname'] + '/api/user/info', options).then(resp => console.log(resp.body));
});