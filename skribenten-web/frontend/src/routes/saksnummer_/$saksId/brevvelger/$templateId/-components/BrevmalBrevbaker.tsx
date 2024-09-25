import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Button, HStack, VStack } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { mapEndreMottakerValueTilMottaker } from "~/types/AdresseUtils";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";

import { Route } from "../route";
import { EndreMottakerModal } from "./endreMottaker/EndreMottaker";
import LetterTemplateHeading from "./LetterTemplate";
import { useMottakerContext } from "./MottakerContext";
import SelectEnhet from "./SelectEnhet";
import SelectLanguage from "./SelectLanguage";
import type { brevmalBrevbakerFormSchema } from "./TemplateUtils";

const BrevmalBrevbaker = (properties: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
  displayLanguages: SpraakKode[];
  defaultValues: {
    isSensitive: undefined;
    brevtittel: string;
    spraak: SpraakKode;
    enhetsId: string;
  };
}) => {
  const { saksId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const { mottaker, setMottaker } = useMottakerContext();

  const form = useForm<z.infer<typeof brevmalBrevbakerFormSchema>>({
    defaultValues: properties.defaultValues,
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={properties.letterTemplate} />
      <Divider />
      <FormProvider {...form}>
        <form
          onSubmit={form.handleSubmit((values) => {
            navigate({
              to: "/saksnummer/$saksId/brev",
              search: () => ({
                brevkode: properties.letterTemplate.id,
                spraak: values.spraak,
                enhetsId: values.enhetsId || null,
              }),
              replace: true,
            });
          })}
        >
          <VStack gap="8">
            <VStack gap="2">
              <VStack>
                <OppsummeringAvMottaker mottaker={mottaker} saksId={saksId} withTitle />

                {modalÅpen && (
                  <EndreMottakerModal
                    error={null}
                    isPending={null}
                    onBekreftNyMottaker={(mottaker) => {
                      setMottaker(mapEndreMottakerValueTilMottaker(mottaker));
                      setModalÅpen(false);
                    }}
                    onClose={() => setModalÅpen(false)}
                    resetOnBekreftState={() => setMottaker(null)}
                    åpen={modalÅpen}
                  />
                )}
              </VStack>
              <HStack>
                <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
                  Endre mottaker
                </Button>
                {mottaker !== null && (
                  <Button onClick={() => setMottaker(null)} size="small" type="button" variant="tertiary">
                    Tilbakestill mottaker
                  </Button>
                )}
              </HStack>
            </VStack>
            <SelectEnhet />
            <SelectLanguage
              preferredLanguage={properties.preferredLanguage}
              sorterteSpråk={properties.displayLanguages}
            />
          </VStack>
          <Button
            css={css`
              width: fit-content;
            `}
            data-cy="order-letter"
            icon={<ArrowRightIcon />}
            iconPosition="right"
            size="small"
          >
            Åpne brev
          </Button>
        </form>
      </FormProvider>
    </>
  );
};

export default BrevmalBrevbaker;
