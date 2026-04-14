import { css } from "@emotion/react";
import { PencilIcon, PlusCircleIcon, TrashIcon } from "@navikt/aksel-icons";
import {
  Alert,
  BodyShort,
  Box,
  Button,
  Checkbox,
  CheckboxGroup,
  HStack,
  Label,
  Loader,
  LocalAlert,
  Modal,
  ReadMore,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Fragment, useState } from "react";
import type { UseFormReturn } from "react-hook-form";
import { Controller, useForm } from "react-hook-form";

import { getBrev } from "~/api/brev-queries";
import { getBrevVedlegg, hentPdfForBrev, oppdaterVedlegg } from "~/api/sak-api-endpoints";
import { P1EditModal } from "~/components/P1/P1EditModal";
import type { SpraakKode } from "~/types/apiTypes";
import type { AlltidValgbartVedlegg, AlltidValgbartVedleggV2 } from "~/types/brev";
import { type BrevInfo, P1_BREVKODE } from "~/types/brev";
import { LANGUAGE_CODE_TO_TEXT, SPRAAKKODE_TO_LANGUAGE_CODE } from "~/types/nameMappings";
import { getErrorMessage } from "~/utils/errorUtils";

type VedleggFormData = {
  valgteVedlegg: AlltidValgbartVedlegg[];
};

export const Vedlegg = (props: { saksId: string; brev: BrevInfo; erLaast: boolean }) => {
  const queryClient = useQueryClient();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isP1ModalOpen, setIsP1ModalOpen] = useState(false);

  const isP1Brev = props.brev.brevkode === P1_BREVKODE;

  const { data: brevData } = useQuery({
    queryKey: getBrev.queryKey(props.brev.id),
    queryFn: () => getBrev.queryFn(props.saksId, props.brev.id, false),
  });

  const savedVedlegg: AlltidValgbartVedlegg[] = brevData?.valgteVedlegg ?? [];

  const form = useForm<VedleggFormData>({
    defaultValues: { valgteVedlegg: [] },
  });

  const {
    data: vedleggKoder,
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: getBrevVedlegg.queryKey(props.saksId, props.brev.id),
    queryFn: () => getBrevVedlegg.queryFn(props.saksId, props.brev.id),
  });

  const getVedleggLabel = (vedlegg: AlltidValgbartVedlegg) => vedlegg.visningstekst ?? vedlegg.kode;

  const leggTilVedleggMutation = useMutation({
    mutationFn: (vedlegg: AlltidValgbartVedlegg[]) =>
      oppdaterVedlegg(props.saksId, props.brev.id, { valgteVedlegg: vedlegg }),
    onSuccess: (data) => {
      queryClient.setQueryData(getBrev.queryKey(props.brev.id), data);
      queryClient.invalidateQueries({
        queryKey: hentPdfForBrev.queryKey(props.brev.id),
      });
      handleCloseModal();
    },
  });

  const fjernVedleggMutation = useMutation({
    mutationFn: (vedleggToRemove: AlltidValgbartVedlegg) =>
      oppdaterVedlegg(props.saksId, props.brev.id, {
        valgteVedlegg: savedVedlegg.filter((lagretVedlegg) => lagretVedlegg.kode !== vedleggToRemove.kode),
      }),
    onSuccess: (data) => {
      queryClient.setQueryData(getBrev.queryKey(props.brev.id), data);
      queryClient.invalidateQueries({
        queryKey: hentPdfForBrev.queryKey(props.brev.id),
      });
    },
  });

  const handleOpenModal = () => {
    form.reset({ valgteVedlegg: savedVedlegg });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.reset({ valgteVedlegg: [] });
  };

  const handleLeggTil = form.handleSubmit((data) => {
    leggTilVedleggMutation.mutate(data.valgteVedlegg);
  });

  const hasVedleggToShow = isP1Brev || savedVedlegg.length > 0;
  const hasVedleggToAdd = vedleggKoder && vedleggKoder.length > 0;

  if (!hasVedleggToShow && !hasVedleggToAdd && !isLoading && !isError) {
    return null;
  }

  if (isLoading) {
    return (
      <div>
        <HStack align="center" gap="space-8">
          <BodyShort size="small" weight="semibold">
            Vedlegg
          </BodyShort>
        </HStack>
        <Loader size="small" />
      </div>
    );
  }

  if (isError) {
    return (
      <div>
        <HStack align="center" gap="space-8">
          <BodyShort size="small" weight="semibold">
            Vedlegg
          </BodyShort>
        </HStack>
        <Alert size="small" variant="error">
          {getErrorMessage(error)}
        </Alert>
      </div>
    );
  }

  if (props.erLaast && !hasVedleggToShow) {
    return (
      <VStack gap="space-8">
        <BodyShort size="small" weight="semibold">
          Ingen vedlegg
        </BodyShort>
      </VStack>
    );
  }

  return (
    <VStack gap="space-8">
      <HStack align="center" gap="space-8">
        <BodyShort size="small" weight="semibold">
          Vedlegg
        </BodyShort>
        {!props.erLaast && (
          <Button
            css={css`
              margin-left: auto;
            `}
            icon={<PlusCircleIcon aria-hidden />}
            onClick={handleOpenModal}
            size="xsmall"
            title="Legg til vedlegg"
            variant="tertiary"
          />
        )}
      </HStack>
      {/* P1 vedlegg with its own edit button */}
      {isP1Brev && (
        <HStack align="center" justify="space-between">
          <BodyShort size="small">P1</BodyShort>
          {!props.erLaast && (
            <Box asChild borderRadius="4">
              <Button
                data-cy="p1-edit-button"
                icon={<PencilIcon />}
                onClick={() => setIsP1ModalOpen(true)}
                size="xsmall"
                type="button"
                variant="tertiary"
              />
            </Box>
          )}
        </HStack>
      )}
      {/* Other saved vedlegg */}
      {savedVedlegg.length > 0 && (
        <VStack gap="space-4">
          {savedVedlegg.map((vedlegg) => (
            <HStack align="center" justify="space-between" key={vedlegg.kode}>
              <BodyShort size="small">{getVedleggLabel(vedlegg)}</BodyShort>
              {!props.erLaast && (
                <Button
                  icon={<TrashIcon title="Fjern vedlegg" />}
                  loading={fjernVedleggMutation.isPending && fjernVedleggMutation.variables?.kode === vedlegg.kode}
                  onClick={() => fjernVedleggMutation.mutate(vedlegg)}
                  size="xsmall"
                  variant="tertiary"
                />
              )}
            </HStack>
          ))}
        </VStack>
      )}
      {/* P1 Edit Modal */}
      {isP1ModalOpen && (
        <P1EditModal
          brevId={props.brev.id}
          onClose={() => setIsP1ModalOpen(false)}
          open={isP1ModalOpen}
          saksId={props.saksId}
        />
      )}
      {/* Add vedlegg modal */}
      <Modal
        css={css`
          width: 700px;
        `}
        header={{ heading: "Legg til vedlegg/skjema" }}
        onClose={handleCloseModal}
        open={isModalOpen}
      >
        <Modal.Body>
          <VedleggModalInnhold brevSpraak={props.brev.spraak} form={form} vedleggKoder={vedleggKoder} />
          {leggTilVedleggMutation.isError && (
            <Alert size="small" variant="error">
              {getErrorMessage(leggTilVedleggMutation.error)}
            </Alert>
          )}
        </Modal.Body>
        <Modal.Footer>
          <HStack gap="space-8" justify="space-between" width="100%">
            <Button onClick={handleCloseModal} variant="tertiary">
              Avbryt
            </Button>
            <Button loading={leggTilVedleggMutation.isPending} onClick={handleLeggTil} variant="primary">
              Lagre
            </Button>
          </HStack>
        </Modal.Footer>
      </Modal>
    </VStack>
  );
};

type VedleggModalInnholdProps = {
  vedleggKoder: AlltidValgbartVedleggV2[] | undefined;
  brevSpraak: SpraakKode;
  form: UseFormReturn<VedleggFormData>;
};

const VedleggModalInnhold = (props: VedleggModalInnholdProps) => {
  const { vedleggKoder, form } = props;

  if (!vedleggKoder || vedleggKoder.length === 0) {
    return <BodyShort>Ingen vedlegg tilgjengelig</BodyShort>;
  }

  const brevSpraakKode = SPRAAKKODE_TO_LANGUAGE_CODE[props.brevSpraak];
  const brevSpraakTekst = brevSpraakKode ? LANGUAGE_CODE_TO_TEXT[brevSpraakKode] : props.brevSpraak;

  const tilgjengelige = vedleggKoder.filter((vedlegg) => vedlegg.tilgjengeligForSpraak);
  const utilgjengelige = vedleggKoder.filter((vedlegg) => !vedlegg.tilgjengeligForSpraak);
  const ingenTilgjengelige = tilgjengelige.length === 0;
  const noenErUtilgjengelige = utilgjengelige.length > 0 && tilgjengelige.length > 0;

  return (
    <VStack gap="space-8">
      {noenErUtilgjengelige && (
        <LocalAlert
          css={css`
            .aksel-base-alert__icon {
              margin-top: 0;
            }
          `}
          status="announcement"
        >
          <LocalAlert.Header
            css={css`
              align-items: center;
              padding-top: var(--ax-space-6);
              padding-bottom: var(--ax-space-6);
            `}
          >
            Noen skjemaer er ikke tilgjengelig på språket i brevet.
          </LocalAlert.Header>
        </LocalAlert>
      )}
      {ingenTilgjengelige ? (
        <VStack gap="space-4">
          <LocalAlert
            css={css`
              .aksel-base-alert__icon {
                margin-top: 0;
              }
            `}
            status="warning"
          >
            <LocalAlert.Header
              css={css`
                align-items: center;
                padding-top: var(--ax-space-6);
                padding-bottom: var(--ax-space-6);
              `}
            >
              Ingen skjemaer er tilgjengelig basert på språket i brevet.
            </LocalAlert.Header>
          </LocalAlert>
        </VStack>
      ) : (
        <Controller
          control={form.control}
          name="valgteVedlegg"
          render={({ field }) => (
            <CheckboxGroup
              hideLegend
              legend="Velg vedlegg"
              onChange={(selectedKodes: string[]) => {
                const selectedVedlegg = vedleggKoder.filter(
                  (vedlegg) => selectedKodes.includes(vedlegg.kode) && vedlegg.tilgjengeligForSpraak,
                );
                field.onChange(selectedVedlegg);
              }}
              value={field.value.map((valgtVedlegg) => valgtVedlegg.kode)}
            >
              {tilgjengelige.map((vedlegg) => (
                <Checkbox key={vedlegg.kode} value={vedlegg.kode}>
                  <VStack gap="space-1">
                    <span>{vedlegg.visningstekst}</span>
                    <BodyShort as="span" size="small">
                      {vedlegg.spraak.map((spraakKode, index) => {
                        const isCurrentLanguage = spraakKode === brevSpraakKode;
                        const tekst = LANGUAGE_CODE_TO_TEXT[spraakKode] ?? spraakKode;
                        return (
                          <Fragment key={spraakKode}>
                            {index > 0 && ", "}
                            {isCurrentLanguage ? <strong>{tekst}</strong> : tekst}
                          </Fragment>
                        );
                      })}
                    </BodyShort>
                  </VStack>
                </Checkbox>
              ))}
            </CheckboxGroup>
          )}
        />
      )}
      {utilgjengelige.length > 0 && (
        <CheckboxGroup hideLegend legend="Skjemaer utilgjengelig på valgt språk">
          {noenErUtilgjengelige && <Label size="small">Skjemaer som ikke er tilgjengelig på {brevSpraakTekst}</Label>}
          {utilgjengelige.map((vedlegg) => (
            <Checkbox
              css={css`
                .aksel-checkbox__input {
                  pointer-events: none;
                }

                .aksel-checkbox__label,
                .aksel-checkbox__description {
                  color: var(--ax-text-neutral-subtle);
                }
              `}
              description={vedlegg.spraak
                .map((spraakKode) => LANGUAGE_CODE_TO_TEXT[spraakKode] ?? spraakKode)
                .join(", ")}
              key={vedlegg.kode}
              readOnly
              value={vedlegg.kode}
            >
              {vedlegg.visningstekst}
            </Checkbox>
          ))}
        </CheckboxGroup>
      )}
      {ingenTilgjengelige && (
        <ReadMore header="Hvorfor kan jeg ikke legge til skjema på et annet språk/målform?">
          Brevet er skrevet på et annet språk enn det skjemaet støtter. Å kombinere språk eller målform, eksempelvis
          bokmål og nynorsk, er ikke anbefalt, ifølge Språkloven.
        </ReadMore>
      )}
    </VStack>
  );
};
