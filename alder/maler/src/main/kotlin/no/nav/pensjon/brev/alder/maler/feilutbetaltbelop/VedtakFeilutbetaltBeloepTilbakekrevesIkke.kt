package no.nav.pensjon.brev.alder.maler.feilutbetaltbelop

import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.erForeloepigUfoerepensjon
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.forhaandsvarselDato
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.hjemmel
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.sakstype
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.virkningFom
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.virkningTom
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_04_061 Vedtak om at feilutbetalt beløp ikke vil bli tilbakekrevet
@TemplateModelHelpers
object VedtakFeilutbetaltBeloepTilbakekrevesIkke : RedigerbarTemplate<VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto> {

    override val kode = Aldersbrevkoder.Redigerbar.FEILUTBETALT_BELOEP_TILBAKEKREVES_IKKE
    override val kategori = Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER, Sakstype.AFP, Sakstype.BARNEP, Sakstype.GJENLEV, Sakstype.UFOREP)

    override val featureToggle = FeatureToggles.feilutbetaltBelopTilbakekrevesIkke.toggle

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om at feilutbetalt beløp ikke vil bli tilbakekrevet",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val sakstype = pesysData.sakstype
        val virkningFom = pesysData.virkningFom
        val virkningTom = pesysData.virkningTom

        title {
            showIf(sakstype.equalTo(Sakstype.ALDER)) {
                text(bokmal { +"Feilutbetalt alderspensjon - melding om vedtak" })
            }.orShowIf(sakstype.equalTo(Sakstype.AFP)) {
                text(bokmal { +"Feilutbetalt avtalefestet pensjon - melding om vedtak" })
            }.orShowIf(sakstype.equalTo(Sakstype.BARNEP)) {
                text(bokmal { +"Feilutbetalt barnepensjon - melding om vedtak" })
            }.orShowIf(sakstype.equalTo(Sakstype.GJENLEV)) {
                text(bokmal { +"Feilutbetalt pensjon til gjenlevende - melding om vedtak" })
            }.orShowIf(sakstype.equalTo(Sakstype.UFOREP) and not(pesysData.erForeloepigUfoerepensjon)) {
                text(bokmal { +"Feilutbetalt uførepensjon - melding om vedtak" })
            }.orShowIf(sakstype.equalTo(Sakstype.UFOREP) and pesysData.erForeloepigUfoerepensjon) {
                text(bokmal { +"Feilutbetalt foreløpig uførepensjon - melding om vedtak" })
            }
        }
        outline {
            paragraph {
                text(bokmal { +"Vi viser til forhåndsvarselet vårt av " + pesysData.forhaandsvarselDato.format() + "." })
            }
            paragraph {
                val erParagraf2216 = pesysData.hjemmel.equalTo(VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.Hjemmel.PARAGRAF_22_16)
                text(
                    bokmal {
                        +"Vilkårene for tilbakekreving i folketrygdloven paragraf " + ifElse(erParagraf2216, "22-16", "22-15") +
                            " er ikke oppfylt. Beløpet som du har fått utbetalt for mye for perioden " +
                            virkningFom.format() + " til " + virkningTom.format() + " kreves derfor ikke tilbakebetalt."
                    }
                )
            }
        }
    }
}
