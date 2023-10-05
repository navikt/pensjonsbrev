

export interface LetterMetadataResponse {
    readonly kategorier: LetterCategory[]
    readonly eblanketter: Metadata[]
}

// val kategorier: List<LetterCategory>, val eblanketter: List<BrevdataDto>
export interface LetterCategory {
    readonly name: string
    readonly templates: Metadata[]
}

export type Brevsystem = 'EXTERAM'| 'DOKSYS'| 'BREVBAKER'

export interface Metadata {
    readonly name: string
    readonly id: string
    readonly brevsystem: Brevsystem
    readonly spraak: string[]
    readonly isVedtaksbrev: boolean
    readonly isEblankett: boolean
}

export interface SkribentServiceResult<ResultType> {
    readonly result: ResultType | null
    readonly errorMessage: string | null
}