import { Button, HStack, LocalAlert } from "@navikt/ds-react";
import { type ReactNode } from "react";

import { ApiProblem } from "../api";
import { workshopLoginUrl } from "../auth";

/** Gjenopprettbar tilstand når API-et ikke er konfigurert eller nede. Dokumentasjonssidene virker uavhengig av dette. */
export function WorkshopUnavailable({ error, onRetry }: { error: unknown; onRetry?: () => void }) {
  const problem = error instanceof ApiProblem ? error : undefined;
  const forbidden = problem?.status === 401 || problem?.status === 403;
  return (
    <LocalAlert status={forbidden ? "error" : "warning"}>
      <LocalAlert.Header>
        <LocalAlert.Title as="h2">
          {forbidden ? "Ingen tilgang til malverkstedet" : "Malverkstedet er ikke tilgjengelig"}
        </LocalAlert.Title>
      </LocalAlert.Header>
      <LocalAlert.Content>
        <HStack align="center" gap="space-16">
          <span>
            {forbidden
              ? (problem?.message ?? "Du har ikke tilgang.")
              : "Tjenesten for arbeidsutkast svarer ikke eller er ikke satt opp i dette miljøet. Lesing av brevoppskrifter, søk og Word-eksport fungerer som vanlig."}
          </span>
          {problem?.status === 401 ? (
            <a href={workshopLoginUrl()} rel="noopener noreferrer" target="_blank">
              Logg inn på nytt (åpnes i ny fane)
            </a>
          ) : null}
          {onRetry ? (
            <Button onClick={onRetry} size="small" variant="secondary">
              Prøv igjen
            </Button>
          ) : null}
        </HStack>
      </LocalAlert.Content>
    </LocalAlert>
  );
}

export function ProblemMessage({ error, children }: { error: unknown; children?: ReactNode }) {
  const problem = error instanceof ApiProblem ? error : undefined;
  const details = Array.isArray(problem?.errors) ? (problem.errors as unknown[]) : [];
  return (
    <LocalAlert status="error">
      <LocalAlert.Header>
        <LocalAlert.Title as="h3">{children ?? "Noe gikk galt"}</LocalAlert.Title>
      </LocalAlert.Header>
      <LocalAlert.Content>
        {problem?.message ?? (error instanceof Error ? error.message : "Ukjent feil")}
        {problem?.code ? ` (${problem.code})` : ""}
        {problem?.status === 401 ? (
          <p>
            <a href={workshopLoginUrl()} rel="noopener noreferrer" target="_blank">
              Logg inn på nytt (åpnes i ny fane)
            </a>
            . Lokale endringer beholdes i denne fanen. Prøv handlingen igjen etter innlogging.
          </p>
        ) : null}
        {details.length > 0 ? (
          <ul>
            {details.slice(0, 10).map((detail) => {
              const text = typeof detail === "string" ? detail : JSON.stringify(detail);
              return <li key={text}>{text}</li>;
            })}
          </ul>
        ) : null}
      </LocalAlert.Content>
    </LocalAlert>
  );
}
