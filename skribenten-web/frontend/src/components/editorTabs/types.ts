/**
 * The document model for the editor's tab bar. Tabs describe documents; they do not implement them.
 * A tab carries only the lightweight metadata needed to render and select it — the actual document
 * content is fetched on demand by the active tab's renderer.
 *
 * Scope: only the brev itself and its redigerbare vedlegg are tabs. Non-redigerbare vedlegg and P1
 * stay in their existing locations and are not part of the tab model.
 */

export type EditorTabType = "brev" | "redigerbartVedlegg";

export type EditorTab = {
  /**
   * Stable identity used for selection and for the ?vedlegg= search param. The brev tab uses a
   * fixed id; a redigerbart vedlegg tab uses its prefixed vedleggId.
   */
  id: string;
  label: string;
  type: EditorTabType;
};

/** The brev tab's stable id. Selecting "no vedlegg" (the brev) is represented by this value. */
export const BREV_TAB_ID = "brev";

/**
 * Redigerbart-vedlegg tab ids are prefixed so they cannot collide with the brev tab id. The prefix
 * is stripped before the id is used in an API call.
 */
export const REDIGERBART_VEDLEGG_PREFIX = "redigerbart:";

export const redigerbartVedleggTabId = (vedleggId: string) => `${REDIGERBART_VEDLEGG_PREFIX}${vedleggId}`;

/** Strip the redigerbart-vedlegg prefix, returning the bare vedleggId for API calls. */
export const vedleggIdFromTabId = (tabId: string) =>
  tabId.startsWith(REDIGERBART_VEDLEGG_PREFIX) ? tabId.slice(REDIGERBART_VEDLEGG_PREFIX.length) : tabId;
