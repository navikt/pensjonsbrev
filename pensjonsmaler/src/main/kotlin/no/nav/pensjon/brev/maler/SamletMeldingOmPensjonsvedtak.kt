package no.nav.pensjon.brev.maler


import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDtoSelectors.PesysDataSelectors.vedlegg
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.vedlegg.Vedleggtyper
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.VedleggType

// Mal 000090 i doksys
@TemplateModelHelpers
object SamletMeldingOmPensjonsvedtak : RedigerbarTemplate<SamletMeldingOmPensjonsvedtakDto> {
    override val kode = Pesysbrevkoder.Redigerbar.P1_SAMLET_MELDING_OM_PENSJONSVEDTAK // 000090
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER, Sakstype.UFOREP, Sakstype.GJENLEV, Sakstype.BARNEP)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = SamletMeldingOmPensjonsvedtakDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Samlet melding om pensjonsvedtak",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Samlet melding om pensjonsvedtak",
                English to "Summary of Pension Decisions"
            )
        }
        outline {
            val sakstype = pesysData.sakstype.format().ifNull(fritekst("sakstype"))

            title1 {
                text(
                    Bokmal to "P1 – Samlet melding om pensjonsvedtak",
                    English to "P1 – Summary of Pension Decisions"
                )
            }
            paragraph {
                text(
                    Bokmal to "I forbindelse med din søknad om ",
                    English to "Your application for "
                )
                eval(sakstype)
                text(
                    Bokmal to " fra EUs og EØS medlemsland legger vi ved",
                    English to " from EU/EEA member countries, we enclose:"
                )
                list {
                    item {
                        text(
                            Bokmal to "P1 – Samlet melding om pensjonsvedtak",
                            English to "P1 – Summary of Pension Decisions"
                        )
                    }
                    item {
                        text(
                            Bokmal to "Informasjon om skjemaet P1 og hvordan det brukes",
                            English to "Information about the P1 form and its use"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "P1 gir deg oversikt over pensjonsvedtak fattet av trygdemyndigheter som har behandlet din søknad om ",
                    English to "The P1 form provides an overview of the decisions taken in your case by the various institutions in the EU/EEA member countries."
                )
                eval(sakstype)
                text(
                    Bokmal to ".",
                    English to ""
                )
            }
        }
        includeAttachment(Vedleggtyper.P1, pesysData.vedlegg)
        includeAttachment(Vedleggtyper.InformasjonOmP1)
    }

}