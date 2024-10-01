import { Button, HStack, VStack } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useEffect, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { mapEndreMottakerValueTilMottaker } from "~/types/AdresseUtils";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";

import { Route } from "../../../route";
import type { SubmitBrevmalButtonOptions } from "../../route";
import { EndreMottakerModal } from "../endreMottaker/EndreMottaker";
import { useMottakerContext } from "../endreMottaker/MottakerContext";
import BrevmalFormWrapper from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";
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
  saksId: string;
  setSubmitBrevmalButtonOptions: (s: SubmitBrevmalButtonOptions) => void;
}) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const { mottaker, setMottaker } = useMottakerContext();
  const formRef = useRef<HTMLFormElement>(null);

  const form = useForm<z.infer<typeof brevmalBrevbakerFormSchema>>({
    defaultValues: properties.defaultValues,
  });

  useEffect(() => {
    properties.setSubmitBrevmalButtonOptions({
      onClick: () => formRef.current?.requestSubmit(),
      status: null,
    });
  }, [properties.setSubmitBrevmalButtonOptions]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={properties.letterTemplate} />
      <Divider />
      <FormProvider {...form}>
        <BrevmalFormWrapper
          formRef={formRef}
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
                <OppsummeringAvMottaker mottaker={mottaker} saksId={properties.saksId} withTitle />

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
        </BrevmalFormWrapper>
      </FormProvider>
    </>
  );
};

export default BrevmalBrevbaker;
