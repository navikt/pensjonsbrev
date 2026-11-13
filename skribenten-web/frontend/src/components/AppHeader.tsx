import { ExternalLinkIcon, LeaveIcon, MenuGridIcon } from "@navikt/aksel-icons";
import { ActionMenu, Box, InternalHeader, Spacer, Theme } from "@navikt/ds-react";
import { Link as RouterLink } from "@tanstack/react-router";

import { NotificationBell } from "~/components/notifications/NotificationBell";
import { useUserInfo } from "~/hooks/useUserInfo";

export function AppHeader() {
  return (
    <Box asChild borderColor="neutral-subtleA" borderWidth="0 0 1 0" height="var(--header-height)">
      <InternalHeader>
        <InternalHeader.Title as="h1">
          <RouterLink
            css={{ color: "inherit", textDecoration: "inherit" }}
            params={(current) => current}
            search={(current) => current}
            to="/"
          >
            Skribenten
          </RouterLink>
        </InternalHeader.Title>
        <Spacer />
        <NotificationBell />
        <UsefulLinksMenu />
        <UserDropdown />
      </InternalHeader>
    </Box>
  );
}

export const DOCUMENTATION_URL = "https://navikt.github.io/pensjonsbrev/docs/main/index.html";

function UsefulLinksMenu() {
  return (
    <ActionMenu>
      <ActionMenu.Trigger>
        <InternalHeader.Button>
          <MenuGridIcon fontSize="1.5rem" title="Nyttige lenker" />
        </InternalHeader.Button>
      </ActionMenu.Trigger>
      {/* InternalHeader is dark; the menu should follow the rest of the app. */}
      <Theme hasBackground={false} theme="light">
        <ActionMenu.Content align="end">
          <ActionMenu.Group aria-label="Nyttige lenker">
            <ActionMenu.Item
              as="a"
              href={DOCUMENTATION_URL}
              icon={<ExternalLinkIcon />}
              rel="noopener noreferrer"
              target="_blank"
            >
              Dokumentasjon og veiledning
              <span className="aksel-sr-only"> (åpnes i ny fane)</span>
            </ActionMenu.Item>
          </ActionMenu.Group>
        </ActionMenu.Content>
      </Theme>
    </ActionMenu>
  );
}

function UserDropdown() {
  const userInfo = useUserInfo();

  return (
    <ActionMenu>
      <ActionMenu.Trigger>
        <InternalHeader.UserButton
          css={{ svg: { width: "24px", height: "24px" } }}
          description={userInfo?.navident ? `id: ${userInfo.navident}` : ""}
          name={userInfo?.name ?? ""}
        />
      </ActionMenu.Trigger>
      <ActionMenu.Content align="end">
        <ActionMenu.Group aria-label="Handlinger">
          <ActionMenu.Item as="a" href="/bff/logout">
            Logg ut <Spacer /> <LeaveIcon aria-hidden fontSize="1.5rem" />
          </ActionMenu.Item>
        </ActionMenu.Group>
      </ActionMenu.Content>
    </ActionMenu>
  );
}
