import {FC} from "react"
import styles from "./LetterPicker.module.css"
import LetterButton from "./LetterButton/LetterButton"
import {LetterMetadata} from "../../model/skribenten"

interface LetterPickerProps {
    letters: LetterMetadata[]
    selectedLetter: string | null,
    onLetterSelected: (id: string) => void
}

const LetterPicker: FC<LetterPickerProps> = ({letters, selectedLetter, onLetterSelected}) =>
    <ul className={styles.letterList}>
        {letters
            .sort((a, b) => a.name.localeCompare(b.name))
            .map(letterData =>
                <LetterButton text={letterData.name}
                              brevsystem={letterData.brevsystem}
                              isSelected={letterData.id === selectedLetter}
                              id={letterData.id}
                              key={letterData.id}
                              onClick={onLetterSelected}/>
            )}
    </ul>

export default LetterPicker