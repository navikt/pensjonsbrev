
export interface LetterCategory {
    readonly name: string
    readonly templates: LetterMetadata[]
}

export interface LetterMetadata {
    readonly name: string
    readonly id: string
}
