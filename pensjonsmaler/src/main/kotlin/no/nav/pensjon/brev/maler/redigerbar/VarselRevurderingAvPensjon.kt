package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata.saksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.SaksbehandlerValgSelectors.revurderingAvRett
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.SaksbehandlerValgSelectors.revurderingReduksjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.sakstype
import no.nav.pensjon.brev.maler.fraser.common.Constants.BESKJED_TIL_NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.ETTERSENDELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
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
        title {
            showIf(saksbehandlerValg.revurderingAvRett) {
                text(
                    Bokmal to "Vi vurderer om du forsatt har rett til <sakTypeKode>",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(saksbehandlerValg.revurderingReduksjon) {
                text(
                    Bokmal to "Vi vurderer om pensjonen din skal reduseres",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
        outline {
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
                text(
                    Bokmal to "<Fritekstfelt - beskriv hva som har skjedd i saken/ årsaken til revurderingen>",
                    Nynorsk to "<Fritekstfelt - beskriv hva som har skjedd i saken/ årsaken til revurderingen>",
                    English to "<Fritekstfelt - beskriv hva som har skjedd i saken/årsaken til revurderingen>",
                )
            }
            title1 {
                text(
                    Bokmal to "Slik uttaler du deg",
                    Nynorsk to "Slik uttaler du deg",
                    English to "How to make a statement",
                )
            }
            showIf(sakstype.equalTo(Sakstype.ALDER)) {
                paragraph {
                    text(
                        Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på $DIN_PENSJON_URL og velge “Kontakt Nav om pensjon”, " +
                                "eller logge deg inn på $BESKJED_TIL_NAV_URL og velge “Send beskjed til Nav”. Du kan også sende uttalelsen din til oss i posten. " +
                                "Adressen finner du på $ETTERSENDELSE_URL.",
                        Nynorsk to "Du kan sende uttalen din ved å logge deg inn på $DIN_PENSJON_URL og velje “Kontakt Nav om pensjon”, " +
                                "eller logge deg inn på $BESKJED_TIL_NAV_URL og velje “Send beskjed til Nav”. Du kan også sende uttalen din til oss i posten. " +
                                "Adressa finn du på $ETTERSENDELSE_URL.",
                        English to "You can submit your statement by logging in to your personal $DIN_PENSJON_URL  pension page and selecting “Kontakt Nav om pensjon”, " +
                                "or by logging in to $BESKJED_TIL_NAV_URL and selecting “Send beskjed til Nav”. You can also send us your statement by post. " +
                                "You can find the address at $ETTERSENDELSE_URL.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velge “Send beskjed til Nav”. " +
                                "Du kan også sende uttalelsen din til oss i posten. Adressen finner du på $ETTERSENDELSE_URL.",
                        Nynorsk to "Du kan sende uttalen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velje “Send beskjed til Nav”. " + "" +
                                "Du kan også sende uttalen din til oss i posten. Adressa finn du på $ETTERSENDELSE_URL.",
                        English to "You can submit your statement by logging in to [_Value URL_beskjedtilnav_] and selecting “Send beskjed til [_Value NAV_]”. You can also send us your statement by post. You can find the address at [_Value URL_ettersendelse_].",
                    )
                }
            }
            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}
