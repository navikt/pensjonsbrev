package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
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
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Blankett oversendelse klage",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Klage oversendelsesblankett" },
                nynorsk { +"Klage oversendelsesblankett" },
                english { +"Klage oversendelsesblankett" }
            )
        }

        outline {
            title2 {
                text(
                    bokmal { +"Informasjon om saken" },
                    nynorsk { +"Informasjon om saken" },
                    english { +"Informasjon om saken" }
                )
            }
            formaterPunktMedSvar("Saktype", sakTypeTekst)
            formaterPunktMedSvar("Klager", klager)
            formaterPunktMedSvar("Klagedato", klageDato.format())

            title2 {
                text(
                    bokmal { +"Formkrav og klagefrist" },
                    nynorsk { +"Formkrav og klagefrist" },
                    english { +"Formkrav og klagefrist" }
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
                text(bokmal { +"Vurdering" }, nynorsk { +"Vurdering" }, english { +"Vurdering" })
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
        text(bokmal { +punkt }, nynorsk { +punkt }, english { +punkt }, FontType.BOLD)
        newline()
        text(bokmal { +svar }, nynorsk { +svar }, english { +svar })
    }
}

fun <T : Any> OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>.formaterKrav(
    tittel: String,
    oppfylt: Expression<Boolean>
) {
    paragraph {
        text(bokmal { +tittel }, nynorsk { +tittel }, english { +tittel }, FontType.BOLD)
        newline()
        showIf(oppfylt) {
            text(bokmal { +"Oppfylt" }, nynorsk { +"Oppfylt" }, english { +"Oppfylt" })
        } orShow {
            text(
                bokmal { +"Ikke oppfylt" },
                nynorsk { +"Ikke oppfylt" },
                english { +"Ikke oppfylt" }
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
            bokmal { +overskrift },
            nynorsk { +overskrift },
            english { +overskrift },
            FontType.BOLD
        )
        newline()
        forEach(linjer) { linje ->
            text(bokmal { +linje }, nynorsk { +linje }, english { +linje })
            newline()
        }
    }
}
