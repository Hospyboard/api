const axios = require('axios');
const formUrlEncoded = x => Object.keys(x).reduce((p, c) => p + `&${c}=${encodeURIComponent(x[c])}`, '');
const domain = 'keycloak:8080'
let accessToken = "";

axios({
    method: 'post',
    url: 'http://' + domain + '/auth/realms/master/protocol/openid-connect/token',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    data: formUrlEncoded({
        'grant_type': 'password',
        'client_id': 'admin-cli',
        'username': 'admin',
        'password': 'admin'
    })
}).then(res => {
    accessToken = res.data.access_token;
    createRealm()
}).catch(err => {
    console.error(err.message);
    process.exit(1)
});

function createRealm() {
    axios({
        method: 'post',
        url: 'http://' + domain + '/auth/admin/realms',
        headers: {
            'Authorization': 'bearer ' + accessToken,
            'Content-Type': 'application/json'
        },
        data: {
            "enabled": true,
            "id": "SpringBootKeycloak",
            "realm": "SpringBootKeycloak"
        }
    }).then(res => {
        createClient();
    }).catch(err => {
        createClient();
    });
}

function createClient() {
    axios({
        method: 'post',
        url: 'http://' + domain + '/auth/admin/realms/SpringBootKeycloak/clients',
        headers: {
            'Authorization': 'bearer ' + accessToken,
            'Content-Type': 'application/json'
        },
        data: {
            "enabled": true,
            "attributes": {},
            "redirectUris": ["http://localhost:9090/*"],
            "clientId": "login-app-hospyboard",
            "protocol": "openid-connect"
        }
    }).then(res => {
        createUserRole();
    }).catch(err => {
        createUserRole();
    });
}

function createUserRole() {

}
