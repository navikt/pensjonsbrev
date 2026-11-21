import { type Draft, produceWithPatches } from "immer";

import { addToHistory } from "../history";
import { type LetterEditorState } from "../model/state";

/**
 * An {@link Action} is a function that accepts a value T and returns a modified copy of the value.
 *
 *
 * Warning: It is important to never mutate the received value directly, any mutations must be
 * made to a copy of that value, i.e. using immer.
 */
export type Action<T, Arguments extends unknown[]> = (target: T, ...arguments_: Arguments) => T;

export type CallbackReceiver<T> = (callback: (previous: T | Readonly<T>) => T) => void;

/**
 * Applies an action as a callback to the CallbackReceiver.
 *
 * @param action to apply
 * @param to the callbackReceiver the action should be applied to
 * @param arguments_ the arguments for the action.
 */
export function applyAction<T, Arguments extends unknown[]>(
  action: Action<T, Arguments>,
  to: CallbackReceiver<T>,
  ...arguments_: Arguments
): void {
  to((target) => target && action(target, ...arguments_));
}

export type LetterEditorStateRecipe<Arguments extends unknown[]> = (
  draft: Draft<LetterEditorState>,
  ...args: Arguments
) => void;

export function withPatches<Arguments extends unknown[]>(
  recipe: LetterEditorStateRecipe<Arguments>,
): Action<LetterEditorState, Arguments> {
  return (current, ...args) => {
    const [produced, patches, inversePatches] = produceWithPatches(current, (draft) => {
      recipe(draft, ...args);
    });

    if (patches.length === 0) {
      return produced;
    }

    // A selection the recipe didn't replace no longer matches a changed document.
    const changedDocument = patches.some((patch) => patch.path[0] === "redigertBrev");
    const next =
      changedDocument && produced.selection !== undefined && produced.selection === current.selection
        ? withoutSelection(produced)
        : produced;

    const history = next.history ?? { entries: [], entryPointer: -1 };

    return {
      ...next,
      history: addToHistory(history, patches, inversePatches),
    };
  };
}

function withoutSelection(state: LetterEditorState): LetterEditorState {
  const { selection: _selection, ...rest } = state;
  return rest;
}
