import { css } from "@emotion/react";
import { CheckmarkIcon } from "@navikt/aksel-icons";
import { Detail, HStack, Loader } from "@navikt/ds-react";
import { type ReactNode } from "react";

/** The status sits directly above the results and swaps between a one-line
 *  "what we are searching" message and the hit summary. Reserving the row's
 *  height stops the results from jumping on every keystroke, since `isWorking`
 *  toggles twice per search. 1.5rem clears both the small Loader (1.25rem) and
 *  Detail's line box. */
const statusRow = css`
  min-height: 1.5rem;
`;
/** `text-success` rather than the default text colour so a finished search
 *  reads at a glance without relying on the icon shape alone. */
const doneIcon = css`
  color: var(--ax-text-success);
`;

export function SearchStatus({
  children,
  isWorking,
  isDone,
}: {
  children: ReactNode;
  /** A search or the initial indexing is in flight: show the spinner. */
  isWorking: boolean;
  /** A search has completed and the summary below describes its results.
   *  Ignored while `isWorking`, since the spinner takes precedence. */
  isDone: boolean;
}) {
  return (
    <HStack align="center" css={statusRow} gap="space-8">
      <Detail aria-live="polite" textColor={isWorking ? "default" : "subtle"}>
        {children}
      </Detail>
      {isWorking ? (
        <Loader size="small" title="Søker" />
      ) : isDone ? (
        <CheckmarkIcon css={doneIcon} fontSize="1.25rem" title="Søket er fullført" />
      ) : null}
    </HStack>
  );
}
