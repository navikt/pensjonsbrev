import {TScalar} from "../../../LetterEditor/model"
import {BoundAction} from "../../../../lib/actions"
import {ScalarValue} from "../../model"
import {FC} from "react"

export interface ScalarEditorProps {
    spec: TScalar
    value: ScalarValue
    updateValue: BoundAction<[value: ScalarValue]>
}

const ScalarEditor: FC<ScalarEditorProps> = ({spec, value, updateValue}: ScalarEditorProps) => {
    switch (spec.kind) {
        case "NUMBER":
            return <input type="number" required={!spec.nullable} step={1} value={value as number} onChange={e => updateValue(e.target.value)}/>
        case "DOUBLE":
            return <input type="number" required={!spec.nullable} step={0.1} value={value as number} onChange={e => updateValue(e.target.value)}/>
        case "STRING":
            return <input type="text" required={!spec.nullable} value={value as string} onChange={e => updateValue(e.target.value)}/>
        case "BOOLEAN":
            return <input type="checkbox" checked={value as boolean} onChange={e => updateValue(e.target.checked)}/>
        case "DATE":
            return <input type="date" required={!spec.nullable} value={value as string} onChange={e => updateValue(e.target.value)}/>
    }
}

export default ScalarEditor