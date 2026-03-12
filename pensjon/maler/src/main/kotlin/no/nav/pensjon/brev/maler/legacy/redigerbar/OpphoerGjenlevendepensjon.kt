package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.blirSamboerBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.blirSamboerTidligereGift
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.erSamboerBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.gifterSeg
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.inngaaPartnerskap
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.tilbakekreving
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_030 Vedtak opphør av gjenlevendepensjon

@TemplateModelHelpers
object OpphoerGjenlevendepensjon : RedigerbarTemplate<OpphoerGjenlevendepensjonDto> {

//  override val featureToggle = FeatureToggles.brevmalOpphoerGjenlevendepensjon.toggle  TODO

    override val kode = Pesysbrevkoder.Redigerbar.GP_OPPHOER_GJENLEVENDEPENSJON
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING //TODO
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør av gjenlevendepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Gjenlevendepensjon - melding om vedtak" },
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
                    english {
                        +"According to the National Insurance Act paragraph 17-11, benefits to a surviving spouse end if he or she remarries. "
                        +"The pension ends as of the month in which the marriage was entered. "
                    }
                )
                showIf(saksbehandlerValg.gifterSeg) {
                    val dato = fritekst("dato")
                    text(
                        bokmal { +"Vi viser til at du giftet deg " + dato + "." },
                        english { +"We refer to your marriage of " + dato + "." }
                    )
                }
                showIf(saksbehandlerValg.inngaaPartnerskap) {
                    val dato = fritekst("dato")
                    text(
                        bokmal {
                            +"Registrert partnerskap er i folketrygdloven likestilt med ekteskap. "
                            +"Vi viser til at du inngikk partnerskap " + dato + "."
                        },
                        english {
                            +"According to the National Insurance Act, registered partnerships are equivalent to marriage. "
                            +"We refer to your registered partnership of " + dato + "."
                        }
                    )
                }
                showIf(saksbehandlerValg.blirSamboerBarn) {
                    val dato = fritekst("dato for samboerskap")
                    text(
                        bokmal {
                            +"Folketrygdloven likestiller samboerskap med ekteskap når samboerne tidligere har vært gift, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at dere fra " + dato + " er samboere med felles barn."
                        },
                        english {
                            +"The National Insurance Act considers cohabiting to be equivalent to marriage when the cohabitants previously have been married or have/have had children. "
                            +"We have based our decision on our finding that from " + dato + " you are cohabiting and have children in common."
                        }
                    )
                }
                showIf(saksbehandlerValg.erSamboerBarn) {
                    val dato = fritekst("dato for fødsel fellesbarn")
                    text(
                        bokmal {
                            +"Folketrygdloven likestiller samboerskap med ekteskap når samboerne tidligere har vært gift, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at dere fra " + dato + " er samboere med felles barn."
                        },
                        english {
                            +"The National Insurance Act considers cohabiting to be equivalent to marriage when the cohabitants previously have been married or have/have had children. "
                            +"We have based our decision on our finding that from " + dato + " you are cohabiting and have children in common."
                        }
                    )
                }
                showIf(saksbehandlerValg.blirSamboerTidligereGift) {
                    val dato = fritekst("dato for fødsel fellesbarn")
                    text(
                        bokmal {
                            +"Folketrygdloven likestiller samboerskap med ekteskap når samboerne tidligere har vært gift, eller har eller har hatt felles barn. "
                            +"Vi har lagt til grunn at dere tidligere har vært gift med hverandre og er samboere fra " + dato + "."
                        },
                        english {
                            +"The National Insurance Act considers cohabiting to be equivalent to marriage when the cohabitants previously have been married or have/have had children in common. "
                            +"We have based our decision on you having previously been married to each other and that you started cohabiting from " + dato + "."
                        }
                    )
                }
            }
            showIf(saksbehandlerValg.tilbakekreving) {
                paragraph {
                    text(
                        bokmal {
                            +"Fordi pensjonen din er opphørt med virkning tilbake i tid, medfører dette at du har fått utbetalt for mye i pensjon i en periode. "
                            +"Vi vil sende deg eget forhåndsvarsel om eventuell tilbakekreving av det feilutbetalte beløpet."
                        },
                        english {
                            +"Because your pension has been stopped and the stop takes effect back in time, you have incorrectly received pension payments during this period. "
                            +"We will send you a separate notification of whether you are required to pay back the money that were paid to you in error."
                        }
                    )
                }
            }
        }
    }
}