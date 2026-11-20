package no.nav.pensjon.brev.maler.legacy.vedlegg

/**
 * Brevtypeprofil for [vedleggOpplysningerBruktIBeregningUTLegacy]. Erstatter tidligere styring på `pebrevkode`:
 * kallstedet velger profilen som tilsvarer brevet, og vedlegget leser bare statiske innholdsvalg herfra.
 *
 * Flaggene er definert som medlemskap i de samme brevkodemengdene som legacy-vedlegget brukte, slik at
 * rendret innhold er uendret. Tilsvarende Exstream-brevkode står i KDoc på hver verdi for sporbarhet.
 */
enum class OpplysningerBruktIBeregningUTProfil {
    /** PE_UT_04_101 – Vedtak: innvilgelse av uføretrygd. */
    INNVILGELSE,

    /** PE_UT_04_102 – Vedtak: endring av uføretrygd. */
    ENDRING,

    /** PE_UT_04_103 – Vedtak: delvis eksport av uføretrygd. */
    DELVIS_EKSPORT,

    /** PE_UT_04_106 – Vedtak: innvilgelse av uføretrygd med endring (bosatt Norge – etter utland). */
    INNVILGELSE_MED_ENDRING_BOSATT_NORGE_ETTER_UTLAND,

    /** PE_UT_04_107 – Vedtak: innvilgelse av uføretrygd (bosatt Norge – etter utland). */
    INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND,

    /** PE_UT_04_108 – Vedtak: ny beregning av barnetillegget (automatisk). */
    NY_BEREGNING_BARNETILLEGG_AUTO,

    /** PE_UT_04_109 – Vedtak: ny beregning av barnetillegget (manuell). */
    NY_BEREGNING_BARNETILLEGG_MANUELL,

    /** PE_UT_04_114 – Vedtak: økning av uføregrad. */
    OEKNING_UFOEREGRAD,

    /** PE_UT_04_115 – Vedtak: full eksport av uføretrygd. */
    FULL_EKSPORT,

    /** PE_UT_04_300 – Vedtak: omregning av uførepensjon til uføretrygd. */
    OMREGNING_UP_TIL_UT,

    /** PE_UT_14_300 – Vedtak: omregning av uførepensjon til uføretrygd (automatisk). */
    OMREGNING_UP_TIL_UT_AUTO,

    /** PE_UT_06_100 – Vedtak: ny beregning av uføretrygd på grunn av endring i opptjening (automatisk). */
    NY_BEREGNING_PGA_OPPTJENING_AUTO,

    /** PE_UT_06_300 – Endring av barnetillegg i uføretrygden. */
    ENDRING_BARNETILLEGG,

    /** PE_UT_05_100/PE_UT_07_100 – Vedtak: endring av uføretrygd på grunn av inntekt (automatisk og manuell). */
    ENDRING_PGA_INNTEKT,

    /** PE_UT_07_200 – Vedtak: opphør av barnetillegg (automatisk). */
    OPPHOER_BARNETILLEGG_AUTO,

    /** Legacy-sjekk mot brevkodeverdien `f_bh_bo_utland` (førstegangsbehandling bosatt utland). */
    FOERSTEGANGSBEHANDLING_BOSATT_UTLAND,
    ;

    val erEndring: Boolean get() = this == ENDRING

    /** 04_102: avsnitt utelates når kravårsaken er dødsfall (`tilst_dod`). */
    val utelatVedDoedsfall: Boolean get() = erEndring

    val erInnvilgelse: Boolean get() = this == INNVILGELSE
    val erOekningUfoeregrad: Boolean get() = this == OEKNING_UFOEREGRAD

    /** Omregning av uførepensjon til uføretrygd (04_300/14_300). */
    val erOmregningUPtilUT: Boolean get() = this in setOf(OMREGNING_UP_TIL_UT, OMREGNING_UP_TIL_UT_AUTO)

    /** Kun manuell omregning (04_300) viser minstepensjonsforklaringen. */
    val visMinstepensjonsforklaring: Boolean get() = this == OMREGNING_UP_TIL_UT

    /** Inntektsendringsbrev (05_100/07_100). */
    val erInntektsendring: Boolean get() = this == ENDRING_PGA_INNTEKT

    val erNyBeregningAvBarnetilleggAuto: Boolean get() = this == NY_BEREGNING_BARNETILLEGG_AUTO
    val erNyBeregningAvBarnetilleggManuell: Boolean get() = this == NY_BEREGNING_BARNETILLEGG_MANUELL

    /** Ny beregning av barnetillegget (04_108/04_109). */
    val erNyBeregningAvBarnetillegg: Boolean get() = erNyBeregningAvBarnetilleggAuto || erNyBeregningAvBarnetilleggManuell

    /** Barnetilleggsbrev uten 06_300 (04_108/04_109/07_200). */
    val erBarnetilleggsbrevUtenEndring: Boolean get() = erNyBeregningAvBarnetillegg || this == OPPHOER_BARNETILLEGG_AUTO

    /** Barnetilleggsbrev (04_108/04_109/07_200/06_300). */
    val erBarnetilleggsbrev: Boolean get() = erBarnetilleggsbrevUtenEndring || this == ENDRING_BARNETILLEGG

    val erEndringAvBarnetillegg: Boolean get() = this == ENDRING_BARNETILLEGG

    /** 04_101/04_114 viser trygdetid uavhengig av trygdetidslengde. */
    val visTrygdetidUansett: Boolean get() = erInnvilgelse || erOekningUfoeregrad

    /** 04_102/04_114 skjuler den detaljerte reduksjonsberegningen. */
    val erEndringEllerOekning: Boolean get() = erEndring || erOekningUfoeregrad

    /** Brevtyper der forklaringen av retten til barnetillegg kan vises. */
    val visForklaringOmRettTilBarnetillegg: Boolean
        get() = this in setOf(
            INNVILGELSE, ENDRING, OEKNING_UFOEREGRAD, OMREGNING_UP_TIL_UT, OMREGNING_UP_TIL_UT_AUTO,
            NY_BEREGNING_PGA_OPPTJENING_AUTO, DELVIS_EKSPORT, INNVILGELSE_MED_ENDRING_BOSATT_NORGE_ETTER_UTLAND,
            NY_BEREGNING_BARNETILLEGG_AUTO, NY_BEREGNING_BARNETILLEGG_MANUELL, OPPHOER_BARNETILLEGG_AUTO,
        )

    /** Som [visForklaringOmRettTilBarnetillegg], men inkluderer også 06_300. */
    val visBarnetilleggsberegning: Boolean get() = visForklaringOmRettTilBarnetillegg || erEndringAvBarnetillegg

    /** Brevtyper som representerer en (re)beregning der reduksjonsprosentavsnittet er aktuelt. */
    val erBeregningsendring: Boolean
        get() = this in setOf(ENDRING, INNVILGELSE, OEKNING_UFOEREGRAD, OMREGNING_UP_TIL_UT, OMREGNING_UP_TIL_UT_AUTO)

    /** Brevtyper som viser minsteytelseseksjonen. */
    val visMinsteytelse: Boolean
        get() = this !in setOf(
            ENDRING_PGA_INNTEKT, OMREGNING_UP_TIL_UT, OMREGNING_UP_TIL_UT_AUTO,
            DELVIS_EKSPORT, NY_BEREGNING_BARNETILLEGG_AUTO, OPPHOER_BARNETILLEGG_AUTO, ENDRING_BARNETILLEGG,
        )

    val visUtlandsforklaringUansett: Boolean get() = this == FOERSTEGANGSBEHANDLING_BOSATT_UTLAND

    /** Brevtyper som kan vise "Dette er inntektene vi har brukt i beregningen din". */
    val visInntekterBruktIBeregning: Boolean
        get() = !erBarnetilleggsbrev && !erInntektsendring && !erOmregningUPtilUT

    /** Brevtyper som kan vise inntektsgrense og avkortning ut fra ifu-/ieu-begrunnelse. */
    val visInntektsgrenseVedBegrunnelse: Boolean
        get() = this !in setOf(
            ENDRING_PGA_INNTEKT, FULL_EKSPORT, DELVIS_EKSPORT,
            NY_BEREGNING_PGA_OPPTJENING_AUTO, OMREGNING_UP_TIL_UT, OMREGNING_UP_TIL_UT_AUTO,
            OPPHOER_BARNETILLEGG_AUTO, ENDRING_BARNETILLEGG,
        )
}
