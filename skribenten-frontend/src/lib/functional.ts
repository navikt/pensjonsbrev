
/* eslint-disable @typescript-eslint/no-explicit-any */

export function compose<Args extends any[], State, Out>(
    f1: (...args: Args) => State,
    f2: (state: State) => Out
): (...args: Args) => Out {
    return (...args: Args) => f2(f1(...args))
}