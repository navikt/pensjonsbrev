import { Box, VStack } from "@navikt/ds-react";
import { Link, useSearch } from "@tanstack/react-router";
import { capitalize } from "lodash";
import { useEffect, useRef } from "react";

import { type FieldType, type LetterModelSpecification, type ObjectTypeSpecification } from "~/api/brevbakerTypes";

export function DataClasses({ templateModelSpecification }: { templateModelSpecification: LetterModelSpecification }) {
  return (
    <Box asChild background="neutral-soft" borderColor="neutral-subtle" borderWidth="1">
      <VStack css={{ whiteSpace: "nowrap" }} gap="space-16" height="100%" overflow="auto" padding="space-16">
        {Object.entries(templateModelSpecification.types).map(([name, value]) => (
          <DataPresentation key={name} name={name} objectTypeSpecification={value} />
        ))}
      </VStack>
    </Box>
  );
}

function DataPresentation({
  name,
  objectTypeSpecification,
}: {
  name: string;
  objectTypeSpecification: ObjectTypeSpecification;
}) {
  const { highlightedDataClass } = useSearch({ from: "/template/$malType/$templateId" });
  const reference = useRef<HTMLSpanElement>(null);

  const isHighlighted = highlightedDataClass === trimClassName(name);

  useEffect(() => {
    if (isHighlighted && reference.current) {
      reference.current.scrollIntoView({ behavior: "smooth", block: "center" });
    }
  }, [isHighlighted]);

  return (
    <VStack className={isHighlighted ? "highlight" : undefined} gap="space-4" minWidth="100%" width="fit-content">
      <span ref={reference}>
        <span
          css={{
            color: "var(--ax-danger-600)",
          }}
        >
          {" "}
          data class
        </span>{" "}
        {trimClassName(name)}(
      </span>
      {Object.entries(objectTypeSpecification).map(([key, value]) => (
        <DataField fieldType={value} key={key} name={key} />
      ))}
      <span>)</span>
    </VStack>
  );
}

function DataField({ name, fieldType }: { name: string; fieldType: FieldType }) {
  const { highlightedDataField } = useSearch({ from: "/template/$malType/$templateId" });
  const reference = useRef<HTMLSpanElement>(null);

  const isHighlighted = highlightedDataField === name;

  useEffect(() => {
    if (isHighlighted && reference.current) {
      reference.current.scrollIntoView({ behavior: "smooth", block: "center" });
    }
  }, [isHighlighted]);

  return (
    <Box asChild marginInline="space-32 space-0">
      <span className={isHighlighted ? "highlight" : undefined} key={name} ref={reference}>
        {name}: <Type fieldType={fieldType} />
        {fieldType.nullable ? "?" : ""}
      </span>
    </Box>
  );
}

function Type({ fieldType }: { fieldType: FieldType }) {
  switch (fieldType.type) {
    case "scalar": {
      return (
        <span
          css={{
            color: "var(--ax-meta-purple-600)",
          }}
        >
          {capitalize(fieldType.kind)}
        </span>
      );
    }
    case "enum": {
      return (
        <span
          css={{
            color: "var(--ax-success-600)",
          }}
        >
          {fieldType.values.join(" | ")}
        </span>
      );
    }
    case "array": {
      return (
        <span>
          <Type fieldType={fieldType.items} />
          []
        </span>
      );
    }
    case "object": {
      return (
        <Link
          from="/template/$malType/$templateId"
          preload={false}
          replace
          search={(s) => ({ ...s, highlightedDataClass: trimClassName(fieldType.typeName).replace("?", "") })}
        >
          {trimClassName(fieldType.typeName)}
        </Link>
      );
    }
  }
}

export function trimClassName(className: string) {
  return className.replace(/(.*)[$.]/, "");
}
