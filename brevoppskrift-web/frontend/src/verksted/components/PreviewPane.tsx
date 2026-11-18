import { Button, HStack, InlineMessage, Loader, VStack } from "@navikt/ds-react";
import { useMemo } from "react";

import { workshopApi } from "../api";
import { type Draft, type Language } from "../types";
import { useServerResult } from "../useServerResult";
import { ProblemMessage } from "./Problems";

/** Forhåndsvisning rendret av serveren. HTML vises i sandkasse uten skript. */
export function PreviewPane({
  draft,
  language,
  scenarioId,
}: {
  draft: Draft;
  language: Language;
  scenarioId: string | undefined;
}) {
  const input = useMemo(() => ({ draft, language, scenarioId }), [draft, language, scenarioId]);
  const result = useServerResult(input, () => workshopApi.preview(draft, language, scenarioId), true, 800);
  return (
    <VStack gap="space-8">
      <HStack align="center" gap="space-8" justify="space-between">
        <strong>
          Forhåndsvisning ({language}
          {scenarioId ? "" : ", uten scenario"})
        </strong>
        <HStack align="center" gap="space-8">
          {result.loading ? <Loader size="xsmall" title="Oppdaterer forhåndsvisning" /> : null}
          <Button onClick={result.refresh} size="xsmall" variant="tertiary">
            Oppdater
          </Button>
        </HStack>
      </HStack>
      {result.error ? <ProblemMessage error={result.error}>Forhåndsvisning feilet</ProblemMessage> : null}
      {result.stale && result.data ? (
        <InlineMessage status="info">Viser forrige versjon mens ny lastes.</InlineMessage>
      ) : null}
      {result.data?.warnings.map((warning) => (
        <InlineMessage key={warning} status="warning">
          {warning}
        </InlineMessage>
      ))}
      {result.data ? (
        <iframe
          sandbox=""
          srcDoc={result.data.html}
          style={{
            width: "100%",
            minHeight: "32rem",
            border: "1px solid var(--ax-border-neutral-subtle)",
            background: "white",
          }}
          title="Forhåndsvisning av brev"
        />
      ) : result.error ? null : (
        <span>Laster forhåndsvisning…</span>
      )}
    </VStack>
  );
}
