import { css } from "@emotion/react";
import { ExternalLinkIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Heading, HStack, Link, TextField, UNSAFE_Combobox, VStack } from "@navikt/ds-react";
import type { Control } from "react-hook-form";
import { Controller, useFormContext, useWatch } from "react-hook-form";

import { useLandData } from "~/hooks/useLandData";

import type { CombinedFormData } from "./EndreMottakerUtils";

const UtfyllingAvManuellAdresseForm = (properties: {
  control: Control<CombinedFormData>;
  onSubmit: () => void;
  onCloseIntent: () => void;
}) => {
  const { data: landData, isLoading, isError, isSuccess } = useLandData();
  const { resetField } = useFormContext<CombinedFormData>();

  const land = useWatch({
    control: properties.control,
    name: "manuellAdresse.adresse.land",
  });
  const isNorge = typeof land === "string" && land === "NO";

  return (
    <VStack gap="6">
      <VStack gap="4">
        <Alert size="small" variant="warning">
          <Heading size="xsmall">Manuell adresseendringsrutine</Heading>
          <Link
            href="https://navno.sharepoint.com/sites/fag-og-ytelser-pensjon-alderspensjon/SitePages/Maler/Mal-for-rutiner.aspx "
            target="_blank"
          >
            Les rutinen for endring av adresse her {<ExternalLinkIcon />}
          </Link>
        </Alert>

        {/* <Controller
          control={properties.control}
          name="manuellAdresse.adresse.erBrukersAdresse"
          render={({ field }) => (
            <Checkbox
              {...field}
              description="Kryss av hvis brevet skal til bruker, men til en annen adresse"
              size="small"
            >
              Brukers adresse
            </Checkbox>
          )}
        /> */}

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.navn"
          render={({ field, fieldState }) => (
            <TextField label="Navn" {...field} error={fieldState.error?.message} size="small" />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.linje1"
          render={({ field, fieldState }) => (
            <TextField label="Adresselinje 1" {...field} error={fieldState.error?.message} size="small" />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.linje2"
          render={({ field, fieldState }) => (
            <TextField label="Adresselinje 2" {...field} error={fieldState.error?.message} size="small" />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.linje3"
          render={({ field, fieldState }) => (
            <TextField label="Adresselinje 3" {...field} error={fieldState.error?.message} size="small" />
          )}
        />
        {isNorge && (
          <HStack gap="4">
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.postnr"
              render={({ field, fieldState }) => (
                <TextField
                  css={css`
                    width: 25%;
                  `}
                  label="Postnummer"
                  {...field}
                  error={fieldState.error?.message}
                  size="small"
                />
              )}
            />
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.poststed"
              render={({ field, fieldState }) => (
                <TextField
                  css={css`
                    width: 25%;
                  `}
                  label="Poststed"
                  {...field}
                  error={fieldState.error?.message}
                  size="small"
                />
              )}
            />
          </HStack>
        )}
        <div>
          {isLoading && <BodyShort size="small">Laster inn land...</BodyShort>}
          {/* TODO - hvis en eller annen feil skjer, vil vi gi dem et input felt der dem kan skrive in koden selv? */}
          {isError && <BodyShort size="small">Kunne ikke laste inn land</BodyShort>}
          {isSuccess && (
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.land"
              render={({ field, fieldState }) => {
                const options = landData
                  .toSorted((a, b) => (a.navn > b.navn ? 1 : -1))
                  .map((land) => ({ label: land.navn, value: land.kode }));
                return (
                  <UNSAFE_Combobox
                    css={css`
                      align-self: flex-start;
                      width: 60%;

                      /*
                        siden input feltet er nederts på modalen, vil det å åpne den tvinge en scroll på modalen
                        vi setter den derfor til å åpne oppover
                      */
                      .navds-combobox__list {
                        bottom: 100%;
                        top: auto;
                      }
                    `}
                    data-cy="land-combobox"
                    error={fieldState.error?.message}
                    label="Land *"
                    onToggleSelected={(option) => {
                      field.onChange(option);
                      if (option !== "NO") {
                        resetField("manuellAdresse.adresse.postnr", { defaultValue: "" });
                        resetField("manuellAdresse.adresse.poststed", { defaultValue: "" });
                      }
                    }}
                    options={options}
                    selectedOptions={options.filter((option) => option.value === field.value) ?? undefined}
                    shouldAutocomplete
                    size="small"
                  />
                );
              }}
            />
          )}
        </div>
      </VStack>
      <HStack
        css={css`
          align-self: flex-end;
        `}
        gap="4"
      >
        <Button onClick={properties.onCloseIntent} size="small" type="button" variant="tertiary">
          Avbryt
        </Button>
        <Button size="small">Gå videre</Button>
      </HStack>
    </VStack>
  );
};

export default UtfyllingAvManuellAdresseForm;
