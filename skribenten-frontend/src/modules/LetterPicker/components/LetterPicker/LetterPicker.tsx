import {FC} from "react"
import styles from "./LetterPicker.module.css"
import LetterButton from "./LetterButton/LetterButton"
import {LetterSelection} from "../../model/skribenten"

interface LetterPickerProps {
    letters: LetterSelection[]
    selectedLetter : string | null,
    onLetterSelected: (id : string | null) => void
}

const LetterPicker: FC<LetterPickerProps> = ({letters,selectedLetter, onLetterSelected}) => {
    return (
        <ul className={styles.letterList}>
            {letters.map(letterData =>
                (<LetterButton text={letterData.name}
                               isSelected={letterData.id === selectedLetter}
                               id={letterData.id}
                               key={letterData.id}
                               onClicked={onLetterSelected}/>)
            )}
        </ul>
    )
}

export default LetterPicker