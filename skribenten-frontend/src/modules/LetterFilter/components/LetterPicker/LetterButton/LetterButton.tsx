import React, {FC} from 'react';
import styles from "./LetterButton.module.css"

interface LetterButtonProps {
    text: string,
    id: string
}

const LetterButton: FC<LetterButtonProps>= (props) => (
    <button className={styles.selectLetterButton}>
        {props.text}
    </button>
);

export default LetterButton;