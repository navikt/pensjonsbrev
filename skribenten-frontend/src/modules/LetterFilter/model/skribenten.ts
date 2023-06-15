
export interface LetterCategory {
    readonly name: string
    readonly templates: LetterSelection[]
}

export interface LetterSelection {
    readonly name: string
    readonly id: string
    readonly spraak: string[]
}
