package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonGjtOppryddingDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonGjtOppryddingDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonGjtOppryddingDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocAlderspensjonGjtOpprydding : AutobrevTemplate<AlderspensjonGjtOppryddingDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2025_OPPRYDDING_GJT_KAP_20

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AlderspensjonGjtOppryddingDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har beregnet for høyt gjenlevendetillegg",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Gjenlevendetillegget i alderspensjonen din blir mindre",
                Nynorsk to "Attlevandetillegget i alderspensjonen din blir mindre",
                English to "The survivor's supplement in your retirement pension will be reduced"
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak",
                    English to "Decision"
                )
            }
            paragraph {
                text(
                    Bokmal to "I april fikk du brev om feil i beregningen av gjenlevendetillegget i pensjonen din. " +
                            "Det har vært satt for høyt, og vi har nå rettet opp i dette.  ",
                    Nynorsk to "I april fekk du brev om feil i berekninga av attlevandetillegget i pensjonen din. " +
                            "Det har vore sett for høgt, og vi har no retta opp i dette.",
                    English to "In April, you received a letter regarding an error in the calculation of the survivor’s " +
                            "supplement in your pension. It had been set too high, but we have now corrected it.",
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner i alderspensjon før skatt fra " +
                            virkFom.format() + ".",
                    Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner i alderspensjon før skatt frå " +
                            virkFom.format() + ".",
                    English to "You will receive NOK ".expr() + totalPensjon.format() + " in retirement pension before tax from " +
                            virkFom.format() + ".",
                )
            }

            paragraph {
                text(
                    Bokmal to "Nav vil ikke kreve tilbake det du har fått for mye utbetalt.",
                    Nynorsk to "Nav vil ikkje krevje tilbake det du har fått for mykje utbetalt.",
                    English to "Nav will not claim repayment of the amount that was overpaid.",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva har skjedd?",
                    Nynorsk to "Kva har skjedd?",
                    English to "What has happened?"
                )
            }

            paragraph {
                text(
                    Bokmal to "Du får et gjenlevendetillegg i den delen av alderspensjonen din som beregnes etter nye " +
                            "regler (kapittel 20 i folketrygdloven). Du har tidligere fått et vedtak med informasjon om " +
                            "at dette tillegget gradvis skal fases ut. Det skjer ved at tillegget reduseres like mye som " +
                            "alderspensjonen din øker ved den årlige reguleringen. Gjenlevendetillegget vil derfor bli " +
                            "lavere hvert år og til slutt bli helt borte.",
                    Nynorsk to "Du får eit attlevandetillegg i den delen av alderspensjonen din som blir berekna etter " +
                            "nye reglar (kapittel 20 i folketrygdlova). Du har tidlegare fått eit vedtak med informasjon " +
                            "om at dette tillegget gradvis skal fasast ut. Det skjer ved at tillegget blir redusert like " +
                            "mykje som alderspensjonen din aukar ved den årlege reguleringa. Attlevandetillegget vil difor " +
                            "bli lågare kvart år og til slutt bli heilt borte.",
                    English to "You receive a survivor's supplement in the part of your retirement pension that is " +
                            "calculated according to new rules (chapter 20 of the National Insurance Act). " +
                            "You have previously received a decision with information that this supplement will be " +
                            "gradually phased out. This is done by reducing the supplement by an amount equal to the " +
                            "annual increase in your retirement pension. The survivor's supplement will therefore become " +
                            "lower each year and will eventually disappear completely. ",
                )
            }

            paragraph {
                text(
                    Bokmal to "I reguleringen mai 2024 ble tillegget ditt redusert, men ved en feil har det senere økt " +
                            "igjen. Det betyr at du har fått for mye utbetalt pensjon. Vi vil nå rette opp feilen og " +
                            "justere pensjonen din til riktig beløp.",
                    Nynorsk to "I reguleringa mai 2024 vart tillegget ditt redusert, men ved ein feil har det seinare " +
                            "auka att. Det betyr at du har fått for mykje utbetalt pensjon. Vi vil no rette opp feilen " +
                            "og justere pensjonen din til rett beløp.",
                    English to "In the adjustment in May 2024, your supplement was reduced, but due to an error it has " +
                            "later increased again. This means that you have been paid too much pension. We will now " +
                            "correct the error and adjust your pension to the correct amount.",
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right to appeal",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                            "Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.",
                    Nynorsk to "Om du meiner vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. " +
                            "Klagen skal vere skriftleg. Du finn skjema og informasjon på nav.no/klage.",
                    English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on " +
                            "which you received notice of the decision. Your appeal must be made in writing. You will find a " +
                            "form you can use and more information about appeals at nav.no/klage.",
                )
            }

            title1 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to "You have the right to access your file",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di.",
                    English to "You have the right to access all documents pertaining to your case.",
                )
            }

            paragraph {
                text(
                    Bokmal to "I vedlegget ",
                    Nynorsk to "I vedlegget ",
                    English to "The attachment "
                )
                namedReference(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
                text(
                    Bokmal to " finner du mer informasjon om klage, innsyn og hvordan du går fram.",
                    Nynorsk to " finn du meir informasjon om klage, innsyn og korleis du går fram.",
                    English to " includes information on how to proceed.",
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/pensjon. På nav.no/kontakt kan du chatte eller skrive til oss. " +
                            "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon " +
                            "+47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, hverdager kl. 09.00-15.00.",
                    Nynorsk to "Du finn meir informasjon på nav.no/pensjon. På nav.no/kontakt kan du chatte eller skrive til oss. " +
                            "Om du ikkje finn svar på nav.no, kan du ringe oss på telefon " +
                            "+47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, kvardagar kl. 09.00-15.00.",
                    English to "You can find more information at nav.no/pensjon. At nav.no/kontakt, you can chat or write to us. " +
                            "If you do not find the answer at nav.no, you can call us at: " +
                            "+47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, weekdays 09:00-15:00.",
                )
            }

            includePhrase(Felles.RettTilAAKlage(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
        }
    }
}
