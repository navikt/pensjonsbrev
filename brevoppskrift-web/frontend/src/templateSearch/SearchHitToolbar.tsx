import { ArrowLeftIcon, ChevronDownIcon, ChevronLeftIcon, ChevronRightIcon, ChevronUpIcon } from "@navikt/aksel-icons";
import { Button, type ButtonProps, Detail, HStack, Switch } from "@navikt/ds-react";
import { createLink } from "@tanstack/react-router";
import { type AnchorHTMLAttributes, type ReactNode, type Ref } from "react";

import { AnimatedEllipsis } from "~/components/AnimatedEllipsis";
import { type ResultRef, type SearchCriteria, toTemplateSearch, toTemplatesSearch } from "~/search/searchContext";
import { blockNavigation, type ResultLinkTarget, type TemplateHits } from "~/templateSearch/useTemplateHits";

/** An Aksel button rendered as an `<a>`. Passing `Button` itself to `createLink`
 *  would render a `<button href>`, which is not a link (no open in new tab). */
function AnchorButton({
  ref,
  ...props
}: Omit<ButtonProps, "ref"> & AnchorHTMLAttributes<HTMLAnchorElement> & { ref?: Ref<HTMLAnchorElement> }) {
  return <Button as="a" ref={ref} {...props} />;
}

/** An Aksel button that is a typed router link. */
const ButtonLink = createLink(AnchorButton);

/**
 * The search controls on a template opened from a search: a switch for the hit marking, stepping between
 * matching blocks in this template while marking is on, and stepping between search results.
 */
export function SearchHitToolbar({
  criteria,
  hits,
  marking,
  onMarkingChange,
  onSelectBlock,
}: {
  criteria: SearchCriteria;
  hits: TemplateHits;
  marking: boolean;
  onMarkingChange: (marking: boolean) => void;
  onSelectBlock: (index: number) => void;
}) {
  return (
    <HStack align="center" gap="space-16" wrap>
      <Switch checked={marking} onChange={(event) => onMarkingChange(event.target.checked)} size="small">
        Vis søketreff
      </Switch>
      <HitNavigation criteria={criteria} hits={hits} marking={marking} onSelectBlock={onSelectBlock} />
    </HStack>
  );
}

/** Back to the result list when the template was opened from a search, otherwise to the template list. */
export function BackButton({ criteria, current }: { criteria: SearchCriteria | undefined; current: ResultRef }) {
  if (!criteria) {
    return (
      <ButtonLink icon={<ArrowLeftIcon aria-hidden />} size="small" to="/templates" variant="secondary">
        Mal-oversikt
      </ButtonLink>
    );
  }
  return (
    <ButtonLink
      icon={<ArrowLeftIcon aria-hidden />}
      search={toTemplatesSearch(criteria, current)}
      size="small"
      to="/templates"
      variant="secondary"
    >
      Søkeresultat
    </ButtonLink>
  );
}

function HitNavigation({
  criteria,
  hits,
  marking,
  onSelectBlock,
}: {
  criteria: SearchCriteria;
  hits: TemplateHits;
  /** Stepping between blocks only makes sense while they are marked. */
  marking: boolean;
  onSelectBlock: (index: number) => void;
}) {
  if (hits.status === "loading") {
    return (
      <Detail textColor="subtle">
        Finner søketreff
        <AnimatedEllipsis />
      </Detail>
    );
  }
  if (hits.status === "failed") {
    return <Detail textColor="subtle">Kunne ikke finne søketreffene på grunn av en teknisk feil.</Detail>;
  }
  const { matchingBlocks, activeBlock, result, previousResult, nextResult } = hits;
  const { at, previousBlock, nextBlock } = blockNavigation(matchingBlocks, activeBlock);
  const resultLink = (target: ResultLinkTarget | undefined, icon: ReactNode) =>
    target ? (
      <ButtonLink
        icon={icon}
        params={{ malType: target.ref.malType, templateId: target.ref.id }}
        search={toTemplateSearch(criteria, target.ref, target.block)}
        size="small"
        to="/template/$malType/$templateId"
        variant="tertiary"
      />
    ) : (
      <Button disabled icon={icon} size="small" variant="tertiary" />
    );
  return (
    <>
      {marking ? (
        <HStack align="center" gap="space-4">
          {matchingBlocks.length === 0 ? (
            <Detail>Ingen treff i denne språkversjonen</Detail>
          ) : (
            <>
              <Button
                disabled={previousBlock === undefined}
                icon={<ChevronUpIcon title="Forrige treff" />}
                onClick={() => previousBlock !== undefined && onSelectBlock(previousBlock)}
                size="small"
                variant="tertiary"
              />
              <Detail aria-live="polite">
                Avsnitt {at === undefined ? "–" : at + 1} av {matchingBlocks.length}
              </Detail>
              <Button
                disabled={nextBlock === undefined}
                icon={<ChevronDownIcon title="Neste treff" />}
                onClick={() => nextBlock !== undefined && onSelectBlock(nextBlock)}
                size="small"
                variant="tertiary"
              />
            </>
          )}
        </HStack>
      ) : null}
      <HStack align="center" gap="space-4">
        {resultLink(previousResult, <ChevronLeftIcon title="Forrige resultat" />)}
        <Detail>
          Mal {result.position ?? "–"} av {result.total}
        </Detail>
        {resultLink(nextResult, <ChevronRightIcon title="Neste resultat" />)}
      </HStack>
    </>
  );
}
