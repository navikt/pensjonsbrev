package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.ufore.extensions.bokmalUbestemtEntall
import no.nav.pensjon.brev.maler.ufore.extensions.engelskUbestemtEntall
import no.nav.pensjon.brev.maler.ufore.extensions.nynorskBestemtEntall
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


data class TBU2339_Generated(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " },
                nynorsk { + "Inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert. " },
            )

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0))) {
                text(
                    bokmal { + "Denne grensen kaller vi for fribeløp. " },
                    nynorsk { + "Denne grensa kallar vi for fribeløp. " },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                text(
                    bokmal { + "Inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " har kun betydning for størrelsen på barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor sammen med begge sine foreldre." },
                    nynorsk { + "Inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har berre betydning for storleiken på barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                )
            }
        }
    }
}

data class TBU2339_Generated_Ny(
    val btfbNetto: Expression<Kroner>,
    val btsbInnvilget: Expression<Boolean>,
    val btfbInnvilget: Expression<Boolean>,
    val btMedFlereBarn: Expression<Boolean>,
    val epsBorMed: Expression<Grunnlag.Eps?>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Inntekten til deg og din " + epsBorMed.safe { bokmalUbestemtEntall() }.ifNull("") + " har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " },
                nynorsk { + "Inntekta til deg og " + epsBorMed.safe { nynorskBestemtEntall() }.ifNull("") + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert. " },
            )

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) THEN      INCLUDE ENDIF
            showIf((btfbNetto.notEqualTo(0))) {
                text(
                    bokmal { + "Denne grensen kaller vi for fribeløp. " },
                    nynorsk { + "Denne grensa kallar vi for fribeløp. " },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf((btsbInnvilget and btfbInnvilget)) {
                text(
                    bokmal { + "Inntekten til din " + epsBorMed.safe { bokmalUbestemtEntall() }.ifNull("") + " har kun betydning for størrelsen på barnetillegget til " + ifElse(btMedFlereBarn, "barna", "barnet") + " som bor sammen med begge sine foreldre." },
                    nynorsk { + "Inntekta til " + epsBorMed.safe { nynorskBestemtEntall() }.ifNull("") + " din har berre betydning for storleiken på barnetillegget til " + ifElse(btMedFlereBarn, "barna", "barnet") + " som bur saman med begge foreldra sine. " },
                )
            }
        }
    }
}
