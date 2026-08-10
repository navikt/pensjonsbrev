package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text

data class SlikBeregnerViUfoeretrygdenKonvertert(
    val pe: Expression<PEgruppe10>,
): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        showIf((pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){
            title1 {
                text (
                    bokmal { + "Slik beregner vi uføretrygden din" },
                    nynorsk { + "Slik bereknar vi uføretrygda di" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Uførepensjonen din har tidligere blitt regnet om til uføretrygd og er justert ut fra trygdetid og uføregrad." },
                    nynorsk { + "Uførepensjonen din har tidligare blitt rekna om til uføretrygd og er justert ut frå trygdetid og uføregrad." },
                )
            }

            showIf((pe.pebrevkode().equalTo("PE_UT_04_102"))){
                paragraph {
                    text (
                        bokmal { + "Når uføretrygden din endres, kan dette medføre at beregningsgrunnlaget har blitt endret." },
                        nynorsk { + "Når uføretrygda di blir endra, kan dette føre til at berekningsgrunnlaget har blitt endra." },
                    )

                    showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            bokmal { + " Dette gjelder også for gjenlevendetillegget du mottar i uføretrygden." },
                            nynorsk { + " Dette gjeld også for attlevandetillegget du får i uføretrygda di." },

                        )
                    }
                }
            }

            showIf((pe.pebrevkode().equalTo("PE_UT_04_114"))){
                paragraph {
                    text (
                        bokmal { + "Når uføregraden din øker, sammenligner vi det tidligere beregningsgrunnlaget på uførepensjonen med beregningsgrunnlaget på uføretrygd. Du får alternativet som gir deg høyest uføretrygd." },
                        nynorsk { + "Når uføregraden din aukar, samanliknar vi det tidligare berekningsgrunnlaget på uførepensjonen med berekningsgrunnlaget på uføretrygd. Du får det alternativet som gjer deg høgast uføretrygd." },
                    )

                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget())){
                        text (
                            bokmal { + " Gjenlevendetillegget ditt vil øke med samme grad som uføregraden din." },
                            nynorsk { + " Attlevandetillegget ditt vil auke med same grad som uføregraden din." },
                        )
                    }
                }
            }
        }
    }

}
