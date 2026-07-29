package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.util.Objects

/**
 * DSL for å beskrive innholdet som skal fylles inn i et utfyllbart PDF-skjema (AcroForm), side for side og
 * felt for felt, uavhengig av selve PDF-rendrings-/utfyllingslogikken (se `SideAppender`/`PDFVedleggAppender`
 * for hvordan denne strukturen faktisk fylles inn i PDF-filene).
 *
 * Et [PDFVedlegg] består av én eller flere [Side]r (typisk én per fysisk PDF-fil), og hver side har ett
 * eller flere [Felt]-blokker med feltnavn -> verdi.
 *
 * ### Eksempel
 * ```kotlin
 * val vedlegg = PDFVedlegg().apply {
 *     side("P1-side1") {
 *         felt {
 *             "Forenames[0]" to innehaver.fornavn
 *             "Surname[0]" to innehaver.etternavn
 *             "Post_code[0]" to innehaver.postnummer?.value
 *         }
 *     }
 *     side("P1-side2") {
 *         felt {
 *             "Institution_awarding_the_pension[0]" to mapOf(
 *                 LanguageCode.BOKMAL to "NAV",
 *                 LanguageCode.ENGLISH to "NAV",
 *             )
 *         }
 *     }
 * }
 * ```
 *
 * ### Viktig om feltnavn
 * Feltnavnene som brukes i `felt { "Feltnavn" to verdi }` er **ikke frie tekststrenger** – de må matche
 * nøyaktig `partialName` til det tilhørende AcroForm-feltet i PDF-ressursen. Se f.eks. `P1pdfV2Dto` for et
 * konkret eksempel på kobling mellom feltnavn i DSL-en og felt i PDF-filen.
 *
 * ### Viktig om språk
 * - `"felt" to verdi` (String/tall/annet enkeltverdi) setter samme tekst for alle [LanguageCode]-verdier
 *   (bokmål, nynorsk, engelsk) via [Felt.leggTilPaaAlleSpraak].
 * - `"felt" to mapOf(LanguageCode.BOKMAL to "...", ...)` brukes når feltet skal ha ulik tekst per språk.
 * - Å sende en `Map` til den enkeltverdi-baserte `to`-varianten kaster [IllegalArgumentException] – bruk
 *   map-varianten eksplisitt i så fall.
 */
@PDFVedleggMarker
class PDFVedlegg {
    val sider: List<Side>
        field: MutableList<Side> = mutableListOf()

    /** Legger til en ny [Side] med gitt [filnavn] i vedlegget, konfigurert med [init]. */
    fun side(filnavn: String, init: Side.() -> Unit) {
        sider.add(Side(filnavn).apply(init))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) return false
        return sider == other.sider
    }

    override fun hashCode() = sider.hashCode()
    override fun toString() = "PDFVedlegg(muterbarSider=${sider})"
}

/**
 * Én side/PDF-fil i et [PDFVedlegg], identifisert ved [filnavn].
 *
 * [filnavn] brukes til å slå opp riktig PDF-ressurs (se `SideAppender.lesInnPDF`), typisk uten filendelse
 * og språksuffiks – disse legges på av kallende kode basert på brevets språk, f.eks.
 * `<filnavn>-<SPRÅK>.pdf`.
 *
 * En side kan ha flere [felt]-blokker (kalt flere ganger via [felt]); disse slås sammen når feltene skal
 * fylles inn i PDF-en.
 */
@PDFVedleggMarker
class Side(val filnavn: String) {
    val felt: List<Felt>
        field: MutableList<Felt> = mutableListOf()

    /** Legger til en ny gruppe med feltverdier ([Felt]) på denne siden. */
    fun felt(init: Felt.() -> Unit) {
        felt.add(Felt().apply(init))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Side) return false
        return filnavn == other.filnavn && felt == other.felt
    }

    override fun hashCode() = Objects.hash(filnavn, felt)
    override fun toString() = "Side(filnavn='$filnavn', felt=$felt)"
}

/**
 * En samling feltnavn -> verdi som skal fylles inn i AcroForm-feltene på en [Side].
 *
 * Feltverdier settes med infix `to`:
 * - `"feltnavn" to enkeltverdi` (String, tall, e.l.) setter samme tekst på alle språk, se
 *   [leggTilPaaAlleSpraak].
 * - `"feltnavn" to mapOf(LanguageCode.BOKMAL to "...", ...)` setter ulik tekst per språk.
 *
 * `null`-verdier er tillatt og resulterer i et felt som fylles inn med tom/manglende tekst per språk.
 */
@PDFVedleggMarker
class Felt {
    val felt: Map<String, Map<LanguageCode, String?>?>
        field: MutableMap<String, Map<LanguageCode, String?>?> = mutableMapOf()

    /** Setter [str] som verdi for dette feltnavnet på alle språk (bokmål, nynorsk, engelsk). */
    infix fun String.to(str: String) {
        felt[this] = leggTilPaaAlleSpraak(str)
    }

    /** Setter en eksplisitt per-språk-verdi ([verdi]) for dette feltnavnet. */
    infix fun String.to(verdi: Map<LanguageCode, String?>) {
        felt[this] = verdi
    }

    /**
     * Setter [verdi] (konvertert med `toString()`) som verdi for dette feltnavnet på alle språk.
     * Håndterer `null` ved å sette et tomt/manglende felt.
     *
     * @throws IllegalArgumentException dersom [verdi] er en [Map] – bruk `infix fun String.to(verdi: Map<LanguageCode, String?>)` i så fall.
     */
    infix fun String.to(verdi: Any?) {
        if (verdi is Map<*, *>) {
            throw IllegalArgumentException("Forventa ikke å legge til map her. Bruk infix-versjonen to Map<LanguageCode, String?> for å legge til map av språk til tekst")
        }
        felt[this] = verdi?.let { leggTilPaaAlleSpraak(it.toString()) }
    }

    /** Bygger et [Map] fra [LanguageCode] til [str] – samme tekst for bokmål, nynorsk og engelsk. */
    private fun leggTilPaaAlleSpraak(str: String?): Map<LanguageCode, String?> = mapOf(
        LanguageCode.BOKMAL to str,
        LanguageCode.NYNORSK to str,
        LanguageCode.ENGLISH to str
    )

    /**
     * Legger til flere felt fra et vanlig [map] (feltnavn -> verdi), som et alternativ til å bruke
     * infix `to` per felt. Kun `String`- og `Map<LanguageCode, String?>`-verdier i [map] tas med;
     * andre typer ignoreres stille.
     */
    fun add(map: Map<String, Any?>) {
        map.entries
            .filter { it.value is String }
            .forEach {
                felt[it.key] = leggTilPaaAlleSpraak(it.value as String)
            }
        map.entries
            .filter { it.value is Map<*, *> }
            .forEach { felt[it.key] = it.value as Map<LanguageCode, String?> }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Felt) return false
        return felt == other.felt
    }
    override fun hashCode() = felt.hashCode()
    override fun toString() = "Felt(felt=$felt)"
}

/** [DslMarker] som hindrer utilsiktet tilgang til ytre scope (f.eks. [PDFVedlegg] inne i [felt]-blokken) i denne DSL-en. */
@DslMarker
internal annotation class PDFVedleggMarker