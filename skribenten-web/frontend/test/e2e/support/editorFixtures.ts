import { SpraakKode } from "~/types/apiTypes";
import { type BrevInfo } from "~/types/brev";
import { type Sakspart, type Signatur } from "~/types/brevbakerTypes";
import { brevInfo, signatur } from "~test/support/brevFixtures";

/** Brev under redigering, delt av spesifikasjonene for editor-komponentene. */
export const editorInfo = (): BrevInfo =>
  brevInfo({
    brevkode: "BREV1",
    brevtittel: "Brev 1",
    opprettet: "2024-01-01",
    sistredigert: "2024-01-01",
    sistredigertAv: { id: "Z123", navn: "Z entotre" },
    opprettetAv: { id: "Z123", navn: "Z entotre" },
    status: { type: "UnderRedigering", redigeresAv: { id: "Z123", navn: "Z entotre" } },
    avsenderEnhet: { enhetNr: "0001", navn: "NAV Familie- og pensjonsytelser" },
    spraak: SpraakKode.Bokmaal,
    saksId: 22981081,
  });

export const baseSakspart = (): Sakspart => ({
  gjelderNavn: "Test Testeson",
  gjelderFoedselsnummer: "12345678910",
  saksnummer: "1234",
  dokumentDato: "2024-03-15",
});

export const baseSignatur = (): Signatur =>
  signatur({
    hilsenTekst: "Med vennlig hilsen",
    saksbehandlerNavn: "Ole Saksbehandler",
    attesterendeSaksbehandlerNavn: "",
    navAvsenderEnhet: "Nav Familie- og pensjonsytelser Porsgrunn",
  });
