import { css } from "@emotion/react";
import { StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { Button } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import { addBrevFavoritt, deleteBrevFavoritt, getBrevFavoritter } from "~/api/me-endpoints";

export default function FavoriteButton(props: {
  templateId: string;
  onAddFavorittSuccess?: (templateId: string) => void;
}) {
  const queryClient = useQueryClient();
  const isFavoritt = useQuery({
    ...getBrevFavoritter,
    select: (favoritter) => favoritter.includes(props.templateId),
  }).data;

  const toggleFavoritesMutation = useMutation<unknown, unknown, string>({
    mutationFn: (id) => (isFavoritt ? deleteBrevFavoritt(id) : addBrevFavoritt(id)),
    onSettled: () => queryClient.invalidateQueries({ queryKey: getBrevFavoritter.queryKey }),
    onSuccess: isFavoritt ? undefined : () => props.onAddFavorittSuccess?.(props.templateId),
  });

  if (isFavoritt) {
    return (
      <Button
        css={css`
          width: fit-content;
        `}
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
      css={css`
        width: fit-content;
      `}
      data-cy="add-favorite-button"
      icon={<StarIcon aria-hidden />}
      onClick={() => toggleFavoritesMutation.mutate(props.templateId)}
      size="small"
      variant="secondary-neutral"
    >
      Legg til som favoritt
    </Button>
  );
}
