import type { Draft } from "immer";
import { produceWithPatches } from "immer";

import { type HistoryEntry, updateHistory } from "../history";
import type { LetterEditorState } from "../model/state";
import { compose } from "./functional";

/* eslint-disable @typescript-eslint/no-explicit-any */

/**
 * An {@link Action} is a function that accepts a value T and returns a modified copy of the value.
 *
 *
 * Warning: It is important to never mutate the received value directly, any mutations must be
 * made to a copy of that value, i.e. using immer.
 */
// export type Action<T extends any, Args extends any[]> = (target: T, ...args: Args) => T
export interface Action<T, Arguments extends any[]> {
  (target: T, ...arguments_: Arguments): T;
}

/**
 * A {@link Receiver} is in general a function that accepts a value and potentially executes some side effect.
 * It is typically either the React {@link Dispatch} function returned by React.useState, or a previously
 * bound action ({@link BoundAction}).
 */
export type Receiver<T> = (update: T) => void;

/**
 * A {@link BoundAction} is in general a function that accepts any arity of arguments (zero included), and
 * executes som side effect. It is typically an {@link Action} bound to a {@link Receiver}.
 */
export type BoundAction<Arguments extends any[]> = (...arguments_: Arguments) => void;

/**
 * Invalid use of bindAction. The to-parameter must be a Receiver<T>, not a no-arg function.
 */
export function bindAction<T, Arguments extends any[]>(
  action: Action<T, Arguments>,
  to: () => void,
  ...arguments_: any[]
): void;

/**
 * Binds an {@link Action} to a {@link Receiver} and results in a {@link BoundAction} that accepts the target of the action and all it's arguments.
 * @param action the action to bind
 * @param to the receiver to bind it to
 */
export function bindAction<T, Arguments extends any[]>(
  action: Action<T, Arguments>,
  to: Receiver<T>,
): BoundAction<[T, ...Arguments]>;

/**
 * Binds the action target to an {@link Action}, the action to a {@link Receiver}, and results in a {@link BoundAction} that accepts all the action arguments (except the target which is already bound).
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param target the target to bind to the action
 */
export function bindAction<T, Arguments extends any[]>(
  action: Action<T, Arguments>,
  to: Receiver<T>,
  target: T,
): BoundAction<Arguments>;

/**
 * Binds the action target and the given arguments to an {@link Action}, the action to a {@link Receiver}, and results in a {@link BoundAction} that accepts the rest of the action arguments.
 * The given arguments does not need to be the complete list of arguments, it can be any number of arguments. Given an action with n-arity, and x arguments given to bindAction, the resulting
 * BoundAction will be an (n minus x)-arity function.
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param target the target to bind to the action
 * @param arguments_ the arguments to bind to the action
 */
export function bindAction<T, BoundArguments extends any[], RestArguments extends any[]>(
  action: Action<T, [...BoundArguments, ...RestArguments]>,
  to: Receiver<T>,
  target: T,
  ...arguments_: BoundArguments
): BoundAction<RestArguments>;

/**
 * Binds an {@link Action} to a {@link Receiver}, and optionally the action target and any of the following arguments.
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param args (optionally) the arguments to bind to the action
 */
export function bindAction(action: Action<any, any[]>, to: Receiver<any>, ...arguments_: any[]) {
  return compose(action, to).bind(null, ...arguments_);
}

export type CallbackReceiver<T> = (callback: (previous: T | Readonly<T>) => T) => void; //| React.Dispatch<React.SetStateAction<T>>//| ((callback: (prev: Readonly<T>, props: Readonly<any>) => T) => void)

/**
 * Invalid use of bindActionWithCallback. The to-parameter must be a CallbackReceiver<T>, not a no-arg function.
 */
export function bindActionWithCallback<T, Arguments extends any[]>(
  action: Action<T, Arguments>,
  to: () => void,
  ...arguments_: any[]
): void;

/**
 * Binds an {@link Action} to a {@link CallbackReceiver}. The target of the Action will be passed from the CallbackReceiver, and the resulting BoundAction takes the arguments of the Action.
 * @param action the action to bind
 * @param to the callback receiver to it to
 */
export function bindActionWithCallback<T, Arguments extends any[]>(
  action: Action<T, Arguments>,
  to: CallbackReceiver<T>,
): BoundAction<Arguments>;
/**
 * Binds an {@link Action} to a {@link CallbackReceiver}, and the given arguments to the action. The target of the action will be passed from the CallbackReceiver.
 * @param action the action to bind
 * @param to the callback receiver to bind it to
 * @param arguments_ the (potentially partial) arguments to bind to the action
 */
export function bindActionWithCallback<T, BoundArguments extends any[], RestArguments extends any[]>(
  action: Action<T, [...BoundArguments, ...RestArguments]>,
  to: CallbackReceiver<T>,
  ...arguments_: BoundArguments
): BoundAction<RestArguments>;

export function bindActionWithCallback(action: Action<any, any[]>, to: CallbackReceiver<any>, ...arguments_: any[]) {
  return (...restArguments: any[]) => to((previous) => action(previous, ...arguments_, ...restArguments));
}

/**
 * Combine multiple receivers to one receiver, such that all receivers will receive the update.
 * @param receivers the receivers to combine.
 */
export function combine<T>(...receivers: Receiver<T>[]): Receiver<T>;

/**
 * Combine multiple {@link BoundAction}s to one BoundAction that will pass the arguments to of them.
 * @param boundActions the bound actions to combine.
 */
export function combine<Arguments extends any[]>(...boundActions: BoundAction<Arguments>[]): BoundAction<Arguments>;
export function combine(...receivers: ((...arguments_: any[]) => void)[]): (...arguments_: any[]) => void {
  return (...arguments_: any[]) => {
    for (const r of receivers) r(...arguments_);
  };
}

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

    if (patches.length > 0) {
      let history = next.history ?? { entries: [], entryPointer: -1 };
      // If we have undone one or more actions, any new action should clear the "redo" history.
      if (history.entryPointer < history.entries.length - 1) {
        history = { ...history, entries: history.entries.slice(0, history.entryPointer + 1) };
      }

      const isTextUpdate =
        patches.some((p) => p.path[p.path.length - 1] === "editedText") &&
        patches.every((p) => p.path[p.path.length - 1] === "editedText" || p.path[p.path.length - 1] === "saveStatus");

      const newHistoryEntry: HistoryEntry = {
        patches,
        inversePatches,
        label: isTextUpdate ? "TEXT_UPDATE" : undefined,
        timestamp: Date.now(),
      };

      return {
        ...next,
        history: updateHistory(history, newHistoryEntry),
      };
    }

    return next;
  };
}
