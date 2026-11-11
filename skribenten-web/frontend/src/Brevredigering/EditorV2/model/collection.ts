import { type IdentifiableV2 } from "~/types/brevV2";

export type Collection<T extends IdentifiableV2> = {
  items: T[];
  deleted: number[];
  ownerId: number | null;
};

export function collection<T extends IdentifiableV2>(
  items: T[],
  deleted: number[],
  owner: IdentifiableV2 | null,
): Collection<T> {
  return { items, deleted, ownerId: owner?.id ?? null };
}

export function isNew(element: IdentifiableV2): boolean {
  return typeof element.id !== "number";
}

export function isFromTemplate(element: IdentifiableV2): element is IdentifiableV2 & { id: number } {
  return typeof element.id === "number";
}

function belongsTo(element: IdentifiableV2, ownerId: number | null): element is IdentifiableV2 & { id: number } {
  return isFromTemplate(element) && (element.parentId ?? null) === ownerId;
}

function markDeleted<T extends IdentifiableV2>(target: Collection<T>, element: T) {
  if (belongsTo(element, target.ownerId) && !target.deleted.includes(element.id)) {
    target.deleted.push(element.id);
  }
}

function unmarkDeleted<T extends IdentifiableV2>(target: Collection<T>, element: T) {
  if (isFromTemplate(element)) {
    const index = target.deleted.indexOf(element.id);
    if (index !== -1) {
      target.deleted.splice(index, 1);
    }
  }
}

export function removeAt<T extends IdentifiableV2>(target: Collection<T>, start: number, count = 1): T[] {
  const removed = target.items.splice(start, count);
  for (const element of removed) {
    markDeleted(target, element);
  }
  return removed;
}

export function removeFrom<T extends IdentifiableV2>(target: Collection<T>, start: number): T[] {
  return removeAt(target, start, target.items.length - start);
}

export function insertAt<T extends IdentifiableV2>(target: Collection<T>, at: number, ...elements: T[]): void {
  target.items.splice(at, 0, ...elements);
  for (const element of elements) {
    unmarkDeleted(target, element);
  }
}

export function append<T extends IdentifiableV2>(target: Collection<T>, ...elements: T[]): void {
  insertAt(target, target.items.length, ...elements);
}

export function replaceAll<T extends IdentifiableV2>(target: Collection<T>, ...elements: T[]): void {
  removeFrom(target, 0);
  append(target, ...elements);
}

export function move<T extends IdentifiableV2>(target: Collection<T>, from: number, to: number): void {
  const [element] = target.items.splice(from, 1);
  target.items.splice(to, 0, element);
}

export function transfer<T extends IdentifiableV2>(
  source: Collection<T>,
  start: number,
  count: number,
  destination: Collection<T>,
  at: number,
): T[] {
  const moved = removeAt(source, start, count);
  insertAt(destination, at, ...moved);
  return moved;
}
