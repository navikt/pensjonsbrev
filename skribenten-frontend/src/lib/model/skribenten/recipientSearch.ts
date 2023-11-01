

export type RecipientType = 'PERSON' | 'SAMHANDLER'
export type Location = 'INNLAND' | 'UTLAND'

export type SearchRequest = {
    soeketekst: string,
    recipientType: RecipientType | null,
    location: Location | null,
    kommunenummer: number[],
    land: string | null,
}

export interface PersonSoekResponse {
    readonly totalHits: number,
    readonly resultat: PersonSoekResultat[],
}

export interface PersonSoekResultat {
    readonly navn: string,
    readonly id: string,
    readonly foedselsdato: string,
}

export type AddressResult = {
    addressName: string,
    adresselinjer: string[]
}