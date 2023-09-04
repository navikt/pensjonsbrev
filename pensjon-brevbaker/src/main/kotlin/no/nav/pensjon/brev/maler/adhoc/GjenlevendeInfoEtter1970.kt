package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.fraser.common.Constants.ARBEID_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.GJENLEVENDEPENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.GJENLEVENDE_TILLEGGSSTOENADER
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object GjenlevendeInfoEtter1970 : AutobrevTemplate<Unit> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_ADHOC_2023_04_GJENLEVENDEINFOETTER1970

    override val template = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "adhoc - Informasjon om endringer i gjenlevendepensjonen din (født etter 1970)", //TODO midlertidig tittel,
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Gjenlevendepensjonen din blir tidsbegrenset",
                Nynorsk to "Attlevandepensjonen din blir tidsavgrensa",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for gjenlevendepensjon fra 1. januar 2024.",
                    Nynorsk to "Stortinget har vedtatt nye reglar for attlevandepensjon frå 1. januar 2024.",
                )
            }

            title2 {
                text(
                    Bokmal to "Hva betyr endringene for deg?",
                    Nynorsk to "Kva betyr endringane for deg?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du beholder gjenlevendepensjonen som den er i dag, men du får den bare i tre år fra 1. januar 2024.",
                    Nynorsk to "Du vil få attlevandepensjonen som den er i dag, men du får den berre i tre år frå 1. januar 2024.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Siste utbetaling av pensjonen din blir i desember 2026. Det kan bety at du får mindre å leve av.",
                    Nynorsk to "Den siste utbetalinga av pensjonen din blir i desember 2026. Det kan bety at du får mindre å leve av.",
                )
            }

            title2 {
                text(
                    Bokmal to "Trenger du hjelp til å få ny jobb eller jobbe mer?",
                    Nynorsk to "Treng du hjelp til å få ny jobb eller jobb meir?",
                )
            }

            paragraph {
                text(
                    Bokmal to "NAV tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. Du kan finne jobbsøkertips og lenke til ledige jobber på $ARBEID_URL.",
                    Nynorsk to "NAV tilbyr ulike tenester og støtteordningar for deg som treng hjelp til å få jobb. Du kan finne jobbsøkjartips og lenkje til ledige jobbar på $ARBEID_URL.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan snakke med deg om ulike muligheter i arbeidsmarkedet, utdanning eller andre tiltak.",
                    Nynorsk to "Vi kan snakke med deg om ulike moglegheiter i arbeidsmarknaden, utdanning eller andre tiltak.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du trenger utdanning som blir godkjent av NAV, eller du får arbeidstrening via NAV, kan du søke om skolepenger og tilleggsstønad. Du finner mer informasjon om disse stønadene på $GJENLEVENDE_TILLEGGSSTOENADER.",
                    Nynorsk to "Viss du treng utdanning som blir godkjent av NAV, eller viss du får arbeidstrening via NAV, kan du søkje om skolepengar eller tilleggsstønad. Du finn meir informasjon om desse stønadene på $GJENLEVENDE_TILLEGGSSTOENADER.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du fortsatt tar utdanning eller har arbeidstrening fra 1. januar 2027, kan du søke om å få pensjonen din forlenget med opptil to år.",
                    Nynorsk to "Om du framleis tar utdanning eller har arbeidstrening frå 1. januar 2027, kan du søkje om å få pensjonen din forlenga med inntil to år.",
                )
            }

            title2 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "Har du helseutfordringar?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale NAV-kontor og på $HELSE_URL.",
                    Nynorsk to "Viss du har helseutfordringar, kan du undersøkje om du kan få andre ytingar eller støtteordningar ved det lokale NAV-kontoret ditt og på $HELSE_URL.",
                )
            }

            title2 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "Meld frå om endringar",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til NAV.",
                    Nynorsk to "Får du endra inntekta di, familie- eller jobbsituasjonen din? Eller har du planar om å flytte til eit anna land? Meld i så fall frå til NAV straks sidan det kan verke inn på attlevandepensjonen din.",
                )
            }
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på $GJENLEVENDEPENSJON_URL.",
                    Nynorsk to "Du finn meir informasjon på $GJENLEVENDEPENSJON_URL.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON hverdager $NAV_KONTAKTSENTER_AAPNINGSTID.",
                    Nynorsk to "Dersom du ikkje finn svar på $NAV_URL, kan du ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar kl. $NAV_KONTAKTSENTER_AAPNINGSTID.",
                )
            }
        }
    }
}
