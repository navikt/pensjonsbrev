import { describe, expect, test } from "vitest";

import { compose } from "~/Brevredigering/LetterEditor/lib/functional";

describe("compose", () => {
  test("composed function applies f2 to output from f1: f2(f1(...))", () => {
    const f1 = (a: number, b: number) => a + b + 1;
    const f2 = (a: number) => a + 2;

    expect(compose(f1, f2)(4, 2)).toEqual(f2(f1(4, 2)));
  });

  test("composed function does not apply f2 then f1: f1(f2(...))", () => {
    const f1 = (a: number) => 1 + a;
    const f2 = (a: number) => a * 2;

    // f2(f1(5)) --> (1 + 5) * 2 === 12
    // f1(f2(5)) --> 1 + (5 * 2) === 11

    expect(compose(f1, f2)(5)).toEqual(12);
    expect(compose(f2, f1)(5)).toEqual(11);
  });
});
