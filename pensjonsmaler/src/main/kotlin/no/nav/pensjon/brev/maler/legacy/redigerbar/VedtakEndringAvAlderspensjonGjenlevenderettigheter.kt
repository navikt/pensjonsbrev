package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.BeloepEndring.ENDR_OKT
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDto
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
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.KravSelectors.etterbetaling
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
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.SaksbehandlerValgSelectors.omregnetTilEnsligISammeVedtak
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.YtelseskomponentInformasjonSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvAlderspensjonGjenlevenderettigheterDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BeregnaPaaNytt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.DuFaarHverMaaned
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FlereBeregningsperioder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerOmAvdoedBruktIBeregning
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
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import java.time.Month

// 00126 i doksys
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
            showIf(virkDatoFomEtter2023 and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                text(
                    bokmal { + "Gjenlevendetillegg i alderspensjonen fra " },
                    nynorsk { + "Attlevandetillegg i alderspensjonen din frå " },
                    english { + "Survivor's supplement in retirement pension from " }
                )
                eval(pesysData.krav.virkDatoFom.format())
            }.orShow {
                includePhrase(BeregnaPaaNytt(pesysData.krav.virkDatoFom))
            }
        }

        outline {
            title1 {
                text(
                    bokmal { + "Vedtak" },
                    nynorsk { + "Vedtak" },
                    english { + "Decision" }
                )
            }
            // innvilgetGjRettAP_001
            showIf(kravInitiertAvNav) {
                paragraph {
                    text(
                        bokmal { + "Nav har mottatt melding om at " + pesysData.avdod.navn + " er død, og du har rettigheter etter avdøde." },
                        nynorsk { + "Nav har fått melding om at " + pesysData.avdod.navn + " er død, og du har rettar etter avdøde." },
                        english { + "Nav has received notification that " + pesysData.avdod.navn + " has died, and you are entitled to rights as a surviving spouse." }
                    )
                }
            }
            // nyBeregningGjtKap19Vedtak
            showIf(virkDatoFomEtter2023 and kravInitiertAvNav and brukerFoedtEtter1944) {
                paragraph {
                    text(
                        bokmal { + "Fra 2024 blir gjenlevenderetten skilt ut fra alderspensjonen din som et eget tillegg." },
                        nynorsk { + "Frå 2024 blir attlevanderetten skild ut frå alderspensjonen som eit eige tillegg." },
                        english { + "From 2024, the survivor’s right will be separated from your retirement pension as a separate supplement." }
                    )
                }
            }
            // innvilgetGjRettAPAvdod_002
            showIf(pesysData.krav.kravInitiertAv.isOneOf(KravInitiertAv.VERGE, KravInitiertAv.BRUKER)) {
                paragraph {
                    text(
                        bokmal { + "Du har rettigheter etter " + pesysData.avdod.navn + "." },
                        nynorsk { + "Du har rettar etter " + pesysData.avdod.navn + "." },
                        english { + "You are entitled to rights as the surviving spouse of " + pesysData.avdod.navn + "." }
                    )
                }
            }

            // innvilgetGjRettAPIngenEndr_001
            showIf(not(virkDatoFomEtter2023) and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and pesysData.ytelseskomponentInformasjon.beloepEndring.notEqualTo(ENDR_OKT)) {
                paragraph {
                    val fritekst = fritekst("skriv inn aktuell ytelseskomponent")
                    text(
                        bokmal { + "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening. Dette fører til en endring av " + fritekst + ", men vil ikke gi deg en høyere pensjon totalt enn den du har tjent opp selv." },
                        nynorsk { + "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening. Dette fører til ei endring av " + fritekst + ", men vil ikkje gi deg ein høgare pensjon enn kva du har tent opp sjølv." },
                        english { + "We have therefore recalculated your pension on the basis of your and the deceased’s earned pension. This leads to a change in " + fritekst + ", but will not give you a higher pension than what you have accumulated yourself." }
                    )
                }
            }

            // innvilgetGjRettAPIngenEndr2_001
            showIf(not(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt)) {
                paragraph {
                    text(
                        bokmal { + "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening, men du vil ikke få høyere alderspensjon enn den du har tjent opp selv." },
                        nynorsk { + "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening, men du vil ikkje få høgare alderspensjon enn kva du har tent opp sjølv." },
                        english { + "We have made a calculation on the basis of your and the deceased’s earned pension and you will not be entitled to a higher retirement pension than what you have accumulated yourself." }
                    )
                }
            }

            // innvilgetGjRettAPEndr_001
            showIf(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt and pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(ENDR_OKT) and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) and not(virkDatoFomEtter2023)) {
                paragraph {
                    text(
                        bokmal { + "Vi har derfor beregnet pensjonen din på nytt ut fra din egen og avdødes pensjonsopptjening, og det gir deg en høyere alderspensjon enn den du har tjent opp selv." },
                        nynorsk { + "Vi har difor berekna pensjonen din på nytt ut frå eigen og avdøde sin pensjonsopptening, og det gir deg ein høgare alderspensjon enn kva du har tent opp sjølv." },
                        english { + "We have therefore recalculated your pension on the basis of your and the deceased’s earned pension, and you are entitled to a higher retirement pension than what you have accumulated yourself." }
                    )
                }
            }

            // nyBeregningGjtKap19Egen_001
            showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016) and brukerFoedtEtter1944 and not(virkDatoFomEtter2023)) {
                paragraph {
                    text(
                        bokmal { + "Fra januar 2024 er gjenlevenderett i alderspensjonen din skilt ut som et eget gjenlevendetillegg. Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv." },
                        nynorsk { + "Frå januar 2024 er attlevanderett i alderspensjonen din skild ut som eit eige attlevandetillegg. Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er differansen mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv." },
                        english { + "From January 2024, the survivor’s right in your retirement pension is separated out as a separate survivor’s supplement. The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between the retirement pension based on your own pension earnings and earnings from the deceased, and the retirement pension you have earned yourself." }
                    )
                }
            }

            // beregningAPGjtKap19_001
            showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                paragraph {
                    text(
                        bokmal { + "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter " + pesysData.avdod.navn + "." },
                        nynorsk { + "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter " + pesysData.avdod.navn + "." },
                        english { + "You receive a survivor’s supplement in the retirement pension because you have pension rights after " + pesysData.avdod.navn + "." }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv." },
                        nynorsk { + "Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv." },
                        english { + "The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself." }
                    )
                }
            }


            showIf(kravInitiertAvNav
                    and pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)
                    and brukerFoedtEtter1944
                    and virkDatoFomEtter2023
            ) {
                // forklaringberegningGjtKap9_148_01
                showIf(
                    pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
                ) {
                    title1 {
                        text(
                            bokmal { + "Slik blir gjenlevendetillegget ditt beregnet" },
                            nynorsk { + "Slik blir attlevandetillegget ditt rekna ut" },
                            english { + "This is how your survivor’s supplement is calculated" }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Alderspensjonen er basert på din egen pensjonsopptjening og opptjening fra den avdøde. Gjenlevendetillegget er fra 1. januar 2024 differansen mellom denne alderspensjonen og den alderspensjonen du har tjent opp selv." },
                            nynorsk { + "Alderspensjonen er basert på di eiga pensjonsopptening og oppteninga frå den avdøde. Attlevandetillegget er frå 1. januar 2024 skilnaden mellom denne alderspensjonen og den alderspensjonen du har tent opp sjølv." },
                            english { + "The retirement pension is based on your own pension earnings and the earnings from the deceased. The survivor’s supplement, from 1 January 2024, is the difference between this retirement pension and the retirement pension you have earned yourself." }
                        )
                    }

                    // forklaringberegningGjtKap19_148_10
                    showIf(
                        pesysData.alderspensjonVedVirk.uttaksgrad.greaterThan(0)
                                and pesysData.alderspensjonVedVirk.uttaksgrad.lessThan(100)
                    ) {
                        paragraph {
                            text(
                                bokmal { +"Du får utbetalt " + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon med gjenlevendetillegg." },
                                nynorsk { +"Du får utbetalt " + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon med attlevandetillegg." },
                                english { +"You receive " + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension with survivor’s supplement." },
                            )
                        }
                    }
                }

                showIf(pesysData.gjenlevendetilleggKapittel19VedVirk.apKap19utenGJR.equalTo(0)) {
                    // forklaringberegningGjtKap19_148_11
                    showIf(pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(Kroner(0)).equalTo(0)) {
                        paragraph {
                            text(
                                bokmal { + "Du får ikke utbetalt alderspensjon etter egen opptjening fordi du har ingen eller lav pensjonsopptjening." },
                                nynorsk { + "Du får ikkje utbetalt alderspensjon etter eigen opptjening fordi du har ingen eller låg pensjonsopptjening." },
                                english { + "You will not receive a retirement pension based on your own earnings because you have no or low pension earnings." }
                            )
                        }
                    }.orShowIf(pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(Kroner(0)).greaterThan(0)) {
                        // forklaringberegningGjtKap19_148_12
                        paragraph {
                            text(
                                bokmal { + "Du får ikke utbetalt alderspensjon etter egen opptjening i den delen som beregnes etter gamle regler fordi du har ingen eller lav pensjonsopptjening." },
                                nynorsk { + "Du får ikkje utbetalt alderspensjon etter eigen opptjening i den delen som blir rekna etter gamle reglar fordi du har ingen eller låg pensjonsopptjening." },
                                english { + "You will not receive a retirement pension based on your own earnings in the part that is calculated according to old rules because you have no or low pension earnings." }
                            )
                        }

                    }
                }

                // forklaringberegningGjtKap19_148_13
                showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                    paragraph {
                        text(
                            bokmal { + "Dette gjenlevendetillegget skal ikke lenger reguleres når pensjonene øker 1. mai hvert år." },
                            nynorsk { + "Dette attlevandetillegget skal ikkje lenger regulerast når pensjonane aukar 1. mai kvart år." },
                            english { + "This survivor’s supplement will no longer be adjusted when pensions increase from 1 May each year." }
                        )
                        showIf(
                            pesysData.gjenlevendetilleggKapittel19VedVirk.apKap19utenGJR.greaterThan(0)
                                    and pesysData.beregnetPensjonPerManedVedVirk.inntektspensjon_safe.ifNull(Kroner(0))
                                .greaterThan(0)
                        ) {
                            text(
                                bokmal { + " Alderspensjonen som er basert på din egen opptjening, blir fortsatt regulert 1. mai hvert år." },
                                nynorsk { + " Alderspensjonen som er basert på di eiga opptening, blir framleis regulert 1. mai kvart år." },
                                english { + " The retirement pension based on your own earnings will continue to be adjusted from 1 May each year." }
                            )
                        }
                    }
                }


                    // referansebeløpGjtKap19ErNull_001
                    showIf(not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget)) {
                        title1 {
                            text(
                                bokmal { + "Hvorfor blir ikke gjenlevendetillegget ditt utbetalt?" },
                                nynorsk { + "Kvifor blir ikkje attlevandetillegget ditt utbetalt?" },
                                english { + "Why is your survivor’s supplement not being paid out?" }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { + "Alderspensjonen er basert på din egen pensjonsopptjening og opptjening fra den avdøde. Gjenlevendetillegget er differansen mellom denne alderspensjonen og den alderspensjonen du har tjent opp selv. Din samlede alderspensjon basert på egen pensjonsopptjening og alderspensjon med opptjening fra den avdøde blir samme beløp. Derfor blir ikke gjenlevendetillegget utbetalt." },
                                nynorsk { + "Alderspensjonen er basert på di eiga pensjonsopptening og opptening frå den avdøde. Attlevandetillegget er differansen mellom denne alderspensjonen og den alderspensjonen du har tent opp sjølv. Den samla alderspensjon basert på di eiga pensjonsopptening og alderspensjon med opptening frå den avdøde blir same beløp. Difor blir ikkje attlevandetillegget utbetalt." },
                                english { + "The retirement pension is based on your own pension earnings and the earnings from the deceased. The survivor’s supplement is the difference between this retirement pension and the retirement pension you have earned yourself. Your total retirement pension, based on your own pension earnings and the retirement pension with earnings from the deceased, becomes the same amount. Therefore, the survivor’s supplement is not paid out." }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { + "Alderspensjonen som nå er basert på din egen opptjening, blir fortsatt regulert 1. mai hvert år." },
                                nynorsk { + "Alderspensjonen som no er basert på di eiga opptening, blir framleis regulert 1. mai kvart år." },
                                english { + "The retirement pension, which is now based on your own earnings, continues to be adjusted from 1 May each year." }
                            )
                        }
                    }
            }

            // forklaringutfasingGjtKap20_001
            showIf(pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget) {
                title1 {
                    text(
                        bokmal { + "Gjenlevendetillegget etter nye regler blir faset ut" },
                        nynorsk { + "Attlevandetillegget etter nye reglar blir fasa ut" },
                        english { + "The survivor’s supplement according to the new rules is being phased out" }
                    )
                }
                paragraph {
                    // TODO: Denne teksten framstår utdatert nå når 2024 er over.
                    text(
                        bokmal { + "Fra 2024 blir dette tillegget redusert med samme beløp som alderspensjonen din øker ved den årlige reguleringen. Tillegget vil dermed bli lavere, og etter hvert opphøre." },
                        nynorsk { + "Frå 2024 blir dette tillegget redusert med same beløp som alderspensjonen din aukar ved den årlege reguleringa. Tillegget vil dermed bli lågare, og etter kvart bli borte." },
                        english { + "From 2024, this supplement will be reduced by the same amount as your retirement pension increases with the annual adjustment. The supplement will thus become lower, and eventually cease." }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Hvis du tar ut mindre enn 100 prosent alderspensjon, har det betydning for gjenlevendetillegget ditt. Hvis du øker uttaksgraden senere, vil ikke gjenlevendetillegget øke." },
                        nynorsk { + "Viss du tar ut mindre enn 100 prosent alderspensjon, verkar det inn på attlevandetillegget ditt. Viss du aukar uttaksgraden seinare, vil ikkje attlevandetillegget auke." },
                        english { + "If you take out less than 100 percent retirement pension, it will affect your survivor’s supplement. If you increase the withdrawal rate later, the survivor’s supplement will not increase." }
                    )
                }
            }

            // omregnetTPAvdod_001
            showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP1967) and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt) {
                paragraph {
                    text(
                        bokmal { + "Tilleggspensjonen til en gjenlevende alderspensjonist kan enten bestå av pensjonistens egen tilleggspensjon eller 55 prosent av summen av pensjonistens egen tilleggspensjon og den avdødes tilleggspensjon. Tilleggspensjonen din er gitt etter det siste alternativet, da dette gir det høyeste beløpet for deg." },
                        nynorsk { + "Tilleggspensjonen til ein attlevande alderspensjonist kan anten bestå av pensjonistens eigen tilleggspensjon eller 55 prosent av summen av pensjonistens eigen tilleggspensjon og tilleggspensjonen til den avdøde. Tilleggspensjonen din er gitt etter det siste alternativet då det gir det høgaste beløpet for deg." },
                        english { + "The supplementary pension for a widowed old age pensioner can be calculated either as the pensioner's own supplementary pension or as 55 percent of the sum of the pensioner's own supplementary pension and the deceased's supplementary pension. Your supplementary pension has been calculated using the latter method, as this is more beneficial for you." }
                    )
                }
            }

            // omregnetEnsligAP_001
            showIf(saksbehandlerValg.omregnetTilEnsligISammeVedtak) {
                paragraph {
                    text(
                        bokmal { + "I tillegg har vi regnet om pensjonen din fordi du har blitt enslig pensjonist." },
                        nynorsk { + "I tillegg har vi rekna om pensjonen din fordi du har blitt einsleg pensjonist." },
                        english { + "We have also recalculated your pension because you have become a single pensioner." }
                    )
                }
            }

            showIf(pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(ENDR_OKT)) {
                // nyBeregningAPØkning_001
                paragraph {
                    text(
                        bokmal { + "Dette fører til at pensjonen din øker." },
                        nynorsk { + "Dette fører til at pensjonen din aukar." },
                        english { + "This leads to an increase in your retirement pension." }
                    )
                }
            }.orShow {
                // ingenEndringBelop_002
                paragraph {
                    text(
                        bokmal { + "Dette får derfor ingen betydning for utbetalingen din." },
                        nynorsk { + "Dette får derfor ingen følgjer for utbetalinga di." },
                        english { + "Therefore, this does not affect the amount you will receive." }
                    )
                }
            }

            showIf(
                not(pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget)
                        and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget)
            ) {
                includePhrase(DuFaarHverMaaned(pesysData.alderspensjonVedVirk.totalPensjon))
            }

            // beloepApOgGjtvedVirk_001
            showIf(
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)
                        and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
                        and not(pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget)
            ) {
                paragraph {
                    text(
                        bokmal { + "Du får " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt. Av dette er gjenlevendetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        ).format() + "." },
                        nynorsk { + "Du får " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt. Av dette er attlevandetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        ).format() + "." },
                        english { + "You receive " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " in retirement pension and survivor’s supplement from the National Insurance Scheme every month before tax. Of this, the survivor’s supplement is " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        ).format() + "." },
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
                    text(
                        bokmal { + "Du får " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt. Av dette er gjenlevendetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        )
                            .format() + " etter gamle regler og " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(
                            Kroner(0)
                        ).format() + " etter nye regler." },
                        nynorsk { + "Du får " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt. Av dette er attlevandetillegget " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        )
                            .format() + " etter gamle reglar og " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(
                            Kroner(0)
                        ).format() + " etter nye reglar." },
                        english { + "You receive " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " in retirement pension and survivor’s supplement from the National Insurance every month before tax. Of this, the survivor’s supplement is " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(
                            Kroner(0)
                        )
                            .format() + " according to old rules and " + pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(
                            Kroner(0)
                        ).format() + " according to new rules." },
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
                        bokmal { + "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL." },
                        nynorsk { + "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL." },
                        english { + "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL." }
                    )
                }
            }

            includePhrase(FlereBeregningsperioder(pesysData.beregnetPensjonPerManedVedVirk.antallBeregningsperioderPensjon, pesysData.alderspensjonVedVirk.totalPensjon))

            showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967, AP2011, AP2016)) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ " },
                        english { + "This decision was made pursuant to the provisions of §§ " }
                    )

                    // gjRettAP1967STHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP1967)
                                and pesysData.alderspensjonVedVirk.saertilleggInnvilget
                                and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)
                    ) {
                        text(
                            bokmal { + "3-2, 3-3, 19-8, 19-16 og 22-12" },
                            nynorsk { + "3-2, 3-3, 19-8, 19-16 og 22-12" },
                            english { + "3-2, 3-3, 19-8, 19-16 and 22-12 of the National Insurance Act" }
                        )
                    }

                    // gjRettAP1967IngenSTHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967)
                                and not(pesysData.alderspensjonVedVirk.saertilleggInnvilget)
                                and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)
                    ) {
                        text(
                            bokmal { + "3-2, 19-8, 19-16 og 22-12" },
                            nynorsk { + "3-2, 19-8, 19-16 og 22-12" },
                            english { + "3-2, 19-8, 19-16 and 22-12 of the National Insurance Act" }
                        )
                    }

                    // gjRettAPMNTHjemmel1967
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967)
                                and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget
                                and not(pesysData.alderspensjonVedVirk.saertilleggInnvilget)
                    ) {
                        text(
                            bokmal { + "3-2, 19-8, 19-14, 19-16 og 22-12" },
                            nynorsk { + "3-2, 19-8, 19-14, 19-16 og 22-12" },
                            english { + "3-2, 19-8, 19-14, 19-16 and 22-12 of the National Insurance Act" }
                        )
                    }

                    // gjRettAP1967MNTHjemmel_001
                    showIf(
                        pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP1967)
                                and pesysData.alderspensjonVedVirk.saertilleggInnvilget
                                and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget
                    ) {
                        text(
                            bokmal { + "3-2, 3-3, 19-8, 19-14, 19-16 og 22-12" },
                            nynorsk { + "3-2, 3-3, 19-8, 19-14, 19-16 og 22-12" },
                            english { + "3-2, 3-3, 19-8, 19-14, 19-16 and 22-12 of the National Insurance Act" }
                        )
                    }

                    // gjRettAP2011Hjemmel_001
                    showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2011) and not(pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget)) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                bokmal { + "3-2, 19-8, 19-9, 19-16 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-9, 19-16 og 22-12" },
                                english { + "3-2, 19-8, 19-9, 19-16 and 22-12" }
                            )
                        }.orShow {
                            text(
                                bokmal { + "3-2, 19-8, 19-16 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-16 og 22-12" },
                                english { + "3-2, 19-8, 19-16 and 22-12" }
                            )
                        }
                    }

                    // gjRettAP2011MNTHjemmel_001
                    showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2011) and pesysData.alderspensjonVedVirk.minstenivaIndividuellInnvilget) {
                        showIf(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) {
                            text(
                                bokmal { + "3-2, 19-8, 19-9, 19-14, 19-16 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-9, 19-14, 19-16 og 22-12" },
                                english { + "3-2, 19-8, 19-9, 19-14, 19-16 and 22-12" }
                            )
                        }.orShow {
                            text(
                                bokmal { + "3-2, 19-8, 19-14, 19-16 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-14, 19-16 og 22-12" },
                                english { + "3-2, 19-8, 19-14, 19-16 and 22-12" }
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
                                bokmal { + "3-2, 19-8, 19-9, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-9, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                english { + "3-2, 19-8, 19-9, 19-15, 19-16, 20-18, 20-19 and 22-12" }
                            )
                        }.orShow {
                            text(
                                bokmal { + "3-2, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                english { + "3-2, 19-15, 19-16, 20-18, 20-19 and 22-12" }
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
                                bokmal { + "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12" },
                                english { + "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 and 22-12" },
                            )
                        }.orShow {
                            text(
                                bokmal { + "3-2, 19-8, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 og 22-12" },
                                english { + "3-2, 19-8, 19-14, 19-15, 19-16, 20-9, 20-18, 20-19 and 22-12" },
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
                                bokmal { + "3-2, 19-8, 19-9, 19-15, 19-16, 20-9, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-9, 19-15, 19-16, 20-9, 20-19 og 22-12" },
                                english { + "3-2, 19-8, 19-9, 19-15, 19-16, 20-9, 20-19 and 22-12" },
                            )
                        }.orShow {
                            text(
                                bokmal { + "3-2, 19-15, 19-16, 20-9, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-15, 19-16, 20-9, 20-19 og 22-12" },
                                english { + "3-2, 19-15, 19-16, 20-9, 20-19 and 22-12" },
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
                                bokmal { + "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                english { + "3-2, 19-8, 19-9, 19-14, 19-15, 19-16, 20-18, 20-19 and 22-12" },
                            )
                        }.orShow {
                            text(
                                bokmal { + "3-2, 19-8, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                nynorsk { + "3-2, 19-8, 19-14, 19-15, 19-16, 20-18, 20-19 og 22-12" },
                                english { + "3-2, 19-8, 19-14, 19-15, 19-16, 20-18, 20-19 and 22-12" },
                            )
                        }
                    }

                    text(
                        bokmal { + "." }, nynorsk { + "." }, english { + "." }
                    )
                }
            }

            // innvilgetGjTilleggKap20_001
            showIf(pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetillegg_safe.ifNull(Kroner(0)).greaterThan(0)) {
                paragraph {
                    text(
                        bokmal { + "Du er også innvilget gjenlevendetillegg etter regler i kapittel 20 i folketrygdloven." },
                        nynorsk { + "Du får også eit attlevandetillegg etter reglar i kapittel 20 i folketrygdlova." },
                        english { + "You are also granted a survivor’s supplement pursuant to the provisions of Chapter 20 of the National Insurance Act." }
                    )
                }
            }

            // innvilgetGjRettKap19_001
            showIf(brukerFoedtEtter1944 and pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget) {
                paragraph {
                    text(
                        bokmal { + "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024." },
                        nynorsk { + "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024." },
                        english { + "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024." }
                    )
                }
            }

            // infoAPopptjRedusPoengUnder67Aar_001
            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(
                pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
                        and saksbehandlerValg.brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar
            ) {
                paragraph {
                    text(
                        bokmal { + "Fram til året du fyller 67 år kan du øke pensjonsopptjeningen din ved å bo i Norge." },
                        nynorsk { + "Fram til året du fyller 67 år, kan du auke pensjonsoppteninga di ved å bu i Noreg." },
                        english { + "You can increase your earned pension until the year you turn 67 by living in Norway." }
                    )
                }
            }

            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            // infoAPopptjRedusPoengOver67Aar_001
            showIf(
                pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
                        and saksbehandlerValg.brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar
            ) {
                paragraph {
                    text(
                        bokmal { + "Fra du er 67 til 75 år, kan pensjonsopptjeningen din øke dersom den årlige inntekten din er høyere enn folketrygdens grunnbeløp. I vedleggene finner du mer detaljerte opplysninger." },
                        nynorsk { + "Frå du er 67 til 75 år, kan pensjonsoppteninga di auke dersom den årlege inntekta din er høgare enn grunnbeløpet i folketrygda. I vedlegga finn du meir detaljerte opplysningar." },
                        english { + "Your earned pension can increase from when you are 67 until 75 years of age if your annual income is higher than the National Insurance basic amount. You will find more detailed information in the attachments." }
                    )
                }
            }

            // skattAPendring_001
            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(pesysData.alderspensjonVedVirk.harEndretPensjon and saksbehandlerValg.endringIPensjonsutbetaling) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            // pensjonFraAndreOverskrift_001
            title1 {
                text(
                    bokmal { + "Andre pensjonsordninger" },
                    nynorsk { + "Andre pensjonsordningar" },
                    english { + "Other pension schemes" }
                )
            }

            // infoAvdodPenFraAndre_001
            paragraph {
                text(
                    bokmal { + "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet." },
                    nynorsk { + "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet." },
                    english { + "If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company." }
                )
            }

            // etterbetalingAP_002
            showIf(pesysData.krav.etterbetaling) {
                includePhrase(Vedtak.Etterbetaling(pesysData.krav.virkDatoFom))
            }

            // infoAPOverskrift_001
            includePhrase(InformasjonOmAlderspensjon)

            // meldEndringerPesysGjenlevende_001
            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                    english { + "You must report changes" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du mister gjenlevenderetten hvis du gifter deg eller du flytter sammen med en du har barn med eller tidligere har vært gift med." },
                    nynorsk { + "Du mistar attlevandetillegget om du giftar deg eller du flyttar saman med ein du har barn med eller tidlegare har vore gift med." },
                    english { + "You lose the survivor's right if you marry, or you move in with someone you have children with or have previously been married to." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du planlegger opphold i et annet land, kan det påvirke utbetalingen din. Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om." },
                    nynorsk { + "Viss du får planlegg opphald i eit anna land, kan det påverke utbetalinga di. Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om." },
                    english { + "If you are planning to stay in another country, it may affect your benefit payment. If there are changes, you must notify us immediately. The appendix specifies which changes you are obligated to notify us of." }
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
        includeAttachmentIfNotNull(vedleggOpplysningerOmAvdoedBruktIBeregning, pesysData.opplysningerOmAvdoedBruktIBeregningDto) // v6
    }
}