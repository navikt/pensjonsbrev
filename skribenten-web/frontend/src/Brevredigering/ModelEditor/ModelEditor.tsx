import { VStack } from "@navikt/ds-react";
import { partition } from "lodash";
import { useMemo } from "react";
import { useFormContext } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { type FieldType, type LetterModelSpecification, type PropertyUsage } from "~/types/brevbakerTypes";

import { FieldEditor } from "./components/ObjectEditor";
import { isBooleanField, isFieldNullableOrBoolean } from "./components/utils";

export const useModelSpecificationForm = (brevkode: string) => {
  const brevKodeSpecification = useModelSpecification(brevkode, (s) => s);

  const saksbehandlerValgType = brevKodeSpecification.specification
    ? findSaksbehandlerValgTypeName(brevKodeSpecification.specification)
    : undefined;

  const saksbehandlerValgSpecification = useModelSpecification(brevkode, (s) =>
    saksbehandlerValgType ? s.types[saksbehandlerValgType] : undefined,
  );

  return {
    status:
      brevKodeSpecification.status === "success" ? saksbehandlerValgSpecification.status : brevKodeSpecification.status,
    specification: saksbehandlerValgSpecification.specification,
    saksbehandlerValgType,
    error:
      brevKodeSpecification.status === "error" ? brevKodeSpecification.error : saksbehandlerValgSpecification.error,
  };
};

export const extractRelevantSaksbehandlerValgFields = (
  propertyUsage: PropertyUsage[],
  saksbehandlerValgType: string | undefined | null,
): Set<string> => {
  if (!saksbehandlerValgType) {
    return new Set();
  }
  return new Set(
    propertyUsage.filter((usage) => usage.typeName === saksbehandlerValgType).map((usage) => usage.propertyName),
  );
};

export const filterModelSpecificationByPropertyUsage = (
  specification: Record<string, FieldType> | undefined,
  propertyUsage: PropertyUsage[] | undefined,
  saksbehandlerValgType: string | undefined | null,
) => {
  if (!specification || propertyUsage === undefined || !saksbehandlerValgType) {
    return specification;
  }

  const relevantFields = extractRelevantSaksbehandlerValgFields(propertyUsage, saksbehandlerValgType);
  return Object.fromEntries(Object.entries(specification).filter(([field]) => relevantFields.has(field)));
};

export const usePartitionedModelSpecification = (brevkode: string, propertyUsage?: PropertyUsage[]) => {
  const { status, specification, error, saksbehandlerValgType } = useModelSpecificationForm(brevkode);

  const filteredSpecification = useMemo(
    () => filterModelSpecificationByPropertyUsage(specification, propertyUsage, saksbehandlerValgType),
    [specification, propertyUsage, saksbehandlerValgType],
  );

  const [optionalFields, requiredFields] = filteredSpecification
    ? partition(Object.entries(filteredSpecification), (spec) => isFieldNullableOrBoolean(spec[1]))
    : [[], []];
  return { status, optionalFields, requiredFields, error };
};

export const createFormElementsFromSpecification = (args: {
  specificationFormElements: {
    optionalFields: [string, FieldType][];
    requiredFields: [string, FieldType][];
  };
  brevkode: string;
  submitOnChange?: () => void;
}) => {
  const optionalFields = args.specificationFormElements.optionalFields.map(([field, fieldType]) => ({
    field: field,
    fieldType: fieldType,
    element: (
      <FieldEditor
        brevkode={args.brevkode}
        field={field}
        fieldType={fieldType}
        key={field}
        prependedName="saksbehandlerValg"
        submitOnChange={args.submitOnChange}
      />
    ),
  }));

  const requiredFields = args.specificationFormElements.requiredFields.map(([field, fieldType]) => ({
    field: field,
    fieldType: fieldType,
    element: (
      <FieldEditor
        brevkode={args.brevkode}
        field={field}
        fieldType={fieldType}
        key={field}
        prependedName="saksbehandlerValg"
        submitOnChange={args.submitOnChange}
      />
    ),
  }));

  return {
    optionalFields,
    requiredFields,
  };
};

export const SaksbehandlerValgModelEditor = (props: {
  brevkode: string;
  specificationFormElements: {
    optionalFields: [string, FieldType][];
    requiredFields: [string, FieldType][];
  };
  fieldsToRender: "required" | "optional";
  submitOnChange?: () => void;
}) => {
  const { register } = useFormContext();
  const fieldsWithElements = createFormElementsFromSpecification({
    specificationFormElements: props.specificationFormElements,
    brevkode: props.brevkode,
    submitOnChange: props.submitOnChange,
  });

  if (props.fieldsToRender === "required") {
    for (const field of fieldsWithElements.requiredFields) {
      if (!field.fieldType.nullable) {
        register(`saksbehandlerValg.${field.field}`, {
          required: "Obligatorisk: du må velge et alternativ",
        });
      }
    }

    /**
     * Boolean felter har spesialbehandling. De er (nesten) alltid non-nullable og er flagg som styrer tekstvalg i malene
     * som regnes som utenfor normen. Vi ønsker derfor ikke å vise dem i Brevvelger, da det ikke er særlig relevant der.
     *
     * Siden disse feltene er non-nullable så betyr det at vi må sende med en verdi for dem i Saksbehandlervalg-objektet,
     * og derfor må de registreres i form-et med false som verdi.
     */
    for (const field of fieldsWithElements.optionalFields) {
      if (isBooleanField(field.fieldType)) {
        register(`saksbehandlerValg.${field.field}`, { value: false });
      }
    }
  }

  return (
    <VStack gap="space-20" marginBlock="space-0 space-20">
      {fieldsWithElements[`${props.fieldsToRender}Fields`].map((field) => field.element)}
    </VStack>
  );
};

function findSaksbehandlerValgTypeName(modelSpecification: LetterModelSpecification): string | null {
  const brevModel = modelSpecification?.letterModelTypeName
    ? modelSpecification.types[modelSpecification.letterModelTypeName]
    : null;

  const saksbehandlerValgModelSpec = brevModel?.saksbehandlerValg;

  return saksbehandlerValgModelSpec?.type === "object" ? saksbehandlerValgModelSpec.typeName : null;
}
