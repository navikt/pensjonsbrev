import { VStack } from "@navikt/ds-react";
import { partition } from "lodash";
import { useFormContext } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import type { FieldType, LetterModelSpecification } from "~/types/brevbakerTypes";

import { FieldEditor } from "./components/ObjectEditor";
import { isBooleanField, isFieldNullableOrBoolean } from "./components/utils";

const useModelSpecificationForm = (brevkode: string) => {
  const brevKodeSpecification = useModelSpecification(brevkode, (s) => s);
  const saksbehandlerValgType = brevKodeSpecification.specification
    ? findSaksbehandlerValgTypeName(brevKodeSpecification.specification)
    : "";
  const saksbehandlerValgSpecification = useModelSpecification(brevkode, (s) => s.types[saksbehandlerValgType]);

  return {
    status:
      brevKodeSpecification.status === "success" ? saksbehandlerValgSpecification.status : brevKodeSpecification.status,
    specification: saksbehandlerValgSpecification.specification,
    error:
      brevKodeSpecification.status === "error" ? brevKodeSpecification.error : saksbehandlerValgSpecification.error,
  };
};

export const usePartitionedModelSpecification = (brevkode: string) => {
  const { status, specification, error } = useModelSpecificationForm(brevkode);

  const [optionalFields, requiredfields] = specification
    ? partition(Object.entries(specification), (spec) => isFieldNullableOrBoolean(spec[1]))
    : [[], []];

  return {
    status: status,
    requiredfields,
    optionalFields,
    error: error,
  };
};

export const createFormElementsFromSpecification = (args: {
  specificationFormElements: {
    requiredfields: [string, FieldType][];
    optionalFields: [string, FieldType][];
  };
  brevkode: string;
  submitOnChange?: () => void;
}) => {
  const requiredFields = args.specificationFormElements.requiredfields.map(([field, fieldType]) => ({
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

  return {
    requiredFields,
    optionalFields,
  };
};

export const SaksbehandlerValgModelEditor = (props: {
  brevkode: string;
  specificationFormElements: {
    requiredfields: [string, FieldType][];
    optionalFields: [string, FieldType][];
  };
  fieldsToRender: "required" | "optional";
  submitOnChange?: () => void;
}) => {
  const { register } = useFormContext();
  const { requiredFields, optionalFields } = createFormElementsFromSpecification({
    specificationFormElements: props.specificationFormElements,
    brevkode: props.brevkode,
    submitOnChange: props.submitOnChange,
  });

  switch (props.fieldsToRender) {
    case "required": {
      /**
       * Boolean felter har spesialbehandling. De er (nesten) alltid non-nullable og er flagg som styrer tekstvalg i malene
       * som regnes som utenfor normen. Vi ønsker derfor ikke å vise dem i Brevvelger, da det ikke er særlig relevant der.
       *
       * Siden disse feltene er non-nullable så betyr det at vi må sende med en verdi for dem i Saksbehandlervalg-objektet,
       * og derfor må de registreres i form-et med false som verdi.
       */
      for (const field of optionalFields) {
        if (isBooleanField(field.fieldType)) {
          register(`saksbehandlerValg.${field.field}`, { value: false });
        }
      }
      return (
        <VStack gap="5" marginBlock="space-0 space-16">
          {requiredFields.map((field) => field.element)}
        </VStack>
      );
    }
    case "optional": {
      return (
        <VStack gap="5" marginBlock="space-0 space-16">
          {optionalFields.map((field) => field.element)}
        </VStack>
      );
    }
  }
};

function findSaksbehandlerValgTypeName(modelSpecification: LetterModelSpecification): string {
  const saksbehandlerValgModelSpec =
    modelSpecification.types[modelSpecification.letterModelTypeName]?.saksbehandlerValg;

  // TODO: Bør vi feile her om modelspesifikasjonen mangler deklarasjon for saksbehandlerValg?
  return saksbehandlerValgModelSpec?.type === "object"
    ? saksbehandlerValgModelSpec.typeName
    : modelSpecification.letterModelTypeName;
}
