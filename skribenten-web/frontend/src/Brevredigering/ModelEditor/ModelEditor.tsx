import { css } from "@emotion/react";
import { Button, Heading } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import type { Draft } from "immer";
import { produce } from "immer";
import { useCallback, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { getSakContext } from "~/api/skribenten-api-endpoints";
import type { SaksbehandlerValg } from "~/types/brev";
import type { LetterModelSpecification } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import { ObjectEditor } from "./components/ObjectEditor";
import { AutoSavingTextField } from "./components/ScalarEditor";

export type ModelEditorProperties = {
  brevkode: string;
  defaultValues?: SaksbehandlerValg & { signatur: string };
  disableSubmit: boolean;
  onSubmit: (saksbehandlerValg: SaksbehandlerValg, signatur: string) => void;
  saksId: string;
  vedtaksId: string | undefined;
  brevId: Nullable<number>;
  showOnlyRequiredFields?: boolean;
};

export const ModelEditor = ({
  brevkode,
  defaultValues,
  disableSubmit,
  onSubmit,
  saksId,
  vedtaksId,
  brevId,
  showOnlyRequiredFields,
}: ModelEditorProperties) => {
  const methods = useForm({ defaultValues });
  const specification = useModelSpecification(brevkode, (s) => s);
  const formRef = useRef<HTMLFormElement>(null);
  const brevmal = useQuery({
    queryKey: getSakContext.queryKey(saksId, vedtaksId),
    queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
    select: (data) => data.brevMetadata.find((brevmal) => brevmal.id === brevkode),
  });

  const requestSubmit = useCallback(() => {
    formRef.current?.requestSubmit();
  }, [formRef]);

  if (specification) {
    const saksbehandlerValgType = findSaksbehandlerValgTypeName(specification);

    const doSubmit = (values: SaksbehandlerValg & { signatur: string }) => {
      const { signatur, ...saksbehandlerValg } = values;
      return onSubmit(createSaksbehandlerValg(saksbehandlerValg, specification, saksbehandlerValgType), signatur);
    };

    return (
      <>
        <FormProvider {...methods}>
          <form
            css={css`
              display: flex;
              flex-direction: column;
              gap: var(--a-spacing-6);

              > {
                width: 100%;
              }
            `}
            onSubmit={methods.handleSubmit(doSubmit)}
            ref={formRef}
          >
            <Heading size="small">{brevmal.data?.name}</Heading>
            <ObjectEditor
              brevkode={brevkode}
              showOnlyRequiredFields={showOnlyRequiredFields}
              submitOnChange={brevId ? requestSubmit : undefined}
              typeName={saksbehandlerValgType}
            />
            {/*
              //TODO - ModelEditor skal i utgangspunktet kun være for SaksbehandlerValg.
              // designet skal ha signatur felt på 'samme sted', altså med editoren.
              //Den skal ikke brukes ved opprettelse av brev, men kun når man redigerer brevet. Derfor
              //gjør vi en enkel fiks for å skjule signatur feltet ved opprettelse av brev.
              */}
            {!showOnlyRequiredFields && (
              <AutoSavingTextField
                field={"signatur"}
                fieldType={{
                  type: "scalar",
                  nullable: false,
                  kind: "STRING",
                }}
                onSubmit={brevId ? requestSubmit : undefined}
                timeoutTimer={2500}
                type={"text"}
              />
            )}
            {!brevId && (
              <Button loading={disableSubmit} type="submit">
                Opprett brev
              </Button>
            )}
          </form>
        </FormProvider>
      </>
    );
  } else {
    return <div>Henter skjema for saksbehandler valg</div>;
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

function createSaksbehandlerValg(
  values: unknown,
  specification: LetterModelSpecification,
  saksbehandlerValgType: string,
): SaksbehandlerValg {
  return produce(values as SaksbehandlerValg, (draft) =>
    saksbehandlerValgObject(draft, specification, saksbehandlerValgType),
  );
}

function saksbehandlerValgObject(
  draft: Draft<SaksbehandlerValg>,
  specification: LetterModelSpecification,
  objectType: string,
) {
  const objectSpecification = specification.types[objectType];

  for (const [field, fieldType] of Object.entries(objectSpecification)) {
    if (fieldType.nullable) {
      if (fieldType.type === "object" && draft[field] !== undefined && draft[field] !== null) {
        saksbehandlerValgObject(draft[field] as SaksbehandlerValg, specification, fieldType.typeName);
      } else if (draft[field] === "") {
        draft[field] = null;
      }
    } else if (fieldType.type === "scalar" && fieldType.kind === "BOOLEAN") {
      draft[field] = draft[field] ?? false;
    }
  }
}
