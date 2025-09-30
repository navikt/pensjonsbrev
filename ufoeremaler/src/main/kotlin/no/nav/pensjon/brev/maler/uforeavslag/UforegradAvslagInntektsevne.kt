package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_INNTEKTSEVNE
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visVurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektEtterUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektForUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforegradAvslagInntektsevne : RedigerbarTemplate<UforeAvslagInntektDto> {

    override val featureToggle = FeatureToggles.uforeAvslag.toggle

    override val kode = UT_AVSLAG_OKT_GRAD_INNTEKTSEVNE
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-7",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om økt uføregrad"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått din søknad om økt uføregrad som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke økt uføregrad" })
            }
            paragraph {
                text(bokmal { +"Inntektsevnen din er ikke ytterligere varig nedsatt." })
            }
            paragraph {
                text(bokmal { +"Du mottar " + fritekst("nåværende uføregrad") + " prosent uføretrygd. " +
                        "Uføregraden kan økes dersom inntektsevnen blir varig nedsatt med mer enn dette, på grunn av sykdom eller skade." })
            }
            paragraph {
                text(bokmal { +"Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og har vurdert hvor mye inntektsevnen din er varig nedsatt." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }
            showIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { + fritekst("Lim inn teksten fra vilkårsvurderingen her") })
                }
            }
            showIf(!saksbehandlerValg.VisVurderingFraVilkarvedtak and !saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { + fritekst("Konkret begrunnelse der det er nødvendig") })
                }
            }

            paragraph {
                text(bokmal { +"Inntekten din før du ble ufør er fastsatt til " +
                    pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner." }
                    )

                showIf(saksbehandlerValg.visVurderingIFU) {
                    text( bokmal { + pesysData.vurderingIFU })
                }.orShow {
                    text( bokmal { + fritekst("Begrunnelse for fastsatt IFU.") })
                }
                text( bokmal { + " Oppjustert til dagens verdi tilsvarer dette en inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                    "Du har en inntekt på " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner, " +
                    "og vi har derfor fastsatt din nedsatte inntektsevne til " +
                    fritekst("sett inn fastsatt uføregrad før avrunding") + " prosent. " })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-7 og 12-10." })
            }

            includePhrase(RettTilAKlageKort)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
