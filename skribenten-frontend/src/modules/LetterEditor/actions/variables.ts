import {Action} from "../../../lib/actions"
import {VariableDecl, Variables} from "../model"
import produce from "immer"

const updateVariable: Action<Variables, [variable: VariableDecl]> =
    produce((draft, variable) => {
        draft[variable.spec.name] = variable
    })

export const VariablesAction = {
    updateVariable,
}