import type {
  FieldType,
  ObjectTypeSpecification,
  ObjectTypeSpecifications,
  TArray,
  TEnum,
  TObject,
  TScalar,
} from "~/types/brevbakerTypes";

export type ScalarValue = number | string | boolean;

export interface ObjectValue {
  [field: string]: FieldValue<FieldType>;
}

export type ArrayValue = FieldValue<FieldType>[];

export type FieldValue<T extends FieldType> = T extends TEnum
  ? string
  : T extends TScalar
    ? ScalarValue
    : T extends TArray
      ? ArrayValue
      : T extends TObject
        ? ObjectValue
        : never;

export function initValueFromSpec<T extends FieldType>(
  objectSpecs: ObjectTypeSpecifications,
  spec: T,
  nullIfNullable?: boolean,
): FieldValue<T>;
export function initValueFromSpec(
  objectSpecs: ObjectTypeSpecifications,
  spec: FieldType,
  nullIfNullable = true,
): FieldValue<FieldType> | null {
  if (spec.nullable && nullIfNullable) {
    return null;
  } else {
    switch (spec.type) {
      case "scalar": {
        switch (spec.kind) {
          case "NUMBER": {
            return 0;
          }
          case "DOUBLE": {
            return 0;
          }
          case "STRING": {
            return "";
          }
          case "BOOLEAN": {
            return false;
          }
          case "DATE": {
            return "";
          }
          case "YEAR": {
            return 0;
          }
          default: {
            throw `Unsupported scalar kind ${spec.kind} for: ${JSON.stringify(spec)}`;
          }
        }
      }
      case "enum": {
        return spec.values[0];
      }
      case "array": {
        return [];
      }
      case "object": {
        return initObjectFromSpec(objectSpecs, objectSpecs[spec.typeName]);
      }
    }
  }
}

export function initObjectFromSpec(allSpecs: ObjectTypeSpecifications, spec: ObjectTypeSpecification): ObjectValue {
  const object: ObjectValue = {};
  for (const [field, fieldSpec] of Object.entries(spec)) {
    object[field] = initValueFromSpec(allSpecs, fieldSpec);
  }
  return object;
}
