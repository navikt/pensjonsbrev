package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object BarnepensjonInnvilgelseFraser {

    data class UtbetalingAvBarnepensjon(
        val beregningsperioder: Expression<List<Beregningsperiode>>,
        val etterbetalingDTO: Expression<EtterbetalingDTO?>,
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
            paragraph {
                text(
                    Language.Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            ifNotNull(etterbetalingDTO) {
                paragraph {
                    text(
                        Language.Bokmal to "Du finner mer informasjon om etterbetaling i vedlegget «Etterbetaling av barnepensjon».",
                        Language.Nynorsk to "Du kan lese meir om etterbetaling i vedlegget «Etterbetaling av barnepensjon».",
                        Language.English to "You can find more information about back payments in the attachment Back Payments for Children's Pension.",
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Se hvordan vi har beregnet barnepensjonen din i vedlegget “Beregning av barnepensjon.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
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
                            "hverdager 9-15. ",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr()
                )
                showIf(brukerUnder18Aar) {
                    text(
                        Language.Bokmal to "Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                        Language.Nynorsk to "",
                        Language.English to ""
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Om du oppgir fødselsnummeret ditt, kan vi lettere gi deg rask og god hjelp.",
                        Language.Nynorsk to "",
                        Language.English to ""
                    )
                }
            }
        }
    }
}
