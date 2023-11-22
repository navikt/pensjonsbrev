import { StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { Button } from "@navikt/ds-react";
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
import { brevmalRoute, brevvelgerRoute } from "../../tanStackRoutes";
import type { LetterMetadata } from "../../types/apiTypes";

export function ValgtBrevmal() {
  const { brevmalId } = useParams({ from: brevmalRoute.id });

  const { getSakQueryOptions } = useRouteContext({ from: brevmalRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  const isFavoritt = useQuery({
    ...getFavoritter,
    select: (favoritter) => favoritter.includes(brevmalId),
  }).data;

  // TODO: deling av data mellom routes må kunne gjøres enklere enn dette.
  const getLetterTemplateQuery = useQuery({
    queryKey: getLetterTemplate.queryKey(sak?.sakType as string),
    queryFn: () => getLetterTemplate.queryFn(sak?.sakType as string),
    select: (letterTemplates) =>
      letterTemplates.kategorier
        .flatMap((kategori) => kategori.templates)
        .find((letterMetadata) => letterMetadata.id === brevmalId),
    enabled: !!sak,
  }).data;

  return (
    <div>
      <FavorittButton />
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
      variant="secondary"
    >
      Legg til som favoritt
    </Button>
  );
}
