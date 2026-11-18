import { createFileRoute } from "@tanstack/react-router";

import { requireWorkshopLogin } from "~/verksted/auth";
import { EditorRoutePage } from "~/verksted/components/Pages";

export const Route = createFileRoute("/arbeidsutkast/$draftId")({
  beforeLoad: requireWorkshopLogin,
  component: Editor,
});

function Editor() {
  const { draftId } = Route.useParams();
  return <EditorRoutePage draftId={draftId} />;
}
