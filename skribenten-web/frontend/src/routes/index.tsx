import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  loader: ({ navigate, preload }) => {
    if (!preload) {
      navigate({ to: "/saksnummer" });
    }
  },
});
