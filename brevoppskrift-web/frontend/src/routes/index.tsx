import { createFileRoute } from "@tanstack/react-router";

const templateId = "UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO";

export const Route = createFileRoute("/")({
  loader: ({ navigate, preload }) => {
    if (!preload) {
      navigate({ to: `/template/${templateId}` });
    }
  },
});
