import { describe, expect, test } from "vitest";

import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import { bindAction, bindActionWithCallback, combine } from "~/Brevredigering/LetterEditor/lib/actions";

describe("bindAction", () => {
  test("output from action is passed as input to receiver", () => {
    let result = "untouched";
    const bound = bindAction(
      (target: string) => "action: " + target,
      (r: string) => {
        result = r;
      },
    );

    bound("hi");
    expect(result).toEqual("action: hi");
  });

  test("actions can bound to other bound actions", () => {
    let result = "untouched";
    const bound = bindAction(
      (target) => "action: " + target,
      (r: string) => {
        result = r;
      },
    );
    const nextBound = bindAction((target) => "next: " + target, bound);

    nextBound("hi");
    expect(result).toEqual("action: next: hi");
  });

  test("can bind target to action", () => {
    let result = "untouched";
    const bound = bindAction(
      (target: string) => "action: " + target,
      (r: string) => {
        result = r;
      },
      "our target",
    );

    bound();
    expect(result).toEqual("action: our target");
  });

  test("can bind arguments to action", () => {
    let result = "untouched";
    const bound = bindAction(
      (target: string, argument: number) => "action: " + target + argument,
      (r: string) => {
        result = r;
      },
      "our target",
      4,
    );

    bound();
    expect(result).toEqual("action: our target4");
  });
});

describe("combine", () => {
  test("all receivers are invoked with the given value", () => {
    let f1Value = "untouched";
    let f2Value = "untouched";
    const f1 = (v: string) => {
      f1Value = "f1: " + v;
    };
    const f2 = (v: string) => {
      f2Value = "f2: " + v;
    };

    combine(f1, f2)("touched");

    expect(f1Value).toEqual("f1: touched");
    expect(f2Value).toEqual("f2: touched");
  });

  test("all receivers are invoked with all arguments for multi-parameter receivers", () => {
    let f1Value = "untouched";
    let f2Value = "untouched";

    const f1 = (v1: string, v2: number) => {
      f1Value = `f1: ${v1} ${v2}`;
    };
    const f2 = (v1: string, v2: number) => {
      f2Value = `f2: ${v1} ${v2}`;
    };

    combine(f1, f2)("touched", 2);

    expect(f1Value).toEqual("f1: touched 2");
    expect(f2Value).toEqual("f2: touched 2");
  });
});

describe("bindActionWithCallback", () => {
  function receiver(this: { result: string }, callback: (current: string) => string) {
    this.result = callback("current value");
  }

  const touchedBy: Action<string, [who: string]> = (target: string, who: string) => target + " touched by " + who;

  test("action is passed input from callbackReceiver", () => {
    const state = { result: "" };
    const bound = bindActionWithCallback(touchedBy, receiver.bind(state));

    bound("an angel");

    expect(state.result).toEqual("current value touched by an angel");
  });

  test("can bind action arguments", () => {
    const state = { result: "" };
    const bound = bindActionWithCallback(touchedBy, receiver.bind(state), "you");

    bound();

    expect(state.result).toEqual("current value touched by you");
  });

  test("can bind other subsequent actions", () => {
    const state = { result: "" };
    const bound = bindActionWithCallback(touchedBy, receiver.bind(state));
    const next = bindAction((target) => "silly " + target, bound);

    next("me");

    expect(state.result).toEqual("current value touched by silly me");
  });
});
