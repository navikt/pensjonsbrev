import {IMsalContext} from "@azure/msal-react/dist/MsalContext"
import {withAuthorization} from "./msal"
import {
    EditedLetter,
    RedigerbarTemplateDescription,
    RenderedLetter,
    Sak, SakType,
    SkribentServiceResult,
} from "../../modules/LetterEditor/model/api"
import {ObjectValue} from "../../modules/ModelEditor/model"
import {LetterCategory} from "../../modules/LetterPicker/model/skribenten"

export interface SkribentenAPIConfig {
    url: string
    scope: string
}

class SkribentenAPI {
    constructor(readonly config: SkribentenAPIConfig) {
    }

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
        console.log(this.config.url)
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

    async renderLetter(msal: IMsalContext, brevkode: string, data: ObjectValue, editedLetter: EditedLetter | undefined): Promise<RenderedLetter> {
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


    async getLetterTemplates(msal: IMsalContext, sakType: SakType): Promise<LetterCategory[]> {
        return withAuthorization(msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/lettertemplates/${sakType}`, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.json())
    }

    async getFavourites(msal: IMsalContext): Promise<string[]> {
        return withAuthorization(msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/favourites`, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.json()).catch(res => console.log(res))
    }

    async addFavourite(msal: IMsalContext, letterId: string) {
        return withAuthorization(msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/favourites`, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: letterId,
            })
        )
    }

    async removeFavourite(msal: IMsalContext, letterId: string) {
        return withAuthorization(msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/favourites`, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'DELETE',
                body: letterId,
            })
        )
    }

    async getSaksinfo(msal: IMsalContext, saksnummer: number): Promise<SkribentServiceResult<Sak>> {
        return withAuthorization(msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/pen/sak/${saksnummer}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(async (res): Promise<SkribentServiceResult<Sak>> => {
            if (res.status == 404) {
                return {result: null, errorMessage: `fant ikke sak med saksnummer ${saksnummer.toString()}`}
            } else if (res.status !== 200) {
                return {result: null, errorMessage: `Error while fetching sak: Error code ${res.status}`}
            }
            return await res.json().then((value) => {
                return {result: value, errorMessage: null}
            }).catch((reason) => {
                return {result: null, errorMessage: reason.message}
            })
        })
    }

    async bestillExtreamBrev(msal: IMsalContext,
                             brevkode: string,
                             sakId: string,
                             gjelderPid: string,
                             spraak: string): Promise<string> {
        return withAuthorization(msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/pen/extream`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: JSON.stringify({brevkode: brevkode, sakId: sakId, spraak: spraak, gjelderPid: gjelderPid}),
            })
        ).then((res) => res.text())
    }


    bestillDoksysBrev(msal: IMsalContext,
                      brevkode: string,
                      sakId: string,
                      gjelderPid: string,
                      spraak: string): Promise<string> {
        return withAuthorization(msal, this.config.scope)
            .then((auth) => fetch(`${this.config.url}/pen/doksys`, {
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${auth.accessToken}`,
                    },
                    method: 'POST',
                    body: JSON.stringify({brevkode: brevkode, sakId: sakId, spraak: spraak, gjelderPid: gjelderPid}),
                })
            ).then((res) => res.text())

    }

    async hentNavn(msal: IMsalContext, fnr: string): Promise<string> {
        return withAuthorization(msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/pdl/navn/${fnr}`, {
                headers: {
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then((res) => res.text())
    }
}

export default SkribentenAPI