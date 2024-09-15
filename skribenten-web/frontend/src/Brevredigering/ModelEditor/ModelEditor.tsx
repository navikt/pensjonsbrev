import { css } from "@emotion/react";
import { Button, Heading } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import type { Dispatch, SetStateAction } from "react";
import { useCallback, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { getSakContext } from "~/api/skribenten-api-endpoints";
import type { SaksbehandlerValg } from "~/types/brev";
import type { LetterModelSpecification } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import type { LetterEditorState } from "../LetterEditor/model/state";
import { ObjectEditor } from "./components/ObjectEditor";
import { AutoSavingTextField } from "./components/ScalarEditor";

export type ModelEditorProperties = {
  brevkode: string;
  defaultValues?: SaksbehandlerValg & { signatur: string };
  disableSubmit: boolean;
  onSubmit: (saksbehandlerValg: SaksbehandlerValg) => void;
  saksId: string;
  vedtaksId: string | undefined;
  brevId: Nullable<number>;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  signaturOnSubmit: (signatur: string) => void;
};

export const ModelEditor = ({
  brevkode,
  defaultValues,
  disableSubmit,
  onSubmit,
  saksId,
  vedtaksId,
  brevId,
  signaturOnSubmit,
}: ModelEditorProperties) => {
  const methods = useForm({ defaultValues });
  const specification = useModelSpecification(brevkode, (s) => s);
  const formRef = useRef<HTMLFormElement>(null);
  const brevmal = useQuery({
    queryKey: getSakContext.queryKey(saksId, vedtaksId),
    queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
    select: (data) => data.brevMetadata.find((brevmal) => brevmal.id === brevkode),
  });
  const doSubmit = (values: SaksbehandlerValg) => onSubmit(createSaksbehandlerValg(values));
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
            <AutoSavingTextField
              field={"signatur"}
              fieldType={{
                type: "scalar",
                nullable: false,
                kind: "STRING",
              }}
              /*
                TODO: per nå så gir onSubmit'en oss saksbehandlerValg tilbake hele tiden.
                      Hvis vi har lyst til at den skal være mer generell, burde den kanskje bare returnere sin input, 
                      så får componenten som brukere den håndtere resten
               */
              onSubmit={() => {
                const signatur = methods.watch("signatur");
                if (signatur) {
                  signaturOnSubmit(signatur);
                }
              }}
              siblings={[]}
              timeoutTimer={3000}
              type={"text"}
            />
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
