import { PencilIcon, XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Box, Button, HStack, Label, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useState } from "react";

import { getBrev } from "~/api/brev-queries";
import { endreMottaker, fjernOverstyrtMottaker, hentAlleBrevInfoForSak, hentPdfForBrev } from "~/api/sak-api-endpoints";
import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import type { BrevInfo, Mottaker } from "~/types/brev";
import { mapEndreMottakerValueTilMottaker } from "~/utils/AdresseUtils";

import OppsummeringAvMottaker from "./OppsummeringAvMottaker";

const EndreMottakerMedOppsummeringOgApiHåndtering = (props: {
  saksId: string;
  brev: BrevInfo;
  endreAsIcon?: boolean;
  overrideOppsummering?: (edit: React.ReactNode) => React.ReactNode;
  withOppsummeringTitle?: boolean;
  kanTilbakestilleMottaker?: boolean;
  withGap?: boolean;
}) => {
  const queryClient = useQueryClient();
  const mottakerMutation = useMutation<BrevInfo, AxiosError, Mottaker>({
    mutationFn: (mottaker) => endreMottaker(props.saksId, props.brev.id, { mottaker: mottaker }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brevInfo) => (brevInfo.id === response.id ? response : brevInfo)),
      );
      queryClient.setQueryData(getBrev.queryKey(props.brev.id), response);
      queryClient.invalidateQueries({
        queryKey: hentPdfForBrev.queryKey(props.brev.id),
      });
      setModalÅpen(false);
    },
  });

  const fjernMottakerMutation = useMutation<void, AxiosError>({
    mutationFn: () => fjernOverstyrtMottaker({ saksId: props.saksId, brevId: props.brev.id }),
    onSuccess: () => {
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brevInfo) =>
          brevInfo.id === props.brev.id ? { ...props.brev, mottaker: null } : brevInfo,
        ),
      );
      queryClient.invalidateQueries({
        queryKey: hentPdfForBrev.queryKey(props.brev.id),
      });
    },
  });

  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  return (
    <VStack gap={props.withGap ? "space-8" : "space-0"}>
      {modalÅpen && (
        <EndreMottakerModal
          error={mottakerMutation.error}
          isPending={mottakerMutation.isPending}
          onBekreftNyMottaker={(mottaker) => {
            mottakerMutation.mutate(mapEndreMottakerValueTilMottaker(mottaker));
          }}
          onClose={() => setModalÅpen(false)}
          resetOnBekreftState={() => mottakerMutation.reset()}
          åpen={modalÅpen}
        />
      )}
      {props.overrideOppsummering ? (
        <HStack align="center" gap="space-8">
          {props.overrideOppsummering(
            <Box asChild borderRadius="4">
              <Button
                icon={<PencilIcon />}
                onClick={() => setModalÅpen(true)}
                size="xsmall"
                type="button"
                variant="tertiary"
              />
            </Box>,
          )}
        </HStack>
      ) : (
        <VStack gap="space-8">
          {props.withOppsummeringTitle && (
            <HStack align="center" justify="space-between">
              <Label size="small">Mottaker</Label>
              <Button
                icon={<PencilIcon />}
                iconPosition="right"
                onClick={() => setModalÅpen(true)}
                size="xsmall"
                type="button"
                variant="tertiary"
              >
                Endre
              </Button>
            </HStack>
          )}
          {!props.withOppsummeringTitle && (
            <Button
              icon={<PencilIcon />}
              iconPosition="right"
              onClick={() => setModalÅpen(true)}
              size="xsmall"
              type="button"
              variant="tertiary"
            >
              Endre
            </Button>
          )}
          <OppsummeringAvMottaker mottaker={props.brev.mottaker} saksId={props.saksId} withTitle={false} />
        </VStack>
      )}
      {props.kanTilbakestilleMottaker && "Kan tilbakestille mottaker"}

      {props.brev.mottaker && props.kanTilbakestilleMottaker && (
        <HStack>
          <Button
            css={{ margin: "0 calc(-1 * var(--ax-space-8))" }}
            loading={fjernMottakerMutation.isPending}
            onClick={() => fjernMottakerMutation.mutate()}
            size="xsmall"
            type="button"
            variant="tertiary"
          >
            Tilbakestill mottaker
          </Button>
          {fjernMottakerMutation.isError && (
            <XMarkOctagonFillIcon
              css={{
                alignSelf: "center",
                color: "var(--ax-text-logo)",
              }}
              title="error"
            />
          )}
        </HStack>
      )}
    </VStack>
  );
};

export default EndreMottakerMedOppsummeringOgApiHåndtering;
