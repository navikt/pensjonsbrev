@file:Suppress("FunctionName")

package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.selectors.grunnlag.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.selectors.persongrunnlag.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.selectors.trygdetidsgrunnlagListeBilateral.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.selectors.trygdetidsgrunnlagListeEOS.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.selectors.trygdetidsgrunnlagListeNor.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.selectors.vedtaksbrev.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.barnetilleggFellesYK.belopgammelbtfb
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.barnetilleggFellesYK.belopnybtfb
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.barnetilleggSerkullYK.belopgammelbtsb
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.barnetilleggSerkullYK.belopnybtsb
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.belopsendring.barnetilleggfellesyk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.belopsendring.barnetilleggserkullyk
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.beregningUfore.belopsendring
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.selectors.beregningsData.beregningufore
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.kravhode.selectors.kravhode.kravarsaktype
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.selectors.vedtaksdata.beregningsdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.selectors.vedtaksdata.kravhode
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun Expression<PEgruppe10>.ut_trygdetid(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and
            pebrevkode().notEqualTo("PE_UT_04_108") and
            pebrevkode().notEqualTo("PE_UT_04_109") and
            pebrevkode().notEqualTo("PE_UT_07_200") and
            pebrevkode().notEqualTo("PE_UT_06_300") and
            (
                    (pebrevkode().equalTo("PE_UT_04_101") or pebrevkode().equalTo("PE_UT_04_114")) or
                            (pebrevkode().notEqualTo("PE_UT_05_100") and pebrevkode().notEqualTo("PE_UT_07_100")
                                    and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40))
                    )


fun Expression<PEgruppe10>.ut_tbu056v() = (
        pebrevkode().equalTo("PE_UT_04_102")
                or pebrevkode().equalTo("PE_UT_04_116")
                or pebrevkode().equalTo("PE_UT_04_101")
                or pebrevkode().equalTo("PE_UT_04_114")
                or pebrevkode().equalTo("PE_UT_04_300")
                or pebrevkode().equalTo("PE_UT_14_300")
                or pebrevkode().equalTo("PE_UT_04_500")
                or (vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")
                and vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(
            vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()
        )
                )
        ) and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(
    vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
)

fun Expression<PEgruppe10>.pe_ut_tbu601v_tbu604v(): Expression<Boolean> {
    val belopsendring = vedtaksbrev.safe { vedtaksdata }.safe { beregningsdata }.safe { beregningufore }.safe { belopsendring }
    return vedtaksbrev.safe { vedtaksdata }.safe { kravhode }.safe { kravarsaktype }.equalTo("endret_inntekt") and
            (belopsendring.safe { barnetilleggfellesyk }.safe { belopgammelbtfb.ifNull(Kroner(0)) }.notEqualTo(belopsendring.safe { barnetilleggfellesyk }.safe { belopnybtfb.ifNull(Kroner(0)) }) or
                    belopsendring.safe { barnetilleggserkullyk }.safe { belopgammelbtsb.ifNull(Kroner(0)) }.notEqualTo(belopsendring.safe { barnetilleggserkullyk }.safe { belopnybtsb.ifNull(Kroner(0)) }))
}

/** Norsk trygdetidsgrunnlagsliste, eller null hvis den mangler. */
fun Expression<PEgruppe10>.trygdetidNorListe() =
    safe { vedtaksbrev }.safe { grunnlag }.safe { persongrunnlagsliste }.getOrNull()
        .safe { trygdetidsgrunnlaglistenor }.safe { trygdetidsgrunnlag }

/** EØS-trygdetidsgrunnlagsliste, eller null hvis den mangler. */
fun Expression<PEgruppe10>.trygdetidEOSListe() =
    safe { vedtaksbrev }.safe { grunnlag }.safe { persongrunnlagsliste }.getOrNull()
        .safe { trygdetidsgrunnlaglisteeos }.safe { trygdetidsgrunnlageos }

/** Bilateral trygdetidsgrunnlagsliste, eller null hvis den mangler. */
fun Expression<PEgruppe10>.trygdetidBilateralListe() =
    safe { vedtaksbrev }.safe { grunnlag }.safe { persongrunnlagsliste }.getOrNull()
        .safe { trygdetidsgrunnlaglistebilateral }.safe { trygdetidsgrunnlagbilateral }

// --- Seksjonsbetingelser ---

/** Gate for TBU034V-036V (rett før inntektsseksjonen). */
fun Expression<PEgruppe10>.skalViseGrunnbeloepOgYrkesskadeForklaring(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and
        pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300")

/** Gate for seksjonen "Dette er inntektene vi har brukt i beregningen din" (TBU037V/038V). */
fun Expression<PEgruppe10>.skalViseInntekterBruktIBeregning(): Expression<Boolean> =
    not(ut_uforetidspunkt_foer_17()) and
        not(vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) and
        vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and
        pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300", "PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_300", "PE_UT_14_300", "PE_UT_04_500") and
        (pebrevkode().notEqualTo("PE_UT_04_102") or vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))

/** Gate for tabellen med norsk trygdetid (under "Dette er trygdetiden din"). */
fun Expression<PEgruppe10>.skalViseTrygdetidNorTabell(erMndEtterFoedsel: Expression<Boolean>): Expression<Boolean> =
    not(erMndEtterFoedsel) and
        (ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40) or vedtaksdata_kravhode_boddarbeidutland()) and
        grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()

/** Gate for tabellen med EØS-trygdetid. */
fun Expression<PEgruppe10>.skalViseTrygdetidEOSTabell(erMndEtterFoedsel: Expression<Boolean>): Expression<Boolean> =
    not(erMndEtterFoedsel) and vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().greaterThan(0)

/** Gate for tabellen med bilateral trygdetid (TBU046V). */
fun Expression<PEgruppe10>.skalViseTrygdetidBilateralTabell(erMndEtterFoedsel: Expression<Boolean>): Expression<Boolean> =
    not(erMndEtterFoedsel) and
        vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and
        ((pebrevkode().equalTo("PE_UT_04_101") or pebrevkode().equalTo("PE_UT_04_114")) or
            (pebrevkode().notEqualTo("PE_UT_05_100") and
                pebrevkode().notEqualTo("PE_UT_07_100") and
                vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40))) and
        pebrevkode().notEqualTo("PE_UT_04_108") and
        pebrevkode().notEqualTo("PE_UT_04_109") and
        grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().notNull()

/** Gate for "Slik har vi fastsatt den nye inntektsgrensen din" (TBUxx4v og TBU048V-TBU055V). */
fun Expression<PEgruppe10>.skalViseInntektsgrenseOgAvkortning(): Expression<Boolean> =
    (pebrevkode().notEqualTo("PE_UT_07_100") and pebrevkode().notEqualTo("PE_UT_05_100") and pebrevkode().notEqualTo("PE_UT_04_115") and pebrevkode().notEqualTo("PE_UT_04_103") and pebrevkode().notEqualTo("PE_UT_06_100") and pebrevkode().notEqualTo("PE_UT_04_300") and pebrevkode().notEqualTo("PE_UT_14_300") and pebrevkode().notEqualTo("PE_UT_07_200") and pebrevkode().notEqualTo("PE_UT_06_300") and (vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().notEqualTo("") or vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse().notEqualTo(""))) or pebrevkode().equalTo("PE_UT_04_500") or (vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) or (pebrevkode().equalTo("PE_UT_04_108") or pebrevkode().equalTo("PE_UT_04_109") and vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0)) and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu")

/** Gate for "Etteroppgjør av uføretrygd og barnetillegg" (TBU052V-TBU073V). */
fun Expression<PEgruppe10>.skalViseEtteroppgjoer(): Expression<Boolean> =
    (vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pebrevkode().notEqualTo("PE_UT_04_108") and pebrevkode().notEqualTo("PE_UT_04_109") and pebrevkode().notEqualTo("PE_UT_04_500") and pebrevkode().notEqualTo("PE_UT_07_200") and (pebrevkode().notEqualTo("PE_UT_04_102") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))) or pebrevkode().equalTo("PE_UT_06_300")
