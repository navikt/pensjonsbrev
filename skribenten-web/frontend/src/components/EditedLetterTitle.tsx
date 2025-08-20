import type { EditedLetter } from "~/types/brevbakerTypes";

export const EditedLetterTitle = ({ title }: { title: EditedLetter["title"] }) => {
  return title.text.map((c) => c.text).join("");
};
