import { useParams } from "@tanstack/react-router";

import { redigeringRoute } from "../../tanStackRoutes";

export function RedigeringPage() {
  const a = useParams({ from: redigeringRoute.id });
  console.log(a);

  return <div>Redigering</div>;
}
