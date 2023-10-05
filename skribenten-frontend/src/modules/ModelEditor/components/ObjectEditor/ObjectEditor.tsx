import {ObjectTypeSpecification, ObjectTypeSpecifications, TObject} from "../../../../lib/model/skribenten"
import {FieldValue} from "../../model"
import {bindAction, BoundAction} from "../../../../lib/actions"
import {FC} from "react"
import FieldEditor from "../FieldEditor/FieldEditor"
import styles from "./ObjectEditor.module.css"
import {ModelValueAction} from "../../actions"

export interface ObjectEditorProps {
    allSpecs: ObjectTypeSpecifications
    spec: ObjectTypeSpecification
    value: FieldValue<TObject>
    updateValue: BoundAction<[value: FieldValue<TObject>]>
}

const ObjectEditor: FC<ObjectEditorProps> = ({allSpecs, spec, value, updateValue}) => {
    return (
        <div className={styles.container}>
            {Object.entries(spec).map(([name, field]) => {
                    const updateField = bindAction(ModelValueAction.UpdateField, updateValue, value, name)
                    return (
                        <div key={name}>
                            <label>
                                {name}:
                                {field.nullable && value[name] != null && <button type="button" onClick={() => updateField(null)}>Remove</button>}
                                <FieldEditor allSpecs={allSpecs} spec={field} value={value[name]} updateValue={updateField}/>
                            </label>
                        </div>
                    )
                }
            )}
        </div>
    )
}

export default ObjectEditor