import type { EditedLetter } from "~/types/brevbakerTypes";

export const EditedLetterTitle = ({ title }: { title: EditedLetter["title"] }) => {
  if (typeof title === "string") {
    return title;
  } else {
    return title.content.map((c) => c.text).join("");
  }
};
