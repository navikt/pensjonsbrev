package no.nav.pensjon.brev.ufore.maler.feilutbetaling

import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_VEDTAK_FEILUTBETALING_ETTERGITT_FORELDET
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.feilutbetaltTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.oversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakIngenTilbakekrevingForeldelse : RedigerbarTemplate<VedtakFeilutbetalingUforeIngenTilbakekrevingDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING_ETTERGITT_FORELDET
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
            text(bokmal { + "Vi krever ikke tilbake feilutbetalt uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { + "Vi viser til forhåndsvarselet vårt " + fritekst("dato") + "." })
            }
            paragraph {
                text(
                    bokmal { + "Du har fått utbetalt for mye uføretrygd i perioden "
                            + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format()
                            + ". Det feilutbetalte beløpet utgjør " + feilutbetaltTotalBeloep.format(CurrencyFormat) + " kroner inkludert skatt." },
                )
            }

            title1 { text(bokmal { + "Derfor krever vi ikke tilbake feilutbetalt uføretrygd" }) }
            paragraph {
                text(
                    bokmal { + "Hele det feilutbetalte beløpet er foreldet. Vi krever derfor ikke tilbake uføretrygden som ble utbetalt i perioden "
                        + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format() + "." }
                )
            }

            title2 { text(bokmal {+ "Foreldelse"}) }
            paragraph {
                text(bokmal {+ "Etter foreldelsesloven §§ 2 og 3 foreldes et pengekrav som hovedregel etter tre år, regnet fra tidspunktet da feilutbetalingen fant sted." })
            }
            paragraph {
                text(bokmal {+ "Den første feilutbetalingen ble gjort " + fritekst("dato") + ", og foreldelsesfristen for denne utbetalingen utløp " + fritekst("dato") + ". " +
                        "Nav har vurdert at hele beløpet som ble feilutbetalt i perioden "
                        + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format() + " er foreldet. " })
            }
            paragraph {
                text(bokmal {+ "Vi har også vurdert om unntaksregelen i foreldelsesloven § 10 kan benyttes. " +
                        "Denne gir en tilleggsfrist på ett år dersom Nav ikke har hatt nødvendig kunnskap om kravet. " +
                        "Nav fikk kunnskap om feilutbetalingen den " + fritekst("dato") + ", men det er ikke gjort fristavbrytende tiltak innen ett år etter dette. Unntaksregelen kan derfor ikke benyttes. " })
            }
            paragraph {
                text(bokmal {+ "Hele det feilutbetalte beløpet er foreldet. Vi krever derfor ikke tilbake uføretrygden som ble utbetalt i perioden "
                    + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format() + ". " +
                        "Vi har ikke vurdert om vilkårene for tilbakekreving etter folketrygdloven § 22-15 første ledd er oppfylt, ettersom hele beløpet er foreldet.  " })
            }
            paragraph {
                text(bokmal { + "Vedtaket er gjort etter §§ 2, 3 og 10 i foreldelsesloven, og vi viser til folketrygdloven § 22-14, som fastslår at foreldelsesloven gjelder for ytelser etter folketrygdloven. " })
            }

            includePhrase(Felles.RettTilAKlageKort)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}