import React, {FC, useState} from 'react';
import styles from "./LetterCategory.module.css"
import {ChevronDownIcon, ChevronUpIcon} from "@navikt/aksel-icons";
import LetterButton from "../../LetterPicker/LetterButton/LetterButton";
import {LetterCategory, LetterMetaData} from "../../../LetterFilter";

export interface LetterCategoryProps {
    category: LetterCategory,
    isExpanded: boolean,
}

const LetterCategory: FC<LetterCategoryProps> = ({category,isExpanded}) => {
    const [isActive, setIsActive] = useState(false)
    const expanded = isActive || isExpanded

    const content = (expanded && (
        <div className={styles.letterCategoryContainer}>
            {category.letters.map(letter=> (<LetterButton text={letter.name} id={letter.id}/>))}
        </div>
    ))

    return (
        <div className={styles.expansionContainer}>
            <button className={`${expanded ? styles.expansionButtonOpen : ""} ${styles.expansionButton}`}
                    onClick={() => setIsActive(!expanded)}>

                <h2 className={`${expanded ? styles.expansionHeaderOpen : ""} ${styles.expansionHeader}`}>
                    {category.name}
                </h2>

                {expanded && <ChevronUpIcon title="a11y-title" fontSize="1rem" className={styles.expansionArrowUp}/>}
                {!expanded && <ChevronDownIcon title="a11y-title" fontSize="1rem" className={styles.expansionArrowDown}/>}
            </button>
            {content}
        </div>
    );
};

export default LetterCategory;