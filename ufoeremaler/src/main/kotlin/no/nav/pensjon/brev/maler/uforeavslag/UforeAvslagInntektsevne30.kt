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
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektEtterUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektForUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagInntektsevne30 : RedigerbarTemplate<UforeAvslagInntektDto> {

    override val featureToggle = FeatureToggles.uforeAvslag.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVSLAG_ALDER
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
                text(bokmal { +"Vi har avslått søknaden din om uføretrygd fordi inntektsevnen din ikke er nok varig nedsatt. " +
                        "Fordi du har en godkjent yrkesskade eller yrkessykdom, må inntektsevnen din være varig nedsatt med minst 30 prosent." })
            }
            paragraph {
                text(bokmal { +"Inntekten din før du ble ufør er fastsatt til " +
                    pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner." +
                    fritekst("Begrunnelse for fastsatt IFU.") +
                    " Oppjustert til dagens verdi tilsvarer dette en inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                    "Du har en inntekt på " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner, " +
                    "og vi har derfor fastsatt din nedsatte inntektsevne til " +
                    fritekst("sett inn fastsatt uføregrad før avrunding") + " prosent. " })
            }
            paragraph {
                text(bokmal { + "Vi vurderer at inntektsevnen din ikke er varig nedsatt med minst 30 prosent." })
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-7 og 12-17." })
            }

            includePhrase(HvaSkjerNa)
            includePhrase(RettTilAKlage)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
