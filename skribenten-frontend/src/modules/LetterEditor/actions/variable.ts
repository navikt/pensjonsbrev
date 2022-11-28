import {Action} from "../../../lib/actions"
import {VariableDecl} from "../model"
import produce from "immer"

const updateValue: Action<VariableDecl, [value: string]> =
    produce((draft, value) => {
        draft.value = value
    })

export const VariableAction = {
    updateValue,
}