import { css } from "@emotion/react";
import { Link, useNavigate } from "@tanstack/react-router";
import { type MouseEvent, type ReactNode } from "react";

import {
  type ResultRef,
  type SearchCriteria,
  type TemplatesSearchParams,
  toTemplateSearch,
  toTemplatesSearch,
} from "~/search/searchContext";

function isPlainLeftClick(event: MouseEvent): boolean {
  return event.button === 0 && !(event.metaKey || event.ctrlKey || event.shiftKey || event.altKey);
}

/**
 * A link from the result list to one result, carrying the search with it.
 *
 * `onOpen` runs before a plain click leaves the list, with the search params
 * that lead back to it, so the list can record them in its own history entry.
 * Browser Back then returns to the same search as "Tilbake til
 * søkeresultatene": both come from `criteria`, the search this result was
 * found in, which can be older than the input while a newer search runs. Clicks that open a new tab or window leave the list alone.
 */
export function ResultLink({
  result,
  criteria,
  block,
  current,
  onOpen,
  children,
}: {
  result: ResultRef;
  criteria: SearchCriteria;
  /** The block to open at, for content hits. */
  block?: number;
  /** The result the user last opened. */
  current?: boolean;
  onOpen?: (back: TemplatesSearchParams) => Promise<void>;
  children: ReactNode;
}) {
  const navigate = useNavigate();
  const params = { malType: result.malType, templateId: result.id };
  const search = toTemplateSearch(criteria, result, block);
  return (
    <Link
      aria-current={current ? "true" : undefined}
      onClick={async (event) => {
        if (!onOpen || !isPlainLeftClick(event)) {
          return;
        }
        event.preventDefault();
        await onOpen(toTemplatesSearch(criteria, result));
        await navigate({ to: "/template/$malType/$templateId", params, search });
      }}
      params={params}
      preload="intent"
      search={search}
      to="/template/$malType/$templateId"
    >
      {children}
    </Link>
  );
}

/** Marks the row of the result the user last opened, so it is easy to find
 *  again after coming back to the list. */
export const selectedRowCss = css`
  &[data-selected-result] {
    border-radius: var(--ax-radius-4);
    background: var(--ax-bg-accent-soft);
    outline: 2px solid var(--ax-border-accent);
    outline-offset: var(--ax-space-4);
  }
`;
