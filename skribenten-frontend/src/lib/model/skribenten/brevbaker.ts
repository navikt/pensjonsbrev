export type Distribusjonstype = 'VEDTAK' | 'VIKTIG' | 'ANNET'
export type LanguageCode = 'BOKMAL' | 'NYNORSK' | 'ENGLISH'

export interface LetterMetadata {
    readonly displayTitle: string
    readonly isSensitiv: boolean
    readonly distribusjonstype: Distribusjonstype
}

export interface TemplateDescription {
    readonly name: string
    readonly letterDataClass: string
    readonly languages: LanguageCode[]
    readonly metadata: LetterMetadata
}