import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  component: () => (
    <div>
      <button
        onClick={() => fetch("/brevbaker/templates/autobrev/UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO/doc/BOKMAL")}
      >
        Hent litt data
      </button>
    </div>
  ),
});
