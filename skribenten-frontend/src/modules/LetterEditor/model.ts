export interface LiteralValue {
    readonly type: 'literal'
    readonly text: string
}
export interface VariableValue {
    readonly type: 'variable'
    readonly name: string
    readonly text: string
}
export type TextContent = LiteralValue | VariableValue


export interface Block {
    readonly type: string
    readonly locked: boolean
    readonly content: TextContent[]
}
export interface ParagraphBlock extends Block {
    readonly type: 'paragraph'
}
export interface Title1Block extends Block {
    readonly type: 'title1'
}
export interface HeaderBlock extends Block {
    readonly type: 'header'
}
export type AnyBlock = HeaderBlock | Title1Block | ParagraphBlock


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