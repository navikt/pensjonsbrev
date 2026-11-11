import { type BrevInfo, type SaksbehandlerValg } from "~/types/brev";
import { type BrevResponseV2, type LetterV2 } from "~/types/brevV2";
import { brevInfo } from "~test/support/brevFixtures";
import { letterV2, literal, paragraph } from "~test/support/editorV2TestUtils";

export const brevInfoV2 = (args: Parameters<typeof brevInfo>[0] = {}): BrevInfo => ({
  ...brevInfo(args),
  markupVersjon: "V2",
});

export const brevResponseV2 = ({
  info = brevInfoV2(),
  redigertBrev = letterV2({ blocks: [paragraph(literal("Innhold"))] }),
  redigertBrevHash = "redigertBrevHash",
  saksbehandlerValg = {},
}: {
  info?: BrevInfo;
  redigertBrev?: LetterV2;
  redigertBrevHash?: string;
  saksbehandlerValg?: SaksbehandlerValg;
} = {}): BrevResponseV2 => ({
  info,
  redigertBrev,
  redigertBrevHash,
  saksbehandlerValg,
  propertyUsage: null,
  valgteVedlegg: null,
});
