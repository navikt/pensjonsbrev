package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.BeregnetPensjonPerManedVedVirkSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.InstitusjonsoppholdVedVirkSelectors.fengsel
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.InstitusjonsoppholdVedVirkSelectors.helseinstitusjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.alderspensjonRedusert
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.alderspensjonStanset
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.alderspensjonUnderOppholdIInstitusjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.alderspensjonUnderSoning
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.alderspensjonVedVaretektsfengsling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.hvisEndringIPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.hvisEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.hvisReduksjonTilbakeITid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.informasjonOmSivilstandVedInstitusjonsopphold
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonInstitusjonsoppholdDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.HvorKanDuFaaViteMerOmAlderspensjonenDin
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.maanedligPensjonFoerSkattAlderspensjon
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
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringAvAlderspensjonInstitusjonsopphold : RedigerbarTemplate<VedtakEndringAvAlderspensjonInstitusjonsoppholdDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_INSTITUSJONSOPPHOLD
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAvAlderspensjonInstitusjonsoppholdDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring ved institusjonsopphold",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            showIf(pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.greaterThan(0)) {
                // nyBeregningAPTittel_001
                textExpr(
                    Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + pesysData.krav.virkDatoFom.format(),
                    Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + pesysData.krav.virkDatoFom.format(),
                    English to "We have recalculated your retirement pension from ".expr() + pesysData.krav.virkDatoFom.format()
                )
            }.orShow {
                // stansAPTittel_002
                textExpr(
                    Bokmal to "Vi stanser utbetalingen av alderspensjonen din fra ".expr() + pesysData.krav.virkDatoFom.format(),
                    Nynorsk to "Vi stansar utbetalinga av alderspensjonen din frå ".expr() + pesysData.krav.virkDatoFom.format(),
                    English to "We are stopping your retirement pension from ".expr() + pesysData.krav.virkDatoFom.format()
                )

            }
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(pesysData.beregnetPensjonPerManedVedVirk.antallBeregningsperioderPensjon.equalTo(1)) {
                showIf(pesysData.institusjonsoppholdVedVirk.helseinstitusjon and pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.greaterThan(0)) {
                    // meldingInstOpphold_001
                    paragraph {
                        val fritekst = fritekst("DatoFom")
                        textExpr(
                            Bokmal to "Vi har fått melding om at du oppholder deg på helseinstitusjon fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du oppheld deg på helseinstitusjon frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you are staying in a state-run health institution from and including ".expr() + fritekst + "."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Pensjonen din blir redusert fra og med fire måneder etter at du ble lagt inn.",
                            Nynorsk to "Pensjonen din blir redusert frå og med den fjerde månaden etter at du blei lagt inn.",
                            English to "Your retirement pension will be reduced from and including the fourth month after you were admitted."
                        )
                    }
                }

                showIf(pesysData.institusjonsoppholdVedVirk.helseinstitusjon) {
                    // innvilgelseAPInstInnled_001
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner før skatt fra " + pesysData.krav.virkDatoFom.format() + " og så lenge du er på helseinstitusjonen.",
                            Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner før skatt frå " + pesysData.krav.virkDatoFom.format() + " og så lenge du er på helseinstitusjon.",
                            English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " before tax from " + pesysData.krav.virkDatoFom.format() + " and as long as you are institutionalized.",
                        )
                    }
                }

                showIf(pesysData.institusjonsoppholdVedVirk.fengsel) {
                    // meldingSoningReduStans_001
                    paragraph {
                        val datoFom = fritekst("dato fom")
                        textExpr(
                            Bokmal to "Vi har fått melding om at du ".expr() + fritekst("soner en straff / er varetektsfengslet") + " fra og med " + datoFom + ".",
                            Nynorsk to "Vi har fått melding om at du ".expr() + fritekst("sonar ei straff / er varetektsfengsla") + " frå og med " + datoFom + ".",
                            English to "We have received notice that you are ".expr() + fritekst("serving a prison sentence / in custody on remand") + " from and including " + datoFom + ".",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Når du sitter i fengsel, får du alderspensjon i innsettelsesmåneden og måneden etter. Deretter vil utbetalingen av pensjonen bli ".expr() + fritekst("redusert / stanset") + ".",
                            Nynorsk to "Når du sit i fengsel, får du alderspensjon i den månaden du blei sett inn, og i månaden etter. Deretter blir utbetalinga av pensjonen ".expr() + fritekst("redusert / stansa") + ".",
                            English to "When you are in prison, you will receive your retirement pension in the month you start your prison sentence and the month after. After that, your pension payments will be ".expr() + fritekst("reduced / suspended") + "."
                        )
                    }

                    showIf(pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.greaterThan(0)) {
                        // innvilgelseAPSonerInnled_001
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner før skatt fra " + pesysData.krav.virkDatoFom.format() + " og så lenge du er under straffegjennomføring.",
                                Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner før skatt frå " + pesysData.krav.virkDatoFom.format() + " og så lenge du er under straffegjennomføring.",
                                English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " before tax from " + pesysData.krav.virkDatoFom.format() + " and as long as you serving a criminal sentence.",
                            )
                        }
                    }
                }.orShowIf(not(pesysData.institusjonsoppholdVedVirk.helseinstitusjon)) {
                    /* TODO: Nå vises alle avsnittene her.
                    I praksis skal saksbehandler velge ett av disse, og fjerne det som ikke er relevant.
                    Vi har ikke ei god løsning for dette per nå, så det må vi finne ut av
                    i https://trello.com/c/QOgu2fHm/178-unders%C3%B8ke-behov-ved-konvertering-av-preselected-innhold
                     */
                    paragraph {
                        // meldingUtskrevetInst_001
                        val fritekst = fritekst("Dato fom")
                        // preselected
                        textExpr(
                            Bokmal to "Vi har mottatt melding om at du er utskrevet fra helseinstitusjon fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du er utskriven frå helseinstitusjon frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you were discharged from a health institution from and including ".expr() + fritekst + "."
                        )
                    }

                    paragraph {
                        // meldingUtskrevetSoning_001
                        val fritekst = fritekst("Dato fom")
                        // preselected
                        textExpr(
                            Bokmal to "Vi har mottatt melding om at du er ferdig med å sone straffen fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du er ferdig med å sone straffa frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you completed serving your prison sentence from and including ".expr() + fritekst + "."
                        )
                    }

                    paragraph {
                        // meldingUtskrevetVaretekt_001
                        val fritekst = fritekst("Dato fom")
                        // preselected
                        textExpr(
                            Bokmal to "Vi har mottatt melding om at du er løslatt fra varetekt fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du er lauslaten frå varetekt frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you were released from custody on remand from and including ".expr() + fritekst + "."
                        )
                    }

                    showIf(pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.greaterThan(0)) {
                        showIf(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder) {
                            // innvilgelseAPogUTInnledn_001
                            paragraph {
                                textExpr(
                                    Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                                    Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                                    English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit.",
                                )
                            }
                        }.orShow {
                            // innvilgelseAPInnledn_001
                            paragraph {
                                textExpr(
                                    Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + " i alderspensjon fra folketrygden.",
                                    Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + " i alderspensjon frå folketrygda.",
                                    English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + " as retirement pension from the National Insurance Scheme.",
                                )
                            }
                        }
                    }
                }
            }.orShowIf(pesysData.beregnetPensjonPerManedVedVirk.antallBeregningsperioderPensjon.greaterThan(1)) {
                // selectable, 1
                showIf(saksbehandlerValg.alderspensjonUnderOppholdIInstitusjon) {
                    // meldingInstOppholdRedu_001
                    paragraph {
                        val fritekst = fritekst("Dato fom")
                        textExpr(
                            Bokmal to "Vi har fått melding om at du oppholder deg på helseinstitusjon fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du oppheld deg på helseinstitusjon frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you are staying in a state-run health institution from and including ".expr() + fritekst + "."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Pensjonen din blir redusert fra og med fire måneder etter at du ble lagt inn.",
                            Nynorsk to "Pensjonen din blir redusert frå og med den fjerde månaden etter at du blei lagt inn.",
                            English to "Your retirement pension will be reduced from and including the fourth month after you were admitted."
                        )
                    }
                    // innvilgelseAPInstInnled_002_]
                    paragraph {
                        val beloep = fritekst("beløp")
                        val datoFom = fritekst("dato fom")
                        textExpr(
                            Bokmal to "Du får ".expr() + beloep + " kroner før skatt fra " + datoFom + " og så lenge du er på helseinstitusjonen.",
                            Nynorsk to "Du får derfor ".expr() + beloep + " kroner før skatt frå " + datoFom + " og så lenge du er på helseinstitusjon.",
                            English to "You will therefore receive NOK ".expr() + beloep + " before tax from " + datoFom + " and as long as you are institutionalized."
                        )
                    }
                }

                showIf(saksbehandlerValg.alderspensjonUnderSoning) {
                    // meldingSoning_001
                    paragraph {
                        val fritekst = fritekst("dato fom")
                        textExpr(
                            Bokmal to "Vi har fått melding om at du soner en straff fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du sonar ei straff frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you are serving a prison sentence from and including ".expr() + fritekst + "."
                        )
                    }
                }

                showIf(saksbehandlerValg.alderspensjonVedVaretektsfengsling) {
                    // meldingVaretekt_001
                    paragraph {
                        val fritekst = fritekst("dato fom")
                        textExpr(
                            Bokmal to "Vi har fått melding om at du er varetektsfengslet fra og med ".expr() + fritekst + ".",
                            Nynorsk to "Vi har fått melding om at du er varetektsfengsla frå og med ".expr() + fritekst + ".",
                            English to "We have received notice that you are in custody on remand from and including ".expr() + fritekst + "."
                        )
                    }
                }

                showIf(saksbehandlerValg.alderspensjonRedusert) {
                    // infoBrukerFengselRedusertAP_001
                    paragraph {
                        text(
                            Bokmal to "Når du sitter i fengsel, får du alderspensjon i innsettelsesmåneden og måneden etter. Deretter blir utbetalingen av pensjonen redusert.",
                            Nynorsk to "Når du sit i fengsel, får du alderspensjon i den månaden du blei sett inn, og i månaden etter. Deretter blir utbetalinga av pensjonen redusert.",
                            English to "When you are in prison, you will receive your retirement pension in the month you start your prison sentence and the month after. After that, your pension payments will be reduced."
                        )
                    }
                    paragraph {
                        val beloep = fritekst("beløp")
                        val datoFom = fritekst("dato fom")
                        textExpr(
                            Bokmal to "Du får derfor ".expr() + beloep + " kroner før skatt fra " + datoFom + " og så lenge du er under straffegjennomføring.",
                            Nynorsk to "Du får derfor ".expr() + beloep + " kroner før skatt frå " + datoFom + " og så lenge du er under straffegjennomføring.",
                            English to "You will therefore receive NOK ".expr() + beloep + " before tax from " + datoFom + " and as long as you are serving a criminal sentence."
                        )
                    }
                }

                showIf(saksbehandlerValg.alderspensjonStanset) {
                    // infoBrukerFengselStansAP_001
                    paragraph {
                        text(
                            Bokmal to "Når du sitter i fengsel, får du alderspensjon i innsettelsesmåneden og måneden etter. Deretter blir utbetalingen av pensjonen stanset.",
                            Nynorsk to "Når du sit i fengsel, får du alderspensjon i den månaden du blei sett inn, og i månaden etter. Deretter blir utbetalinga av pensjonen stansa.",
                            English to "When you are in prison, you will receive your retirement pension in the month you start your prison sentence and the month after. After that, your pension payments will be suspended."
                        )
                    }
                    paragraph {
                        val datoFom = fritekst("dato fom")
                        textExpr(
                            Bokmal to "Du får derfor ikke utbetalt alderspensjon fra ".expr() + datoFom + " og så lenge du er under straffegjennomføring.",
                            Nynorsk to "Du får derfor ikkje betalt ut alderspensjon frå ".expr() + datoFom + " og så lenge du er under straffegjennomføring.",
                            English to "You will therefor no longer receive retirement pension from ".expr() + datoFom + " and as long as you are serving a criminal sentence."
                        )
                    }
                }

                showIf(saksbehandlerValg.informasjonOmSivilstandVedInstitusjonsopphold) {
                    // infoBrukerInst_001
                    paragraph {
                        textExpr(
                            Bokmal to "Fordi du ikke bor sammen med ".expr() + fritekst("ektefelle/partner/samboer") + " mens du er på institusjon får du pensjon som enslig pensjonist. Det vil si at pensjonen din først øker til " + fritekst("beløp") + " kroner før skatt fra og med " + pesysData.krav.virkDatoFom.format() + ", men deretter blir " + fritekst("redusert/stanset") + " fra " + fritekst("dato fom") + ".",
                            Nynorsk to "Fordi du ikkje bur saman med ".expr() + fritekst("ektefelle/partnar/sambuar") + " mens du er på institusjon, får du pensjon som einsleg pensjonist. Det vil si at pensjonen din først aukar til " + fritekst("beløp") + " kroner før skatt frå og med " + pesysData.krav.virkDatoFom.format() + ", men deretter blir " + fritekst("redusert/stansa") + " frå " + fritekst("dato fom") + ".",
                            English to "Because you are not living with your ".expr() + fritekst("spouse/partner/cohabitant") + "  when you are institutionalized, your pension will be calculated as if you are a single pensioner. This means that first your pension payments will increase to NOK " + fritekst("beløp") + " before tax from " + pesysData.krav.virkDatoFom.format() + ", but then will be " + fritekst("reduced/suspended") + " from " + fritekst("dato fom") + ".",
                        )
                    }
                }
            }

            showIf(pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.greaterThan(0)) {
                // utbetalingsInfoMndUtbet_001
                paragraph {
                    text(
                        Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL.",
                        Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL.",
                        English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."
                    )
                }
            }


            showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP1967)) {
                // hjemmelBrukerAP2011Inst_001
                paragraph {
                    text(
                        Bokmal to "Pensjonen din beregnes etter folketrygdloven § 19-21 så lenge du er på institusjon.",
                        Nynorsk to "Pensjonen din blir berekna etter folketrygdlova § 19-21 så lenge du er på institusjon.",
                        English to "Your retirement pension is calculated according to the provisions of § 19-21 of the National Insurance Act, as long as you are institutionalized."
                    )
                }
                paragraph {
                    // hjemmelBrukerAP2011Straff_001
                    text(
                        Bokmal to "Pensjonen din beregnes etter folketrygdloven § 19-22 så lenge du er under straffegjennomføring.",
                        Nynorsk to "Pensjonen din blir berekna etter folketrygdlova § 19-22 så lenge du er under straffegjennomføring.",
                        English to "Your retirement pension is calculated according to the provisions of § 19-22 of the National Insurance Act, as long as you are serving a criminal sentence."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
                paragraph {
                    // hjemmelBrukerAP2016Inst_001
                    text(
                        Bokmal to "Pensjonen din beregnes etter folketrygdloven §§ 19-21 og 20-22 så lenge du er på institusjon.",
                        Nynorsk to "Pensjonen din blir berekna etter folketrygdlova §§ 19-21 og 20-22 så lenge du er på institusjon.",
                        English to "Your retirement pension is calculated according to the provisions of §§ 19-21 and 20-22 of the National Insurance Act, as long as you are institutionalized."
                    )
                }
                paragraph {
                    // hjemmelBrukerAP2016Straff_001
                    text(
                        Bokmal to "Pensjonen din beregnes etter folketrygdloven §§ 19-22 og 20-23 så lenge du er under straffegjennomføring.",
                        Nynorsk to "Pensjonen din blir berekna etter folketrygdlova §§ 19-22 og 20-23 så lenge du er under straffegjennomføring.",
                        English to "Your retirement pension is calculated according to the provisions of §§ 19-22 and 20-23 of the National Insurance Act, as long as you are serving a criminal sentence."
                    )
                }
            }

            showIf(pesysData.beregnetPensjonPerManedVedVirk.antallBeregningsperioderPensjon.greaterThan(1) and not(pesysData.institusjonsoppholdVedVirk.fengsel) and not (pesysData.institusjonsoppholdVedVirk.helseinstitusjon)) {
                paragraph {
                    // hjemmelEndrIForbindelseMedInst_001_]
                    val fritekst = fritekst("Fyll inn aktuelle paragrafer>.")
                    textExpr(
                        Bokmal to "Pensjonen din beregnes etter folketrygdloven §§ ".expr() + fritekst + ".",
                        Nynorsk to "Pensjonen din berekna etter folketrygdlova §§ ".expr() + fritekst + ".",
                        English to "Your retirement pension is calculated according to the provisions of §§ ".expr() + fritekst + "."
                    )
                }
            }

            showIf(saksbehandlerValg.hvisReduksjonTilbakeITid) {
                // feilutbetalingAP_001
                title1 {
                    text(
                        Bokmal to "Feilutbetaling",
                        Nynorsk to "Feilutbetaling",
                        English to "Incorrect payment"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi har redusert pensjonen din tilbake i tid. Derfor har du fått for mye utbetalt. Vi vil sende deg et eget varselbrev om en eventuell tilbakebetaling.",
                        Nynorsk to "Vi har redusert pensjonen din tilbake i tid. Derfor har du fått for mykje utbetalt. Vi vil sende deg eit eige varselbrev om ei eventuell tilbakebetaling.",
                        English to "We have reduced your retirement pension for a previous period. You have therefore been paid too much. We will send you a separate notice letter concerning possible repayment."
                    )
                }
            }

            showIf(saksbehandlerValg.hvisEtterbetaling) {
                // etterbetalingAP_002
                includePhrase(Vedtak.Etterbetaling(pesysData.krav.virkDatoFom))
            }

            showIf(saksbehandlerValg.hvisEndringIPensjon) {
                // skattAPendring_001
                includePhrase(Vedtak.EndringKanHaBetydningForSkatt)
            }

            // pensjonFraAndreInfoOverskrift_001
            // pensjonFraAndreInfoAP_001
            includePhrase(InfoPensjonFraAndreAP)

            // infoAPOverskrift_001
            // infoAP_001
            includePhrase(HvorKanDuFaaViteMerOmAlderspensjonenDin)

            // TODO: Denne burde samkøyrast med Felles.MeldeFraEndringer
            // meldEndringerPesys_001
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must notify Nav if anything changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet, eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav. I slike tilfeller må du derfor straks melde fra til oss. I vedlegget ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet, eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav. I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om.",
                    English to "If there are changes in your family situation or you are planning a long-term stay abroad, or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav. In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav.",
                    Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav.",
                    English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav."
                )
            }

             includePhrase(Felles.RettTilAAKlage(vedleggOrienteringOmRettigheterOgPlikter))
             includePhrase(Felles.RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikterDto)
        includeAttachment(maanedligPensjonFoerSkattAlderspensjon, pesysData.maanedligPensjonFoerSkattAlderspensjonDto)
    }
}