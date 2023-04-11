import {Content} from "./api"

export type CursorPosition = {
    contentId: number,
    startOffset: number,
}
export type StealFocus = {
    [blockId: number]: CursorPosition | undefined
}
export type ContentGroup = { content: Content[] }