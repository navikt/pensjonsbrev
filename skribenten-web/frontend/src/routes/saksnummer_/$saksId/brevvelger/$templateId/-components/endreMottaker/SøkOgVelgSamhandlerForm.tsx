import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button, Select, TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { Control } from "react-hook-form";
import { Controller, useForm } from "react-hook-form";

import { finnSamhandler2 } from "~/api/skribenten-api-endpoints";
import { SamhandlerTypeSelect } from "~/components/select/SamhandlerSelect";

import { SamhandlerSearchResults } from "../Adresse";
import type { FinnSamhandlerFormData } from "./EndreMottakerUtils";
import { finnSamhandlerFormDataSchema, Identtype, identtypeToText, InnOgUtland, Søketype } from "./EndreMottakerUtils";

/**
 * @param onSamhandlerValg - Callback for når en samhandler er valgt
 * @param onCloseIntent - signaliserer intent om å lukke modalen
 */
const SøkOgVelgSamhandlerForm = (properties: { onSamhandlerValg: (id: string) => void; onCloseIntent: () => void }) => {
  const finnSamhandlerMutation = useMutation({ mutationFn: finnSamhandler2 });

  const onSubmit = (values: FinnSamhandlerFormData) => {
    finnSamhandlerMutation.mutate({
      samhandlerType: values.samhandlerType!,
      direkteOppslag:
        values.søketype === Søketype.DIREKTE_OPPSLAG
          ? {
              identtype: values.direkteOppslag.identtype!,
              id: values.direkteOppslag.id!,
            }
          : null,
      organisasjonsnavn:
        values.søketype === Søketype.ORGANISASJONSNAVN
          ? {
              innOgUtland: values.organisasjonsnavn.innOgUtland!,
              navn: values.organisasjonsnavn.navn!,
            }
          : null,
      personnavn:
        values.søketype === Søketype.PERSONNAVN
          ? {
              fornavn: values.personnavn.fornavn!,
              etternavn: values.personnavn.etternavn!,
            }
          : null,
    });
  };

  const form = useForm<FinnSamhandlerFormData>({
    defaultValues: {
      søketype: null,
      samhandlerType: null,
      direkteOppslag: { identtype: null, id: "" },
      organisasjonsnavn: { innOgUtland: null, navn: "" },
      personnavn: { fornavn: "", etternavn: "" },
    },
    resolver: zodResolver(finnSamhandlerFormDataSchema),
  });

  const watchedSøketype = form.watch("søketype");

  return (
    <form
      onSubmit={(event) => {
        event.stopPropagation();
        form.handleSubmit(onSubmit)(event);
      }}
    >
      <VStack gap="6">
        <VStack gap="4">
          <Controller
            control={form.control}
            name="søketype"
            render={({ field, fieldState }) => (
              <Select
                error={fieldState.error?.message}
                id={field.name}
                label="Søketype"
                {...field}
                value={field.value ?? ""}
              >
                <option disabled value={""}>
                  Klikk for å velge søketype
                </option>
                <option value={Søketype.DIREKTE_OPPSLAG}>Direkte oppslag</option>
                <option value={Søketype.ORGANISASJONSNAVN}>Organisasjonsnavn</option>
                <option value={Søketype.PERSONNAVN}>Personnavn</option>
              </Select>
            )}
          />

          {watchedSøketype === Søketype.DIREKTE_OPPSLAG && <SamhandlerDirekteOppslag control={form.control} />}
          {watchedSøketype === Søketype.ORGANISASJONSNAVN && <SamhandlerOrganisasjonsnavn control={form.control} />}
          {watchedSøketype === Søketype.PERSONNAVN && <SamhandlerPersonnavn control={form.control} />}
        </VStack>

        {watchedSøketype && (
          <Button
            css={css`
              align-self: flex-start;
            `}
            loading={false}
            size="small"
          >
            Søk
          </Button>
        )}

        {finnSamhandlerMutation.isSuccess && (
          <SamhandlerSearchResults
            onSelect={properties.onSamhandlerValg}
            samhandlere={finnSamhandlerMutation.data.samhandlere}
          />
        )}

        <Button
          css={css`
            align-self: flex-end;
          `}
          onClick={properties.onCloseIntent}
          type="button"
          variant="tertiary"
        >
          Avbryt
        </Button>
      </VStack>
    </form>
  );
};

export default SøkOgVelgSamhandlerForm;

const SamhandlerDirekteOppslag = (properties: { control: Control<FinnSamhandlerFormData> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name={"direkteOppslag.identtype"}
        render={({ field, fieldState }) => (
          <Select
            error={fieldState.error?.message}
            id={field.name}
            label="Identtype"
            {...field}
            value={field.value ?? ""}
          >
            <option value="">Klikk for å velge identtype</option>
            {Object.values(Identtype).map((identtype) => (
              <option key={identtype} value={identtype}>
                {identtypeToText(identtype)}
              </option>
            ))}
          </Select>
        )}
      />

      <Controller
        control={properties.control}
        name={"direkteOppslag.id"}
        render={({ field, fieldState }) => (
          <TextField {...field} error={fieldState.error?.message} label="ID" size="medium" value={field.value ?? ""} />
        )}
      />
    </VStack>
  );
};

const SamhandlerOrganisasjonsnavn = (properties: { control: Control<FinnSamhandlerFormData> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="organisasjonsnavn.innOgUtland"
        render={({ field, fieldState }) => (
          <Select label="Inn-/utland" {...field} error={fieldState.error?.message} value={field.value ?? ""}>
            <option disabled value="">
              Klikk for å velge inn-/utland
            </option>
            <option value={InnOgUtland.INNLAND}>Innland</option>
            <option value={InnOgUtland.UTLAND}>Utland</option>
            <option value={InnOgUtland.ALLE}>Alle</option>
          </Select>
        )}
      />
      <Controller
        control={properties.control}
        name="samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="organisasjonsnavn.navn"
        render={({ field, fieldState }) => (
          <TextField
            label="Navn"
            {...field}
            error={fieldState.error?.message}
            size="medium"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};

const SamhandlerPersonnavn = (properties: { control: Control<FinnSamhandlerFormData> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="personnavn.fornavn"
        render={({ field, fieldState }) => (
          <TextField
            label="Fornavn"
            {...field}
            error={fieldState.error?.message}
            size="medium"
            value={field.value ?? ""}
          />
        )}
      />
      <Controller
        control={properties.control}
        name="personnavn.etternavn"
        render={({ field, fieldState }) => (
          <TextField
            label="Etternavn"
            {...field}
            error={fieldState.error?.message}
            size="medium"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};
