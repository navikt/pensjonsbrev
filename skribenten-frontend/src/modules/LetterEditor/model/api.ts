export interface Identifiable {
    readonly id: number
}

export const LITERAL = 'LITERAL'
export interface LiteralValue {
    readonly id: number
    readonly type: typeof LITERAL
    readonly text: string
}
export const VARIABLE = 'VARIABLE'
export interface VariableValue {
    readonly id: number
    readonly type: typeof VARIABLE
    readonly name?: string
    readonly text: string
}

export const ITEM_LIST = 'ITEM_LIST'
export interface ItemList {
    readonly id: number
    readonly type: typeof ITEM_LIST
    readonly items: Item[]
}
export interface Item {
    content: TextContent[]
}

export type TextContent = LiteralValue | VariableValue
export type Content = ItemList | LiteralValue | VariableValue

export interface Block extends Identifiable {
    readonly type: string
    readonly locked?: boolean
    readonly editable?: boolean
}

export const PARAGRAPH = 'PARAGRAPH'
export interface ParagraphBlock extends Block {
    readonly type: typeof PARAGRAPH
    readonly content: Content[]
}

export const TITLE1 = 'TITLE1'
export interface Title1Block extends Block {
    readonly type: typeof TITLE1
    readonly content: TextContent[]
}

export const TITLE2 = 'TITLE2'
export interface Title2Block extends Block {
    readonly type: typeof TITLE2
    readonly content: TextContent[]
}

export type AnyBlock = Title1Block | Title2Block | ParagraphBlock
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

export interface EditedLetter {
    readonly letter: RenderedLetter
    readonly deletedBlocks: number[]
}

export interface Sak {
    readonly sakId: number,
    readonly foedselsnr: string,
    readonly foedselsdato: string,
    readonly sakType: SakType,
}

export type SakType = 'AFP' |  'AFP_PRIVAT' |  'ALDER' |  'BARNEP' |  'FAM_PL' |  'GAM_YRK' |  'GENRL' |  'GJENLEV' |  'GRBL' |  'KRIGSP' |  'OMSORG' |  'UFOREP'

export interface SkribentServiceResult<ResultType> {
    readonly result: ResultType | null
    readonly errorMessage: string | null
}

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
