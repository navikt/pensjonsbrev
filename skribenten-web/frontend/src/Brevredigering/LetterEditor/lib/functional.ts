/* eslint-disable @typescript-eslint/no-explicit-any */

export function compose<Arguments extends any[], State, Out>(
  f1: (...arguments_: Arguments) => State,
  f2: (state: State) => Out,
): (...arguments_: Arguments) => Out {
  return (...arguments_: Arguments) => f2(f1(...arguments_));
}
