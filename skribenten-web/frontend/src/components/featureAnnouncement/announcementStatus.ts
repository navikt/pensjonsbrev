import { useMutation, useQueryClient } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useCallback } from "react";

import {
  type AnnouncementStatus,
  acknowledgeAnnouncement,
  getAnnouncementStatus,
  markAnnouncementSeen,
} from "~/api/skribenten-api-endpoints";
import { logError } from "~/utils/logger";

/**
 * Marks an announcement as acknowledged, i.e. the user has closed its tour. It then no longer starts automatically,
 * and it no longer gives a red dot in the notification bell.
 */
export const useAcknowledgeAnnouncement = () =>
  useSaveAnnouncementStatus("gjennomfoertOmvisningen", acknowledgeAnnouncement);

/**
 * Marks an announcement as seen, i.e. the user has opened the notification bell while it was listed. It then no longer
 * gives a red dot, but its tour still starts automatically until it is acknowledged.
 */
export const useMarkAnnouncementSeen = () => useSaveAnnouncementStatus("settAnnonsen", markAnnouncementSeen);

// Updates the cached status right away, so the tour and the bell react at once, and then saves it in the backend.
// Only call it once the status has been read: a read that is still pending would overwrite the update when it arrives.
function useSaveAnnouncementStatus(field: keyof AnnouncementStatus, save: (id: string) => Promise<void>) {
  const queryClient = useQueryClient();
  const { mutate } = useMutation({
    mutationFn: (id: string) => save(id),
    retry: 2,
    onError: (error) =>
      logError(error, error instanceof AxiosError ? error.response?.status : undefined).catch(() =>
        console.error("Unable to log error message"),
      ),
  });

  return useCallback(
    (id: string) => {
      queryClient.setQueryData(
        getAnnouncementStatus.queryKey,
        (previous: AnnouncementStatus = { settAnnonsen: [], gjennomfoertOmvisningen: [] }) =>
          previous[field].includes(id) ? previous : { ...previous, [field]: [...previous[field], id] },
      );
      mutate(id);
    },
    [queryClient, field, mutate],
  );
}
