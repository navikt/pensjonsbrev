package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.exstreamFunctions.*
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.maler.legacy.FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear
import no.nav.pensjon.brev.maler.legacy.barnetilleggfelles_justeringsbelopperarutenminus
import no.nav.pensjon.brev.maler.legacy.barnetilleggserkull_justeringsbelopperarutenminus
import no.nav.pensjon.brev.maler.legacy.foedselsdatoTilBarnTilleggErInnvilgetFor
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagsliste_personbostedsland
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.pe_ut_tbu601v_tbu604v
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_felles
import no.nav.pensjon.brev.maler.legacy.ut_barnet_barna_serkull
import no.nav.pensjon.brev.maler.legacy.ut_btfbinntektbruktiavkortningminusbtfbfribelop
import no.nav.pensjon.brev.maler.legacy.ut_btsbinntektbruktiavkortningminusbtsbfribelop
import no.nav.pensjon.brev.maler.legacy.ut_etteroppgjor_bt_utbetalt
import no.nav.pensjon.brev.maler.legacy.ut_nettoakk_pluss_nettorestar
import no.nav.pensjon.brev.maler.legacy.ut_tbu056v
import no.nav.pensjon.brev.maler.legacy.ut_tbu069v
import no.nav.pensjon.brev.maler.legacy.ut_tbu501v
import no.nav.pensjon.brev.maler.legacy.ut_tbu605
import no.nav.pensjon.brev.maler.legacy.ut_tbu605v_eller_til_din
import no.nav.pensjon.brev.maler.legacy.ut_tbu606v_tbu608v
import no.nav.pensjon.brev.maler.legacy.ut_tbu606v_tbu611v
import no.nav.pensjon.brev.maler.legacy.ut_tbu608_far_ikke
import no.nav.pensjon.brev.maler.legacy.ut_tbu609v_tbu611v
import no.nav.pensjon.brev.maler.legacy.ut_tbu611_far_ikke
import no.nav.pensjon.brev.maler.legacy.ut_tbu613v
import no.nav.pensjon.brev.maler.legacy.ut_tbu613v_1_3
import no.nav.pensjon.brev.maler.legacy.ut_tbu613v_4_5
import no.nav.pensjon.brev.maler.legacy.ut_virkningstidpunktstorreenn01012016
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar_utenminus
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt
import no.nav.pensjon.brev.maler.legacy.ut_antallbarnserkullogfelles
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_belopredusert
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_fradrag
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettorestar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_totalnetto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import kotlin.or
import kotlin.text.format
import java.time.LocalDate
import no.nav.pensjon.brev.template.dsl.expression.ifNull

//[TBU028V-TBU020V]
data class ReduksjonsprosentOgUtbetalingUPTilUT (
    val pe: Expression<PEgruppe10>
): OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        val kravarsaktype = pe.vedtaksdata_kravhode_kravarsaktype()
        val brevkode = pe.pebrevkode()
        val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
        val anvendtTrygdetid = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
        val yrkesskaderesultat = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
        val virkningstidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom()
        val sivilstand = pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
        val harBarnetilleggFelles = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
        val harBarnetilleggSerkull = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
        val barnetilleggFellesNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
        val barnetilleggSerkullNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
        val inntektsgrense = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
        val inntektstak = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
        val forventetInntekt = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
        val kompensasjonsgrad = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
        val utbetalingsgrad = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
        val ugradertBruttoPerAr = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_ugradertbruttoperar()
        val oifu = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oifu()
        val gammelUforetrygd = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut()
        val nyUforetrygd = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()
        val harGjenlevendetillegg = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()
        val andelYtelseAvOifu = pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu()
        val justeringsbelopFellesPerAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
        val justeringsbelopSerkullPerAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
        val avkortingsbelopFellesPerAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
        val avkortingsbelopSerkullPerAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
        val erFribelopFellesPeriodisert = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()
        val erFribelopSerkullPeriodisert = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()
        val erEndretInntekt = kravarsaktype.equalTo("endret_inntekt")
        val erIkkeSoknadBarnetillegg = kravarsaktype.notEqualTo("soknad_bt")
        val harEktefelletillegg = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
        val skalViseFellesBarnetillegg = pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v()
        val skalViseSerkullBarnetillegg = pe.ut_tbu606v_tbu611v() and pe.ut_tbu609v_tbu611v()
        val ifuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
        val erMinsteIfu = ifuBegrunnelse.equalTo("stdbegr_12_8_2_3") or ifuBegrunnelse.equalTo("stdbegr_12_8_2_5")
        val erMinsteIfu35G = ifuBegrunnelse.equalTo("stdbegr_12_8_2_5")
        val erMinsteIfu33G = ifuBegrunnelse.equalTo("stdbegr_12_8_2_3")
        val virkningsdato = pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000, 1, 1))
        val erOvergangsperiodeBarnetillegg = virkningsdato.greaterThanOrEqual(LocalDate.of(2016, 1, 1)) and virkningsdato.lessThan(LocalDate.of(2021, 1, 1))

        showIf(erMinsteIfu){
            title1 {
                text (
                    bokmal { + "Inntekten din før du ble ufør" },
                    nynorsk { + "Inntekta di før du blei ufør" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Ved overgangen til uføretrygd, skal inntekten din før du ble ufør derfor settes til minst " },
                    nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Ved overgangen til uføretrygd skal inntekta di før du blei ufør, derfor setjast til minst " },
                )

                showIf(erMinsteIfu35G){
                    text (
                        bokmal { + "3,5 " },
                        nynorsk { + "3,5 " },
                    )
                }

                showIf(erMinsteIfu33G){
                    text (
                        bokmal { + "3,3 " },
                        nynorsk { + "3,3 " },
                    )
                }
                text (
                    bokmal { + "ganger folketrygdens grunnbeløp." },
                    nynorsk { + "gonger grunnbeløpet i folketrygda." },
                )
            }
        }

        showIf((pe.ut_tbu056v())){
            title1 {
                text (
                    bokmal { + "Slik har vi fastsatt reduksjonsprosenten din" },
                    nynorsk { + "Slik har vi fastsett reduksjonsprosenten din" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Vi fastsetter reduksjonsprosenten ved å sammenligne det du " },
                    nynorsk { + "Vi fastset reduksjonsprosenten ved å samanlikne det du " },
                )

                showIf((uforegrad.equalTo(100))){
                    text (
                        bokmal { + "har rett til i" },
                        nynorsk { + "har rett til i" },
                    )
                }

                showIf((uforegrad.lessThan(100))){
                    text (
                        bokmal { + "ville hatt rett til i" },
                        nynorsk { + "ville hatt rett til i" },
                    )
                }
                text (
                    bokmal { + " 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Reduksjonsprosenten brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen." },
                    nynorsk { + " 100 prosent uføretrygd, med den oppjusterte inntekta di før du blei ufør. Reduksjonsprosenten blir brukt til å berekne kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa." },
                )
            }
        }

        showIf(pe.ut_tbu056v()){
            paragraph {
                text (
                    bokmal { + "Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. For å kunne fastsette reduksjonsprosenten din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på " + oifu.format() + " kroner." },
                    nynorsk { + "Inntekta di før du blei ufør er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. For å kunne fastsetje reduksjonsprosenten din, må inntekta oppjusterast til dagens verdi. Oppjustert til dagens verdi utgjer dette ei inntekt på " + oifu.format() + " kroner." },
                )
            }
        }

        showIf(pe.ut_tbu056v() and uforegrad.equalTo(100)){
            paragraph {
                text (
                    bokmal { + "Du har rett til 100 prosent uføretrygd, som utgjør " + ugradertBruttoPerAr.format() + " kroner per år." },
                    nynorsk { + "Du har rett til 100 prosent uføretrygd, som utgjer " + ugradertBruttoPerAr.format() + " kroner per år." },
                )
            }
        }

        showIf(pe.ut_tbu056v() and uforegrad.lessThan(100)){
            paragraph {
                text (
                    bokmal { + "Du har rett til " + uforegrad.format() + " prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette " + ugradertBruttoPerAr.format() + " kroner per år." },
                    nynorsk { + "Du har rett til " + uforegrad.format() + " prosent uføretrygd. Rekna om til 100 prosent uføretrygd, utgjer dette " + ugradertBruttoPerAr.format() + " kroner per år." },
                )
            }
        }

        showIf(pe.ut_tbu056v()){
            paragraph {
                text (
                    bokmal { + "Vi beregner reduksjonsprosenten din slik:(" + ugradertBruttoPerAr.format() + " / " + oifu.format() + ") * 100 = " + kompensasjonsgrad.format() + " prosent." },
                    nynorsk { + "Vi bereknar reduksjonsprosenten din slik:(" + ugradertBruttoPerAr.format() + " / " + oifu.format() + ") * 100 = " + kompensasjonsgrad.format() + " prosent." },
                )
            }
        }

        showIf((pe.ut_tbu056v())){
            paragraph {

                showIf(kompensasjonsgrad.equalTo(70.0)){
                    text (
                        bokmal { + "Reduksjonsprosenten skal ved beregningen ikke settes høyere enn 70 prosent. " },
                        nynorsk { + "Reduksjonsprosenten skal ikkje setjast høgare enn 70 prosent i berekninga. " },
                    )
                }
                text (
                    bokmal { + "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig reduksjonsprosent i beregningen." },
                    nynorsk { + "Dersom uføretrygda di blir endra i løpet av eit kalenderår, vil vi bruke ein gjennomsnittleg reduksjonsprosent i berekninga." },
                )
            }
        }

        showIf((erEndretInntekt and gammelUforetrygd.notEqualTo(nyUforetrygd) and inntektsgrense.lessThan(inntektstak))){
            title1 {
                text (
                    bokmal { + "Slik beregner vi utbetaling av uføretrygden når inntekten din endres" },
                    nynorsk { + "Slik bereknar vi utbetaling av uføretrygda når inntekta di er endra" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Utbetalingen av uføretrygden din er beregnet på nytt, fordi inntekten din er endret. Det er den innmeldte inntekten din og uføretrygden du har fått utbetalt hittil i år, som avgjør hvor mye du får utbetalt i de månedene som er igjen i kalenderåret." },
                    nynorsk { + "Utbetalinga av uføretrygda di er berekna på nytt fordi inntekta di er endra. Det er den innmelde inntekta di og uføretrygda du har fått utbetalt hittil i år, som avgjer kor mykje du får utbetalt i dei månadene som er att av kalenderåret." },
                )
            }
        }

        showIf((erEndretInntekt and gammelUforetrygd.notEqualTo(nyUforetrygd) and forventetInntekt.greaterThanOrEqual(inntektsgrense) and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "Uføretrygden reduseres med " + kompensasjonsgrad.format() + " prosent av inntekten over " + inntektsgrense.format() + " kroner fordi du har en reduksjonsprosent som er " + kompensasjonsgrad.format() + " prosent." },
                    nynorsk { + "Uføretrygda blir redusert med " + kompensasjonsgrad.format() + " prosent av inntekta over " + inntektsgrense.format() + " kroner fordi du har ein reduksjonsprosent som er " + kompensasjonsgrad.format() + " prosent." },
                )
            }
        }

        showIf((erEndretInntekt and gammelUforetrygd.notEqualTo(nyUforetrygd) and forventetInntekt.greaterThanOrEqual(inntektsgrense) and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "Du har en inntektsgrense på " + inntektsgrense.format() + " kroner og den innmeldte inntekten din er " + forventetInntekt.format() + " kroner. Dette betyr at overskytende inntekt er " + pe.functions.pe_ut_overskytende.format() + " kroner." },
                    nynorsk { + "Du har ei inntektsgrense på " + inntektsgrense.format() + " kroner, og den innmelde inntekta di er " + forventetInntekt.format() + " kroner. Dette vil seie at overskytande inntekt er " + pe.functions.pe_ut_overskytende.format() + " kroner." },
                )
            }
        }


        showIf((erEndretInntekt and gammelUforetrygd.notEqualTo(nyUforetrygd) and forventetInntekt.greaterThanOrEqual(inntektsgrense) and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            title1 {
                text (
                    bokmal { + "Slik beregner vi reduksjonen av uføretrygden" },
                    nynorsk { + "Slik bereknar vi reduksjonen av uføretrygda" },
                )
            }
        }

        showIf((erEndretInntekt and gammelUforetrygd.notEqualTo(nyUforetrygd) and forventetInntekt.greaterThanOrEqual(inntektsgrense) and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + pe.functions.pe_ut_overskytende.format() + " kr" },
                    nynorsk { + pe.functions.pe_ut_overskytende.format() + " kr" },
                )
                text (
                    bokmal { + "x" },
                    nynorsk { + "x" },
                )
                text (
                    bokmal { + kompensasjonsgrad.format() + " %" },
                    nynorsk { + kompensasjonsgrad.format() + " %" },
                )
                text (
                    bokmal { + "=" },
                    nynorsk { + "=" },
                )
                text (
                    bokmal { + pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format() },
                    nynorsk { + pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format() },
                )
                text (
                    bokmal { + "kroner i reduksjon for året" },
                    nynorsk { + "kroner i reduksjon for året" },
                )
            }
        }

        showIf((utbetalingsgrad.lessThan((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad())) and erIkkeSoknadBarnetillegg)){
            title1 {
                text (
                    bokmal { + "Slik blir din utbetaling før skatt" },
                    nynorsk { + "Slik blir den månadlege utbetalinga di før skatt" },
                )
            }
        }

        showIf((utbetalingsgrad.lessThan(uforegrad) and erIkkeSoknadBarnetillegg and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "Brutto beregnet uføretrygd som følge av innmeldt inntekt: " },
                    nynorsk { + "Brutto berekna uføretrygd som følgje av innmeld inntekt: " },
                )
                text (
                    bokmal { + pe.ut_nettoakk_pluss_nettorestar().format() + " kr" },
                    nynorsk { + pe.ut_nettoakk_pluss_nettorestar().format() + " kr" },
                )
            }
        }

        showIf((utbetalingsgrad.lessThan(uforegrad) and erIkkeSoknadBarnetillegg and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "- Utbetalt uføretrygd hittil i år:" },
                    nynorsk { + "- Utbetalt uføretrygd hittil i år:" },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kr" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kr" },
                )
            }
        }

        showIf((utbetalingsgrad.lessThan(uforegrad) and erIkkeSoknadBarnetillegg and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "= Utbetaling av uføretrygd for resterende måneder i året:" },
                    nynorsk { + "= Utbetaling av uføretrygd for resterande månader i året:" },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettorestar().format() + " kr" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettorestar().format() + " kr" },
                )
            }
        }


        showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().greaterThan(0) and utbetalingsgrad.lessThan(uforegrad) and erIkkeSoknadBarnetillegg and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "Du vil få en månedlig reduksjon i uføretrygden din på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_fradrag().format() + " kroner i resterende måneder i kalenderåret." },
                    nynorsk { + "Du får ein månadleg reduksjon i uføretrygda di på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_fradrag().format() + " kroner i resterande månader i kalenderåret." },
                )
            }
        }

        showIf((utbetalingsgrad.lessThan(uforegrad) and pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().greaterThan(0) and erIkkeSoknadBarnetillegg and inntektsgrense.lessThan(inntektstak) and nyUforetrygd.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "Uføretrygden og inntekten din vil ut fra dette til sammen utgjøre " + pe.functions.pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt.format() + " kroner for dette året." },
                    nynorsk { + "Uføretrygda di og inntekta di utgjer til saman " + pe.functions.pe_ut_nettoakk_pluss_nettorestar_pluss_forventetinntekt.format() + " kroner i dette året." },
                )
            }
        }

        showIf((inntektsgrense.greaterThanOrEqual(inntektstak) and utbetalingsgrad.lessThan(uforegrad) and erIkkeSoknadBarnetillegg and nyUforetrygd.equalTo(0))){
            paragraph {
                text (
                    bokmal { + "Utbetalingen av uføretrygden din er redusert, fordi du har inntekt. Den innmeldte inntekten er høyere enn inntektsgrensen din på " + inntektsgrense.format() + " kroner og uføretrygden blir derfor ikke utbetalt. " },
                    nynorsk { + "Utbetalinga av uføretrygda di er redusert, fordi du har inntekt. Den innmelde inntekta er høgare enn inntektsgrensa di på " + inntektsgrense.format() + " kroner og uføretrygda blir derfor ikkje utbetalt. " },
                )
            }
        }

        showIf((nyUforetrygd.equalTo(0) and inntektsgrense.lessThan(inntektstak) and forventetInntekt.greaterThan(inntektstak))){
            paragraph {
                text (
                    bokmal { + "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si " + inntektstak.format() + " kroner." },
                    nynorsk { + "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil si " + inntektstak.format() + " kroner." },
                )
            }
        }

        showIf((utbetalingsgrad.lessThan(uforegrad) and erIkkeSoknadBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Du vil få tilbake " + uforegrad.format() + " prosent uføretrygd uten søknad, dersom du tjener mindre enn inntektsgrensen din. Hvis du allerede har fått utbetalt det du har rett til i uføretrygd for kalenderåret, vil du ikke få utbetalt uføretrygd med den opprinnelige uføregraden din før neste kalenderår." },
                    nynorsk { + "Du får tilbake " + uforegrad.format() + " prosent uføretrygd utan søknad dersom du tener mindre enn inntektsgrensa di. Dersom du allereie har fått utbetalt det du har rett til i uføretrygd for kalenderåret, får du ikkje utbetalt uføretrygd med den opphavlege uføregraden din før neste kalenderår." },
                )
            }
        }

        showIf(pe.pe_ut_tbu601v_tbu604v()) {
            title1 {
                text(
                    bokmal { +"Slik reduserer vi barnetillegget ut fra inntekt" },
                    nynorsk { +"Slik reduserer vi barnetillegget ut frå inntekt" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Størrelsen på barnetillegget er avhengig av inntekt. Barnetillegget blir redusert ut fra personinntekt. Dette kan for eksempel være:uføretrygdarbeidsinntektnæringsinntektinntekt fra utlandetytelser/pensjon fra Norgepensjon fra utlandetDu kan lese mer om personinntekt på $SKATTEETATEN_URL. " },
                    nynorsk { +"Storleiken på barnetillegget er avhengig av inntekt.Barnetillegget kan bli redusert ut frå personinntekt. Dette kan til dømes være: uføretrygdarbeidsinntektnæringsinntektinntekt frå utlandetytingar/pensjon frå Noregpensjon frå utlandetDu kan lese meir om personinntekt på $SKATTEETATEN_URL. " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Det er inntekten " },
                    nynorsk { +"Det er inntekta " },
                )

                showIf((harBarnetilleggFelles)) {
                    text(
                        bokmal { +"til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { +"til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                    )
                }

                showIf((harBarnetilleggSerkull and not(harBarnetilleggFelles))) {
                    text(
                        bokmal { +"din " },
                        nynorsk { +"di " },
                    )
                }
                text(
                    bokmal { +"som avgjør hva du får utbetalt i barnetillegg i løpet av året. Er inntekten høyere enn fribeløpet blir barnetillegget redusert. " },
                    nynorsk { +"som avgjer kva du får utbetalt i barnetillegg i løpet av året. Er inntekta høgare enn fribeløpet blir barnetillegget redusert. " },
                )
            }
            paragraph {
                showIf((harBarnetilleggFelles)) {
                    text(
                        bokmal { +"For barn som bor sammen med begge sine foreldre, er fribeløpet 4,6 ganger folketrygdens grunnbeløp. " },
                        nynorsk { +"For barn som bur saman med begge foreldra sine, er fribeløpet 4,6 gonger grunnbeløpet i folketrygda. " },
                    )
                }

                showIf((harBarnetilleggSerkull)) {
                    text(
                        bokmal { +"For barn som ikke bor sammen med begge sine foreldre, er fribeløpet 3,1 ganger folketrygdens grunnbeløp. " },
                        nynorsk { +"For barn som ikkje bur saman med begge foreldra, er fribeløpet 3,1 gonger grunnbeløpet i folketrygda. " },
                    )
                }
                text(
                    bokmal { +"Fribeløpet øker med 0,4 ganger folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { +"Fribeløpet aukar med 0,4 gonger grunnbeløpet i folketrygda for kvart ekstra barn. " },
                )
            }

            showIf(((justeringsbelopSerkullPerAr.equalTo(0) and harBarnetilleggSerkull and barnetilleggSerkullNetto.equalTo(0)) or (justeringsbelopFellesPerAr.equalTo(0) and harBarnetilleggFelles and barnetilleggFellesNetto.equalTo(0)))) {
                paragraph {
                    text(
                        bokmal { +"Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet. Er inntekten " },
                        nynorsk { +"Barnetillegget blir redusert med 50 prosent av inntekta som overstig fribeløpet. Er inntekta " },
                    )

                    showIf((harBarnetilleggFelles and justeringsbelopFellesPerAr.equalTo(0))) {
                        text(
                            bokmal { +"til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                            nynorsk { +"til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                        )
                    }

                    showIf((harBarnetilleggSerkull and not(harBarnetilleggFelles) or (harBarnetilleggSerkull and harBarnetilleggFelles and justeringsbelopSerkullPerAr.equalTo(0) and justeringsbelopFellesPerAr.greaterThan(0)))) {
                        text(
                            bokmal { +"din " },
                            nynorsk { +"di " },
                        )
                    }
                    text(
                        bokmal { +"over grensen for å få utbetalt barnetillegg, blir ikke barnetillegget utbetalt. " },
                        nynorsk { +"over grensa for å få utbetalt barnetillegg, blir ikkje barnetillegget utbetalt. " },
                    )
                }
            }
        }

        showIf((pe.ut_tbu501v())) {
            title1 {
                text(
                    bokmal { +"For deg som har rett til barnetillegg" },
                    nynorsk { +"For deg som har rett til barnetillegg" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til barnetillegg for barn født: " },
                    nynorsk { +"Du har rett til barnetillegg for barn fødd: " },
                )
            }

            forEach(pe.foedselsdatoTilBarnTilleggErInnvilgetFor()) { foedselsdato ->
                paragraph {
                    text(
                        bokmal { + foedselsdato.format() },
                        nynorsk { + foedselsdato.format() },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Barnetillegget kan utgjøre opptil 40 prosent av folketrygdens grunnbeløp for hvert barn du forsørger. Du har rett til barnetillegg så lenge du forsørger barn som er under 18 år. Barnetillegget opphører når barnet fyller 18 år. " },
                    nynorsk { + "Barnetillegget kan utgjere opptil 40 prosent av grunnbeløpet i folketrygda for kvart barn du forsørgjer. Du har rett til barnetillegg så lenge du forsørgjer barn som er under 18 år. Barnetillegget opphøyrer når barnet fyller 18 år. " },
                )

                showIf((anvendtTrygdetid.lessThan(40) and yrkesskaderesultat.notEqualTo("oppfylt"))){
                    text (
                        bokmal { + "Hvor mye du får i barnetillegg er også avhengig av trygdetiden din. Fordi trygdetiden din er kortere enn 40 år, blir barnetillegget ditt redusert. " },
                        nynorsk { + " Kor mykje du får i barnetillegg, er også avhengig av trygdetida di. Fordi trygdetida di er kortare enn 40 år, blir barnetillegget ditt redusert. " },
                    )
                }
            }
        }

        showIf((pe.ut_tbu501v() and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000, 1, 1)).greaterThanOrEqual(LocalDate.of(2016, 1, 1)) and andelYtelseAvOifu.greaterThan(95.0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().notEqualTo("overgangsregler_2016"))){
            paragraph {
                text (
                    bokmal { + "Uføretrygd" },
                    nynorsk { + "Uføretrygd" },
                )

                showIf((harGjenlevendetillegg)){
                    text (
                        bokmal { + ", gjenlevendetillegg" },
                        nynorsk { + ", attlevendetillegg" },
                    )
                }
                text (
                    bokmal { + " og barnetillegg kan ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Fordi uføretrygden og barnetillegget til sammen er høyere enn dette beløpet," },
                    nynorsk { + " og barnetillegg kan ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Fordi uføretrygda og barnetillegget til saman er høgare enn dette beløpet," },
                )

                showIf((andelYtelseAvOifu.greaterThan(95.0) and (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().greaterThan(0) or pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().greaterThan(0)))){
                    text (
                        bokmal { + " blir barnetillegget ditt redusert." },
                        nynorsk { + " blir barnetillegget ditt redusert." },
                    )
                }

                showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){
                    text (
                        bokmal { + " blir barnetillegget ditt ikke utbetalt." },
                        nynorsk { + " blir ikkje barnetillegget ditt utbetalt." },
                    )
                }
            }
        }

        showIf((harBarnetilleggFelles or harBarnetilleggSerkull) and uforegrad.greaterThan(0) and uforegrad.lessThan(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000, 1, 1)).greaterThanOrEqual(LocalDate.of(2016, 1, 1)) and andelYtelseAvOifu.greaterThan(95.0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().notEqualTo("overgangsregler_2016")){
            paragraph {
                text (
                    bokmal { + "Du har en uføregrad som er lavere enn 100 prosent. Vi har tatt utgangspunkt i en uføregrad som tilsvarer 100 prosent når vi har sammenlignet størrelsen på uføretrygd og barnetillegg med inntekten du hadde før du ble ufør. " },
                    nynorsk { + "Du har ein uføregrad som er lågare enn 100 prosent. Vi har teke utgangspunkt i ein uføregrad som tilsvarer 100 prosent når vi har samanlikna storleiken på uføretrygd og barnetillegg med inntekta du hadde før du blei ufør. " },
                )
            }
        }

        showIf(erOvergangsperiodeBarnetillegg) {
            paragraph {
                text(
                    bokmal { +"I perioden fra 1. januar 2016 til 31. desember 2020 er det egne regler for hvordan barnetillegget blir redusert. Dette skjer gradvis fram til 1. januar 2021. Uføretrygden" },
                    nynorsk { +"I perioden frå 1. januar 2016 til 31. desember 2020 er det eigne reglar for korleis barnetillegget blir redusert. Dette skjer gradvis fram til 1. januar 2021. Uføretrygda" },
                )

                showIf((harGjenlevendetillegg)) {
                    text(
                        bokmal { +", gjenlevendetillegget" },
                        nynorsk { +", attlevandetillegget" },
                    )
                }
                text(
                    bokmal { +" og barnetillegget vil da til sammen utgjøre 95 prosent av det som var inntekten din før du ble ufør. " },
                    nynorsk { +" og barnetillegget utgjer då til saman 95 prosent av det som var inntekta di før du blei ufør. " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Slik blir barnetillegget ditt justert ned:I 2016 kan ikke uføretrygden" },
                    nynorsk { +"Slik blir barnetillegget ditt justert ned:I 2016 kan ikkje uføretrygda" },
                )

                showIf((harGjenlevendetillegg)) {
                    text(
                        bokmal { +", gjenlevendetillegget" },
                        nynorsk { +", attlevandetillegget" },
                    )
                }
                text(
                    bokmal { +" og barnetillegget være høyere enn 110 prosent av det som var inntekten din før du ble ufør.I 2017 kan ikke uføretrygden" },
                    nynorsk { +" og barnetillegget vere høgare enn 110 prosent av det som var inntekta di før du blei ufør.I 2017 kan ikkje uføretrygda" },
                )

                showIf((harGjenlevendetillegg)) {
                    text(
                        bokmal { +", gjenlevendetillegget" },
                        nynorsk { +", attlevandetillegget" },
                    )
                }
                text(
                    bokmal { +" og barnetillegget være høyere enn 107 prosent av det som var inntekten din før du ble ufør. I 2018 kan ikke uføretrygden" },
                    nynorsk { +" og barnetillegget vere høgare enn 107 prosent av det som var inntekta di før du blei ufør.I 2018 kan ikkje uføretrygda" },
                )

                showIf((harGjenlevendetillegg)) {
                    text(
                        bokmal { +", gjenlevendetillegget" },
                        nynorsk { +", attlevandetillegget" },
                    )
                }
                text(
                    bokmal { +" og barnetillegget være høyere enn 104 prosent av det som var inntekten din før du ble ufør. I 2019 kan ikke uføretrygden" },
                    nynorsk { +" og barnetillegget vere høgare enn 104 prosent av det som var inntekta di før du blei ufør.I 2019 kan ikkje uføretrygda" },
                )

                showIf((harGjenlevendetillegg)) {
                    text(
                        bokmal { +", gjenlevendetillegget" },
                        nynorsk { +", attlevandetillegget" },
                    )
                }
                text(
                    bokmal { +" og barnetillegget være høyere enn 101 prosent av det som var inntekten din før du ble ufør. I 2020 kan ikke uføretrygden" },
                    nynorsk { +" og barnetillegget vere høgare enn 101 prosent av det som var inntekta di før du blei ufør.I 2020 kan ikkje uføretrygda" },
                )

                showIf((harGjenlevendetillegg)) {
                    text(
                        bokmal { +", gjenlevendetillegget" },
                        nynorsk { +", attlevandetillegget" },
                    )
                }
                text(
                    bokmal { +" og barnetillegget være høyere enn 98 prosent av det som var inntekten din før du ble ufør. " },
                    nynorsk { +" og barnetillegget vere høgare enn 98 prosent av det som var inntekta di før du blei ufør. " },
                )
            }

            showIf(pe.ut_antallbarnserkullogfelles().greaterThan(1)) {
                paragraph {
                    text(
                        bokmal { +"Mottar du barnetillegg for flere barn er det lik fordeling på alle barnetilleggene." },
                        nynorsk { +"Får du barnetillegg for fleire barn, er det lik fordeling på alle barnetillegga." },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Du kan lese mer om overgangsreglene for barnetillegg på $NAV_URL." },
                    nynorsk { + "Du kan lese meir om overgangsreglane for barnetillegg på $NAV_URL." },
                )
            }
        }

        showIf((pe.ut_tbu069v())){
            title1 {
                text (
                    bokmal { + "Slik beregner vi størrelsen på barnetillegget" },
                    nynorsk { + "Slik bereknar vi storleiken på barnetillegget" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Størrelsen på barnetillegget er avhengig av samlet inntekt. " },
                    nynorsk { + "Storleiken på barnetillegget er avhengig av samla inntekt. " },
                )
            }
            paragraph {
                text (
                    bokmal { + "Barnetillegget kan bli redusert ut fra:uføretrygdarbeidsinntektnæringsinntektinntekt fra utlandetytelser/pensjon fra Norgepensjon fra utlandet " },
                    nynorsk { + "Barnetillegget kan bli redusert ut frå:uføretrygdarbeidsinntektnæringsinntektinntekt frå utlandetytingar/pensjon frå Noregpensjon frå utlandet " },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (harBarnetilleggFelles and not(harBarnetilleggSerkull)))){
            paragraph {
                text (
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene." },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra." },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (harBarnetilleggFelles and not(harBarnetilleggSerkull)))){
            paragraph {
                text (
                    bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                )

                showIf((anvendtTrygdetid.lessThan(40) and yrkesskaderesultat.notEqualTo("oppfylt"))){
                    text (
                        bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. " },
                        nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. " },
                    )
                }
            }
        }

        showIf((pe.ut_tbu069v() and (harBarnetilleggFelles and not(harBarnetilleggSerkull)))){
            paragraph {
                text (
                    bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. Dette gjelder også dersom den ene forelderen mottar alderspensjon. " },
                    nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. Dette gjeld også dersom den eine forelderen får alderspensjon. " },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (not(harBarnetilleggFelles) and harBarnetilleggSerkull))){
            paragraph {
                text (
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. " },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. " },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (not(harBarnetilleggFelles) and harBarnetilleggSerkull))){
            paragraph {
                text (
                    bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                )

                showIf((anvendtTrygdetid.lessThan(40) and yrkesskaderesultat.notEqualTo("oppfylt"))){
                    text (
                        bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. " },
                        nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. " },
                    )
                }
            }
        }

        showIf((pe.ut_tbu069v() and (not(harBarnetilleggFelles) and harBarnetilleggSerkull))){
            paragraph {
                text (
                    bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Dette gjelder også dersom den ene forelderen mottar alderspensjon. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet. " },
                    nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Dette gjeld også dersom den eine forelderen får alderspensjon. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet. " },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (harBarnetilleggFelles and harBarnetilleggSerkull))){
            paragraph {
                text (
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din for " + pe.ut_barnet_barna_felles() + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (harBarnetilleggFelles and harBarnetilleggSerkull))){
            paragraph {
                text (
                    bokmal { + "For " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "For " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                )
            }
        }

        showIf((pe.ut_tbu069v() and (harBarnetilleggFelles and harBarnetilleggSerkull) and (anvendtTrygdetid.lessThan(40) and yrkesskaderesultat.notEqualTo("oppfylt")) and (pe.ut_virkningstidpunktstorreenn01012016()))){
            paragraph {
                text (
                    bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpene redusert ut fra den trygdetiden du har." },
                    nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpa reduserte ut frå den trygdetida du har." },
                )
            }
        }

        showIf((pe.ut_tbu605())){
            paragraph {
                showIf(((pe.ut_tbu605v_eller_til_din()))){
                    text (
                        bokmal { + "Har det vært en endring i inntekten din" },
                        nynorsk { + "Har det vore ei endring i inntekta di" },
                    )
                }

                showIf(((pe.ut_tbu605v_eller_til_din() and harBarnetilleggFelles and (sivilstand.equalTo("bormed ektefelle") or sivilstand.equalTo("bormed registrert partner") or sivilstand.equalTo("bormed 1-5") or sivilstand.equalTo("bormed 1_5") or sivilstand.equalTo("bormed 3-2"))))){
                    text (
                        bokmal { + " eller til din " },
                        nynorsk { + " eller til di " },
                    )
                }

                showIf(((pe.ut_tbu605v_eller_til_din() and harBarnetilleggFelles))){
                    text (
                        bokmal { + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + "," },
                        nynorsk { + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + "," },
                    )
                }

                showIf((erEndretInntekt)){
                    text (
                        bokmal { + "Når inntekten din " },
                        nynorsk { + "Når inntekta di " },
                    )
                }

                showIf(((erEndretInntekt and harBarnetilleggFelles and (sivilstand.equalTo("bormed ektefelle") or sivilstand.equalTo("bormed registrert partner") or sivilstand.equalTo("bormed 1-5") or sivilstand.equalTo("bormed 1_5") or sivilstand.equalTo("bormed 3-2"))))){
                    text (
                        bokmal { + "eller til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "eller til di " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " " },
                    )
                }

                showIf((erEndretInntekt)){
                    text (
                        bokmal { + "endrer seg," },
                        nynorsk { + "endrar seg," },
                    )
                }
                text (
                    bokmal { + " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet " },
                    nynorsk { + "blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekta som overstig fribeløpet " },
                )

                showIf((erFribelopSerkullPeriodisert or erFribelopFellesPeriodisert or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())){
                    text (
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "blir omrekna til eit årleg beløp som tilsvarar " },
                    )
                }

                showIf((not(erFribelopSerkullPeriodisert) and not(erFribelopFellesPeriodisert) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert()))){
                    text (
                        bokmal { + "er " },
                        nynorsk { + "er " },
                    )
                }

                showIf((harBarnetilleggFelles)){
                    text (
                        bokmal { + avkortingsbelopFellesPerAr.format() },
                        nynorsk { + avkortingsbelopFellesPerAr.format() },
                    )
                }

                showIf((harBarnetilleggSerkull and not(harBarnetilleggFelles))){
                    text (
                        bokmal { + avkortingsbelopSerkullPerAr.format() },
                        nynorsk { + avkortingsbelopSerkullPerAr.format() },
                    )
                }
                text (
                    bokmal { + " kroner. " },
                    nynorsk { + " kroner. " },
                )

                showIf((justeringsbelopFellesPerAr.equalTo(0) and justeringsbelopSerkullPerAr.equalTo(0))){
                    text (
                        bokmal { + "Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året. " },
                        nynorsk { + "Dette beløpet brukar vi til å redusere barnetillegget ditt for heile året. " },
                    )
                }
            }
        }

        showIf((pe.ut_tbu605() and (justeringsbelopFellesPerAr.notEqualTo(0) or justeringsbelopSerkullPerAr.notEqualTo(0)))){
            paragraph {
                text (
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor  " },
                )

                showIf((justeringsbelopFellesPerAr.greaterThan(0) or justeringsbelopSerkullPerAr.greaterThan(0))){
                    text (
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                showIf((justeringsbelopFellesPerAr.lessThan(0) or justeringsbelopSerkullPerAr.lessThan(0))){
                    text (
                        bokmal { + "redusert" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text (
                    bokmal { + " " },
                    nynorsk { + " " },
                )

                showIf((harBarnetilleggFelles)){
                    text (
                        bokmal { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() },
                        nynorsk { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() },
                    )
                }

                showIf((harBarnetilleggSerkull)){
                    text (
                        bokmal { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() },
                        nynorsk { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() },
                    )
                }
                text (
                    bokmal { + " kroner i beløpet vi reduserer barnetillegget med for resten av året. " },
                    nynorsk { + " kroner i beløpet vi reduserer barnetillegget med for resten av året. " },
                )
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())){
            paragraph {

                showIf(((pe.ut_tbu605v_eller_til_din()))){
                    text (
                        bokmal { + "Har det vært en endring i inntekten din" },
                        nynorsk { + "Har det vore ei endring i inntekta di" }
                    )
                }

                showIf(((pe.ut_tbu605v_eller_til_din() and harBarnetilleggFelles and (sivilstand.equalTo("bormed ektefelle") or sivilstand.equalTo("bormed registrert partner") or sivilstand.equalTo("bormed 1-5") or sivilstand.equalTo("bormed 1_5") or sivilstand.equalTo("bormed 3-2"))))){
                    text (
                        bokmal { + " eller til din" },
                        nynorsk { + " eller til di" }
                    )
                }

                showIf(((pe.ut_tbu605v_eller_til_din()))){
                    text (
                        bokmal { + " " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + "," },
                        nynorsk { + " " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + "," }
                    )
                }

                showIf((erEndretInntekt)){
                    text (
                        bokmal { + "Når inntekten din " },
                        nynorsk { + "Når inntekta di " }
                    )
                }

                showIf((erEndretInntekt and (sivilstand.equalTo("bormed ektefelle") or sivilstand.equalTo("bormed registrert") or sivilstand.equalTo("bormed 1-5") or sivilstand.equalTo("bormed 1_5") or sivilstand.equalTo("bormed 3-2")))){
                    text (
                        bokmal { + "eller til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "eller til di " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " " },
                    )
                }

                showIf((erEndretInntekt)){
                    text (
                        bokmal { + "endrer seg," },
                        nynorsk { + "endrar seg," },
                    )
                }
                text (
                    bokmal { + " blir reduksjonen av barnetilleggene vurdert på nytt. " },
                    nynorsk { + "blir reduksjonen av barnetillegga vurdert på nytt. " },
                )
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())){
            paragraph {
                text (
                    bokmal { + "50 prosent av den inntekten som overstiger fribeløpet for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                    nynorsk { + "50 prosent av den inntekta som overstig fribeløpet for " + pe.ut_barnet_barna_felles() + " som bur med begge sine foreldre " },
                )

                showIf((erFribelopFellesPeriodisert)){
                    text (
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "blir omrekna til eit årleg beløp som tilsvarar " }
                    )
                }

                showIf((not(erFribelopFellesPeriodisert))){
                    text (
                        bokmal { + "er " },
                        nynorsk { + "er " }
                    )
                }
                text (
                    bokmal { + avkortingsbelopFellesPerAr.format() + " kroner. " },
                    nynorsk { + avkortingsbelopFellesPerAr.format() + " kroner. " },
                )

                showIf((justeringsbelopSerkullPerAr.equalTo(0))){
                    text (
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                        nynorsk { + "Dette beløpet brukar vi til å redusere dette barnetillegget for heile året." },
                    )
                }
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3() and justeringsbelopFellesPerAr.notEqualTo(0))){
            paragraph {
                text (
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                )

                showIf((justeringsbelopFellesPerAr.greaterThan(0))){
                    text (
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                showIf((justeringsbelopFellesPerAr.lessThan(0))){
                    text (
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text (
                    bokmal { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_4_5())){
            paragraph {
                text (
                    bokmal { + "For " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene " },
                    nynorsk { + "For " + pe.ut_barnet_barna_serkull() + " som ikkje bur med begge foreldra " },
                )

                showIf((not(erFribelopSerkullPeriodisert))){
                    text (
                        bokmal { + "er 50 prosent av den inntekten som overstiger fribeløpet " },
                        nynorsk { + "er 50 prosent av den inntekten som overstiger fribeløpet " },
                    )
                }

                showIf((erFribelopSerkullPeriodisert)){
                    text (
                        bokmal { + "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "blir 50 prosent av den inntekten som overstiger fribeløpet omrekna til eit årleg beløp som tilsvarar " },
                    )
                }
                text (
                    bokmal { + avkortingsbelopSerkullPerAr.format() + " kroner. " },
                    nynorsk { + avkortingsbelopSerkullPerAr.format() + " kroner. " },
                )

                showIf((justeringsbelopFellesPerAr.equalTo(0))){
                    text (
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. " },
                        nynorsk { + "Dette beløpet brukar vi til å redusere dette barnetillegget for heile året. " },
                    )
                }
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_4_5() and justeringsbelopSerkullPerAr.notEqualTo(0))){
            paragraph {
                text (
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                )

                showIf((justeringsbelopSerkullPerAr.greaterThan(0))){
                    text (
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                showIf((justeringsbelopSerkullPerAr.lessThan(0))){
                    text (
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text (
                    bokmal { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Reduksjon av barnetillegg for fellesbarn før skatt " },
                    nynorsk { + "Reduksjon av barnetillegg for fellesbarn før skatt " },
                )

                showIf(not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(virkningstidspunkt))){
                    text (
                        bokmal { + "i år" },
                        nynorsk { + "i år" },
                    )
                }

                showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(virkningstidspunkt)){
                    text (
                        bokmal { + "for neste år" },
                        nynorsk { + "for neste år" },
                    )
                }
                text (
                    bokmal { + " " },
                    nynorsk { + " " },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                    nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar().format() + " kr" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar().format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Samlet inntekt brukt i fastsettelse av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().format() + " kr" },
                    nynorsk { + "Samla inntekt brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg and (barnetilleggFellesNetto.greaterThan(0) or (barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "Fribeløp brukt i fastsettelsen av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kr" },
                    nynorsk { + "Fribeløp brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg and (barnetilleggFellesNetto.notEqualTo(0) or (barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "Inntekt over fribeløpet er " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop().format() + " kr" },
                    nynorsk { + "Inntekt over fribeløpet er " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop().format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg and (barnetilleggFellesNetto.notEqualTo(0) or (barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.notEqualTo(0))) and avkortingsbelopFellesPerAr.greaterThan(0))){
            paragraph {
                text (
                    bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet" },
                    nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet" },
                )

                showIf((erFribelopFellesPeriodisert)){
                    text (
                        bokmal { + "(oppgitt som et årlig beløp)" },
                        nynorsk { + "(oppgitt som eit årleg beløp)" },
                    )
                }
                text (
                    bokmal { + avkortingsbelopFellesPerAr.format() + " kr" },
                    nynorsk { + avkortingsbelopFellesPerAr.format() + " kr" },
                )
            }
        }

        showIf(((skalViseFellesBarnetillegg) and justeringsbelopFellesPerAr.notEqualTo(0))){
            paragraph {
                showIf((justeringsbelopFellesPerAr.greaterThan(0))){
                    text (
                        bokmal { + "-" },
                        nynorsk { + "-" },
                    )
                }

                showIf((justeringsbelopFellesPerAr.lessThan(0))){
                    text (
                        bokmal { + "+" },
                        nynorsk { + "+" },
                    )
                }
                text (
                    bokmal { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                    nynorsk { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                )
                text (
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus().format() + " kr" },
                    nynorsk { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg and (barnetilleggFellesNetto.notEqualTo(0) or (barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                    nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format() + " kr" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg and (barnetilleggFellesNetto.notEqualTo(0) or (barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "Utbetaling av barnetillegg per måned " },
                    nynorsk { + "Utbetaling av barnetillegg per månad " },
                )
                text (
                    bokmal { + barnetilleggFellesNetto.format() + " kr" },
                    nynorsk { + barnetilleggFellesNetto.format() + " kr" },
                )
            }
        }

        showIf((skalViseFellesBarnetillegg and barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.equalTo(0))){
            paragraph {
                text (
                    bokmal { + "Grensen for å få utbetalt barnetillegg" },
                    nynorsk { + "Grensa for å få utbetalt barnetillegg" },
                )
                text (
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kr" },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kr" },
                )
            }
        }

        showIf((harBarnetilleggFelles and barnetilleggFellesNetto.greaterThan(0) and skalViseFellesBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Du vil få utbetalt " + barnetilleggFellesNetto.format() + " kroner i måneden før skatt i barnetillegg" },
                    nynorsk { + "Du vil få utbetalt " + barnetilleggFellesNetto.format() + " kroner i månaden før skatt i barnetillegg" },
                )

                showIf((harBarnetilleggFelles and harBarnetilleggSerkull and pe.ut_etteroppgjor_bt_utbetalt())){
                    text (
                        bokmal { + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre" },
                        nynorsk { + " for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine" },
                    )
                }
                text (
                    bokmal { + ". " },
                    nynorsk { + ". " },
                )
            }
        }

        showIf((barnetilleggFellesNetto.equalTo(0) and skalViseFellesBarnetillegg)){
            paragraph {
                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        bokmal { + "Du får ikke utbetalt barnetillegget " },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget " },
                    )
                }

                showIf((pe.ut_tbu608_far_ikke() and harBarnetilleggFelles and harBarnetilleggSerkull)){
                    text (
                        bokmal { + "for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                        nynorsk { + "for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine " },
                    )
                }

                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                        nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                    )
                }

                showIf((barnetilleggFellesNetto.equalTo(0) and justeringsbelopFellesPerAr.notEqualTo(0))){
                    text (
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                    )
                }
            }
        }

        showIf((skalViseSerkullBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Reduksjon av barnetillegg for særkullsbarn før skatt " },
                    nynorsk { + "Reduksjon av barnetillegg for særkullsbarn før skatt " },
                )

                showIf(not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(virkningstidspunkt))){
                    text (
                        bokmal { + "i år" },
                        nynorsk { + "i år" },
                    )
                }

                showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(virkningstidspunkt)){
                    text (
                        bokmal { + "for neste år" },
                        nynorsk { + "for neste år" },
                    )
                }
            }
        }

        showIf((skalViseSerkullBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                    nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format() + " kr" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Samlet inntekt brukt i fastsettelse av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kr" },
                    nynorsk { + "Samla inntekt brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg and (barnetilleggSerkullNetto.greaterThan(0) or (barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "Fribeløp brukt i fastsettelsen av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kr" },
                    nynorsk { + "Fribeløp brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg and (barnetilleggSerkullNetto.notEqualTo(0) or (barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "Inntekt over fribeløpet er " + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format() + " kr" },
                    nynorsk { + "Inntekt over fribeløpet er " + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg and (barnetilleggSerkullNetto.notEqualTo(0) or (barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.notEqualTo(0)) and avkortingsbelopSerkullPerAr.greaterThan(0)))){
            paragraph {
                text (
                    bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet" },
                    nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet" },
                )

                showIf((erFribelopFellesPeriodisert)){
                    text (
                        bokmal { + "(oppgitt som et årlig beløp)" },
                        nynorsk { + "(oppgitt som eit årleg beløp)" },
                    )
                }
                text (
                    bokmal { + avkortingsbelopSerkullPerAr.format() + " kr" },
                    nynorsk { + avkortingsbelopSerkullPerAr.format() + " kr" },
                )
            }
        }

        showIf(((skalViseSerkullBarnetillegg) and justeringsbelopSerkullPerAr.notEqualTo(0))){
            paragraph {
                showIf((justeringsbelopSerkullPerAr.greaterThan(0))){
                    text (
                        bokmal { + "-" },
                        nynorsk { + "-" },
                    )
                }

                showIf((justeringsbelopSerkullPerAr.lessThan(0))){
                    text (
                        bokmal { + "+" },
                        nynorsk { + "+" },
                    )
                }
                text (
                    bokmal { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                    nynorsk { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                )
                text (
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar_utenminus().format() + " kr" },
                    nynorsk { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg and (barnetilleggSerkullNetto.notEqualTo(0) or (barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                    nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                )
                text (
                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format() + " kr" },
                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg and (barnetilleggSerkullNetto.notEqualTo(0) or (barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.notEqualTo(0))))){
            paragraph {
                text (
                    bokmal { + "Utbetaling av barnetillegg per måned " },
                    nynorsk { + "Utbetaling av barnetillegg per månad " },
                )
                text (
                    bokmal { + barnetilleggSerkullNetto.format() + " kr" },
                    nynorsk { + barnetilleggSerkullNetto.format() + " kr" },
                )
            }
        }

        showIf((skalViseSerkullBarnetillegg and barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.equalTo(0))){
            paragraph {
                text (
                    bokmal { + "Grensen for å få utbetalt barnetillegg" },
                    nynorsk { + "Grensa for å få utbetalt barnetillegg" },
                )
                text (
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kr" },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kr" },
                )
            }
        }

        showIf((harBarnetilleggSerkull and barnetilleggSerkullNetto.greaterThan(0) and skalViseSerkullBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Du vil få utbetalt " + barnetilleggSerkullNetto.format() + " kroner i måneden før skatt i barnetillegg" },
                    nynorsk { + "Du vil få utbetalt " + barnetilleggSerkullNetto.format() + " kroner i månaden før skatt i barnetillegg" },
                )

                showIf((harBarnetilleggFelles and harBarnetilleggSerkull and pe.ut_etteroppgjor_bt_utbetalt())){
                    text (
                        bokmal { + " for " + pe.ut_barnet_barna_serkull() + " som ikke bor med begge sine foreldre" },
                        nynorsk { + " for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra" },
                    )
                }
                text (
                    bokmal { + ". " },
                    nynorsk { + ". " },
                )
            }
        }

        showIf((barnetilleggSerkullNetto.equalTo(0) and skalViseSerkullBarnetillegg)){
            paragraph {
                showIf((pe.ut_tbu611_far_ikke())){
                    text (
                        bokmal { + "Du får ikke utbetalt barnetillegget " },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget " },
                    )
                }

                showIf((pe.ut_tbu611_far_ikke() and harBarnetilleggFelles and harBarnetilleggSerkull)){
                    text (
                        bokmal { + "for " + pe.ut_barnet_barna_serkull() + " som ikke bor med begge sine foreldre " },
                        nynorsk { + "for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra " },
                    )
                }

                showIf((pe.ut_tbu611_far_ikke())){
                    text (
                        bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                        nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                    )
                }

                showIf((barnetilleggSerkullNetto.equalTo(0) and justeringsbelopSerkullPerAr.notEqualTo(0))){
                    text (
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                    )
                }
            }
        }

        showIf((harGjenlevendetillegg)){
            title1 {
                text (
                    bokmal { + "Slik beregner vi gjenlevendetillegget ditt" },
                    nynorsk { + "Slik bereknar vi attlevandetillegget ditt" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Når vi beregner gjenlevendetillegget ditt sammenligner vi det du har rett til i uførepensjon beregnet med avdødes rettigheter, og det du har rett til i uførepensjon ut fra egen opptjening. Vi regner om månedsbeløpene for desember 2014 til årsbeløp. Differansen mellom disse beløpene skal legges til grunn- og tilleggspensjonen din ut fra egen opptjening. Vi regner om grunn- og tilleggspensjonen din ut fra egen opptjening til 100 prosent uføregrad. For å ta hensyn til at skatten øker, blir det samlede beløpet justert opp etter overgangsregler. Dette er beregningsgrunnlaget ditt med gjenlevenderettighet." },
                    nynorsk { + "Når vi bereknar attlevandetillegget ditt, samanliknar vi det du har rett til i uførepensjon berekna med rettane til den avdøde, og det du har rett til i uførepensjon ut frå eiga opptening. Vi reknar om månadsbeløpa for desember 2014 til årsbeløp. Differansen mellom desse beløpa skal leggjast til grunn- og tilleggspensjonen din ut frå eiga opptening. Vi reknar om grunn- og tilleggspensjonen din ut frå di eiga opptening til 100 prosent uføregrad. For å ta omsyn til at skatten aukar, blir det samla beløpet justert opp etter overgangsreglar. Dette er berekningsgrunnlaget ditt med attlevanderett." },
                )
            }
            paragraph {
                text (
                    bokmal { + "Beregningsgrunnlaget ditt ut fra egen opptjening skal justeres for trygdetid og trekkes fra beregningsgrunnlaget med gjenlevenderettighet. Denne differansen deles deretter på uføregraden din, og 66 prosent av dette vil utgjøre gjenlevendetillegget ditt. Tillegget vil til slutt justeres ut fra uføregraden din." },
                    nynorsk { + "Berekningsgrunnlaget ditt ut frå eiga opptening skal justerast for trygdetid og trekkjast frå berekningsgrunnlaget med attlevanderett. Denne differansen blir deretter delt på uføregraden din, og 66 prosent av dette vil utgjere attlevandetillegget ditt. Tillegget blir til slutt justert ut frå uføregraden din." },
                )
            }
        }

        showIf((harEktefelletillegg and erIkkeSoknadBarnetillegg)){
            title1 {
                text (
                    bokmal { + "For deg som mottar ektefelletillegg" },
                    nynorsk { + "For deg som får ektefelletillegg" },
                )
            }
        }

        showIf((harEktefelletillegg and erIkkeSoknadBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Ektefelletillegget blir utbetalt som et fast tillegg ved siden av uføretrygden. Tillegget blir ikke endret i perioden ektefelletillegget er innvilget." },
                    nynorsk { + "Ektefelletillegget blir utbetalt som eit fast tillegg ved sida av uføretrygda. Tillegget blir ikkje endra i den perioden ektefelletillegget er innvilga for." },
                )
            }
        }

        showIf(harEktefelletillegg){
            paragraph {
                text (
                    bokmal { + "Når vi beregner ektefelletillegget tar vi utgangspunkt i den årlige uførepensjonen du har rett til i desember 2014. Deretter regner vi ut tillegget ut fra fastsatte overgangsregler. " },
                    nynorsk { + "Når vi bereknar ektefelletillegget, tek vi utgangspunkt i den årlege uførepensjonen du har rett til i desember 2014. Deretter reknar vi ut tillegget ut frå fastsette overgangsreglar. " },
                )

                showIf((harEktefelletillegg and erIkkeSoknadBarnetillegg)){
                    text (
                        bokmal { + "Du kan beholde ektefelletillegget ut vedtaksperioden, men det opphører senest 31. desember 2024." },
                        nynorsk { + "Du kan behalde ektefelletillegget ut vedtaksperioden, men det tek slutt seinast 31. desember 2024." },
                    )
                }
            }
        }

        showIf((harEktefelletillegg and erIkkeSoknadBarnetillegg)){
            paragraph {
                text (
                    bokmal { + "Ektefelletillegget vil falle bort hvis du skiller deg, uføretrygden opphører eller hvis ektefellen din dør." },
                    nynorsk { + "Ektefelletillegget fell bort dersom du skil deg, uføretrygda tek slutt eller dersom ektefellen din døyr." },
                )
            }
        }

        showIf((brevkode.equalTo("PE_UT_04_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().equalTo(3.76) and harEktefelletillegg)){
            paragraph {
                text (
                    bokmal { + "Du har minstepensjon og tillegg for ektefelle som har fylt 60 år. Du har derfor rett til å motta uføretrygd som minst tilsvarer 3,76 ganger folketrygdens grunnbeløp. Dette grunnlaget justeres ut fra uføregraden og trygdetiden din, og du beholder dette ut vedtaksperioden for ektefelletillegget. Etter dette vil vi beregne uføretrygden etter ordinære regler." },
                    nynorsk { + "Du har minstepensjon og tillegg for ektefelle som har fylt 60 år. Du har derfor rett til å få uføretrygd som minst tilsvarer 3,76 gonger grunnbeløpet i folketrygda. Dette grunnlaget blir justert ut frå uføregraden din og trygdetida di, og du beheld dette ut vedtaksperioden for ektefelletillegget. Etter dette bereknar vi uføretrygda etter ordinære reglar." },
                )
            }
        }

        showIf(((erIkkeSoknadBarnetillegg))){
            paragraph {
                text (
                    bokmal { + "Etteroppgjør av uføretrygd" },
                    nynorsk { + "Etteroppgjer av uføretrygd" },
                )

                showIf(((harBarnetilleggFelles or harBarnetilleggSerkull))){
                    text (
                        bokmal { + " og barnetillegg" },
                        nynorsk { + " og barnetillegg" },
                    )
                }
            }
        }

        showIf(((erIkkeSoknadBarnetillegg))){
            paragraph {
                text (
                    bokmal { + "Hvert år når likningen er klar mottar vi opplysninger om inntekten" },
                    nynorsk { + "Kvart år når likninga er klar får vi opplysningar om inntekta" },
                )

                showIf(((harBarnetilleggFelles))){
                    text (
                        bokmal { + " til deg og din " },
                        nynorsk { + " til deg og din " },
                    )
                }

                showIf(((harBarnetilleggFelles and (sivilstand.equalTo("bormed ektefelle"))))){
                    text (
                        bokmal { + "ektefelle" },
                        nynorsk { + "ektefelle" },
                    )
                }

                showIf(((harBarnetilleggFelles and (sivilstand.equalTo("bormed registrert partner"))))){
                    text (
                        bokmal { + "partner" },
                        nynorsk { + "partnaren" },
                    )
                }

                showIf(((harBarnetilleggFelles and (sivilstand.equalTo("bormed 1-5") or sivilstand.equalTo("bormed 3-2"))))){
                    text (
                        bokmal { + "samboer" },
                        nynorsk { + "sambuar" },
                    )
                }

                showIf(not(harBarnetilleggFelles)){
                    text (
                        bokmal { + " din" },
                        nynorsk { + " di" },
                    )
                }
                text (
                    bokmal { + " fra Skatteetaten. Vi bruker likningsopplysningene til å beregne riktig utbetaling av uføretrygd" },
                    nynorsk { + " frå Skatteetaten. Vi brukar likningsopplysningane til å berekne riktig utbetaling av uføretrygd" },
                )

                showIf(((harBarnetilleggFelles or harBarnetilleggSerkull))){
                    text (
                        bokmal { + " og barnetillegg" },
                        nynorsk { + " og barnetillegg" },
                    )
                }
                text (
                    bokmal { + " for året likningen gjelder for. Har du fått for mye eller for lite utbetalt i uføretrygd" },
                    nynorsk { + " for året likninga gjeld for. Har du fått for mykje eller for lite utbetalt i uføretrygd" },
                )

                showIf(((harBarnetilleggFelles or harBarnetilleggSerkull))){
                    text (
                        bokmal { + " og barnetillegg" },
                        nynorsk { + " og barnetillegg" },
                    )
                }
                text (
                    bokmal { + ", vil vi foreta et etteroppgjør. " },
                    nynorsk { + ", vil vi foreta eit etteroppgjer. " },
                )

                showIf(pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo("nor") and pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo("")){
                    text (
                        bokmal { + "Har du meldt inn inntekt fra arbeid i et annet land enn Norge, og vi ikke mottar inntektsopplysninger fra Skatteetaten, gjør vi etteroppgjøret ut fra inntekten din fra utlandet. " },
                        nynorsk { + "Har du meldt inn inntekt frå arbeid i eit anna land enn Noreg, og vi ikkje får opplysningar om inntekt frå Skatteetaten, gjer vi etteroppgjeret ut frå inntekta di frå utlandet. " },
                    )
                }
                text (
                    bokmal { + "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mye, må du betale dette tilbake. Det er viktig at du melder fra om inntektsendringer slik at uføretrygden" },
                    nynorsk { + "Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mye, må du betale dette tilbake. Det er viktig at du melder frå om inntektsendringar slik at uføretrygda" },
                )

                showIf(harBarnetilleggFelles or harBarnetilleggSerkull){
                    text (
                        bokmal { + " og barnetillegget" },
                        nynorsk { + " og barnetillegg" },
                    )
                }
                text (
                    bokmal { + " blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL." },
                    nynorsk { + " blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på $NAV_URL." },
                )
            }
        }
    }
}
