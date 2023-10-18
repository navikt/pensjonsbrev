import {AnyBlock, Content, Item, ITEM_LIST, ItemList, LITERAL, LiteralValue, PARAGRAPH, ParagraphBlock, TextContent, TITLE1, Title1Block, VARIABLE, VariableValue} from "../../../src/lib/model/skribenten"
import * as Actions from "../../../src/modules/LetterEditor/actions/model"

import {LetterEditorState} from "../../../src/modules/LetterEditor/model/state"
import {randomInt} from "crypto"

export function letter(...blocks: AnyBlock[]): LetterEditorState {
    return {
        editedLetter: {
            deletedBlocks: [],
            letter: {
                title: "tittel",
                sakspart: {gjelderNavn: "navn", gjelderFoedselsnummer: "123", saksnummer: "456", dokumentDato: "2022-01-01"},
                signatur: {hilsenTekst: "Mvh", navAvsenderEnhet: "enhet", saksbehandlerRolleTekst: "Saksbehandler", saksbehandlerNavn: "navn"},
                blocks: blocks,
            },
        },
        stealFocus: {},
    }
}

export function paragraph(...content: Content[]): ParagraphBlock {
    return {
        id: randomInt(1000),
        editable: true,
        type: PARAGRAPH,
        content,
    }
}

export function title(...content: TextContent[]): Title1Block {
    return {
        id: randomInt(1000),
        editable: true,
        type: TITLE1,
        content,
    }
}

export function literal(text: string): LiteralValue {
    return {
        id: randomInt(1000),
        type: LITERAL,
        text,
    }
}

export function variable(text: string): VariableValue {
    return {
        id: randomInt(1000),
        type: VARIABLE,
        text,
    }
}

export function itemList(...items: Item[]): ItemList {
    return {
        id: randomInt(1000),
        type: ITEM_LIST,
        items,
    }
}

export function item(...content: TextContent[]): Item {
    return { content }
}

export function select<T>(from: LetterEditorState, id: Partial<Actions.ItemContentId> & { blockId: number }): T {
    const block = (from.editedLetter.letter.blocks)[id.blockId]

    if (id.contentId != null) {
        const content = block.content[id.contentId]

        if (id.itemId != null) {
            const item = (content as ItemList).items[id.itemId]

            if (id.itemContentId != null) {
                return item.content[id.itemContentId] as T
            } else {
                return item as T
            }
        } else {
            return content as T
        }
    } else {
        return block as T
    }
}