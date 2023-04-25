import {IMsalContext} from "@azure/msal-react/dist/MsalContext"
import {withAuthorization} from "./msal"
import {EditedLetter, RedigerbarTemplateDescription, RenderedLetter} from "../../modules/LetterEditor/model/api"
import {ObjectValue} from "../../modules/ModelEditor/model"

export interface SkribentenAPIConfig {
    url: string
    scope: string
}
class SkribentenAPI {
    constructor(readonly config: SkribentenAPIConfig) {}

    async testPesys(msal: IMsalContext): Promise<string> {
        return withAuthorization(msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/test/pen`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: JSON.stringify({name: 'Alexander', age: 35}),
            })
        ).then((res) => res.json()).then(JSON.stringify)
    }

    async testBrevbaker(msal: IMsalContext): Promise<Blob> {
        return withAuthorization(msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/test/brevbaker`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.blob())
    }

    async getRedigerbarTemplateDescription(msal: IMsalContext, brevkode: string): Promise<RedigerbarTemplateDescription> {
        return withAuthorization(msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/template/${brevkode}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.json())
    }

    async renderLetter(msal: IMsalContext, brevkode: string, data: ObjectValue, editedLetter: EditedLetter | null | undefined): Promise<RenderedLetter> {
        return withAuthorization(msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/letter/${brevkode}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: JSON.stringify({letterData: data, editedLetter: editedLetter}),
            })
        ).then(resp => resp.json())
    }
}

export default SkribentenAPI