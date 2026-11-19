package no.nav.pensjon.brev.alder.maler.feilutbetaltbelop

import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.ubestemtForm
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.forhaandsvarselDato
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.sakstype
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.virkningFom
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.selectors.vedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.pesysData.virkningTom
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_04_061 Vedtak om at feilutbetalt beløp ikke vil bli tilbakekrevet
// Fått nytt innhold fra enhetene
@TemplateModelHelpers
object VedtakFeilutbetaltBeloepTilbakekrevesIkke : RedigerbarTemplate<VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto> {

    override val kode = Aldersbrevkoder.Redigerbar.FEILUTBETALT_BELOEP_TILBAKEKREVES_IKKE
    override val kategori = Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER, Sakstype.AFP, Sakstype.BARNEP, Sakstype.GJENLEV)

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
        val belopNetto = 1000 // pesysData.belopNetto

        val forhandsvarselSendt = saksbehandlervalg("forhandsvarselSendt", "Forhåndsvarsel sendt").bool()
        val paragraf2215AndrePunktum = saksbehandlervalg("paragraf2215AndrePunktum", "Folketrygdloven § 22-15 første ledd andre setning").bool()
        val kravFallBort = saksbehandlervalg("kravFallBort", "Krav reduseres eller faller bort").bool()
        val belopetIkkeIBehold = saksbehandlervalg("belopetIkkeIBehold", "Beløp er ikke i behold").bool()

        val sakTypeText = sakstype.ubestemtForm()


        title {
            text(bokmal { +"Du skal ikke betale tilbake feilutbetalt " + sakTypeText })
        }
        outline {
            showIf(forhandsvarselSendt) {
                paragraph {
                    text(bokmal { +"Vi viser til forhåndsvarselet vårt av " + pesysData.forhaandsvarselDato.format() + "." })
                }
            }

            paragraph {
                text(bokmal { +"Du har fått for mye " + sakTypeText + " utbetalt fra " + virkningFom.format() + " til " + virkningTom.format() + ". " +
                        "Dette er " + fritekst("nettobeløp") + " kroner inkludert skatt." })
            }

            paragraph {
                text(bokmal { +"Vi har kommet fram til at du ikke skal betale tilbake noe av dette beløpet." })
            }

            title1{
                text(bokmal { +"Rettslig grunnlag for vedtaket" })
            }

            paragraph {
                text(bokmal { +"Hvis du forstod eller burde forstått at utbetalingen du mottok, skyldtes en feil, kan vi kreve at du betaler tilbake det du har fått for mye utbetalt." })
            }

            paragraph {
                text(bokmal { +"Dette framgår av folketrygdloven § 22-15 første ledd, første setning." })
            }

            showIf(paragraf2215AndrePunktum) {
                paragraph {
                    text(bokmal { +"Hvis feilutbetalingen er forårsaket av at du uaktsomt har gitt Nav feilaktige eller mangelfulle opplysninger, eller meldte fra for sent om en endring, " +
                            "kan vi kreve at du betaler tilbake det du har fått for mye utbetalt." })
                }

                paragraph {
                    text(bokmal { +"Dette framgår av folketrygdloven § 22-15 første ledd andre setning og forvaltningspraksis." })
                }
            }

            paragraph {
                text(bokmal { +"Selv om vilkårene i § 22-15 første ledd ikke er oppfylt, kan vi kreve tilbake det som var i behold av det feilutbetalte beløpet da du ble kjent med feilen. "})
                showIf(kravFallBort) {
                    text(bokmal { +"Et slikt krav kan reduseres eller falle bort hvis det er spesielle forhold som taler for det." })
                }
            }

            paragraph {
                text(bokmal { +"Dette framgår av folketrygdloven § 22-15 femte ledd." })
            }

            title1{
                text(bokmal { +"Vurderingen vår" })
            }

            paragraph {
                text(bokmal { +"Feilutbetalingen skyldes " + fritekst("hva") + ". Videre legger vi til grunn at " + fritekst("hva") + ". " +
                        "Det går heller ikke klart frem av " + fritekst("vedtaket av <dato> // vedtakene du har mottatt fra oss at hva") + "." })
            }

            paragraph {
                text(bokmal { +"Det er ikke grunnlag for å si at " + fritekst("du har vært uaktsom i forhold til opplysningsplikten din overfor Nav // forstod eller burde forstått at du fikk for mye utbetalt <ytelse>") + "." })
            }

            paragraph {
                text(bokmal { +"Vilkårene for tilbakekreving i folketrygdloven § 22-15 første ledd er dermed ikke oppfylt." })
            }

            showIf(belopetIkkeIBehold) {
                paragraph {
                    text(bokmal { +"Vi legger til grunn at det feilutbetalte beløpet er medgått til løpende utgifter. Da er det heller ikke grunnlag for å kreve tilbake noe etter folketrygdloven § 22-15 femte ledd." })
                }
            } orShow {
                paragraph {
                    text(bokmal { +"Det fremgår av opplysningene i saken at noe av det feilutbetalte beløpet fortsatt er i behold hos deg. Vi mener likevel at det er grunnlag for ikke å kreve tilbakebetaling. " +
                            "Vi legger da vekt på at " + fritekst("begrunnelse") + ". " +
                            "Etter dette er heller ikke vilkårene for tilbakekreving etter folketrygdloven § 22-15 femte ledd oppfylt." })
                }
            }

            title1{
                text(bokmal { +"Vedtak" })
            }

            paragraph {
                text(bokmal { +"Vi krever ikke tilbake " + sakTypeText + " som er feilutbetalt i perioden " + virkningFom.format() + " til " + virkningTom.format() + "." })
            }

            title1{
                text(bokmal { +"Ønsker du innsyn?" })
            }
            paragraph {
                text(bokmal { +"Du kan logge deg inn på $DIN_PENSJON_URL for å se all kommunikasjon som har vært mellom deg og Nav i saken din. Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON hverdager kl. $NAV_KONTAKTSENTER_AAPNINGSTID. " })
            }

            includePhrase(HarDuSpoersmaal.alder)

        }
    }
}
