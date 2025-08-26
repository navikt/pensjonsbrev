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
  const [optionalFields, requiredFields] = specification
    ? partition(Object.entries(specification), (spec) => isFieldNullableOrBoolean(spec[1]))
    : [[], []];
  return {
    status: status,
    optionalFields,
    requiredFields,
    error: error,
  };
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
    /**
     * Enum felter kan foreløpig være non-nullable i mal-spesifikasjonen,
     * men dette håndteres ikke av backend, så et enum-felt uten verdi fører til feil.
     * Derfor legger vi på en validering her, sånn at saksbehandler må ta stilling
     * og frontend slipper å sende ugyldige data til backend.
     */
    for (const field of fieldsWithElements.requiredFields) {
      if (field.fieldType.type === "enum" && !field.fieldType.nullable) {
        register(`saksbehandlerValg.${field.field}`, {
          required: "Obligatorisk: du må velge et alternativ",
        });
      }
    }
  }

  return (
    <>
      <VStack gap="5" marginBlock="space-0 space-16">
        {fieldsWithElements[`${props.fieldsToRender}Fields`].map((field) => field.element)}
      </VStack>
    </>
  );
};

function findSaksbehandlerValgTypeName(modelSpecification: LetterModelSpecification): string {
  const saksbehandlerValgModelSpec =
    modelSpecification.types[modelSpecification.letterModelTypeName]?.saksbehandlerValg;

  // TODO: Bør vi feile her om modelspesifikasjonen mangler deklarasjon for saksbehandlerValg?
  return saksbehandlerValgModelSpec?.type === "object"
    ? saksbehandlerValgModelSpec.typeName
    : modelSpecification.letterModelTypeName;
}
