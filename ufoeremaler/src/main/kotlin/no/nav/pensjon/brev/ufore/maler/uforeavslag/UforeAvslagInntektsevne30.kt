package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_INNTEKTSEVNE_30
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visVurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektEtterUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektForUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagInntektsevne30 : RedigerbarTemplate<UforeAvslagInntektDto> {

    override val kode = UT_AVSLAG_INNTEKTSEVNE_30
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-7",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått din søknad om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om uføretrygd fordi inntektsevnen din er varig nedsatt med mindre enn 30 prosent." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { +"Inntekten din før du ble ufør er fastsatt til " +
                    pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner." })
                showIf(saksbehandlerValg.visVurderingIFU) {
                    text( bokmal { + pesysData.vurderingIFU })
                }.orShow {
                    text( bokmal { + fritekst("Begrunnelse for fastsatt IFU.") })
                }
                text(bokmal { + " Oppjustert til dagens verdi tilsvarer dette en inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                    "Du har en inntekt på " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner, " +
                    "og vi har derfor fastsatt din nedsatte inntektsevne til " +
                    fritekst("sett inn fastsatt uføregrad før avrunding") + " prosent. " +
                        "Fordi du har en godkjent yrkesskade eller yrkessykdom, må inntektsevnen din være varig nedsatt med minst 30 prosent" })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-7 og 12-17." })
            }

            includePhrase(Felles.HvaSkjerNa)
            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
