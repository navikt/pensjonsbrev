package no.nav.pensjon.brev.ufore.maler.feilutbetaling

import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.oversiktOverFeilutbetalinger
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
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
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakIngenTilbakekreving : RedigerbarTemplate<VedtakFeilutbetalingUforeIngenTilbakekrevingDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING_INGEN_TILBAKEKREVING
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak- ingen tilbakekreving av feilutbetalt beløp",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val dato = fritekst("dato")
        val arsak = fritekst("årsak")

        title {
            text(
                bokmal { + "Vi krever ikke tilbake feilutbetalt uføretrygd "},
                nynorsk { + "Vi krev ikkje tilbake feilutbetalt uføretrygd " }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "I perioden " + dato + " til " + dato + " fikk du utbetalt beløp kroner for mye i uføretrygd. " +
                            "Vi krever ikke tilbake beløpet du har fått utbetalt for mye. " },
                    nynorsk { + "I perioden " + dato + " til " + dato + " fekk du utbetalt beløp kroner for mykje i uføretrygd. " +
                            "Vi krev ikkje tilbake beløpet du har fått utbetalt for mykje. " }
                )
            }
            title1 {
                text(
                    bokmal { + "Derfor krever vi ikke pengene tilbake " },
                    nynorsk { + "Derfor krev vi ikkje pengane tilbake " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har fått informasjon om " + arsak + ". Denne endringen har ført til at du har fått " +
                            "utbetalt for mye i perioden " + dato + " til " + dato + ".  " },
                    nynorsk { + "Vi har fått informasjon om " + arsak + ". Denne endringa har ført til at du har fått " +
                            "utbetalt for mykje i perioden " + dato + " til " + dato + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi vurderer at du ikke kunne forstå at utbetalingen var feil, og at du derfor mottok beløpet i god tro. " +
                            "Ifølge folketrygdloven § 22-15 kan vi bare kreve tilbake feilutbetalinger hvis mottakeren forstod eller " +
                            "burde ha forstått at det var en feil.  " },
                    nynorsk { + "Vi vurderer at du ikkje kunne forstå at utbetalinga var feil, og at du derfor mottok beløpet i god tru. " +
                            "Ifølgje folketrygdlova § 22-15 kan vi berre krevje tilbake feilutbetalingar dersom mottakaren forstod eller " +
                            "burde ha forstått at det var ein feil. " }
                )
            }
            paragraph {
                text(
                    bokmal { + fritekst("fritekst") },
                    nynorsk { + fritekst("fritekst") }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi kan kreve tilbake feilutbetalte beløp som fortsatt er i behold når feilen oppdages. " +
                            "Dette står i folketrygdloven § 22-15 femte ledd.  Siden uføretrygd er en livsoppholdsytelse, " +
                            "antar vi at beløpet ikke er i behold. Derfor kan vi ikke kreve tilbake beløpet etter dette vilkåret. " },
                    nynorsk { + "Vi kan krevje tilbake feilutbetalte beløp som framleis er i behald når feilen blir oppdaga. " +
                            "Dette står i folketrygdlova § 22-15 femte ledd. Sidan uføretrygd er ei livsopphaldsyting, " +
                            "går vi ut frå at beløpet ikkje er i behald. Derfor kan vi ikkje krevje tilbake beløpet etter dette vilkåret. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15 første og femte ledd. " },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 22-15 første og femte ledd. " }
                )
            }

        }
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}