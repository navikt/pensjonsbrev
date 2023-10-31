

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
export type SpraakKode = 'EN' | 'NB' | 'NN' | 'SE' | 'FR'

export interface Metadata {
    readonly name: string
    readonly id: string
    readonly brevsystem: Brevsystem
    readonly spraak: SpraakKode[]
    readonly isVedtaksbrev: boolean
    readonly isEblankett: boolean
}

export interface SkribentServiceResult<ResultType> {
    readonly result: ResultType | null
    readonly errorMessage: string | null
}

export interface KontaktinfoResponse {
    readonly spraakKode: SpraakKode | null,
    readonly error: string[],
}
/*

@JsonIgnoreProperties(ignoreUnknown = true)
data class KontaktinfoResponse(val spraakKode: SpraakKode? = null) {
    enum class SpraakKode {
        nb, // bokmål
        nn, //nynorsk
        en, //engelsk
        se, //samisk
    }
}

data class KontaktinfoErrorResponse(
    val errors: List<Error>,
) {
    data class Error(val id: String, val status: String, val title: String, val detail: String)
}
* */