package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.BeregnetPensjonPerManedVedVirkSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.BeregnetPensjonPerManedVedVirkSelectors.antallBeregningsperioderPensjon_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.InstitusjonsoppholdVedVirkSelectors.fengsel
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.InstitusjonsoppholdVedVirkSelectors.helseinstitusjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.SaksbehandlerValgSelectors.redusertHelseinstitusjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAlderspensjonInstitusjonsoppholdDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
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
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringAlderspensjonInstitusjonsopphold : RedigerbarTemplate<VedtakEndringAlderspensjonInstitusjonsoppholdDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_INSTITUSJONSOPPHOLD
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAlderspensjonInstitusjonsoppholdDto::class,
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
                showIf(saksbehandlerValg.redusertHelseinstitusjon) {
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
            }

        }
    }
}

    // vedlegg:                         doksysVedleggMapper.map("RETTIGH_PLIKT_V1", "AP_MND_UTB_V4")));
// RETTIGH_PLIKT_V1 == v2
// AP_MND_UTB_V4 == v7