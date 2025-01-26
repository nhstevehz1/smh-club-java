import {AuthConfig} from "angular-oauth2-oidc";

export const authCodeFlowConfig: AuthConfig = {
    issuer: 'http://localhost:8080/realms/smh-club',
    redirectUri: window.location.origin + '/index.html',
    clientId: 'smh-club-spa',
    responseType: 'code',
    scope: 'openid profile email offline_access',
    showDebugInformation: true,
};
