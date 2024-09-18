import { css } from "@emotion/react";
import { Button, Heading } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
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
  //TODO - ModelEditor skal i utgangspunktet kun være for SaksbehandlerValg.
  // designet skal ha signatur felt på 'samme sted', altså med editoren.
  //Den skal ikke brukes ved opprettelse av brev, men kun når man redigerer brevet. Derfor
  //gjør vi en enkel fiks for å skjule signatur feltet ved opprettelse av brev.
  showSignaturField?: boolean;
};

export const ModelEditor = ({
  brevkode,
  defaultValues,
  disableSubmit,
  onSubmit,
  saksId,
  vedtaksId,
  brevId,
  showSignaturField,
}: ModelEditorProperties) => {
  const methods = useForm({ defaultValues });
  const specification = useModelSpecification(brevkode, (s) => s);
  const formRef = useRef<HTMLFormElement>(null);
  const brevmal = useQuery({
    queryKey: getSakContext.queryKey(saksId, vedtaksId),
    queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
    select: (data) => data.brevMetadata.find((brevmal) => brevmal.id === brevkode),
  });

  const doSubmit = (values: SaksbehandlerValg & { signatur: string }) => {
    return onSubmit(createSaksbehandlerValg(values), values.signatur);
  };

  const requestSubmit = useCallback(() => {
    formRef.current?.requestSubmit();
  }, [formRef]);

  if (specification) {
    const saksbehandlerValgType = findSaksbehandlerValgTypeName(specification);
    return (
      <>
        <FormProvider {...methods}>
          <form
            css={css`
              padding: var(--a-spacing-6) var(--a-spacing-4);
              display: flex;
              flex-direction: column;
              gap: var(--a-spacing-4);

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
              submitOnChange={brevId ? requestSubmit : undefined}
              typeName={saksbehandlerValgType}
            />
            {showSignaturField && (
              <AutoSavingTextField
                field={"signatur"}
                fieldType={{
                  type: "scalar",
                  nullable: false,
                  kind: "STRING",
                }}
                onSubmit={brevId ? requestSubmit : undefined}
                timeoutTimer={3000}
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

function createSaksbehandlerValg(values: unknown): SaksbehandlerValg {
  // In React-Hook-Form it is convential, and easiest, to keep empty inputs as an empty string.
  // However, in the api empty strings are interpreted literally, we want these to be null in the payload.
  // To deal with any nested/array properties we use this JSON trick
  return JSON.parse(JSON.stringify(values), (key, value) => (value === "" ? null : value));
}
