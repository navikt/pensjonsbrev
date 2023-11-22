import { css } from "@emotion/react";
import { StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Heading } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useParams, useRouteContext, useSearch } from "@tanstack/react-router";

import { addFavoritt, deleteFavoritt, getFavoritter, getLetterTemplate } from "../../api/skribenten-api-endpoints";
import { Divider } from "../../components/Divider";
import { brevmalRoute } from "../../tanStackRoutes";
import type { LetterMetadata } from "../../types/apiTypes";
import { BrevvelgerTabOptions } from "./BrevvelgerPage";

export function ValgtBrevmal() {
  const { fane } = useSearch({ from: brevmalRoute.id });

  return (
    <div
      css={css`
        display: flex;
        width: 400px;
        padding: var(--a-spacing-6) var(--a-spacing-4);
        flex-direction: column;
        align-items: flex-start;
        gap: var(--a-spacing-5);
        border-left: 2px solid var(--a-gray-400);
        border-right: 1px solid var(--a-gray-400);
      `}
    >
      <FavorittButton />
      {fane === BrevvelgerTabOptions.BREVMALER ? <Brevmal /> : <Eblankett />}
    </div>
  );
}

function Brevmal() {
  const { brevmalId } = useParams({ from: brevmalRoute.id });

  const { getSakQueryOptions } = useRouteContext({ from: brevmalRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  // TODO: deling av data mellom routes må kunne gjøres enklere enn dette??
  const letterTemplate = useQuery({
    queryKey: getLetterTemplate.queryKey(sak?.sakType as string),
    queryFn: () => getLetterTemplate.queryFn(sak?.sakType as string),
    select: (letterTemplates) =>
      letterTemplates.kategorier
        .flatMap((kategori) => kategori.templates)
        .find((letterMetadata) => letterMetadata.id === brevmalId),
    enabled: !!sak,
  }).data;

  if (!letterTemplate) {
    return <></>;
  }

  return (
    <>
      <MalHeading letterTemplate={letterTemplate} />
      <Heading level="3" size="xsmall">
        Formål og målgruppe
      </Heading>
      <BodyShort size="small">TODO</BodyShort>
      <Divider />
      <Heading level="3" size="xsmall">
        Mottaker
      </Heading>
      <span>TODO</span>
    </>
  );
}

function Eblankett() {
  const { brevmalId } = useParams({ from: brevmalRoute.id });

  const { getSakQueryOptions } = useRouteContext({ from: brevmalRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  // TODO: deling av data mellom routes må kunne gjøres enklere enn dette??
  const letterTemplate = useQuery({
    queryKey: getLetterTemplate.queryKey(sak?.sakType as string),
    queryFn: () => getLetterTemplate.queryFn(sak?.sakType as string),
    select: (letterTemplates) => letterTemplates.eblanketter.find((letterMetadata) => letterMetadata.id === brevmalId),
    enabled: !!sak,
  }).data;

  if (!letterTemplate) {
    return <></>;
  }

  return (
    <>
      <MalHeading letterTemplate={letterTemplate} />
      <Heading level="3" size="xsmall">
        Formål og målgruppe
      </Heading>
      <BodyShort size="small">E-blankett</BodyShort>
      <Divider />
    </>
  );
}

function MalHeading({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <div>
      <Heading level="2" size="medium">
        {letterTemplate.name}
      </Heading>
      <div
        css={css`
          display: flex;
          align-items: center;
          gap: var(--a-spacing-3);
          margin-top: var(--a-spacing-2);
        `}
      >
        <div
          aria-hidden
          css={css`
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background: var(--a-green-500);
          `}
        />
        <BodyShort size="small">REDIGERBAR {letterTemplate.isEblankett ? "BLANKETT" : "MAL"}</BodyShort>
      </div>
    </div>
  );
}

function FavorittButton() {
  const { brevmalId } = useParams({ from: brevmalRoute.id });
  const queryClient = useQueryClient();
  const isFavoritt = useQuery({
    ...getFavoritter,
    select: (favoritter) => favoritter.includes(brevmalId),
  }).data;

  const toggleFavoritesMutation = useMutation<unknown, unknown, string>({
    mutationFn: (id) => (isFavoritt ? deleteFavoritt(id) : addFavoritt(id)),
    onSettled: () => queryClient.invalidateQueries({ queryKey: getFavoritter.queryKey }),
  });

  if (isFavoritt) {
    return (
      <Button
        css={css`
          width: fit-content;
        `}
        icon={<StarFillIcon aria-hidden />}
        onClick={() => toggleFavoritesMutation.mutate(brevmalId)}
        size="small"
        variant="secondary"
      >
        Fjern som favoritt
      </Button>
    );
  }

  return (
    <Button
      css={css`
        width: fit-content;
      `}
      icon={<StarIcon aria-hidden />}
      onClick={() => toggleFavoritesMutation.mutate(brevmalId)}
      size="small"
      variant="secondary"
    >
      Legg til som favoritt
    </Button>
  );
}
