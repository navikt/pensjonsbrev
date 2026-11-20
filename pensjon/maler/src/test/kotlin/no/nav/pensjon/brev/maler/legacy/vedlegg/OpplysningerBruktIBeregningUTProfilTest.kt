package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.maler.legacy.vedlegg.OpplysningerBruktIBeregningUTProfil.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** PE-brevkoden hver profil erstatter. */
internal val legacyBrevkode: Map<OpplysningerBruktIBeregningUTProfil, String> = mapOf(
    INNVILGELSE to "PE_UT_04_101",
    ENDRING to "PE_UT_04_102",
    DELVIS_EKSPORT to "PE_UT_04_103",
    INNVILGELSE_MED_ENDRING_BOSATT_NORGE_ETTER_UTLAND to "PE_UT_04_106",
    INNVILGELSE_BOSATT_NORGE_ETTER_UTLAND to "PE_UT_04_107",
    NY_BEREGNING_BARNETILLEGG_AUTO to "PE_UT_04_108",
    NY_BEREGNING_BARNETILLEGG_MANUELL to "PE_UT_04_109",
    OEKNING_UFOEREGRAD to "PE_UT_04_114",
    FULL_EKSPORT to "PE_UT_04_115",
    OMREGNING_UP_TIL_UT to "PE_UT_04_300",
    OMREGNING_UP_TIL_UT_AUTO to "PE_UT_14_300",
    ENDRING_PGA_INNTEKT_AUTO to "PE_UT_05_100",
    NY_BEREGNING_PGA_OPPTJENING_AUTO to "PE_UT_06_100",
    ENDRING_BARNETILLEGG to "PE_UT_06_300",
    ENDRING_PGA_INNTEKT to "PE_UT_07_100",
    OPPHOER_BARNETILLEGG_AUTO to "PE_UT_07_200",
    FOERSTEGANGSBEHANDLING_BOSATT_UTLAND to "f_bh_bo_utland",
)

/**
 * Sjekker at hvert profilflagg er sant for nøyaktig de brevkodene legacy-uttrykket i vedlegget testet på.
 * Kodesettene er kopiert fra `pebrevkode()`-sjekkene de erstatter.
 */
class OpplysningerBruktIBeregningUTProfilTest {
    private fun alleUnntatt(vararg koder: String) = legacyBrevkode.values.toSet() - koder.toSet()

    private val forventet: List<Triple<String, (OpplysningerBruktIBeregningUTProfil) -> Boolean, Set<String>>> = listOf(
        Triple("erEndring", { it.erEndring }, setOf("PE_UT_04_102")),
        Triple("utelatVedDoedsfall", { it.utelatVedDoedsfall }, setOf("PE_UT_04_102")),
        Triple("erInnvilgelse", { it.erInnvilgelse }, setOf("PE_UT_04_101")),
        Triple("erOekningUfoeregrad", { it.erOekningUfoeregrad }, setOf("PE_UT_04_114")),
        Triple("erOmregningUPtilUT", { it.erOmregningUPtilUT }, setOf("PE_UT_04_300", "PE_UT_14_300")),
        Triple("visMinstepensjonsforklaring", { it.visMinstepensjonsforklaring }, setOf("PE_UT_04_300")),
        Triple("erInntektsendring", { it.erInntektsendring }, setOf("PE_UT_05_100", "PE_UT_07_100")),
        Triple("erNyBeregningAvBarnetilleggAuto", { it.erNyBeregningAvBarnetilleggAuto }, setOf("PE_UT_04_108")),
        Triple("erNyBeregningAvBarnetilleggManuell", { it.erNyBeregningAvBarnetilleggManuell }, setOf("PE_UT_04_109")),
        Triple("erNyBeregningAvBarnetillegg", { it.erNyBeregningAvBarnetillegg }, setOf("PE_UT_04_108", "PE_UT_04_109")),
        Triple("erBarnetilleggsbrevUtenEndring", { it.erBarnetilleggsbrevUtenEndring }, setOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200")),
        Triple("erBarnetilleggsbrev", { it.erBarnetilleggsbrev }, setOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300")),
        Triple("erEndringAvBarnetillegg", { it.erEndringAvBarnetillegg }, setOf("PE_UT_06_300")),
        Triple("visTrygdetidUansett", { it.visTrygdetidUansett }, setOf("PE_UT_04_101", "PE_UT_04_114")),
        Triple("erEndringEllerOekning", { it.erEndringEllerOekning }, setOf("PE_UT_04_102", "PE_UT_04_114")),
        Triple(
            "visForklaringOmRettTilBarnetillegg", { it.visForklaringOmRettTilBarnetillegg },
            setOf(
                "PE_UT_04_101", "PE_UT_04_102", "PE_UT_04_114", "PE_UT_04_300", "PE_UT_14_300",
                "PE_UT_06_100", "PE_UT_04_103", "PE_UT_04_106", "PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200",
            ),
        ),
        Triple(
            "visBarnetilleggsberegning", { it.visBarnetilleggsberegning },
            setOf(
                "PE_UT_04_101", "PE_UT_04_102", "PE_UT_04_114", "PE_UT_04_300", "PE_UT_14_300",
                "PE_UT_06_100", "PE_UT_04_103", "PE_UT_04_106", "PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200",
                "PE_UT_06_300",
            ),
        ),
        Triple(
            "erBeregningsendring", { it.erBeregningsendring },
            setOf("PE_UT_04_102", "PE_UT_04_101", "PE_UT_04_114", "PE_UT_04_300", "PE_UT_14_300"),
        ),
        Triple(
            "visMinsteytelse", { it.visMinsteytelse },
            alleUnntatt("PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_300", "PE_UT_14_300", "PE_UT_04_103", "PE_UT_04_108", "PE_UT_07_200", "PE_UT_06_300"),
        ),
        Triple("visUtlandsforklaringUansett", { it.visUtlandsforklaringUansett }, setOf("f_bh_bo_utland")),
        Triple(
            "visInntekterBruktIBeregning", { it.visInntekterBruktIBeregning },
            alleUnntatt("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300", "PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_300", "PE_UT_14_300"),
        ),
        Triple(
            "visInntektsgrenseVedBegrunnelse", { it.visInntektsgrenseVedBegrunnelse },
            alleUnntatt("PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_115", "PE_UT_04_103", "PE_UT_06_100", "PE_UT_04_300", "PE_UT_14_300", "PE_UT_07_200", "PE_UT_06_300"),
        ),
    )

    @Test
    fun `alle profiler har en legacy-brevkode`() {
        assertEquals(OpplysningerBruktIBeregningUTProfil.entries.toSet(), legacyBrevkode.keys)
    }

    @Test
    fun `profilflagg tilsvarer legacy-brevkodesettene`() {
        forventet.forEach { (navn, flagg, koder) ->
            val faktisk = OpplysningerBruktIBeregningUTProfil.entries.filter(flagg).map { legacyBrevkode.getValue(it) }.toSet()
            assertEquals(koder, faktisk, navn)
        }
    }
}
