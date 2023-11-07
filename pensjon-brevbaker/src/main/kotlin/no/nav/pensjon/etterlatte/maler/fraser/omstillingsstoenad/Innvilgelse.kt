package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.datoTOM
import no.nav.pensjon.etterlatte.maler.BeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTO
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object Innvilgelse {

    data class BegrunnelseForVedtaket(
        val virkningsdato: Expression<LocalDate>,
        val avdoed: Expression<Avdoed>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "",
                    English to "",
                )
            }

            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                textExpr(
                    Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoed.navn + " døde " + formatertDoedsdato + ".",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }

    object BegrunnelseForVedtaketRedigerbart : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            paragraph {
                text(
                    Bokmal to "(utfall jamfør tekstbibliotek)",
                    Nynorsk to "",
                    English to "",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § <riktig paragrafhenvisning> og § 22-12.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class Utbetaling(
        val beregningsperioder: Expression<List<Beregningsperiode>>,
        val etterbetalingDTO: Expression<EtterbetalingDTO?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Utbetaling av omstillingsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                table(
                    header = {
                        column(2) {
                            text(Bokmal to "Periode", Nynorsk to "", English to "")
                        }
                        column(2) {
                            text(
                                Bokmal to "Utbetaling per måned før skatt",
                                Nynorsk to "",
                                English to "",
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
                    Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. Du finner utbetalingsdatoer på ${Constants.UTBETALINGSDATOER_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon om hvordan vi har beregnet omstillingsstønaden din i vedlegget “Beregning av omstillingsstønad”.",
                    Nynorsk to "",
                    English to "",
                )
            }
            ifNotNull(etterbetalingDTO) {
                paragraph {
                    text(
                        Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker. Mer informasjon om perioder og etterbetaling finnes i vedleggene \"Beregning av omstillingsstønad\" og \"Etterbetaling av omstillingsstønad\".",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }
}
