import {FC} from "react"
import {LetterModelSpecification} from "../LetterEditor/model"
import {BoundAction} from "../../lib/actions"
import ObjectEditor from "./components/ObjectEditor/ObjectEditor"
import {ObjectValue} from "./model"
import styles from "./ModelEditor.module.css"

export interface ModelSpecificationEditorProps {
    spec: LetterModelSpecification
    value: ObjectValue
    updateValue: BoundAction<[value: ObjectValue]>
}

const ModelSpecificationEditor: FC<ModelSpecificationEditorProps> = ({spec, value, updateValue}) => {
    const objectTypeSpec = spec.types[spec.letterModelTypeName]

    return (
        <div className={styles.container}>
            <div><h2>Brevredigering</h2></div>
            <div className={styles.tabmenu}>
                <div>Overstyring</div>
            </div>
            <div>
                <form>
                    <ObjectEditor allSpecs={spec.types} spec={objectTypeSpec} value={value} updateValue={updateValue}/>
                </form>
            </div>
        </div>
    )
}

export default ModelSpecificationEditor