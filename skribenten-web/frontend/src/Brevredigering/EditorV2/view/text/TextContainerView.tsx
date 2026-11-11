import { Fragment } from "react";

import { type TextV2 } from "~/types/brevV2";

import { containerAttributes } from "../../caret/domCaret";
import { DeletedContentAtV2 } from "../../diff/DeletedMarkupV2";
import { type ContainerPath } from "../../model/paths";
import { textOf } from "../../model/text";
import { EditableLiteral } from "./EditableLiteral";
import { VariableView } from "./VariableView";

const WORD_JOINER = "\u2060";
const NO_BREAK_PUNCTUATION = /^[.,;:!?'"()[\]{}°…«»%\u201D\u2019]/;

function startsWithPunctuation(text: TextV2): boolean {
  return text.type === "LITERAL" && NO_BREAK_PUNCTUATION.test(textOf(text));
}

function TextView({ path, index, text }: { path: ContainerPath; index: number; text: TextV2 }) {
  switch (text.type) {
    case "LITERAL":
      return (
        <>
          {index > 0 && startsWithPunctuation(text) && WORD_JOINER}
          <EditableLiteral index={index} literal={text} path={path} />
        </>
      );
    case "VARIABLE":
      return <VariableView index={index} path={path} variable={text} />;
    case "NEW_LINE":
      return <br />;
  }
}

export function TextContainerView({ path, content }: { path: ContainerPath; content: TextV2[] }) {
  return (
    <span {...containerAttributes(path)}>
      {content.map((text, index) => (
        <Fragment key={index}>
          <DeletedContentAtV2 content={index} path={path} />
          <TextView index={index} path={path} text={text} />
        </Fragment>
      ))}
      <DeletedContentAtV2 content={content.length} path={path} trailing />
    </span>
  );
}
