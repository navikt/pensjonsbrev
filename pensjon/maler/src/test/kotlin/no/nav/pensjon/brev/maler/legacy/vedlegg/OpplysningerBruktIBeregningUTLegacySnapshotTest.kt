package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderToMarkup
import no.nav.brev.brevbaker.toPrettyJson
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.exists
import kotlin.io.path.readText

/**
 * Behavior-preservation ("golden") snapshot-test for [vedleggOpplysningerBruktIBeregningUTLegacy].
 *
 * Vedlegget forgrener seg tungt på `pebrevkode` og `kravarsaktype`. Denne testen rendrer vedlegget til
 * [no.nav.pensjon.brevbaker.api.model.LetterMarkup] for hele matrisen av (brevkode × kravårsak × språk)
 * og sammenligner en deterministisk **digest** (SHA-256) av markupen mot en committet golden-fil.
 *
 * Vi lagrer digest (ikke full markup) fordi full JSON for alle kombinasjonene er flere MB — for stort til
 * å committe. Digesten fanger likevel enhver endring i output per kombinasjon. Ved avvik skriver testen den
 * faktiske markupen for de endrede kombinasjonene til `build/opplysningerBruktIBeregningUTLegacy-actual/`
 * slik at man kan inspisere/diffe lokalt.
 *
 * Formålet er å fange enhver utilsiktet endring i output under refaktorering av vedlegget.
 *
 * Regenerering av golden-fila (kun ved *tilsiktet* endring) — env-variabel forwardes til test-JVM-en:
 *   UPDATE_SNAPSHOT=true ./gradlew :pensjon:maler:test --tests "*OpplysningerBruktIBeregningUTLegacySnapshotTest*"
 */
class OpplysningerBruktIBeregningUTLegacySnapshotTest {

    private val goldenFile: Path = Path.of(
        "src", "test", "resources", "snapshots",
        "opplysningerBruktIBeregningUTLegacy.digest.txt",
    )

    private val actualDumpDir: Path = Path.of("build", "opplysningerBruktIBeregningUTLegacy-actual")

    // Alle pebrevkoder vedlegget forgrener på (fra fraser + hovedfil).
    private val brevkoder = listOf(
        "PE_UT_04_101", "PE_UT_04_102", "PE_UT_04_103", "PE_UT_04_108", "PE_UT_04_109",
        "PE_UT_04_114", "PE_UT_04_115", "PE_UT_04_300", "PE_UT_04_402", "PE_UT_04_500",
        "PE_UT_05_100", "PE_UT_06_100", "PE_UT_06_300", "PE_UT_07_100", "PE_UT_07_200",
        "PE_UT_14_300", "PE_UT_23_001",
    )

    // Alle kravårsakstyper vedlegget forgrener på.
    private val kravaarsaker = listOf(
        "endret_inntekt", "endring_ifu", "sivilstandsendring", "soknad_bt", "tilst_dod",
    )

    private val spraak = listOf(Language.Bokmal, Language.Nynorsk)

    private fun pegruppe10(brevkode: String, kravaarsak: String): PEgruppe10 {
        val base = Fixtures.create(PEgruppe10::class)
        val vedtaksbrev = base.vedtaksbrev
        val vedtaksdata = vedtaksbrev.vedtaksdata
        val kravhode = vedtaksdata?.kravhode
        return base.copy(
            pebrevkode = brevkode,
            vedtaksbrev = vedtaksbrev.copy(
                vedtaksdata = vedtaksdata?.copy(
                    kravhode = kravhode?.copy(kravarsaktype = kravaarsak),
                ),
            ),
        )
    }

    /** Rendrer hele matrisen. Returnerer (nøkkel -> pretty JSON av markup), i deterministisk rekkefølge. */
    private fun renderAll(): Map<String, String> {
        val result = LinkedHashMap<String, String>()
        for (brevkode in brevkoder) {
            for (kravaarsak in kravaarsaker) {
                val data = pegruppe10(brevkode, kravaarsak)
                val template = createVedleggTestTemplate(
                    vedleggOpplysningerBruktIBeregningUTLegacy,
                    data.expr(),
                    languages(Language.Bokmal, Language.Nynorsk),
                )
                for (language in spraak) {
                    val key = "$brevkode|$kravaarsak|${language::class.simpleName}"
                    val markup = LetterTestImpl(
                        template,
                        EmptyAutobrevdata,
                        language,
                        Fixtures.fellesAuto,
                    ).renderToMarkup()
                    result[key] = toPrettyJson(markup)
                }
            }
        }
        return result
    }

    private fun sha256(value: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(value.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

    /** Serialiserer digesten som en sortert, lettleselig `nøkkel = sha256`-tekst. */
    private fun digestText(markupByKey: Map<String, String>): String =
        markupByKey.entries
            .sortedBy { it.key }
            .joinToString("\n") { "${it.key} = ${sha256(it.value)}" } + "\n"

    @Test
    fun `vedlegget rendrer identisk markup som golden-snapshot`() {
        val rendered = renderAll()
        val actualDigest = digestText(rendered)

        val regenerate = System.getProperty("updateSnapshot") == "true" ||
            System.getenv("UPDATE_SNAPSHOT") == "true"
        if (regenerate || !goldenFile.exists()) {
            Files.createDirectories(goldenFile.parent)
            Files.writeString(goldenFile, actualDigest)
            println(
                if (regenerate) "Snapshot regenerert: ${goldenFile.toAbsolutePath()}"
                else "Golden-snapshot opprettet (manglet): ${goldenFile.toAbsolutePath()}",
            )
            return
        }

        val expectedDigest = goldenFile.readText()
        if (expectedDigest != actualDigest) {
            dumpChangedCombos(expectedDigest, actualDigest, rendered)
        }
        assertEquals(
            expectedDigest,
            actualDigest,
            "Rendret markup for vedlegget avviker fra golden-snapshot. Faktisk markup for endrede " +
                "kombinasjoner er skrevet til ${actualDumpDir.toAbsolutePath()} for inspeksjon. " +
                "Hvis endringen er tilsiktet, regenerer med UPDATE_SNAPSHOT=true.",
        )
    }

    private fun dumpChangedCombos(
        expectedDigest: String,
        actualDigest: String,
        rendered: Map<String, String>,
    ) {
        val expectedByKey = parseDigest(expectedDigest)
        val actualByKey = parseDigest(actualDigest)
        val changedKeys = (expectedByKey.keys + actualByKey.keys)
            .filter { expectedByKey[it] != actualByKey[it] }
        if (changedKeys.isEmpty()) return
        Files.createDirectories(actualDumpDir)
        for (key in changedKeys) {
            val markup = rendered[key] ?: continue
            val fileName = key.replace("|", "__") + ".json"
            Files.writeString(actualDumpDir.resolve(fileName), markup)
        }
    }

    private fun parseDigest(text: String): Map<String, String> =
        text.lineSequence()
            .filter { it.isNotBlank() }
            .associate { line ->
                val idx = line.lastIndexOf(" = ")
                line.substring(0, idx) to line.substring(idx + 3)
            }
}
