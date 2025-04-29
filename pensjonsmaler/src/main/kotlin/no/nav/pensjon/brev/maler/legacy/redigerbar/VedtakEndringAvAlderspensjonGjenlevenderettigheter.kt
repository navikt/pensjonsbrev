package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.BeloepEndring.ENDR_OKT
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.harEndretPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.AvdodSelectors.navn
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.gjenlevendetilleggKap19_safe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.gjenlevendetillegg_safe
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
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.opplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.endringIPensjonsutbetaling
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.gjenlevendetilleggTittel
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.omregnetTilEnsligISammeVedtak
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.pensjonenOeker
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.visTilleggspensjonavsnittAP1967
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.YtelseskomponentInformasjonSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.opplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.format
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
import no.nav.pensjon.brevbaker.api.model.Kroner
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
            displayTitle = "Vedtak - endring ved gjenlevenderett",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val virkDatoFomEtter2023 = pesysData.krav.virkDatoFom.greaterThanOrEqual(LocalDate.of(2024, Month.JANUARY, 1))
        val kravInitiertAvNav = pesysData.krav.kravInitiertAv.equalTo(KravInitiertAv.NAV)
        val brukerFoedtEtter1944 = pesysData.bruker.fodselsdato.greaterThanOrEqual(LocalDate.of(1944, Month.JANUARY, 1))

        title {
            showIf(virkDatoFomEtter2023 and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget and saksbehandlerValg.gjenlevendetilleggTittel) {
                text(
                    Bokmal to "Gjenlevendetillegg i alderspensjonen fra ",
                    Nynorsk to "Attlevandetillegg i alderspensjonen din frå ",
                    English to "Survivor's supplement in retirement pension from "
                )
            }.orShow {
                text(
                    Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ",
                    Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ",
                    English to "We have recalculated your retirement pension from "
                )
            }
            eval(pesysData.krav.virkDatoFom.format())
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
            showIf(virkDatoFomEtter2023 and kravInitiertAvNav and brukerFoedtEtter1944) {
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
            showIf(not(virkDatoFomEtter2023) and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and pesysData.ytelseskomponentInformasjon.beloepEndring.notEqualTo(ENDR_OKT)) {
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
            showIf(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(ENDR_OKT) and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) and not(virkDatoFomEtter2023)) {
                paragraph {
                    text(
                        Bokmal to "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening, og det gir deg en høyere alderspensjon enn den du har tjent opp selv.",
                        Nynorsk to "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening, og det gir deg ein høgare alderspensjon enn kva du har tent opp sjølv.",
                        English to "We have therefore recalculated your pension on the basis of your and the deceased’s earned pension, and you are entitled to a higher retirement pension than what you have accumulated yourself."
                    )
                }
            }

            // nyBeregningGjtKap19Egen_001
            showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016) and brukerFoedtEtter1944 and not(virkDatoFomEtter2023)) {
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


            showIf(kravInitiertAvNav
                    and pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)
                    and brukerFoedtEtter1944
                    and virkDatoFomEtter2023
            ) {
                // forklaringberegningGjtKap9_148_01
                showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
                ) {
                    title1 {
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

                    // forklaringberegningGjtKap19_148_10
                    showIf(
                        pesysData.alderspensjonVedVirk.uttaksgrad.greaterThan(0)
                                and pesysData.alderspensjonVedVirk.uttaksgrad.lessThan(100)
                    ) {
                        paragraph {
                            textExpr(
                                Bokmal to "Du får utbetalt ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon med gjenlevendetillegg.",
                                Nynorsk to "Du får utbetalt ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon med attlevandetillegg.",
                                English to "You receive".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension with survivor’s supplement."
                            )
                        }
                    }
                }

                showIf(pesysData.gjenlevendetilleggKapittel19VedVirk.apKap19utenGJR.equalTo(0)) {
                    // forklaringberegningGjtKap19_148_11
                    showIf(pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(0).equalTo(0)) {
                        paragraph {
                            text(
                                Bokmal to "Du får ikke utbetalt alderspensjon etter egen opptjening fordi du har ingen eller lav pensjonsopptjening.",
                                Nynorsk to "Du får ikkje utbetalt alderspensjon etter eigen opptjening fordi du har ingen eller låg pensjonsopptjening.",
                                English to "You will not receive a retirement pension based on your own earnings because you have no or low pension earnings."
                            )
                        }
                    }.orShowIf(pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(0).greaterThan(0)) {
                        // forklaringberegningGjtKap19_148_12
                        paragraph {
                            text(
                                Bokmal to "Du får ikke utbetalt alderspensjon etter egen opptjening i den delen som beregnes etter gamle regler fordi du har ingen eller lav pensjonsopptjening.",
                                Nynorsk to "Du får ikkje utbetalt alderspensjon etter eigen opptjening i den delen som blir rekna etter gamle reglar fordi du har ingen eller låg pensjonsopptjening.",
                                English to "You will not receive a retirement pension based on your own earnings in the part that is calculated according to old rules because you have no or low pension earnings."
                            )
                        }

                    }
                }

                // forklaringberegningGjtKap19_148_13
                showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                    paragraph {
                        text(
                            Bokmal to "Dette gjenlevendetillegget skal ikke lenger reguleres når pensjonene øker 1. mai hvert år.",
                            Nynorsk to "Dette attlevandetillegget skal ikkje lenger regulerast når pensjonane aukar 1. mai kvart år.",
                            English to "This survivor’s supplement will no longer be adjusted when pensions increase from 1 May each year."
                        )
                        showIf(
                            pesysData.gjenlevendetilleggKapittel19VedVirk.apKap19utenGJR.greaterThan(0)
                                    and pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(0)
                                .greaterThan(0)
                        ) {
                            text(
                                Bokmal to " Alderspensjonen som er basert på din egen opptjening, blir fortsatt regulert 1. mai hvert år.",
                                Nynorsk to " Alderspensjonen som er basert på di eiga opptening, blir framleis regulert 1. mai kvart år.",
                                English to " The retirement pension based on your own earnings will continue to be adjusted from 1 May each year."
                            )
                        }
                    }
                }


                // referansebeløpGjtKap19ErNull_001
                showIf(not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget)) {
                    title1 {
                        text(
                            Bokmal to "Hvorfor blir ikke gjenlevendetillegget ditt utbetalt?",
                            Nynorsk to "Kvifor blir ikkje attlevandetillegget ditt utbetalt?",
                            English to "Why is your survivor’s supplement not being paid out?"
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening og opptjening fra den avdøde. Gjenlevendetillegget er differansen mellom denne alderspensjonen og den alderspensjonen du har tjent opp selv. Din samlede alderspensjon basert på egen pensjonsopptjening og alderspensjon med opptjening fra den avdøde blir samme beløp. Derfor blir ikke gjenlevendetillegget utbetalt.",
                            Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening og opptening frå den avdøde. Attlevandetillegget er differansen mellom denne alderspensjonen og den alderspensjonen du har tent opp sjølv. Den samla alderspensjon basert på di eiga pensjonsopptening og alderspensjon med opptening frå den avdøde blir same beløp. Difor blir ikkje attlevandetillegget utbetalt.",
                            English to "The retirement pension is based on your own pension earnings and the earnings from the deceased. The survivor’s supplement is the difference between this retirement pension and the retirement pension you have earned yourself. Your total retirement pension, based on your own pension earnings and the retirement pension with earnings from the deceased, becomes the same amount. Therefore, the survivor’s supplement is not paid out."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Alderspensjonen som nå er basert på din egen opptjening, blir fortsatt regulert 1. mai hvert år.",
                            Nynorsk to "Alderspensjonen som no er basert på di eiga opptening, blir framleis regulert 1. mai kvart år.",
                            English to "The retirement pension, which is now based on your own earnings, continues to be adjusted from 1 May each year."
                        )
                    }
                }
            }

            // forklaringutfasingGjtKap20_001
            showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget) {
                title1 {
                    text(
                        Bokmal to "Gjenlevendetillegget etter nye regler blir faset ut",
                        Nynorsk to "Attlevandetillegget etter nye reglar blir fasa ut",
                        English to "The survivor’s supplement according to the new rules is being phased out"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Fra 2024 blir dette tillegget redusert med samme beløp som alderspensjonen din øker ved den årlige reguleringen. Tillegget vil dermed bli lavere, og etter hvert opphøre.",
                        Nynorsk to "Frå 2024 blir dette tillegget redusert med same beløp som alderspensjonen din aukar ved den årlege reguleringa. Tillegget vil dermed bli lågare, og etter kvart bli borte.",
                        English to "From 2024, this supplement will be reduced by the same amount as your retirement pension increases with the annual adjustment. The supplement will thus become lower, and eventually cease."
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du tar ut mindre enn 100 prosent alderspensjon, har det betydning for gjenlevendetillegget ditt. Hvis du øker uttaksgraden senere, vil ikke gjenlevendetillegget øke.",
                        Nynorsk to "Viss du tar ut mindre enn 100 prosent alderspensjon, verkar det inn på attlevandetillegget ditt. Viss du aukar uttaksgraden seinare, vil ikkje attlevandetillegget auke.",
                        English to "If you take out less than 100 percent retirement pension, it will affect your survivor’s supplement. If you increase the withdrawal rate later, the survivor’s supplement will not increase."
                    )
                }
            }

            // omregnetTPAvdod_001
            showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP1967) and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and saksbehandlerValg.visTilleggspensjonavsnittAP1967) {
                paragraph {
                    text(
                        Bokmal to "Tilleggspensjonen til en gjenlevende alderspensjonist kan enten bestå av pensjonistens egen tilleggspensjon eller 55 prosent av summen av pensjonistens egen tilleggspensjon og den avdødes tilleggspensjon. Tilleggspensjonen din er gitt etter det siste alternativet, da dette gir det høyeste beløpet for deg.",
                        Nynorsk to "Tilleggspensjonen til ein attlevande alderspensjonist kan anten bestå av pensjonistens eigen tilleggspensjon eller 55 prosent av summen av pensjonistens eigen tilleggspensjon og tilleggspensjonen til den avdøde. Tilleggspensjonen din er gitt etter det siste alternativet då det gir det høgaste beløpet for deg.",
                        English to "The supplementary pension for a widowed old age pensioner can be calculated either as the pensioner's own supplementary pension or as 55 percent of the sum of the pensioner's own supplementary pension and the deceased's supplementary pension. Your supplementary pension has been calculated using the latter method, as this is more beneficial for you."
                    )
                }
            }

            // omregnetEnsligAP_001
            showIf(saksbehandlerValg.omregnetTilEnsligISammeVedtak) {
                paragraph {
                    text(
                        Bokmal to "I tillegg har vi regnet om pensjonen din fordi du har blitt enslig pensjonist.",
                        Nynorsk to "I tillegg har vi rekna om pensjonen din fordi du har blitt einsleg pensjonist.",
                        English to "We have also recalculated your pension because you have become a single pensioner."
                    )
                }
            }

            showIf(saksbehandlerValg.pensjonenOeker) {
                // nyBeregningAPØkning_001
                paragraph {
                    text(
                        Bokmal to "Dette fører til at pensjonen din øker.",
                        Nynorsk to "Dette fører til at pensjonen din aukar.",
                        English to "This leads to an increase in your retirement pension."
                    )
                }
            }.orShow {
                // ingenEndringBelop_002
                paragraph {
                    text(
                        Bokmal to "Dette får derfor ingen betydning for utbetalingen din.",
                        Nynorsk to "Dette får derfor ingen følgjer for utbetalinga di.",
                        English to "Therefore, this does not affect the amount you will receive."
                    )
                }
            }

            // beløpAP_001
            showIf(
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)
                        and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget)
                        and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget)
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon fra folketrygden hver måned før skatt.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon frå folketrygda kvar månad før skatt.",
                        English to "You will receive ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax as retirement pension through the National Insurance Act."
                    )
                }
            }

            // beloepApOgGjtvedVirk_001
            showIf(
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)
                        and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
                        and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget)
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt. Av dette er gjenlevendetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        ).format() + ".",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt. Av dette er attlevandetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        ).format() + ".",
                        English to "You receive ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " in retirement pension and survivor’s supplement from the National Insurance Scheme every month before tax. Of this, the survivor’s supplement is " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        ).format() + ".",
                    )
                }
            }

            // beloepApOgGjtKap19OgKap20vedVirk_001
            showIf(
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)
                        and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
                        and pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt. Av dette er gjenlevendetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        )
                            .format() + " etter gamle regler og " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(
                            Kroner(0)
                        ).format() + " etter nye regler.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt. Av dette er attlevandetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        )
                            .format() + " etter gamle reglar og " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(
                            Kroner(0)
                        ).format() + " etter nye reglar.",
                        English to "You receive ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner in retirement pension and survivor’s supplement from the National Insurance every month before tax. Of this, the survivor’s supplement is " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        )
                            .format() + " according to old rules and " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(
                            Kroner(0)
                        ).format() + " according to new rules.",
                    )
                }
            }

            // utbetalingsInfoMndUtbet_001
            showIf(
                pesysData.beregnetPensjonPerManedVedVirk.antallBeregningsperioderPensjon.greaterThan(0)
                        and pesysData.alderspensjonVedVirk.uttaksgrad.greaterThan(0)
            ) {
                paragraph {
                    text(
                        Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL.",
                        Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL.",
                        English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."
                    )
                }
            }

            // flereBeregningsperioderVedlegg_001
            showIf(
                pesysData.beregnetPensjonPerManedVedVirk.antallBeregningsperioderPensjon.greaterThan(1)
                        and pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)
            ) {
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du kan lese meir om andre berekningsperiodar i vedlegget.",
                        English to "There is more information about other calculation periods in the attachment."
                    )
                }
            }


            showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967, AP2011, AP2016)) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ ",
                        English to "This decision was made pursuant to the provisions of §§ "
                    )

                    // gjRettAP1967STHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP1967)
                                and pesysData.alderspensjonVedVirk.saertilleggInnvilget
                                and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)
                    ) {
                        text(
                            Bokmal to "3-2, 3-3, 19-8, 19-16 og 22-12",
                            Nynorsk to "3-2, 3-3, 19-8, 19-16 og 22-12",
                            English to "3-2, 3-3, 19-8, 19-16 and 22-12 of the National Insurance Act"
                        )
                    }

                    // gjRettAP1967IngenSTHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967)
                                and not(pesysData.alderspensjonVedVirk.saertilleggInnvilget)
                                and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)
                    ) {
                        text(
                            Bokmal to "3-2, 19-8, 19-16 og 22-12",
                            Nynorsk to "3-2, 19-8, 19-16 og 22-12",
                            English to "3-2, 19-8, 19-16 and 22-12 of the National Insurance Act"
                        )
                    }

                    // gjRettAPMNTHjemmel1967
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967)
                                and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget
                                and not(pesysData.alderspensjonVedVirk.saertilleggInnvilget)
                    ) {
                        text(
                            Bokmal to "3-2, 19-8, 19-14, 19-16 og 22-12",
                            Nynorsk to "3-2, 19-8, 19-14, 19-16 og 22-12",
                            English to "3-2, 19-8, 19-14, 19-16 and 22-12 of the National Insurance Act"
                        )
                    }

                    // gjRettAP1967MNTHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967)
                                and pesysData.alderspensjonVedVirk.saertilleggInnvilget
                                and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget
                    ) {
                        text(
                            Bokmal to "3-2, 3-3, 19-8, 19-14, 19-16 og 22-12",
                            Nynorsk to "3-2, 3-3, 19-8, 19-14, 19-16 og 22-12",
                            English to "3-2, 3-3, 19-8, 19-14, 19-16 and 22-12 of the National Insurance Act"
                        )
                    }

                    // gjRettAP2011Hjemmel_001
                    showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2011) and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                Bokmal to "3-2, 19-8, 19-9, 19-16 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-9, 19-16 og 22-12",
                                English to "3-2, 19-8, 19-9, 19-16 and 22-12"
                            )
                        }.orShow {
                            text(
                                Bokmal to "3-2, 19-8, 19-16 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-16 og 22-12",
                                English to "3-2, 19-8, 19-16 and 22-12"
                            )
                        }
                    }

                    // gjRettAP2011MNTHjemmel_001
                    showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2011) and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                Bokmal to "3-2, 19-8, 19-9, 19-14, 19-16 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-9, 19-14, 19-16 og 22-12",
                                English to "3-2, 19-8, 19-9, 19-14, 19-16 and 22-12"
                            )
                        }.orShow {
                            text(
                                Bokmal to "3-2, 19-8, 19-14, 19-16 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-14, 19-16 og 22-12",
                                English to "3-2, 19-8, 19-14, 19-16 and 22-12"
                            )
                        }
                    }

                    //  gjRettAP2016Hjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2016)
                                and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)
                                and not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget)
                    ) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                Bokmal to "3-2, 19-8, 19-9, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-9, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                English to "3-2, 19-8, 19-9, 19-15, 19-16, 20-18, 20-19 and 22-12"
                            )
                        }.orShow {
                            text(
                                Bokmal to "3-2, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                English to "3-2, 19-15, 19-16, 20-18, 20-19 and 22-12"
                            )
                        }
                    }

                    // gjRettAP2016MNTGarantiPensjonHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2016)
                                and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget
                                and pesysData.alderspensjonVedVirk.garantipensjonInnvilget
                    )
                    {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                Bokmal to "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12",
                                English to "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 and 22-12",
                            )
                        }.orShow {
                            text(
                                Bokmal to "3-2, 19-8, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12",
                                English to "3-2, 19-8, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 and 22-12",
                            )
                        }
                    }

                    // gjRettAP2016GarantiPensjonHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2016)
                                and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)
                                and pesysData.alderspensjonVedVirk.garantipensjonInnvilget
                    ) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                Bokmal to "3-2, 19-8, 19-9, 19-15, 19-16, 20-9, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-9, 19-15, 19-16, 20-9, 20-19 og 22-12",
                                English to "3-2, 19-8, 19-9, 19-15, 19-16, 20-9, 20-19 and 22-12",
                            )
                        }.orShow {
                            text(
                                Bokmal to "3-2, 19-15, 19-16, 20-9, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-15, 19-16, 20-9, 20-19 og 22-12",
                                English to "3-2, 19-15, 19-16, 20-9, 20-19 and 22-12",
                            )
                        }
                    }

                    // gjRettAP2016MNTHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2016)
                                and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget
                                and not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget)
                    ) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                Bokmal to "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                English to "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-18, 20-19 and 22-12",
                            )
                        }.orShow {
                            text(
                                Bokmal to "3-2, 19-8, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                Nynorsk to "3-2, 19-8, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12",
                                English to "3-2, 19-8, 19-14, 19-15, 19-16, 20-18, 20-19 and 22-12",
                            )
                        }
                    }

                    text(
                        Bokmal to ".", Nynorsk to ".", English to "."
                    )
                }
            }

            // innvilgetGjTilleggKap20_001
            showIf(pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(Kroner(0)).greaterThan(0)) {
                paragraph {
                    text(
                        Bokmal to "Du er også innvilget gjenlevendetillegg etter regler i kapittel 20 i folketrygdloven.",
                        Nynorsk to "Du får også eit attlevandetillegg etter reglar i kapittel 20 i folketrygdlova.",
                        English to "You are also granted a survivor’s supplement pursuant to the provisions of Chapter 20 of the National Insurance Act."
                    )
                }
            }

            // innvilgetGjRettKap19_001
            showIf(brukerFoedtEtter1944 and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                paragraph {
                    text(
                        Bokmal to "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024.",
                        Nynorsk to "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024.",
                        English to "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024."
                    )
                }
            }

            // infoAPopptjRedusPoengUnder67Aar_001
            showIf(
                pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
                        and saksbehandlerValg.brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar
            ) {
                paragraph {
                    text(
                        Bokmal to "Fram til året du fyller 67 år kan du øke pensjonsopptjeningen din ved å bo i Norge.",
                        Nynorsk to "Fram til året du fyller 67 år, kan du auke pensjonsoppteninga di ved å bu i Noreg.",
                        English to "You can increase your earned pension until the year you turn 67 by living in Norway."
                    )
                }
            }


            // infoAPopptjRedusPoengOver67Aar_001
            showIf(
                pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
                        and saksbehandlerValg.brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar
            ) {
                paragraph {
                    text(
                        Bokmal to "Fra du er 67 til 75 år, kan pensjonsopptjeningen din øke dersom den årlige inntekten din er høyere enn folketrygdens grunnbeløp. I vedleggene finner du mer detaljerte opplysninger.",
                        Nynorsk to "Frå du er 67 til 75 år, kan pensjonsoppteninga di auke dersom den årlege inntekta din er høgare enn grunnbeløpet i folketrygda. I vedlegga finn du meir detaljerte opplysningar.",
                        English to "Your earned pension can increase from when you are 67 until 75 years of age if your annual income is higher than the National Insurance basic amount. You will find more detailed information in the attachments."
                    )
                }
            }

            // skattAPendring_001
            showIf(pesysData.alderspensjonVedVirk.harEndretPensjon and saksbehandlerValg.endringIPensjonsutbetaling) {
                title1 {
                    text(
                        Bokmal to "Endring av alderspensjon kan ha betydning for skatt",
                        Nynorsk to "Endring av alderspensjon kan ha betyding for skatt",
                        English to "The change in your retirement pension may affect how much tax you pay"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endret. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister.",
                        Nynorsk to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endra. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar.",
                        English to "When your retirement pension has been changed, you should check if your tax deduction card is correctly calculated. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners."
                    )
                }
                paragraph {
                    text(
                        Bokmal to "På $DIN_PENSJON_URL får du vite hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket vil dette gjelde fra måneden etter at vi har fått beskjed.",
                        Nynorsk to "På $DIN_PENSJON_URL får du vite kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det. Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed.",
                        English to "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change."
                    )
                }
            }

            // pensjonFraAndreOverskrift_001
            title1 {
                text(
                    Bokmal to "Andre pensjonsordninger",
                    Nynorsk to "Andre pensjonsordningar",
                    English to "Other pension schemes"
                )
            }

            // infoAvdodPenFraAndre_001
            paragraph {
                text(
                    Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                    Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                    English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company."
                )
            }

            // etterbetalingAP_002
            showIf(saksbehandlerValg.etterbetaling) {
                title1 {
                    text(
                        Bokmal to "Etterbetaling",
                        Nynorsk to "Etterbetaling",
                        English to "Retroactive payment"
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du får etterbetalt pensjon fra ".expr() + pesysData.krav.virkDatoFom.format() + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV.",
                        Nynorsk to "Du får etterbetalt pensjon frå ".expr() + pesysData.krav.virkDatoFom.format() + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Vi kan trekke frå skatt og ytingar du har fått frå for eksempel Nav eller tenestepensjonsordningar. Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV.",
                        English to "You will receive retroactive pension payments from ".expr() + pesysData.krav.virkDatoFom.format() + ". The retroactive payments will normally be made in the course of seven working days. We can make deductions for tax and benefits you have received, for example, from Nav or occupational pension schemes. Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. You can check if there are any deductions from the payment notice at $DITT_NAV."
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser.",
                        Nynorsk to "Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten.",
                        English to "If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates."
                    )
                }
            }

            // infoAPOverskrift_001
            title1 {
                text(
                    Bokmal to "Hvor kan du få vite mer om alderspensjonen din?",
                    Nynorsk to "Kvar kan du få vite meir om alderspensjonen din?",
                    English to "Where can you find out more about your retirement pension?"
                )
            }
            // infoAP_001
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon om hvordan alderspensjon er satt sammen og oversikter over grunnbeløp og aktuelle satser på $ALDERSPENSJON.",
                    Nynorsk to "Du finn meir informasjon om korleis alderspensjonen er sett saman, og oversikter over grunnbeløp og aktuelle satsar på $ALDERSPENSJON.",
                    English to "There is more information on how retirement pension is calculated, with overviews of basic amounts and relevant rates, at $ALDERSPENSJON."
                )
            }
            paragraph {
                text(
                    Bokmal to "Informasjon om utbetalingene dine finner du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                    Nynorsk to "Informasjon om utbetalingane dine finn du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                    English to "You can find more detailed information on what you will receive at $DITT_NAV. Here you can also change your bank account number."
                )
            }

            // meldEndringerPesysGjenlevende_001
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must report changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du mister gjenlevenderetten hvis du gifter deg eller du flytter sammen med en du har barn med eller tidligere har vært gift med.",
                    Nynorsk to "Du mistar attlevandetillegget om du giftar deg eller du flyttar saman med ein du har barn med eller tidlegare har vore gift med.",
                    English to "You lose the survivor's right if you marry, or you move in with someone you have children with or have previously been married to."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du planlegger opphold i et annet land, kan det påvirke utbetalingen din. Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Viss du får planlegg opphald i eit anna land, kan det påverke utbetalinga di. Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om.",
                    English to "If you are planning to stay in another country, it may affect your benefit payment. If there are changes, you must notify us immediately. The appendix specifies which changes you are obligated to notify us of."
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggOrienteringOmRettigheterOgPlikter,
            pesysData.orienteringOmRettigheterOgPlikterDto
        ) // v2
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto) // v3
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025Dto) // v10
        includeAttachmentIfNotNull(opplysningerOmAvdoedBruktIBeregning, pesysData.opplysningerOmAvdoedBruktIBeregningDto) // v6
    }
}