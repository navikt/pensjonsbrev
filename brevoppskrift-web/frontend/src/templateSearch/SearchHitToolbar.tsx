import { ArrowLeftIcon, ChevronDownIcon, ChevronLeftIcon, ChevronRightIcon, ChevronUpIcon } from "@navikt/aksel-icons";
import { Button, Detail, HStack, Switch } from "@navikt/ds-react";
import { createLink } from "@tanstack/react-router";
import { type ReactNode } from "react";

import { AnimatedEllipsis } from "~/components/AnimatedEllipsis";
import { type ResultRef, type SearchCriteria, toTemplateSearch, toTemplatesSearch } from "~/search/searchContext";
import { blockNavigation, type ResultLinkTarget, type TemplateHits } from "~/templateSearch/useTemplateHits";

/** An Aksel button that is a typed router link. */
const ButtonLink = createLink(Button);

/**
 * The search controls on a template opened from a search: a switch for the hit marking, and - while marking is on - stepping
 * between matching blocks in this template and between search results.
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
        Marker søketreff
      </Switch>
      {marking ? <HitNavigation criteria={criteria} hits={hits} onSelectBlock={onSelectBlock} /> : null}
    </HStack>
  );
}

/** Back to the result list when the template was opened from a search, otherwise to the template list. */
export function BackButton({ criteria, current }: { criteria: SearchCriteria | undefined; current: ResultRef }) {
  if (!criteria) {
    return (
      <ButtonLink icon={<ArrowLeftIcon aria-hidden />} size="small" to="/templates" variant="secondary">
        Tilbake til mal-oversikten
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
      Tilbake til søkeresultatene
    </ButtonLink>
  );
}

function HitNavigation({
  criteria,
  hits,
  onSelectBlock,
}: {
  criteria: SearchCriteria;
  hits: TemplateHits;
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
      {result ? (
        <HStack align="center" gap="space-4">
          {resultLink(previousResult, <ChevronLeftIcon title="Forrige resultat" />)}
          <Detail>
            Mal/språk {result.position} av {result.total}
          </Detail>
          {resultLink(nextResult, <ChevronRightIcon title="Neste resultat" />)}
        </HStack>
      ) : null}
    </>
  );
}
