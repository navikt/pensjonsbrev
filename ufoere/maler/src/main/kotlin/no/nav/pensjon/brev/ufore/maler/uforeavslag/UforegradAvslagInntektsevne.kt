package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_INNTEKTSEVNE
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visVurderingIEU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.SaksbehandlervalgInntektSelectors.visVurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektEtterUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.inntektForUforhet
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.uforegrad
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurderingIEU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.UforeAvslagInntektPendataSelectors.vurderingIFU
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagInntektDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforegradAvslagInntektsevne : RedigerbarTemplate<UforeAvslagInntektDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_INNTEKTSEVNE
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-7",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om økt uføregrad"},
                nynorsk { + "Nav har avslått søknaden din om auka uføregrad"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om økt uføregrad som vi fikk den " + pesysData.kravMottattDato.format() + ". " +
                        "Du beholder uføregraden din på " + pesysData.uforegrad.format() + " prosent." },
                    nynorsk { +"Vi har avslått søknaden din om auka uføregrad som vi fekk den " + pesysData.kravMottattDato.format() + ". " +
                            "Du beheld uføregraden din på " + pesysData.uforegrad.format() + " prosent." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke økt uføregrad" },
                    nynorsk { +"Derfor får du ikkje auka uføregrad" })
            }
            paragraph {
                text(bokmal { +"Vi avslår søknaden din fordi inntektsevnen din ikke er varig nedsatt i større grad enn den uføregraden du allerede har." },
                    nynorsk { +"Vi avslår søknaden din fordi inntektsevna di ikkje er varig nedsett i større grad enn den uføregraden du allereie har." })
            }
            paragraph {
                text(bokmal { +"Uføretidspunktet ditt før var " + fritekst("dato for tidligere uføretidspunkt") +
                        ". Vi har fått opplysninger om at arbeidsevnen din er mer nedsatt fra " + fritekst("X (nytt alternativt uføretidspunkt)" + "." )},
                    nynorsk { +"Uføretidspunktet ditt før var " + fritekst("dato for tidligere uføretidspunkt") +
                            ". Vi har fått opplysninger om at arbeidsevna din er meir nedsett frå " + fritekst("X (nytt alternativt uføretidspunkt)" + "." )}
                )
            }

            paragraph {
                text(bokmal { +"Inntekten din før du ble ufør er fastsatt til " + pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner. " },
                    nynorsk { +"Inntekten din før du blei ufør er fastsett til " + pesysData.inntektForUforhet.format(CurrencyFormat) + " kroner. " })

                showIf(saksbehandlerValg.visVurderingIFU) {
                    text(bokmal { +pesysData.vurderingIFU },
                        nynorsk { +pesysData.vurderingIFU } )
                }.orShow {
                    text(bokmal { +fritekst("Begrunnelse for fastsatt IFU.") },
                        nynorsk { +fritekst("Begrunnelse for fastsatt IFU.") } )
                }
                text(bokmal {
                    +" Oppjustert til dagens verdi tilsvarer dette en inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                            "Du har en inntekt på " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner. "
                },
                    nynorsk {
                        +" Oppjustert til dagens verdi tilsvarar dette ei inntekt på " + fritekst("oppjustert IFU") + " kroner. " +
                                "Du har ei inntekt på " + pesysData.inntektEtterUforhet.format(CurrencyFormat) + " kroner. "
                    }
                )

                showIf(saksbehandlerValg.visVurderingIEU) {
                    text(bokmal { +pesysData.vurderingIEU },
                        nynorsk { +pesysData.vurderingIEU } )
                }.orShow {
                    text(bokmal { +fritekst("Begrunnelse for fastsatt IEU.") },
                        nynorsk { +fritekst("Begrunnelse for fastsatt IEU.") } )
                }
            }
            paragraph {
                text(bokmal {+"Vi har sammenlignet inntekten før og etter at du ble ufør. " +
                        "Vi har kommet fram til at uføregraden din er " + pesysData.uforegrad.format() + " prosent. " +
                        "Det viser at inntektsevnen din ikke er mer nedsatt enn det vi tidligere har vurdert."
                },
                    nynorsk {+"Vi har samanlikna inntekta før og etter at du blei ufør. " +
                            "Vi har kome fram til at uføregraden din er " + pesysData.uforegrad.format() + " prosent. " +
                            "Det viser at inntektsevna di ikkje er meir nedsett enn det vi tidlegare har vurdert."
                    }
                )
            }
            paragraph {
                text(bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføregrad."},
                    nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføregrad." })
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-7 og 12-10." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-7 og 12-10." })
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
