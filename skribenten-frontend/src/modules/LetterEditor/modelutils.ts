import {Content, LITERAL, TextContent, VARIABLE} from "./model/api"

export function isTextContent(content: Content): content is TextContent {
    return content.type === LITERAL || content.type === VARIABLE
}