import { createFileRoute } from "@tanstack/react-router";

import { requireWorkshopLogin } from "~/verksted/auth";
import { DraftListPage } from "~/verksted/components/Pages";

export const Route = createFileRoute("/arbeidsutkast/")({
  beforeLoad: requireWorkshopLogin,
  component: DraftListPage,
});
