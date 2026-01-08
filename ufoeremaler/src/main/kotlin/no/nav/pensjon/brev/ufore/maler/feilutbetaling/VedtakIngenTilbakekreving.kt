package no.nav.pensjon.brev.ufore.maler.feilutbetaling

import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.oversiktOverFeilutbetalinger
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_VEDTAK_FEILUTBETALING_INGEN_TILBAKEKREVING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.feilutbetaltTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.oversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakIngenTilbakekreving : RedigerbarTemplate<VedtakFeilutbetalingUforeIngenTilbakekrevingDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING_INGEN_TILBAKEKREVING
    override val kategori = Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak- ingen tilbakekreving av feilutbetalt beløp",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val feilutbetaltTotalBeloep = pesysData.feilutbetaltTotalBelop
        val sluttPeriodeForTilbakekreving = pesysData.sluttPeriodeForTilbakekreving
        val startPeriodeForTilbakekreving = pesysData.startPeriodeForTilbakekreving

        title {
            text(bokmal { + "Vi krever ikke tilbake pengene du har fått for mye i uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { + "I vedtaket datert " + fritekst("dato") + " ble uføretrygden din endret på grunn av " + fritekst("årsak") + "." })
            }
            paragraph {
                text(
                    bokmal { + "Vi har vurdert om du har fått riktig utbetaling tidligere. Du har fått utbetalt for mye i perioden "
                            + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format()
                            + ". Det feilutbetalte beløpet er brutto " + feilutbetaltTotalBeloep.format(CurrencyFormat) + " kroner." },
                )
            }
            paragraph {
                text (bokmal { + "Du har mottatt beløpet i god tro, og vi vurderer at du ikke kunne forstå at det var feil. "})
            }

            title1 { text(bokmal { + "Derfor krever vi ikke pengene tilbake" }) }
            paragraph {
                text(
                    bokmal { + "Nav har mottatt informasjon om " + fritekst("årsak") + ". Dette har ført til at du har fått utbetalt for mye i perioden "
                        + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format() + "." }
                )
            }
            paragraph {
                text(bokmal {+ "Etter folketrygdloven § 22-15 kan Nav kreve tilbake feilutbetalinger hvis mottakeren forstod eller burde ha forstått at det var en feil. " +
                        "Vi har vurdert om du kunne ha oppdaget feilen, basert på informasjon du har fått fra Nav."})
            }
            paragraph {
                text(bokmal {+ "Vi mener at du mottok beløpet i god tro, og at du ikke hadde grunnlag for å forstå at utbetalingen var feil. " +
                        "Vilkårene for tilbakekreving etter § 22-15 første ledd er derfor ikke oppfylt. " })
            }
            paragraph {
                text(bokmal {+ "Etter § 22-15 femte ledd kan Nav kreve tilbake beløp som fortsatt er i behold når feilen oppdages. " +
                        "Siden uføretrygd er en livsoppholdsytelse, antar vi at beløpet ikke er i behold. " +
                        "Vilkårene for tilbakekreving etter femte ledd er derfor heller ikke oppfylt." })
            }
            paragraph {
                text(bokmal {+ "Konklusjon: Du trenger ikke betale tilbake beløpet du fikk for mye i perioden "
                    + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format() + "." })
            }
            paragraph {
                text(bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15 første og femte ledd. " })
            }

            includePhrase(Felles.RettTilAKlageKort)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}