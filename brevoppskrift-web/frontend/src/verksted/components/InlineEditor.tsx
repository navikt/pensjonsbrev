import { ScissorsIcon } from "@navikt/aksel-icons";
import { Box, Button, HStack, Select, Tag, ToggleGroup, VStack } from "@navikt/ds-react";
import { useEffect, useState } from "react";

import { type AiKind, type AiRequest, buildAiRequest, isSuggestionStale, validateSuggestion } from "../aiSuggestion";
import { toApiProblem, workshopApi } from "../api";
import { duplicateInline, moveItem, newInline, removeAt, replaceAt, withTranslation } from "../draftOps";
import { type FontType, type Formatter, type Inline } from "../types";
import { useEditor } from "./EditorContext";
import { FieldSelect, PredicateEditor } from "./PredicateEditor";
import { RowActions } from "./RowActions";
import { TranslationsField } from "./TranslationsField";

const INLINE_LABELS: Record<Inline["type"], string> = {
  TEXT: "Tekst",
  FIELD: "Variabel",
  NEW_LINE: "Linjeskift",
  CONDITIONAL: "Vilkår i tekst",
  FREE_TEXT: "Fritekst",
};

const FORMATTERS: [Formatter, string][] = [
  ["DEFAULT", "Standard"],
  ["CURRENCY", "Kroner"],
  ["MONTH_YEAR", "Måned og år"],
];

/** Redigerer en rekke av innholdselementer (tekst, variabler, linjeskift, vilkår, fritekst). */
export function InlineSequenceEditor({
  inlines,
  onChange,
  scope,
  depth = 0,
  label,
  onSplitAfter,
}: {
  inlines: Inline[];
  onChange: (next: Inline[]) => void;
  scope: string[];
  depth?: number;
  label: string;
  /** Når satt vises «Del etter dette» på hvert element unntatt det siste. */
  onSplitAfter?: (index: number) => void;
}) {
  const { languages, canEditStructure: canEdit, draft } = useEditor();
  const allowFreeText = draft.metadata.type !== "AUTOBREV";
  const kinds = (Object.keys(INLINE_LABELS) as Inline["type"][]).filter((k) => allowFreeText || k !== "FREE_TEXT");
  return (
    <VStack aria-label={label} gap="space-8" role="group">
      {inlines.map((inline, index) => (
        <InlineRow
          count={inlines.length}
          depth={depth}
          inline={inline}
          key={index}
          onChange={(next) => onChange(replaceAt(inlines, index, next))}
          onDelete={() => onChange(removeAt(inlines, index))}
          onDuplicate={() => onChange(duplicateInline(inlines, index))}
          onMove={(delta) => onChange(moveItem(inlines, index, delta))}
          onSplit={onSplitAfter && index < inlines.length - 1 ? () => onSplitAfter(index) : undefined}
          position={index}
          scope={scope}
        />
      ))}
      {canEdit ? (
        <HStack gap="space-4" wrap>
          {kinds.map((kind) => (
            <Button
              key={kind}
              onClick={() => onChange([...inlines, newInline(kind, languages)])}
              size="xsmall"
              variant="tertiary"
            >
              + {INLINE_LABELS[kind]}
            </Button>
          ))}
        </HStack>
      ) : null}
    </VStack>
  );
}

function InlineRow({
  inline,
  position,
  count,
  scope,
  depth,
  onChange,
  onDelete,
  onDuplicate,
  onMove,
  onSplit,
}: {
  inline: Inline;
  position: number;
  count: number;
  scope: string[];
  depth: number;
  onChange: (next: Inline) => void;
  onDelete: () => void;
  onDuplicate: () => void;
  onMove: (delta: -1 | 1) => void;
  onSplit?: () => void;
}) {
  const { canEditStructure: canEdit, languages } = useEditor();
  return (
    <Box background="default" borderColor="neutral-subtle" borderRadius="4" borderWidth="1" padding="space-8">
      <VStack gap="space-8">
        <HStack align="center" gap="space-8" justify="space-between">
          <Tag data-color="neutral" size="xsmall" variant="outline">
            {INLINE_LABELS[inline.type]}
          </Tag>
          <RowActions
            count={count}
            extra={
              onSplit ? (
                <Button
                  icon={<ScissorsIcon aria-hidden />}
                  onClick={onSplit}
                  size="xsmall"
                  title="Del avsnittet etter dette elementet"
                  variant="tertiary"
                />
              ) : undefined
            }
            index={position}
            onDelete={onDelete}
            onDuplicate={onDuplicate}
            onMove={onMove}
            what={INLINE_LABELS[inline.type].toLowerCase()}
          />
        </HStack>
        {inline.type === "TEXT" ? (
          <>
            <TranslationsField
              label="Tekst"
              multiline
              onChange={(text) => onChange({ ...inline, text })}
              value={inline.text}
            />
            <HStack align="center" gap="space-8" wrap>
              <ToggleGroup
                label="Tekststil"
                onChange={(value) => canEdit && onChange({ ...inline, fontType: value as FontType })}
                size="small"
                value={inline.fontType}
              >
                <ToggleGroup.Item value="PLAIN">Vanlig</ToggleGroup.Item>
                <ToggleGroup.Item value="BOLD">Fet</ToggleGroup.Item>
                <ToggleGroup.Item value="ITALIC">Kursiv</ToggleGroup.Item>
              </ToggleGroup>
              <AiSuggestions inline={inline} onAccept={onChange} />
            </HStack>
          </>
        ) : null}
        {inline.type === "FIELD" ? (
          <HStack align="end" gap="space-8" wrap>
            <FieldSelect
              label="Felt"
              onChange={(fieldId) => onChange({ ...inline, fieldId })}
              onlyKinds={["STRING", "NUMBER", "DOUBLE", "BOOLEAN", "DATE", "YEAR", "MONTH", "PERCENT", "ENUM"]}
              scope={scope}
              value={inline.fieldId}
            />
            <Select
              disabled={!canEdit}
              label="Formatering"
              onChange={(event) => onChange({ ...inline, formatter: event.target.value as Formatter })}
              size="small"
              value={inline.formatter}
            >
              {FORMATTERS.map(([value, text]) => (
                <option key={value} value={value}>
                  {text}
                </option>
              ))}
            </Select>
            {canEdit ? (
              <Button
                onClick={() =>
                  onChange(
                    inline.fallback
                      ? { ...inline, fallback: null }
                      : { ...inline, fallback: Object.fromEntries(languages.map((l) => [l, ""])) },
                  )
                }
                size="small"
                variant="tertiary"
              >
                {inline.fallback ? "Fjern reservetekst" : "Legg til reservetekst"}
              </Button>
            ) : null}
            {inline.fallback ? (
              <TranslationsField
                label="Reservetekst når feltet mangler"
                onChange={(fallback) => onChange({ ...inline, fallback })}
                value={inline.fallback}
              />
            ) : null}
          </HStack>
        ) : null}
        {inline.type === "FREE_TEXT" ? (
          <TranslationsField
            description="Veiledning til saksbehandler som fyller ut teksten."
            label="Veiledning"
            multiline
            onChange={(prompt) => onChange({ ...inline, prompt })}
            value={inline.prompt}
          />
        ) : null}
        {inline.type === "CONDITIONAL" ? (
          <VStack gap="space-8">
            <PredicateEditor
              onChange={(predicate) => onChange({ ...inline, predicate })}
              predicate={inline.predicate}
              scope={scope}
            />
            <InlineSequenceEditor
              depth={depth + 1}
              inlines={inline.ifTrue}
              label="Hvis vilkåret er sant"
              onChange={(ifTrue) => onChange({ ...inline, ifTrue })}
              scope={scope}
            />
            <InlineSequenceEditor
              depth={depth + 1}
              inlines={inline.ifFalse}
              label="Ellers"
              onChange={(ifFalse) => onChange({ ...inline, ifFalse })}
              scope={scope}
            />
          </VStack>
        ) : null}
      </VStack>
    </Box>
  );
}

type AiState =
  | { status: "idle" }
  | { status: "loading"; request: AiRequest }
  | { status: "ready"; request: AiRequest; suggestion: string }
  | { status: "message"; message: string };

/**
 * Valgfrie KI-forslag. Skjer bare etter eksplisitt knappetrykk, og endrer aldri teksten før brukeren godtar.
 * Å godta er én endring (ett angresteg). Forslaget avvises hvis teksten eller språket er endret i mellomtiden.
 */
function AiSuggestions({
  inline,
  onAccept,
}: {
  inline: Extract<Inline, { type: "TEXT" }>;
  onAccept: (next: Inline) => void;
}) {
  const { aiEnabled, canEdit, language, languages } = useEditor();
  const [state, setState] = useState<AiState>({ status: "idle" });

  // Forslag følger ikke med når språket byttes.
  useEffect(() => {
    setState((current) =>
      current.status === "ready" && current.request.activeLanguage !== language ? { status: "idle" } : current,
    );
  }, [language]);

  if (!aiEnabled || !canEdit) {
    return null;
  }

  const ask = async (kind: AiKind) => {
    const request = buildAiRequest(kind, inline, language, languages);
    if ("error" in request) {
      setState({ status: "message", message: request.error });
      return;
    }
    setState({ status: "loading", request });
    try {
      const call = kind === "nynorsk" ? workshopApi.aiNynorsk : workshopApi.aiKlarsprak;
      const result = await call(request.sourceText, request.sourceLanguage);
      const suggestion = validateSuggestion(request, result.text);
      setState(
        suggestion === undefined
          ? { status: "message", message: "KI-svaret kunne ikke brukes. Prøv igjen eller skriv selv." }
          : { status: "ready", request, suggestion },
      );
    } catch (error) {
      setState({ status: "message", message: `KI-forslag feilet: ${toApiProblem(error).message}` });
    }
  };

  return (
    <VStack gap="space-4">
      <HStack gap="space-4">
        <Button
          disabled={state.status === "loading"}
          loading={state.status === "loading" && state.request.kind === "nynorsk"}
          onClick={() => ask("nynorsk")}
          size="xsmall"
          variant="secondary"
        >
          Foreslå nynorsk
        </Button>
        <Button
          disabled={state.status === "loading"}
          loading={state.status === "loading" && state.request.kind === "klarsprak"}
          onClick={() => ask("klarsprak")}
          size="xsmall"
          variant="secondary"
        >
          Foreslå klarspråk
        </Button>
      </HStack>
      {state.status === "message" ? <span role="status">{state.message}</span> : null}
      {state.status === "ready" ? (
        <Box background="info-soft" borderRadius="4" padding="space-8">
          <VStack gap="space-8">
            <span>
              <strong>Forslag ({state.request.targetLanguage}):</strong> {state.suggestion}
            </span>
            <HStack gap="space-8">
              <Button
                onClick={() => {
                  if (isSuggestionStale(inline, state.request, language)) {
                    setState({
                      status: "message",
                      message: "Teksten er endret siden forespørselen. Forslaget ble forkastet.",
                    });
                    return;
                  }
                  onAccept({
                    ...inline,
                    text: withTranslation(inline.text, state.request.targetLanguage, state.suggestion),
                  });
                  setState({ status: "idle" });
                }}
                size="xsmall"
              >
                Godta forslag
              </Button>
              <Button onClick={() => setState({ status: "idle" })} size="xsmall" variant="tertiary">
                Avvis forslag
              </Button>
            </HStack>
          </VStack>
        </Box>
      ) : null}
    </VStack>
  );
}
