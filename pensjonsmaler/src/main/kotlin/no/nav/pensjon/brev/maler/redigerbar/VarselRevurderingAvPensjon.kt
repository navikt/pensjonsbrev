package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.SaksbehandlerValgSelectors.tittelValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Constants.BESKJED_TIL_NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.ETTERSENDELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselRevurderingAvPensjon : RedigerbarTemplate<VarselRevurderingAvPensjonDto> {

    // MF_000156
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_REVURDERING_AV_PENSJON
    override val kategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VarselRevurderingAvPensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om revurdering av pensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        val sakstype = pesysData.sakstype
        title {
            showIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett)) {
                text(
                    Bokmal to "Vi vurderer om du fortsatt har rett til ",
                    Nynorsk to "Vi vurderer om du framleis har rett til ",
                    English to "We are considering if you are still entitled to "
                )
                showIf(sakstype.isOneOf(AFP)) {
                    text(
                        Bokmal to "AFP",
                        Nynorsk to "AFP",
                        English to "contractual pension (AFP)",
                    )
                }.orShowIf(sakstype.isOneOf(AFP_PRIVAT)) {
                    text(
                        Bokmal to "AFP i privat sektor",
                        Nynorsk to "AFP i privat sektor",
                        English to "contractual pension (AFP) in the private sector",
                    )
                }.orShowIf(sakstype.isOneOf(ALDER)) {
                    text(
                        Bokmal to "alderspensjon",
                        Nynorsk to "alderspensjon",
                        English to "retirement pension",
                    )
                }.orShow {
                    val ytelse = fritekst("ytelse")
                    textExpr(
                        Bokmal to ytelse,
                        Nynorsk to ytelse,
                        English to ytelse,
                    )
                }
            }.orShowIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingReduksjon)) {
                text(
                    Bokmal to "Vi vurderer om pensjonen din skal reduseres",
                    Nynorsk to "Vi vurderer om pensjonen din skal reduserast",
                    English to "We are considering a reduction in your pension",
                )
            }
        }

        outline {
            showIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett)) {
                paragraph {
                    text(
                        Bokmal to "Dette er et varsel om at vi vurderer om du fortsatt har rett til ",
                        Nynorsk to "Dette er eit varsel om at vi vurderer om du framleis har rett til ",
                        English to "This letter is a notification that we are considering if you are still entitled to receive ",
                    )
                    showIf(sakstype.isOneOf(AFP)) {
                        text(
                            Bokmal to "AFP",
                            Nynorsk to "AFP",
                            English to "contractual pension (AFP)",
                        )
                    }.orShowIf(sakstype.isOneOf(AFP_PRIVAT)) {
                        text(
                            Bokmal to "AFP i privat sektor",
                            Nynorsk to "AFP i privat sektor",
                            English to "contractual pension (AFP) in the private sector",
                        )
                    }.orShowIf(sakstype.isOneOf(ALDER)) {
                        text(
                            Bokmal to "alderspensjon",
                            Nynorsk to "alderspensjon",
                            English to "retirement pension",
                        )
                    }.orShow {
                        val ytelse = fritekst("ytelse")
                        textExpr(
                            Bokmal to ytelse,
                            Nynorsk to ytelse,
                            English to ytelse,
                        )
                    }
                    text(
                        Bokmal to ".",
                        Nynorsk to ".",
                        English to ".",
                    )
                }
            }
            showIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingReduksjon)) {
                paragraph {
                    text(
                        Bokmal to "Dette er et varsel om at vi vurderer om din ",
                        Nynorsk to "Dette er eit varsel om at vi vurderer om din ",
                        English to "This letter is a notification that we are considering if your ",
                    )
                    showIf(sakstype.isOneOf(AFP)) {
                        text(
                            Bokmal to "AFP",
                            Nynorsk to "AFP",
                            English to "contractual pension (AFP)",
                        )
                    }.orShowIf(sakstype.isOneOf(AFP_PRIVAT)) {
                        text(
                            Bokmal to "AFP i privat sektor",
                            Nynorsk to "AFP i privat sektor",
                            English to "contractual pension (AFP) in the private sector",
                        )
                    }.orShowIf(sakstype.isOneOf(ALDER)) {
                        text(
                            Bokmal to "alderspensjon",
                            Nynorsk to "alderspensjon",
                            English to "retirement pension",
                        )
                    }.orShow {
                        val ytelse = fritekst("ytelse")
                        textExpr(
                            Bokmal to ytelse,
                            Nynorsk to ytelse,
                            English to ytelse,
                        )
                    }
                    text(
                        Bokmal to " skal beregnes på nytt på grunn av nye opplysninger.",
                        Nynorsk to " skal bli berekna på nytt på grunn av nye opplysningar.",
                        English to " will be recalculated based on new information.",
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Før vi fatter et vedtak i saken, har du rett til å uttale deg. Dette må du gjøre innen 14 dager etter at du har fått dette varselet.",
                    Nynorsk to "Før vi gjer eit vedtak i saka, har du rett til å uttale deg. Dette må du gjere innan 14 dagar etter at du har fått dette varselet.",
                    English to "Before we decide in your case, you are entitled to make a statement. You must do this within 14 days of receiving this letter.",
                )
            }
            title1 {
                text(
                    Bokmal to "Dette har skjedd",
                    Nynorsk to "Dette har skjedd",
                    English to "What has happened",
                )
            }
            paragraph {
                val beskrivelse = fritekst("Fritekst felt - beskriv hva som har skjedd i saken/ årsaken til revurderingen")
                textExpr(
                    Bokmal to beskrivelse,
                    Nynorsk to beskrivelse,
                    English to beskrivelse,
                )
            }
            title1 {
                text(
                    Bokmal to "Slik uttaler du deg",
                    Nynorsk to "Slik uttaler du deg",
                    English to "How to make a statement",
                )
            }
            showIf(sakstype.equalTo(ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på $DIN_PENSJON_URL og velge ${quoted("Kontakt Nav om pensjon")}, " +
                                "eller logge deg inn på $BESKJED_TIL_NAV_URL og velge ${quoted("Send beskjed til Nav")}. Du kan også sende uttalelsen din til oss i posten. " +
                                "Adressen finner du på $ETTERSENDELSE_URL.",
                        Nynorsk to "Du kan sende uttalen din ved å logge deg inn på $DIN_PENSJON_URL og velje ${quoted("Kontakt Nav om pensjon")}, " +
                                "eller logge deg inn på $BESKJED_TIL_NAV_URL og velje ${quoted("Send beskjed til Nav")}. Du kan også sende uttalen din til oss i posten. " +
                                "Adressa finn du på $ETTERSENDELSE_URL.",
                        English to "You can submit your statement by logging in to your personal $DIN_PENSJON_URL  pension page and selecting ${quoted("Kontakt Nav om pensjon")}, " +
                                "or by logging in to $BESKJED_TIL_NAV_URL and selecting ${quoted("Send beskjed til Nav")}. You can also send us your statement by post. " +
                                "You can find the address at $ETTERSENDELSE_URL.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velge ${quoted("Send beskjed til Nav")}. " +
                                "Du kan også sende uttalelsen din til oss i posten. Adressen finner du på $ETTERSENDELSE_URL.",
                        Nynorsk to "Du kan sende uttalen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velje ${quoted("Send beskjed til Nav")}. " + "" +
                                "Du kan også sende uttalen din til oss i posten. Adressa finn du på $ETTERSENDELSE_URL.",
                        English to "You can submit your statement by logging in to $BESKJED_TIL_NAV_URL and selecting ${quoted("Send beskjed til Nav")}. You can also send us your statement by post. You can find the address at $ETTERSENDELSE_URL.",
                    )
                }
            }
            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            showIf(sakstype.equalTo(FAM_PL)) {includePhrase(Felles.HarDuSpoersmaal.familiepleie)
            }.orShowIf(sakstype.equalTo(GJENLEV)) {includePhrase(Felles.HarDuSpoersmaal.gjenlevende)
            }.orShow{includePhrase(Felles.HarDuSpoersmaal.alder)}
        }
    }
}

