package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenAlternativ.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.folketrygdlovenAlternativ
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.opphoerMedTilbakekreving
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.generated.TBU2212_Generated
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_030 Vedtak opphør av gjenlevendepensjon

@TemplateModelHelpers
object OpphoerGjenlevendepensjon : RedigerbarTemplate<OpphoerGjenlevendepensjonDto> {

    override val featureToggle = FeatureToggles.brevmalOpphoerGjenlevendepensjon.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_OPPHOER_GJENLEVENDEPENSJON
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør av gjenlevendepensjon",
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
                    bokmal {
                        +"Etter folketrygdloven paragraf 17-11 faller ytelser til gjenlevende ektefelle bort dersom han eller hun gifter seg igjen. "
                        +"Pensjonen opphører fra måneden etter at eksteskapet ble inngått. "
                    },
                    nynorsk {
                        +"«Etter folketrygdlova paragraf 17‑11 fell ytingar til attlevande ektefelle bort dersom han eller ho giftar seg på nytt. "
                        +"Pensjonen fell bort frå månaden etter at ekteskapet vart inngått. "
                    },
                    english {
                        +"According to the National Insurance Act paragraph 17-11, benefits to a surviving spouse end if he or she remarries. "
                        +"The pension ends as of the month in which the marriage was entered. "
                    }
                )

                showIf(
                    saksbehandlerValg.folketrygdlovenAlternativ.equalTo(gifterSeg)
                ) {
                    val dato = fritekst("dato")
                    text(
                        bokmal { +"Vi viser til at du giftet deg " + dato + "." },
                        nynorsk { +"Vi viser til at du har gifta deg " + dato + "." },
                        english { +"We refer to your marriage of " + dato + "." }
                    )
                }

                showIf(
                    saksbehandlerValg.folketrygdlovenAlternativ.equalTo(inngaaPartnerskap)
                ) {
                    val dato = fritekst("dato")
                    text(
                        bokmal {
                            +"Registrert partnerskap er i folketrygdloven likestilt med ekteskap. "
                            +"Vi viser til at du inngikk partnerskap " + dato + "."
                        },
                        nynorsk {
                            +"Registrert partnarskap er i folketrygdlova likestilt med ekteskap. "
                            +"«Vi viser til at du har inngjekk partnarskap " + dato + "."
                        },
                        english {
                            +"According to the National Insurance Act, registered partnerships are equivalent to marriage. "
                            +"We refer to your registered partnership of " + dato + "."
                        }
                    )
                }

                showIf(
                    saksbehandlerValg.folketrygdlovenAlternativ.isOneOf(
                        erErSamboerOgFellesBarn, blirSamboerOgHarFellesBarn
                    )
                )
                {
                    val datoSamboerskap = fritekst("dato for samboerskap")
                    val datoFellesbarn = fritekst("dato for fødsel av fellesbarn")
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