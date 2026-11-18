import { ChatAddIcon, ScissorsIcon } from "@navikt/aksel-icons";
import { Box, Button, HStack, Select, Switch, Tag, VStack } from "@navikt/ds-react";

import {
  addTableColumn,
  addTableRow,
  type CreatableBlockType,
  canMergeWithNext,
  containsLockedSource,
  duplicateBlockAt,
  mergeWithNext,
  moveItem,
  newBlock,
  newTextInline,
  removeAt,
  removeBlockAt,
  removeTableColumn,
  replaceAt,
  splitBlock,
} from "../draftOps";
import { type Block, type BlockType, type Inline } from "../types";
import { useEditor } from "./EditorContext";
import { InlineSequenceEditor } from "./InlineEditor";
import { FieldSelect, PredicateEditor } from "./PredicateEditor";
import { RowActions } from "./RowActions";
import { TranslationsField } from "./TranslationsField";

const BLOCK_LABELS: Record<BlockType, string> = {
  LOCKED_SOURCE: "Låst kildeblokk",
  HEADING: "Overskrift",
  PARAGRAPH: "Avsnitt",
  ITEM_LIST: "Liste",
  TABLE: "Tabell",
  CONDITIONAL: "Vilkår",
  REPEAT: "Gjentakelse",
};

const CHANGE_LABELS = { added: "Ny", changed: "Endret", moved: "Flyttet" } as const;

/** En liste av blokker. Strukturelt ulovlige nestinger kan ikke lages: blokker kan bare settes inn i lister der kontrakten tillater dem. */
export function BlockList({
  blocks,
  onChange,
  scope,
  label,
}: {
  blocks: Block[];
  onChange: (next: Block[]) => void;
  scope: string[];
  label: string;
}) {
  const { canEditStructure: canEdit, languages } = useEditor();
  return (
    <VStack aria-label={label} gap="space-12" role="group">
      {blocks.map((block, index) => (
        <BlockCard
          block={block}
          count={blocks.length}
          index={index}
          key={block.id}
          mergeable={canMergeWithNext(blocks, index)}
          onChange={(next) => onChange(replaceAt(blocks, index, next))}
          onDelete={() => onChange(removeBlockAt(blocks, index))}
          onDuplicate={() => onChange(duplicateBlockAt(blocks, index))}
          onMerge={() => onChange(mergeWithNext(blocks, index) ?? blocks)}
          onMove={(delta) => onChange(moveItem(blocks, index, delta))}
          onSplit={(parts) => onChange([...blocks.slice(0, index), ...parts, ...blocks.slice(index + 1)])}
          scope={scope}
        />
      ))}
      {canEdit ? (
        <HStack gap="space-4" wrap>
          {(Object.keys(BLOCK_LABELS).filter((t) => t !== "LOCKED_SOURCE") as CreatableBlockType[]).map((type) => (
            <Button
              key={type}
              onClick={() => onChange([...blocks, newBlock(type, languages)])}
              size="small"
              variant="secondary"
            >
              + {BLOCK_LABELS[type]}
            </Button>
          ))}
        </HStack>
      ) : null}
    </VStack>
  );
}

function BlockCard({
  block,
  index,
  count,
  scope,
  mergeable,
  onChange,
  onDelete,
  onDuplicate,
  onMerge,
  onMove,
  onSplit,
}: {
  block: Block;
  index: number;
  count: number;
  scope: string[];
  mergeable: boolean;
  onChange: (next: Block) => void;
  onDelete: () => void;
  onDuplicate: () => void;
  onMerge: () => void;
  onMove: (delta: -1 | 1) => void;
  onSplit: (parts: [Block, Block]) => void;
}) {
  const { blockChanges, issues, commentCounts, onComment, canEditStructure: canEdit, languages } = useEditor();
  const change = blockChanges.get(block.id);
  const blockIssues = issues.filter((i) => i.blockId === block.id && i.language === undefined);
  const comments = commentCounts.get(block.id) ?? 0;
  const lockedInside = containsLockedSource(block);
  const split = (boundary: number) => {
    const parts = splitBlock(block, boundary);
    if (parts) {
      onSplit(parts);
    }
  };
  if (block.type === "LOCKED_SOURCE") {
    // Låste kildeblokker kan ikke redigeres, kopieres eller slettes herfra; de beholdes uendret ved lagring.
    return (
      <Box
        aria-label={`${BLOCK_LABELS[block.type]} ${index + 1}`}
        background="neutral-soft"
        borderColor="neutral-subtle"
        borderRadius="8"
        borderWidth="1"
        padding="space-12"
        role="group"
      >
        <HStack align="center" gap="space-8" justify="space-between" wrap>
          <HStack align="center" gap="space-8">
            <Tag data-color="neutral" size="small" variant="moderate">
              {BLOCK_LABELS[block.type]}
            </Tag>
            <span>Innholdet kommer fra kilden ({block.bindingId}) og kan ikke redigeres her.</span>
          </HStack>
          <Button
            icon={<ChatAddIcon aria-hidden />}
            onClick={() => onComment({ type: "BLOCK", blockId: block.id })}
            size="xsmall"
            title="Kommenter låst kildeblokk"
            variant="tertiary"
          />
        </HStack>
      </Box>
    );
  }
  return (
    <Box
      aria-label={`${BLOCK_LABELS[block.type]} ${index + 1}`}
      background="default"
      borderColor={change ? "info" : "neutral-subtle"}
      borderRadius="8"
      borderWidth="1"
      padding="space-12"
      role="group"
    >
      <VStack gap="space-8">
        <HStack align="center" gap="space-8" justify="space-between" wrap>
          <HStack align="center" gap="space-8">
            <Tag data-color="neutral" size="small" variant="moderate">
              {BLOCK_LABELS[block.type]}
            </Tag>
            {change ? (
              <Tag data-color="info" size="small" variant="outline">
                {CHANGE_LABELS[change]}
              </Tag>
            ) : null}
            {comments > 0 ? (
              <Tag data-color="warning" size="small" variant="outline">
                {comments} kommentar{comments === 1 ? "" : "er"}
              </Tag>
            ) : null}
          </HStack>
          <HStack gap="space-4">
            <Button
              icon={<ChatAddIcon aria-hidden />}
              onClick={() => onComment({ type: "BLOCK", blockId: block.id })}
              size="xsmall"
              title={`Kommenter ${BLOCK_LABELS[block.type].toLowerCase()}`}
              variant="tertiary"
            />
            <RowActions
              canDelete={!lockedInside}
              count={count}
              extra={
                canEdit ? (
                  <>
                    {mergeable ? (
                      <Button onClick={onMerge} size="xsmall" variant="tertiary">
                        Slå sammen med neste
                      </Button>
                    ) : null}
                    <MoveToSelect blockId={block.id} label={BLOCK_LABELS[block.type]} />
                  </>
                ) : undefined
              }
              index={index}
              onDelete={onDelete}
              onDuplicate={lockedInside ? undefined : onDuplicate}
              onMove={onMove}
              what={BLOCK_LABELS[block.type].toLowerCase()}
            />
          </HStack>
        </HStack>

        {blockIssues.map((issue) => (
          <span key={issue.key} role="alert">
            <Tag data-color={issue.severity === "error" ? "danger" : "warning"} size="xsmall" variant="moderate">
              {issue.message}
            </Tag>
          </span>
        ))}

        {block.type === "HEADING" ? (
          <>
            <Select
              disabled={!canEdit}
              label="Nivå"
              onChange={(event) => onChange({ ...block, level: Number(event.target.value) })}
              size="small"
              value={String(block.level)}
            >
              <option value="1">Overskrift 1</option>
              <option value="2">Overskrift 2</option>
              <option value="3">Overskrift 3</option>
            </Select>
            <InlineSequenceEditor
              inlines={block.text}
              label="Overskriftstekst"
              onChange={(text) => onChange({ ...block, text })}
              onSplitAfter={canEdit ? (i) => split(i + 1) : undefined}
              scope={scope}
            />
          </>
        ) : null}
        {block.type === "PARAGRAPH" ? (
          <InlineSequenceEditor
            inlines={block.text}
            label="Avsnittstekst"
            onChange={(text) => onChange({ ...block, text })}
            onSplitAfter={canEdit ? (i) => split(i + 1) : undefined}
            scope={scope}
          />
        ) : null}
        {block.type === "ITEM_LIST" ? (
          <VStack gap="space-8">
            <Switch
              checked={block.numbered}
              disabled={!canEdit}
              onChange={(event) => onChange({ ...block, numbered: event.target.checked })}
              size="small"
            >
              Nummerert liste
            </Switch>
            {block.items.map((item: Inline[], itemIndex) => (
              <Box borderRadius="4" key={`${block.id}:${itemIndex}`} padding="space-4">
                <VStack gap="space-4">
                  <HStack align="center" gap="space-8" justify="space-between">
                    <span>Punkt {itemIndex + 1}</span>
                    <RowActions
                      count={block.items.length}
                      extra={
                        canEdit && itemIndex < block.items.length - 1 ? (
                          <Button
                            icon={<ScissorsIcon aria-hidden />}
                            onClick={() => split(itemIndex + 1)}
                            size="xsmall"
                            title="Del listen etter dette punktet"
                            variant="tertiary"
                          />
                        ) : undefined
                      }
                      index={itemIndex}
                      onDelete={() =>
                        onChange({
                          ...block,
                          items: block.items.length > 1 ? removeAt(block.items, itemIndex) : block.items,
                        })
                      }
                      onMove={(delta) => onChange({ ...block, items: moveItem(block.items, itemIndex, delta) })}
                      what="punkt"
                    />
                  </HStack>
                  <InlineSequenceEditor
                    inlines={item}
                    label={`Listepunkt ${itemIndex + 1}`}
                    onChange={(next) => onChange({ ...block, items: replaceAt(block.items, itemIndex, next) })}
                    scope={scope}
                  />
                </VStack>
              </Box>
            ))}
            {canEdit ? (
              <div>
                <Button
                  onClick={() => onChange({ ...block, items: [...block.items, [newTextInline(languages)]] })}
                  size="xsmall"
                  variant="secondary"
                >
                  Legg til punkt
                </Button>
              </div>
            ) : null}
          </VStack>
        ) : null}
        {block.type === "TABLE" ? <TableEditor block={block} onChange={onChange} scope={scope} /> : null}
        {block.type === "CONDITIONAL" ? (
          <VStack gap="space-8">
            <PredicateEditor
              onChange={(predicate) => onChange({ ...block, predicate })}
              predicate={block.predicate}
              scope={scope}
            />
            <Box borderColor="success" borderRadius="4" borderWidth="0 0 0 3" padding="space-8">
              <VStack gap="space-8">
                <strong>Hvis vilkåret er sant</strong>
                <BlockList
                  blocks={block.ifTrue}
                  label="Blokker hvis sant"
                  onChange={(ifTrue) => onChange({ ...block, ifTrue })}
                  scope={scope}
                />
              </VStack>
            </Box>
            <Box borderColor="warning" borderRadius="4" borderWidth="0 0 0 3" padding="space-8">
              <VStack gap="space-8">
                <strong>Ellers</strong>
                <BlockList
                  blocks={block.ifFalse}
                  label="Blokker ellers"
                  onChange={(ifFalse) => onChange({ ...block, ifFalse })}
                  scope={scope}
                />
              </VStack>
            </Box>
          </VStack>
        ) : null}
        {block.type === "REPEAT" ? (
          <VStack gap="space-8">
            <FieldSelect
              label="Gjenta for hvert element i listen"
              onChange={(fieldId) => onChange({ ...block, fieldId })}
              onlyKinds={["LIST"]}
              scope={scope}
              value={block.fieldId}
            />
            <Box borderColor="info" borderRadius="4" borderWidth="0 0 0 3" padding="space-8">
              <BlockList
                blocks={block.body}
                label="Innhold i gjentakelsen"
                onChange={(body) => onChange({ ...block, body })}
                scope={block.fieldId ? [...scope, block.fieldId] : scope}
              />
            </Box>
          </VStack>
        ) : null}
      </VStack>
    </Box>
  );
}

/** Flytter en blokk til en annen liste (inn i/ut av vilkår og gjentakelser). */
function MoveToSelect({ blockId, label }: { blockId: string; label: string }) {
  const { moveTargets, onRelocate } = useEditor();
  const options = moveTargets(blockId);
  return (
    <Select
      hideLabel
      label={`Flytt ${label.toLowerCase()} til`}
      onChange={(event) => {
        const option = options.find((o) => o.key === event.target.value);
        if (option) {
          onRelocate(blockId, option);
        }
      }}
      size="small"
      value=""
    >
      <option value="">Flytt til…</option>
      {options.map((o) => (
        <option key={o.key} value={o.key}>
          {o.label}
        </option>
      ))}
    </Select>
  );
}

/** Tabellredigering: kolonner med oversatte overskrifter og rektangulære rader. */
function TableEditor({
  block,
  onChange,
  scope,
}: {
  block: Extract<Block, { type: "TABLE" }>;
  onChange: (next: Block) => void;
  scope: string[];
}) {
  const { canEditStructure: canEdit, languages } = useEditor();
  return (
    <VStack gap="space-8">
      <strong>Kolonner</strong>
      <HStack gap="space-8" wrap>
        {block.header.map((header, column) => (
          <Box
            borderColor="neutral-subtle"
            borderRadius="4"
            borderWidth="1"
            key={`${block.id}:h${column}`}
            padding="space-8"
          >
            <VStack gap="space-4">
              <TranslationsField
                label={`Kolonne ${column + 1}`}
                onChange={(next) => onChange({ ...block, header: replaceAt(block.header, column, next) })}
                value={header}
              />
              {canEdit && block.header.length > 1 ? (
                <Button
                  data-color="danger"
                  onClick={() => onChange(removeTableColumn(block, column))}
                  size="xsmall"
                  variant="tertiary"
                >
                  Fjern kolonne {column + 1}
                </Button>
              ) : null}
            </VStack>
          </Box>
        ))}
      </HStack>
      {block.rows.map((row, rowIndex) => (
        <Box
          borderColor="neutral-subtle"
          borderRadius="4"
          borderWidth="1"
          key={`${block.id}:r${rowIndex}`}
          padding="space-8"
        >
          <VStack gap="space-8">
            <HStack align="center" justify="space-between">
              <strong>Rad {rowIndex + 1}</strong>
              <RowActions
                count={block.rows.length}
                index={rowIndex}
                onDelete={() =>
                  onChange({ ...block, rows: block.rows.length > 1 ? removeAt(block.rows, rowIndex) : block.rows })
                }
                onMove={(delta) => onChange({ ...block, rows: moveItem(block.rows, rowIndex, delta) })}
                what="rad"
              />
            </HStack>
            {row.map((cell, column) => (
              <InlineSequenceEditor
                inlines={cell}
                key={`${block.id}:r${rowIndex}c${column}`}
                label={`Rad ${rowIndex + 1}, kolonne ${column + 1}`}
                onChange={(next) =>
                  onChange({
                    ...block,
                    rows: replaceAt(block.rows, rowIndex, replaceAt(row, column, next)),
                  })
                }
                scope={scope}
              />
            ))}
          </VStack>
        </Box>
      ))}
      {canEdit ? (
        <HStack gap="space-8">
          <Button onClick={() => onChange(addTableRow(block, languages))} size="xsmall" variant="secondary">
            Legg til rad
          </Button>
          <Button onClick={() => onChange(addTableColumn(block, languages))} size="xsmall" variant="secondary">
            Legg til kolonne
          </Button>
        </HStack>
      ) : null}
    </VStack>
  );
}
