import React, {FC} from 'react';
import styles from "./LetterButton.module.css"

interface LetterButtonProps {
    text: string,
    id: string,
    isSelected: boolean,
    onClicked: (id: string | null) => void,
}

const LetterButton: FC<LetterButtonProps> = ({text, id, isSelected = false, onClicked}) =>
    <li className={styles.buttonElement}>
        <button className={`${isSelected ? styles.letterButtonOpen : ""} ${styles.letterButton}`}
                         onClick={() => onClicked(isSelected ? null : id)}>
            {text}
        </button>
    </li>

export default LetterButton