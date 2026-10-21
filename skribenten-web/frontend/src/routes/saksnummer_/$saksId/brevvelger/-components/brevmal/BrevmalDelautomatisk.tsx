import { zodResolver } from "@hookform/resolvers/zod";
import { Alert, BodyLong, BodyShort, Button, Heading, Label, Modal, Spacer, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { type z } from "zod";

import { bestillDelautomatiskBrev, getEnheter, orderLetterKeys } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { type BestillDelautomatiskBrevRequest, type LetterMetadata, type SpraakKode } from "~/types/apiTypes";
import { type Nullable } from "~/types/Nullable";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";
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
  const formRef = useRef<HTMLFormElement>(null);
  const [bestillingTilBekreftelse, setBestillingTilBekreftelse] =
    useState<Nullable<BestillDelautomatiskBrevRequest>>(null);

  const bestillMutation = useMutation<Nullable<number>, AxiosError<Error> | Error, BestillDelautomatiskBrevRequest>({
    mutationFn: (payload) => bestillDelautomatiskBrev(saksId, payload),
    onSuccess: (_, variables) => {
      trackEvent("brev sendt", {
        brevkode: letterTemplate.id,
        spraak: variables.spraak,
        brevtittel: letterTemplate.name,
        brevsystem: "delautomatisk",
        dokumentkategori: letterTemplate.dokumentkategoriCode || undefined,
        enhetsId: variables.enhetsId,
      });
      setBestillingTilBekreftelse(null);
    },
    mutationKey: orderLetterKeys.brevsystem("delautomatisk"),
  });

  const validationSchema = createValidationSchema(letterTemplate);

  useEffect(() => {
    setOnFormSubmitClick({ onClick: () => formRef.current?.requestSubmit(), label: "Bestill" });
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

            bestillMutation.reset();
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

      <Spacer />
      {bestillMutation.isSuccess && (
        <Alert data-testid="delautomatisk-brev-sendt" size="small" variant="success">
          <Heading level="3" size="xsmall">
            Brevet er bestilt
          </Heading>
          <BodyShort size="small">
            Brevet sendes automatisk til mottaker.
            {bestillMutation.data !== null && ` Journalpost-ID: ${bestillMutation.data}.`}
          </BodyShort>
        </Alert>
      )}

      {bestillingTilBekreftelse && (
        <BekreftBestillingModal
          bestilling={bestillingTilBekreftelse}
          error={bestillMutation.error}
          isPending={bestillMutation.isPending}
          letterTemplate={letterTemplate}
          onBekreft={() => bestillMutation.mutate(bestillingTilBekreftelse)}
          onClose={() => setBestillingTilBekreftelse(null)}
          saksId={saksId}
        />
      )}
    </>
  );
}

const BekreftBestillingModal = (props: {
  saksId: string;
  letterTemplate: LetterMetadata;
  bestilling: BestillDelautomatiskBrevRequest;
  isPending: boolean;
  error: Nullable<AxiosError<Error> | Error>;
  onBekreft: () => void;
  onClose: () => void;
}) => {
  const enheter = useQuery(getEnheter).data ?? [];
  const enhetNavn = enheter.find((enhet) => enhet.id === props.bestilling.enhetsId)?.navn ?? props.bestilling.enhetsId;
  const mottaker = props.bestilling.idTSSEkstern
    ? { type: "Samhandler" as const, tssId: props.bestilling.idTSSEkstern, navn: null }
    : null;

  return (
    <Modal
      data-testid="bekreft-delautomatisk-brev-modal"
      header={{ heading: "Vil du sende brevet?", size: "small", closeButton: false }}
      onClose={() => {
        if (!props.isPending) props.onClose();
      }}
      open
      portal
      width="small"
    >
      <Modal.Body>
        <VStack gap="space-16">
          <BodyLong size="small">
            Brevet sendes automatisk til mottaker uten mulighet for redigering. Du kan ikke angre denne handlingen.
          </BodyLong>
          <VStack gap="space-4">
            <Label size="small">Brev</Label>
            <BodyShort size="small">{props.letterTemplate.name}</BodyShort>
          </VStack>
          <OppsummeringAvMottaker mottaker={mottaker} saksId={props.saksId} withTitle />
          <VStack gap="space-4">
            <Label size="small">Språk</Label>
            <BodyShort size="small">{SPRAAK_ENUM_TO_TEXT[props.bestilling.spraak]}</BodyShort>
          </VStack>
          <VStack gap="space-4">
            <Label size="small">Avsenderenhet</Label>
            <BodyShort size="small">{enhetNavn}</BodyShort>
          </VStack>
          {props.error && <ApiError error={props.error} title="Bestilling feilet" />}
        </VStack>
      </Modal.Body>
      <Modal.Footer>
        <Button
          data-testid="send-delautomatisk-brev"
          loading={props.isPending}
          onClick={props.onBekreft}
          size="small"
          type="button"
        >
          {props.error ? "Prøv igjen" : "Send brev"}
        </Button>
        <Button disabled={props.isPending} onClick={props.onClose} size="small" type="button" variant="secondary">
          Avbryt
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
