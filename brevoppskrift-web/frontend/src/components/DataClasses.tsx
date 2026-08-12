import { ChevronLeftIcon, ChevronRightIcon } from "@navikt/aksel-icons";
import { Box, Button, VStack } from "@navikt/ds-react";
import { Link, useSearch } from "@tanstack/react-router";
import { capitalize } from "lodash";
import {
  type KeyboardEvent as ReactKeyboardEvent,
  type MouseEvent as ReactMouseEvent,
  useEffect,
  useRef,
  useState,
} from "react";

import { type FieldType, type LetterModelSpecification, type ObjectTypeSpecification } from "~/api/brevbakerTypes";

const PANEL_WIDTH_STORAGE_KEY = "brevoppskrift-data-class-panel-width";
const DEFAULT_PANEL_WIDTH = 320;
const MIN_PANEL_WIDTH = 240;
const MAX_PANEL_WIDTH = 720;
const RESIZE_KEYBOARD_STEP = 16;

function clampPanelWidth(width: number): number {
  return Math.min(MAX_PANEL_WIDTH, Math.max(MIN_PANEL_WIDTH, width));
}

function readStoredPanelWidth(): number {
  const stored = Number(globalThis.localStorage.getItem(PANEL_WIDTH_STORAGE_KEY));
  return Number.isFinite(stored) && stored > 0 ? clampPanelWidth(stored) : DEFAULT_PANEL_WIDTH;
}

/**
 * Kollapsbart, drabart sidepanel for data-klassene. Standard bredde er lavere enn tidligere
 * (som brukte 35% av vinduet - veldig bredt på store skjermer), panelet kan dras bredere/smalere
 * via håndtaket på høyre kant, og starter skjult med mindre man allerede har en lenket
 * felt/klasse i URL-en ved lasting (delt lenke) - man ekspanderer selv via knappen, eller
 * automatisk ved å klikke en lenke til et felt i brevoppskriften eller panelet.
 */
export function DataClassesPanel({
  templateModelSpecification,
}: {
  templateModelSpecification: LetterModelSpecification;
}) {
  const { highlightedDataClass, highlightedDataField } = useSearch({ from: "/template/$malType/$templateId" });
  const [open, setOpen] = useState(() => Boolean(highlightedDataClass || highlightedDataField));
  const [width, setWidth] = useState(readStoredPanelWidth);
  const dragState = useRef<{ startX: number; startWidth: number } | null>(null);

  // Ekspander automatisk når en feltlenke klikkes (i brevoppskriften eller i panelet selv),
  // selv om brukeren tidligere har lukket panelet manuelt.
  useEffect(() => {
    if (highlightedDataClass || highlightedDataField) {
      setOpen(true);
    }
  }, [highlightedDataClass, highlightedDataField]);

  useEffect(() => {
    globalThis.localStorage.setItem(PANEL_WIDTH_STORAGE_KEY, String(width));
  }, [width]);

  useEffect(() => {
    function handleMouseMove(event: MouseEvent) {
      if (!dragState.current) {
        return;
      }
      const delta = event.clientX - dragState.current.startX;
      setWidth(clampPanelWidth(dragState.current.startWidth + delta));
    }
    function handleMouseUp() {
      dragState.current = null;
    }
    globalThis.addEventListener("mousemove", handleMouseMove);
    globalThis.addEventListener("mouseup", handleMouseUp);
    return () => {
      globalThis.removeEventListener("mousemove", handleMouseMove);
      globalThis.removeEventListener("mouseup", handleMouseUp);
    };
  }, []);

  if (!open) {
    return (
      <Box asChild marginBlock="space-16 space-0" marginInline="space-0 space-16">
        <Button
          icon={<ChevronRightIcon />}
          iconPosition="right"
          onClick={() => setOpen(true)}
          size="small"
          variant="tertiary"
        >
          Vis datamodell
        </Button>
      </Box>
    );
  }

  return (
    <Box
      background="neutral-soft"
      borderColor="neutral-subtle"
      borderWidth="0 1 0 0"
      css={{ flexShrink: 0, position: "relative", width }}
      minHeight="0"
    >
      <Box asChild marginBlock="space-16 space-0" marginInline="space-16">
        <Button icon={<ChevronLeftIcon />} onClick={() => setOpen(false)} size="small" variant="tertiary">
          Skjul datamodell
        </Button>
      </Box>
      <DataClasses templateModelSpecification={templateModelSpecification} />
      {/* biome-ignore lint/a11y/useSemanticElements: dette er en interaktiv drabar splitter
          (role="separator" + tabIndex/onKeyDown for pil-tast-justering), ikke en semantisk
          <hr>-tematisk-skille - <hr> støtter ikke tastaturinteraksjon på tilsvarende vis. */}
      <div
        aria-label="Endre bredden på datamodell-panelet"
        aria-orientation="vertical"
        aria-valuemax={MAX_PANEL_WIDTH}
        aria-valuemin={MIN_PANEL_WIDTH}
        aria-valuenow={width}
        css={{
          bottom: 0,
          cursor: "col-resize",
          position: "absolute",
          right: 0,
          top: 0,
          transform: "translateX(50%)",
          width: "6px",
          zIndex: 1,
        }}
        onKeyDown={(event: ReactKeyboardEvent) => {
          if (event.key === "ArrowLeft") {
            setWidth((current) => clampPanelWidth(current - RESIZE_KEYBOARD_STEP));
          } else if (event.key === "ArrowRight") {
            setWidth((current) => clampPanelWidth(current + RESIZE_KEYBOARD_STEP));
          }
        }}
        onMouseDown={(event: ReactMouseEvent) => {
          dragState.current = { startX: event.clientX, startWidth: width };
        }}
        role="separator"
        tabIndex={0}
      />
    </Box>
  );
}

export function DataClasses({ templateModelSpecification }: { templateModelSpecification: LetterModelSpecification }) {
  return (
    <VStack css={{ whiteSpace: "nowrap" }} gap="space-16" height="100%" overflow="auto" padding="space-16">
      {Object.entries(templateModelSpecification.types).map(([name, value]) => (
        <DataPresentation key={name} name={name} objectTypeSpecification={value} />
      ))}
    </VStack>
  );
}

function DataPresentation({
  name,
  objectTypeSpecification,
}: {
  name: string;
  objectTypeSpecification: ObjectTypeSpecification;
}) {
  const { highlightedDataClass } = useSearch({ from: "/template/$malType/$templateId" });
  const reference = useRef<HTMLSpanElement>(null);

  const isHighlighted = highlightedDataClass === trimClassName(name);

  useEffect(() => {
    if (isHighlighted && reference.current) {
      reference.current.scrollIntoView({ behavior: "smooth", block: "center" });
    }
  }, [isHighlighted]);

  return (
    <VStack className={isHighlighted ? "highlight" : undefined} gap="space-4" minWidth="100%" width="fit-content">
      <span ref={reference}>
        <span
          css={{
            color: "var(--ax-danger-600)",
          }}
        >
          {" "}
          data class
        </span>{" "}
        {trimClassName(name)}(
      </span>
      {Object.entries(objectTypeSpecification).map(([key, value]) => (
        <DataField fieldType={value} key={key} name={key} ownerClassName={name} />
      ))}
      <span>)</span>
    </VStack>
  );
}

function DataField({
  name,
  fieldType,
  ownerClassName,
}: {
  name: string;
  fieldType: FieldType;
  ownerClassName: string;
}) {
  const { highlightedDataField, highlightedDataFieldOwner } = useSearch({ from: "/template/$malType/$templateId" });
  const reference = useRef<HTMLSpanElement>(null);

  // Uten highlightedDataFieldOwner (slik v1 lenker felt) matches feltnavnet globalt på
  // tvers av alle data-klasser, som tidligere. Når highlightedDataFieldOwner er satt
  // (v2s FieldPath-lenker, som kjenner feltets eierklasse via leafOwnerType), skal
  // treffet begrenses til akkurat den klassen, slik at felt med samme navn i andre
  // klasser ikke highlightes ved en feiltakelse.
  const isHighlighted =
    highlightedDataField === name &&
    (!highlightedDataFieldOwner || highlightedDataFieldOwner === trimClassName(ownerClassName));

  useEffect(() => {
    if (isHighlighted && reference.current) {
      reference.current.scrollIntoView({ behavior: "smooth", block: "center" });
    }
  }, [isHighlighted]);

  return (
    <Box asChild marginInline="space-32 space-0">
      <span className={isHighlighted ? "highlight" : undefined} key={name} ref={reference}>
        {name}: <Type fieldType={fieldType} />
        {fieldType.nullable ? "?" : ""}
      </span>
    </Box>
  );
}

function Type({ fieldType }: { fieldType: FieldType }) {
  switch (fieldType.type) {
    case "scalar": {
      return (
        <span
          css={{
            color: "var(--ax-meta-purple-600)",
          }}
        >
          {capitalize(fieldType.kind)}
        </span>
      );
    }
    case "enum": {
      return (
        <span
          css={{
            color: "var(--ax-success-600)",
          }}
        >
          {fieldType.values.join(" | ")}
        </span>
      );
    }
    case "array": {
      return (
        <span>
          <Type fieldType={fieldType.items} />
          []
        </span>
      );
    }
    case "object": {
      return (
        <Link
          from="/template/$malType/$templateId"
          preload={false}
          replace
          search={(s) => ({ ...s, highlightedDataClass: trimClassName(fieldType.typeName).replace("?", "") })}
        >
          {trimClassName(fieldType.typeName)}
        </Link>
      );
    }
  }
}

export function trimClassName(className: string) {
  return className.replace(/(.*)[$.]/, "");
}
