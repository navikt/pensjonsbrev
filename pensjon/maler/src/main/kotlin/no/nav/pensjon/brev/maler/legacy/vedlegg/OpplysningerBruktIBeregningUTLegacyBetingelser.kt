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
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.selectors.vedtaksdata.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.selectors.beregningsData.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.beregningUfore.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.belopsendring.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.barnetilleggFellesYK.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.selectors.barnetilleggSerkullYK.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.kravhode.selectors.kravhode.*
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

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

fun Expression<PEgruppe10>.oppfyllerDoedsfallsvilkaar(
    utelatVedDoedsfall: Expression<Boolean>,
): Expression<Boolean> =
    not(utelatVedDoedsfall) or
        (utelatVedDoedsfall and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))

/**
 * Gate for generell ektefelletillegg-tekst (tittel, "faller bort" og "behold ut vedtaksperioden").
 * Kombinerer datavilkår med eksplisitte innholdsvalg fra kallstedet.
 * Forutsetter at ektefelletillegg er innvilget.
 */
fun Expression<PEgruppe10>.skalViseEktefelletilleggGenerell(
    visGenerellForklaring: Expression<Boolean>,
    utelatVedDoedsfall: Expression<Boolean>,
): Expression<Boolean> =
    visGenerellForklaring and
        erIkkeSoknadOmBarnetillegg() and
        oppfyllerDoedsfallsvilkaar(utelatVedDoedsfall)

/**
 * DATASTYRT gate: kravårsaken er ikke "søknad om barnetillegg". Dette er ekte vedtaksdata og
 * skal overleve porten til `vedtaksdata` (i motsetning til brevkode-gatene som forsvinner ved
 * variant-splitting).
 */
fun Expression<PEgruppe10>.erIkkeSoknadOmBarnetillegg(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")

/**
 * Tidl. `ut_tbu069v` (Exstream TBU069V): innvilget barnetillegg som faktisk er utbetalt, med
 * brevtypevalg og dødsfallsunntak fra kallstedet.
 */
fun Expression<PEgruppe10>.skalViseBarnetilleggsberegning(
    visForklaringForBrevtype: Expression<Boolean>,
    utelatVedDoedsfall: Expression<Boolean>,
): Expression<Boolean> =
    (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or
        vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and
        (visForklaringForBrevtype and oppfyllerDoedsfallsvilkaar(utelatVedDoedsfall)) and
        ut_etteroppgjor_bt_utbetalt()

/**
 * Tidl. `ut_tbu606v_tbu611v` (Exstream TBU606V-TBU611V): som `skalViseBarnetilleggsberegning`, men
 * vises også ved inntektsendring med endret barnetilleggsbeløp, uavhengig av brevtype.
 */
fun Expression<PEgruppe10>.skalViseBarnetilleggsstoerrelse(
    visForklaringForBrevtype: Expression<Boolean>,
    utelatVedDoedsfall: Expression<Boolean>,
): Expression<Boolean> =
    (
        (
            (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or
                vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and
                (visForklaringForBrevtype and oppfyllerDoedsfallsvilkaar(utelatVedDoedsfall))
            ) or
            (
                vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and
                    (vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopgammelbtfb()
                        .notEqualTo(vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopnybtfb()) or
                        vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopgammelbtsb()
                            .notEqualTo(vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopnybtsb()))
                )
        ) and
        ut_etteroppgjor_bt_utbetalt()

/** TBU501V: innvilget barnetillegg og dødsfallsunntak, med brevtypevalg fra kallstedet. */
fun Expression<PEgruppe10>.skalViseRettTilBarnetillegg(
    visForklaringForBrevtype: Expression<Boolean>,
    utelatVedDoedsfall: Expression<Boolean>,
): Expression<Boolean> =
    (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or
        vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and
        visForklaringForBrevtype and
        oppfyllerDoedsfallsvilkaar(utelatVedDoedsfall)

/**
 * DATASTYRT gate: anvendt trygdetid er lavere enn full opptjening (40 år), slik at trygdetiden må
 * forklares i brevet.
 */
fun Expression<PEgruppe10>.harIkkeFullTrygdetid(): Expression<Boolean> =
    vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40)

/**
 * Gate for trygdetidsseksjonen ("Dette er trygdetiden din" + "redusert framtidig trygdetid").
 * Vises hvis kravårsaken ikke er søknad om barnetillegg, brevet ikke er et barnetillegg-brev, og
 * enten brevtypen alltid viser trygdetid (04_101/04_114) eller brevet ikke er et inntektsendringsbrev
 * og trygdetiden er lavere enn full opptjening.
 */
fun Expression<PEgruppe10>.ut_trygdetid(profil: OpplysningerBruktIBeregningUTProfil): Expression<Boolean> =
    erIkkeSoknadOmBarnetillegg() and
            (!profil.erBarnetilleggsbrev).expr() and
            (
                    profil.visTrygdetidUansett.expr() or
                            ((!profil.erInntektsendring).expr() and harIkkeFullTrygdetid())
                    )

/** Gate for TBU034V-036V (rett før inntektsseksjonen). */
fun Expression<PEgruppe10>.skalViseGrunnbeloepOgYrkesskadeForklaring(profil: OpplysningerBruktIBeregningUTProfil): Expression<Boolean> =
    erIkkeSoknadOmBarnetillegg() and (!profil.erBarnetilleggsbrev).expr()

/** Gate for seksjonen "Dette er inntektene vi har brukt i beregningen din" (TBU037V/038V). */
fun Expression<PEgruppe10>.skalViseInntekterBruktIBeregning(profil: OpplysningerBruktIBeregningUTProfil): Expression<Boolean> =
    not(ut_uforetidspunkt_foer_17()) and
        not(vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) and
        erIkkeSoknadOmBarnetillegg() and
        profil.visInntekterBruktIBeregning.expr() and
        oppfyllerDoedsfallsvilkaar(profil.utelatVedDoedsfall.expr())

/** Datavilkår for avdødes inntektsgrunnlag, med brevtypevalg fra kallstedet. */
fun Expression<PEgruppe10>.skalViseInntektsgrunnlagAvdoed(
    utelatVedDoedsfall: Expression<Boolean>,
): Expression<Boolean> =
    (vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and
        erIkkeSoknadOmBarnetillegg() and
        vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and
        not(vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_opptjeningutliste_opptjeningut_ar().notEqualTo(0)) and
        oppfyllerDoedsfallsvilkaar(utelatVedDoedsfall)

/** Gate for tabellen med norsk trygdetid (under "Dette er trygdetiden din"). */
fun Expression<PEgruppe10>.skalViseTrygdetidNorTabell(erMndEtterFoedsel: Expression<Boolean>): Expression<Boolean> =
    not(erMndEtterFoedsel) and
        (ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40) or vedtaksdata_kravhode_boddarbeidutland()) and
        grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()

/** Gate for tabellen med EØS-trygdetid. */
fun Expression<PEgruppe10>.skalViseTrygdetidEOSTabell(erMndEtterFoedsel: Expression<Boolean>): Expression<Boolean> =
    not(erMndEtterFoedsel) and vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().greaterThan(0)

/** Gate for tabellen med bilateral trygdetid (TBU046V). */
fun Expression<PEgruppe10>.skalViseTrygdetidBilateralTabell(
    erMndEtterFoedsel: Expression<Boolean>,
    profil: OpplysningerBruktIBeregningUTProfil,
): Expression<Boolean> =
    not(erMndEtterFoedsel) and
        erIkkeSoknadOmBarnetillegg() and
        (profil.visTrygdetidUansett.expr() or
            ((!profil.erInntektsendring).expr() and
                vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40))) and
        (!profil.erNyBeregningAvBarnetillegg).expr() and
        grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().notNull()

/** Gate for "Slik har vi fastsatt den nye inntektsgrensen din" (TBUxx4v og TBU048V-TBU055V). */
fun Expression<PEgruppe10>.skalViseInntektsgrenseOgAvkortning(profil: OpplysningerBruktIBeregningUTProfil): Expression<Boolean> =
    (profil.visInntektsgrenseVedBegrunnelse.expr() and (vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().notEqualTo("") or vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse().notEqualTo(""))) or (vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) or (profil.erNyBeregningAvBarnetilleggAuto.expr() or profil.erNyBeregningAvBarnetilleggManuell.expr() and vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0)) and vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu")

/** Gate for "Etteroppgjør av uføretrygd og barnetillegg" (TBU052V-TBU073V). */
fun Expression<PEgruppe10>.skalViseEtteroppgjoer(profil: OpplysningerBruktIBeregningUTProfil): Expression<Boolean> =
    (vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and (!profil.erBarnetilleggsbrevUtenEndring).expr() and oppfyllerDoedsfallsvilkaar(profil.utelatVedDoedsfall.expr())) or profil.erEndringAvBarnetillegg.expr()

// --- Seksjon: "Slik beregner vi utbetaling av uføretrygden når inntekten din endres" ---

/** DATASTYRT: kravårsaken er inntektsendring og det utbetalte uførebeløpet er faktisk endret. */
fun Expression<PEgruppe10>.erInntektsendringMedEndretUtbetaling(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and
        vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut()
            .notEqualTo(vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut())

/** Gate for tittel + intro i inntektsendrings-utbetalingsseksjonen. */
fun Expression<PEgruppe10>.skalViseUtbetalingVedInntektsendringTittel(): Expression<Boolean> =
    erInntektsendringMedEndretUtbetaling() and
        bunnfradragUnderInntektstak()

/** Gate for detalj-avsnittene (reduksjon + bunnfradrag) i samme seksjon (identisk gate for begge). */
fun Expression<PEgruppe10>.skalViseUtbetalingVedInntektsendringDetaljer(): Expression<Boolean> =
    erInntektsendringMedEndretUtbetaling() and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
            .greaterThanOrEqual(vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()) and
        bunnfradragUnderInntektstak() and
        vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0)

// --- Seksjon: "For deg som mottar ektefelletillegg" (midtre avsnitt + minstepensjon) ---

/** Gate for minstepensjons-avsnittet (3,76 G). Forutsetter at ektefelletillegg er innvilget. */
fun Expression<PEgruppe10>.skalViseEktefelletilleggMinstepensjon(visMinstepensjonsforklaring: Expression<Boolean>): Expression<Boolean> =
    visMinstepensjonsforklaring and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().equalTo(3.76)

// --- Seksjon: "Slik beregner vi gjenlevendetillegget ditt" (generell vs. omregning up→ut) ---

/** DATA: gjenlevendetillegg er innvilget. Felles forutsetning for begge gjenlevendetillegg-avsnittene (overlever porten). */
fun Expression<PEgruppe10>.harGjenlevendetilleggInnvilget(): Expression<Boolean> =
    vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()

/**
 * Vilkår for det GENERELLE (uføretrygd-baserte) gjenlevendetillegg-avsnittet, gitt at tillegget er
 * innvilget. Brukes som `orShowIf`-fallback etter omregningsforklaringen.
 * Kombinerer datavilkår med et eksplisitt innholdsvalg fra kallstedet.
 */
fun Expression<PEgruppe10>.harGenereltGjenlevendetilleggKrav(visGenerellForklaring: Expression<Boolean>): Expression<Boolean> =
    erIkkeSoknadOmBarnetillegg() and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg() and
        visGenerellForklaring

// --- Seksjon: "Slik beregner vi uføretrygden din" (fullt beregningsbrev, A-klyngen) ---

/**
 * Felles ytre gate for de to "Slik beregner vi uføretrygden din"-avsnittene (ordinær og konvertert).
 * Dette er A-klyngen "fullt beregningsbrev": ikke omregning (04_300/14_300), ikke inntektsendring
 * (05_100/07_100), ikke søknad om barnetillegg (DATA) og ikke barnetilleggsbrev
 * (04_108/04_109/07_200/06_300).
 */
fun Expression<PEgruppe10>.erFulltBeregningsbrev(profil: OpplysningerBruktIBeregningUTProfil): Expression<Boolean> =
    not(profil.erOmregningUPtilUT.expr()) and
        (!profil.erInntektsendring).expr() and
        erIkkeSoknadOmBarnetillegg() and
        (!profil.erBarnetilleggsbrev).expr()

// --- skalViseReduksjonsprosentavsnitt (tidl. Exstream TBU056V; flyttet hit fra LegacyFunksjoner, brukes kun i dette vedlegget) ---

/** DATASTYRT: inntektstaket ligger over bunnfradraget (avkortning er aktuell). Overlever porten. */
fun Expression<PEgruppe10>.bunnfradragUnderInntektstak(): Expression<Boolean> =
    vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
        .lessThan(vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak())

/**
 * TBU056V (Exstream): gate for reduksjonsprosent-/inntektsavsnittene. Dekomponert til navngitte deler
 * (identisk output): enten er dette en beregningsendrings-brevkode, ELLER en reell inntektsendring med
 * endret utbetaling (DATA) – og brevet er ikke en søknad om barnetillegg (DATA), og inntektstaket er
 * over bunnfradraget (DATA). Brevtypevalget kommer fra kallstedet; resten er datastyrt og overlever porten.
 */
fun Expression<PEgruppe10>.skalViseReduksjonsprosentavsnitt(erBeregningsendring: Expression<Boolean>): Expression<Boolean> =
    (erBeregningsendring or erInntektsendringMedEndretUtbetaling()) and
        erIkkeSoknadOmBarnetillegg() and
        bunnfradragUnderInntektstak()

/**
 * DATASTYRT (tidl. Exstream TBU601V/TBU604V): kravårsaken er inntektsendring og barnetilleggsbeløpet
 * (felles- eller særkullsbarn) er faktisk endret. Brukes kun i dette vedlegget.
 */
fun Expression<PEgruppe10>.erInntektsendringMedEndretBarnetillegg(): Expression<Boolean> {
    val belopsendring = vedtaksbrev.safe { vedtaksdata }.safe { beregningsdata }.safe { beregningufore }.safe { belopsendring }
    return vedtaksbrev.safe { vedtaksdata }.safe { kravhode }.safe { kravarsaktype }.equalTo("endret_inntekt") and
            (belopsendring.safe { barnetilleggfellesyk }.safe { belopgammelbtfb.ifNull(Kroner(0)) }.notEqualTo(belopsendring.safe { barnetilleggfellesyk }.safe { belopnybtfb.ifNull(Kroner(0)) }) or
                    belopsendring.safe { barnetilleggserkullyk }.safe { belopgammelbtsb.ifNull(Kroner(0)) }.notEqualTo(belopsendring.safe { barnetilleggserkullyk }.safe { belopnybtsb.ifNull(Kroner(0)) }))
}
