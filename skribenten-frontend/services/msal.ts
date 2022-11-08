import {PublicClientApplication} from "@azure/msal-browser";
import {IMsalContext} from "@azure/msal-react/dist/MsalContext";
import {AuthenticationResult} from "@azure/msal-common";

export interface AzureConfig {
    clientId: string
    tenantId: string
}

function createMsal(config: AzureConfig): PublicClientApplication {
    return new PublicClientApplication({
        auth: {
            clientId: config.clientId,
            authority: `https://login.microsoftonline.com/${config.tenantId}`,
            redirectUri: "/",
        }
    })
}

function withAuthorization(msal: IMsalContext, scope: string): Promise<AuthenticationResult> {
    return msal.instance.acquireTokenSilent({
        scopes: [scope],
        account: msal.accounts[0],
    })
}

export {createMsal, withAuthorization}