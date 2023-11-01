import {Action} from "../../lib/actions"
import produce from "immer"
import {FieldValue} from "./model"
import {FieldType, TArray, TObject} from "../../lib/model/skribenten"

const updateField = produce((draft, name, value) => {
    draft[name] = value
})

const pushToArray = produce((draft, newValue) => {
    draft.push(newValue)
})

const UpdateField: Action<FieldValue<TObject>, [name: string, value: FieldValue<FieldType> | null]> = updateField
const UpdateArrayIndex: Action<FieldValue<TArray>, [index: number, value: FieldValue<FieldType>]> = updateField
const AddToArray: Action<FieldValue<TArray>, [newValue: FieldValue<FieldType>]> = pushToArray

export const ModelValueAction = {
    UpdateField, UpdateArrayIndex, AddToArray,
}