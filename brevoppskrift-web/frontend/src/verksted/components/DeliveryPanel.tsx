import { Button, HStack, Link, Select, Tag, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";

import { workshopApi, workshopKeys } from "../api";
import { capabilityState } from "../capabilities";
import { type ArchiveReason, type DraftRecord, type DraftStatus, type Integrations } from "../types";
import { ProblemMessage } from "./Problems";

const CAPABILITIES: [keyof Integrations, string][] = [
  ["validation", "Validering"],
  ["persistence", "Lagring"],
  ["generation", "Kodegenerering"],
  ["ai", "KI-forslag"],
  ["slack", "Slack-varsling"],
  ["pullRequests", "Pull request"],
];

const REASONS: Record<ArchiveReason, string> = {
  DONE: "Ferdig – levert",
  RESET: "Tilbakestilt – start på nytt",
  DISCARDED: "Forkastet",
};

export function IntegrationStatus({ integrations }: { integrations: Integrations | undefined }) {
  return (
    <HStack gap="space-8" wrap>
      {CAPABILITIES.map(([key, label]) => {
        const state = integrations ? capabilityState(integrations[key]) : { enabled: false, label: "Ukjent" };
        return (
          <Tag data-color={state.enabled ? "success" : "neutral"} key={key} size="xsmall" variant="outline">
            {label}: {state.label}
          </Tag>
        );
      })}
    </HStack>
  );
}

/**
 * Levering: status, pull request og arkivering. Alle handlinger gjelder lagret revisjon,
 * så de er låst mens det finnes ulagrede endringer.
 */
export function DeliveryPanel({
  draftId,
  revision,
  status,
  dirty,
  canEdit,
  blocking,
  integrations,
  onRecord,
  onArchived,
}: {
  draftId: string;
  revision: number;
  status: DraftStatus;
  dirty: boolean;
  canEdit: boolean;
  blocking: boolean;
  integrations: Integrations | undefined;
  onRecord: (record: DraftRecord) => void;
  onArchived: () => void;
}) {
  const client = useQueryClient();
  const [reason, setReason] = useState<ArchiveReason>("DONE");
  const [confirmArchive, setConfirmArchive] = useState(false);
  const prEnabled = integrations ? capabilityState(integrations.pullRequests).enabled : false;

  const statusMutation = useMutation({
    mutationFn: (next: DraftStatus) => workshopApi.setStatus(draftId, revision, next),
    onSuccess: (record) => {
      onRecord(record);
      void client.invalidateQueries({ queryKey: workshopKeys.drafts });
    },
  });
  const archive = useMutation({
    mutationFn: () => workshopApi.archive(draftId, revision, reason),
    onSuccess: () => {
      void client.invalidateQueries({ queryKey: workshopKeys.drafts });
      onArchived();
    },
  });
  const pr = useQuery({
    queryKey: workshopKeys.pullRequest(draftId),
    queryFn: () => workshopApi.pullRequest(draftId),
    refetchInterval: (query) => {
      const s = query.state.data?.status;
      return s === "PENDING" || s === "RUNNING" ? 3000 : false;
    },
    retry: false,
  });
  const createPr = useMutation({
    mutationFn: () => workshopApi.createPullRequest(draftId, revision),
    onSuccess: (job) => client.setQueryData(workshopKeys.pullRequest(draftId), job),
  });

  const locked = dirty ? "Lagre endringene først." : undefined;
  const job = pr.data;
  const running = job?.status === "PENDING" || job?.status === "RUNNING";

  return (
    <VStack gap="space-16">
      <IntegrationStatus integrations={integrations} />

      <VStack gap="space-8">
        <HStack align="center" gap="space-8">
          <strong>Status</strong>
          <Tag data-color={status === "READY" ? "success" : "neutral"} size="small" variant="moderate">
            {status === "READY" ? "Klar" : "Under arbeid"}
          </Tag>
        </HStack>
        {canEdit ? (
          <div>
            <Button
              disabled={dirty || (status === "WORK" && blocking)}
              loading={statusMutation.isPending}
              onClick={() => statusMutation.mutate(status === "READY" ? "WORK" : "READY")}
              size="small"
              variant="secondary"
            >
              {status === "READY" ? "Sett tilbake til under arbeid" : "Marker som klar"}
            </Button>
          </div>
        ) : null}
        {locked ? <span>{locked}</span> : null}
        {status === "WORK" && blocking ? <span>Rett feilene i brevet før det kan merkes som klart.</span> : null}
        {statusMutation.isError ? (
          <ProblemMessage error={statusMutation.error}>Kunne ikke endre status</ProblemMessage>
        ) : null}
      </VStack>

      <VStack gap="space-8">
        <strong>Pull request</strong>
        {!prEnabled ? <span>Pull request-opprettelse er ikke aktivert i dette miljøet.</span> : null}
        {status !== "READY" ? <span>Utkastet må lagres og være klart før du kan opprette pull request.</span> : null}
        {canEdit ? (
          <div>
            <Button
              disabled={!prEnabled || status !== "READY" || dirty || running}
              loading={createPr.isPending}
              onClick={() => createPr.mutate()}
              size="small"
            >
              Opprett utkast til pull request
            </Button>
          </div>
        ) : null}
        {job ? (
          <HStack gap="space-8">
            <Tag
              data-color={job.status === "FAILED" ? "danger" : job.status === "SUCCEEDED" ? "success" : "info"}
              size="small"
              variant="moderate"
            >
              {job.status}
            </Tag>
            {job.prUrl ? (
              <Link href={job.prUrl} rel="noreferrer" target="_blank">
                Åpne pull request
              </Link>
            ) : null}
            {job.error ? <span>{job.error}</span> : null}
          </HStack>
        ) : null}
        {createPr.isError ? (
          <ProblemMessage error={createPr.error}>Kunne ikke opprette pull request</ProblemMessage>
        ) : null}
        {pr.isError ? <ProblemMessage error={pr.error}>Kunne ikke hente pull request-status</ProblemMessage> : null}
      </VStack>

      {canEdit ? (
        <VStack gap="space-8">
          <strong>Arkiver</strong>
          <Select
            label="Årsak"
            onChange={(e) => setReason(e.target.value as ArchiveReason)}
            size="small"
            value={reason}
          >
            {(Object.keys(REASONS) as ArchiveReason[]).map((r) => (
              <option key={r} value={r}>
                {REASONS[r]}
              </option>
            ))}
          </Select>
          {confirmArchive ? (
            <HStack gap="space-8">
              <Button data-color="danger" loading={archive.isPending} onClick={() => archive.mutate()} size="small">
                Bekreft arkivering
              </Button>
              <Button onClick={() => setConfirmArchive(false)} size="small" variant="tertiary">
                Avbryt
              </Button>
            </HStack>
          ) : (
            <div>
              <Button disabled={dirty} onClick={() => setConfirmArchive(true)} size="small" variant="secondary">
                Arkiver utkastet
              </Button>
            </div>
          )}
          {archive.isError ? <ProblemMessage error={archive.error}>Kunne ikke arkivere</ProblemMessage> : null}
        </VStack>
      ) : null}
    </VStack>
  );
}
