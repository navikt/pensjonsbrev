import { css } from "@emotion/react";
import { ArrowLeftIcon, XMarkIcon } from "@navikt/aksel-icons";
import { Button, HStack, VStack } from "@navikt/ds-react";
import { Link, useLoaderData, useNavigate, useRouter, useSearch } from "@tanstack/react-router";
import { capitalize } from "lodash";

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

export function InspectedDataClass() {
  const { history } = useRouter();
  const navigate = useNavigate({ from: "/template/$templateId" });
  const { inspectedModel } = useSearch({ from: "/template/$templateId" });
  const { documentation } = useLoaderData({ from: "/template/$templateId" });

  if (!inspectedModel) {
    return <></>;
  }
  const selectedModel = documentation.templateModelSpecification.types[inspectedModel];
  const modelIsPrimitive = !selectedModel;

  return (
    <div
      css={css`
        width: 800px;
        max-width: 50%;
        background: var(--a-bg-subtle);
        position: fixed;
        right: 0;
        height: 100%;
        border-left: 1px solid var(--a-border-divider);
        padding: var(--a-spacing-4);
        white-space: nowrap;
        overflow: scroll;
      `}
    >
      <HStack align="end" gap="4" justify="space-between">
        <Button icon={<ArrowLeftIcon />} onClick={() => history.go(-1)} size="small" variant="secondary-neutral" />
        <Button
          icon={<XMarkIcon />}
          onClick={() =>
            navigate({ replace: true, to: "/template/$templateId", params: (p) => ({ templateId: p.templateId }) })
          }
          size="small"
          variant="secondary-neutral"
        />
      </HStack>
      {selectedModel && <DataView name={inspectedModel} objectTypeSpecification={selectedModel} />}
      {modelIsPrimitive && <span>{inspectedModel}</span>}
    </div>
  );
}

function DataView({
  name,
  objectTypeSpecification,
}: {
  name: string;
  objectTypeSpecification: ObjectTypeSpecification;
}) {
  return (
    <VStack gap="1">
      <span>
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
        <span
          css={css`
            margin-left: var(--a-spacing-16);
          `}
          key={key}
        >
          {key}: <Type fieldType={value} />
          {value.nullable ? "?" : ""}
        </span>
      ))}
      <span>)</span>
    </VStack>
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
          from="/template/$templateId"
          preload={false}
          replace
          search={(s) => ({ ...s, inspectedModel: fieldType.typeName })}
        >
          {trimClassName(fieldType.typeName)}
        </Link>
      );
    }
  }
}

function trimClassName(className: string) {
  return className.replace(/(.*)[$.]/, "");
}
