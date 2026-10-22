import { zodResolver } from "@hookform/resolvers/zod";
import { BodyLong, Button, Modal } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useEffect, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { type z } from "zod";

import { bestillDelautomatiskBrev, orderLetterKeys } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import { useSendtBrev } from "~/routes/saksnummer_/$saksId/kvittering/-components/SendtBrevContext";
import { type BestillDelautomatiskBrevRequest, type LetterMetadata, type SpraakKode } from "~/types/apiTypes";
import { type Nullable } from "~/types/Nullable";
import { trackEvent } from "~/utils/umami";

import { Route, type SubmitTemplateOptions } from "../../route";
import EndreMottaker from "../endreMottaker/EndreMottaker";
import BrevmalFormWrapper from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";
import { byggDelautomatiskOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

export default function BrevmalDelautomatisk({
  letterTemplate,
  preferredLanguage,
  displayLanguages,
  defaultValues,
  saksId,
  setOnFormSubmitClick,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: Nullable<SpraakKode>;
  displayLanguages: SpraakKode[];
  defaultValues: {
    brevtittel: string;
    spraak: SpraakKode;
    enhetsId: string;
  };
  saksId: string;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) {
  const { enhetsId: urlEnhetsId, vedtaksId, idTSSEkstern } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const { leggTilDelautomatiskBrevResultat } = useSendtBrev();
  const formRef = useRef<HTMLFormElement>(null);
  const [bestillingTilBekreftelse, setBestillingTilBekreftelse] =
    useState<Nullable<BestillDelautomatiskBrevRequest>>(null);

  const gåTilKvittering = () =>
    navigate({
      to: "/saksnummer/$saksId/kvittering",
      params: { saksId },
      search: { enhetsId: urlEnhetsId, vedtaksId },
    });

  const bestillMutation = useMutation<Nullable<number>, AxiosError<Error> | Error, BestillDelautomatiskBrevRequest>({
    mutationFn: (payload) => bestillDelautomatiskBrev(saksId, payload),
    onSuccess: (journalpostId, variables) => {
      trackEvent("brev sendt", {
        brevkode: letterTemplate.id,
        spraak: variables.spraak,
        brevtittel: letterTemplate.name,
        brevsystem: "delautomatisk",
        dokumentkategori: letterTemplate.dokumentkategoriCode || undefined,
        enhetsId: variables.enhetsId,
      });
      leggTilDelautomatiskBrevResultat({
        status: "success",
        brevtittel: letterTemplate.name,
        idTSSEkstern: variables.idTSSEkstern ?? null,
        journalpostId,
        error: null,
      });
      gåTilKvittering();
    },
    onError: (error, variables) => {
      leggTilDelautomatiskBrevResultat({
        status: "error",
        brevtittel: letterTemplate.name,
        idTSSEkstern: variables.idTSSEkstern ?? null,
        journalpostId: null,
        error,
      });
      gåTilKvittering();
    },
    mutationKey: orderLetterKeys.brevsystem("delautomatisk"),
  });

  const validationSchema = createValidationSchema(letterTemplate);

  useEffect(() => {
    setOnFormSubmitClick({ onClick: () => formRef.current?.requestSubmit(), label: "Bestill og send brev" });
  }, [setOnFormSubmitClick]);

  type FormIn = z.input<typeof validationSchema>;
  type FormOut = z.output<typeof validationSchema>;

  const methods = useForm<FormIn, unknown, FormOut>({
    defaultValues,
    resolver: zodResolver(validationSchema),
  });

  const { getValues, setValue } = methods;

  useEffect(() => {
    const enhetsId = urlEnhetsId ?? "";

    if (getValues("enhetsId") !== enhetsId) {
      setValue("enhetsId", enhetsId);
    }
  }, [urlEnhetsId, getValues, setValue]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <BrevmalFormWrapper
          formRef={formRef}
          onSubmit={methods.handleSubmit((submittedValues) => {
            const parsedVedtaksId = Number.parseInt(vedtaksId ?? "", 10);

            setBestillingTilBekreftelse(
              byggDelautomatiskOnSubmitRequest({
                template: letterTemplate,
                idTSSEkstern: idTSSEkstern ?? null,
                vedtaksId: Number.isNaN(parsedVedtaksId) ? null : parsedVedtaksId,
                formValues: {
                  enhetsId: submittedValues.enhetsId,
                  spraak: submittedValues.spraak ?? null,
                },
              }),
            );
          })}
        >
          <EndreMottaker saksId={saksId} />
          <SelectEnhet />
          <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={displayLanguages} />
        </BrevmalFormWrapper>
      </FormProvider>

      {bestillingTilBekreftelse && (
        <BekreftBestillingModal
          onBekreft={() => {
            bestillMutation.mutate(bestillingTilBekreftelse);
            setBestillingTilBekreftelse(null);
          }}
          onClose={() => setBestillingTilBekreftelse(null)}
        />
      )}
    </>
  );
}

const BekreftBestillingModal = (props: { onBekreft: () => void; onClose: () => void }) => {
  return (
    <Modal
      data-testid="bekreft-delautomatisk-brev-modal"
      header={{ heading: "Vil du bestille og sende brevet?", closeButton: false }}
      onClose={props.onClose}
      open
      portal
      width="small"
    >
      <Modal.Body>
        <BodyLong>
          Brevet blir sendt til angitt mottaker, og lagt til i dokumentoversikten. Du kan ikke angre denne handlingen.
        </BodyLong>
      </Modal.Body>
      <Modal.Footer>
        <Button data-testid="send-delautomatisk-brev" onClick={props.onBekreft} type="button">
          Ja, bestill og send brevet
        </Button>
        <Button onClick={props.onClose} type="button" variant="secondary">
          Nei
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
