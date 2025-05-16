import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import {
  BodyLong,
  BodyShort,
  Button,
  Checkbox,
  CheckboxGroup,
  HStack,
  Label,
  List,
  Modal,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { partition } from "lodash";
import { useEffect, useMemo } from "react";
import { Controller, useForm } from "react-hook-form";
import { z } from "zod";

import { hentAlleBrevForSak, sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BestillBrevError, BestillBrevResponse } from "~/types/brev";
import { type BrevInfo } from "~/types/brev";
import { erBrevArkivert, erBrevKlar, erBrevKlarTilAttestering } from "~/utils/brevUtils";
import { queryFold } from "~/utils/tanstackUtils";

import { useBrevInfoKlarTilAttestering } from "../../kvittering/-components/KlarTilAttesteringContext";
import { useSendtBrev } from "../../kvittering/-components/SendtBrevContext";
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

const validationSchema = z.object({
  valgteBrevSomSkalSendes: z.array(z.number()).min(1, "Du må velge minst ett brev å sende"),
});

export const FerdigstillOgSendBrevModal = (properties: { sakId: string; åpen: boolean; onClose: () => void }) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId, vedtaksId } = Route.useSearch();

  const { setBrevResult, sendteBrev } = useSendtBrev();
  const { setBrevListKlarTilAttestering } = useBrevInfoKlarTilAttestering();

  const alleBrevResult = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(properties.sakId),
    queryFn: () => hentAlleBrevForSak.queryFn(properties.sakId),
    select: (data) => data.filter((brev) => erBrevKlar(brev) || erBrevArkivert(brev) || erBrevKlarTilAttestering(brev)),
  });

  const [brevAttestering, brevSending] = useMemo(() => {
    const alle = alleBrevResult.data ?? [];
    return partition(alle, (brev) => erBrevKlarTilAttestering(brev));
  }, [alleBrevResult.data]);

  const sendBrevMutation = useMutation<BestillBrevResponse, AxiosError, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  const form = useForm<z.infer<typeof validationSchema>>({
    defaultValues: { valgteBrevSomSkalSendes: [] },
    resolver: zodResolver(validationSchema),
  });

  useEffect(() => {
    setBrevListKlarTilAttestering(brevAttestering);
  }, [brevAttestering, setBrevListKlarTilAttestering]);

  useEffect(() => {
    form.setValue(
      "valgteBrevSomSkalSendes",
      brevSending.map((brev) => brev.id),
    );
  }, [brevSending, form]);

  const onSendValgteBrev = async (values: { valgteBrevSomSkalSendes: number[] }) => {
    const toSend = values.valgteBrevSomSkalSendes.map((id) => brevSending.find((brev) => brev.id === id)!);

    await Promise.all(
      toSend.map(async (brevInfo) => {
        try {
          const response = await sendBrevMutation.mutateAsync(brevInfo.id);
          if (response.error) {
            setBrevResult(String(brevInfo.id), {
              status: "error",
              brevInfo,
              error: response.error,
            });
          } else {
            setBrevResult(String(brevInfo.id), {
              status: "success",
              brevInfo,
              response,
            });
          }
        } catch (error) {
          setBrevResult(String(brevInfo.id), {
            status: "error",
            brevInfo,
            error: error as AxiosError | BestillBrevError,
          });
        }
      }),
    );

    const sentIds = new Set<number>(
      Object.entries(sendteBrev)
        .filter(([, result]) => result.status === "success")
        .map(([id]) => Number(id)),
    );

    queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (current: BrevInfo[] = []) =>
      current.filter((b) => !sentIds.has(b.id)),
    );

    navigate({
      to: "/saksnummer/$saksId/kvittering",
      params: { saksId: properties.sakId },
      search: { enhetsId, vedtaksId },
    });
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
              query: alleBrevResult,
              initial: () => null,
              pending: () => <Label>Henter alle ferdigstilte brev...</Label>,
              error: (error) => <ApiError error={error} title={"Klarte ikke å hente alle ferdigstilte for saken"} />,
              success: () => (
                <VStack gap="6">
                  {brevSending.length > 0 && (
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
                            data-cy="ferdigstillbrev-valgte-brev"
                            error={fieldState.error?.message}
                            hideLegend
                            legend="Velg brev som skal sendes"
                            onChange={field.onChange}
                            value={field.value}
                          >
                            {brevSending.map((brev) => (
                              <Checkbox key={brev.id} value={brev.id}>
                                {brev.brevtittel}
                              </Checkbox>
                            ))}
                          </CheckboxGroup>
                        )}
                      />
                    </VStack>
                  )}

                  {brevAttestering.length > 0 && (
                    <VStack>
                      <BodyLong>Vedtaksbrev sendes av attestant etter at attestering er gjennomført.</BodyLong>
                      {brevAttestering.map((brev) => (
                        <List key={brev.id}>
                          <List.Item>{brev.brevtittel}</List.Item>
                        </List>
                      ))}
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

            <Button loading={sendBrevMutation.isPending} type="submit">
              Ja, send valgte brev
            </Button>
          </HStack>
        </Modal.Footer>
      </form>
    </Modal>
  );
};
