package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBUxx2V. "Slik beregner vi uføretrygden din" for konvertert uførepensjon.
 * Hele frasen inkluderes naar [Visningsflagg.visBrukerKonvertertUP]; interne brevkode-betingelser
 * er erstattet med flagg.
 */
data class TBUxx2V(
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Slik beregner vi uføretrygden din" },
                nynorsk { + "Slik bereknar vi uføretrygda di" },
            )
        }
        paragraph {
            text(
                bokmal { + "Uførepensjonen din har tidligere blitt regnet om til uføretrygd og er justert ut fra trygdetid og uføregrad." },
                nynorsk { + "Uførepensjonen din har tidligare blitt rekna om til uføretrygd og er justert ut frå trygdetid og uføregrad." },
            )
        }

        showIf(flagg.visEndringBeregningsgrunnlag) {
            paragraph {
                text(
                    bokmal { + "Når uføretrygden din endres, kan dette medføre at beregningsgrunnlaget har blitt endret." },
                    nynorsk { + "Når uføretrygda di blir endra, kan dette føre til at berekningsgrunnlaget har blitt endra." },
                )
                showIf(flagg.visEndringGjelderGjenlevende) {
                    text(
                        bokmal { + " Dette gjelder også for gjenlevendetillegget du mottar i uføretrygden." },
                        nynorsk { + " Dette gjeld også for attlevandetillegget du får i uføretrygda di." },
                    )
                }
            }
        }

        showIf(flagg.visOektUfoeregrad) {
            paragraph {
                text(
                    bokmal { + "Når uføregraden din øker, sammenligner vi det tidligere beregningsgrunnlaget på uførepensjonen med beregningsgrunnlaget på uføretrygd. Du får alternativet som gir deg høyest uføretrygd." },
                    nynorsk { + "Når uføregraden din aukar, samanliknar vi det tidligare berekningsgrunnlaget på uførepensjonen med berekningsgrunnlaget på uføretrygd. Du får det alternativet som gjer deg høgast uføretrygd." },
                )
                showIf(flagg.visGjenlevendetilleggOekerMedGrad) {
                    text(
                        bokmal { + " Gjenlevendetillegget ditt vil øke med samme grad som uføregraden din." },
                        nynorsk { + " Attlevandetillegget ditt vil auke med same grad som uføregraden din." },
                    )
                }
            }
        }
    }
}
