import { BellIcon, ExternalLinkIcon } from "@navikt/aksel-icons";
import {
  BodyLong,
  BodyShort,
  Box,
  Button,
  Dialog,
  Heading,
  HStack,
  InternalHeader,
  Link,
  Popover,
  Theme,
  VStack,
} from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useId, useState } from "react";

import { getAcknowledgedAnnouncements } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import {
  isAnnouncementExpired,
  requestAnnouncementReplay,
  useIsAnnouncementAvailable,
} from "~/components/featureAnnouncement/featureAnnouncementState";
import { useUserInfo } from "~/hooks/useUserInfo";

import { PRODUCT_ANNOUNCEMENTS, type ProductAnnouncement } from "./productAnnouncements";

export const DOCUMENTATION_URL = "https://navikt.github.io/pensjonsbrev/docs/main/index.html";

/**
 * Bell in the top bar with product announcements and useful links. A red dot marks announcements the user has not
 * acknowledged yet. Acknowledged announcements stay in the list until their last display day.
 */
export function NotificationBell({ announcements = PRODUCT_ANNOUNCEMENTS }: { announcements?: ProductAnnouncement[] }) {
  const user = useUserInfo();
  const [anchor, setAnchor] = useState<HTMLButtonElement | null>(null);
  const [open, setOpen] = useState(false);
  const [unavailableOpen, setUnavailableOpen] = useState(false);
  const [unavailable, setUnavailable] = useState<ProductAnnouncement | null>(null);
  const popoverId = useId();
  const newsHeadingId = useId();
  const linksHeadingId = useId();

  const relevant = user
    ? announcements.filter(
        (announcement) =>
          !isAnnouncementExpired(announcement.showUntil) && (announcement.isRelevantFor?.(user) ?? true),
      )
    : [];
  const acknowledgedQuery = useQuery({ ...getAcknowledgedAnnouncements, enabled: relevant.length > 0 });
  const unseenCount = acknowledgedQuery.isSuccess
    ? relevant.filter((announcement) => !acknowledgedQuery.data.includes(announcement.id)).length
    : 0;
  const label =
    unseenCount > 0
      ? `Nytt i Skribenten og nyttige lenker, ${unseenCount} ${unseenCount === 1 ? "ny nyhet" : "nye nyheter"}`
      : "Nytt i Skribenten og nyttige lenker";

  const showMe = (announcement: ProductAnnouncement, availableHere: boolean) => {
    setOpen(false);
    if (availableHere) {
      // The tour returns focus to the element that had it when the tour started.
      anchor?.focus();
      requestAnnouncementReplay(announcement.id);
    } else {
      setUnavailable(announcement);
      setUnavailableOpen(true);
    }
  };

  return (
    <>
      <InternalHeader.Button
        aria-controls={open ? popoverId : undefined}
        aria-expanded={open}
        onClick={() => setOpen((current) => !current)}
        ref={setAnchor}
      >
        <Box as="span" css={{ display: "inline-flex", position: "relative" }}>
          <BellIcon fontSize="1.5rem" title={label} />
          {unseenCount > 0 && (
            <Box
              as="span"
              background="danger-strong"
              borderRadius="full"
              css={{ position: "absolute", top: 0, right: 0 }}
              data-testid="unseen-announcements-dot"
              height="var(--ax-space-8)"
              width="var(--ax-space-8)"
            />
          )}
        </Box>
      </InternalHeader.Button>
      {/* InternalHeader is dark; the popover and dialog should follow the rest of the app. */}
      <Theme hasBackground={false} theme="light">
        <Popover
          anchorEl={anchor}
          css={{ overflow: "hidden" }}
          id={popoverId}
          onClose={() => setOpen(false)}
          open={open}
          placement="bottom-end"
        >
          <Box maxWidth="calc(100vw - var(--ax-space-32))" width="24rem">
            <VStack aria-labelledby={newsHeadingId} as="section">
              <Box
                asChild
                background="meta-purple-soft"
                borderColor="meta-purple-subtleA"
                borderWidth="0 0 1 0"
                paddingInline="space-16"
              >
                <HStack align="center" minHeight="var(--ax-space-48)">
                  <Heading css={{ color: "var(--ax-text-meta-purple)" }} id={newsHeadingId} level="2" size="small">
                    Nytt i Skribenten
                  </Heading>
                </HStack>
              </Box>
              <Box padding="space-16">
                {relevant.length > 0 ? (
                  <VStack as="ul" css={{ listStyle: "none", margin: 0, padding: 0 }} gap="space-16">
                    {relevant.map((announcement) => (
                      <AnnouncementItem announcement={announcement} key={announcement.id} onShowMe={showMe} />
                    ))}
                  </VStack>
                ) : (
                  <BodyShort size="small">Ingen nyheter akkurat nå.</BodyShort>
                )}
              </Box>
            </VStack>
            <Box paddingInline="space-16">
              <Divider />
            </Box>
            <VStack aria-labelledby={linksHeadingId} as="section" gap="space-8" padding="space-16">
              <Heading id={linksHeadingId} level="2" size="xsmall">
                Dokumentasjon og nyttige lenker
              </Heading>
              <BodyShort size="small">
                <Link href={DOCUMENTATION_URL} rel="noopener noreferrer" target="_blank">
                  Dokumentasjon og veiledning
                  <ExternalLinkIcon title="åpnes i ny fane" />
                </Link>
              </BodyShort>
            </VStack>
          </Box>
        </Popover>
        <Dialog onOpenChange={setUnavailableOpen} open={unavailableOpen} size="small">
          <Dialog.Popup returnFocusTo={() => anchor} width="small">
            <Dialog.Header>
              <Dialog.Title>{unavailable?.unavailable.heading}</Dialog.Title>
            </Dialog.Header>
            <Dialog.Body>
              <BodyLong>{unavailable?.unavailable.body}</BodyLong>
            </Dialog.Body>
            <Dialog.Footer>
              <Dialog.CloseTrigger>
                <Button size="small">Lukk</Button>
              </Dialog.CloseTrigger>
            </Dialog.Footer>
          </Dialog.Popup>
        </Dialog>
      </Theme>
    </>
  );
}

const AnnouncementItem = ({
  announcement,
  onShowMe,
}: {
  announcement: ProductAnnouncement;
  onShowMe: (announcement: ProductAnnouncement, availableHere: boolean) => void;
}) => {
  const availableHere = useIsAnnouncementAvailable(announcement.id);
  const titleId = useId();

  return (
    <VStack align="start" as="li" gap="space-4">
      <Heading id={titleId} level="3" size="xsmall">
        {announcement.title}
      </Heading>
      <BodyShort size="small">{announcement.description}</BodyShort>
      <Button
        aria-describedby={titleId}
        css={{ marginTop: "var(--ax-space-4)" }}
        onClick={() => onShowMe(announcement, availableHere)}
        size="small"
        variant="secondary"
      >
        Vis meg
      </Button>
    </VStack>
  );
};
