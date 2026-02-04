package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_MEDLEMSKAP
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDtoSelectors.SaksbehandlervalgSelectors.visSupplerendeStonadUforeFlykninger
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagSupplerendeStonadEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagMedlemskap : RedigerbarTemplate<UforeAvslagSupplerendeStonadEnkelDto> {

    override val featureToggle = FeatureToggles.avslagMedlemskap.toggle

    override val kode = UT_AVSLAG_MEDLEMSKAP
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-2",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"},
                nynorsk { + "Nav har avslått søknaden din om uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." },
                    nynorsk { +"Vi har avslått søknaden din om uføretrygd som vi fekk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd" },
                    nynorsk { +"Derfor får du ikkje uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi du ikke var medlem av folketrygden i de fem siste årene før du ble ufør. Du oppfyller heller ingen av unntaksreglene." },
                    nynorsk { +"Vi avslår søknaden din fordi du ikkje var medlem av folketrygda i dei fem siste åra før du blei ufør. Du oppfyller heller ingen av unntaksreglane." })
            }
            paragraph {
                text(bokmal { +"For å ha rett til uføretrygd, må du som hovedregel ha vært medlem av folketrygden i de siste fem årene fram til uføretidspunktet. " +
                        "Vi kan gjøre unntak fra hovedregelen dersom: " },
                    nynorsk { +"For å ha rett til uføretrygd, må du som hovudregel ha vore medlem av folketrygda i dei siste fem åra fram til uføretidspunktet. " +
                            "Vi kan gjere unntak frå hovudregelen dersom: " })
                list {
                    item {
                        text(bokmal { + "uførheten din skyldes en godkjent yrkesskade eller yrkessykdom " },
                            nynorsk { + "uførleiken din skuldast ein godkjent yrkesskade eller yrkessjukdom " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Eller dersom du har vært medlem av folketrygden i minst ett år umiddelbart før du søker om uføretrygd, og " },
                    nynorsk { +"Eller dersom du har vore medlem av folketrygda i minst eitt år umiddelbart før du søkjer om uføretrygd, og " })
                list {
                    item {
                        text(bokmal { + "ble ufør før du fylte 26 år og da var medlem av trygden eller " },
                            nynorsk { + "blei ufør før du fylte 26 år og då var medlem av trygda eller " })
                    }
                    item {
                        text(bokmal { + "etter fylte 16 år har vært medlem i trygden med unntak av maksimum fem år " },
                            nynorsk { + "etter fylte 16 år har vore medlem i trygda med unntak av maksimum fem år " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Eller dersom du var medlem av folketrygden på uføretidspunktet, og: " },
                    nynorsk { +"Eller dersom du var medlem av folketrygda på uføretidspunktet, og: " })
                list {
                    item {
                        text(bokmal { + "har tjent opp rett til minst halvparten av minsteytelsen for uføretrygd. " },
                            nynorsk { + "har tent opp rett til minst halvparten av minsteytinga for uføretrygd. " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Du flyttet til Norge " + fritekst("siste innflyttingsdato til Norge") + ", og ble da medlem av folketrygden. "},
                    nynorsk { +"Du flytta til Noreg " + fritekst("siste innflyttingsdato til Norge") + ", og blei då medlem av folketrygda. " })
            }

            paragraph {
                showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering } )
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { + "Vi har fastsatt uføretidspunktet ditt til " + fritekst("dato") + ". " +
                        "Vi har vurdert at inntektsevnen din ble varig nedsatt med minst halvparten fra dette tidspunktet." },
                    nynorsk { + "Vi har fastsett uføretidspunktet ditt til " + fritekst("dato") + ". " +
                            "Vi har vurdert at inntektsevna di blei varig nedsett med minst halvparten frå dette tidspunktet." }
                )
            }

            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd." },
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd." })
            }
            paragraph {
                text(bokmal { + "Vedtaket har vi gjort etter folketrygdloven § 12-2. " },
                    nynorsk { + "Vedtaket har vi gjort etter folketrygdlova § 12-2. " })
            }

            showIf(saksbehandlerValg.visSupplerendeStonadUforeFlykninger) {
                title1 {
                    text(bokmal { +"Supplerende stønad til uføre flyktninger " },
                        nynorsk { +"Supplerande stønad til uføre flyktningar " })
                }
                paragraph {
                    text(bokmal {
                        +"Hvis du har godkjent flyktningstatus etter § 28 i utlendingsloven gitt i vedtak fra Utlendingsnemnda (UDI), " +
                                "kan du søke om supplerende stønad til uføre flyktninger. Stønaden er behovsprøvd og all inntekt/formue fra Norge og utlandet blir regnet med. " +
                                "Inntekten/formuen til eventuell ektefelle, samboer eller registrert partner blir også regnet med. " +
                                "Du kan lese mer om supplerende stønad til uføre flyktninger på vår nettside nav.no. "
                    },
                        nynorsk {
                            +"Dersom du har godkjent flyktningstatus etter § 28 i utlendingslova gitt i vedtak frå Utlendingsnemnda (UDI), " +
                                    "kan du søkje om supplerande stønad til uføre flyktningar. Stønaden er behovsprøvd og all inntekt/formue frå Noreg og utlandet blir rekna med. " +
                                    "Inntekten/formua til eventuell ektefelle, sambuar eller registrert partnar blir òg rekna med. " +
                                    "Du kan lese meir om supplerande stønad til uføre flyktningar på vår nettside nav.no. "
                        } )
                }
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
