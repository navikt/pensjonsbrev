import { Box, Button, HStack, Select, Tag, Textarea, TextField, VStack } from "@navikt/ds-react";
import { useState } from "react";

import { addComment, addReply, isOrphaned, isResolved, setResolved } from "../comments";
import { type DraftDiff, type WordPart } from "../diff";
import { type CommentAnchor, type Draft, type DraftComment } from "../types";
import { useEditor } from "./EditorContext";

const CHANGE_LABEL = { added: "Ny", removed: "Fjernet", changed: "Endret", moved: "Flyttet" } as const;

function Words({ parts }: { parts: WordPart[] }) {
  return (
    <span>
      {parts.map((part, i) =>
        part.type === "same" ? (
          <span key={i}>{part.text}</span>
        ) : part.type === "added" ? (
          <ins key={i}>{part.text}</ins>
        ) : (
          <del key={i}>{part.text}</del>
        ),
      )}
    </span>
  );
}

function anchorLabel(anchor: CommentAnchor, draft: Draft): string {
  if (anchor.blockId) {
    return "Blokk";
  }
  if (anchor.fieldId) {
    return "Datafelt";
  }
  if (anchor.choiceId) {
    const choice = draft.choices.find((c) => c.id === anchor.choiceId);
    return `Valg ${choice?.name ?? ""}`.trim();
  }
  return "Hele brevet";
}

export function ReviewPanel({
  draft,
  diff,
  onChange,
  scenarioId,
  draftAnchor,
  onClearAnchor,
  canApproveBaseline,
  approveHint,
  onApprove,
  approving,
}: {
  draft: Draft;
  diff: DraftDiff | undefined;
  onChange: (next: Draft) => void;
  scenarioId: string | undefined;
  /** Forankring valgt via «Kommenter»-knapp. */
  draftAnchor: CommentAnchor | undefined;
  onClearAnchor: () => void;
  canApproveBaseline: boolean;
  approveHint?: string;
  onApprove: () => void;
  approving: boolean;
}) {
  const { canEdit, language } = useEditor();
  const [text, setText] = useState("");
  const [role, setRole] = useState("");
  const [filter, setFilter] = useState<"open" | "resolved" | "all">("open");
  const [replyText, setReplyText] = useState<Record<string, string>>({});
  const comments = draft.comments ?? [];
  const visible = comments.filter((c) =>
    filter === "all" ? true : filter === "open" ? !isResolved(c) : isResolved(c),
  );
  const anchor: CommentAnchor = draftAnchor ?? { type: "DOCUMENT" };

  return (
    <VStack gap="space-16">
      <VStack gap="space-8">
        <HStack align="center" gap="space-8" justify="space-between">
          <strong>Grunnlinje</strong>
          {draft.baseline ? (
            <Tag data-color="info" size="small" variant="outline">
              {draft.baseline.kind}
              {draft.baseline.approvedBy ? ` · godkjent av ${draft.baseline.approvedBy}` : ""}
            </Tag>
          ) : (
            <Tag data-color="neutral" size="small" variant="outline">
              Ingen grunnlinje
            </Tag>
          )}
        </HStack>
        {canEdit ? (
          <div>
            <Button
              disabled={!canApproveBaseline}
              loading={approving}
              onClick={onApprove}
              size="small"
              variant="secondary"
            >
              Godkjenn versjon som ny grunnlinje
            </Button>
            {!canApproveBaseline && approveHint ? <div>{approveHint}</div> : null}
          </div>
        ) : null}
      </VStack>

      <VStack gap="space-8">
        <strong>Endringer fra grunnlinjen</strong>
        {!diff ? <span>Ingen grunnlinje å sammenligne med.</span> : null}
        {diff && diff.items.length === 0 ? <span>Ingen endringer.</span> : null}
        {diff?.items.map((item) => (
          <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" key={item.key} padding="space-8">
            <VStack gap="space-4">
              <HStack gap="space-8">
                <Tag data-color={item.change === "removed" ? "danger" : "info"} size="xsmall" variant="moderate">
                  {CHANGE_LABEL[item.change]}
                </Tag>
                <Tag data-color="neutral" size="xsmall" variant="outline">
                  {item.area}
                </Tag>
              </HStack>
              <span>{item.label}</span>
              {item.words?.map((w) => (
                <div key={w.language}>
                  <strong>{w.language}: </strong>
                  <Words parts={w.parts} />
                </div>
              ))}
            </VStack>
          </Box>
        ))}
      </VStack>

      <VStack gap="space-8">
        <HStack align="end" gap="space-8" justify="space-between">
          <strong>Kommentarer</strong>
          <Select label="Vis" onChange={(e) => setFilter(e.target.value as typeof filter)} size="small" value={filter}>
            <option value="open">Åpne</option>
            <option value="resolved">Løste</option>
            <option value="all">Alle</option>
          </Select>
        </HStack>
        {visible.length === 0 ? <span>Ingen kommentarer her.</span> : null}
        {visible.map((comment) => (
          <CommentCard
            canEdit={canEdit}
            comment={comment}
            draft={draft}
            key={comment.id}
            onReply={(reply) => {
              onChange({ ...draft, comments: addReply(comments, comment.id, reply) });
              setReplyText((r) => ({ ...r, [comment.id]: "" }));
            }}
            onResolve={(resolved) => onChange({ ...draft, comments: setResolved(comments, comment.id, resolved) })}
            replyText={replyText[comment.id] ?? ""}
            setReplyText={(v) => setReplyText((r) => ({ ...r, [comment.id]: v }))}
          />
        ))}
        {canEdit ? (
          <Box background="neutral-soft" borderRadius="4" padding="space-8">
            <VStack gap="space-8">
              <span>
                Ny kommentar på: <strong>{anchorLabel(anchor, draft)}</strong>
                {draftAnchor ? (
                  <Button onClick={onClearAnchor} size="xsmall" variant="tertiary">
                    Kommenter hele brevet i stedet
                  </Button>
                ) : null}
              </span>
              <Textarea label="Kommentar" onChange={(e) => setText(e.target.value)} size="small" value={text} />
              <TextField
                description="Valgfri etikett, f.eks. jurist. Gir ingen tilgang."
                label="Rolle"
                onChange={(e) => setRole(e.target.value)}
                size="small"
                value={role}
              />
              <div>
                <Button
                  disabled={text.trim() === ""}
                  onClick={() => {
                    onChange({
                      ...draft,
                      comments: addComment(comments, {
                        anchor: { ...anchor, language },
                        text,
                        role,
                        scenarioId,
                      }),
                    });
                    setText("");
                    onClearAnchor();
                  }}
                  size="small"
                >
                  Legg til kommentar
                </Button>
              </div>
              <span>Forfatter og tidspunkt settes av serveren når du lagrer.</span>
            </VStack>
          </Box>
        ) : null}
      </VStack>
    </VStack>
  );
}

function CommentCard({
  comment,
  draft,
  canEdit,
  onReply,
  onResolve,
  replyText,
  setReplyText,
}: {
  comment: DraftComment;
  draft: Draft;
  canEdit: boolean;
  onReply: (text: string) => void;
  onResolve: (resolved: boolean) => void;
  replyText: string;
  setReplyText: (v: string) => void;
}) {
  const resolved = isResolved(comment);
  const orphan = isOrphaned(comment, draft);
  const meta = (c: { author?: string | null; at?: string | null }) =>
    c.author ? `${c.author}${c.at ? ` · ${new Date(c.at).toLocaleString("nb-NO")}` : ""}` : "Ikke lagret ennå";
  return (
    <Box borderColor="neutral-subtle" borderRadius="4" borderWidth="1" padding="space-8">
      <VStack gap="space-4">
        <HStack gap="space-4" wrap>
          <Tag data-color="neutral" size="xsmall" variant="outline">
            {anchorLabel(comment.anchor, draft)}
          </Tag>
          {comment.anchor.language ? (
            <Tag data-color="neutral" size="xsmall" variant="outline">
              {comment.anchor.language}
            </Tag>
          ) : null}
          {comment.role ? (
            <Tag data-color="neutral" size="xsmall" variant="outline">
              {comment.role}
            </Tag>
          ) : null}
          {orphan ? (
            <Tag data-color="warning" size="xsmall" variant="moderate">
              Foreldreløs – elementet er fjernet
            </Tag>
          ) : null}
          <Tag data-color={resolved ? "success" : "warning"} size="xsmall" variant="moderate">
            {resolved ? "Løst" : "Åpen"}
          </Tag>
        </HStack>
        {comment.anchor.quote ? <blockquote>{comment.anchor.quote}</blockquote> : null}
        <span>{comment.text}</span>
        <small>{meta(comment)}</small>
        {comment.replies.map((reply) => (
          <Box background="neutral-soft" borderRadius="4" key={reply.id} padding="space-8">
            <div>{reply.text}</div>
            <small>{meta(reply)}</small>
          </Box>
        ))}
        {comment.states.length > 0 ? (
          <small>
            Historikk: {comment.states.map((s) => `${s.resolved ? "løst" : "gjenåpnet"} (${meta(s)})`).join(", ")}
          </small>
        ) : null}
        {canEdit ? (
          <VStack gap="space-4">
            <TextField label="Svar" onChange={(e) => setReplyText(e.target.value)} size="small" value={replyText} />
            <HStack gap="space-8">
              <Button
                disabled={replyText.trim() === ""}
                onClick={() => onReply(replyText)}
                size="xsmall"
                variant="secondary"
              >
                Send svar
              </Button>
              <Button onClick={() => onResolve(!resolved)} size="xsmall" variant="tertiary">
                {resolved ? "Gjenåpne" : "Marker som løst"}
              </Button>
            </HStack>
          </VStack>
        ) : null}
      </VStack>
    </Box>
  );
}
