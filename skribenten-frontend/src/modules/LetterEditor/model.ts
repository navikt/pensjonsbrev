export interface Identifiable {
    readonly id: number
    readonly location: string[]
}

export const LITERAL = 'LITERAL'
export interface LiteralValue extends Identifiable {
    readonly type: typeof LITERAL
    readonly text: string
}
export const VARIABLE = 'VARIABLE'
export interface VariableValue extends Identifiable {
    readonly type: typeof VARIABLE
    readonly name?: string
    readonly text: string
}
export type TextContent = LiteralValue | VariableValue

export interface Block extends Identifiable {
    readonly type: string
    readonly locked?: boolean
    readonly editable?: boolean
    readonly content: TextContent[]
}
export interface ParagraphBlock extends Block {
    readonly type: 'PARAGRAPH'
}
export interface Title1Block extends Block {
    readonly type: 'TITLE1'
}
export type AnyBlock = Title1Block | ParagraphBlock
export interface Sakspart {
    readonly gjelderNavn: string
    readonly gjelderFoedselsnummer: string
    readonly saksnummer: string
    readonly dokumentDato: string
}
export interface Signatur {
    readonly hilsenTekst: string
    readonly saksbehandlerRolleTekst: string
    readonly saksbehandlerNavn: string
    readonly attesterendeSaksbehandlerNavn?: string
    readonly navAvsenderEnhet: string
}
export interface RenderedLetter {
    readonly title: string
    readonly sakspart: Sakspart
    readonly blocks: AnyBlock[]
    readonly signatur: Signatur
}


// TODO: Variables might not be necessary any more
export type VariableType = 'text' | 'date' // | 'int' | 'double'
export interface VariableSpec {
    readonly name: string
    readonly type: VariableType
}
export interface VariableDecl {
    readonly spec: VariableSpec
    readonly value: string
}
export type Variables = Record<string, VariableDecl>


export type LanguageCode = 'BOKMAL' | 'NYNORSK' | 'ENGLISH'
export type Distribusjonstype = 'VEDTAK' | 'VIKTIG' | 'ANNET'
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
export type ScalarKind = 'NUMBER' | 'DOUBLE' | 'STRING' | 'BOOLEAN' | 'DATE'
export interface TScalar {
    readonly type: 'scalar'
    readonly nullable: boolean
    readonly kind: ScalarKind
}
export interface TEnum {
    readonly type: 'enum'
    readonly nullable: boolean
    readonly values: string[]
}
export interface TArray {
    readonly type: 'array'
    readonly nullable: boolean
    readonly items: FieldType
}
export interface TObject {
    readonly type: 'object'
    readonly nullable: boolean
    readonly typeName: string
}
export type FieldType = TScalar | TEnum | TArray | TObject
export interface ObjectTypeSpecification {
    readonly [field: string]: FieldType
}
export interface ObjectTypeSpecifications {
    readonly [name: string]: ObjectTypeSpecification
}
export interface LetterModelSpecification {
    readonly types: ObjectTypeSpecifications
    readonly letterModelTypeName: string
}
export interface RedigerbarTemplateDescription {
    readonly description: TemplateDescription
    readonly modelSpecification: LetterModelSpecification
}