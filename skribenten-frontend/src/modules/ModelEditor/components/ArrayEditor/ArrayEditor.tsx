import {FieldType, ObjectTypeSpecifications, TArray} from "../../../LetterEditor/model/api"
import {bindAction, BoundAction} from "../../../../lib/actions"
import {FieldValue, initValueFromSpec} from "../../model"
import {FC, useState} from "react"
import FieldEditor from "../FieldEditor/FieldEditor"
import {ModelValueAction} from "../../actions"

interface AddArrayValueProps {
    allSpecs: ObjectTypeSpecifications
    spec: FieldType
    addValue: BoundAction<[newValue: FieldValue<FieldType>]>
}
const AddArrayValue: FC<AddArrayValueProps> = ({allSpecs, spec, addValue}) => {
    const [value, updateValue] = useState(initValueFromSpec(allSpecs, spec, false))
    const doAddValue = () => {
        addValue(value)
        updateValue(initValueFromSpec(allSpecs, spec, false))
    }
    return (
        <div>
            <FieldEditor allSpecs={allSpecs} spec={spec} value={value} updateValue={updateValue}/>
            <button type="button" onClick={doAddValue}>Add</button>
        </div>
    )
}

export interface ArrayEditorProps {
    allSpecs: ObjectTypeSpecifications
    spec: TArray
    array: FieldValue<TArray>
    updateArray: BoundAction<[FieldValue<TArray>]>
}

const ArrayEditor: FC<ArrayEditorProps> = ({allSpecs, spec, array, updateArray}) => (
    <div>
        <ul>
            {array.map((item: FieldValue<FieldType>, index: number) =>
                <li key={index}>
                    <FieldEditor allSpecs={allSpecs} spec={spec.items} value={item} updateValue={bindAction(ModelValueAction.UpdateArrayIndex, updateArray, array, index)}/>
                </li>
            )}
        </ul>
        <AddArrayValue allSpecs={allSpecs} spec={spec.items} addValue={bindAction(ModelValueAction.AddToArray, updateArray, array)}/>
    </div>
)

export default ArrayEditor