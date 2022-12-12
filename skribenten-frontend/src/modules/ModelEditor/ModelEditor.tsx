import {FC} from "react"
import {LetterModelSpecification} from "../LetterEditor/model"
import {BoundAction} from "../../lib/actions"
import ObjectEditor from "./components/ObjectEditor/ObjectEditor"
import {ObjectValue} from "./model"

export interface ModelSpecificationEditorProps {
    spec: LetterModelSpecification
    value: ObjectValue
    updateValue: BoundAction<[value: ObjectValue]>
}

const ModelSpecificationEditor: FC<ModelSpecificationEditorProps> = ({spec, value, updateValue}) => {
    const objectTypeSpec = spec.types[spec.letterModelTypeName]

    return (
        <form>
            <ObjectEditor allSpecs={spec.types} spec={objectTypeSpec} value={value} updateValue={updateValue}/>
        </form>
    )
}

export default ModelSpecificationEditor