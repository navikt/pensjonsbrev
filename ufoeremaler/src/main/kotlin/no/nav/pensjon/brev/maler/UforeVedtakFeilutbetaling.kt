package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles
import no.nav.pensjon.brev.maler.fraser.Felles.HarDuSporsmal
import no.nav.pensjon.brev.maler.fraser.Felles.RettTilInnsynRefVedlegg
import no.nav.pensjon.brev.maler.vedlegg.oversiktOverFeilutbetalinger
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_VEDTAK_FEILUTBETALING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto.TilbakekrevingResultat
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.PesysDataSelectors.feilutbetaltTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.PesysDataSelectors.oversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.PesysDataSelectors.resultatAvVurderingenForTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.PesysDataSelectors.sumTilInnkrevingTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.pesysData
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object UforeVedtakFeilutbetaling : RedigerbarTemplate<VedtakFeilutbetalingUforeDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - tilbakekreving av feilutbetalt beløp",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val feilutbetaltTotalBeloep = pesysData.feilutbetaltTotalBelop
        val resultatAvVurderingenForTotalBeloep = pesysData.resultatAvVurderingenForTotalBelop
        val sluttPeriodeForTilbakekreving = pesysData.sluttPeriodeForTilbakekreving
        val startPeriodeForTilbakekreving = pesysData.startPeriodeForTilbakekreving
        val sumTilInnkrevingTotalBeloep = pesysData.sumTilInnkrevingTotalBelop

        title {
            text(bokmal { + "Du må betale tilbake uføretrygd"})
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                text(bokmal { + "Vi viser til forhåndsvarselet vårt " + dato + "." })
            }
            paragraph {
                text(
                    bokmal { + "Du har fått for mye uføretrygd"
                            + " utbetalt fra " + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format()
                            + ". Dette er " + feilutbetaltTotalBeloep.format() + " inkludert skatt." },

                )
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
                paragraph {
                    text(bokmal { + "Vi har kommet fram til at du skal betale tilbake hele beløpet. Det vil si " +
                            sumTilInnkrevingTotalBeloep.format() + " etter at skatten er trukket fra." }
                    )
                }
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
                paragraph {
                    text(
                        bokmal { + "Vi har kommet fram til at du skal betale tilbake deler av beløpet. Det vil si " +
                                sumTilInnkrevingTotalBeloep.format() + " etter at skatten er trukket fra." },
                    )
                }
            }
            paragraph {
                text(bokmal { + "Beløpet som er trukket i skatt vil Nav få tilbake av Skatteetaten." }
                )
            }
            paragraph {
                text(bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15." })
            }
            paragraph {
                text(bokmal { + "I vedlegget " })
                namedReference(oversiktOverFeilutbetalinger)
                text(bokmal { + " finner du en oversikt over periodene med feilutbetalinger og beløpet du må betale tilbake." })
            }
            title1 {
                text(bokmal { + "Betydning for skatteoppgjøret" })
            }
            paragraph {
                text(bokmal { + "Vi rapporterer endringen til Skatteetaten. De vil korrigere skatteoppgjøret ditt ut fra denne endringen." })
            }
            includePhrase(Felles.RettTilAKlageKort)
            paragraph {
                text(
                    bokmal { + "Selv om du klager på vedtaket, må du begynne å betale tilbake. Dette går frem av forvaltningsloven § 42 og hvordan vi må praktisere regelverket." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi kan utsette tilbakekrevingen til klagen er behandlet, for eksempel hvis det er sannsynlig at vedtaket blir omgjort. " +
                            "Du kan også søke om utsettelse av tilbakebetaling, men vi kan ikke ta hensyn til den økonomiske situasjonen din når vi vurderer om du kan utsette og betale tilbake." },
                )
            }
            includePhrase(RettTilInnsynRefVedlegg)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto)
    }
}