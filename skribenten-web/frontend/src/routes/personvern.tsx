import { BodyLong, BodyShort, Box, Heading, Link, List, Page, VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";
import { type ReactNode } from "react";

export const Route = createFileRoute("/personvern")({
  component: PersonvernPage,
});

function Section({ title, children }: { title: string; children: ReactNode }) {
  return (
    <>
      <Box asChild marginBlock="space-32 space-12">
        <Heading level="2" size="medium">
          {title}
        </Heading>
      </Box>
      <VStack gap="space-16">{children}</VStack>
    </>
  );
}

function SubSection({ title, children }: { title: string; children: ReactNode }) {
  return (
    <div>
      <Box asChild marginBlock="space-12 space-12">
        <Heading level="3" size="small">
          {title}
        </Heading>
      </Box>
      <VStack gap="space-16">{children}</VStack>
    </div>
  );
}

function PersonvernPage() {
  return (
    <Box as="main" background="default" height="calc(100vh - var(--header-height))" overflowY="auto" tabIndex={0}>
      <Page.Block gutters width="md">
        <Box paddingBlock="space-48">
          <Heading level="1" size="xlarge">
            Personvernerklæring
          </Heading>
          <Section title="Personvern og sikkerhet i Skribenten">
            <BodyLong>
              Skribenten er en intern arbeidsflate for veiledere og saksbehandlere i Nav, for å opprette, redigere,
              kvalitetssikre og sende brev. Denne personvernerklæringen er knyttet til behandlingen av
              personopplysninger i Skribenten. For utfyllende informasjon om hvordan Nav behandler personopplysninger,
              kan du lese mer i <Link href="https://www.nav.no/personvern">Navs generelle personvernerklæring</Link>.
            </BodyLong>
          </Section>
          <Section title="Bruk av informasjonskapsler (cookies)">
            <BodyLong>Når du besøker nettsiden bruker vi informasjonskapsler (cookies).</BodyLong>
            <BodyLong>
              Informasjonskapsler er små tekstfiler som plasseres på din datamaskin når du laster ned en nettside. Noen
              av informasjonskapslene er nødvendige for at ulike tjenester på nettsiden vår skal fungere slik vi ønsker.
              Funksjonen kan slås av og på i de fleste nettlesere gjennom «innstillinger», «sikkerhet» eller liknende.
              Hvis du slår av informasjonskapsler i nettleseren din, vil ikke all funksjonalitet virke som den skal.
              Skribenten sine informasjonskapsler inneholder ikke personopplysninger og er ingen sikkerhetsrisiko for
              deg.
            </BodyLong>
            <BodyLong>
              Vi bruker informasjonskapsler til å forbedre brukeropplevelsen og innholdet. Når du besøker oss sender
              nettleseren din opplysninger til Navs analyseverktøy. For hver side du åpner, lagres opplysninger om
              hvilken side du er på, hvilken side du kommer fra og går til, hvilken nettleser du bruker, om du bruker PC
              eller mobile løsninger m.m.
            </BodyLong>
            <BodyLong>
              Opplysningene brukes til å kartlegge hvordan og hvor mye Skribenten brukes, uten å identifisere
              IP-adresser. Vi bruker verktøyet Innblikk (internt Nav verktøy) og Skyra i analysearbeidet.
            </BodyLong>
            <SubSection title="Skyra">
              <BodyLong>
                Vi bruker verktøyet Skyra for å innhente og lagre de tilbakemeldingene du gir på undersøkelser i
                Skribenten. Det er valgfritt å svare. Når du sender inn en tilbakemelding, lagres opplysningene i Skyra
                for å hjelpe oss med å forbedre tjenesten. Opplysningene som samles inn inkluderer tilbakemeldingen du
                gir, tidspunktet for tilbakemeldingen, og en anonym ID som ikke kan knyttes til deg personlig. Vi bruker
                denne informasjonen for å forstå brukernes behov og forbedre Skribenten basert på tilbakemeldingene vi
                mottar.
              </BodyLong>
            </SubSection>
            <SubSection title="Statistikk og analyseverktøy (Innblikk)">
              <BodyLong>
                Vi bruker et verktøy laget internt i Nav, Innblikk, til å samle inn data til bruk i statistikk og
                analyse. Vi samler inn, analyserer og tolker data for å se hvordan brukerene beveger seg gjennom
                systemet, hvilke handlinger og valg de gjør underveis. Ved å følge atferden over tid, kan vi
                identifisere utfordringer, teste forbedringer og måle effekten av endringer.
              </BodyLong>
              <VStack gap="space-12">
                <BodyShort>Dataene vi samler inn omfatter blant annet:</BodyShort>
                <List as="ul">
                  <List.Item>Hvor mange som besøker ulike sider, og hvor lenge besøket varer.</List.Item>
                  <List.Item>Hvordan folk navigerer mellom sider eller steg i en prosess.</List.Item>
                  <List.Item>Hvilke knapper, menyer og lenker folk klikker på.</List.Item>
                  <List.Item>Hvordan de fant frem til Skribenten, for eksempel via andre fagsystemer.</List.Item>
                  <List.Item>
                    Hvilke nettlesere som brukes, hvilket språk nettleseren bruker, hva slags enhet som blir brukt
                    (mobil eller pc)
                  </List.Item>
                </List>
              </VStack>
              <BodyLong>
                Data og opplysninger kobles ikke til deg som person, og analysene gjøres kun på gruppenivå.
                Informasjonskapselen slettes hver 30. dag. Vi kartlegger ikke enkeltpersoners bruksmønstre, og
                sammenstiller aldri data og opplysninger som kan identifisere enkeltindivider. Alt vi samler inn lagres
                sikkert internt i Nav.
              </BodyLong>
            </SubSection>
            <SubSection title="Dine rettigheter">
              <BodyLong>
                Du har blant annet rett til innsyn i hvilke opplysninger vi har lagret om deg. Les mer her om dine
                rettigheter og hvordan du gjør krav på disse
                <Link href="https://www.nav.no/no/Nav-og-samfunn/om-Nav/personvern-i-arbeids-og-velferdsetaten">
                  Personvernerklæring for ansatte i Arbeids- og velferdsetaten
                </Link>
                .
              </BodyLong>
            </SubSection>

            <SubSection title="Personvernombudet">
              <BodyLong>
                Arbeids- og velferdsetaten har et{" "}
                <Link href="https://www.nav.no/personvernombudet">personvernombud</Link> som skal ivareta
                personverninteressene, også til de ansatte. Personvernombudet kan gi råd og veiledning generelt om Navs
                behandling av personopplysninger og kan hjelpe deg med å ivareta dine personverninteresser.
              </BodyLong>
            </SubSection>

            <SubSection title="Klage til Datatilsynet">
              <BodyLong>
                Du har rett til å klage til Datatilsynet hvis du ikke er fornøyd med hvordan vi behandler
                personopplysninger om deg, eller hvis du mener behandlingen er i strid med personvernreglene. Ta først
                kontakt med vårt personvernombud. Informasjon om hvordan du går frem finner du på nettsidene til{" "}
                <Link href="https://www.datatilsynet.no/">Datatilsynet</Link>.
              </BodyLong>
            </SubSection>
          </Section>
        </Box>
      </Page.Block>
    </Box>
  );
}
