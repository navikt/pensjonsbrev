package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.fraser.common.Constants.ARBEID_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.GJENLEVENDEPENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.GJENLEVENDE_TILLEGGSSTOENADER_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.HELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object GjenlevendeInfoEtter1970 : AutobrevTemplate<Unit> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_ADHOC_2023_04_GJENLEVENDEINFOETTER1970

    override val template = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Gjenlevendepensjonen din blir tidsbegrenset",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Gjenlevendepensjonen din blir tidsbegrenset",
                Nynorsk to "Attlevandepensjonen din blir tidsavgrensa",
                English to "Your survivor's pension will be time-limited",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for gjenlevendepensjon fra folketrygden fra 1. januar 2024.",
                    Nynorsk to "Stortinget har vedtatt nye reglar for attlevandepensjon frå folketrygda frå 1. januar 2024.",
                    English to "The Norwegian Parliament has adopted new rules regarding the survivor's pension through the National Insurance Act from 1 January 2024.",
                )
            }

            title2 {
                text(
                    Bokmal to "Hva betyr endringene for deg?",
                    Nynorsk to "Kva betyr endringane for deg?",
                    English to "What do the changes mean for you?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du beholder gjenlevendepensjonen som den er i dag, men du får den bare i tre år fra 1. januar 2024.",
                    Nynorsk to "Du vil få attlevandepensjonen som den er i dag, men du får den berre i tre år frå 1. januar 2024.",
                    English to "You will keep the survivor's pension as it is today, but you will only receive it for three years as of 1 January 2024.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Siste utbetaling av pensjonen din blir i desember 2026. Det kan bety at du får mindre å leve av.",
                    Nynorsk to "Den siste utbetalinga av pensjonen din blir i desember 2026. Det kan bety at du får mindre å leve av.",
                    English to "The last payment of your survivor's pension will be in December 2026. That may mean you will have less money to live on.",
                )
            }

            title2 {
                text(
                    Bokmal to "Trenger du hjelp til å få ny jobb eller jobbe mer?",
                    Nynorsk to "Treng du hjelp til å få ny jobb eller jobb meir?",
                    English to "Do you need help getting a new job or more work?",
                )
            }

            paragraph {
                text(
                    Bokmal to "NAV tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. Du kan finne jobbsøkertips og lenke til ledige jobber på $ARBEID_URL.",
                    Nynorsk to "NAV tilbyr ulike tenester og støtteordningar for deg som treng hjelp til å få jobb. Du kan finne jobbsøkjartips og lenkje til ledige jobbar på $ARBEID_URL.",
                    English to "The Norwegian Labour and Welfare Administration (NAV) offers various services and support schemes for those who need help finding a job. You can find job search tips and a hyperlink to open jobs online at $ARBEID_URL.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan snakke med deg om ulike muligheter i arbeidsmarkedet, utdanning eller andre tiltak.",
                    Nynorsk to "Vi kan snakke med deg om ulike moglegheiter i arbeidsmarknaden, utdanning eller andre tiltak.",
                    English to "We are available for a conversation about the various opportunities available to you in the labour market, education, or other initiatives.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du tar en utdanning som NAV mener er nødvendig for at du skal kunne få jobb og forsørge deg selv, kan du få tilleggsstønader. Det samme gjelder hvis du er registrert som arbeidssøker hos NAV. Du finner mer informasjon om disse stønadene på $GJENLEVENDE_TILLEGGSSTOENADER_URL.",
                    Nynorsk to "Viss du tar utdanning som NAV meiner er naudsynt for at du skal få jobb og forsørge deg sjølv, kan du få tilleggsstønader. Det same gjeld om du er registrert som arbeidssøkjar hos NAV. Du finn meir informasjon om desse stønadene på $GJENLEVENDE_TILLEGGSSTOENADER_URL.",
                    English to "If you are undertaking an education that NAV means is necessary for you to get a job and support yourself, you may get additional benefits. The same applies if you are registered as a jobseeker with NAV. You can find more information about these benefits at $GJENLEVENDE_TILLEGGSSTOENADER_URL.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du fortsatt tar nødvendig utdanning eller trenger hjelp til å få jobb fra 1. januar 2027, kan du søke om å få pensjonen din forlenget med opptil to år.",
                    Nynorsk to "Om du framleis tar naudsynt utdanning eller treng hjelp til å få jobb frå 1. januar 2027, kan du søkje om å få pensjonen din forlenga med inntil to år.",
                    English to "If you are still undertaking an education or require help to find a job as of 1 January 2027, you can apply to have your pension extended by up to two years.",
                )
            }

            title2 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "Har du helseutfordringar?",
                    English to "Do you have health issues?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser eller støtteordninger ved ditt lokale NAV-kontor og på $HELSE_URL.",
                    Nynorsk to "Viss du har helseutfordringar, kan du undersøkje om du kan få andre ytingar eller støtteordningar ved det lokale NAV-kontoret ditt og på $HELSE_URL.",
                    English to "If you have health issues, you can check if you are eligible for other benefits or support schemes through your local NAV office or see $HELSE_URL.",
                )
            }

            title2 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "Meld frå om endringar",
                    English to "Notify NAV about changes",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til NAV.",
                    Nynorsk to "Får du endra inntekta di, familie- eller jobbsituasjonen din? Eller har du planar om å flytte til eit anna land? Meld i så fall frå til NAV straks sidan det kan verke inn på attlevandepensjonen din.",
                    English to "If you have experienced a change in income, changes to your family or job situation, or plan to move to another country, this may affect your survivor's pension. You are obligated to notify NAV as soon as you are aware of any of these changes.",
                )
            }
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på $GJENLEVENDEPENSJON_URL.",
                    Nynorsk to "Du finn meir informasjon på $GJENLEVENDEPENSJON_URL.",
                    English to "You can find more information at $GJENLEVENDEPENSJON_URL.",
                )
                newline()
                text(
                    Bokmal to "På $KONTAKT_URL kan du chatte eller skrive til oss.",
                    Nynorsk to "På $KONTAKT_URL kan du chatte eller skrive til oss.",
                    English to "At $KONTAKT_URL you can chat or write to us.",
                )
                newline()
                text(
                    Bokmal to "Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager $NAV_KONTAKTSENTER_AAPNINGSTID.",
                    Nynorsk to "Om du ikkje finn svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar $NAV_KONTAKTSENTER_AAPNINGSTID.",
                    English to "If you do not find the answer at $NAV_URL, you can call us at: +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON, weekdays $NAV_KONTAKTSENTER_AAPNINGSTID.",
                )
            }
        }
    }
}
