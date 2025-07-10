package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.KravInitiertAv.BRUKER
import no.nav.pensjon.brev.api.model.KravInitiertAv.NAV
import no.nav.pensjon.brev.api.model.KravInitiertAv.VERGE
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.KravSelectors.kravInitiertAv
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagPaaGjenlevenderettIAlderspensjon : RedigerbarTemplate<AvslagPaaGjenlevenderettIAlderspensjonDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_GJENLEVENDERETT
    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagPaaGjenlevenderettIAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag paa ",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            // avslagGjRettAPTittel_001
            showIf(
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0) and pesysData.krav.kravInitiertAv.isOneOf(
                    BRUKER,
                    VERGE
                )
            ) {
                text(
                    Bokmal to "Vi har avslått søknaden din om gjenlevenderett i alderspensjonen",
                    Nynorsk to "Vi har avslått søknaden din om attlevanderett i alderspensjonen",
                    English to "We have declined your application for survivor's rights in your retirement pension"
                )
            }.orShowIf(
                // avslagGjRettAPTittel_002
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0) and pesysData.krav.kravInitiertAv.isOneOf(
                    NAV
                )
            ) {
                text(
                    Bokmal to "Vi har vurdert om du har pensjonsrettigheter etter avdøde",
                    Nynorsk to "Vi har vurdert om du har pensjonsrettar etter avdøde",
                    English to "We have assessed whether you have survivor’s rights in your retirement pension"
                )
            }.orShowIf(pesysData.alderspensjonVedVirk.totalPensjon.equalTo(0)) {
                // avslagAPGjRettTittel_001
                text(
                    Bokmal to "Vi har avslått søknaden din om alderspensjon med gjenlevenderett",
                    Nynorsk to "Vi har avslått søknaden din om alderspensjon med attlevanderett",
                    English to "We have declined your application for  retirement pension with survivor’s rights"
                )
            }
        }
        outline {
            includePhrase(Vedtak.Overskrift)
            showIf(pesysData.krav.kravInitiertAv.equalTo(NAV)) {
                // avslagGjRettAPAvdod_001
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått beskjed om at ".expr() + pesysData.avdoed.navn + " døde " + fritekst("dato") + ".",
                        Nynorsk to "Vi har fått beskjed om at ".expr() + pesysData.avdoed.navn + " døydde " + fritekst("dato") + ".",
                        English to "We have received notice that ".expr() + pesysData.avdoed.navn + " died " + fritekst(
                            "dato"
                        ) + "."
                    )
                }

                // TODO: Her kjem alle Under x års medlemstid-blokkene, som har returnverdi 1 i doksys
                // Må finne ut av korleis vi handterer dei
                // Heilt fram til og med Under 20 år-blokkene

                showIf(pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE)) {
                    // avslagGJRettAPGiftUnder5aarSøknad_001
                    paragraph {
                        text(
                            Bokmal to "For å ha rettigheter som gift må du og avdøde ha vært gift i minst fem år eller ha felles barn.",
                            Nynorsk to "For å ha rettar som gift må du og avdøde ha vore gifte i minst fem år eller ha felles barn.",
                            English to "To have rights as a married person, you and the deceased must have been married for at least five years or have joint children."
                        )
                    }
                    paragraph {
                        text(
                            
                        )
                    }
                }

            }
        }
    }
}