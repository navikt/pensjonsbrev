import type { EditedLetter } from "~/types/brevbakerTypes";

export const EditedLetterTitle = ({ letter }: { letter: EditedLetter }) => {
  if (typeof letter.title === "string") {
    return letter.title;
  } else {
    return letter.title.content.map((c) => c.text).join("");
  }
};
