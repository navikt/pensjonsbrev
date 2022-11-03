import {Configuration, PublicClientApplication} from "@azure/msal-browser";
import {IMsalContext} from "@azure/msal-react/dist/MsalContext";
import {AuthenticationResult} from "@azure/msal-common";

const msalConfig = {
    auth: {
        clientId: process.env.NEXT_PUBLIC_AZURE_APP_CLIENT_ID,
        authority: `https://login.microsoftonline.com/${process.env.NEXT_PUBLIC_AZURE_APP_TENANT_ID}`,
        redirectUri: "/",
    }
} as Configuration

function withAuthorization(msal: IMsalContext, scope: string): Promise<AuthenticationResult> {
    console.log(msalConfig)
    return msal.instance.acquireTokenSilent({
        scopes: [scope],
        account: msal.accounts[0],
    })
}
const msalInstance = new PublicClientApplication(msalConfig)
export { msalInstance, withAuthorization }