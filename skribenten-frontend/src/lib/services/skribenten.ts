import {IMsalContext} from "@azure/msal-react/dist/MsalContext"
import {withAuthorization} from "./msal"
import {
    AddressResult,
    EditedLetter, PersonSoekResponse,
    RedigerbarTemplateDescription,
    RenderedLetter,
    Sak, SakType, SearchRequest,
    SkribentServiceResult,
} from "../model/skribenten"
import {ObjectValue} from "../../modules/ModelEditor/model"
import {Metadata, LetterMetadataResponse} from "../model/skribenten"
import {
    Avtaleland,
    KommuneResult,
} from "../../modules/LetterPicker/components/ChangeRecipient/RecipientSearch/RecipientSearch"

export interface SkribentenAPIConfig {
    url: string
    scope: string
}

class SkribentenAPI {
    constructor(readonly config: SkribentenAPIConfig, readonly msal: IMsalContext) {
    }

    async testPesys(): Promise<string> {
        return withAuthorization(this.msal, this.config.scope).then((auth) =>
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

    async testBrevbaker(): Promise<Blob> {
        console.log(this.config.url)
        return withAuthorization(this.msal, this.config.scope).then((auth) =>
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

    async getRedigerbarTemplateDescription(brevkode: string): Promise<RedigerbarTemplateDescription> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
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

    async renderLetter(brevkode: string, data: ObjectValue, editedLetter: EditedLetter | undefined): Promise<RenderedLetter> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
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

    async getLetterTemplates(sakType: SakType): Promise<LetterMetadataResponse> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/lettertemplates/${sakType}`, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.json())
    }

    async getFavourites(): Promise<string[]> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/favourites`, {
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'GET',
            })
        ).then(resp => resp.json())
    }

    async addFavourite(letterId: string) {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
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

    async removeFavourite(letterId: string) {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
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

    async getSaksinfo(saksnummer: number): Promise<SkribentServiceResult<Sak>> {
        return withAuthorization(this.msal, this.config.scope).then((auth) =>
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


    async bestillExtreamBrev(
        metadata: Metadata,
        sak: Sak,
        gjelderPid: string,
        spraak: string,
        landkode?: string,
        mottakerText?: string,
    ): Promise<string> {
        return withAuthorization(this.msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/pen/extream`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: JSON.stringify({
                    brevkode: metadata.id,
                    gjelderPid: gjelderPid,
                    sakId: sak.sakId,
                    spraak: spraak,
                    landkode: landkode,
                    mottakerText: mottakerText,
                }),
            })
        ).then((res) => res.text())
    }


    async bestillDoksysBrev(
        brevkode: string,
        sakId: string,
        gjelderPid: string,
        spraak: string): Promise<string> {
        return withAuthorization(this.msal, this.config.scope)
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

    async hentNavn(fnr: string): Promise<string> {
        return withAuthorization(this.msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/pdl/navn/${fnr}`, {
                headers: {'Authorization': `Bearer ${auth.accessToken}`},
                method: 'GET',
            })
        ).then((res) => res.text())
    }

    async soekEtterMottaker(request: SearchRequest): Promise<SkribentServiceResult<PersonSoekResponse>> {
        return withAuthorization(this.msal, this.config.scope).then((auth) =>
            fetch(`${this.config.url}/pdl/soekmottaker`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.accessToken}`,
                },
                method: 'POST',
                body: JSON.stringify(request),
            })
        ).then(async (res): Promise<SkribentServiceResult<PersonSoekResponse>> => {
            if (res.status !== 200) {
                return {result: null, errorMessage: `Error while seraching for recipient: Error code ${res.body}`}
            }
            return await res.json().then((value) => {
                return {result: value, errorMessage: null}
            }).catch((reason) => {
                return {result: null, errorMessage: reason.message}
            })
        })
    }

    async hentKommuneForslag(): Promise<KommuneResult[]> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/kodeverk/kommune`, {
                headers: {'Authorization': `Bearer ${auth.accessToken}`},
                method: 'GET',
            })
        ).then(res => res.json())
    }

    async hentAvtaleland(): Promise<Avtaleland[]> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/kodeverk/avtaleland`, {
                headers: {'Authorization': `Bearer ${auth.accessToken}`},
                method: 'GET',
            })
        ).then(res => res.json())
    }

    async hentAdresse(pid: string): Promise<AddressResult> {
        return withAuthorization(this.msal, this.config.scope).then(auth =>
            fetch(`${this.config.url}/adresse/${pid}`, {
                headers: {'Authorization': `Bearer ${auth.accessToken}`},
                method: 'GET',
            })
        ).then(res => res.json())
    }
}

export default SkribentenAPI