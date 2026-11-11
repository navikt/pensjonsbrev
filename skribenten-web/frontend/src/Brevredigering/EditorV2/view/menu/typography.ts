import { Typography, TypographyToText } from "~/Brevredigering/LetterEditor/utils";
import { type TextBlockTypeV2 } from "~/types/brevV2";

export const TYPOGRAPHY: { type: TextBlockTypeV2; label: string; key: string }[] = [
  { type: "TITLE2", label: TypographyToText[Typography.TITLE1], key: "Digit1" },
  { type: "TITLE3", label: TypographyToText[Typography.TITLE2], key: "Digit2" },
  { type: "TITLE4", label: TypographyToText[Typography.TITLE3], key: "Digit3" },
  { type: "PARAGRAPH", label: TypographyToText[Typography.PARAGRAPH], key: "Digit4" },
];

export function typographyOf(value: string): TextBlockTypeV2 | undefined {
  return TYPOGRAPHY.find((entry) => entry.type === value)?.type;
}
