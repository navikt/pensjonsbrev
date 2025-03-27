import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  loader: ({ preload }) => {
    if (!preload) {
      return redirect({ to: "/templates" });
    }
  },
});
