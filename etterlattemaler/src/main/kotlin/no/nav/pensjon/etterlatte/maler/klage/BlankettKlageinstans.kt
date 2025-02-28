package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.FormkravSelectors.begrunnelseLinjer
import no.nav.pensjon.etterlatte.maler.klage.FormkravSelectors.erKlagenFramsattInnenFrist
import no.nav.pensjon.etterlatte.maler.klage.FormkravSelectors.erKlagenSignert
import no.nav.pensjon.etterlatte.maler.klage.FormkravSelectors.erKlagerPartISaken
import no.nav.pensjon.etterlatte.maler.klage.FormkravSelectors.gjelderKlagenNoeKonkretIVedtaket
import no.nav.pensjon.etterlatte.maler.klage.FormkravSelectors.vedtaketKlagenGjelder
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.formkrav
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.hjemmel
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.internKommentarLinjer
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.klageDato
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.klager
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.oversendelseLinjer
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBlankettDTOSelectors.sakTypeTekst
import no.nav.pensjon.etterlatte.maler.klage.VedtakKlagenGjelderSelectors.datoAttestert
import no.nav.pensjon.etterlatte.maler.klage.VedtakKlagenGjelderSelectors.vedtakTypeFormatert
import java.time.LocalDate


data class KlageOversendelseBlankettDTO(
    override val innhold: List<Element>,
    val formkrav: Formkrav,
    val hjemmel: String,
    val sakType: SakType,
    val internKommentar: String?,
    val ovesendelseTekst: String,
    val klager: String,
    val klageDato: LocalDate
) : FerdigstillingBrevDTO {
    val oversendelseLinjer = ovesendelseTekst.split("\n")
    val internKommentarLinjer = internKommentar?.split("\n")
    val sakTypeTekst = when (sakType) {
        SakType.OMSTILLINGSSTOENAD -> "omstillingsstønad"
        SakType.BARNEPENSJON -> "barnepensjon"
    }
}

data class Formkrav(
    val vedtaketKlagenGjelder: VedtakKlagenGjelder?,
    val erKlagenSignert: Boolean,
    val gjelderKlagenNoeKonkretIVedtaket: Boolean,
    val erKlagerPartISaken: Boolean,
    val erKlagenFramsattInnenFrist: Boolean,
    val erFormkraveneOppfylt: Boolean,
    val begrunnelse: String?
) {
    val begrunnelseLinjer = begrunnelse?.split("\n")
}

data class VedtakKlagenGjelder(
    val datoAttestert: LocalDate,
    val vedtakType: VedtakType
) {
    val vedtakTypeFormatert = when (vedtakType) {
        VedtakType.INNVILGELSE -> "Innvilgelse"
        VedtakType.OPPHOER -> "Opphør"
        VedtakType.AVSLAG -> "Avslag på søknad"
        VedtakType.ENDRING -> "Revurdering"
        VedtakType.TILBAKEKREVING -> "Tilbakekreving"
        VedtakType.AVVIST_KLAGE -> "Avvist klage"
    }
}

enum class VedtakType {
    INNVILGELSE,
    OPPHOER,
    AVSLAG,
    ENDRING,
    TILBAKEKREVING,
    AVVIST_KLAGE,
}

@TemplateModelHelpers
object BlankettKlageinstans : EtterlatteTemplate<KlageOversendelseBlankettDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.KLAGE_OVERSENDELSE_BLANKETT
    override val template = createTemplate(
        name = kode.name,
        letterDataType = KlageOversendelseBlankettDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Blankett oversendelse klage",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Klage oversendelsesblankett",
                Language.Nynorsk to "Klage oversendelsesblankett",
                Language.English to "Klage oversendelsesblankett"
            )
        }

        outline {
            title2 {
                text(
                    Language.Bokmal to "Informasjon om saken",
                    Language.Nynorsk to "Informasjon om saken",
                    Language.English to "Informasjon om saken"
                )
            }
            formaterPunktMedSvar("Saktype", sakTypeTekst)
            formaterPunktMedSvar("Klager", klager)
            formaterPunktMedSvar("Klagedato", klageDato.format())

            title2 {
                text(
                    Language.Bokmal to "Formkrav og klagefrist",
                    Language.Nynorsk to "Formkrav og klagefrist",
                    Language.English to "Formkrav og klagefrist"
                )
            }

            ifNotNull(formkrav.vedtaketKlagenGjelder) {
                formaterPunktMedSvar("Dato vedtak attestert", it.datoAttestert.format())
                formaterPunktMedSvar("Type vedtak", it.vedtakTypeFormatert)
            } orShow {
                formaterPunktMedSvar("Vedtak som klages på", "Det klages ikke på et konkret vedtak".expr())
            }

            formaterKrav("Er klagen signert?", formkrav.erKlagenSignert)
            formaterKrav("Er klager part i saken?", formkrav.erKlagerPartISaken)
            formaterKrav("Er klagen framsatt innen frist?", formkrav.erKlagenFramsattInnenFrist)
            formaterKrav("Klages det på konkrete elementer i vedtaket?", formkrav.gjelderKlagenNoeKonkretIVedtaket)

            ifNotNull(formkrav.begrunnelseLinjer) {
                formaterTekstlinjer("Begrunnelse", it)
            }


            title2 {
                text(Language.Bokmal to "Vurdering", Language.Nynorsk to "Vurdering", Language.English to "Vurdering")
            }
            formaterPunktMedSvar("Utfall", "Oppretthold vedtak".expr())
            formaterPunktMedSvar("Hjemmel", hjemmel)

            formaterTekstlinjer("Innstilling til Nav klageinstans", oversendelseLinjer)
            ifNotNull(internKommentarLinjer) {
                formaterTekstlinjer("Intern kommentar", it)
            }
        }
    }

}

fun <T : Any> OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>.formaterPunktMedSvar(
    punkt: String,
    svar: Expression<String>
) {
    paragraph {
        text(Language.Bokmal to punkt, Language.Nynorsk to punkt, Language.English to punkt, FontType.BOLD)
        newline()
        textExpr(Language.Bokmal to svar, Language.Nynorsk to svar, Language.English to svar)
    }
}

fun <T : Any> OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>.formaterKrav(
    tittel: String,
    oppfylt: Expression<Boolean>
) {
    paragraph {
        text(Language.Bokmal to tittel, Language.Nynorsk to tittel, Language.English to tittel, FontType.BOLD)
        newline()
        showIf(oppfylt) {
            text(Language.Bokmal to "Oppfylt", Language.Nynorsk to "Oppfylt", Language.English to "Oppfylt")
        } orShow {
            text(
                Language.Bokmal to "Ikke oppfylt",
                Language.Nynorsk to "Ikke oppfylt",
                Language.English to "Ikke oppfylt"
            )
        }
    }
}

fun <T : Any> OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>.formaterTekstlinjer(
    overskrift: String,
    linjer: Expression<List<String>>
) {
    paragraph {
        text(
            Language.Bokmal to overskrift,
            Language.Nynorsk to overskrift,
            Language.English to overskrift,
            FontType.BOLD
        )
        newline()
        forEach(linjer) { linje ->
            textExpr(Language.Bokmal to linje, Language.Nynorsk to linje, Language.English to linje)
            newline()
        }
    }
}