import { css } from "@emotion/react";
import { Button, Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";

import { useModelSpecification } from "~/api/brev-queries";
import { getEnheter } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { PersonAdresse } from "~/routes/saksnummer_/$saksId/brevvelger/$templateId/-components/Adresse";
import { SpraakKode } from "~/types/apiTypes";
import type { SaksbehandlerValg } from "~/types/brev";
import type { LetterModelSpecification } from "~/types/brevbakerTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import { ObjectEditor } from "./components/ObjectEditor";

export type ModelEditorProperties = {
  sakId: string;
  brevkode: string;
  defaultValues?: { spraak: SpraakKode; avsenderEnhet: string; saksbehandlerValg: SaksbehandlerValg };
  disableSubmit: boolean;
  onSubmit: (submittedValues: {
    spraak: SpraakKode;
    avsenderEnhet: string;
    saksbehandlerValg: SaksbehandlerValg;
  }) => void;
};

export const ModelEditor = ({ sakId, brevkode, defaultValues, disableSubmit, onSubmit }: ModelEditorProperties) => {
  const enheterQuery = useQuery(getEnheter);
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
                spraak: values.spraak!,
                avsenderEnhet: values.avsenderEnhet,
                saksbehandlerValg: createSaksbehandlerValg(values.saksbehandlerValg),
              }),
            )}
          >
            <PersonAdresse kanEndreAndresse={false} sakId={sakId} />

            <Select
              {...methods.register("spraak", { required: "Må oppgis" })}
              data-cy="språk-velger-select"
              error={methods.formState.errors.spraak?.message}
              label="Språk"
              size="medium"
            >
              {Object.entries(SpraakKode).map((spraak) => (
                <option key={spraak[0]} value={spraak[1]}>
                  {/* TODO hent inn hva som er brukerens foretrukkede språk */}
                  {SPRAAK_ENUM_TO_TEXT[spraak[1]]}
                </option>
              ))}
            </Select>

            {enheterQuery.isSuccess && (
              <Select
                {...methods.register("avsenderEnhet", { required: "Må oppgis" })}
                error={methods.formState.errors.avsenderEnhet?.message}
                label="Avsenderenhet"
                size="medium"
              >
                <option value={""}>Velg enhet</option>
                {enheterQuery.data.map((option) => (
                  <option key={option.id} value={option.id}>
                    {option.navn}
                  </option>
                ))}
              </Select>
            )}
            {enheterQuery.isPending && <>Henter enheter....</>}
            {enheterQuery.isError && <ApiError error={enheterQuery.error} title="Klarte ikke å hente enheter" />}

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
