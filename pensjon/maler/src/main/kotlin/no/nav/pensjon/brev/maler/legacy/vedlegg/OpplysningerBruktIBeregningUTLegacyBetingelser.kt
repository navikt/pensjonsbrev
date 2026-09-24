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
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.*

/*
 * Navngitte betingelser og navigasjonskjeder for vedleggOpplysningerBruktIBeregningUTLegacy.
 *
 * Betingelsene er hentet ordrett fra det Exstream-avledede vedlegget (kun `pe.`-prefikset er fjernet
 * siden funksjonene er extensions på Expression<PEgruppe10>). Uttrykkene er strukturelt identiske med
 * originalen, så rendret output er uendret — dette er kun for lesbarhet og gjenbruk. TBU-referanser er
 * beholdt i KDoc for sporbarhet mot Exstream.
 */

// --- Navigasjonskjeder til trygdetidslistene (Persongrunnlag) ---

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

/** Gate for omregning av uførepensjon til uføretrygd (auto & manuell) – kun PE_UT_04_300 og PE_UT_14_300. */
fun Expression<PEgruppe10>.skalViseOmregningUPtilUT(): Expression<Boolean> =
    pebrevkode().equalTo("PE_UT_04_300") or pebrevkode().equalTo("PE_UT_14_300")

/**
 * Gate for generell ektefelletillegg-tekst (tittel, "faller bort" og "behold ut vedtaksperioden").
 * Ordrett kopi av betingelsen som lå duplisert 3× i ForDegSomMottarEktefelletillegg (kun `pe.`-prefiks
 * fjernet) – strukturelt identisk, så rendret output er uendret.
 */
fun Expression<PEgruppe10>.skalViseEktefelletilleggGenerell(): Expression<Boolean> =
    pebrevkode().notEqualTo("PE_UT_04_101") and
        vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and
        erIkkeSoknadOmBarnetillegg() and
        harIkkeBarnetilleggBrevkode() and
        (pebrevkode().notEqualTo("PE_UT_04_102") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))

/**
 * DATASTYRT gate: kravårsaken er ikke "søknad om barnetillegg". Dette er ekte vedtaksdata og
 * skal overleve porten til `vedtaksdata` (i motsetning til brevkode-gatene som forsvinner ved
 * variant-splitting).
 */
fun Expression<PEgruppe10>.erIkkeSoknadOmBarnetillegg(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")

/**
 * BREVKODE-STYRT gate: brevet er ikke et barnetillegg-brev (04_108/04_109/07_200/06_300).
 * Ren brevkode-routing – forsvinner ved variant-splitting (barnetillegg-variantene inkluderer/
 * utelater seksjonene eksplisitt i stedet). Holdt adskilt fra den datastyrte kravårsak-sjekken
 * med vilje, slik at brevkode-halvdelen er lett å fjerne mekanisk når variantene lages.
 */
fun Expression<PEgruppe10>.harIkkeBarnetilleggBrevkode(): Expression<Boolean> =
    pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300")

/** Gate for TBU034V-036V (rett før inntektsseksjonen). */
fun Expression<PEgruppe10>.skalViseGrunnbeloepOgYrkesskadeForklaring(): Expression<Boolean> =
    erIkkeSoknadOmBarnetillegg() and harIkkeBarnetilleggBrevkode()

/** Gate for seksjonen "Dette er inntektene vi har brukt i beregningen din" (TBU037V/038V). */
fun Expression<PEgruppe10>.skalViseInntekterBruktIBeregning(): Expression<Boolean> =
    not(ut_uforetidspunkt_foer_17()) and
        not(vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) and
        erIkkeSoknadOmBarnetillegg() and
        pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300", "PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_300", "PE_UT_14_300") and
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
        erIkkeSoknadOmBarnetillegg() and
        ((pebrevkode().equalTo("PE_UT_04_101") or pebrevkode().equalTo("PE_UT_04_114")) or
            (pebrevkode().notEqualTo("PE_UT_05_100") and
                pebrevkode().notEqualTo("PE_UT_07_100") and
                vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40))) and
        pebrevkode().notEqualTo("PE_UT_04_108") and
        pebrevkode().notEqualTo("PE_UT_04_109") and
        grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().notNull()

/** Gate for "Slik har vi fastsatt den nye inntektsgrensen din" (TBUxx4v og TBU048V-TBU055V). */
fun Expression<PEgruppe10>.skalViseInntektsgrenseOgAvkortning(): Expression<Boolean> =
    (pebrevkode().notEqualTo("PE_UT_07_100") and pebrevkode().notEqualTo("PE_UT_05_100") and pebrevkode().notEqualTo("PE_UT_04_115") and pebrevkode().notEqualTo("PE_UT_04_103") and pebrevkode().notEqualTo("PE_UT_06_100") and pebrevkode().notEqualTo("PE_UT_04_300") and pebrevkode().notEqualTo("PE_UT_14_300") and pebrevkode().notEqualTo("PE_UT_07_200") and pebrevkode().notEqualTo("PE_UT_06_300") and (vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().notEqualTo("") or vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse().notEqualTo(""))) or (vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) or (pebrevkode().equalTo("PE_UT_04_108") or pebrevkode().equalTo("PE_UT_04_109") and vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0)) and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu")

/** Gate for "Etteroppgjør av uføretrygd og barnetillegg" (TBU052V-TBU073V). */
fun Expression<PEgruppe10>.skalViseEtteroppgjoer(): Expression<Boolean> =
    (vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pebrevkode().notEqualTo("PE_UT_04_108") and pebrevkode().notEqualTo("PE_UT_04_109") and pebrevkode().notEqualTo("PE_UT_07_200") and (pebrevkode().notEqualTo("PE_UT_04_102") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))) or pebrevkode().equalTo("PE_UT_06_300")
