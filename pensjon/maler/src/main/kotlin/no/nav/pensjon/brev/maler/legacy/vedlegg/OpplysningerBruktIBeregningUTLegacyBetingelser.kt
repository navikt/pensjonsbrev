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

/**
 * DATASTYRT gate: anvendt trygdetid er lavere enn full opptjening (40 år), slik at trygdetiden må
 * forklares i brevet. Ekte vedtaksdata – skal overleve porten til `vedtaksdata` (i motsetning til
 * brevkode-gatene i `ut_trygdetid`, som forsvinner ved variant-splitting).
 */
fun Expression<PEgruppe10>.harIkkeFullTrygdetid(): Expression<Boolean> =
    vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40)

/**
 * BREVKODE-STYRT gate: 04_101 (innvilgelse) og 04_114 (økning uføregrad) viser alltid
 * trygdetidsseksjonen, uavhengig av trygdetidslengde. Ren brevkode-routing – forsvinner ved
 * variant-splitting (disse variantene inkluderer seksjonen eksplisitt).
 */
fun Expression<PEgruppe10>.harTrygdetidBrevkodeUansett(): Expression<Boolean> =
    pebrevkode().equalTo("PE_UT_04_101") or pebrevkode().equalTo("PE_UT_04_114")

/**
 * BREVKODE-STYRT gate: brevet er ikke et inntektsendringsbrev (05_100/07_100). Ren brevkode-routing
 * – forsvinner ved variant-splitting. Holdt adskilt fra datagaten `harIkkeFullTrygdetid` med vilje,
 * slik at brevkode-halvdelen er lett å fjerne mekanisk når variantene lages.
 */
fun Expression<PEgruppe10>.erIkkeInntektsendringBrevkode(): Expression<Boolean> =
    pebrevkode().isNotAnyOf("PE_UT_05_100", "PE_UT_07_100")

/**
 * Gate for trygdetidsseksjonen ("Dette er trygdetiden din" + "redusert framtidig trygdetid").
 * Vises hvis kravårsaken ikke er søknad om barnetillegg, brevet ikke er et barnetillegg-brev, og
 * enten brevkoden alltid viser trygdetid (04_101/04_114) eller brevet ikke er et inntektsendringsbrev
 * og trygdetiden er lavere enn full opptjening. Data- og brevkode-leddene er skilt ut i egne
 * navngitte betingelser over, slik at brevkode-halvdelen kan fjernes mekanisk ved variant-splitting.
 */
fun Expression<PEgruppe10>.ut_trygdetid(): Expression<Boolean> =
    erIkkeSoknadOmBarnetillegg() and
            harIkkeBarnetilleggBrevkode() and
            (
                    harTrygdetidBrevkodeUansett() or
                            (erIkkeInntektsendringBrevkode() and harIkkeFullTrygdetid())
                    )

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

// --- Seksjon: "Slik beregner vi utbetaling av uføretrygden når inntekten din endres" ---

/** DATASTYRT: kravårsaken er inntektsendring og det utbetalte uførebeløpet er faktisk endret. */
fun Expression<PEgruppe10>.erInntektsendringMedEndretUtbetaling(): Expression<Boolean> =
    vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and
        vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut()
            .notEqualTo(vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut())

/**
 * BREVKODE-STYRT gate for inntektsendrings-seksjonen: ikke barnetillegg-brev (04_108/04_109/07_200)
 * og 04_102 kun når kravårsaken ikke er dødsfall. NB: 06_300 er bevisst IKKE ekskludert her (i
 * motsetning til `harIkkeBarnetilleggBrevkode` som ekskluderer hele 4-settet) – avviket er bevart
 * ordrett fra legacy. `tilst_dod`-leddet er egentlig data, men beholdes inline for å være strukturelt
 * identisk med originalen. Forsvinner/reduseres ved variant-splitting.
 */
fun Expression<PEgruppe10>.harInntektsendringBrevkodeUtenBarnetillegg(): Expression<Boolean> =
    pebrevkode().notEqualTo("PE_UT_04_108") and
        pebrevkode().notEqualTo("PE_UT_04_109") and
        pebrevkode().notEqualTo("PE_UT_07_200") and
        (pebrevkode().notEqualTo("PE_UT_04_102") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))

/** Gate for tittel + intro i inntektsendrings-utbetalingsseksjonen. */
fun Expression<PEgruppe10>.skalViseUtbetalingVedInntektsendringTittel(): Expression<Boolean> =
    erInntektsendringMedEndretUtbetaling() and
        bunnfradragUnderInntektstak() and
        harInntektsendringBrevkodeUtenBarnetillegg()

/** Gate for detalj-avsnittene (reduksjon + bunnfradrag) i samme seksjon (identisk gate for begge). */
fun Expression<PEgruppe10>.skalViseUtbetalingVedInntektsendringDetaljer(): Expression<Boolean> =
    erInntektsendringMedEndretUtbetaling() and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
            .greaterThanOrEqual(vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()) and
        bunnfradragUnderInntektstak() and
        vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0) and
        harInntektsendringBrevkodeUtenBarnetillegg()

// --- Seksjon: "For deg som mottar ektefelletillegg" (midtre avsnitt + minstepensjon) ---

/**
 * BREVKODE-STYRT: barnetillegg-brevkodene 04_108/04_109/06_300 (som `harIkkeBarnetilleggBrevkode`,
 * men UTEN 07_200). Midtavsnittet i ForDegSomMottarEktefelletillegg viser bevisst ektefelletillegg-
 * teksten også for 07_200 (opphør av barnetillegg, auto) – avviket er bevart ordrett fra legacy.
 * Forsvinner/reduseres ved variant-splitting.
 */
fun Expression<PEgruppe10>.harIkkeBarnetilleggBrevkodeUtenOpphoer(): Expression<Boolean> =
    pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_06_300")

/** Gate for det midtre avsnittet i "For deg som mottar ektefelletillegg" (omregnings-/behold-tekst). */
fun Expression<PEgruppe10>.skalViseEktefelletilleggMidtreAvsnitt(): Expression<Boolean> =
    (skalViseOmregningUPtilUT() and vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()) or
        (vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget() and
            pebrevkode().notEqualTo("PE_UT_04_101") and
            erIkkeSoknadOmBarnetillegg() and
            harIkkeBarnetilleggBrevkodeUtenOpphoer() and
            (pebrevkode().notEqualTo("PE_UT_04_102") or (pebrevkode().equalTo("PE_UT_04_102") and vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))

/** Gate for minstepensjons-avsnittet (3,76 G) i "For deg som mottar ektefelletillegg". */
fun Expression<PEgruppe10>.skalViseEktefelletilleggMinstepensjon(): Expression<Boolean> =
    pebrevkode().equalTo("PE_UT_04_300") and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().equalTo(3.76) and
        vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()

// --- Seksjon: "Slik beregner vi gjenlevendetillegget ditt" (generell vs. omregning up→ut) ---

/** DATA: gjenlevendetillegg er innvilget. Felles forutsetning for begge gjenlevendetillegg-avsnittene (overlever porten). */
fun Expression<PEgruppe10>.harGjenlevendetilleggInnvilget(): Expression<Boolean> =
    vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()

/**
 * Vilkår for det GENERELLE (uføretrygd-baserte) gjenlevendetillegg-avsnittet, gitt at tillegget er
 * innvilget. Brukes som `orShowIf`-fallback ETTER `skalViseOmregningUPtilUT()`, så `not(omregning)`
 * (04_300/14_300) er implisitt her – ikke gjentatt. Kombinerer DATA (`erIkkeSoknadOmBarnetillegg`,
 * `nyttgjenlevendetillegg`) med BREVKODE-gates: ikke inntektsbrev (05_100/07_100) og ikke
 * barnetillegg-brev (04_108/04_109/07_200 – UTEN 04_102-leddet, jf. avviket ved
 * `harInntektsendringBrevkodeUtenBarnetillegg`). Brevkode-leddene forsvinner ved variant-splitting.
 */
fun Expression<PEgruppe10>.harGenereltGjenlevendetilleggKrav(): Expression<Boolean> =
    erIkkeSoknadOmBarnetillegg() and
        vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg() and
        erIkkeInntektsendringBrevkode() and
        pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200")

// --- Seksjon: "Slik beregner vi uføretrygden din" (fullt beregningsbrev, A-klyngen) ---

/**
 * Felles ytre gate for de to "Slik beregner vi uføretrygden din"-avsnittene (ordinær og konvertert).
 * Dette er A-klyngen "fullt beregningsbrev": ikke omregning (04_300/14_300), ikke inntektsendring
 * (05_100/07_100), ikke søknad om barnetillegg (DATA) og ikke barnetillegg-brevkode
 * (04_108/04_109/07_200/06_300). Brevkode-leddene forsvinner ved variant-splitting; `erIkkeSoknadOmBarnetillegg`
 * er datastyrt og overlever porten.
 */
fun Expression<PEgruppe10>.erFulltBeregningsbrev(): Expression<Boolean> =
    not(skalViseOmregningUPtilUT()) and
        erIkkeInntektsendringBrevkode() and
        erIkkeSoknadOmBarnetillegg() and
        harIkkeBarnetilleggBrevkode()

/**
 * BREVKODE-STYRT: brevet er verken en endring (04_102) eller en økning av uføregrad (04_114).
 * Brukes til å skjule ifu-/reduksjonsberegnings-avsnittet for disse brevtypene. Forsvinner ved
 * variant-splitting.
 */
fun Expression<PEgruppe10>.erIkkeEndringEllerOekningBrevkode(): Expression<Boolean> =
    pebrevkode().isNotAnyOf("PE_UT_04_102", "PE_UT_04_114")

// --- skalViseReduksjonsprosentavsnitt (tidl. Exstream TBU056V; flyttet hit fra LegacyFunksjoner, brukes kun i dette vedlegget) ---

/**
 * BREVKODE-STYRT: brevkoder som representerer en (re)beregning der reduksjonsprosent-/inntektsavsnitt
 * er aktuelt (innvilgelse 04_101, endring 04_102, full eksport 04_116, økning uføregrad 04_114,
 * omregning up→ut 04_300/14_300). Forsvinner ved variant-splitting.
 */
fun Expression<PEgruppe10>.erBeregningsendringsBrevkode(): Expression<Boolean> =
    pebrevkode().isOneOf("PE_UT_04_102", "PE_UT_04_116", "PE_UT_04_101", "PE_UT_04_114", "PE_UT_04_300", "PE_UT_14_300")

/** DATASTYRT: inntektstaket ligger over bunnfradraget (avkortning er aktuell). Overlever porten. */
fun Expression<PEgruppe10>.bunnfradragUnderInntektstak(): Expression<Boolean> =
    vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag()
        .lessThan(vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak())

/**
 * TBU056V (Exstream): gate for reduksjonsprosent-/inntektsavsnittene. Dekomponert til navngitte deler
 * (identisk output): enten er dette en beregningsendrings-brevkode, ELLER en reell inntektsendring med
 * endret utbetaling (DATA) – og brevet er ikke en søknad om barnetillegg (DATA), og inntektstaket er
 * over bunnfradraget (DATA). Kun `erBeregningsendringsBrevkode()` er brevkode-styrt og forsvinner ved
 * variant-splitting; resten er datastyrt og overlever porten.
 */
fun Expression<PEgruppe10>.skalViseReduksjonsprosentavsnitt(): Expression<Boolean> =
    (erBeregningsendringsBrevkode() or erInntektsendringMedEndretUtbetaling()) and
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
