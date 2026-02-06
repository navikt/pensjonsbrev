import { StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { Button } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { addFavoritt, deleteFavoritt, getFavoritter } from "~/api/skribenten-api-endpoints";

export default function FavoriteButton(props: {
  templateId: string;
  onAddFavorittSuccess?: (templateId: string) => void;
}) {
  const queryClient = useQueryClient();
  const isFavoritt = useQuery({
    ...getFavoritter,
    select: (favoritter) => favoritter.includes(props.templateId),
  }).data;

  const toggleFavoritesMutation = useMutation<unknown, unknown, string>({
    mutationFn: (id) => (isFavoritt ? deleteFavoritt(id) : addFavoritt(id)),
    onSettled: () => queryClient.invalidateQueries({ queryKey: getFavoritter.queryKey }),
    onSuccess: isFavoritt ? undefined : () => props.onAddFavorittSuccess?.(props.templateId),
  });

  if (isFavoritt) {
    return (
      <Button
        data-cy="remove-favorite-button"
        icon={<StarFillIcon aria-hidden />}
        onClick={() => toggleFavoritesMutation.mutate(props.templateId)}
        size="small"
        variant="secondary"
      >
        Fjern som favoritt
      </Button>
    );
  }

  return (
    <Button
      data-color="neutral"
      data-cy="add-favorite-button"
      icon={<StarIcon aria-hidden />}
      onClick={() => toggleFavoritesMutation.mutate(props.templateId)}
      size="small"
      variant="secondary"
    >
      Legg til som favoritt
    </Button>
  );
}
