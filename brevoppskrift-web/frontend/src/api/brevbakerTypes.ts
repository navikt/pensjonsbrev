export type TemplateMetadata = {
  displayTitle?: string;
  distribusjonstype?: string;
  brevtype?: string;
};

export type LineSegment = { type: "text"; value: string } | { type: "var"; label: string };
export type Line = LineSegment[];
export type SearchLine = { index: number; segments: LineSegment[] };

export type TemplateDescription = {
  name: string;
  letterDataClass: string;
  languages: string[];
  metadata?: TemplateMetadata | null;
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
