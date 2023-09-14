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
object GjenlevendeInfoFoer1971 : AutobrevTemplate<Unit> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_ADHOC_2023_04_GJENLEVENDEINFOFOER1971

    override val template = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Gjenlevendepensjonen din kan bli tidsbegrenset",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {

        title {
            text(
                Bokmal to "Gjenlevendepensjonen din kan bli tidsbegrenset",
                Nynorsk to "Attlevandepensjonen din kan bli tidsavgrensa",
                English to "Your survivor's pension may be time-limited",
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
                    Bokmal to "Du beholder gjenlevendepensjonen som den er i dag, men du får den bare i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026.",
                    Nynorsk to "Du vil få attlevandepensjonen som den er i dag, men du får den berre i tre år frå 1. januar 2024. Det kan bety at den siste utbetalinga av pensjonen din blir i desember 2026.",
                    English to "You will keep the survivor's pension as it is today, but you will only receive it for three years as of 1 January 2024. The last payment of your survivor's pension will be in December 2026.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har hatt lav eller ingen arbeidsinntekt de siste fem årene, kan du beholde utbetaling til du blir 67 år.",
                    Nynorsk to "Viss du har hatt låg eller inga arbeidsinntekt dei siste fem åra, kan du få utbetaling til du blir 67 år.",
                    English to "If you have had a low or no employment income during the last five years, you can keep the payments until you are age 67 years."
                )
            }
            title2 {
                text(
                    Bokmal to "Hvem kan beholde utbetaling til 67 år?",
                    Nynorsk to "Kven kan få utbetaling fram til dei er 67 år?",
                    English to "Who can keep the payments to age 67 years?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Det er arbeidsinntekten din i årene 2019-2023 som avgjør om du kan beholde utbetaling.",
                    Nynorsk to "Det er arbeidsinntekta di i åra 2019-2023 som avgjer om du kan behalde utbetaling.",
                    English to "Your income from employment in the years 2019-2023 determine if you can keep the payments."
                )
            }
            paragraph {
                text(
                    Bokmal to "Den gjennomsnittlige arbeidsinntekten din kan ikke være høyere enn to ganger grunnbeløpet i folketrygden i disse årene. I tillegg kan ikke arbeidsinntekten din være høyere enn tre ganger grunnbeløpet i årene 2022 eller 2023. Du kan se en oversikt over hva som regnes som lav inntekt på $GJENLEVENDEPENSJON_URL.",
                    Nynorsk to "Den gjennomsnittlege arbeidsinntekta di kan ikkje vere høgare enn to gonger grunnbeløpet i folketrygda i desse åra. I tillegg kan ikkje arbeidsinntekta di vere høgare enn tre gonger grunnbeløpet i åra 2022 eller 2023. Du kan sjå ei oversikt over kva som blir rekna som låg inntekt på $GJENLEVENDEPENSJON_URL.",
                    English to "Your average employment income cannot be higher than two times the basic National Insurance minimum benefit amount in these years. In addition, your employment income cannot be higher than three times the minimum benefit amount in the years 2022 or 2023. You can see an overview of what is considered as low income at $GJENLEVENDEPENSJON_URL."
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis inntekten din har vært lavere enn dette, beholder du utbetaling av gjenlevendepensjonen din til og med 31. desember 2028. Så vil pensjonen regnes ut på nytt til omstillingsstønad fra 1. januar 2029. Det kan bety at du får en lavere utbetaling fra 2029.",
                    Nynorsk to "Viss inntekta di har vore lågare enn dette, vil du få attlevandepensjonen til og med 31. desember 2028. Så vil pensjonen bli rekna ut på nytt til omstillingsstønad frå 1. januar 2029. Det kan bety at du får lågare utbetaling frå desember 2029.",
                    English to "If your income has been lower than this, you will keep your survivor’s pension payments up to and including 31 December 2028. Thereafter, your pension will be recalculated to an adjustment allowance from 1 January 2029. This can mean that you will receive lower payments in 2029."
                )
            }
            paragraph {
                text(
                    Bokmal to "NAV må vite inntekten din for 2023 for å kunne vurdere om du kan beholde utbetaling til du er 67 år. Vi kan tidligst se om du har rett til utbetaling når tallene fra Skatteetaten er klare. Du vil få et brev om dette mot slutten av 2024.",
                    Nynorsk to "NAV må vite inntekta di for 2023 for å kunne vurdere om du kan få utbetaling til du er 67 år. Vi kan tidlegast sjå om du har rett til utbetaling når tala frå Skatteetaten er klare. Du vil få brev om dette mot slutten av 2024.",
                    English to "NAV must have your income for 2023 in order to evaluate if you can keep the payments until you are age 67 years. We can earliest see if you can be granted the payments when The Norwegian Tax Administration has completed their assessments. You will receive a letter towards the end of 2024."
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
                    Nynorsk to "Viss du tar utdanning som NAV meiner er naudsynt for at du skal få jobb og forsørgje deg sjølv, kan du få tilleggsstønader. Det same gjeld om du er registrert som arbeidssøkjar hos NAV. Du finn meir informasjon om desse stønadene på $GJENLEVENDE_TILLEGGSSTOENADER_URL.",
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

