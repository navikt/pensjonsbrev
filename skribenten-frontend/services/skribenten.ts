import {IMsalContext} from "@azure/msal-react/dist/MsalContext";
import {withAuthorization} from "./msal";

const backendUrl = process.env.NEXT_PUBLIC_SKRIBENTEN_API_URL || "http://localhost:8082"
const backendScope = process.env.NEXT_PUBLIC_SKRIBENTEN_API_SCOPE || "api://dev-gcp.pensjonsbrev.skribenten-backend-lokal/.default"

const SkribentenAPI = {
    testPesys: (msal: IMsalContext): Promise<string> =>
        withAuthorization(msal, backendScope).then((auth) =>
            fetch(`${backendUrl}/test/pen`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: JSON.stringify({name: 'Alexander', age: 35})
            })
        ).then((res) => res.json()).then(JSON.stringify),

    testBrevbaker: (msal: IMsalContext): Promise<Blob> =>
        withAuthorization(msal, backendScope).then((auth) =>
            fetch(`${backendUrl}/test/brevbaker`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.blob())
}

export default SkribentenAPI