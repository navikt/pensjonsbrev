import { QuestionmarkCircleIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, GlobalAlert, HStack, Loader, Popover, Switch, VStack } from "@navikt/ds-react";
import { useId, useState } from "react";

import { type AttestantLetterDiffStatus, type DiffToggles } from "~/hooks/useAttestantLetterDiff";

const SWITCHES: { toggle: keyof DiffToggles; label: string }[] = [
  { toggle: "inserted", label: "Marker tekst som er lagt til" },
  { toggle: "deleted", label: "Vis slettet tekst" },
];

export const AttestantDiffMenu = ({
  toggles,
  status,
  onToggle,
}: {
  toggles: DiffToggles;
  status: AttestantLetterDiffStatus;
  onToggle: (toggle: keyof DiffToggles, checked: boolean) => void;
}) => {
  const [alertDismissed, setAlertDismissed] = useState(false);
  const [legendAnchor, setLegendAnchor] = useState<HTMLButtonElement | null>(null);
  const [legendOpen, setLegendOpen] = useState(false);
  const legendId = useId();

  return (
    <>
      <Box
        asChild
        borderColor="neutral-subtle"
        borderWidth="0 0 1 0"
        flexShrink="0"
        paddingBlock="space-4"
        paddingInline="space-8"
        width="100%"
      >
        <HStack
          align="center"
          css={{ minHeight: "var(--ax-space-48)", background: "var(--ax-bg-accent-softA)" }}
          gap="space-16"
          justify="space-between"
          wrap={false}
        >
          <HStack align="center" gap="space-0 space-24">
            {SWITCHES.map(({ toggle, label }) => (
              <Switch
                checked={toggles[toggle]}
                key={toggle}
                onChange={(event) => {
                  setAlertDismissed(false);
                  onToggle(toggle, event.target.checked);
                }}
                size="small"
              >
                {label}
              </Switch>
            ))}
            {status === "loading" && <Loader size="small" title="Markerer endringer" />}
          </HStack>
          <Button
            aria-controls={legendOpen ? legendId : undefined}
            aria-expanded={legendOpen}
            icon={<QuestionmarkCircleIcon title="Slik vises endringer" />}
            onClick={() => setLegendOpen((open) => !open)}
            ref={setLegendAnchor}
            size="small"
            type="button"
            variant="tertiary"
          />
          <Popover
            anchorEl={legendAnchor}
            id={legendId}
            offset={16}
            onClose={() => setLegendOpen(false)}
            open={legendOpen}
            placement="bottom-end"
          >
            <Popover.Content>
              <VStack gap="space-16">
                <BodyShort size="small">
                  <span className="attestant-diff-inserted">Slik vises ord som er lagt til manuelt</span>
                </BodyShort>
                <BodyShort size="small">
                  <span className="attestant-diff-deleted">Slik vises ord som er slettet manuelt</span>
                </BodyShort>
              </VStack>
            </Popover.Content>
          </Popover>
        </HStack>
      </Box>
      {!alertDismissed && status === "error" && (
        <GlobalAlert centered={false} size="small" status="error">
          <GlobalAlert.Header>
            <GlobalAlert.Title as="div">
              Endringer kan ikke vises akkurat nå. Du kan fortsatt gjennomgå brevet.
            </GlobalAlert.Title>
            <GlobalAlert.CloseButton onClick={() => setAlertDismissed(true)} />
          </GlobalAlert.Header>
        </GlobalAlert>
      )}
      {!alertDismissed && status === "empty" && (
        <GlobalAlert centered={false} size="small" status="announcement">
          <GlobalAlert.Header>
            <GlobalAlert.Title as="div">Ingen endringer å vise.</GlobalAlert.Title>
            <GlobalAlert.CloseButton onClick={() => setAlertDismissed(true)} />
          </GlobalAlert.Header>
        </GlobalAlert>
      )}
    </>
  );
};
