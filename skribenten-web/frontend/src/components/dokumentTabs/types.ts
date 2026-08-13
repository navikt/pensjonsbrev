/**
 * The document model for the editor's tab bar. Tabs describe documents; they do not implement them.
 * A tab carries only the lightweight metadata needed to render and select it — the actual document
 * content is fetched on demand by the active tab's renderer.
 */

export type DokumentType = "brev" | "redigerbartVedlegg" | "alltidValgbartVedlegg" | "p1";

export type DokumentTab = {
  /**
   * Stable identity used for selection and for the ?vedlegg= search param. The brev tab uses the
   * brevId as a string; vedlegg tabs use their vedleggId / kode; P1 uses a fixed key.
   */
  id: string;
  label: string;
  type: DokumentType;
  /**
   * True for read-only tabs (alltidValgbare vedlegg). Drives the lock icon in the tab and the
   * read-only rendering in the document surface.
   */
  locked: boolean;
};

/** The brev tab's stable id. Selecting "no vedlegg" (the brev) is represented by this value. */
export const BREV_TAB_ID = "brev";

export const P1_TAB_ID = "p1";
