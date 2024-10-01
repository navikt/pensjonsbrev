import { css } from "@emotion/react";
import { ExternalLinkIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Heading, HStack, Link, TextField, UNSAFE_Combobox, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import type { Control } from "react-hook-form";
import { Controller } from "react-hook-form";

import { hentLandForManuellUtfyllingAvAdresse } from "~/api/skribenten-api-endpoints";

import type { CombinedFormData } from "./EndreMottakerUtils";

const UtfyllingAvManuellAdresseForm = (properties: {
  control: Control<CombinedFormData>;
  onSubmit: () => void;
  onCloseIntent: () => void;
}) => {
  const hentLand = useQuery({
    queryKey: hentLandForManuellUtfyllingAvAdresse.queryKey,
    queryFn: () => hentLandForManuellUtfyllingAvAdresse.queryFn(),
  });

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
        <Alert size="small" variant="info">
          <BodyShort>
            Brev sendes til brukers folkeregistrerte adresse eller annen foretrukken kanal. Legg til mottaker dersom
            brev skal sendes til utenlandsk adresse, fullmektig, verge eller dødsbo.
          </BodyShort>
        </Alert>

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

        <div>
          {hentLand.isLoading && <BodyShort size="small">Laster inn land...</BodyShort>}
          {/* TODO - hvis en eller annen feil skjer, vil vi gi dem et input felt der dem kan skrive in koden selv? */}
          {hentLand.isError && <BodyShort size="small">Kunne ikke laste inn land</BodyShort>}
          {hentLand.isSuccess && (
            <Controller
              control={properties.control}
              name="manuellAdresse.adresse.land"
              render={({ field, fieldState }) => {
                const options = hentLand.data
                  .toSorted((a, b) => (a.navn > b.navn ? 1 : -1))
                  .map((land) => ({ label: land.navn, value: land.kode }));
                return (
                  <UNSAFE_Combobox
                    css={css`
                      align-self: flex-start;
                      width: 60%;
                    `}
                    size="small"
                    {...field}
                    error={fieldState.error?.message}
                    label="Land *"
                    onToggleSelected={(option) => field.onChange(option)}
                    options={options}
                    ref={field.ref}
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
