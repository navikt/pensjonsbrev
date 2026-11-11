import {
  isTekstValgHighlighted,
  useInsertedTekstValgHighlight,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import { type VariableV2 } from "~/types/brevV2";

import { textAttributes } from "../../caret/domCaret";
import { moveFocus } from "../../commands/updateText";
import { type ContainerPath, samePath } from "../../model/paths";
import { useEditorV2 } from "../EditorContextV2";

export function VariableView({ path, index, variable }: { path: ContainerPath; index: number; variable: VariableV2 }) {
  const { state, dispatch } = useEditorV2();
  const highlighted = isTekstValgHighlighted(useInsertedTekstValgHighlight(), variable);
  const focused = samePath(state.focus.path, path) && state.focus.content === index;
  const empty = variable.text.trim().length === 0;

  return (
    // biome-ignore lint/a11y/useKeyWithClickEvents lint/a11y/noStaticElementInteractions: fokus følger klikk; tastatur håndteres av redigerbar tekst
    <span
      {...textAttributes(path, index)}
      className={highlighted ? "inserted-flash-text" : undefined}
      css={{
        background: "var(--ax-bg-neutral-moderate)",
        color: "var(--ax-text-neutral)",
        borderRadius: "var(--ax-radius-2)",
        cursor: "default",
        display: "inline-block",
        margin: "0 var(--ax-space-1)",
        outline: focused ? "2px solid var(--ax-border-accent)" : "1px solid var(--ax-border-neutral)",
        padding: "0 var(--ax-space-2)",
        ...(empty && { background: "transparent", margin: 0, outline: "none", padding: 0 }),
        ...(variable.fontType === "BOLD" && { fontWeight: "var(--ax-font-weight-bold)" }),
        ...(variable.fontType === "ITALIC" && { fontStyle: "italic" }),
      }}
      onClick={() => dispatch(moveFocus, { path, content: index, offset: 0 })}
    >
      {variable.text}
    </span>
  );
}
