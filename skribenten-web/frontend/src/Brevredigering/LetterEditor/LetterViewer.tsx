import "./editor.css";

import { Box, Heading, VStack } from "@navikt/ds-react";

import { type DiffResponse } from "~/api/brev-queries";
import { type SpraakKode } from "~/types/apiTypes";
import { type EditedLetter, TITLE_INDEX } from "~/types/brevbakerTypes";

import { ContentGroup } from "./components/ContentGroup";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { EditorStateContext } from "./LetterEditor";
import { type LetterEditorState } from "./model/state";

/**
 * Read-only letter viewer. Renders an EditedLetter with the same structure and styling as
 * LetterEditor, but without any editing functionality.
 *
 * When `diff` is provided and `showDiff` is true, deleted segments (relative to the original
 * rendered letter) are highlighted with strikethrough in red — the complement of what LetterEditor
 * shows (inserted segments highlighted in green).
 *
 * Intended use: pass `diff.rendretBrev` as `letter` so that index references in `diff.deletes`
 * align correctly with the content being rendered.
 */
export const LetterViewer = ({
  letter,
  spraak,
  diff,
  showDiff = false,
}: {
  letter: EditedLetter;
  spraak: SpraakKode;
  diff?: DiffResponse;
  showDiff?: boolean;
}) => {
  const stubState: LetterEditorState = {
    redigertBrev: letter,
    // info is only used for spraak in SakspartView; stub the rest
    info: { spraak } as LetterEditorState["info"],
    redigertBrevHash: "",
    saveStatus: "SAVED",
    focus: { blockIndex: 0, contentIndex: 0 },
    history: { entries: [], entryPointer: -1 },
  };

  return (
    <EditorStateContext.Provider
      value={{
        freeze: true,
        error: false,
        editorState: stubState,
        setEditorState: () => {},
        undo: () => {},
        redo: () => {},
        diff,
        showDiff,
        setShowDiff: () => {},
        diffMode: "deletes",
      }}
    >
      <VStack align="center">
        <Box className="editor" height="100%">
          <SakspartView sakspart={letter.sakspart} spraak={spraak} />
          <Heading className="letter-title" level="1" size="medium">
            <ContentGroup literalIndex={{ blockIndex: TITLE_INDEX, contentIndex: 0 }} />
          </Heading>
          <div className="editor-surface">
            {letter.blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView signatur={letter.signatur} />
        </Box>
      </VStack>
    </EditorStateContext.Provider>
  );
};

