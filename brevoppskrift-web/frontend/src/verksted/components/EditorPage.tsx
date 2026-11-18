import { ArrowRedoIcon, ArrowUndoIcon } from "@navikt/aksel-icons";
import {
  Box,
  Button,
  Checkbox,
  HStack,
  InlineMessage,
  LocalAlert,
  Select,
  Tabs,
  Tag,
  ToggleGroup,
  VStack,
} from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useBlocker, useNavigate } from "@tanstack/react-router";
import { useCallback, useMemo, useRef, useState } from "react";

import { ApiProblem, workshopApi, workshopKeys } from "../api";
import { capabilityState } from "../capabilities";
import { openCommentCount } from "../comments";
import { diffDrafts } from "../diff";
import { blockMoveTargets, containsLockedSource, indexFields, metadataLanguages, relocateBlock } from "../draftOps";
import { useDraftSession } from "../draftSession";
import { canRedo, canUndo } from "../history";
import { type CommentAnchor, type Draft, type DraftRecord, type Integrations, type Language } from "../types";
import { hasBlockingIssues, validateDraft } from "../validate";
import { AttachmentsPanel } from "./AttachmentsPanel";
import { BlockList } from "./BlockEditor";
import { CodePanel } from "./CodePanel";
import { DeliveryPanel } from "./DeliveryPanel";
import { EditorProvider } from "./EditorContext";
import { ChoicesPanel, FieldsPanel } from "./FieldsPanel";
import { MetadataPanel } from "./MetadataPanel";
import { PreviewPane } from "./PreviewPane";
import { ProblemMessage } from "./Problems";
import { ReviewPanel } from "./ReviewPanel";
import { ScenariosPanel } from "./ScenariosPanel";
import { TranslationsField } from "./TranslationsField";

function exportJson(draft: Draft, name: string) {
  const url = URL.createObjectURL(new Blob([JSON.stringify(draft, null, 2)], { type: "application/json" }));
  const a = document.createElement("a");
  a.href = url;
  a.download = `${name || "utkast"}-lokale-endringer.json`;
  a.click();
  URL.revokeObjectURL(url);
}

export function EditorPage({
  record,
  canEdit,
  integrations,
}: {
  record: DraftRecord;
  canEdit: boolean;
  integrations: Integrations | undefined;
}) {
  const navigate = useNavigate();
  const client = useQueryClient();
  const { session, draft, dirty, edit, save, undo, redo, adoptRecord } = useDraftSession(record, record.id);
  const archived = Boolean(record.archivedAt);
  const editable = canEdit && !archived;

  const [language, setLanguage] = useState<Language>(draft.metadata.languages[0] ?? "NB");
  const [showAll, setShowAll] = useState(false);
  const [selectedScenarioId, setScenarioId] = useState<string>();
  const sourceProfile = Boolean(draft.source) || draft.outline.some(containsLockedSource);
  // Kildeprofiler har ingen støttet datamodell for scenarier, så ingen scenarioforhåndsvisning later som om.
  const scenarioId = sourceProfile ? undefined : selectedScenarioId;
  const [mainTab, setMainTab] = useState("innhold");
  const [sideTab, setSideTab] = useState("forhandsvisning");
  const [commentAnchor, setCommentAnchor] = useState<CommentAnchor>();
  const [serverRecord, setServerRecord] = useState<DraftRecord>();
  const [confirmAdopt, setConfirmAdopt] = useState(false);
  const [conflictError, setConflictError] = useState<unknown>();

  const pendingKey = useRef<string | undefined>(undefined);
  const update = useCallback(
    (next: Draft) => {
      const key = pendingKey.current;
      pendingKey.current = undefined;
      edit(() => next, key);
    },
    [edit],
  );
  const typing = useCallback((key: string) => {
    pendingKey.current = key;
  }, []);

  const blocker = useBlocker({ shouldBlockFn: () => dirty, enableBeforeUnload: () => dirty, withResolver: true });

  const languages = useMemo(() => metadataLanguages(draft), [draft]);
  const activeLanguage = languages.includes(language) ? language : (languages[0] ?? "NB");
  const fieldIndex = useMemo(() => indexFields(draft.fields), [draft.fields]);
  const issues = useMemo(() => validateDraft(draft), [draft]);
  const diff = useMemo(() => (draft.baseline ? diffDrafts(draft.baseline.draft, draft) : undefined), [draft]);
  const commentCounts = useMemo(() => {
    const counts = new Map<string, number>();
    for (const c of draft.comments ?? []) {
      const id = c.anchor.blockId;
      if (id && !(c.states.at(-1)?.resolved ?? false)) {
        counts.set(id, (counts.get(id) ?? 0) + 1);
      }
    }
    return counts;
  }, [draft.comments]);

  const aiEnabled = integrations ? capabilityState(integrations.ai).enabled : false;
  const blocking = hasBlockingIssues(issues);

  const onComment = useCallback((anchor: CommentAnchor) => {
    setCommentAnchor(anchor);
    setSideTab("gjennomgang");
  }, []);

  const approve = useMutation({
    mutationFn: () => workshopApi.approveBaseline(record.id, session.revision),
    onSuccess: (r) => {
      adoptRecord(r);
      void client.invalidateQueries({ queryKey: workshopKeys.drafts });
    },
  });

  const fetchServer = async () => {
    setConflictError(undefined);
    try {
      setServerRecord(await workshopApi.draft(record.id));
    } catch (error) {
      setConflictError(error);
    }
  };

  const saving = session.save.state === "saving";
  const openComments = openCommentCount(draft.comments);

  return (
    <EditorProvider
      value={{
        draft,
        languages,
        language: activeLanguage,
        showAllLanguages: showAll,
        canEdit: editable,
        canEditStructure: editable && !sourceProfile,
        aiEnabled,
        fieldIndex,
        issues,
        blockChanges: diff?.blocks ?? new Map(),
        onComment,
        commentCounts,
        typing,
        moveTargets: (blockId) => blockMoveTargets(draft.outline, blockId),
        onRelocate: (blockId, option) => {
          const next = relocateBlock(draft.outline, blockId, {
            containerId: option.containerId,
            branch: option.branch,
            index: Number.MAX_SAFE_INTEGER,
          });
          if (next) {
            update({ ...draft, outline: next });
          }
        },
      }}
    >
      <VStack gap="space-16">
        <Box background="default" borderColor="neutral-subtle" borderRadius="4" borderWidth="1" padding="space-12">
          <HStack align="end" gap="space-12" justify="space-between" wrap>
            <HStack align="end" gap="space-12" wrap>
              <ToggleGroup
                label="Språk"
                onChange={(v) => setLanguage(v as Language)}
                size="small"
                value={activeLanguage}
              >
                {languages.map((l) => (
                  <ToggleGroup.Item key={l} value={l}>
                    {l}
                  </ToggleGroup.Item>
                ))}
              </ToggleGroup>
              <Checkbox checked={showAll} onChange={(e) => setShowAll(e.target.checked)} size="small">
                Vis alle språk
              </Checkbox>
              {sourceProfile ? null : (
                <Select
                  label="Scenario"
                  onChange={(e) => setScenarioId(e.target.value || undefined)}
                  size="small"
                  value={scenarioId ?? ""}
                >
                  <option value="">Ingen</option>
                  {(draft.scenarios ?? []).map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.name}
                    </option>
                  ))}
                </Select>
              )}
            </HStack>
            <HStack align="center" gap="space-8" wrap>
              {dirty ? (
                <Tag data-color="warning" size="small" variant="moderate">
                  Ulagrede endringer
                </Tag>
              ) : (
                <Tag data-color="success" size="small" variant="outline">
                  Lagret (rev. {session.revision})
                </Tag>
              )}
              {blocking ? (
                <Tag data-color="danger" size="small" variant="moderate">
                  Feil i brevet
                </Tag>
              ) : null}
              {editable ? (
                <>
                  <Button
                    disabled={!canUndo(session.history)}
                    icon={<ArrowUndoIcon aria-hidden />}
                    onClick={undo}
                    size="small"
                    title="Angre"
                    variant="tertiary"
                  >
                    Angre
                  </Button>
                  <Button
                    disabled={!canRedo(session.history)}
                    icon={<ArrowRedoIcon aria-hidden />}
                    onClick={redo}
                    size="small"
                    title="Gjør om"
                    variant="tertiary"
                  >
                    Gjør om
                  </Button>
                  <Button
                    disabled={!dirty || session.save.state === "conflict"}
                    loading={saving}
                    onClick={() => void save()}
                    size="small"
                  >
                    Lagre
                  </Button>
                </>
              ) : null}
            </HStack>
          </HStack>
        </Box>

        {sourceProfile ? (
          <InlineMessage status="info">
            Importert fra kilde med tekstprofil: bare tekst og oversettelser kan redigeres. Låste kildeblokker, datafelt
            og strukturen bak dem vises som de er og kan ikke endres, kopieres eller slettes her. Scenarier og
            forhåndsvisning med data støttes ikke for denne malen.
          </InlineMessage>
        ) : null}

        {archived ? (
          <LocalAlert status="announcement">
            <LocalAlert.Header>
              <LocalAlert.Title as="h2">Utkastet er arkivert ({record.archiveReason})</LocalAlert.Title>
            </LocalAlert.Header>
            <LocalAlert.Content>Arkiverte utkast kan bare leses.</LocalAlert.Content>
          </LocalAlert>
        ) : !canEdit ? (
          <InlineMessage status="info">Du har kun lesetilgang til malverkstedet.</InlineMessage>
        ) : null}

        {session.save.state === "error" ? (
          <ProblemMessage error={new ApiProblem(session.save)}>Lagring feilet</ProblemMessage>
        ) : null}

        {session.save.state === "conflict" ? (
          <LocalAlert status="warning">
            <LocalAlert.Header>
              <LocalAlert.Title as="h2">Noen andre har lagret nyere versjon</LocalAlert.Title>
            </LocalAlert.Header>
            <LocalAlert.Content>
              <VStack gap="space-8">
                <span>
                  Dine endringer er beholdt og ingenting er overskrevet. Serverens revisjon er{" "}
                  {session.save.currentRevision ?? "nyere enn din"} (din: {session.revision}).
                </span>
                <HStack gap="space-8" wrap>
                  <Button onClick={() => exportJson(draft, draft.metadata.name)} size="small" variant="secondary">
                    Last ned mine endringer (JSON)
                  </Button>
                  <Button onClick={() => void fetchServer()} size="small" variant="secondary">
                    Hent serverens versjon
                  </Button>
                </HStack>
                {conflictError ? (
                  <ProblemMessage error={conflictError}>Kunne ikke hente serverens versjon</ProblemMessage>
                ) : null}
                {serverRecord ? (
                  <VStack gap="space-8">
                    <span>
                      Serverens revisjon {serverRecord.revision}, sist endret av {serverRecord.updatedBy}. Forskjell fra
                      dine endringer: {diffDrafts(serverRecord.draft, draft).items.length} punkter.
                    </span>
                    {confirmAdopt ? (
                      <HStack gap="space-8">
                        <Button
                          data-color="danger"
                          onClick={() => {
                            adoptRecord(serverRecord);
                            setServerRecord(undefined);
                            setConfirmAdopt(false);
                          }}
                          size="small"
                        >
                          Forkast mine endringer og bruk serverens
                        </Button>
                        <Button onClick={() => setConfirmAdopt(false)} size="small" variant="tertiary">
                          Avbryt
                        </Button>
                      </HStack>
                    ) : (
                      <div>
                        <Button onClick={() => setConfirmAdopt(true)} size="small" variant="secondary">
                          Erstatt mine endringer med serverens…
                        </Button>
                      </div>
                    )}
                  </VStack>
                ) : null}
              </VStack>
            </LocalAlert.Content>
          </LocalAlert>
        ) : null}

        {blocker.status === "blocked" ? (
          <LocalAlert status="warning">
            <LocalAlert.Header>
              <LocalAlert.Title as="h2">Du har ulagrede endringer</LocalAlert.Title>
            </LocalAlert.Header>
            <LocalAlert.Content>
              <HStack gap="space-8">
                <Button onClick={() => blocker.reset?.()} size="small">
                  Bli og fortsett
                </Button>
                <Button data-color="danger" onClick={() => blocker.proceed?.()} size="small" variant="secondary">
                  Forlat uten å lagre
                </Button>
              </HStack>
            </LocalAlert.Content>
          </LocalAlert>
        ) : null}

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "minmax(0, 3fr) minmax(0, 2fr)",
            gap: "var(--ax-space-24)",
            alignItems: "start",
          }}
        >
          <Tabs onChange={setMainTab} size="small" value={mainTab}>
            <Tabs.List>
              <Tabs.Tab label="Innhold" value="innhold" />
              <Tabs.Tab label="Datafelt og valg" value="data" />
              <Tabs.Tab label="Scenarier" value="scenarier" />
              <Tabs.Tab label="Metadata" value="metadata" />
            </Tabs.List>
            <Box paddingBlock="space-16">
              {mainTab === "innhold" ? (
                <VStack gap="space-16">
                  <TranslationsField
                    label="Tittel"
                    onChange={(title) => update({ ...draft, title })}
                    value={draft.title}
                  />
                  <BlockList
                    blocks={draft.outline}
                    label="Brevets innhold"
                    onChange={(outline) => update({ ...draft, outline })}
                    scope={[]}
                  />
                </VStack>
              ) : null}
              {mainTab === "data" ? (
                <VStack gap="space-24">
                  <FieldsPanel draft={draft} onChange={update} />
                  <ChoicesPanel draft={draft} onChange={update} />
                </VStack>
              ) : null}
              {mainTab === "scenarier" ? (
                sourceProfile ? (
                  <InlineMessage status="info">
                    Scenarier støttes ikke for importerte kildeprofiler, siden datafeltene ikke kan redigeres her.
                  </InlineMessage>
                ) : (
                  <ScenariosPanel draft={draft} onChange={update} onSelect={setScenarioId} selectedId={scenarioId} />
                )
              ) : null}
              {mainTab === "metadata" ? <MetadataPanel draft={draft} onChange={update} /> : null}
            </Box>
          </Tabs>

          <Tabs onChange={setSideTab} size="small" value={sideTab}>
            <Tabs.List>
              <Tabs.Tab label="Forhåndsvisning" value="forhandsvisning" />
              <Tabs.Tab
                label={openComments > 0 ? `Gjennomgang (${openComments})` : "Gjennomgang"}
                value="gjennomgang"
              />
              <Tabs.Tab label="Vedlegg" value="vedlegg" />
              <Tabs.Tab label="Kode" value="kode" />
              <Tabs.Tab label="Levering" value="levering" />
            </Tabs.List>
            <Box paddingBlock="space-16">
              {sideTab === "forhandsvisning" ? (
                <PreviewPane draft={draft} language={activeLanguage} scenarioId={scenarioId} />
              ) : null}
              {sideTab === "gjennomgang" ? (
                <VStack gap="space-8">
                  {approve.isError ? (
                    <ProblemMessage error={approve.error}>Kunne ikke godkjenne grunnlinje</ProblemMessage>
                  ) : null}
                  <ReviewPanel
                    approveHint={dirty ? "Lagre endringene først." : undefined}
                    approving={approve.isPending}
                    canApproveBaseline={!dirty && editable}
                    diff={diff}
                    draft={draft}
                    draftAnchor={commentAnchor}
                    onApprove={() => approve.mutate()}
                    onChange={update}
                    onClearAnchor={() => setCommentAnchor(undefined)}
                    scenarioId={scenarioId}
                  />
                </VStack>
              ) : null}
              {sideTab === "vedlegg" ? <AttachmentsPanel draft={draft} draftId={record.id} onChange={update} /> : null}
              {sideTab === "kode" ? <CodePanel draft={draft} /> : null}
              {sideTab === "levering" ? (
                <DeliveryPanel
                  blocking={blocking}
                  canEdit={editable}
                  dirty={dirty}
                  draftId={record.id}
                  integrations={integrations}
                  onArchived={() => void navigate({ to: "/arbeidsutkast" })}
                  onRecord={adoptRecord}
                  revision={session.revision}
                  status={session.status}
                />
              ) : null}
            </Box>
          </Tabs>
        </div>
      </VStack>
    </EditorProvider>
  );
}

export { EditorPage as default };
