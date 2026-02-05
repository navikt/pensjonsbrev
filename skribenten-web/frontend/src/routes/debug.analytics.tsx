import { TrashIcon } from "@navikt/aksel-icons";
import { BodyShort, BoxNew, Button, Heading, HStack, Table, Tabs, Tag, VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useMemo, useState } from "react";

export const Route = createFileRoute("/debug/analytics")({
  component: AnalyticsDashboard,
});

interface AnalyticsEvent {
  timestamp: number;
  type: "event" | "pageview";
  name: string;
  data?: Record<string, unknown>;
}

const getStoredEvents = (): AnalyticsEvent[] => {
  if (globalThis.window) {
    const stored = sessionStorage.getItem("localAnalyticsEvents");
    if (stored) {
      try {
        return JSON.parse(stored);
      } catch {
        // Ignore parse errors
      }
    }
  }
  return [];
};

function AnalyticsDashboard() {
  const [events, setEvents] = useState<AnalyticsEvent[]>(getStoredEvents);
  const [activeTab, setActiveTab] = useState<string>("all");

  useEffect(() => {
    // Setup live update - only the interval callback calls setState
    const interval = setInterval(() => {
      setEvents(getStoredEvents());
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  const clearEvents = () => {
    sessionStorage.setItem("localAnalyticsEvents", "[]");
    if (globalThis.localAnalyticsEvents) {
      globalThis.localAnalyticsEvents = [];
    }
    setEvents([]);
  };

  // Filter events based on active tab
  const filteredEvents = useMemo(() => {
    if (activeTab === "all") return events;
    if (activeTab === "attestering")
      return events.filter((e) => e.name.includes("attest") || e.data?.type === "attestering");
    if (activeTab === "brev") return events.filter((e) => e.name.includes("brev"));
    return events;
  }, [events, activeTab]);

  // Calculate statistics - focused on core metrics
  const stats = useMemo(() => {
    // 1. Attestering funnel
    const brevAttestert = events.filter((e) => e.name === "brev attestert").length;
    const brevSendt = events.filter((e) => e.name === "brev sendt" && e.data?.type === "attestering").length;

    // 2. Template usage
    const brevOpprettet = events.filter((e) => e.name === "brev opprettet");
    const templateUsage = brevOpprettet.reduce(
      (acc, e) => {
        const brevkode = (e.data?.brevkode as string) || "ukjent";
        const brevtittel = (e.data?.brevtittel as string) || brevkode;
        acc[brevkode] = {
          count: (acc[brevkode]?.count || 0) + 1,
          tittel: brevtittel,
        };
        return acc;
      },
      {} as Record<string, { count: number; tittel: string }>,
    );

    // 3. Paste tracking - large pastes per template
    const pasteEvents = events.filter((e) => e.name === "tekst limt inn");
    const largePastes = pasteEvents.filter((e) => e.data?.erStor === true);
    const pastesByTemplate = pasteEvents.reduce(
      (acc, e) => {
        const brevkode = (e.data?.brevkode as string) || "ukjent";
        const antallTegn = (e.data?.antallTegn as number) || 0;
        if (!acc[brevkode]) {
          acc[brevkode] = { count: 0, totalChars: 0, largePastes: 0 };
        }
        acc[brevkode].count += 1;
        acc[brevkode].totalChars += antallTegn;
        if (e.data?.erStor) acc[brevkode].largePastes += 1;
        return acc;
      },
      {} as Record<string, { count: number; totalChars: number; largePastes: number }>,
    );

    return {
      total: events.length,
      brevAttestert,
      brevSendt,
      brevOpprettet: brevOpprettet.length,
      templateUsage,
      completionRate: brevAttestert > 0 ? Math.round((brevSendt / brevAttestert) * 100) : 0,
      pasteCount: pasteEvents.length,
      largePasteCount: largePastes.length,
      pastesByTemplate,
    };
  }, [events]);

  return (
    <div style={{ height: "100vh", overflow: "auto" }}>
      <BoxNew background="default" padding="space-32" style={{ minHeight: "100%" }}>
        <VStack gap="space-32" style={{ maxWidth: "1400px", margin: "0 auto" }}>
          <HStack align="center" justify="space-between">
            <VStack gap="space-8">
              <Heading level="1" size="large">
                📊 Analytics PoC
              </Heading>
              <BodyShort>To enkle metrikker som viser verdien av analytics</BodyShort>
            </VStack>
            <Button icon={<TrashIcon />} onClick={clearEvents} size="small" variant="danger">
              Nullstill
            </Button>
          </HStack>
          {/* METRIC 1: Attestering Funnel */}
          <BoxNew background="neutral-soft" borderRadius="large" padding="space-24">
            <VStack gap="space-16">
              <VStack gap="space-4">
                <Heading level="2" size="medium">
                  1. Attesterings-funnel
                </Heading>
                <BodyShort size="small" style={{ color: "var(--a-text-subtle)" }}>
                  <strong>Hvorfor måle dette?</strong> Viser om attestanter fullfører arbeidsflyten. Høy drop-off kan
                  indikere UX-problemer eller tekniske feil.
                </BodyShort>
              </VStack>

              <HStack align="center" gap="space-16">
                <BoxNew
                  background="default"
                  borderRadius="medium"
                  padding="space-16"
                  style={{ textAlign: "center", minWidth: "120px" }}
                >
                  <Heading level="3" size="xlarge">
                    {stats.brevAttestert}
                  </Heading>
                  <BodyShort size="small">Attestert</BodyShort>
                </BoxNew>
                <span style={{ fontSize: "1.5rem" }}>→</span>
                <BoxNew
                  background="default"
                  borderRadius="medium"
                  padding="space-16"
                  style={{ textAlign: "center", minWidth: "120px" }}
                >
                  <Heading level="3" size="xlarge">
                    {stats.brevSendt}
                  </Heading>
                  <BodyShort size="small">Sendt</BodyShort>
                </BoxNew>
                <span style={{ fontSize: "1.5rem" }}>=</span>
                <BoxNew
                  background={stats.completionRate === 100 ? "success-soft" : "warning-soft"}
                  borderRadius="medium"
                  padding="space-16"
                  style={{ textAlign: "center", minWidth: "120px" }}
                >
                  <Heading level="3" size="xlarge">
                    {stats.completionRate}%
                  </Heading>
                  <BodyShort size="small">Fullført</BodyShort>
                </BoxNew>
              </HStack>
            </VStack>
          </BoxNew>
          {/* METRIC 2: Template Usage */}
          <BoxNew background="neutral-soft" borderRadius="large" padding="space-24">
            <VStack gap="space-16">
              <VStack gap="space-4">
                <Heading level="2" size="medium">
                  2. Mal-bruk
                </Heading>
                <BodyShort size="small" style={{ color: "var(--a-text-subtle)" }}>
                  <strong>Hvorfor måle dette?</strong> Viser hvilke brevmaler som brukes mest. Kan hjelpe prioritere
                  forbedringer og identifisere ubrukte maler.
                </BodyShort>
              </VStack>

              {stats.brevOpprettet === 0 ? (
                <BodyShort style={{ color: "var(--a-text-subtle)" }}>Ingen brev opprettet ennå</BodyShort>
              ) : (
                <Table size="small">
                  <Table.Header>
                    <Table.Row>
                      <Table.HeaderCell>Mal</Table.HeaderCell>
                      <Table.HeaderCell>Brevkode</Table.HeaderCell>
                      <Table.HeaderCell style={{ textAlign: "right" }}>Antall</Table.HeaderCell>
                    </Table.Row>
                  </Table.Header>
                  <Table.Body>
                    {Object.entries(stats.templateUsage)
                      .sort(([, a], [, b]) => b.count - a.count)
                      .map(([brevkode, { count, tittel }]) => (
                        <Table.Row key={brevkode}>
                          <Table.DataCell>{tittel}</Table.DataCell>
                          <Table.DataCell>
                            <code style={{ fontSize: "0.85em" }}>{brevkode}</code>
                          </Table.DataCell>
                          <Table.DataCell style={{ textAlign: "right" }}>
                            <Tag variant="info">{count}</Tag>
                          </Table.DataCell>
                        </Table.Row>
                      ))}
                  </Table.Body>
                </Table>
              )}
            </VStack>
          </BoxNew>
          {/* METRIC 3: Paste Tracking */}
          <BoxNew background="neutral-soft" borderRadius="large" padding="space-24">
            <VStack gap="space-16">
              <VStack gap="space-4">
                <Heading level="2" size="medium">
                  3. Innliming av tekst
                </Heading>
                <BodyShort size="small" style={{ color: "var(--a-text-subtle)" }}>
                  <strong>Hvorfor måle dette?</strong> Hyppig innliming av store tekstmengder kan indikere at malen
                  mangler nødvendig innhold og bør forbedres.
                </BodyShort>
              </VStack>

              <HStack gap="space-16">
                <BoxNew
                  background="default"
                  borderRadius="medium"
                  padding="space-16"
                  style={{ textAlign: "center", minWidth: "100px" }}
                >
                  <Heading level="3" size="xlarge">
                    {stats.pasteCount}
                  </Heading>
                  <BodyShort size="small">Totalt limt inn</BodyShort>
                </BoxNew>
                <BoxNew
                  background={stats.largePasteCount > 0 ? "warning-soft" : "default"}
                  borderRadius="medium"
                  padding="space-16"
                  style={{ textAlign: "center", minWidth: "100px" }}
                >
                  <Heading level="3" size="xlarge">
                    {stats.largePasteCount}
                  </Heading>
                  <BodyShort size="small">Store (&gt;200 tegn)</BodyShort>
                </BoxNew>
              </HStack>

              {Object.keys(stats.pastesByTemplate).length > 0 && (
                <Table size="small">
                  <Table.Header>
                    <Table.Row>
                      <Table.HeaderCell>Brevkode</Table.HeaderCell>
                      <Table.HeaderCell style={{ textAlign: "right" }}>Antall</Table.HeaderCell>
                      <Table.HeaderCell style={{ textAlign: "right" }}>Store</Table.HeaderCell>
                      <Table.HeaderCell style={{ textAlign: "right" }}>Snitt tegn</Table.HeaderCell>
                    </Table.Row>
                  </Table.Header>
                  <Table.Body>
                    {Object.entries(stats.pastesByTemplate)
                      .sort(([, a], [, b]) => b.largePastes - a.largePastes)
                      .map(([brevkode, { count, totalChars, largePastes }]) => (
                        <Table.Row key={brevkode}>
                          <Table.DataCell>
                            <code style={{ fontSize: "0.85em" }}>{brevkode}</code>
                          </Table.DataCell>
                          <Table.DataCell style={{ textAlign: "right" }}>{count}</Table.DataCell>
                          <Table.DataCell style={{ textAlign: "right" }}>
                            <Tag variant={largePastes > 0 ? "warning" : "neutral"}>{largePastes}</Tag>
                          </Table.DataCell>
                          <Table.DataCell style={{ textAlign: "right" }}>
                            {Math.round(totalChars / count)}
                          </Table.DataCell>
                        </Table.Row>
                      ))}
                  </Table.Body>
                </Table>
              )}
            </VStack>
          </BoxNew>
          {/* Raw Events - for debugging/demo */}
          <BoxNew background="neutral-soft" borderRadius="large" padding="space-24">
            <VStack gap="space-16">
              <Heading level="2" size="medium">
                Rådata ({events.length} hendelser)
              </Heading>
              <Tabs onChange={setActiveTab} value={activeTab}>
                <Tabs.List>
                  <Tabs.Tab label={`Alle (${events.length})`} value="all" />
                  <Tabs.Tab label="Attestering" value="attestering" />
                  <Tabs.Tab label="Brev" value="brev" />
                </Tabs.List>
              </Tabs>
              <div style={{ overflowX: "auto" }}>
                <Table>
                  <Table.Header>
                    <Table.Row>
                      <Table.HeaderCell>Tid</Table.HeaderCell>
                      <Table.HeaderCell>Type</Table.HeaderCell>
                      <Table.HeaderCell>Hendelse</Table.HeaderCell>
                      <Table.HeaderCell>Data</Table.HeaderCell>
                    </Table.Row>
                  </Table.Header>
                  <Table.Body>
                    {filteredEvents.length === 0 ? (
                      <Table.Row>
                        <Table.DataCell colSpan={4}>
                          <BodyShort>
                            Ingen hendelser registrert ennå. Naviger i appen for å generere hendelser.
                          </BodyShort>
                        </Table.DataCell>
                      </Table.Row>
                    ) : (
                      filteredEvents.map((event, index) => (
                        <Table.Row key={`${event.timestamp}-${index}`}>
                          <Table.DataCell>{new Date(event.timestamp).toLocaleTimeString("no-NO")}</Table.DataCell>
                          <Table.DataCell>
                            <Tag variant={event.type === "pageview" ? "info" : "success"}>{event.type}</Tag>
                          </Table.DataCell>
                          <Table.DataCell style={{ fontWeight: "bold" }}>{event.name}</Table.DataCell>
                          <Table.DataCell>
                            {event.data ? (
                              <pre style={{ fontSize: "0.85em", margin: 0 }}>{JSON.stringify(event.data, null, 2)}</pre>
                            ) : (
                              <span style={{ color: "var(--a-text-subtle)" }}>-</span>
                            )}
                          </Table.DataCell>
                        </Table.Row>
                      ))
                    )}
                  </Table.Body>
                </Table>
              </div>{" "}
            </VStack>
          </BoxNew>{" "}
        </VStack>
      </BoxNew>
    </div>
  );
}
