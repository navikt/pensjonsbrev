import { CheckmarkIcon, XMarkIcon } from "@navikt/aksel-icons";
import { Button, HStack } from "@navikt/ds-react";

import {
  isTekstValgHighlighted,
  useInsertedTekstValgHighlight,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import { useRedigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { type BlockTypeV2, type BlockV2 } from "~/types/brevV2";
import { trackMissingFromTemplateAction } from "~/utils/editorTracking";

import { keepMissingFromTemplate, removeMissingFromTemplate } from "../commands/missingFromTemplate";
import { countMissingFromTemplate } from "../model/structure";
import { useEditorV2 } from "./EditorContextV2";
import { ListView } from "./ListView";
import { TableView } from "./TableView";
import { TextContainerView } from "./text/TextContainerView";

const BLOCK_CLASS: Record<BlockTypeV2, string> = {
  TITLE2: "TITLE1",
  TITLE3: "TITLE2",
  TITLE4: "TITLE3",
  PARAGRAPH: "PARAGRAPH",
  ITEM_LIST: "PARAGRAPH",
  NUMBERED_LIST: "PARAGRAPH",
  TABLE: "PARAGRAPH",
};

function BlockContent({ block, index }: { block: BlockV2; index: number }) {
  switch (block.type) {
    case "TITLE2":
    case "TITLE3":
    case "TITLE4":
    case "PARAGRAPH":
      return <TextContainerView content={block.content} path={{ kind: "block", block: index }} />;
    case "ITEM_LIST":
    case "NUMBERED_LIST":
      return <ListView block={index} list={block} />;
    case "TABLE":
      return <TableView block={index} table={block} />;
  }
}

function MissingFromTemplateActions({ index }: { index: number }) {
  const { state, dispatch } = useEditorV2();
  const redigeringsflate = useRedigeringsflate();

  const handle = (action: "blokk beholdt" | "blokk slettet") => {
    const { id, brevkode, brevtype, spraak } = state.info;
    trackMissingFromTemplateAction(action, redigeringsflate, {
      brevId: id,
      brevkode,
      brevtype,
      spraak,
      antallGjenstaaende: countMissingFromTemplate(state.redigertBrev) - 1,
      blockIndex: index,
    });
    dispatch(action === "blokk beholdt" ? keepMissingFromTemplate : removeMissingFromTemplate, index);
  };

  return (
    <HStack className="missing-from-template-actions" gap="space-4" justify="end">
      <Button
        icon={<CheckmarkIcon aria-hidden />}
        onClick={() => handle("blokk beholdt")}
        size="xsmall"
        type="button"
        variant="secondary"
      >
        Behold
      </Button>
      <Button
        icon={<XMarkIcon aria-hidden />}
        onClick={() => handle("blokk slettet")}
        size="xsmall"
        type="button"
        variant="secondary"
      >
        Slett
      </Button>
    </HStack>
  );
}

export function BlockView({ block, index }: { block: BlockV2; index: number }) {
  const showMissingFromTemplate = useRedigeringsflate() !== "attestant-redigering";
  const flash = isTekstValgHighlighted(useInsertedTekstValgHighlight(), block);
  const missing = block.missingFromTemplate && showMissingFromTemplate;
  const className = [BLOCK_CLASS[block.type], flash && "inserted-flash-block", missing && "missing-from-template-block"]
    .filter(Boolean)
    .join(" ");

  return (
    <div className={className}>
      {missing && <MissingFromTemplateActions index={index} />}
      <BlockContent block={block} index={index} />
    </div>
  );
}
