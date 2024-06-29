import { css } from "@emotion/react";
import { Button, Select, TextField, VStack } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import type { Control } from "react-hook-form";
import { Controller, useWatch } from "react-hook-form";

import { SamhandlerTypeSelect } from "~/components/select/SamhandlerSelect";
import type { FinnSamhandlerRequest, FinnSamhandlerResponseDto } from "~/types/apiTypes";

import { SamhandlerSearchResults } from "../Adresse";
import type { CombinedFormData } from "./EndreMottakerUtils";
import { Identtype, identtypeToText, InnOgUtland, Søketype } from "./EndreMottakerUtils";

const SøkOgVelgSamhandlerForm = (properties: {
  control: Control<CombinedFormData>;
  onCloseIntent: () => void;
  onSamhandlerValg: (id: string) => void;
  onFinnSamhandlerSubmit: UseMutationResult<FinnSamhandlerResponseDto, Error, FinnSamhandlerRequest, unknown>;
}) => {
  const watchedSøketype = useWatch({
    control: properties.control,
    name: "finnSamhandler.søketype",
  });

  return (
    <VStack gap="6">
      <VStack gap="4">
        <Controller
          control={properties.control}
          name="finnSamhandler.søketype"
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

        {watchedSøketype === Søketype.DIREKTE_OPPSLAG && <SamhandlerDirekteOppslag control={properties.control} />}
        {watchedSøketype === Søketype.ORGANISASJONSNAVN && <SamhandlerOrganisasjonsnavn control={properties.control} />}
        {watchedSøketype === Søketype.PERSONNAVN && <SamhandlerPersonnavn control={properties.control} />}
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

      {properties.onFinnSamhandlerSubmit.isSuccess && (
        <SamhandlerSearchResults
          onSelect={(id) => properties.onSamhandlerValg(id)}
          samhandlere={properties.onFinnSamhandlerSubmit.data.samhandlere}
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
  );
};

export default SøkOgVelgSamhandlerForm;

const SamhandlerDirekteOppslag = (properties: { control: Control<CombinedFormData> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.direkteOppslag.identtype"
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
        name="finnSamhandler.direkteOppslag.id"
        render={({ field, fieldState }) => (
          <TextField {...field} error={fieldState.error?.message} label="ID" size="medium" value={field.value ?? ""} />
        )}
      />
    </VStack>
  );
};

const SamhandlerOrganisasjonsnavn = (properties: { control: Control<CombinedFormData> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="finnSamhandler.organisasjonsnavn.innOgUtland"
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
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.organisasjonsnavn.navn"
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

const SamhandlerPersonnavn = (properties: { control: Control<CombinedFormData> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.personnavn.fornavn"
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
        name="finnSamhandler.personnavn.etternavn"
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
