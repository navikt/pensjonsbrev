import { createFileRoute } from "@tanstack/react-router";

import { RedigeringPage } from "~/pages/Brevredigering/RedigeringPage";

export const Route = createFileRoute("/saksnummer/$sakId/redigering/$templateId")({
  component: RedigeringPage,
});
