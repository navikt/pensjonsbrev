import { createFileRoute } from "@tanstack/react-router";

import { SelectedTemplate } from "~/pages/Brevvelger/SelectedTemplate";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger/$templateId")({
  component: SelectedTemplate,
});
