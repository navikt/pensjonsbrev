import { useQuery } from "@tanstack/react-query";
import { useRouteContext } from "@tanstack/react-router";

import { sakRoute } from "../../tanStackRoutes";

export function SakPage() {
  const { queryOptions } = useRouteContext({ from: sakRoute.id });
  const sak = useQuery(queryOptions);

  return <div>Sak</div>;
}
