import { css } from "@emotion/react";
import { Button } from "@navikt/ds-react";
import { FormProvider, useForm } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { PersonAdresse } from "~/routes/saksnummer_/$saksId/brevvelger/$templateId/-components/Adresse";
import type { SaksbehandlerValg } from "~/types/brev";
import type { LetterModelSpecification } from "~/types/brevbakerTypes";

import { ObjectEditor } from "./components/ObjectEditor";

export type ModelEditorProperties = {
  sakId: string;
  brevkode: string;
  defaultValues?: SaksbehandlerValg;
  disableSubmit: boolean;
  onSubmit: (saksbehandlerValg: SaksbehandlerValg) => void;
};

export const ModelEditor = ({ sakId, brevkode, defaultValues, disableSubmit, onSubmit }: ModelEditorProperties) => {
  const methods = useForm({ defaultValues });
  const specification = useModelSpecification(brevkode, (s) => s);

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
            onSubmit={methods.handleSubmit((values) =>
              onSubmit({
                saksbehandlerValg: createSaksbehandlerValg(values.saksbehandlerValg),
              }),
            )}
          >
            <PersonAdresse kanEndreAndresse={false} sakId={sakId} />

            <ObjectEditor brevkode={brevkode} typeName={saksbehandlerValgType} />
            <Button loading={disableSubmit} type="submit">
              Send
            </Button>
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
