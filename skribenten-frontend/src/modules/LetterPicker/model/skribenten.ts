
export interface LetterCategory {
    readonly name: string
    readonly templates: LetterSelection[]
}

export type Brevsystem = 'EXTERAM'| 'DOKSYS'| 'BREVBAKER'

export interface LetterSelection {
    readonly name: string
    readonly id: string
    readonly brevsystem: Brevsystem
    readonly spraak: string[]
}

