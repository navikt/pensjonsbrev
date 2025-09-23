import type { Draft } from "immer";
import { produceWithPatches } from "immer";

import { addToHistory } from "../history";
import type { LetterEditorState } from "../model/state";

/* eslint-disable @typescript-eslint/no-explicit-any */

/**
 * An {@link Action} is a function that accepts a value T and returns a modified copy of the value.
 *
 *
 * Warning: It is important to never mutate the received value directly, any mutations must be
 * made to a copy of that value, i.e. using immer.
 */
export interface Action<T, Arguments extends any[]> {
  (target: T, ...arguments_: Arguments): T;
}

export type CallbackReceiver<T> = (callback: (previous: T | Readonly<T>) => T) => void;

/**
 * Applies an action as a callback to the CallbackReceiver.
 *
 * @param action to apply
 * @param to the callbackReceiver the action should be applied to
 * @param arguments_ the arguments for the action.
 */
export function applyAction<T, Arguments extends any[]>(
  action: Action<T, Arguments>,
  to: CallbackReceiver<T>,
  ...arguments_: Arguments
): void {
  to((target) => target && action(target, ...arguments_));
}

export type LetterEditorStateRecipe<Arguments extends any[]> = (
  draft: Draft<LetterEditorState>,
  ...args: Arguments
) => void;

export function withPatches<Arguments extends any[]>(
  recipe: LetterEditorStateRecipe<Arguments>,
): Action<LetterEditorState, Arguments> {
  return (current, ...args) => {
    const [next, patches, inversePatches] = produceWithPatches(current, (draft) => {
      recipe(draft, ...args);
    });

    if (patches.length === 0) {
      return next;
    }

    const history = next.history ?? { entries: [], entryPointer: -1 };

    return {
      ...next,
      history: addToHistory(history, patches, inversePatches),
    };
  };
}
