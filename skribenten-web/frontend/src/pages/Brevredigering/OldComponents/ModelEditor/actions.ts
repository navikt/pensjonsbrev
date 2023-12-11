import produce from "immer";

import type { Action } from "../LetterEditor/lib/actions";
import type { FieldType, TArray, TObject } from "../LetterEditor/lib/model/skribenten";
import type { FieldValue } from "./model";

const updateField = produce((draft, name, value) => {
  draft[name] = value;
});

const pushToArray = produce((draft, newValue) => {
  draft.push(newValue);
});

const UpdateField: Action<FieldValue<TObject>, [name: string, value: FieldValue<FieldType> | null]> = updateField;
const UpdateArrayIndex: Action<FieldValue<TArray>, [index: number, value: FieldValue<FieldType>]> = updateField;
const AddToArray: Action<FieldValue<TArray>, [newValue: FieldValue<FieldType>]> = pushToArray;

export const ModelValueAction = {
  UpdateField,
  UpdateArrayIndex,
  AddToArray,
};
