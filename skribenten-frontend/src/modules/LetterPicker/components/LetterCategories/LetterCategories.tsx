import React, {FC} from "react"
import styles from "./LetterCategories.module.css"
import {LetterCategory as Category} from "../../../../lib/model/skribenten/skribenten"
import LetterCategory from "./LetterCategory/LetterCategory"

interface LetterCategoriesProps {
    categories: Category[],
    expanded: boolean,
    selectedLetter : string | null,
    onLetterSelected: (id : string | null) => void
}


const LetterCategories: FC<LetterCategoriesProps> = ({categories, expanded, selectedLetter, onLetterSelected}) => {
    return (
        <ul className={styles.letterCategories}>
            {categories.filter((category: Category) => category.templates.length > 0)
                .map((category: Category) => (
                    <LetterCategory
                        category={category}
                        key={category.name}
                        isExpanded={expanded}
                        selectedLetter={selectedLetter}
                        onLetterSelected={onLetterSelected}/>))
            }
        </ul>
    )
}

export default LetterCategories