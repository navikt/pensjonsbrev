import {compose} from "./functional"

/* eslint-disable @typescript-eslint/no-explicit-any */

/**
 * An {@link Action} is a function that accepts a value T and returns a modified copy of the value.
 *
 *
 * Warning: It is important to never mutate the received value directly, any mutations must be
 * made to a copy of that value, i.e. using immer.
 */
// export type Action<T extends any, Args extends any[]> = (target: T, ...args: Args) => T
export interface Action<T, Args extends any[]> {
    (target: T, ...args: Args): T
}

/**
 * A {@link Receiver} is in general a function that accepts a value and potentially executes some side effect.
 * It is typically either the React {@link Dispatch} function returned by React.useState, or a previously
 * bound action ({@link BoundAction}).
 */
export type Receiver<T> = (update: T) => void

/**
 * A {@link BoundAction} is in general a function that accepts any arity of arguments (zero included), and
 * executes som side effect. It is typically an {@link Action} bound to a {@link Receiver}.
 */
export type BoundAction<Args extends any[]> = (...args: Args) => void

// Invalid type declartion for bindAction.
// If your bindAction-call matches this type, then it means that to-param cannot be bound to the action-param.
export function bindAction<T, Args extends any[]>(action: Action<T, Args>, to: () => void, ...args: any[]): void

// Valid type declarations for bindAction
export function bindAction<T, Args extends any[]>(action: Action<T, Args>, to: (receive: T) => void): BoundAction<[T, ...Args]>
export function bindAction<T, Args extends any[]>(action: Action<T, Args>, to: (receive: T) => void, target: T): BoundAction<Args>
export function bindAction<T, BoundArgs extends any[], RestArgs extends any[]>(action: Action<T, [...BoundArgs, ...RestArgs]>, to: (receive: T) => void, target: T, ...args: BoundArgs): BoundAction<RestArgs>

/**
 * Binds an {@link Action} to a {@link Receiver}, and optionally any of the actions arguments.
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param args (optionally) the arguments to bind to the action
 */
export function bindAction(action: Action<any, any[]>, to: Receiver<any>, ...args: any[]) {
    return compose(action, to).bind(null, ...args)
}

/**
 * Combine multiple receivers to one receiver, such that all receivers will receive the update.
 * @param receivers the receivers to combine.
 */
export function combine<T>(...receivers: Receiver<T>[]): Receiver<T> {
    return (update: T) => receivers.forEach(r => r(update))
}