package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Avkortning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.avkortning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres.
 * Gates av [Visningsflagg.visUtbetalingNaarInntektEndres] (intro) og
 * [Visningsflagg.visUtbetalingNaarInntektEndresDetaljer] (detaljblokkene).
 */
data class TBU052V_TBU073V_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(
    val flagg: Expression<Visningsflagg>,
    val avkortning: Expression<Avkortning>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.visUtbetalingNaarInntektEndres) {
            title1 {
                text(
                    bokmal { + "Slik beregner vi utbetaling av uføretrygden når inntekten din endres" },
                    nynorsk { + "Slik bereknar vi utbetaling av uføretrygda når inntekta di er endra" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Utbetalingen av uføretrygden din er beregnet på nytt, fordi inntekten din er endret. Det er den innmeldte inntekten din og uføretrygden du har fått utbetalt hittil i år, som avgjør hvor mye du får utbetalt i de månedene som er igjen i kalenderåret." },
                    nynorsk { + "Utbetalinga av uføretrygda di er berekna på nytt fordi inntekta di er endra. Det er den innmelde inntekta di og uføretrygda du har fått utbetalt hittil i år, som avgjer kor mykje du får utbetalt i dei månadene som er att av kalenderåret." },
                )
            }
        }

        showIf(flagg.visUtbetalingNaarInntektEndresDetaljer) {
            paragraph {
                text(
                    bokmal { + "Uføretrygden reduseres med " + avkortning.kompensasjonsgrad.format() + " prosent av inntekten over " + avkortning.inntektsgrenseAar.format() + " fordi du har en reduksjonsprosent som er " + avkortning.kompensasjonsgrad.format() + " prosent." },
                    nynorsk { + "Uføretrygda blir redusert med " + avkortning.kompensasjonsgrad.format() + " prosent av inntekta over " + avkortning.inntektsgrenseAar.format() + " fordi du har ein reduksjonsprosent som er " + avkortning.kompensasjonsgrad.format() + " prosent." },
                )
            }
            ifNotNull(avkortning.overskytendeInntekt) { overskytende ->
                paragraph {
                    text(
                        bokmal { + "Du har en inntektsgrense på " + avkortning.inntektsgrenseAar.format() + " og den innmeldte inntekten din er " + avkortning.forventetInntektAar.format() + ". Dette betyr at overskytende inntekt er " + overskytende.format() + "." },
                        nynorsk { + "Du har ei inntektsgrense på " + avkortning.inntektsgrenseAar.format() + ", og den innmelde inntekta di er " + avkortning.forventetInntektAar.format() + ". Dette vil seie at overskytande inntekt er " + overskytende.format() + "." },
                    )
                }
            }
        }
    }
}
