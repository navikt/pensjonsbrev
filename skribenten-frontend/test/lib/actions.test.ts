import {describe, expect, test} from "vitest";
import {bindAction, combine} from "../../src/lib/actions";

describe("bindAction", () => {
    test("output from action is passed as input to receiver", () => {
        let result: string = "untouched"
        const bound = bindAction((target: string) => "action: " + target, (r: string) => { result = r})

        bound("hi")
        expect(result).toEqual("action: hi")
    })

    test("actions can bound to other bound actions", () => {
        let result: string = "untouched"
        const bound = bindAction((target) => "action: " + target, (r: string) => { result = r})
        const nextBound = bindAction((target) => "next: " + target, bound)

        nextBound("hi")
        expect(result).toEqual("action: next: hi")
    })

    test("can bind target to action", () => {
        let result: string = "untouched"
        const bound = bindAction((target: string) => "action: " + target, (r: string) => { result = r}, "our target")

        bound()
        expect(result).toEqual("action: our target")
    })

    test("can bind arguments to action", () => {
        let result: string = "untouched"
        const bound = bindAction((target: string, arg: number) => "action: " + target + arg, (r: string) => { result = r}, "our target", 4)

        bound()
        expect(result).toEqual("action: our target4")
    })
})

describe("combine", () => {
    test("all receivers are invoked with the given value", () => {
        let f1Value ="untouched"
        let f2Value = "untouched"
        const f1 = (v: string) => { f1Value = "f1: " + v }
        const f2 = (v: string) => { f2Value = "f2: " + v }

        combine(f1, f2)("touched")

        expect(f1Value).toEqual("f1: touched")
        expect(f2Value).toEqual("f2: touched")
    })
})
