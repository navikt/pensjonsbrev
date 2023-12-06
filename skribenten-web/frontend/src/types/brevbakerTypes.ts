import type { LetterMetadata } from "./apiTypes";

export type RedigerbarTemplateDescription = {
  readonly description: TemplateDescription;
  readonly modelSpecification: LetterModelSpecification;
};

export type TemplateDescription = {
  readonly name: string;
  readonly letterDataClass: string;
  readonly languages: LanguageCode[];
  readonly metadata: LetterMetadata;
};

export type LetterModelSpecification = {
  readonly types: ObjectTypeSpecifications;
  readonly letterModelTypeName: string;
};

export type ObjectTypeSpecifications = {
  readonly [name: string]: ObjectTypeSpecification;
};

export type ObjectTypeSpecification = {
  readonly [field: string]: FieldType;
};

export type FieldType = TScalar | TEnum | TArray | TObject;

export type TScalar = {
  readonly type: "scalar";
  readonly nullable: boolean;
  readonly kind: ScalarKind;
};
export type TEnum = {
  readonly type: "enum";
  readonly nullable: boolean;
  readonly values: string[];
};
export type TArray = {
  readonly type: "array";
  readonly nullable: boolean;
  readonly items: FieldType;
};
export type TObject = {
  readonly type: "object";
  readonly nullable: boolean;
  readonly typeName: string;
};

export type ScalarKind = "NUMBER" | "DOUBLE" | "STRING" | "BOOLEAN" | "DATE";

export type LanguageCode = "BOKMAL" | "NYNORSK" | "ENGLISH";
