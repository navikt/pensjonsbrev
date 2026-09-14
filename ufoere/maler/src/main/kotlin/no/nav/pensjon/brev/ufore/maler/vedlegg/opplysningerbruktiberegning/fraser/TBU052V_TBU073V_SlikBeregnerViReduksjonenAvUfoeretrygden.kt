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
 * Portert fra legacy TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden. Viser regnestykket
 * for reduksjonen. Gates av [Visningsflagg.visReduksjonAvUforetrygd].
 */
data class TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden(
    val flagg: Expression<Visningsflagg>,
    val avkortning: Expression<Avkortning>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.visReduksjonAvUforetrygd) {
            title1 {
                text(
                    bokmal { + "Slik beregner vi reduksjonen av uføretrygden" },
                    nynorsk { + "Slik bereknar vi reduksjonen av uføretrygda" },
                )
            }
            ifNotNull(avkortning.overskytendeInntekt, avkortning.reduksjonPerAar) { overskytende, reduksjon ->
                paragraph {
                    text(
                        bokmal { + overskytende.format(false) + " kr" + " x " + avkortning.kompensasjonsgrad.format() + " %" + " = " + reduksjon.format() + " i reduksjon for året" },
                        nynorsk { + overskytende.format(false) + " kr" + " x " + avkortning.kompensasjonsgrad.format() + " %" + " = " + reduksjon.format() + " i reduksjon for året" },
                    )
                }
            }
        }
    }
}
