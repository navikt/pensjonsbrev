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
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object BrukerTestBrev : RedigerbarTemplate<BrukerTestBrevDto> {
    override val kode = Pesysbrevkoder.Redigerbar.BRUKERTEST_BREV_PENSJON_2025
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BrukerTestBrevDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "TEST brev skribenten 2025",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Alt i dette brevet er tull",
            )
        }
        outline {
            title1 { 
                text(Bokmal to "Vedtak")
            }
            paragraph {
                textExpr(
                    Bokmal to "Vi har ".expr() + fritekst("dato") + " mottatt søknaden din om " + fritekst("ytelse") + ". Eller kanskje ikke. Dette er nemlig bare et testbrev.",
                )
            }
            title1 {
                text(
                    Bokmal to "Jevnlig inspeksjon kreves",
                )
            }
            paragraph {
                text(
                    Bokmal to "Sokkelesten på kontorbyggets tak krever jevnlig inspeksjon for å unngå opphopning av løv og smuss som kan tette avløpene. Men det er ikke din jobb å verken tenke på eller utføre, så det er ikke relevant informasjon for deg.",
                )
            }

            showIf(saksbehandlerValg.utsiktenFraKontoret.equalTo(MOT_TRAER_OG_NATUR)) {
                title1 {
                    text(
                        Bokmal to "Grønt er skjønt",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Fin balanse mellom distraksjon og dagslys.",
                    )
                }
            }.orShowIf(saksbehandlerValg.utsiktenFraKontoret.equalTo(MOT_PARKERINGSPLASSEN)) {
                title1 {
                    text(
                        Bokmal to "Grått er rått",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Full oversikt over hvem som kommer og går.",
                    )
                }
            }

            showIf(saksbehandlerValg.denBesteKaken.isOneOf(GULROTKAKE ,RULLEKAKE ,OSTEKAKE)) {
                title1 {
                    text(
                        Bokmal to "Min favorittkake",
                    )
                }
                paragraph {
                    showIf(saksbehandlerValg.denBesteKaken.equalTo(GULROTKAKE)) {
                        text(
                            Bokmal to "Gulrotkake er den beste kaken av alle kaker. Krydret, ærlig, og nesten sunn med 1 av 5 for dagen.",
                        )
                    }.orShowIf(saksbehandlerValg.denBesteKaken.equalTo(RULLEKAKE)) {
                        text(
                            Bokmal to "Rullekake er den beste kaken av alle kaker. Praktisk form, upåklagelig konsistens, lavt konfliktnivå.",
                        )
                    }.orShowIf(saksbehandlerValg.denBesteKaken.equalTo(OSTEKAKE)) {
                        text(
                            Bokmal to "Ostekake er den beste kaken av alle kaker. En kake som ikke prøver å imponere – den er bare perfekt.",
                        )
                    }
                }
            }
            showIf(saksbehandlerValg.kaffemaskinensTilgjengelighet) {
                title1 {
                    text(
                        Bokmal to "Kaffemaskinens tilgjengelighet",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Kaffemaskinen i tredje etasje har gått av med pensjon. Den er erstattet av et bilde av en banan med solbriller. Vi håper det gir deg den samme energien.",
                    )
                }
            }
            showIf(saksbehandlerValg.kontorplantenTorlill) {
                title1 {
                    text(
                        Bokmal to "Kontorplanten TorLill",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi minner om at den store fredsliljen i resepsjonen har fått navn etter en intern navnekonkurranse. Den heter nå TorLill. Du trenger ikke gjøre noe med dette.",
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Knirkende stoler i rom 4A",
                )
            }
            paragraph {
                text(
                    Bokmal to "Noen stoler i 4A har begynt å kommunisere via knirk. Dette er en kjent, men ikke prioritert feil.",
                )
            }
            title1 {
                text(
                    Bokmal to "Lurer du på noe?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan nå oss på 815 493 00 på tirsdager mellom kl 11:03-11:15 hvis det var fullmåne søndag forrige uke.",
                )
            }
        }
    }
}
