import { Button, HStack, InlineMessage, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useNavigate } from "@tanstack/react-router";

import { workshopApi, workshopKeys } from "../api";
import { type DraftSummary, type TemplateSource } from "../types";
import { ProblemMessage } from "./Problems";

export function diagnosticText(diagnostic: unknown): string {
  if (typeof diagnostic === "string") {
    return diagnostic;
  }
  if (diagnostic && typeof diagnostic === "object") {
    const record = diagnostic as Record<string, unknown>;
    if (typeof record.message === "string") {
      return typeof record.code === "string" ? `${record.message} (${record.code})` : record.message;
    }
  }
  return JSON.stringify(diagnostic);
}

/** Aktivt utkast for denne kilden, hvis det finnes. */
export function existingDraftFor(source: TemplateSource, drafts: DraftSummary[] | undefined): DraftSummary | undefined {
  return drafts?.find((d) => d.sourceKey === source.id);
}

/** Starter redigering fra en katalogkilde. Kilder som ikke kan redigeres kan ikke starte, og årsaken vises. */
export function StartFromSource({ source, drafts }: { source: TemplateSource; drafts: DraftSummary[] | undefined }) {
  const navigate = useNavigate();
  const client = useQueryClient();
  const existing = existingDraftFor(source, drafts);
  const create = useMutation({
    mutationFn: () => workshopApi.create({ sourceId: source.id }),
    onSuccess: (record) => {
      void client.invalidateQueries({ queryKey: workshopKeys.drafts });
      void navigate({ to: "/arbeidsutkast/$draftId", params: { draftId: record.id } });
    },
  });

  if (!source.editable) {
    return (
      <VStack gap="space-4">
        <InlineMessage status="warning">
          Denne malen støttes ikke i malverkstedet ennå og kan ikke redigeres.
        </InlineMessage>
        {source.diagnostics.length > 0 ? (
          <ul>
            {source.diagnostics.map((d) => {
              const text = diagnosticText(d);
              return <li key={text}>{text}</li>;
            })}
          </ul>
        ) : null}
      </VStack>
    );
  }
  if (existing) {
    return (
      <HStack align="center" gap="space-8">
        <span>Det finnes allerede et arbeidsutkast.</span>
        <Link params={{ draftId: existing.id }} to="/arbeidsutkast/$draftId">
          Åpne arbeidsutkast
        </Link>
      </HStack>
    );
  }
  return (
    <VStack gap="space-4">
      <div>
        <Button loading={create.isPending} onClick={() => create.mutate()} size="small" variant="secondary">
          Lag arbeidsutkast
        </Button>
      </div>
      {create.isError ? <ProblemMessage error={create.error}>Kunne ikke opprette arbeidsutkast</ProblemMessage> : null}
    </VStack>
  );
}

/** Knapp på malens dokumentasjonsside. Skjules helt når malverkstedet ikke er tilgjengelig eller malen ikke finnes i katalogen. */
export function StartDraftFromTemplate({ templateId }: { templateId: string }) {
  const me = useQuery({ queryKey: workshopKeys.me, queryFn: workshopApi.me, retry: false });
  const catalog = useQuery({
    queryKey: workshopKeys.catalog,
    queryFn: workshopApi.catalog,
    retry: false,
    enabled: me.isSuccess,
  });
  const drafts = useQuery({
    queryKey: workshopKeys.drafts,
    queryFn: workshopApi.drafts,
    retry: false,
    enabled: me.isSuccess,
  });
  const source = catalog.data?.find((s) => s.name === templateId);
  if (!me.data?.canEdit || !source) {
    return null;
  }
  return <StartFromSource drafts={drafts.data} source={source} />;
}
