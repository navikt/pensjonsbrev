import { zodResolver } from "@hookform/resolvers/zod";
import { BodyShort, Button, HStack, Modal, TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderExstreamLetter, orderLetterKeys } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { BestillOgRedigerBrevResponse, SpraakKode } from "~/types/apiTypes";
import { type LetterMetadata, type OrderExstreamLetterRequest } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import type { SubmitTemplateOptions } from "../../route";
import { Route } from "../../route";
import EndreMottaker from "../endreMottaker/EndreMottaker";
import HentOgVisAdresse from "../endreMottaker/HentOgVisAdresse";
import BrevmalFormWrapper, { OrderLetterResult } from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";
import SelectSensitivity from "./components/SelectSensitivity";
import { byggExstreamOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

const BestillAutobrevModal = (props: { åpen: boolean; onClose: () => void; onFormSubmit: () => void }) => {
  return (
    <Modal
      header={{ heading: "Vil du bestille og sende brevet?" }}
      onClose={props.onClose}
      open={props.åpen}
      portal
      width={450}
    >
      <Modal.Body>
        <BodyShort>
          Brevet blir sendt til angitt mottaker, og lagt til i dokumentoversikten. Du kan ikke angre denne handlingen.{" "}
        </BodyShort>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="4">
          <Button data-cy="autobrev-modal-nei" onClick={props.onClose} type="button" variant="secondary">
            Nei
          </Button>
          <Button onClick={props.onFormSubmit} type="button" variant="primary">
            Ja, bestill og send brevet
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};

export default function BrevmalForExstream({
  letterTemplate,
  preferredLanguage,
  displayLanguages,
  defaultValues,
  templateId,
  saksId,
  setOnFormSubmitClick,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: Nullable<SpraakKode>;
  displayLanguages: SpraakKode[];
  defaultValues: {
    isSensitive: undefined;
    brevtittel: string;
    spraak: SpraakKode;
    enhetsId: string;
  };
  templateId: string;
  saksId: string;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) {
  const { vedtaksId, idTSSEkstern } = Route.useSearch();
  const formRef = useRef<HTMLFormElement>(null);
  const [vilSendeAutobrev, setVilSendeAutobrev] = useState(false);
  const orderLetterMutation = useMutation<
    BestillOgRedigerBrevResponse,
    AxiosError<Error> | Error,
    OrderExstreamLetterRequest
  >({
    mutationFn: (payload) => orderExstreamLetter(saksId, payload),
    onSuccess: (res) => {
      if (res.url) {
        window.open(res.url);
      }
    },
    mutationKey: orderLetterKeys.brevsystem("exstream"),
  });

  const validationSchema = createValidationSchema(letterTemplate);
  const methods = useForm<z.infer<typeof validationSchema>>({
    defaultValues: defaultValues,
    resolver: zodResolver(validationSchema),
  });

  useEffect(() => {
    if (letterTemplate.redigerbart) {
      setOnFormSubmitClick({ onClick: () => formRef.current?.requestSubmit() });
    } else {
      setOnFormSubmitClick({
        onClick: () => {
          methods.trigger().then((isValid) => {
            if (isValid) {
              setVilSendeAutobrev(true);
            }
          });
        },
      });
    }
  }, [setOnFormSubmitClick, letterTemplate.redigerbart, methods]);

  const { reset: resetMutation } = orderLetterMutation;
  const { reset: resetForm } = methods;
  useEffect(() => {
    //ved template endring vil vi resette formet - men beholde preferredLanguage hvis den finnes
    resetForm(defaultValues);
    resetMutation();
  }, [templateId, defaultValues, resetForm, resetMutation]);

  return (
    <>
      {vilSendeAutobrev && (
        <BestillAutobrevModal
          onClose={() => setVilSendeAutobrev(false)}
          onFormSubmit={() => {
            formRef.current?.requestSubmit();
            setVilSendeAutobrev(false);
          }}
          åpen={vilSendeAutobrev}
        />
      )}
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <BrevmalFormWrapper
          formRef={formRef}
          onSubmit={methods.handleSubmit((submittedValues) =>
            orderLetterMutation.mutate(
              byggExstreamOnSubmitRequest({
                template: letterTemplate,
                idTSSEkstern: idTSSEkstern ?? null,
                vedtaksId: vedtaksId ?? null,
                formValues: {
                  enhetsId: submittedValues.enhetsId,
                  isSensitive: submittedValues.isSensitive,
                  spraak: submittedValues.spraak ?? null,
                  brevtittel: submittedValues.brevtittel ?? null,
                },
              }),
            ),
          )}
        >
          {/*Special case to hide mottaker for "Notat" & "Posteringsgrunnlag" */}
          {templateId !== "PE_IY_03_156" && templateId !== "PE_OK_06_101" && (
            <VStack gap="2">
              <HentOgVisAdresse sakId={saksId} samhandlerId={idTSSEkstern} showMottakerTitle />
              <EndreMottaker />
            </VStack>
          )}

          <SelectEnhet />
          {letterTemplate.redigerbarBrevtittel && (
            <TextField
              {...methods.register("brevtittel")}
              autoComplete="on"
              data-cy="brev-title-textfield"
              description="Gi brevet en kort og forklarende tittel."
              error={methods.formState.errors.brevtittel?.message}
              label="Endre tittel"
              size="small"
            />
          )}
          {/* Utfylling av språk vil vi ikke gjøre dersom templaten er 'Notat' */}
          {letterTemplate.id !== "PE_IY_03_156" && (
            <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={displayLanguages} />
          )}
          <SelectSensitivity />
        </BrevmalFormWrapper>

        <OrderLetterResult data={orderLetterMutation.data} error={orderLetterMutation.error} />
      </FormProvider>
    </>
  );
}
