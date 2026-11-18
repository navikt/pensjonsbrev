import { createFileRoute } from "@tanstack/react-router";

import { requireWorkshopLogin } from "~/verksted/auth";
import { CreateDraftPage } from "~/verksted/components/Pages";

export const Route = createFileRoute("/opprett")({
  beforeLoad: requireWorkshopLogin,
  component: CreateDraftPage,
});
