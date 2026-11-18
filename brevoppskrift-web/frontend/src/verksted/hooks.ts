import { useQuery } from "@tanstack/react-query";

import { workshopApi, workshopKeys } from "./api";

/** /meg først; utkastlisten hentes bare når brukeren er kjent, så et utilgjengelig API gir én tydelig tilstand. */
export function useDraftsLoader() {
  const me = useQuery({ queryKey: workshopKeys.me, queryFn: workshopApi.me, retry: false });
  const drafts = useQuery({
    queryKey: workshopKeys.drafts,
    queryFn: workshopApi.drafts,
    retry: false,
    enabled: me.isSuccess,
  });
  return { me, drafts };
}
