import { PlusIcon, TrashIcon } from "@navikt/aksel-icons";
import { Button, ErrorMessage, HStack, Label, VStack } from "@navikt/ds-react";
import { useFieldArray, useFormContext } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import type { TArray } from "~/types/brevbakerTypes";

import { initValueFromSpec } from "../model";
import { FieldEditor } from "./ObjectEditor";
import { convertFieldToReadableLabel } from "./utils";

export const ArrayEditor = ({
  fieldName,
  fieldType,
  brevkode,
  submitOnChange,
}: {
  fieldName: string;
  fieldType: TArray;
  brevkode: string;
  submitOnChange?: () => void;
}) => {
  const {
    control,
    formState: { errors },
  } = useFormContext();

  const { specification: objectTypes } = useModelSpecification(brevkode, (s) => s.types);

  const { fields, append, remove } = useFieldArray({
    control,
    name: fieldName,
  });

  const arrayError = `${fieldName}.root.message`
    .split(".")
    .reduce<unknown>((acc, key) => (acc as Record<string, unknown>)?.[key], errors) as string | undefined;

  const handleAppend = () => {
    append(initValueFromSpec(objectTypes ?? {}, fieldType.items, false) as object);
    submitOnChange?.();
  };

  const handleRemove = (index: number) => {
    remove(index);
    submitOnChange?.();
  };

  const label = fieldType.displayText ?? convertFieldToReadableLabel(fieldName);

  return (
    <VStack gap="space-8">
      <Label size="small">{label}</Label>
      {fields.map((field, index) => (
        <HStack align="start" gap="space-8" key={field.id}>
          <VStack flexGrow="1" gap="space-8">
            <FieldEditor
              brevkode={brevkode}
              field={String(index)}
              fieldType={fieldType.items}
              prependedName={fieldName}
              submitOnChange={submitOnChange}
            />
          </VStack>
          <Button
            icon={<TrashIcon aria-hidden />}
            onClick={() => handleRemove(index)}
            size="small"
            type="button"
            variant="tertiary-neutral"
          />
        </HStack>
      ))}
      {arrayError && <ErrorMessage size="small">{arrayError}</ErrorMessage>}
      <div>
        <Button icon={<PlusIcon aria-hidden />} onClick={handleAppend} size="small" type="button" variant="secondary">
          Legg til
        </Button>
      </div>
    </VStack>
  );
};
