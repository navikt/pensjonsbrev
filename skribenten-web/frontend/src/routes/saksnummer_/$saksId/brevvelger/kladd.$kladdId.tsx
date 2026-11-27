import { createFileRoute } from "@tanstack/react-router";
import { z } from "zod";

export const Route = createFileRoute("/saksnummer_/$saksId/brevvelger/kladd/$kladdId")({
  params: { parse: ({ kladdId }) => ({ kladdId: z.coerce.number().parse(kladdId) }) },
});
