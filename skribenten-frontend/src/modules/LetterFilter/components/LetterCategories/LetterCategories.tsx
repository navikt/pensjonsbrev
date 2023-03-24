import React, {FC} from "react";
import styles from "./LetterCategories.module.css"
import {LetterCategory as Category}  from "../../LetterFilter";
import LetterCategory from "./LetterCategory/LetterCategory";

interface LetterCategoriesProps {
    categories: Category[],
    expanded: boolean,
}


const LetterCategories: FC<LetterCategoriesProps> = ({categories, expanded}) => {
    return (
        <div className={styles.letterCategories}>
            {categories.filter((category: Category) => category.letters.length > 0)
                .map((category: Category) =>(<LetterCategory category={category} isExpanded={expanded}/>))
            }
        </div>
    )
}

export default LetterCategories