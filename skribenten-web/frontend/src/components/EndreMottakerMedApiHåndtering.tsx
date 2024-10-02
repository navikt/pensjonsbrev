import { css } from "@emotion/react";
import { PencilIcon, XMarkOctagonFillIcon } from "@navikt/aksel-icons";
import { Button, HStack, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useState } from "react";

import { delvisOppdaterBrev, fjernOverstyrtMottaker, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { EndreMottakerModal } from "~/routes/saksnummer_/$saksId/brevvelger/-components/endreMottaker/EndreMottaker";
import { mapEndreMottakerValueTilMottaker } from "~/types/AdresseUtils";
import type { BrevInfo, DelvisOppdaterBrevResponse, Mottaker } from "~/types/brev";

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
  const mottakerMutation = useMutation<DelvisOppdaterBrevResponse, AxiosError, Mottaker>({
    mutationFn: (mottaker) =>
      delvisOppdaterBrev({
        saksId: props.saksId,
        brevId: props.brev.id,
        mottaker: mottaker,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? response.info : brev)),
      );
      setModalÅpen(false);
    },
  });

  const fjernMottakerMutation = useMutation<void, AxiosError>({
    mutationFn: () => fjernOverstyrtMottaker({ saksId: props.saksId, brevId: props.brev.id }),
    onSuccess: () => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? { ...props.brev, mottaker: null } : brev)),
      );
    },
  });

  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  return (
    <VStack gap={props.withGap ? "2" : "0"}>
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
      <HStack align={"center"} gap="2">
        {props.overrideOppsummering ? (
          props.overrideOppsummering(
            <div>
              {props.endreAsIcon && (
                <Button
                  css={css`
                    padding: 0;
                  `}
                  icon={<PencilIcon fontSize="24px" />}
                  onClick={() => setModalÅpen(true)}
                  size="xsmall"
                  type="button"
                  variant="tertiary"
                />
              )}
            </div>,
          )
        ) : (
          <OppsummeringAvMottaker
            mottaker={props.brev.mottaker}
            saksId={props.saksId}
            withTitle={props.withOppsummeringTitle ?? false}
          />
        )}
      </HStack>
      <HStack>
        {!props.endreAsIcon && (
          <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
            Endre mottaker
          </Button>
        )}

        {props.brev.mottaker && props.kanTilbakestilleMottaker && (
          <HStack>
            <Button
              css={css`
                padding: 0.5rem 0;
              `}
              loading={fjernMottakerMutation.isPending}
              onClick={() => fjernMottakerMutation.mutate()}
              size="small"
              type="button"
              variant="tertiary"
            >
              Tilbakestill mottaker
            </Button>
            {fjernMottakerMutation.isError && (
              <XMarkOctagonFillIcon
                css={css`
                  align-self: center;
                  color: var(--a-nav-red);
                `}
                title="error"
              />
            )}
          </HStack>
        )}
      </HStack>
    </VStack>
  );
};

export default EndreMottakerMedOppsummeringOgApiHåndtering;
