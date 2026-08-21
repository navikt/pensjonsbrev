// Re-export barrel for helpers used by LetterEditor tests.
// Named rather than star re-exports, so a future name collision between the two
// support modules fails at compile time instead of being silently dropped by ESM.
export { brevInfo, brevResponse, editedLetter, signatur } from "~test/support/brevFixtures";
export {
  asNew,
  cell,
  item,
  itemList,
  letter,
  literal,
  newLine,
  type ParagraphArgs,
  paragraph,
  row,
  select,
  table,
  title1,
  title2,
  title3,
  variable,
  withDeleted,
  withMissingFromTemplate,
  withParent,
} from "~test/support/letterEditorTestUtils";
