

export interface LetterMetadataResponse {
    readonly kategorier: LetterCategory[]
    readonly eblanketter: LetterMetadata[]
}

// val kategorier: List<LetterCategory>, val eblanketter: List<BrevdataDto>
export interface LetterCategory {
    readonly name: string
    readonly templates: LetterMetadata[]
}

export type Brevsystem = 'EXTERAM'| 'DOKSYS'| 'BREVBAKER'

export interface LetterMetadata {
    readonly name: string
    readonly id: string
    readonly brevsystem: Brevsystem
    readonly spraak: string[]
    readonly isVedtaksbrev: boolean
    readonly isEblankett: boolean
}