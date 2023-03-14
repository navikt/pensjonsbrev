import {FieldType, ObjectTypeSpecification, ObjectTypeSpecifications, TArray, TEnum, TObject, TScalar} from "../LetterEditor/model/api"

export type ScalarValue = number | string | boolean

export interface ObjectValue {
    [field: string]: FieldValue<FieldType>
}

export type ArrayValue = FieldValue<FieldType>[]

export type FieldValue<T extends FieldType> =
    T extends TEnum ? string :
    T extends TScalar ? ScalarValue :
    T extends TArray ? ArrayValue :
    T extends TObject ? ObjectValue :
    never


export function initValueFromSpec<T extends FieldType>(objSpecs: ObjectTypeSpecifications, spec: T, nullIfNullable?: boolean): FieldValue<T>
export function initValueFromSpec(objSpecs: ObjectTypeSpecifications, spec: FieldType, nullIfNullable = true): FieldValue<FieldType> | null {
    if (spec.nullable && nullIfNullable) {
        return null
    } else {
        switch (spec.type) {
            case "scalar":
                switch (spec.kind) {
                    case "NUMBER":
                        return 0
                    case "DOUBLE":
                        return 0.0
                    case "STRING":
                        return ""
                    case "BOOLEAN":
                        return false
                    case "DATE":
                        return ""
                    default:
                        throw `Unsupported scalar kind ${spec.kind} for: ${JSON.stringify(spec)}`
                }
            case "enum":
                return spec.values[0]
            case "array":
                return []
            case "object":
                return initObjectFromSpec(objSpecs, objSpecs[spec.typeName])
        }
    }
}

export function initObjectFromSpec(allSpecs: ObjectTypeSpecifications, spec: ObjectTypeSpecification): ObjectValue {
    const obj: ObjectValue = {}
    Object.entries(spec).forEach(([field, fieldSpec]) => {
        obj[field] = initValueFromSpec(allSpecs, fieldSpec)
    })
    return obj
}