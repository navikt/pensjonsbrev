package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.ut_sumutbt_hoyere_lavere
import no.nav.pensjon.brev.maler.legacy.ut_virkningstidpunktar
import no.nav.pensjon.brev.maler.legacy.ut_virkningstidpunkttilprosent
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU3802_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3802, TBU3802_NN]

		paragraph {
			text (
				bokmal { + "I perioden fra 1. januar 2016 til 31. desember 2020 er det egne regler for hvordan barnetillegget ditt blir justert. " },
				nynorsk { + "I perioden frå 1. januar 2016 til 31. desember 2020 er det eigne reglar for korleis barnetillegget ditt blir justert. " },
			)
			text (
				bokmal { + "Fram til 31. desember " + pe.ut_virkningstidpunktar().format() + " kan ikke uføretrygden og barnetillegget ditt til sammen være høyere enn " + pe.ut_virkningstidpunkttilprosent().format() + " prosent av inntekten din før du ble ufør. Uføretrygden og barnetillegget ditt utgjør " + pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().format() + " prosent av det som var inntekten din før du ble ufør." },
				nynorsk { + "Fram til 31. desember " + pe.ut_virkningstidpunktar().format() + " kan ikkje uføretrygda di og barnetillegget ditt til saman vere høgare enn " + pe.ut_virkningstidpunkttilprosent().format() + " prosent av inntekta di før du blei ufør. Uføretrygda og barnetillegget ditt utgjer " + pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().format() + " prosent av det som var inntekta di før du blei ufør." },
			)
			text (
				bokmal { + pe.ut_virkningstidpunkttilprosent().format() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + ". Uføretrygden og barnetillegget ditt er til sammen " + pe.ut_sumutbt_hoyere_lavere() + " enn dette." },
				nynorsk { + pe.ut_virkningstidpunkttilprosent().format() + " prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + ". Uføretrygda di og barnetillegget ditt er til saman " + pe.ut_sumutbt_hoyere_lavere() + " enn dette." },
			)

			//IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0) THEN     INCLUDE END IF
			showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))){
				text (
					bokmal { + " Barnetillegget blir derfor ikke utbetalt." },
					nynorsk { + " Barnetillegget blir derfor ikkje utbetalt." },
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
				text (
					bokmal { + " Årlig barnetillegg før reduksjon ut fra inntekt er derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + "." },
					nynorsk { + " Brutto årleg barnetillegg før reduksjon ut frå inntekt er derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + "." },
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT < PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().lessThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
				text (
					bokmal { + " Barnetillegget er derfor fastsatt ut fra 40 prosent av folketrygdens grunnbeløp for hvert barn du får barnetillegg for." },
					nynorsk { + " Barnetillegget er derfor fastsett ut frå 40 prosent av grunnbeløpet i folketrygda for kvart barn du får barnetillegg for." },
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT < PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40) and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().lessThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
				text (
					bokmal { + " Fordi du har mindre enn 40 års trygdetid er barnetillegget ditt justert ned." },
					nynorsk { + " Fordi du har mindre enn 40 års trygdetid, er barnetillegget ditt justert ned." },
				)
			}

			//IF(NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
			showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
				text (
					bokmal { + " Har du " },
					nynorsk { + " Har du " },
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
				text (
					bokmal { + "og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " din " },
					nynorsk { + "og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
				)
			}

			//IF(NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
			showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
				text (
					bokmal { + "inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt. " },
					nynorsk { + "inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt. " },
				)
			}
		}
    }
}
        