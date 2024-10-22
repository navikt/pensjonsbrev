import { Button, HStack, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { memo, useEffect, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { createBrev, getBrev } from "~/api/brev-queries";
import { SaksbehandlerValgModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { mapEndreMottakerValueTilMottaker } from "~/types/AdresseUtils";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import type { BrevResponse, Mottaker, SaksbehandlerValg } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../../../route";
import type { SubmitTemplateOptions } from "../../route";
import { EndreMottakerModal } from "../endreMottaker/EndreMottaker";
import BrevmalFormWrapper from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";

interface BrevbakerFormData {
  enhetsId: string;
  spraak: SpraakKode;
  saksbehandlerValg: SaksbehandlerValg;
  mottaker: Nullable<Mottaker>;
}

const BrevmalBrevbaker = (props: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
  displayLanguages: SpraakKode[];
  defaultValues: {
    spraak: SpraakKode;
    enhetsId: string;
  };
  saksId: string;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const formRef = useRef<HTMLFormElement>(null);

  const opprettBrevMutation = useMutation<BrevResponse, Error, BrevbakerFormData>({
    mutationFn: async (values) =>
      createBrev(props.saksId, {
        brevkode: props.letterTemplate.id,
        spraak: values.spraak,
        avsenderEnhetsId: values.enhetsId,
        saksbehandlerValg: values.saksbehandlerValg,
        mottaker: values.mottaker,
      }),

    onSuccess: async (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      return navigate({
        to: "/saksnummer/$saksId/brev/$brevId",
        params: { brevId: response.info.id },
      });
    },
  });

  const form = useForm<BrevbakerFormData>({
    defaultValues: {
      enhetsId: props.defaultValues.enhetsId,
      spraak: props.defaultValues.spraak,
      mottaker: null,
    },
  });

  useEffect(() => {
    props.setOnFormSubmitClick({ onClick: () => formRef.current?.requestSubmit() });
  }, [props.setOnFormSubmitClick, props]);

  return (
    <VStack gap="4">
      <LetterTemplateHeading letterTemplate={props.letterTemplate} />
      <Divider />
      <FormProvider {...form}>
        <BrevmalFormWrapper formRef={formRef} onSubmit={form.handleSubmit((v) => opprettBrevMutation.mutate(v))}>
          <VStack gap="8">
            <VStack gap="2">
              <VStack>
                <OppsummeringAvMottaker mottaker={form.watch("mottaker")} saksId={props.saksId} withTitle />

                {modalÅpen && (
                  <EndreMottakerModal
                    error={null}
                    isPending={null}
                    onBekreftNyMottaker={(mottaker) => {
                      form.setValue("mottaker", mapEndreMottakerValueTilMottaker(mottaker));
                      setModalÅpen(false);
                    }}
                    onClose={() => setModalÅpen(false)}
                    resetOnBekreftState={() => form.setValue("mottaker", null)}
                    åpen={modalÅpen}
                  />
                )}
              </VStack>
              <HStack>
                <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
                  Endre mottaker
                </Button>
                {form.watch("mottaker") !== null && (
                  <Button onClick={() => form.setValue("mottaker", null)} size="small" type="button" variant="tertiary">
                    Tilbakestill mottaker
                  </Button>
                )}
              </HStack>
            </VStack>
            <SelectEnhet />
            <SelectLanguage preferredLanguage={props.preferredLanguage} sorterteSpråk={props.displayLanguages} />
            <SaksbehandlerValgModelEditor brevkode={props.letterTemplate.id} fieldsToRender="required" />
          </VStack>
        </BrevmalFormWrapper>
      </FormProvider>
    </VStack>
  );
};

export default memo(BrevmalBrevbaker);
