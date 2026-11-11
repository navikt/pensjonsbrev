import { type DocumentV2 } from "~/types/brevV2";

import { type Caret, containers, resolve, TITLE } from "./paths";
import { isUneditedFritekst } from "./text";

export function firstUneditedFritekst(doc: DocumentV2): Caret | undefined {
  for (const path of [TITLE, ...containers(doc)]) {
    const content = resolve(doc, path)?.content ?? [];
    const index = content.findIndex(isUneditedFritekst);
    if (index >= 0) return { path, content: index, offset: 0 };
  }
  return undefined;
}
