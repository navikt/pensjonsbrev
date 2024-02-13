import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  component: () => (
    <div>
      <button onClick={() => fetch("/brevbaker")}>Hent litt data</button>
    </div>
  ),
});
