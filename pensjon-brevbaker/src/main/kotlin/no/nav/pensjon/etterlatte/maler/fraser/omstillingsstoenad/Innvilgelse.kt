package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
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
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import java.time.LocalDate

object Innvilgelse {

    data class BegrunnelseForVedtaket(
        val virkningsdato: Expression<LocalDate>,
        val avdoed: Expression<Avdoed>,
        val utbetalingsbeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "",
                    English to "",
                )
            }
            val formatertVirkningsdato = virkningsdato.format()
            paragraph {
                textExpr(
                    Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoed.navn + " døde " + avdoed.doedsdato.format() + ". Du får " +
                            utbetalingsbeloep.format() + " kroner i stønad hver måned før skatt. " +
                            "(Du får ikke utbetalt omstillingsstønad fordi inntekt er for høy).",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Gift i minst 5 år
            paragraph {
                text(
                    Bokmal to "GIFT I MINST 5 ÅR",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får omstillingsstønad fordi du har vært gift med ".expr() + avdoed.navn +
                            " i minst fem år. Avdøde har vært medlem i folketrygden, eller mottatt pensjon eller " +
                            "uføretrygd fra folketrygden de siste fem årene før dødsfallet.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Gift og har felles barn
            paragraph {
                text(
                    Bokmal to "GIFT OG HAR FELLES BARN",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får omstillingsstønad fordi du har vært gift og har felles barn med ".expr() +
                            avdoed.navn + ". Avdøde har vært medlem i folketrygden, eller mottatt pensjon eller " +
                            "uføretrygd fra folketrygden de siste fem årene før dødsfallet.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Samboere og felles barn
            paragraph {
                text(
                    Bokmal to "SAMBOERE OG FELLES BARN",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får omstillingsstønad fordi du var samboer og har felles barn med ".expr() +
                            avdoed.navn + " på tidspunktet for dødsfallet. Avdøde har vært medlem i folketrygden, " +
                            "eller mottatt pensjon eller uføretrygd fra folketrygden de siste fem årene før dødsfallet.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Omsorg for barn -  Gift under 5 år - ingen felles barn
            paragraph {
                text(
                    Bokmal to "OMSORG FOR BARN - GIFT UNDER 5 ÅR - INGEN FELLES BARN",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får omstillingsstønad fordi du var gift med ".expr() + avdoed.navn +
                            " og har omsorg for barn under 18 år med minst halvparten av full tid på dødstidspunktet. " +
                            "Avdøde har vært medlem i folketrygden, eller mottatt pensjon eller uføretrygd fra " +
                            "folketrygden de siste fem årene før dødsfallet.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Omsorg for barn – Samboer med tidligere ektefelle/felles barn
            paragraph {
                text(
                    Bokmal to "OMSORG FOR BARN - SAMBOER MED TIDLIGERE EKTEFELLE/FELLES BARN",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får omstillingsstønad fordi du var samboer med ".expr() + avdoed.navn +
                            " som du tidligere har [vært gift med/hatt barn og bodd sammen med], og fordi du " +
                            "på dødstidspunktet har omsorg for barn under 18 år med minst halvparten av full tid.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Fraskilt – Ekteskap varte i minst 15 år og felles barn
            paragraph {
                text(
                    Bokmal to "FRASKILT - EKTESKAP VARTE I MINST 15 ÅR OG FELLES BARN",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får omstillingsstønad fordi du var gift med ".expr() + avdoed.navn +
                            " i minst 15 år, hadde felles barn og du har vært [fritekst: helt eller delvis] " +
                            "forsørget av bidrag fra avdøde. Avdøde har vært medlem i folketrygden, " +
                            "eller mottatt pensjon eller uføretrygd fra folketrygden de siste fem årene før dødsfallet.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            // Skilt – Ekteskapet varte i minst 25 år
            paragraph {
                text(
                    Bokmal to "SKILT - EKTESKAPET VARTE I MINST 25 ÅR",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får om stillingsstønad fordi du var gift med ".expr() + avdoed.navn +
                            " i minst 25 år og du har vært [fritekst: helt eller delvis] forsørget av bidrag " +
                            "fra avdøde. Avdøde har vært medlem i folketrygden, eller mottatt pensjon eller " +
                            "uføretrygd fra folketrygden de siste fem årene før dødsfallet.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

        }
    }
}
