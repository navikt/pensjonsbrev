package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto.SaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.folketrygdlovenParagraf
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.generated.TBU2212_Generated
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_010 Vedtak avslag av gjenlevendepensjon

@TemplateModelHelpers
object AvslagGjenlevendepensjon : RedigerbarTemplate<AvslagGjenlevendepensjonDto> {

//    override val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjon.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag av gjenlevendepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Gjenlevendepensjon - melding om vedtak" },
                nynorsk { +"Gjenlevendepensjon - melding om vedtak" },
                english { +"Survivor's pension - notification of decision" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Nav viser til søknaden din om gjenlevendepensjon mottatt PE_Kravdata_Kravhode_KravMotattDato. Søknaden din er avslått." },
                    nynorsk { +"Nav viser til søknaden din om etterlatnepensjon motteken PE_Kravdata_Kravhode_KravMotattDato. Søknaden din er avslått." },
                    english { +"Nav makes reference to your application for a survivor's pension, received on PE_Kravdata_Kravhode_KravMotattDato. Your application has been denied." }
                )

                showIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(paragraf17_2_foersteEllerTredje_ledd)) {
                    text(
                        bokmal {
                            +"I folketrygdloven paragraf 17-2 er det angitt hvilke personer som kan få rett til ytelser til gjenlevende ektefelle. "
                            +"Du tilhører ikke denne gruppen fordi <FRITEKST: Angi grunn>."
                        },
                        nynorsk {
                            +"I folketrygdlova paragraf 17-2 er det fastsett kven som kan få rett til ytingar til attlevande ektefelle. "
                            +"Du høyrer ikkje til denne gruppa fordi <FRITEKST: Angi grunn>."
                        },
                        english {
                            +"The National Insurance Act paragraph 17-2 specifies who may have the right to receive benefit as a surviving spouse. "
                            +"You are not entitled to survivor’s benefit in accordance with this provision because <FRITEKST: Angi grunn>."
                        }
                    )
                }

                showIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(paragraf17_2_andre_ledd)) {
                    text(
                        bokmal {
                            +"Etter folketrygdloven paragraf 17-2 andre ledd kan det gis gjenlevendeytelser dersom ektefellen er forsvunnet og det er avsagt kjennelse eller dom om at vedkommende formodes å være død. "
                            +"Du har ikke rett til gjenlevendepensjon etter denne bestemmelsen fordi <FRITEKST: Angi grunn>."
                        },
                        nynorsk {
                            +"Etter folketrygdlova paragraf 17-2 andre ledd kan det givast ytingar til attlevande dersom ektefellen er forsvunnen, og det er avsagt kjennelse eller dom om at vedkomande er rekna som død. "
                            +"Du har ikkje rett til attlevandepensjon etter denne føresegna fordi <FRITEKST: Angi grunn>."
                        },
                        english {
                            +"According to the National Insurance Act paragraph 17-2 second sub-section, survivor's benefit may be awarded if the spouse can not be found or a ruling or judgment has been made that the spouse is assumed dead. "
                            +"You are not entitled to survivor's benefit in accordance with this provision because <FRITEKST: Angi grunn>."
                        }
                    )
                }

                showIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(paragraf17_3)) {
                    text(
                        bokmal {
                            +"Folketrygdloven likestiller samboerskap med ekteskap når samboerne tidligere har vært gift, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at dere fra "
                        },
                        nynorsk {
                            +"Folketrygdlova likestiller sambuarskap med ekteskap når sambuarane tidlegare har vore gifte, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at dere frå "
                        },
                        english {
                            +"The National Insurance Act considers cohabiting to be equivalent to marriage when the cohabitants previously have been married or have/have had children. "
                            +"We have based our decision on our finding that from "
                        }
                    )
                    showIf(saksbehandlerValg.folketrygdlovenAlternativ.isOneOf(blirSamboerOgHarFellesBarn)) {
                        text(bokmal { +datoSamboerskap }, nynorsk { +datoSamboerskap }, english { +datoSamboerskap })
                    }.orShow {
                        text(bokmal { +datoFellesbarn }, nynorsk { +datoFellesbarn }, english { +datoFellesbarn })
                    }
                    text(bokmal { +" er samboere med felles barn." }, nynorsk { +" er sambuarar med felles barn." }, english { +" you are cohabiting and have children in common." })
                }

                showIf(saksbehandlerValg.folketrygdlovenAlternativ.isOneOf(blirSamboerTidligereGift)) {
                    val dato = fritekst("dato for fødsel av fellesbarn")
                    text(
                        bokmal {
                            +"Folketrygdloven likestiller samboerskap med ekteskap når samboerne tidligere har vært gift, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at dere tidligere har vært gift med hverandre og er samboere fra " + dato + "."
                        },
                        nynorsk {
                            +"Folketrygdlova likestiller sambuarskap med ekteskap når sambuarane tidlegare har vore gifte, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at de tidlegare har vore gifte med kvarandre og er sambuarar frå " + dato + "."
                        },
                        english {
                            +"The National Insurance Act considers cohabiting to be equivalent to marriage when the cohabitants previously have been married or have/have had children in common. "
                            +"We have based our decision on you having previously been married to each other and that you started cohabiting from " + dato + "."
                        }
                    )
                }
            }

            showIf(saksbehandlerValg.opphoerMedTilbakekreving) {
                paragraph {
                    text(
                        bokmal {
                            +"Fordi pensjonen din er opphørt med virkning tilbake i tid, medfører dette at du har fått utbetalt for mye i pensjon i en periode. "
                            +"Vi vil sende deg eget forhåndsvarsel om eventuell tilbakekreving av det feilutbetalte beløpet."
                        },
                        nynorsk {
                            +"Fordi pensjonen din er falle bort med verknad tilbake i tid, fører dette til at du har fått utbetalt for mykje pensjon i ein periode. "
                            +"Vi vil sende deg eit eige førehandsvarsel om eventuell tilbakekrevjing av det feilutbetalte beløpet."
                        },
                        english {
                            +"Because your pension has been stopped and the stop takes effect back in time, you have incorrectly received pension payments during this period. "
                            +"We will send you a separate notification of whether you are required to pay back the money that were paid to you in error."
                        }
                    )
                }
            }

            includePhrase(TBU2212_Generated(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}