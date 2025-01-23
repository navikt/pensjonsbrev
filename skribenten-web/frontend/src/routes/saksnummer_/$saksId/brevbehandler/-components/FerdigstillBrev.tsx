import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyLong, BodyShort, Button, Checkbox, CheckboxGroup, HStack, Label, Modal, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { partition } from "lodash";
import { useEffect, useMemo } from "react";
import { Controller, useForm } from "react-hook-form";
import { z } from "zod";

import { hentAlleBrevForSak, sendBrev, sendBrevTilAttestering } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BestillBrevResponse } from "~/types/brev";
import { type BrevInfo } from "~/types/brev";
import { erBrevArkivert, erBrevKlar, skalBrevAttesteres } from "~/utils/brevUtils";
import { queryFold } from "~/utils/tanstackUtils";

import { useSendBrevAttesteringContext } from "../../kvittering/-components/SendBrevTilAttesteringResultatContext";
import { useSendtBrevResultatContext } from "../../kvittering/-components/SendtBrevResultatContext";
import { Route } from "../route";

export const FerdigstillOgSendBrevButton = (properties: {
  sakId: string;
  valgtBrevId?: number;
  brevInfo: BrevInfo[];
  åpneFerdigstillModal: () => void;
}) => {
  const antallBrevSomErKlarTilSending = properties.brevInfo.filter((b) => erBrevKlar(b) || erBrevArkivert(b)).length;
  if (properties.valgtBrevId) {
    const valgtBrev = properties.brevInfo.find((brev) => brev.id === properties.valgtBrevId);

    //safe-guard i det tilfellet bogus id er sendt
    if (!valgtBrev) {
      return <BodyShort>Fant ikke brev med id {properties.valgtBrevId}</BodyShort>;
    }

    return (
      <FerdigstillValgtBrev
        antallBrevKlarTilSending={antallBrevSomErKlarTilSending}
        brev={valgtBrev}
        sakId={properties.sakId}
        åpneFerdigstillModal={properties.åpneFerdigstillModal}
      />
    );
  }

  //hvis ingen spesifikk brev er valgt, og noen står klar til å sendes, vis knapp for å sende
  if (!properties.valgtBrevId && properties.brevInfo.some((b) => erBrevKlar(b) || erBrevArkivert(b))) {
    return (
      <Button onClick={properties.åpneFerdigstillModal} size="small" type="button">
        <HStack gap="2">
          {antallBrevSomErKlarTilSending === 1 ? (
            <Label>Send 1 ferdigstilt brev</Label>
          ) : (
            <Label>Send {antallBrevSomErKlarTilSending} ferdigstilte brev</Label>
          )}
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    );
  }

  //hvis ingen brev er klare til å sendes, og ingen spesifikk brev er valgt, vis ingenting
  return null;
};

const FerdigstillValgtBrev = (properties: {
  sakId: string;
  brev: BrevInfo;
  åpneFerdigstillModal: () => void;
  antallBrevKlarTilSending: number;
}) => {
  const erLåst = useMemo(() => erBrevKlar(properties.brev) || erBrevArkivert(properties.brev), [properties.brev]);

  if (erLåst) {
    return (
      <Button
        onClick={() => {
          if (erLåst) {
            properties.åpneFerdigstillModal();
          }
        }}
        size="small"
        type="button"
      >
        <HStack gap="2">
          <Label>Ferdigstill {properties.antallBrevKlarTilSending} brev</Label>
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    );
  }

  return null;
};

const validationSchema = z
  .object({
    valgteBrevSomSkalSendes: z.array(z.number()),
    valgteBrevSomSkalAttesteres: z.array(z.number()),
  })
  .superRefine((data, refinementContext) => {
    if (data.valgteBrevSomSkalSendes.length === 0 && data.valgteBrevSomSkalAttesteres.length === 0) {
      refinementContext.addIssue({
        code: z.ZodIssueCode.custom,
        message: "Du må velge minst ett brev som skal sendes, eller attesteres",
      });
    }
  });

export const FerdigstillOgSendBrevModal = (properties: { sakId: string; åpen: boolean; onClose: () => void }) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const ferdigstillBrevContext = useSendtBrevResultatContext();
  const attesteringContext = useSendBrevAttesteringContext();

  const alleFerdigstilteBrevResult = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(properties.sakId),
    queryFn: () => hentAlleBrevForSak.queryFn(properties.sakId),
    select: (data) => data.filter((b) => erBrevKlar(b) || erBrevArkivert(b)),
  });

  const alleFerdigstilteBrev = useMemo(() => alleFerdigstilteBrevResult.data ?? [], [alleFerdigstilteBrevResult.data]);
  const [ferdigstilteBrevTilAttestering, ferdigstilteBrevTilSending] = partition(
    alleFerdigstilteBrev,
    skalBrevAttesteres,
  );

  const bestillBrevMutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  const sendBrevTilAttesteringMutation = useMutation<BrevInfo, AxiosError, number>({
    mutationFn: (brevId) => sendBrevTilAttestering({ saksId: properties.sakId, brevId: brevId }),
  });

  const form = useForm<z.infer<typeof validationSchema>>({
    defaultValues: { valgteBrevSomSkalSendes: [] },
    resolver: zodResolver(validationSchema),
  });

  useEffect(() => {
    form.setValue(
      "valgteBrevSomSkalSendes",
      ferdigstilteBrevTilSending.map((brev) => brev.id),
    );
    form.setValue(
      "valgteBrevSomSkalAttesteres",
      ferdigstilteBrevTilAttestering.map((brev) => brev.id),
    );
  }, [ferdigstilteBrevTilAttestering, ferdigstilteBrevTilSending, form]);

  const onSendValgteBrev = async (values: {
    valgteBrevSomSkalSendes: number[];
    valgteBrevSomSkalAttesteres: number[];
  }) => {
    const brevSomSkalSendes = values.valgteBrevSomSkalSendes.map(
      (brevId) => ferdigstilteBrevTilSending.find((brev) => brev.id === brevId)!,
    );
    const brevSomSkalTilAttestering = values.valgteBrevSomSkalAttesteres.map(
      (brevId) => ferdigstilteBrevTilAttestering.find((brev) => brev.id === brevId)!,
    );

    const sendBrevRequests = brevSomSkalSendes.map((brevInfo) =>
      bestillBrevMutation.mutateAsync(brevInfo.id).then(
        //vi har fortsatt behov for informasjon i brevet, så vi returnerer brevinfo sammen med responsen
        (response) => ({ requestType: "sendBrev" as const, status: "success" as const, brevInfo, response }),
        (error: AxiosError) => ({ requestType: "sendBrev" as const, status: "error" as const, brevInfo, error }),
      ),
    );
    const attesteringRequests = brevSomSkalTilAttestering.map((brevInfo) =>
      sendBrevTilAttesteringMutation.mutateAsync(brevInfo.id).then(
        (response) => ({ requestType: "attestering" as const, status: "success" as const, brevInfo, response }),
        (error: AxiosError) => ({ requestType: "attestering" as const, status: "error" as const, brevInfo, error }),
      ),
    );

    const alleRequests = [...sendBrevRequests, ...attesteringRequests];

    const resultat = await Promise.allSettled(alleRequests).then((result) =>
      result.map((response) => {
        switch (response.status) {
          //fordi vi håndterer vanlige caser av rejected i mutation, vil resultatet 'alltid' være fulfilled
          case "fulfilled": {
            return response.value;
          }
          //en safe-guard dersom noe uventet går feil? Kan se om vi kan håndtere dette på en bedre måte
          case "rejected": {
            throw new Error(`Feil ved sending av minst 1 brev. Original error: ${response.reason}`);
          }
        }
      }),
    );

    const [tilAttestering, brevSendt] = partition(resultat, (res) => res.requestType === "attestering");

    ferdigstillBrevContext.setResultat(brevSendt);
    attesteringContext.setResultat(tilAttestering);

    const sendteBrev = new Set(
      resultat
        .filter(
          (resultat) =>
            resultat.status === "success" &&
            resultat.brevInfo.id === brevSomSkalSendes.find((brev) => brev.id === resultat.brevInfo.id)?.id,
        )
        .map((res) => res.brevInfo.id),
    );

    queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (currentBrevInfo: BrevInfo[]) =>
      currentBrevInfo
        .filter((brev) => !sendteBrev.has(brev.id))
        .map((brev) => {
          const brevSomSkalErstattesDetSomFinnes = brevSomSkalTilAttestering.find((b) => b.id === brev.id)!;
          return brev.id === brevSomSkalErstattesDetSomFinnes?.id ? brevSomSkalErstattesDetSomFinnes : brev;
        }),
    );
    navigate({ to: "/saksnummer/$saksId/kvittering", params: { saksId: properties.sakId } });
  };

  return (
    <Modal
      css={css`
        border-radius: 0.25rem;
      `}
      header={{
        heading: "Vil du ferdigstille, og sende disse brevene?",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      portal
      width={600}
    >
      <form onSubmit={form.handleSubmit(onSendValgteBrev)}>
        <Modal.Body>
          <div
            css={css`
              margin-bottom: 1rem;
            `}
          >
            {queryFold({
              query: alleFerdigstilteBrevResult,
              initial: () => null,
              pending: () => <Label>Henter alle ferdigstilte brev...</Label>,
              error: (error) => <ApiError error={error} title={"Klarte ikke å hente alle ferdigstilte for saken"} />,
              success: () => (
                <VStack gap="6">
                  {ferdigstilteBrevTilSending.length > 0 && (
                    <VStack gap="1">
                      <BodyLong>
                        Valgte brev du ferdigstiller og sender vil bli lagt til i brukers dokumentoversikt. Du kan ikke
                        angre denne handlingen.
                      </BodyLong>
                      <Controller
                        control={form.control}
                        name="valgteBrevSomSkalSendes"
                        render={({ field, fieldState }) => (
                          <CheckboxGroup
                            data-cy="ferdigstillbrev-valgte-brev-til-sending"
                            error={fieldState.error?.message}
                            hideLegend
                            legend="Velg brev som skal sendes"
                            onChange={field.onChange}
                            value={field.value}
                          >
                            {ferdigstilteBrevTilSending.map((brev) => (
                              <Checkbox key={brev.id} value={brev.id}>
                                {brev.brevtittel}
                              </Checkbox>
                            ))}
                          </CheckboxGroup>
                        )}
                      />
                    </VStack>
                  )}

                  {ferdigstilteBrevTilAttestering.length > 0 && (
                    <VStack>
                      <BodyLong>Vedtaksbrev vil bli ferdigstilt, men sendes ikke før saken er attestert.</BodyLong>
                      <Controller
                        control={form.control}
                        name="valgteBrevSomSkalAttesteres"
                        render={({ field, fieldState }) => (
                          <CheckboxGroup
                            data-cy="ferdigstillbrev-valgte-brev-til-attestering"
                            error={fieldState.error?.message}
                            hideLegend
                            legend="Velg brev som skal sendes"
                            onChange={field.onChange}
                            value={field.value}
                          >
                            {ferdigstilteBrevTilAttestering.map((brev) => (
                              <Checkbox key={brev.id} value={brev.id}>
                                {brev.brevtittel}
                              </Checkbox>
                            ))}
                          </CheckboxGroup>
                        )}
                      />
                    </VStack>
                  )}
                </VStack>
              ),
            })}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <HStack gap="4">
            <Button onClick={properties.onClose} type="button" variant="tertiary">
              Avbryt
            </Button>

            <Button loading={bestillBrevMutation.isPending} type="submit">
              Ja, send valgte brev
            </Button>
          </HStack>
        </Modal.Footer>
      </form>
    </Modal>
  );
};
