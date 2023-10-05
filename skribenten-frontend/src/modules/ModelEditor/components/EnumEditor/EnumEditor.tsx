import {TEnum} from "../../../../lib/model/skribenten"
import {FieldValue} from "../../model"
import {BoundAction} from "../../../../lib/actions"
import {FC} from "react"

interface EnumEditorProps {
    spec: TEnum
    value: FieldValue<TEnum>
    updateValue: BoundAction<[FieldValue<TEnum>]>
}

const EnumEditor: FC<EnumEditorProps> = ({spec, value, updateValue}) => (
    <select value={value} onChange={e => updateValue(e.target.value)}>
        {spec.values.map(opt =>
            <option key={opt} value={opt}>{opt}</option>
        )}
    </select>
)

export default EnumEditor