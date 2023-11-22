import { css } from "@emotion/react";
import { StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Heading } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useParams, useRouteContext } from "@tanstack/react-router";
import { useContext } from "react";

import {
  addFavoritt,
  deleteFavoritt,
  favoritterKeys,
  getFavoritter,
  getLetterTemplate,
  letterTemplatesKeys,
} from "../../api/skribenten-api-endpoints";
import { Divider } from "../../components/Divider";
import { brevmalRoute, brevvelgerRoute } from "../../tanStackRoutes";
import type { LetterMetadata } from "../../types/apiTypes";

export function ValgtBrevmal() {
  const { brevmalId } = useParams({ from: brevmalRoute.id });

  const { getSakQueryOptions } = useRouteContext({ from: brevmalRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  // TODO: deling av data mellom routes må kunne gjøres enklere enn dette.
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
    <div>
      <FavorittButton />
      <Heading level="2" size="medium">
        {letterTemplate.name}
      </Heading>
      <div
        css={css`
          display: flex;
          align-items: center;
          gap: var(--a-spacing-3);
          align-self: stretch;
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
        <BodyShort size="small">REDIGERBAR MAL</BodyShort>
      </div>
      <Heading level="3" size="xsmall">
        Formål og målgruppe
      </Heading>
      <BodyShort size="small">TODO</BodyShort>
      <Divider />
      <Heading level="3" size="xsmall">
        Mottaker
      </Heading>
      <span>TODO</span>
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
      icon={<StarIcon aria-hidden />}
      onClick={() => toggleFavoritesMutation.mutate(brevmalId)}
      size="small"
      variant="secondary"
    >
      Legg til som favoritt
    </Button>
  );
}
