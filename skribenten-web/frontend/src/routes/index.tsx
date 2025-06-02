import { createFileRoute } from "@tanstack/react-router";

import { setupWindowOnError } from "~/utils/logger";

setupWindowOnError();

export const Route = createFileRoute("/")({
  loader: ({ navigate, preload }) => {
    if (!preload) {
      navigate({ to: "/saksnummer" });
    }
  },
});
