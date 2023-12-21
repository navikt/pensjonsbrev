package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.utbetaltBeloep
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
                table(
                    header = {
                        column(2) {
                            text(Language.Bokmal to "Periode", Language.Nynorsk to "Periode", Language.English to "Period")
                        }
                        column(2) {
                            text(
                                Language.Bokmal to "Utbetaling per måned før skatt",
                                Language.Nynorsk to "Utbetaling per månad før skatt",
                                Language.English to "Payout per month before taxes",
                            )
                        }
                    }
                ) {
                    forEach(beregningsperioder) {
                        row {
                            cell { includePhrase(Barnepensjon.PeriodeITabell(it.datoFOM, it.datoTOM)) }
                            cell { includePhrase(Felles.KronerText(it.utbetaltBeloep)) }
                        }
                    }
                }
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
                    Language.Bokmal to "Du finner mer informasjon om hvordan vi har beregnet barnepensjonen din i vedlegget «Beregning av barnepensjon».",
                    Language.Nynorsk to "Sjå vedlegget «Utrekning av barnepensjon» for meir informasjon om korleis vi har rekna ut barnepensjonen din.",
                    Language.English to "You can find more information about how we have calculated your children's pension in the attachment Calculating the Children's Pension.",
                )
            }
            ifNotNull(etterbetalingDTO) {
                paragraph {
                    text(
                        Language.Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker. Du finner mer informasjon om etterbetaling i vedlegget «Etterbetaling av barnepensjon».",
                        Language.Nynorsk to "Dersom du har rett på etterbetaling, får du vanlegvis denne i løpet av tre veker. Du kan lese meir om etterbetaling i vedlegget «Etterbetaling av barnepensjon».",
                        Language.English to "If you are entitled to a back payment, you will normally receive this within three weeks. You can find more information about back payments in the attachment Back Payments for Children's Pension.",
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
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen åtte uker fra du mottar dette brevet. " +
                            "Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå datoen du fekk vedtaket. Klaga må vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.English to "If you believe the decision is incorrect, you may appeal the decision within six weeks from the date you received the decision. The appeal must be in writing. You can find the form and information online: ${Constants.Engelsk.KLAGE_URL}",
                )
            }
        }
    }

    object HarDuSpoersmaal : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "Har du spørsmål?",
                    Language.English to "Any questions?"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. " +
                            "Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon ${Constants.KONTAKTTELEFON_PENSJON} " +
                            "hverdager 9-15. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                    Language.Nynorsk to "Du finn meir informasjon på ${Constants.BARNEPENSJON_URL}. " +
                            "Dersom du ikkje finn svar på spørsmålet ditt, kan du ringje oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}, " +
                            "kvardagar 9–15. Viss du oppgir fødselsnummeret til barnet, kan vi enklare gi deg rask og god hjelp.",
                    Language.English to "For more information, visit us online: ${Constants.Engelsk.BARNEPENSJON_URL}. " +
                            "If you cannot find the answer to your question, " +
                            "you can call us by phone (${Constants.KONTAKTTELEFON_PENSJON}) weekdays 9-15. " +
                            "If you provide your child's national identity number, " +
                            "we can more easily provide you with quick and good help."
                )
            }
        }
    }
}
