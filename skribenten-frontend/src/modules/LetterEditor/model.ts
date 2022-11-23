export interface LiteralValue {
    type: 'literal'
    text: string
}
export interface VariableValue {
    type: 'variable'
    name: string
    text: string
}
export type TextContent = LiteralValue | VariableValue


export interface Block {
    type: string
    locked: boolean
    content: TextContent[]
}
export interface ParagraphBlock extends Block {
    type: 'paragraph'
}
export interface Title1Block extends Block {
    type: 'title1'
}
export interface HeaderBlock extends Block {
    type: 'header'
}
export type AnyBlock = HeaderBlock | Title1Block | ParagraphBlock

export type VariableType = 'string' | 'datetime' // | 'int' | 'double'
export interface VariableSpec {
    name: string
    type: VariableType
}
export interface VariableDecl {
    spec: VariableSpec
    value: any
}