package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.BeloepEndring.ENDR_OKT
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AvdodSelectors.navn
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.inntektspensjon_safe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.BrukerSelectors.fodselsdato
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.GjenlevendetilleggKapittel19VedVirkSelectors.apKap19utenGJR
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.KravSelectors.kravInitiertAv
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.avdod
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.gjenlevendetilleggKapittel19VedVirk
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.YtelseskomponentInformasjonSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import java.time.Month

@TemplateModelHelpers
object VedtakEndringAvAlderspensjonGjenlevenderettigheter :
    RedigerbarTemplate<VedtakEndringAvAlderspensjonGjenlevenderettigheterDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_GJENLEVENDERETT
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAvAlderspensjonGjenlevenderettigheterDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vi har beregnet alderspensjonen din på nytt fra <dato>",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val virkDatoFomEtter2023 = pesysData.krav.virkDatoFom.greaterThanOrEqual(
            LocalDate.of(
                1,
                Month.JANUARY,
                2024
            )
        )
        val kravInitiertAvNav = pesysData.krav.kravInitiertAv.equalTo(KravInitiertAv.NAV)
        val brukerFoedtEtter1944 = pesysData.bruker.fodselsdato.greaterThanOrEqual(
            LocalDate.of(1944, Month.JANUARY, 1)
        )

        title {
            text(
                Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ",
                Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ",
                English to "We have recalculated your retirement pension from "
            )
            eval(pesysData.krav.virkDatoFom.format())
            showIf(
                virkDatoFomEtter2023 and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
            ) {
                text(
                    Bokmal to "Gjenlevendetillegg i alderspensjonen fra ",
                    Nynorsk to "Attlevandetillegg i alderspensjonen din frå ",
                    English to "Survivor's supplement in retirement pension from "
                )
                eval(pesysData.krav.virkDatoFom.format())
            }
        }

        outline {
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak",
                    English to "Decision"
                )
            }
            // innvilgetGjRettAP_001
            showIf(kravInitiertAvNav) {
                paragraph {
                    textExpr(
                        Bokmal to "Nav har mottatt melding om at ".expr() + pesysData.avdod.navn + " er død, og du har rettigheter etter avdøde.",
                        Nynorsk to "Nav har fått melding om at ".expr() + pesysData.avdod.navn + " er død, og du har rettar etter avdøde.",
                        English to "Nav has received notification that ".expr() + pesysData.avdod.navn + " has died, and you are entitled to rights as a surviving spouse."
                    )
                }
            }
            // nyBeregningGjtKap19Vedtak
            showIf(
                virkDatoFomEtter2023 and kravInitiertAvNav and brukerFoedtEtter1944
            ) {
                paragraph {
                    text(
                        Bokmal to "Fra 2024 blir gjenlevenderetten skilt ut fra alderspensjonen din som et eget tillegg.",
                        Nynorsk to "Frå 2024 blir attlevanderetten skild ut frå alderspensjonen som eit eige tillegg.",
                        English to "From 2024, the survivor’s right will be separated from your retirement pension as a separate supplement."
                    )
                }
            }
            // innvilgetGjRettAPAvdod_002
            showIf(pesysData.krav.kravInitiertAv.isOneOf(KravInitiertAv.VERGE, KravInitiertAv.BRUKER)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du har rettigheter etter ".expr() + pesysData.avdod.navn + ".",
                        Nynorsk to "Du har rettar etter ".expr() + pesysData.avdod.navn + ".",
                        English to "You are entitled to rights as the surviving spouse of ".expr() + pesysData.avdod.navn + "."
                    )
                }
            }

            // innvilgetGjRettAPIngenEndr_001
            showIf(
                not(virkDatoFomEtter2023) and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and pesysData.ytelseskomponentInformasjon.beloepEndring.notEqualTo(
                    ENDR_OKT
                )
            ) {
                paragraph {
                    val fritekst = fritekst("skriv inn aktuell ytelseskomponent")
                    textExpr(
                        Bokmal to "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening. Dette fører til en endring av ".expr() + fritekst + ", men vil ikke gi deg en høyere pensjon totalt enn den du har tjent opp selv.",
                        Nynorsk to "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening. Dette fører til ei endring av ".expr() + fritekst + ", men vil ikkje gi deg ein høgare pensjon enn kva du har tent opp sjølv.",
                        English to "We have therefore recalculated your pension on the basis of your and the deceased’s earned pension. This leads to a change in ".expr() + fritekst + ", but will not give you a higher pension than what you have accumulated yourself."
                    )
                }
            }

            // innvilgetGjRettAPIngenEndr2_001
            showIf(not(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt)) {
                paragraph {
                    text(
                        Bokmal to "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening, men du vil ikke få høyere alderspensjon enn den du har tjent opp selv.",
                        Nynorsk to "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening, men du vil ikkje få høgare alderspensjon enn kva du har tent opp sjølv.",
                        English to "We have made a calculation on the basis of your and the deceased’s earned pension and you will not be entitled to a higher retirement pension than what you have accumulated yourself."
                    )
                }
            }

            // innvilgetGjRettAPEndr_001
            showIf(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(ENDR_OKT)
            and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) and not(virkDatoFomEtter2023)) {
                paragraph {
                    text(
                        Bokmal to "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening, og det gir deg en høyere alderspensjon enn den du har tjent opp selv.",
                        Nynorsk to "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening, og det gir deg ein høgare alderspensjon enn kva du har tent opp sjølv.",
                        English to "We have therefore recalculated your pension on the basis of your and the deceased’s earned pension, and you are entitled to a higher retirement pension than what you have accumulated yourself."
                    )
                }
            }

            // nyBeregningGjtKap19Egen_001
            showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)
                and brukerFoedtEtter1944 and not(virkDatoFomEtter2023)
            ) {
                paragraph {
                    text(
                        Bokmal to "Fra januar 2024 er gjenlevenderett i alderspensjonen din skilt ut som et eget gjenlevendetillegg. Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                        Nynorsk to "Frå januar 2024 er attlevanderett i alderspensjonen din skild ut som eit eige attlevandetillegg. Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er differansen mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                        English to "From January 2024, the survivor’s right in your retirement pension is separated out as a separate survivor’s supplement. The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between the retirement pension based on your own pension earnings and earnings from the deceased, and the retirement pension you have earned yourself."
                    )
                }
            }

            // beregningAPGjtKap19_001
            showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr() + pesysData.avdod.navn + ".",
                        Nynorsk to "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr() + pesysData.avdod.navn + ".",
                        English to "You receive a survivor’s supplement in the retirement pension because you have pension rights after ".expr() + pesysData.avdod.navn + "."
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                        Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                        English to "The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself."
                    )
                }
            }

            // forklaringberegningGjtKap9_148_01
            showIf(kravInitiertAvNav and pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)
            and brukerFoedtEtter1944 and virkDatoFomEtter2023 and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                title2 {
                    text(
                        Bokmal to "Slik blir gjenlevendetillegget ditt beregnet",
                        Nynorsk to "Slik blir attlevandetillegget ditt rekna ut",
                        English to "This is how your survivor’s supplement is calculated"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening og opptjening fra den avdøde. Gjenlevendetillegget er fra 1. januar 2024 differansen mellom denne alderspensjonen og den alderspensjonen du har tjent opp selv.",
                        Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening og oppteninga frå den avdøde. Attlevandetillegget er frå 1. januar 2024 skilnaden mellom denne alderspensjonen og den alderspensjonen du har tent opp sjølv.",
                        English to "The retirement pension is based on your own pension earnings and the earnings from the deceased. The survivor’s supplement, from 1 January 2024, is the difference between this retirement pension and the retirement pension you have earned yourself."
                    )
                }
            }

            // forklaringberegningGjtKap19_148_10
            showIf(pesysData.alderspensjonVedVirk.uttaksgrad.greaterThan(0)
            and pesysData.alderspensjonVedVirk.uttaksgrad.lessThan(100)
                and kravInitiertAvNav
                and pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)
                and brukerFoedtEtter1944
                and virkDatoFomEtter2023
                and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får utbetalt ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon med gjenlevendetillegg.",
                        Nynorsk to "Du får utbetalt ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon med attlevandetillegg.",
                        English to "You receive".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension with survivor’s supplement."
                    )
                }
            }

            // forklaringberegningGjtKap19_148_11
            showIf(
                kravInitiertAvNav
                and pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)
                and brukerFoedtEtter1944
                and virkDatoFomEtter2023
                and not(pesysData.gjenlevendetilleggKapittel19VedVirk.apKap19utenGJR)
                and not(pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(false))
            ) {
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt alderspensjon etter egen opptjening fordi du har ingen eller lav pensjonsopptjening.",
                        Nynorsk to "Du får ikkje utbetalt alderspensjon etter eigen opptjening fordi du har ingen eller låg pensjonsopptjening.",
                        English to "You will not receive a retirement pension based on your own earnings because you have no or low pension earnings."
                    )
                }

            }
        }
    }
}