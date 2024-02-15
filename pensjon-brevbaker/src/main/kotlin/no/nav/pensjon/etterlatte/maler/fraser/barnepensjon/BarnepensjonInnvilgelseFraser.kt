package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object BarnepensjonInnvilgelseFraser {

    data class Foerstegangsbehandlingsvedtak(
        val avdoed: Expression<Avdoed>,
        val virkningsdato: Expression<LocalDate>,
        val sisteBeregningsperiodeDatoFom: Expression<LocalDate>,
        val sisteBeregningsperiodeBeloep: Expression<Kroner>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                val formatertNyesteUtbetalingsperiodeDatoFom = sisteBeregningsperiodeDatoFom.format()
                val formatertBeloep = sisteBeregningsperiodeBeloep.format()
                val avdoedNavn = avdoed.navn

                textExpr(
                    Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                    Language.Nynorsk to "Du er innvilga barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". ",
                    Language.English to "You have been granted a children's pension ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on "+ formatertDoedsdato + ". "
                )

                showIf(harFlereUtbetalingsperioder) {
                    textExpr(
                        Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se utbetalingsbeløp for tidligere perioder i vedlegg om etterbetaling.",
                        Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt frå og med " + formatertNyesteUtbetalingsperiodeDatoFom + ". Sjå utbetalingsbeløp for tidlegare periodar i vedlegget om etterbetaling.",
                        Language.English to "You will receive ".expr() + formatertBeloep + " kroner each month before tax starting on " + formatertNyesteUtbetalingsperiodeDatoFom + ". See the payment amount for previous periods in the Back Payment Attachment."
                    )
                }.orShow {
                    textExpr(
                        Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt.",
                        Language.Nynorsk to "Du får ".expr() + formatertBeloep + " kroner per månad før skatt.",
                        Language.English to "You will receive NOK ".expr() + formatertBeloep + " each month before tax."
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjonen utbetales til og med den kalendermåneden du fyller 20 år.",
                    Language.Nynorsk to "Barnepensjonen blir utbetalt fram til og med kalendermånaden du fyller 20 år.",
                    Language.English to "The children’s pension is paid up to and including the calendar month in which you turn 20.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon gis på bakgrunn av at du er medlem i folketrygden og at avdøde i de siste fem årene før dødsfallet var medlem i folketrygden eller fikk pensjon fra folketrygden.",
                    Language.Nynorsk to "Barnepensjon blir gitt på bakgrunn av at du er medlem i folketrygda, og at avdøde var medlem i eller fekk pensjon frå folketrygda dei siste fem åra før sin død.",
                    Language.English to "You are eligible for a children's pension because you are a member of the Norwegian National Insurance Scheme, and the deceased has been a member of the National Insurance Scheme in the five years prior to the death or he/she has been receiving a pension from the Scheme.",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova §§ 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections 18-2, 18-3, 18-4, 18-5".expr() + ifElse(erEtterbetaling, ", 22-12 and 22-13.", " and 22-12."),
                )
            }
        }
    }

    data class UtbetalingAvBarnepensjon(
        val etterbetaling: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Utbetaling av barnepensjon",
                    Language.Nynorsk to "Utbetaling av barnepensjon",
                    Language.English to "Payment of the children's pension",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. Du finner utbetalingsdatoer på ${Constants.UTBETALINGSDATOER_URL}.",
                    Language.Nynorsk to "Pensjonen blir utbetalt innan den 20. kvar månad. Du finn utbetalingsdatoane på ${Constants.UTBETALINGSDATOER_URL}.",
                    Language.English to "The pension is paid by the 20th of each month. You can find payout dates online: ${Constants.Engelsk.UTBETALINGSDATOER_URL}.",
                )
            }
            showIf(etterbetaling) {
                paragraph {
                    text(
                        Language.Bokmal to "Du får etterbetalt pensjon. Vanligvis vil du få denne i løpet av " +
                                "tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan " +
                                "denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Det trekkes vanligvis skatt av etterbetaling. Gjelder " +
                                "etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens standardsatser. " +
                                "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Language.Nynorsk to "Det blir normalt sett bli trekt skatt av etterbetaling. Dersom " +
                                "etterbetalinga gjeld tidlegare år, vil NAV trekkje skatt etter standardsatsane til " +
                                "Skatteetaten. Du kan lese meir om satsane på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Language.English to "Tax is usually deducted from back payments. If the back payment " +
                                "applies to previous years, NAV will deduct the tax at the Tax Administration's " +
                                "standard rates. You can read more about the rates here: " +
                                "${Constants.SKATTETREKK_ETTERBETALING_URL}. ",
                    )
                }
            }
        }
    }

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du må melde fra om endringer",
                    Language.Nynorsk to "Du må melde frå om endringar",
                    Language.English to "You must report changes",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for utbetalingen av barnepensjon, eller retten til å få barnepensjon. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Language.Nynorsk to "Du pliktar å melde frå til oss om endringar som påverkar utbetalinga di av barnepensjon eller retten på barnepensjon. I vedlegget «Rettane og pliktene dine» ser du kva endringar du må seie frå om.",
                    Language.English to "You are obligated to notify us any of changes that affect the payment of a children's pension, or the right to receive a children's pension. You will see which changes you must report in the attachment Your Rights and Obligations.",
                )
            }
        }
    }

    object DuHarRettTilAaKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                    Language.Nynorsk to "Du har rett til å klage",
                    Language.English to "You have the right to appeal",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå datoen du fekk vedtaket. Klaga må vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.English to "If you believe the decision is incorrect, you may appeal the decision within six weeks from the date you received the decision. The appeal must be in writing. You can find the form and information online: ${Constants.Engelsk.KLAGE_URL}",
                )
            }
        }
    }

    data class HarDuSpoersmaal(val brukerUnder18Aar: Expression<Boolean>, val bosattUtland: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val kontaktTelefon = ifElse(bosattUtland, Constants.Utland.KONTAKTTELEFON_PENSJON, Constants.KONTAKTTELEFON_PENSJON)

            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "Har du spørsmål?",
                    Language.English to "Any questions?"
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Du finner mer informasjon på ".expr() + Constants.BARNEPENSJON_URL + ". " +
                            "Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon " + kontaktTelefon + " " +
                            "hverdager mellom kl. 09.00-15.00. ",
                    Language.Nynorsk to "Du finn meir informasjon på ".expr() + Constants.BARNEPENSJON_URL + ". " +
                            "Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon " + kontaktTelefon + " " +
                            ", kvardagar mellom kl. 09.00–15.00.",
                    Language.English to "For more information, visit us online: ".expr() + Constants.Engelsk.BARNEPENSJON_URL + ". " +
                            "If you cannot find the answer to your question, you can call us by phone (" + kontaktTelefon + ") " +
                            "weekdays between 09.00-15.00."
                )
                showIf(brukerUnder18Aar) {
                    text(
                        Language.Bokmal to "Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                        Language.Nynorsk to "Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret til barnet.",
                        Language.English to "If you provide your child's national identity number, we can more easily provide you with quick and good help."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Om du oppgir fødselsnummeret ditt, kan vi lettere gi deg rask og god hjelp.",
                        Language.Nynorsk to "Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummeret ditt.",
                        Language.English to "If you provide your national identity number, we can more easily provide you with quick and good help."
                    )
                }
            }
        }
    }
}
