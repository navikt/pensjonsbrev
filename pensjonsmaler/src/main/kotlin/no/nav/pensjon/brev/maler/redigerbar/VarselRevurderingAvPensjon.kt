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
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselRevurderingAvPensjon : RedigerbarTemplate<VarselRevurderingAvPensjonDto> {

    // MF_000156
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_REVURDERING_AV_PENSJON
    override val kategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om revurdering av pensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        val sakstype = pesysData.sakstype
        val sakstypeText = sakstype.format().ifNull(fritekst("ytelse"))
        title {
            showIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett)) {
                text(
                    bokmal { + "Vi vurderer om du fortsatt har rett til " + sakstypeText },
                    nynorsk { + "Vi vurderer om du framleis har rett til " + sakstypeText },
                    english { + "We are considering if you are still entitled to " + sakstypeText },
                )
            }.orShowIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingReduksjon)) {
                text(
                    bokmal { + "Vi vurderer om pensjonen din skal reduseres" },
                    nynorsk { + "Vi vurderer om pensjonen din skal reduserast" },
                    english { + "We are considering a reduction in your pension" },
                )
            }
        }

        outline {
            showIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett)) {
                paragraph {
                    text(
                        bokmal { + "Dette er et varsel om at vi vurderer om du fortsatt har rett til " .expr() + sakstypeText + "." },
                        nynorsk { + "Dette er eit varsel om at vi vurderer om du framleis har rett til " .expr() + sakstypeText + "." },
                        english { + "This letter is a notification that we are considering if you are still entitled to receive " .expr() + sakstypeText + "." },
                    )

                }
            }
            showIf(saksbehandlerValg.tittelValg.isOneOf(VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingReduksjon)) {
                paragraph {
                    text(
                        bokmal { + "Dette er et varsel om at vi vurderer om din " + sakstypeText + " skal beregnes på nytt på grunn av nye opplysninger." },
                        nynorsk { + "Dette er eit varsel om at vi vurderer om din " + sakstypeText +" skal bli berekna på nytt på grunn av nye opplysningar." },
                        english { + "This letter is a notification that we are considering if your " + sakstypeText + " will be recalculated based on new information." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Før vi fatter et vedtak i saken, har du rett til å uttale deg. Dette må du gjøre innen 14 dager etter at du har fått dette varselet." },
                    nynorsk { + "Før vi gjer eit vedtak i saka, har du rett til å uttale deg. Dette må du gjere innan 14 dagar etter at du har fått dette varselet." },
                    english { + "Before we decide in your case, you are entitled to make a statement. You must do this within 14 days of receiving this letter." },
                )
            }
            title1 {
                text(
                    bokmal { + "Dette har skjedd" },
                    nynorsk { + "Dette har skjedd" },
                    english { + "What has happened" },
                )
            }
            paragraph {
                val beskrivelse = fritekst("Fritekst felt - beskriv hva som har skjedd i saken/ årsaken til revurderingen")
                text(
                    bokmal { + beskrivelse },
                    nynorsk { + beskrivelse },
                    english { + beskrivelse },
                )
            }
            title1 {
                text(
                    bokmal { + "Slik uttaler du deg" },
                    nynorsk { + "Slik uttaler du deg" },
                    english { + "How to make a statement" },
                )
            }
            showIf(sakstype.equalTo(ALDER)) {
                paragraph {
                    text(
                        bokmal { + "Du kan sende uttalelsen din ved å logge deg inn på $DIN_PENSJON_URL og velge " + quoted("Kontakt Nav om pensjon") +", " +
                                "eller logge deg inn på $BESKJED_TIL_NAV_URL og velge " + quoted("Send beskjed til Nav") +". Du kan også sende uttalelsen din til oss i posten. " +
                                "Adressen finner du på $ETTERSENDELSE_URL." },
                        nynorsk { + "Du kan sende uttalen din ved å logge deg inn på $DIN_PENSJON_URL og velje " + quoted("Kontakt Nav om pensjon") + ", " +
                                "eller logge deg inn på $BESKJED_TIL_NAV_URL og velje " +  quoted("Send beskjed til Nav") + ". Du kan også sende uttalen din til oss i posten. " +
                                "Adressa finn du på $ETTERSENDELSE_URL." },
                        english { + "You can submit your statement by logging in to your personal $DIN_PENSJON_URL pension page and selecting " + quoted("Kontakt Nav om pensjon") +", " +
                                "or by logging in to $BESKJED_TIL_NAV_URL and selecting " + quoted("Send beskjed til Nav") +". You can also send us your statement by post. " +
                                "You can find the address at $ETTERSENDELSE_URL." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Du kan sende uttalelsen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velge " + quoted("Send beskjed til Nav") +". " +
                                "Du kan også sende uttalelsen din til oss i posten. Adressen finner du på $ETTERSENDELSE_URL." },
                        nynorsk { + "Du kan sende uttalen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velje " + quoted("Send beskjed til Nav") +". " +
                                "Du kan også sende uttalen din til oss i posten. Adressa finn du på $ETTERSENDELSE_URL." },
                        english { + "You can submit your statement by logging in to $BESKJED_TIL_NAV_URL and selecting " + quoted("Send beskjed til Nav") +". You can also send us your statement by post. You can find the address at $ETTERSENDELSE_URL." },
                    )
                }
            }
            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            showIf(sakstype.equalTo(FAM_PL)) {
                includePhrase(Felles.HarDuSpoersmaal.familiepleie)
            }.orShowIf(sakstype.equalTo(GJENLEV)) {
                includePhrase(Felles.HarDuSpoersmaal.gjenlevende)
            }.orShow { includePhrase(Felles.HarDuSpoersmaal.alder) }
        }
    }
}

