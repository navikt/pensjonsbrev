import { css } from "@emotion/react";
import { ExternalLinkIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Checkbox, HStack, Link, TextField, UNSAFE_Combobox, VStack } from "@navikt/ds-react";
import { useMemo } from "react";
import { type Control, Controller, useFormContext, useWatch } from "react-hook-form";

import { ApiError } from "~/components/ApiError";
import { useLandData } from "~/hooks/useLandData";
import { ManueltAdressertTil } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";

import { type CombinedFormData } from "./EndreMottakerUtils";

const NORGE = "NO";

const UtfyllingAvManuellAdresseForm = (properties: {
  control: Control<CombinedFormData>;
  onCloseIntent: () => void;
  error: Nullable<Error>;
  isPending: Nullable<boolean>;
}) => {
  const { data: landData, isLoading, isError, isSuccess } = useLandData();
  const { resetField, clearErrors } = useFormContext<CombinedFormData>();

  const land = useWatch({
    control: properties.control,
    name: "manuellAdresse.adresse.land",
  });

  /*
  Land er gating-feltet: før saksbehandler har valgt land skal ingen av de andre feltene kunne fylles ut.
  Vi bruker readOnly framfor disabled fordi disabled-felter ikke kan få fokus og hoppes over av skjermlesere,
  slik at forklaringen på hvorfor skjemaet er låst aldri når fram.
  */
  const harValgtLand = land !== null;
  const isNorge = land === NORGE;
  //postnummer og poststed er kun relevant for norske adresser, men vises (låst) før land er valgt
  const visPostnummerOgPoststed = !harValgtLand || isNorge;

  /*
  Listen er alfabetisk, men Norge løftes til toppen. Aksel sin combobox har ingen gruppering -
  options er en flat liste som rendres i den rekkefølgen den kommer inn, og filtreringen ved søk
  beholder rekkefølgen. Derfor holder det å legge på sortering her.
  */
  const landOptions = useMemo(() => {
    const alfabetisk = (landData ?? [])
      .toSorted((a, b) => a.navn.localeCompare(b.navn, "no"))
      .map((land) => ({ label: land.navn, value: land.kode }));

    return alfabetisk.toSorted((a, b) => Number(b.value === NORGE) - Number(a.value === NORGE));
  }, [landData]);

  const nullstillAdressefelter = () => {
    resetField("manuellAdresse.adresse.navn", { defaultValue: "" });
    resetField("manuellAdresse.adresse.linje1", { defaultValue: "" });
    resetField("manuellAdresse.adresse.linje2", { defaultValue: "" });
    resetField("manuellAdresse.adresse.linje3", { defaultValue: "" });
    resetField("manuellAdresse.adresse.postnr", { defaultValue: null });
    resetField("manuellAdresse.adresse.poststed", { defaultValue: null });
  };

  return (
    <VStack gap="space-24">
      <VStack gap="space-16">
        <Alert size="small" variant="warning">
          <Link
            href="https://navno.sharepoint.com/sites/fag-og-ytelser-pensjon-alderspensjon/SitePages/Maler/Mal-for-rutiner.aspx "
            target="_blank"
          >
            Les rutinen for manuell adresseendring her {<ExternalLinkIcon />}
          </Link>
        </Alert>

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.manueltAdressertTil"
          render={({ field }) => (
            <Checkbox
              {...field}
              checked={field.value === ManueltAdressertTil.ANNEN}
              description="Kryss av hvis bruker skal motta brevet på en annen adresse"
              onChange={(event) =>
                field.onChange(event.target.checked ? ManueltAdressertTil.ANNEN : ManueltAdressertTil.BRUKER)
              }
              size="small"
            >
              Overstyring av brukers adresse
            </Checkbox>
          )}
        />

        <div>
          {isLoading && <BodyShort size="small">Laster inn land...</BodyShort>}
          {isError && <BodyShort size="small">Kunne ikke laste inn land</BodyShort>}
          {isSuccess && (
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.land"
              render={({ field, fieldState }) => (
                <UNSAFE_Combobox
                  css={css`
                    align-self: flex-start;
                    width: 60%;
                  `}
                  data-testid="land-combobox"
                  description="Du må velge land før resten kan fylles ut."
                  error={fieldState.error?.message}
                  label="Land"
                  onToggleSelected={(option, isSelected) => {
                    if (!isSelected) {
                      //fjernes landet er vi tilbake til utgangspunktet, og alt som er fylt ut nedenfor låses igjen
                      field.onChange(null);
                      nullstillAdressefelter();
                      clearErrors("manuellAdresse.adresse");
                      return;
                    }

                    field.onChange(option);
                    if (option !== NORGE) {
                      resetField("manuellAdresse.adresse.postnr", { defaultValue: null });
                      resetField("manuellAdresse.adresse.poststed", { defaultValue: null });
                    }
                    //feilmeldinger på felter som nå er skjult eller låst skal ikke kunne blokkere lagring
                    clearErrors("manuellAdresse.adresse");
                  }}
                  options={landOptions}
                  placeholder="Velg"
                  selectedOptions={landOptions.filter((option) => option.value === field.value)}
                  shouldAutocomplete
                  size="small"
                />
              )}
            />
          )}
        </div>

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.navn"
          render={({ field, fieldState }) => (
            <TextField
              label="Navn"
              {...field}
              error={fieldState.error?.message}
              readOnly={!harValgtLand}
              size="small"
            />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.linje1"
          render={({ field, fieldState }) => (
            <TextField
              label="Adresselinje 1"
              {...field}
              error={fieldState.error?.message}
              readOnly={!harValgtLand}
              size="small"
            />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.linje2"
          render={({ field, fieldState }) => (
            <TextField
              label="Adresselinje 2"
              {...field}
              error={fieldState.error?.message}
              readOnly={!harValgtLand}
              size="small"
            />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.linje3"
          render={({ field, fieldState }) => (
            <TextField
              label="Adresselinje 3"
              {...field}
              error={fieldState.error?.message}
              readOnly={!harValgtLand}
              size="small"
            />
          )}
        />

        {visPostnummerOgPoststed && (
          <HStack align="start" gap="space-16">
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.postnr"
              render={({ field, fieldState }) => (
                <TextField
                  htmlSize={15}
                  label="Postnummer"
                  {...field}
                  error={fieldState.error?.message}
                  readOnly={!harValgtLand}
                  size="small"
                  value={field.value ?? ""}
                />
              )}
            />
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.poststed"
              render={({ field, fieldState }) => (
                <TextField
                  htmlSize={15}
                  label="Poststed"
                  {...field}
                  error={fieldState.error?.message}
                  readOnly={!harValgtLand}
                  size="small"
                  value={field.value ?? ""}
                />
              )}
            />
          </HStack>
        )}
      </VStack>
      <HStack gap="space-16" justify="space-between">
        <Button onClick={properties.onCloseIntent} size="small" type="button" variant="tertiary">
          Avbryt
        </Button>
        <Button loading={properties.isPending ?? false} size="small">
          Lagre og lukk
        </Button>
      </HStack>
      {properties.error && <ApiError error={properties.error} title="En feil skjedde" />}
    </VStack>
  );
};

export default UtfyllingAvManuellAdresseForm;
