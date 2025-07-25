package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AdhocAlderspensjonGjtOppryddingAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AdhocAlderspensjonGjtOppryddingAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocAlderspensjonGjtOpprydding : AutobrevTemplate<AdhocAlderspensjonGjtOppryddingAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2025_OPPRYDDING_GJT_KAP_20

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AdhocAlderspensjonGjtOppryddingAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har beregnet for høyt gjenlevendetillegg",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
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
                    Bokmal to "Du har tidligere fått brev om feil i beregningen av gjenlevendetillegget ditt. " +
                            "Det har vært satt for høyt, og vi har nå rettet opp i dette.  ",
                    Nynorsk to "Du har tidlegare fått brev om feil i berekninga av attlevandetillegget ditt. " +
                            "Det har vore sett for høgt, og vi har no retta opp i dette.",
                    English to "You have previously received a letter regarding an error in the calculation of the survivor’s " +
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
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-19a.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-19a.",
                    English to "This decision was made pursuant to the provision of § 20-19a of the National Insurance Act.",
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

            includePhrase(Felles.RettTilAAKlage(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
            includePhrase(Felles.RettTilInnsyn(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))

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
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }
}
