package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text


data class TBU2339_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Inntekten til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " },
                nynorsk { + "Inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert. " },
                english { + "The incomes of you and your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. " },
            )

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0))) {
                text(
                    bokmal { + "Denne grensen kaller vi for fribeløp. " },
                    nynorsk { + "Denne grensa kallar vi for fribeløp. " },
                    english { + "We call this limit the exemption amount. " },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                text(
                    bokmal { + "Inntekten til  " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din har kun betydning for størrelsen på barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor sammen med begge sine foreldre." },
                    nynorsk { + "Inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din har berre betydning for storleiken på barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                    english { + "The income of your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " only affects the size of the child supplement for the " + pe.ut_barnet_barna_felles() + " who live" + pe.ut_barnet_barna_felles_en_entall_flertall() + " together with both parents. " },
                )
            }
        }
    }
}
        