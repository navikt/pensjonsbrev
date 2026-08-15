/**
 * The document model for the editor's tab bar. Tabs describe documents; they do not implement them.
 * A tab carries only the lightweight metadata needed to render and select it — the actual document
 * content is fetched on demand by the active tab's renderer.
 */

export type EditorTabType = "brev" | "redigerbartVedlegg" | "alltidValgbartVedlegg" | "p1";

export type EditorTab = {
  /**
   * Stable identity used for selection and for the ?vedlegg= search param. The brev tab uses the
   * brevId as a string; vedlegg tabs use their vedleggId / kode; P1 uses a fixed key.
   */
  id: string;
  label: string;
  type: EditorTabType;
  /**
   * True for read-only tabs (alltidValgbare vedlegg). Drives the lock icon in the tab and the
   * read-only rendering in the document surface.
   */
  locked: boolean;
};

/** The brev tab's stable id. Selecting "no vedlegg" (the brev) is represented by this value. */
export const BREV_TAB_ID = "brev";

export const P1_TAB_ID = "p1";

/**
 * Tab IDs are prefixed by document type so the flat string namespace cannot collide across
 * redigerbare vedlegg ids, alltidValgbare koder, brev, and p1. The prefix is stripped before the id
 * is used in an API call.
 */
export const REDIGERBART_VEDLEGG_PREFIX = "redigerbart:";
export const ALLTID_VALGBART_VEDLEGG_PREFIX = "alltidValgbart:";

export const redigerbartVedleggTabId = (vedleggId: string) => `${REDIGERBART_VEDLEGG_PREFIX}${vedleggId}`;
export const alltidValgbartVedleggTabId = (kode: string) => `${ALLTID_VALGBART_VEDLEGG_PREFIX}${kode}`;

/** Strip the redigerbart-vedlegg prefix, returning the bare vedleggId for API calls. */
export const vedleggIdFromTabId = (tabId: string) =>
  tabId.startsWith(REDIGERBART_VEDLEGG_PREFIX) ? tabId.slice(REDIGERBART_VEDLEGG_PREFIX.length) : tabId;
