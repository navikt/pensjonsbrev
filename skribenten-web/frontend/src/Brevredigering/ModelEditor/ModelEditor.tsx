import { BodyShort, VStack } from "@navikt/ds-react";
import { partition } from "lodash";

import { useModelSpecification } from "~/api/brev-queries";
import { ApiError } from "~/components/ApiError";
import type { FieldType, LetterModelSpecification } from "~/types/brevbakerTypes";

import { FieldEditor } from "./components/ObjectEditor";

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

const usePartitionedModelSpecification = (brevkode: string) => {
  const { status, specification, error } = useModelSpecificationForm(brevkode);

  const [optionalFields, requiredfields] = specification
    ? partition(Object.entries(specification), (spec) => spec[1].nullable)
    : [[], []];

  return {
    status: status,
    requiredfields,
    optionalFields,
    error: error,
  };
};

const createFormElementsFromSpecification = (
  specificationFormElements: {
    requiredfields: [string, FieldType][];
    optionalFields: [string, FieldType][];
  },
  brevkode: string,
  submitOnChange?: () => void,
) => {
  const requiredFields = specificationFormElements.requiredfields.map(([field, fieldType]) => (
    <FieldEditor
      brevkode={brevkode}
      field={field}
      fieldType={fieldType}
      key={field}
      prependedName="saksbehandlerValg"
      submitOnChange={submitOnChange}
    />
  ));

  const optionalFields = specificationFormElements.optionalFields.map(([field, fieldType]) => (
    <FieldEditor
      brevkode={brevkode}
      field={field}
      fieldType={fieldType}
      key={field}
      prependedName="saksbehandlerValg"
      submitOnChange={submitOnChange}
    />
  ));

  return {
    requiredFields,
    optionalFields,
  };
};

export const SaksbehandlerValgModelEditor = (props: {
  brevkode: string;
  fieldsToRender: "required" | "optional";
  submitOnChange?: () => void;
}) => {
  const specificationFormElements = usePartitionedModelSpecification(props.brevkode);
  const { requiredFields, optionalFields } = createFormElementsFromSpecification(
    specificationFormElements,
    props.brevkode,
    props.submitOnChange,
  );

  switch (specificationFormElements.status) {
    case "error": {
      return <ApiError error={specificationFormElements.error} title={"En feil skjedde"} />;
    }
    case "success": {
      switch (props.fieldsToRender) {
        case "required": {
          return <VStack gap="6">{...requiredFields}</VStack>;
        }
        case "optional": {
          return <VStack gap="6">{...optionalFields}</VStack>;
        }
      }

      break;
    }
    case "pending": {
      return <BodyShort size="small">Henter skjema for saksbehandler valg...</BodyShort>;
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
