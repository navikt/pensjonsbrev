import { BellIcon, ExternalLinkIcon, SparklesIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, HStack, InternalHeader, Link, Modal, Theme, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useId, useState } from "react";

import { getAcknowledgedAnnouncements, getSeenAnnouncements } from "~/api/skribenten-api-endpoints";
import { useMarkAnnouncementSeen } from "~/components/featureAnnouncement/announcementStatus";
import {
  isAnnouncementExpired,
  isAnnouncementPublished,
  requestAnnouncementReplay,
  useIsAnnouncementAvailable,
} from "~/components/featureAnnouncement/featureAnnouncementState";
import { formatStringDate } from "~/utils/dateUtils";

import { listedUntil, PRODUCT_ANNOUNCEMENTS, type ProductAnnouncement } from "./productAnnouncements";

/**
 * Bell in the top bar with product announcements. A red dot marks announcements the user has neither seen in the bell
 * nor acknowledged in their tour. Opening the bell marks them as seen, while their tour still starts automatically
 * where it belongs. They stay in the list until their last day.
 */
export function NotificationBell({ announcements = PRODUCT_ANNOUNCEMENTS }: { announcements?: ProductAnnouncement[] }) {
  const [bell, setBell] = useState<HTMLButtonElement | null>(null);
  const [open, setOpen] = useState(false);
  const position = usePositionBelow(open ? bell : null);
  const modalId = useId();
  const headingId = useId();
  const markSeen = useMarkAnnouncementSeen();

  const current = announcements.filter(
    (announcement) =>
      isAnnouncementPublished(announcement.publishedAt) && !isAnnouncementExpired(listedUntil(announcement)),
  );
  const acknowledgedQuery = useQuery({ ...getAcknowledgedAnnouncements, enabled: current.length > 0 });
  const seenQuery = useQuery({ ...getSeenAnnouncements, enabled: current.length > 0 });
  const unseen =
    acknowledgedQuery.isSuccess && seenQuery.isSuccess
      ? current.filter(
          (announcement) =>
            !acknowledgedQuery.data.includes(announcement.id) && !seenQuery.data.includes(announcement.id),
        )
      : [];
  const label =
    unseen.length > 0
      ? `Nytt i Skribenten, ${unseen.length} ${unseen.length === 1 ? "ny nyhet" : "nye nyheter"}`
      : "Nytt i Skribenten";

  // Everything shown in the open bell counts as seen, also when its status arrives after the bell was opened.
  // Marking updates the cached list right away, so an announcement is only marked once.
  const unseenIds = unseen.map((announcement) => announcement.id).join(",");
  useEffect(() => {
    if (!open || unseenIds === "") return;
    for (const id of unseenIds.split(",")) markSeen(id);
  }, [open, unseenIds, markSeen]);

  const [replayAfterClose, setReplayAfterClose] = useState<string | null>(null);
  const showMe = (announcement: ProductAnnouncement) => {
    setReplayAfterClose(announcement.id);
    setOpen(false);
  };

  // Runs after the modal has closed, when the bell can take focus again. The tour returns focus to it when it ends.
  useEffect(() => {
    if (open || replayAfterClose === null) return;
    bell?.focus();
    requestAnnouncementReplay(replayAfterClose);
    setReplayAfterClose(null);
  }, [open, replayAfterClose, bell]);

  return (
    <>
      <InternalHeader.Button
        aria-controls={open ? modalId : undefined}
        aria-haspopup="dialog"
        onClick={() => setOpen(true)}
        ref={setBell}
      >
        <Box as="span" css={{ display: "inline-flex", position: "relative" }}>
          <BellIcon fontSize="1.5rem" title={label} />
          {unseen.length > 0 && (
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
      {/* InternalHeader is dark; the modal should follow the rest of the app. */}
      <Theme hasBackground={false} theme="light">
        <Modal
          aria-labelledby={headingId}
          closeOnBackdropClick
          id={modalId}
          onClose={() => {
            setOpen(false);
            bell?.focus();
          }}
          open={open}
          size="small"
          width={451}
          {...(position && {
            css: {
              position: "fixed",
              inset: "auto",
              top: `calc(${position.top}px + var(--ax-space-8))`,
              right: `${position.right}px`,
              maxWidth: `calc(100vw - ${position.right}px - var(--ax-space-16))`,
              margin: 0,
            },
          })}
        >
          <Modal.Header
            css={{
              background: "var(--ax-bg-meta-purple-soft)",
              borderBottom: "1px solid var(--ax-border-meta-purple-subtleA)",
              boxSizing: "border-box",
              height: "var(--ax-space-48)",
              // Centres Aksel's 32px close button in the 24px left between the 12px top and bottom padding.
              "& .aksel-modal__button": { marginTop: "calc(-1 * var(--ax-space-4))" },
            }}
          >
            <HStack
              align="center"
              css={{ color: "var(--ax-text-meta-purple)" }}
              gap="space-8"
              height="100%"
              wrap={false}
            >
              <SparklesIcon aria-hidden fontSize="1.5rem" />
              <Heading id={headingId} level="2" size="small">
                Nytt i Skribenten
              </Heading>
            </HStack>
          </Modal.Header>
          <Modal.Body css={{ paddingTop: "var(--ax-space-12)" }}>
            {/* Aksel keeps a closed modal in the page, so its content is only rendered while it is open. */}
            {!open ? null : current.length > 0 ? (
              <VStack
                as="ul"
                css={{
                  listStyle: "none",
                  margin: 0,
                  padding: 0,
                  "& > li:not(:last-child)": {
                    paddingBottom: "var(--ax-space-16)",
                    borderBottom: "1px solid var(--ax-border-neutral-subtle)",
                  },
                }}
                gap="space-16"
              >
                {current.map((announcement) => (
                  <AnnouncementItem announcement={announcement} key={announcement.id} onShowMe={showMe} />
                ))}
              </VStack>
            ) : (
              <BodyShort size="small">Ingen nyheter akkurat nå.</BodyShort>
            )}
          </Modal.Body>
        </Modal>
      </Theme>
    </>
  );
}

const AnnouncementItem = ({
  announcement,
  onShowMe,
}: {
  announcement: ProductAnnouncement;
  onShowMe: (announcement: ProductAnnouncement) => void;
}) => {
  const availableHere = useIsAnnouncementAvailable(announcement.id);
  const titleId = useId();

  return (
    <VStack align="start" as="li" gap="space-4">
      <BodyShort as="time" dateTime={announcement.publishedAt} size="small">
        {formatStringDate(announcement.publishedAt)}
      </BodyShort>
      <Heading id={titleId} level="3" size="xsmall">
        {announcement.title}
      </Heading>
      <BodyShort size="small">{announcement.description}</BodyShort>
      <HStack align="center" css={{ marginTop: "var(--ax-space-8)" }} gap="space-12">
        {availableHere && (
          <Button aria-describedby={titleId} onClick={() => onShowMe(announcement)} size="small" variant="secondary">
            Se hvordan
          </Button>
        )}
        <BodyShort size="small" weight="semibold">
          <Link
            aria-describedby={titleId}
            href={announcement.documentationUrl}
            rel="noopener noreferrer"
            target="_blank"
            underline={false}
          >
            <ExternalLinkIcon aria-hidden fontSize="1.25rem" />
            Se dokumentasjon
            <span className="aksel-sr-only"> (åpnes i ny fane)</span>
          </Link>
        </BodyShort>
      </HStack>
    </VStack>
  );
};

/**
 * Where to place a panel right below `element`, right-aligned with it. Follows the element when the window is resized
 * or its neighbours change size, e.g. when the web font or the user's name loads after the panel was opened.
 */
function usePositionBelow(element: HTMLElement | null) {
  const [position, setPosition] = useState<{ top: number; right: number } | null>(null);

  useEffect(() => {
    if (!element) {
      setPosition(null);
      return;
    }
    const update = () => {
      const rect = element.getBoundingClientRect();
      setPosition({ top: rect.bottom, right: window.innerWidth - rect.right });
    };
    update();
    window.addEventListener("resize", update);
    const observer = typeof ResizeObserver === "undefined" ? null : new ResizeObserver(update);
    for (const sibling of element.parentElement?.children ?? []) observer?.observe(sibling);
    return () => {
      window.removeEventListener("resize", update);
      observer?.disconnect();
    };
  }, [element]);

  return position;
}
