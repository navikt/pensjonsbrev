import { Box, Button, HStack, Select, Tag, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { Link } from "@tanstack/react-router";
import { useState } from "react";

import { workshopApi, workshopKeys } from "../api";
import { removeAt, replaceAt } from "../draftOps";
import { type Attachment, type Draft } from "../types";
import { useEditor } from "./EditorContext";
import { ProblemMessage } from "./Problems";

/** Vedlegg er versjonerte kopier av andre arbeidsutkast av type VEDLEGG. Oppdatering er alltid et eksplisitt valg. */
export function AttachmentsPanel({
  draft,
  draftId,
  onChange,
}: {
  draft: Draft;
  draftId: string;
  onChange: (next: Draft) => void;
}) {
  const { canEdit, draft: current } = useEditor();
  const attachments = draft.attachments ?? [];
  const drafts = useQuery({ queryKey: workshopKeys.drafts, queryFn: workshopApi.drafts });
  const candidates = (drafts.data ?? []).filter((d) => d.metadata.type === "VEDLEGG" && d.id !== draftId);
  const [pick, setPick] = useState("");
  const [fieldId, setFieldId] = useState("");
  const [error, setError] = useState<unknown>();
  const [busy, setBusy] = useState(false);

  const fetchRecord = async (id: string) => {
    setBusy(true);
    setError(undefined);
    try {
      return await workshopApi.draft(id);
    } catch (e) {
      setError(e);
      return undefined;
    } finally {
      setBusy(false);
    }
  };

  const add = async () => {
    const record = await fetchRecord(pick);
    if (!record) {
      return;
    }
    const attachment: Attachment = {
      id: record.id,
      revision: record.revision,
      draft: record.draft,
      fieldId: fieldId || null,
    };
    onChange({ ...draft, attachments: [...attachments, attachment] });
    setPick("");
  };

  return (
    <VStack gap="space-12">
      {draft.metadata.type === "VEDLEGG" ? <AttachmentUsageList draftId={draftId} /> : null}
      <span>
        Et vedlegg er en fastlåst kopi av en bestemt revisjon. Endringer i vedleggsutkastet slår ikke inn før du
        oppdaterer.
      </span>
      {drafts.isError ? <ProblemMessage error={drafts.error}>Kunne ikke hente vedlegg</ProblemMessage> : null}
      {attachments.length === 0 ? <span>Ingen vedlegg.</span> : null}
      {attachments.map((attachment, index) => (
        <AttachmentRow
          attachment={attachment}
          canEdit={canEdit}
          key={`${attachment.id}:${index}`}
          onRemove={() => onChange({ ...draft, attachments: removeAt(attachments, index) })}
          onUpdate={(next) => onChange({ ...draft, attachments: replaceAt(attachments, index, next) })}
          setError={setError}
        />
      ))}
      {error ? <ProblemMessage error={error}>Vedlegg</ProblemMessage> : null}
      {canEdit ? (
        <Box background="neutral-soft" borderRadius="4" padding="space-8">
          <VStack gap="space-8">
            <Select label="Legg til vedlegg" onChange={(e) => setPick(e.target.value)} size="small" value={pick}>
              <option value="">Velg vedleggsutkast…</option>
              {candidates.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.metadata.name} (rev. {d.revision})
                </option>
              ))}
            </Select>
            <Select
              description="Valgfritt: datafeltet som styrer om vedlegget tas med."
              label="Knyttet til datafelt"
              onChange={(e) => setFieldId(e.target.value)}
              size="small"
              value={fieldId}
            >
              <option value="">Ingen</option>
              {current.fields.map((f) => (
                <option key={f.id} value={f.id}>
                  {f.name}
                </option>
              ))}
            </Select>
            <div>
              <Button disabled={!pick} loading={busy} onClick={() => void add()} size="small">
                Legg til
              </Button>
            </div>
          </VStack>
        </Box>
      ) : null}
    </VStack>
  );
}

/** Viser hvilke aktive brev som har en kopi av dette vedlegget, så en endring kan vurderes mot dem. Lesbar for alle med tilgang. */
function AttachmentUsageList({ draftId }: { draftId: string }) {
  const usage = useQuery({
    queryKey: workshopKeys.usage(draftId),
    queryFn: () => workshopApi.usage(draftId),
    retry: false,
  });
  return (
    <Box background="neutral-soft" borderRadius="4" padding="space-8">
      <VStack gap="space-4">
        <strong>Brukes i</strong>
        {usage.isPending ? <span>Henter bruk…</span> : null}
        {usage.isError ? <ProblemMessage error={usage.error}>Kunne ikke hente bruk av vedlegget</ProblemMessage> : null}
        {usage.data && usage.data.length === 0 ? <span>Ikke brukt i noen aktive brev.</span> : null}
        {usage.data && usage.data.length > 0 ? (
          <ul>
            {usage.data.map((u) => (
              <li key={u.id}>
                <Link params={{ draftId: u.id }} to="/arbeidsutkast/$draftId">
                  {u.name}
                </Link>{" "}
                (kopi av rev. {u.revision})
              </li>
            ))}
          </ul>
        ) : null}
      </VStack>
    </Box>
  );
}

function AttachmentRow({
  attachment,
  canEdit,
  onRemove,
  onUpdate,
  setError,
}: {
  attachment: Attachment;
  canEdit: boolean;
  onRemove: () => void;
  onUpdate: (next: Attachment) => void;
  setError: (e: unknown) => void;
}) {
  const latest = useQuery({
    queryKey: workshopKeys.draft(attachment.id),
    queryFn: () => workshopApi.draft(attachment.id),
    retry: false,
  });
  const newer = latest.data && latest.data.revision > attachment.revision ? latest.data : undefined;
  return (
    <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" padding="space-8">
      <VStack gap="space-4">
        <HStack gap="space-8" justify="space-between" wrap>
          <strong>{attachment.draft.metadata.name}</strong>
          <Tag data-color="neutral" size="xsmall" variant="outline">
            rev. {attachment.revision}
          </Tag>
        </HStack>
        {newer ? (
          <Tag data-color="info" size="xsmall" variant="moderate">
            Ny revisjon {newer.revision} finnes
          </Tag>
        ) : latest.isError ? (
          <Tag data-color="warning" size="xsmall" variant="moderate">
            Kan ikke sjekke ny versjon
          </Tag>
        ) : null}
        {canEdit ? (
          <HStack gap="space-8">
            {newer ? (
              <Button
                onClick={() => {
                  setError(undefined);
                  onUpdate({ ...attachment, revision: newer.revision, draft: newer.draft });
                }}
                size="xsmall"
                variant="secondary"
              >
                Oppdater til rev. {newer.revision}
              </Button>
            ) : null}
            <Button onClick={onRemove} size="xsmall" variant="tertiary">
              Fjern
            </Button>
          </HStack>
        ) : null}
      </VStack>
    </Box>
  );
}
