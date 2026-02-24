package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto.DenBesteKaken.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto.UtsiktenFraKontoret.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDtoSelectors.SaksbehandlerValgSelectors.denBesteKaken
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDtoSelectors.SaksbehandlerValgSelectors.kaffemaskinensTilgjengelighet
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDtoSelectors.SaksbehandlerValgSelectors.kontorplantenTorlill
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDtoSelectors.SaksbehandlerValgSelectors.utsiktenFraKontoret
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object BrukerTestBrev : RedigerbarTemplate<BrukerTestBrevDto> {

    override val featureToggle = FeatureToggles.brukertestbrev2025.toggle

    override val kode = Pesysbrevkoder.Redigerbar.BRUKERTEST_BREV_PENSJON_2025
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "TEST brev skribenten 2025",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Alt i dette brevet er tull" },
            )
        }
        outline {
            title1 { 
                text(bokmal { + "Vedtak" })
            }
            paragraph {
                text(
                    bokmal { + "Vi har " + fritekst("dato") + " mottatt søknaden din om " + fritekst("ytelse") + ". Eller kanskje ikke. Dette er nemlig bare et testbrev." },
                )
            }
            title1 {
                text(
                    bokmal { + "Jevnlig inspeksjon kreves" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Sokkelesten på kontorbyggets tak krever jevnlig inspeksjon for å unngå opphopning av løv og smuss som kan tette avløpene. Men det er ikke din jobb å verken tenke på eller utføre, så det er ikke relevant informasjon for deg." },
                )
            }

            showIf(saksbehandlerValg.utsiktenFraKontoret.equalTo(MOT_TRAER_OG_NATUR)) {
                title1 {
                    text(
                        bokmal { + "Trær og natur" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Fin balanse mellom distraksjon og dagslys." },
                    )
                }
            }.orShowIf(saksbehandlerValg.utsiktenFraKontoret.equalTo(MOT_PARKERINGSPLASSEN)) {
                title1 {
                    text(
                        bokmal { + "Parkeringsplassen" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Full oversikt over hvem som kommer og går." },
                    )
                }
            }

            ifNotNull(saksbehandlerValg.denBesteKaken) { denBesteKaken ->
                showIf(denBesteKaken.isOneOf(GULROTKAKE, RULLEKAKE, OSTEKAKE)) {
                    title1 {
                        showIf(denBesteKaken.equalTo(GULROTKAKE)) {
                            text(
                                bokmal { +"Den beste kaken er gulrotkake" },
                            )
                        }.orShowIf(denBesteKaken.equalTo(RULLEKAKE)) {
                            text(
                                bokmal { +"Den beste kaken er rullekake" },
                            )
                        }.orShowIf(denBesteKaken.equalTo(OSTEKAKE)) {
                            text(
                                bokmal { +"Den beste kaken er ostekake" },
                            )
                        }
                    }
                    paragraph {
                        showIf(denBesteKaken.equalTo(GULROTKAKE)) {
                            text(
                                bokmal { +"Gulrotkake er den beste kaken av alle kaker. Krydret, ærlig, og nesten sunn med 1 av 5 for dagen." },
                            )
                        }.orShowIf(denBesteKaken.equalTo(RULLEKAKE)) {
                            text(
                                bokmal { +"Rullekake er den beste kaken av alle kaker. Praktisk form, upåklagelig konsistens, lavt konfliktnivå." },
                            )
                        }.orShowIf(denBesteKaken.equalTo(OSTEKAKE)) {
                            text(
                                bokmal { +"Ostekake er den beste kaken av alle kaker. En kake som ikke prøver å imponere – den er bare perfekt." },
                            )
                        }
                    }
                }
            }
            showIf(saksbehandlerValg.kaffemaskinensTilgjengelighet) {
                title1 {
                    text(
                        bokmal { + "Kaffemaskinens tilgjengelighet" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Kaffemaskinen i tredje etasje har gått av med pensjon. Den er erstattet av et bilde av en banan med solbriller. Vi håper det gir deg den samme energien." },
                    )
                }
            }
            showIf(saksbehandlerValg.kontorplantenTorlill) {
                title1 {
                    text(
                        bokmal { + "Kontorplanten TorLill" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi minner om at den store fredsliljen i resepsjonen har fått navn etter en intern navnekonkurranse. Den heter nå TorLill. Du trenger ikke gjøre noe med dette." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { + "Knirkende stoler i rom 4A" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Noen stoler i 4A har begynt å kommunisere via knirk. Dette er en kjent, men ikke prioritert feil." },
                )
            }
            title1 {
                text(
                    bokmal { + "Lurer du på noe?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan nå oss på 815 493 00 på tirsdager mellom kl 11:03-11:15 hvis det var fullmåne søndag forrige uke." },
                )
            }
        }
    }
}
