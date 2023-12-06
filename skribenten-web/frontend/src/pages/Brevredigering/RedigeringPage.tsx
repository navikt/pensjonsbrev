import { useQuery } from "@tanstack/react-query";
import { useParams } from "@tanstack/react-router";

import { getTemplate } from "../../api/skribenten-api-endpoints";
import { redigeringRoute } from "../../tanStackRoutes";

export function RedigeringPage() {
  const { templateId } = useParams({ from: redigeringRoute.id });
  const b = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

  const a = useQuery({
    queryKey: getTemplate.queryKey(b),
    queryFn: () => getTemplate.queryFn(b),
  }).data;

  console.log(a);

  return <div>Redigering</div>;
}
