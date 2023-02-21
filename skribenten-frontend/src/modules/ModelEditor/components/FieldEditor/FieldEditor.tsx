import {FieldType, ObjectTypeSpecifications, TArray, TEnum, TObject, TScalar} from "../../../LetterEditor/model"
import {BoundAction} from "../../../../lib/actions"
import {FC} from "react"
import {FieldValue, initValueFromSpec} from "../../model"
import ScalarEditor from "../ScalarEditor/ScalarEditor"
import EnumEditor from "../EnumEditor/EnumEditor"
import ObjectEditor from "../ObjectEditor/ObjectEditor"
import ArrayEditor from "../ArrayEditor/ArrayEditor"

export interface FieldEditorProps {
    allSpecs: ObjectTypeSpecifications
    spec: FieldType
    value: FieldValue<FieldType> | null
    updateValue: BoundAction<[value: FieldValue<FieldType>]>
}

const FieldEditor: FC<FieldEditorProps> = ({allSpecs, spec, value, updateValue}) => {
    if (value == null) {
        if (spec.nullable) {
            return <button type="button" onClick={() => updateValue(initValueFromSpec(allSpecs, spec, false))}>Give value</button>
        } else {
            // Since the entire model should be initialized we shouldn't end up here, but it is technically possible.
            updateValue(initValueFromSpec(allSpecs, spec, false))
            return <div>Initierer verdi</div>
        }
    } else {
        switch (spec.type) {
            case "object":
                return <ObjectEditor allSpecs={allSpecs} spec={allSpecs[spec.typeName]} value={value as FieldValue<TObject>} updateValue={updateValue}/>
            case "scalar":
                return <ScalarEditor spec={spec} value={value as FieldValue<TScalar>} updateValue={updateValue}/>
            case "array":
                return <ArrayEditor allSpecs={allSpecs} spec={spec} array={value as FieldValue<TArray>} updateArray={updateValue}/>
            case "enum":
                return <EnumEditor spec={spec} value={value as FieldValue<TEnum>} updateValue={updateValue}/>
        }
    }
}

export default FieldEditor
