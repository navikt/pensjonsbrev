package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.uforeavslag.UforeAvslagIFUOktStilling.fritekst
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_MEDLEMSKAP
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagMedlemskap : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val featureToggle = FeatureToggles.avslagMedlemskap.toggle

    override val kode = UT_AVSLAG_MEDLEMSKAP
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-2",
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
                text(bokmal { +"Du var ikke medlem av folketrygden i fem år før du ble ufør. Du oppfyller heller ingen av unntaksreglene." })
            }
            paragraph {
                text(bokmal { +"For å ha rett til uføretrygd, må du ha vært medlem av folketrygden i de siste fem årene fram til uføretidspunktet. " })
            }
            paragraph {
                text(bokmal { +"Vi kan gjøre unntak fra hovedregelen dersom: " })
                list {
                    item {
                        text(bokmal { + "uførheten din skyldes en godkjent yrkesskade eller yrkessykdom " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Eller dersom du har vært medlem av folketrygden i minst ett år umiddelbart før du setter fram krav om uføretrygd, og " })
                list {
                    item {
                        text(bokmal { + "ble ufør før du fylte 26 år og da var medlem av trygden eller " })
                    }
                    item {
                        text(bokmal { + "etter fylte 16 år har vært medlem i trygden med unntak av maksimum fem år " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Eller dersom du var medlem av folketrygden på uføretidspunktet, og: " })
                list {
                    item {
                        text(bokmal { + "har tjent opp rett til minst halvparten av minsteytelsen for uføretrygd. " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Du flyttet til Norge " + fritekst("innflyttingsdato til Norge") + ", og ble da medlem av folketrygden. " +
                        "Vi har fastsatt uføretidspunktet ditt til " + fritekst("dato") + ". Da ble inntektsevnen din varig nedsatt med minst halvparten. " })
            }

            paragraph {
                showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                    text(bokmal { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, vi avslår derfor søknaden din om uføretrygd. Vedtaket er gjort etter folketrygdloven § 12-2. " })
            }
            title1 {
                text(bokmal { +"Supplerende stønad til uføre flyktninger " })
            }
            paragraph {
                text(bokmal { +"Hvis du har godkjent flyktningstatus etter § 28 i utlendingsloven gitt i vedtak fra Utlendingsnemnda (UDI), " +
                        "kan du søke om supplerende stønad til uføre flyktninger. Stønaden er behovsprøvd og all inntekt/formue fra Norge og utlandet blir regnet med. " +
                        "Inntekten/formuen til eventuell ektefelle, samboer eller registrert partner blir også regnet med. " +
                        "Du kan lese mer om supplerende stønad til uføre flyktninger på vår nettside nav.no. " })
            }

            includePhrase(RettTilAKlageLang)
            includePhrase(RettTilInnsynRefVedlegg)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
