import { describe, expect, test } from "vitest";

import { literal } from "~test/support/editorV2TestUtils";

import { append, collection, insertAt, move, removeAt, removeFrom, replaceAll, transfer } from "./collection";
import { newLiteral } from "./text";

const owner = { id: 1, parentId: null };
const other = { id: 2, parentId: null };

const own = (id: number) => literal({ id, parentId: 1, text: `t${id}` });

describe("collection", () => {
  test("removing an element owned by the collection records its id", () => {
    const target = collection([own(10), own(11)], [], owner);
    expect(removeAt(target, 0).map((e) => e.id)).toEqual([10]);
    expect(target.deleted).toEqual([10]);
    expect(target.items.map((e) => e.id)).toEqual([11]);
  });

  test("removing an element with another parent does not record it", () => {
    const target = collection([literal({ id: 10, parentId: 99, text: "x" })], [], owner);
    removeAt(target, 0);
    expect(target.deleted).toEqual([]);
  });

  test("removing a new element does not record it", () => {
    const target = collection([newLiteral({ text: "x" })], [], owner);
    removeAt(target, 0);
    expect(target.deleted).toEqual([]);
  });

  test("ownerless collections record top level elements", () => {
    const target = collection([literal({ id: 10, text: "x" })], [], null);
    removeAt(target, 0);
    expect(target.deleted).toEqual([10]);
  });

  test("ids are recorded only once", () => {
    const element = own(10);
    const target = collection([element, element], [10], owner);
    removeFrom(target, 0);
    expect(target.deleted).toEqual([10]);
  });

  test("re-inserting a removed element unmarks it", () => {
    const target = collection([own(10), own(11)], [], owner);
    const [removed] = removeAt(target, 0);
    insertAt(target, 1, removed);
    expect(target.deleted).toEqual([]);
    expect(target.items.map((e) => e.id)).toEqual([11, 10]);
  });

  test("replaceAll records removed and unmarks re-added elements", () => {
    const kept = own(10);
    const target = collection([kept, own(11)], [], owner);
    replaceAll(target, kept);
    expect(target.deleted).toEqual([11]);
    expect(target.items).toEqual([kept]);
  });

  test("transfer records deletion in the source only", () => {
    const source = collection([own(10), own(11), own(12)], [], owner);
    const destination = collection([literal({ id: 20, parentId: 2, text: "d" })], [], other);
    expect(transfer(source, 1, 2, destination, 0).map((e) => e.id)).toEqual([11, 12]);
    expect(source.deleted).toEqual([11, 12]);
    expect(destination.deleted).toEqual([]);
    expect(destination.items.map((e) => e.id)).toEqual([11, 12, 20]);
  });

  test("transferring back unmarks the deletion", () => {
    const source = collection([own(10)], [], owner);
    const destination = collection([], [], other);
    transfer(source, 0, 1, destination, 0);
    transfer(destination, 0, 1, source, 0);
    expect(source.deleted).toEqual([]);
    expect(destination.deleted).toEqual([]);
  });

  test("move does not touch deleted", () => {
    const target = collection([own(10), own(11), own(12)], [5], owner);
    move(target, 0, 2);
    expect(target.items.map((e) => e.id)).toEqual([11, 12, 10]);
    expect(target.deleted).toEqual([5]);
  });

  test("append adds at the end", () => {
    const target = collection([own(10)], [], owner);
    append(target, own(11));
    expect(target.items.map((e) => e.id)).toEqual([10, 11]);
  });
});
