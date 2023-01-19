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

/**
 * Invalid use of bindAction. The to-parameter must be a Receiver<T>, not a no-arg function.
 */
export function bindAction<T, Args extends any[]>(action: Action<T, Args>, to: () => void, ...args: any[]): void

/**
 * Binds an {@link Action} to a {@link Receiver} and results in a {@link BoundAction} that accepts the target of the action and all it's arguments.
 * @param action the action to bind
 * @param to the receiver to bind it to
 */
export function bindAction<T, Args extends any[]>(action: Action<T, Args>, to: Receiver<T>): BoundAction<[T, ...Args]>

/**
 * Binds the action target to an {@link Action}, the action to a {@link Receiver}, and results in a {@link BoundAction} that accepts all the action arguments (except the target which is already bound).
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param target the target to bind to the action
 */
export function bindAction<T, Args extends any[]>(action: Action<T, Args>, to: Receiver<T>, target: T): BoundAction<Args>

/**
 * Binds the action target and the given arguments to an {@link Action}, the action to a {@link Receiver}, and results in a {@link BoundAction} that accepts the rest of the action arguments.
 * The given arguments does not need to be the complete list of arguments, it can be any number of arguments. Given an action with n-arity, and x arguments given to bindAction, the resulting
 * BoundAction will be an (n minus x)-arity function.
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param target the target to bind to the action
 * @param args the arguments to bind to the action
 */
export function bindAction<T, BoundArgs extends any[], RestArgs extends any[]>(action: Action<T, [...BoundArgs, ...RestArgs]>, to: Receiver<T>, target: T, ...args: BoundArgs): BoundAction<RestArgs>

/**
 * Binds an {@link Action} to a {@link Receiver}, and optionally the action target and any of the following arguments.
 * @param action the action to bind
 * @param to the receiver to bind it to
 * @param args (optionally) the arguments to bind to the action
 */
export function bindAction(action: Action<any, any[]>, to: Receiver<any>, ...args: any[]) {
    return compose(action, to).bind(null, ...args)
}



export type CallbackReceiver<T> = ((callback: (prev: T | Readonly<T>) => T) => void) //| React.Dispatch<React.SetStateAction<T>>//| ((callback: (prev: Readonly<T>, props: Readonly<any>) => T) => void)

/**
 * Invalid use of bindActionWithCallback. The to-parameter must be a CallbackReceiver<T>, not a no-arg function.
 */
export function bindActionWithCallback<T, Args extends any[]>(action: Action<T, Args>, to: () => void, ...args: any[]): void

/**
 * Binds an {@link Action} to a {@link CallbackReceiver}. The target of the Action will be passed from the CallbackReceiver, and the resulting BoundAction takes the arguments of the Action.
 * @param action the action to bind
 * @param to the callback receiver to it to
 */
export function bindActionWithCallback<T, Args extends any[]>(action: Action<T, Args>, to: CallbackReceiver<T>): BoundAction<Args>
/**
 * Binds an {@link Action} to a {@link CallbackReceiver}, and the given arguments to the action. The target of the action will be passed from the CallbackReceiver.
 * @param action the action to bind
 * @param to the callback receiver to bind it to
 * @param args the (potentially partial) arguments to bind to the action
 */
export function bindActionWithCallback<T, BoundArgs extends any[], RestArgs extends any[]>(action: Action<T, [...BoundArgs, ...RestArgs]>, to: CallbackReceiver<T>, ...args: BoundArgs): BoundAction<RestArgs>


export function bindActionWithCallback(action: Action<any, any[]>, to: CallbackReceiver<any>, ...args: any[]) {
    return (...restArgs: any[]) => to((prev) => action(prev, ...args, ...restArgs))
}

/**
 * Combine multiple receivers to one receiver, such that all receivers will receive the update.
 * @param receivers the receivers to combine.
 */
export function combine<T>(...receivers: Receiver<T>[]): Receiver<T>

/**
 * Combine multiple {@link BoundAction}s to one BoundAction that will pass the arguments to of them.
 * @param boundActions the bound actions to combine.
 */
export function combine<Args extends any[]>(...boundActions: BoundAction<Args>[]): BoundAction<Args>
export function combine(...receivers: ((...args: any[]) => void)[]): (...args: any[]) => void {
    return (...args: any[]) => receivers.forEach(r => r(...args))
}