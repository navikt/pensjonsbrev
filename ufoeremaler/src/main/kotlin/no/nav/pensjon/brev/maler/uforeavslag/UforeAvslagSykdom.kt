package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.SaksbehandlervalgSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagSykdom : RedigerbarTemplate<UforeAvslagDto> {

    override val featureToggle = FeatureToggles.uforeAvslag.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVSLAG_SYKDOM
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-6",
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
                text(bokmal { +"Vi avslår søknaden din fordi det ikke er dokumentert at sykdom eller skade er hovedårsaken til din nedsatte inntektsevne." })
            }
            paragraph {
                text(bokmal { +"For å få innvilget uføretrygd må den varige nedsatte inntektsevnen i hovedsak skyldes varig sykdom eller skade. " +
                        "Dokumentasjonen i din sak viser at det i all hovedsak er andre forhold enn sykdom og skade som påvirker funksjons- og inntektsevnen din." })
            }
            showIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +
                    "For eksempel " +
                            fritekst("konkret individuell begrunnelse, som sosiale eller økonomiske forhold, viktig å skrive hva vi mener er hovedårsaken til nedsatt inntektsevne") + "."
                    })
                }
            }
            paragraph {
                text(bokmal { +
                "Vi har vurdert at sykdom eller skade har bidratt til nedsatt funksjonsevne, men det er ikke tilstrekkelig dokumentert at dette er hovedårsaken. " +
                        "Det er derfor for tidlig å ta stilling til om inntektsevnen din er varig nedsatt."})
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."})
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-6 og 12-7." })
            }

            includePhrase(HvaSkjerNa)
            includePhrase(RettTilAKlage)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
