package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.security.MessageDigest

/**
 * Midlertidig ekvivalenstest for migreringen bort fra `pebrevkode` i vedlegget. Skriver én linje per
 * rendering (profil × datavariant × språk) med en hash av innholdet, pluss resultatet av gaten
 * `inkluderopplysningerbruktiberegningen`. Kjør på to commits og sammenlign filene:
 *
 * `EKVIVALENS_UT=/tmp/a.txt ./gradlew :pensjon:maler:manualTest --tests '*OpplysningerBruktIBeregningUTLegacyEkvivalensTest*'`
 *
 * Sett `EKVIVALENS_FULL` til en linjenøkkel (alt før lengden) for å få full rendering skrevet til `<EKVIVALENS_UT>.full`.
 * Slettes når migreringen er ferdig.
 */
@Tag(TestTags.MANUAL_TEST)
class OpplysningerBruktIBeregningUTLegacyEkvivalensTest {

    @Suppress("UNUSED_PARAMETER")
    private fun Expression<PEgruppe10>.gate(profil: OpplysningerBruktIBeregningUTProfil) =
        inkluderopplysningerbruktiberegningen()

    @Test
    fun skrivEkvivalensfil() {
        val ut = File(System.getenv("EKVIVALENS_UT") ?: "build/ekvivalens.txt")
        val full = System.getenv("EKVIVALENS_FULL")
        val md = MessageDigest.getInstance("SHA-256")
        ut.bufferedWriter().use { w ->
            for (profil in OpplysningerBruktIBeregningUTProfil.entries) {
                val kode = legacyBrevkode.getValue(profil)
                val vedlegg = vedleggOpplysningerBruktIBeregningUTLegacy(profil)
                for ((navn, data) in vedleggsvarianter()) {
                    val pe = data.copy(pebrevkode = kode)
                    for (spraak in listOf(Language.Bokmal, Language.Nynorsk)) {
                        val tekst = render(vedlegg, pe, spraak)
                        val noekkel = "$profil $navn $spraak"
                        if (noekkel == full) File(ut.path + ".full").writeText(tekst)
                        val hash = md.digest(tekst.toByteArray()).joinToString("") { "%02x".format(it) }.take(16)
                        w.write("$noekkel ${tekst.length} $hash\n")
                    }
                }
                val gatevedlegg = gatevedlegg(profil)
                for ((navn, data) in gatevarianter()) {
                    val vises = render(gatevedlegg, data.copy(pebrevkode = kode), Language.Bokmal).contains("text=JA")
                    w.write("GATE $profil $navn $vises\n")
                }
            }
        }
    }

    private fun render(vedlegg: AttachmentTemplate<LangBokmalNynorsk, PEgruppe10>, pe: PEgruppe10, spraak: Language) =
        LetterTestRenderer.render(
            LetterTestImpl(
                createVedleggTestTemplate(vedlegg, pe.expr(), languages(Language.Bokmal, Language.Nynorsk)),
                EmptyAutobrevdata,
                spraak,
                Fixtures.fellesAuto,
            )
        ).attachments.toString()
            .replace(Regex("id=-?\\d+"), "id=X")
            .replace(Regex("StableHashCode\\(value=-?\\d+\\)"), "StableHashCode(X)")

    private fun gatevedlegg(profil: OpplysningerBruktIBeregningUTProfil) =
        createAttachment<LangBokmalNynorsk, PEgruppe10>(title = { text(bokmal { +"T" }, nynorsk { +"T" }) }) {
            showIf(argument.gate(profil)) { paragraph { text(bokmal { +"JA" }, nynorsk { +"JA" }) } }
        }

    private fun vedleggsvarianter(): Sequence<Pair<String, PEgruppe10>> = sequence {
        val basis = createPEgruppe10()
        for (krav in listOf("endring_ifu", "tilst_dod", "endret_inntekt", "soknad_bt", "sivilstandsendring", "foerstegangsbehandling"))
            for (bt in listOf("begge", "fb", "sb", "ingen"))
                for (btEndret in listOf(true, false))
                    for (ifu in listOf(null, "stdbegr_12_8_2_1"))
                        for (minste in listOf(false, true))
                            for (metode in listOf("folketrygd", "eos")) {
                                val vd = basis.vedtaksbrev.vedtaksdata!!
                                val bu = vd.beregningsdata!!.beregningufore!!
                                val yk = bu.beregningytelseskomp!!
                                val be = bu.belopsendring!!
                                val vl = vd.vilkarsvedtaklist!!
                                val nyBu = bu.copy(
                                    beregningytelseskomp = yk.copy(
                                        barnetilleggfelles = if (bt == "begge" || bt == "fb") yk.barnetilleggfelles else null,
                                        barnetilleggserkull = if (bt == "begge" || bt == "sb") yk.barnetilleggserkull else null,
                                    ),
                                    belopsendring = if (btEndret) be else be.copy(
                                        barnetilleggfellesyk = be.barnetilleggfellesyk!!.copy(belopnybtfb = be.barnetilleggfellesyk!!.belopgammelbtfb),
                                        barnetilleggserkullyk = be.barnetilleggserkullyk!!.copy(belopnybtsb = be.barnetilleggserkullyk!!.belopgammelbtsb),
                                    ),
                                    uforetrygdberegning = bu.uforetrygdberegning!!.copy(mottarminsteytelse = minste, beregningsmetode = metode),
                                )
                                val pe = basis.copy(
                                    vedtaksbrev = basis.vedtaksbrev.copy(
                                        vedtaksdata = vd.copy(
                                            kravhode = vd.kravhode!!.copy(kravarsaktype = krav),
                                            beregningsdata = vd.beregningsdata!!.copy(beregningufore = nyBu),
                                            vilkarsvedtaklist = vl.copy(vilkarsvedtak = vl.vilkarsvedtak.map {
                                                it.copy(beregningsvilkar = it.beregningsvilkar!!.copy(ifubegrunnelse = ifu))
                                            }),
                                        )
                                    )
                                )
                                yield("$krav|$bt|$btEndret|$ifu|$minste|$metode" to pe)
                            }
    }

    private fun gatevarianter(): Sequence<Pair<String, PEgruppe10>> = sequence {
        val basis = createPEgruppe10()
        for (faktor in listOf(true, false)) for (okt in listOf(true, false)) for (minste in listOf(true, false))
            for (grad in listOf(0, 100)) for (ifu in listOf(null, "stdbegr_12_8_2_5")) for (krav in listOf("tilst_dod", "endring_ifu")) {
                val vd = basis.vedtaksbrev.vedtaksdata!!
                val bu = vd.beregningsdata!!.beregningufore!!
                val yk = bu.beregningytelseskomp!!
                val ord = yk.uforetrygdordiner!!
                val vl = vd.vilkarsvedtaklist!!
                val pe = basis.copy(
                    vedtaksbrev = basis.vedtaksbrev.copy(
                        vedtaksdata = vd.copy(
                            faktoromregnet = faktor,
                            kravhode = vd.kravhode!!.copy(kravarsaktype = krav),
                            vilkarsvedtaklist = vl.copy(vilkarsvedtak = vl.vilkarsvedtak.map {
                                it.copy(beregningsvilkar = it.beregningsvilkar!!.copy(ifubegrunnelse = ifu))
                            }),
                            beregningsdata = vd.beregningsdata!!.copy(
                                beregningufore = bu.copy(
                                    belopokt = okt,
                                    uforetrygdberegning = bu.uforetrygdberegning!!.copy(mottarminsteytelse = minste),
                                    beregningytelseskomp = yk.copy(
                                        uforetrygdordiner = ord.copy(avkortningsinformasjon = ord.avkortningsinformasjon!!.copy(utbetalingsgrad = grad)),
                                    ),
                                ),
                            ),
                        )
                    )
                )
                yield("$faktor|$okt|$minste|$grad|$ifu|$krav" to pe)
            }
    }
}
