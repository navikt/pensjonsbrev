import { css } from "@emotion/react";
import { VStack } from "@navikt/ds-react";
import { Link, useSearch } from "@tanstack/react-router";
import { capitalize } from "lodash";
import { useRef } from "react";

import type { FieldType, LetterModelSpecification, ObjectTypeSpecification } from "~/api/brevbakerTypes";

export function DataClasses({ templateModelSpecification }: { templateModelSpecification: LetterModelSpecification }) {
  return (
    <VStack
      css={css`
        position: fixed;
        width: 800px;
        max-width: 20%;
        background: var(--a-bg-subtle);
        left: 0;
        height: 100%;
        border-right: 1px solid var(--a-border-divider);
        padding: var(--a-spacing-4);
        white-space: nowrap;
        overflow: scroll;
      `}
      gap="4"
    >
      {Object.entries(templateModelSpecification.types).map(([name, value]) => (
        <DataView key={name} name={name} objectTypeSpecification={value} />
      ))}
    </VStack>
  );
}

function DataView({
  name,
  objectTypeSpecification,
}: {
  name: string;
  objectTypeSpecification: ObjectTypeSpecification;
}) {
  const { highlightedDataClass } = useSearch({ from: "/template/$malType/$templateId" });
  const reference = useRef<HTMLSpanElement>(null);

  const isHighlighted = highlightedDataClass === trimClassName(name);
  if (isHighlighted && reference.current) {
    reference.current.scrollIntoView({ behavior: "smooth", block: "center" });
  }

  return (
    <VStack className={isHighlighted ? "highlight" : undefined} gap="1">
      <span ref={reference}>
        <span
          css={css`
            color: var(--a-red-500);
          `}
        >
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

  if (isHighlighted && reference.current) {
    reference.current.scrollIntoView({ behavior: "smooth", block: "center" });
  }

  return (
    <span
      className={isHighlighted ? "highlight" : undefined}
      css={[
        css`
          margin-left: var(--a-spacing-16);
        `,
      ]}
      key={name}
      ref={reference}
    >
      {name}: <Type fieldType={fieldType} />
      {fieldType.nullable ? "?" : ""}
    </span>
  );
}

function Type({ fieldType }: { fieldType: FieldType }) {
  switch (fieldType.type) {
    case "scalar": {
      return (
        <span
          css={css`
            color: var(--a-purple-500);
          `}
        >
          {capitalize(fieldType.kind)}
        </span>
      );
    }
    case "enum": {
      return (
        <span
          css={css`
            color: var(--a-green-500);
          `}
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
