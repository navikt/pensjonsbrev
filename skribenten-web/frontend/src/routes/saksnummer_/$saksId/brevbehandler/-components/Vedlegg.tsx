import { css } from "@emotion/react";
import { PencilIcon, PlusCircleIcon, TrashIcon } from "@navikt/aksel-icons";
import {
  Alert,
  BodyShort,
  BoxNew,
  Button,
  Checkbox,
  CheckboxGroup,
  HStack,
  Loader,
  Modal,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { Controller, useForm } from "react-hook-form";

import { getBrev } from "~/api/brev-queries";
import { getBrevVedlegg, hentPdfForBrev, oppdaterVedlegg } from "~/api/sak-api-endpoints";
import { P1EditModal } from "~/components/P1/P1EditModal";
import type { AlltidValgbartVedlegg } from "~/types/brev";
import { type BrevInfo, P1_BREVKODE } from "~/types/brev";
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
      oppdaterVedlegg(props.saksId, props.brev.id, { alltidValgbareVedlegg: vedlegg }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(props.brev.id) });
      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.id) });
      handleCloseModal();
    },
  });

  const fjernVedleggMutation = useMutation({
    mutationFn: (vedleggToRemove: AlltidValgbartVedlegg) =>
      oppdaterVedlegg(props.saksId, props.brev.id, {
        alltidValgbareVedlegg: savedVedlegg.filter((v) => v.kode !== vedleggToRemove.kode),
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(props.brev.id) });
      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.id) });
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
            <BoxNew asChild borderRadius="4">
              <Button
                data-cy="p1-edit-button"
                icon={<PencilIcon />}
                onClick={() => setIsP1ModalOpen(true)}
                size="xsmall"
                type="button"
                variant="tertiary"
              />
            </BoxNew>
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
          {vedleggKoder && vedleggKoder.length > 0 ? (
            <Controller
              control={form.control}
              name="valgteVedlegg"
              render={({ field }) => (
                <CheckboxGroup
                  hideLegend
                  legend="Velg vedlegg"
                  onChange={(selectedKodes: string[]) => {
                    const selectedVedlegg = vedleggKoder.filter((v) => selectedKodes.includes(v.kode));
                    field.onChange(selectedVedlegg);
                  }}
                  value={field.value.map((v) => v.kode)}
                >
                  {vedleggKoder.map((vedlegg) => (
                    <Checkbox key={vedlegg.kode} value={vedlegg.kode}>
                      {vedlegg.visningstekst}
                    </Checkbox>
                  ))}
                </CheckboxGroup>
              )}
            />
          ) : (
            <BodyShort>Ingen vedlegg tilgjengelig</BodyShort>
          )}
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
              Legg til
            </Button>
          </HStack>
        </Modal.Footer>
      </Modal>
    </VStack>
  );
};
