package no.nav.pensjon.brev.ufore.maler.feilutbetaling

import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_VEDTAK_FEILUTBETALING_ETTERGITT_FORELDET
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.feilutbetaltTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.oversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeIngenTilbakekrevingDtoSelectors.pesysData
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakIngenTilbakekrevingForeldelse : RedigerbarTemplate<VedtakFeilutbetalingUforeIngenTilbakekrevingDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING_ETTERGITT_FORELDET
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
                    bokmal { + "Derfor krever vi ikke beløpet tilbake " },
                    nynorsk { + "Derfor krev vi ikkje beløpet tilbake " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har vurdert at hele det feilutbetalte beløpet er foreldet. Når et pengekrav er foreldet, " +
                            "kan vi som hovedregel ikke kreve det tilbake.  " },
                    nynorsk { + "Vi har vurdert at heile det feilutbetalte beløpet er forelda. Når eit pengekrav er forelda, " +
                            "kan vi som hovudregel ikkje krevje det tilbake. " }
                )
            }
            title1 {
                text(
                    bokmal { + "Derfor vurderer vi at beløpet er foreldet " },
                    nynorsk { + "Derfor vurderer vi at beløpet er forelda " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Den første feilutbetalingen ble gjort " + dato + ", og denne feilutbetalingen ble foreldet " + dato + ". " },
                    nynorsk { + "Den første feilutbetalinga blei gjort " + dato + ", og denne feilutbetalinga blei forelda " + dato + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Når et pengekrav er foreldet, kan vi ikke kreve det tilbake. Et pengekrav regnes som hovedregel som foreldet " +
                            "etter tre år fra feilutbetalingen fant sted. Det står i foreldelsesloven §§ 2 og 3. " },
                    nynorsk { + "Når eit pengekrav er forelda, kan vi ikkje krevje det tilbake. Eit pengekrav blir som hovudregel rekna som forelda " +
                            "etter tre år frå feilutbetalinga fann stad. Det står i foreldingslova §§ 2 og 3. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har også vurdert om unntaksregelen i foreldelsesloven § 10 kan brukes. Denne gir en tilleggsfrist på 1 år i " +
                            "de tilfellene vi ikke har fått informasjon om forholdene som har ført til feilutbetalingen. " +
                            "Vi fikk informasjon om feilutbetalingen dato, vi har ikke gjort fristavbrytende tiltak innen ett år etter dette. " +
                            "Derfor kan vi ikke kan bruke unntaksregler i foreldelsesloven som forlenger den vanlige fristen. " },
                    nynorsk { + "Vi har òg vurdert om unntaksregelen i foreldingslova § 10 kan brukast. Denne gir ein tilleggsfrist på 1 år " +
                            "i dei tilfella vi ikkje har fått informasjon om forholda som har ført til feilutbetalinga. " +
                            "Vi fekk informasjon om feilutbetalinga dato, og vi har ikkje gjort fristavbrytande tiltak innan eitt år etter dette. " +
                            "Derfor kan vi ikkje bruke unntaksreglar i foreldingslova som forlenger den vanlege fristen. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket har vi gjort etter §§ 2, 3 og 10 i foreldelsesloven og folketrygdloven § 22-14 " },
                    nynorsk { + "Vedtaket er gjort etter §§ 2, 3 og 10 i foreldingslova og folketrygdlova § 22-14. " }
                )
            }
        }
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}