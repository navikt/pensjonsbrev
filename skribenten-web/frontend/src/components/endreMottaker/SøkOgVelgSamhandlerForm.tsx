import { Alert, Button, HStack, Search, Select, VStack } from "@navikt/ds-react";
import { type UseMutationResult } from "@tanstack/react-query";
import { type Control, Controller, useWatch } from "react-hook-form";

import { ApiError } from "~/components/ApiError";
import { SamhandlerTypeSelect } from "~/components/select/SamhandlerSelect";
import { type FinnSamhandlerRequestDto, type FinnSamhandlerResponseDto } from "~/types/apiTypes";
import { type Nullable } from "~/types/Nullable";

import { type CombinedFormData, Identtype, InnOgUtland, identtypeToText, Søketype } from "./EndreMottakerUtils";
import { SamhandlerSearchResults } from "./SamhandlerSearchResults";

const SøkOgVelgSamhandlerForm = (properties: {
  control: Control<CombinedFormData>;
  onFinnSamhandlerSubmit: UseMutationResult<FinnSamhandlerResponseDto, Error, FinnSamhandlerRequestDto, unknown>;
  selectedSamhandler: Nullable<string>;
  onSelectedChange: (id: string) => void;
}) => {
  const watchedSøketype = useWatch({
    control: properties.control,
    name: "finnSamhandler.søketype",
  });

  return (
    <VStack gap="space-24">
      <VStack align="start" gap="space-24">
        <Controller
          control={properties.control}
          name="finnSamhandler.søketype"
          render={({ field, fieldState }) => (
            <Select
              data-cy="endre-mottaker-søketype-select"
              error={fieldState.error?.message}
              id={field.name}
              label="Søketype"
              {...field}
              size="small"
              value={field.value ?? ""}
            >
              <option disabled value="">
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
        <HStack align="start">
          <Button
            data-cy="endre-mottaker-søk-button"
            loading={properties.onFinnSamhandlerSubmit.isPending}
            size="small"
          >
            Søk
          </Button>
        </HStack>
      )}

      {properties.onFinnSamhandlerSubmit.isSuccess && (
        <div>
          {properties.onFinnSamhandlerSubmit.data.failureType ? (
            <Alert variant="error">{properties.onFinnSamhandlerSubmit.data.failureType}</Alert>
          ) : (
            <SamhandlerSearchResults
              onSelectedChange={properties.onSelectedChange}
              samhandlere={properties.onFinnSamhandlerSubmit.data.samhandlere}
              selectedId={properties.selectedSamhandler}
            />
          )}
        </div>
      )}
      {properties.onFinnSamhandlerSubmit.isError && (
        <ApiError error={properties.onFinnSamhandlerSubmit.error} title="En feil skjedde" />
      )}
    </VStack>
  );
};

export default SøkOgVelgSamhandlerForm;

const SamhandlerDirekteOppslag = (properties: { control: Control<CombinedFormData> }) => {
  return (
    <VStack gap="space-24">
      <Controller
        control={properties.control}
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect
            data-cy="endre-mottaker-samhandlertype-select"
            error={fieldState.error}
            onChange={field.onChange}
            value={field.value ?? ""}
          />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.direkteOppslag.identtype"
        render={({ field, fieldState }) => (
          <Select
            data-cy="endre-mottaker-identtype-select"
            error={fieldState.error?.message}
            id={field.name}
            label="Identtype"
            {...field}
            size="small"
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
          <Search
            variant="simple"
            {...field}
            error={fieldState.error?.message}
            label="ID"
            size="small"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};

const SamhandlerOrganisasjonsnavn = (properties: { control: Control<CombinedFormData> }) => {
  return (
    <VStack gap="space-24">
      <Controller
        control={properties.control}
        name="finnSamhandler.organisasjonsnavn.innOgUtland"
        render={({ field, fieldState }) => (
          <Select
            data-cy="endre-mottaker-organisasjonsnavn-innOgUtland"
            label="Inn-/utland"
            {...field}
            error={fieldState.error?.message}
            size="small"
            value={field.value ?? ""}
          >
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
          <Search
            label="Navn"
            variant="simple"
            {...field}
            error={fieldState.error?.message}
            size="small"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};

const SamhandlerPersonnavn = (properties: { control: Control<CombinedFormData> }) => {
  return (
    <VStack gap="space-24">
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
          <Search
            label="Fornavn"
            variant="simple"
            {...field}
            error={fieldState.error?.message}
            size="small"
            value={field.value ?? ""}
          />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.personnavn.etternavn"
        render={({ field, fieldState }) => (
          <Search
            label="Etternavn"
            variant="simple"
            {...field}
            error={fieldState.error?.message}
            size="small"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};
