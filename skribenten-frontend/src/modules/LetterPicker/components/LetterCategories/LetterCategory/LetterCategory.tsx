import React, {FC, useState} from 'react'
import styles from "./LetterCategory.module.css"
import {ChevronDownIcon, ChevronUpIcon} from "@navikt/aksel-icons"
import {LetterCategory as Category} from "../../../../../lib/model/skribenten/skribenten"
import LetterPicker from "../../LetterPicker/LetterPicker"

export interface LetterCategoryProps {
    category: Category,
    isExpanded: boolean,
    selectedLetter: string | null,
    onLetterSelected: (id: string | null) => void
}

const LetterCategory: FC<LetterCategoryProps> = ({category, isExpanded, selectedLetter, onLetterSelected}) => {
    const [isActive, setIsActive] = useState(false)
    const expanded = isActive || isExpanded

    const content = (expanded && (
        <div className={`${styles.letterCategoryContainer}`}>
            <LetterPicker letters={category.templates}
                          selectedLetter={selectedLetter}
                          onLetterSelected={onLetterSelected}/>
        </div>
    ))

    return (
        <li className={styles.expansionContainer}>
            <button className={`${expanded ? styles.expansionButtonOpen : ""} ${styles.expansionButton}`}
                    onClick={() => setIsActive(!expanded)}>

                <h2 className={`${expanded ? styles.expansionHeaderOpen : ""} ${styles.expansionHeader}`}>
                    {letterCategoryToName(category.name)}
                </h2>

                {expanded && <ChevronUpIcon title="a11y-title" fontSize="1rem" className={styles.expansionArrow}/>}
                {!expanded && <ChevronDownIcon title="a11y-title" fontSize="1rem" className={styles.expansionArrow}/>}
            </button>
            {content}
        </li>
    )
}

export default LetterCategory

function letterCategoryToName(category: string | null): string{
    switch (category) {
        case "BREV_MED_SKJEMA": return "Brev med skjema"
        case "INFORMASJON": return "Informasjon"
        case "INNHENTE_OPPL": return "Innhente opplysninger"
        case "NOTAT": return "Notat"
        case "OVRIG": return "Øvrig"
        case "VARSEL": return "Varsel"
        case "VEDTAK": return "Vedtak"
        case null: return "Annet"
        default: return category
    }
}