import {FC} from "react";
import styles from "./LetterPicker.module.css"
import {LetterMetaData} from "../../LetterFilter";
import LetterButton from "./LetterButton/LetterButton";

interface PickerContainerProps {
    letters: LetterMetaData[]
}

const LetterPicker:FC<PickerContainerProps> = (props) => {
    return (
        <div className={styles.letterCategoryContainer}>
            {props.letters.map(letterData=>
                (<LetterButton text={letterData.name} id={letterData.id}/>)
            )}
        </div>
    );
};

export default LetterPicker;