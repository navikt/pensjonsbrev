package brev.auto

import brev.felles.Constants.DITT_NAV_URL
import brev.felles.Constants.NAV_URL
import no.nav.pensjon.brev.api.model.maler.Aldersbrevkoder
import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.Institusjon
import no.nav.pensjon.brev.api.model.maler.Sivilstand
import no.nav.pensjon.brev.api.model.maler.SivilstandAvdoed
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDto
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.harEndretPensjon
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AvdodInformasjonSelectors.ektefelletilleggOpphort
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AvdodInformasjonSelectors.gjenlevendesAlder
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.AvdodInformasjonSelectors.sivilstandAvdoed
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.BarnSelectors.harBarnUnder18
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.BeregnetPensjonPerManedSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.BeregnetPensjonPerManedSelectors.erPerioderMedUttak
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.BeregnetPensjonPerManedSelectors.garantiPensjon
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.institusjon
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.InstitusjonsoppholdVedVirkSelectors.institusjon
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.avdod
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.avdodNavn
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.barn
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.beregnetPensjonPerManed
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.etterBetaling
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.auto.EndringAvAlderspensjonAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EndringAvAlderspensjonAuto : AutobrevTemplate<EndringAvAlderspensjonAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ENDRING_AV_ALDERSPENSJON_AUTO

    override val template = createTemplate(
        letterDataType = EndringAvAlderspensjonAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Vi har regnet om alderspensjonen din" },
                nynorsk { +"Vi har rekna om alderspensjonen din" },
                english { +"We have altered your retirement pension" }
            )
        }
        outline {
            title2 {
                text(
                    bokmal { +"Vedtak" },
                    nynorsk { +"Vedtak" },
                    english { +"Decision" }
                )
            }
            showIf(alderspensjonVedVirk.harEndretPensjon) {
                paragraph {
                    text(
                        bokmal { +"Vi har fått melding om at " + avdodNavn + " er død, og vi har regnet om pensjonen din fra " + virkFom.format() + " fordi du har blitt enslig pensjonist." },
                        nynorsk { +"Vi har fått melding om at " + avdodNavn + " er død, og vi har rekna om pensjonen din frå " + virkFom.format() + " fordi du har blitt einsleg pensjonist." },
                        english { +"We have received notice that " + avdodNavn + " has died, and we have recalculated your pension from " + virkFom.format() + " because you have become a single pensioner." }
                    )
                }
            } orShow {
                paragraph {
                    text(
                        bokmal { +"Vi har fått melding om at " + avdodNavn + " er død. Pensjonen din blir ikke endret." },
                        nynorsk { +"Vi har fått melding om at " + avdodNavn + " er død. Pensjonen din blir ikkje endra." },
                        english {
                            +"We have received notice that " + avdodNavn + " has died. " +
                                    "The recalculation did not lead to any changes in your pension payments."
                        }
                    )
                }
            }
            showIf(alderspensjonVedVirk.totalPensjon.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal { +"Du får " + alderspensjonVedVirk.totalPensjon.format() + " kroner i alderspensjon fra folketrygden hver måned før skatt." },
                        nynorsk { +"Du får " + alderspensjonVedVirk.totalPensjon.format() + " kroner i alderspensjon frå folketrygda kvar månad før skatt." },
                        english { +"You will receive " + alderspensjonVedVirk.totalPensjon.format() + " every month before tax as retirement pension through the National Insurance Act." }
                    )
                }
            }
            showIf(
                beregnetPensjonPerManed.antallBeregningsperioderPensjon.equalTo(1) and alderspensjonVedVirk.totalPensjon.greaterThan(
                    0
                )
            ) {
                paragraph {
                    text(
                        bokmal {
                            +"Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                                    "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger."
                        },
                        nynorsk {
                            +"Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                                    "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på nav.no/utbetalinger."
                        },
                        english {
                            +"If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                                    "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at nav.no/utbetalingsinformasjon."
                        }
                    )
                }
            }
            showIf(
                beregnetPensjonPerManed.antallBeregningsperioderPensjon.greaterThan(1) and alderspensjonVedVirk.totalPensjon.greaterThan(
                    0
                )
            ) {
                paragraph {
                    text(
                        bokmal { +"Du kan lese mer om andre beregningsperioder i vedlegget." },
                        nynorsk { +"Du kan lese meir om andre berekningsperiodar i vedlegget." },
                        english { +"There is more information about other calculation periods in the attachment." }
                    )
                }
            }

            showIf(
                beregnetPensjonPerManed.erPerioderMedUttak
                        and alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP1967)
                        and institusjonsoppholdVedVirk.institusjon.notEqualTo(Institusjon.FENGSEL)
            ) {
                paragraph {
                    text(
                        bokmal { +"Du har valgt å stanse uttak av alderspensjon. Derfor får du ingen endring i utbetalingen." },
                        nynorsk { +"Du har valt å stanse uttak av alderspensjon. Derfor får du ingen endring i utbetalinga." },
                        english { +"You have chosen to stop your retirement pension. Therefore there will be no change in payment." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.uttaksgrad.equalTo(0)
                        and beregnetPensjonPerManed.erPerioderMedUttak
                        and alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP1967)
                        and institusjonsoppholdVedVirk.institusjon.notEqualTo(Institusjon.FENGSEL)
            ) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har valgt å stanse uttak av alderspensjon. Derfor får du ingen endring i utbetalingen. " +
                                    "Du kan lese mer om andre beregningsperioder i vedlegget."
                        },
                        nynorsk {
                            +"Du har valt å stanse uttak av alderspensjon. Derfor får du ingen endring i utbetalinga. " +
                                    "Du kan lese meir om andre utrekningsperiodar i vedlegget."
                        },
                        english {
                            +"You have chosen to stop your retirement pension. " +
                                    "Therefore there will be no change in payment. You can read more about other calculation periods in the appendix. "
                        }
                    )
                }
            }

            showIf(
                (beregnetPensjonPerManed.erPerioderMedUttak.not()
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.FENGSEL)
                        or
                        (alderspensjonVedVirk.totalPensjon.equalTo(0)
                                and beregnetPensjonPerManed.erPerioderMedUttak
                                and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.FENGSEL)
                                and institusjonsoppholdGjeldende.institusjon.equalTo(Institusjon.FENGSEL)))
            ) {
                paragraph {
                    text(
                        bokmal { +"Du får ikke utbetalt alderspensjon fordi du er under straffegjennomføring." },
                        nynorsk { +"Du får ikkje utbetalt alderspensjon fordi du er under straffegjennomføring." },
                        english { +"You will not receive retirement pension because you are serving a criminal sentence." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.totalPensjon.equalTo(0)
                        and beregnetPensjonPerManed.erPerioderMedUttak
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.FENGSEL)
                        and institusjonsoppholdGjeldende.institusjon.notEqualTo(Institusjon.FENGSEL)
            ) {
                paragraph {
                    text(
                        bokmal {
                            +"Du får ikke utbetalt alderspensjon fordi du er under straffegjennomføring. " +
                                    "Du kan lese mer om andre beregningsperioder i vedlegget."
                        },
                        nynorsk {
                            +"Du får ikkje utbetalt alderspensjon fordi du er under straffegjennomføring. " +
                                    "Du kan lese meir om andre utrekningsperiodar i vedlegget."
                        },
                        english {
                            +"You will not receive retirement pension because you are serving a criminal sentence. " +
                                    "You can read more about other calculation periods in the appendix."
                        }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.harEndretPensjon.not()
                        and avdod.sivilstandAvdoed.notEqualTo(SivilstandAvdoed.SAMBOER3_2)
                        and alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP2025)
            ) {
                paragraph {
                    text(
                        bokmal { +"Nå skal vi vurdere om du har rettigheter etter avdøde. Du vil få et nytt vedtak fra oss innen tre måneder." },
                        nynorsk { +"No skal vi vurdere om du har rettar etter avdøde. Du vil få eit nytt vedtak frå oss innan tre månader." },
                        english {
                            +"We will now consider if you are entitled to survivor's rights as a surviving spouse. " +
                                    "You will receive a new decision from us within three months."
                        }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.harEndretPensjon
                        and alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP2025)
            ) {
                paragraph {
                    text(
                        bokmal { +"Du kan ha rettigheter etter avdøde. Derfor bør du søke om gjenlevenderett i alderspensjonen." },
                        nynorsk { +"Du kan ha rettar etter avdøde. Derfor bør du søkje om attlevanderett i alderspensjonen." },
                        english { +"You can be entitled to survivor's rights as a surviving spouse. Therefore, you should apply for survivor's rights in retirement pension." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP1967)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget.not()
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 3-3 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 3-3 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 3-3 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP1967)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 3-3, 19-14 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 3-3, 19-14 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 3-3, 19-14 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget.not()
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 19-8, 19-14 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 19-8, 19-14 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 19-8, 19-14 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget.not()
                        and (
                        beregnetPensjonPerManed.garantiPensjon.equalTo(0)
                        )
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 19-15, 20-18, 20-19 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 19-15, 20-18, 20-19 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 19-15, 20-18, 20-19 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget.not()
                        and (
                        beregnetPensjonPerManed.garantiPensjon.greaterThan(0)
                        )
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 19-15, 20-9, 20-19 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 19-15, 20-9, 20-19 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 19-15, 20-9, 20-19 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget
                        and (
                        beregnetPensjonPerManed.garantiPensjon.equalTo(0)
                        )
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 19-8, 19-14, 19-15, 20-18, 20-19 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 19-8, 19-14, 19-15, 20-18, 20-19 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 19-8, 19-14, 19-15, 20-18, 20-19 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)
                        and alderspensjonVedVirk.minstenivaIndividuellInnvilget
                        and (
                        beregnetPensjonPerManed.garantiPensjon.greaterThan(0)
                        )
            ) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 3-2, 19-8, 19-14, 19-15, 20-9, 20-18, 20-19 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 3-2, 19-8, 19-14, 19-15, 20-9, 20-18, 20-19 og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 3-2, 19-8, 19-14, 19-15, 20-9, 20-18, 20-19 and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12." },
                        english { +"This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act." }
                    )
                }
            }

            showIf(
                (alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP1967)
                        or alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011))
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.HELSE)
            ) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen din beregnes etter folketrygdloven § 19-21 så lenge du er på institusjon." },
                        nynorsk { +"Pensjonen din reknast ut etter folketrygdlova § 19-21 så lenge du er på institusjon." },
                        english { +"Your retirement pension is calculated according to the provisions of § 19-21 of the National Insurance Act, as long as you are institutionalised." }
                    )
                }
            }

            showIf(
                (alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP1967)
                        or alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011))
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.FENGSEL)
            ) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen din beregnes etter folketrygdloven § 19-22 så lenge du er under straffegjennomføring." },
                        nynorsk { +"Pensjonen din reknast ut etter folketrygdlova § 19-22 så lenge du er under straffegjennomføring." },
                        english { +"Your retirement pension is calculated according to the provisions of § 19-22 of the National Insurance Act, as long as you are serving a criminal sentence." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.HELSE)
            ) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen din beregnes etter folketrygdloven § 20-22 så lenge du er på institusjon." },
                        nynorsk { +"Pensjonen din reknast ut etter folketrygdlova § 20-22 så lenge du er på institusjon." },
                        english { +"Your retirement pension is calculated according to the provision of § 20-22 of the National Insurance Act, as long as you are institutionalised." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.FENGSEL)
            ) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen din beregnes etter folketrygdloven §§ 19-22 og 20-23 så lenge du er under straffegjennomføring." },
                        nynorsk { +"Pensjonen din reknast ut etter folketrygdlova §§ 19-22 og 20-23 så lenge du er under straffegjennomføring." },
                        english { +"Your retirement pension is calculated according to the provisions of §§ 19-22 and 20-23 of the National Insurance Act, as long as you are serving a criminal sentence." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.HELSE)
            ) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen din beregnes etter folketrygdloven § 20-22 så lenge du er på institusjon." },
                        nynorsk { +"Pensjonen din reknast ut etter folketrygdlova § 20-22 så lenge du er på institusjon." },
                        english { +"Your retirement pension is calculated according to the provision of § 20-22 of the National Insurance Act, as long as you are institutionalised." }
                    )
                }
            }

            showIf(
                alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)
                        and institusjonsoppholdVedVirk.institusjon.equalTo(Institusjon.FENGSEL)
            ) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen din beregnes etter folketrygdloven § 20-23 så lenge du er under straffegjennomføring." },
                        nynorsk { +"Pensjonen din reknast ut etter folketrygdlova § 20-23 så lenge du er under straffegjennomføring." },
                        english { +"Your retirement pension is calculated according to the provision of § 20-23 of the National Insurance Act, as long as you are serving a criminal sentence." }
                    )
                }
            }

            showIf(avdod.ektefelletilleggOpphort) {
                title2 {
                    text(
                        bokmal { +"Ektefelletillegget ditt opphører" },
                        nynorsk { +"Ektefelletillegget ditt avsluttast" },
                        english { +"Your spouse supplement will end" }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du forsørger ikke lenger en [_Script Script_Tekst_001_]. Derfor opphører ektefelletillegget ditt." }, //todo må lage formatter for pensjon også
                        nynorsk { +"Du forsørgjer ikkje lenger for ein [_Script Script_Tekst_001_]. Derfor vert ektefelletillegget ditt avslutta." },
                        english { +"You no longer provide for a [_Script Script_Tekst_001_]. Your spouse supplement will therefore end." }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8." },
                        nynorsk { +"Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8." },
                        english { +"The decision has been made pursuant to Section 8 of the transitional provisions for the implementation of disability benefit." }
                    )
                }
            }

            showIf(
                avdod.sivilstandAvdoed.equalTo(SivilstandAvdoed.SAMBOER3_2)
                        and alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP2025)
            ) {
                title2 {
                    text(
                        bokmal { +"Rettigheter du kan ha som tidligere samboer med " + avdodNavn },
                        nynorsk { +"Rettar du kan ha som tidlegare sambuar med " + avdodNavn },
                        english { +"Rights you may be entitled to as a former cohabitant with" + avdodNavn }
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Samboere som tidligere har vært gift, eller som har eller har hatt felles barn, " +
                                    "kan ha rett til høyere alderspensjon hvis avdødes pensjonsopptjening tas med i beregningen. " +
                                    "Du finner mer informasjon og søknadsskjema for gjenlevende ektefelle, " +
                                    "partner eller samboer på nav.no/gjenlevendeektefelle."
                        },
                        nynorsk {
                            +"Sambuarar som tidlegare har vore gift, eller som har eller har hatt felles barn, " +
                                    "kan ha rett til høgare alderspensjon viss avdøde si pensjonsopptening blir tatt med i utrekninga. " +
                                    "Du finn meir informasjon og søknadsskjema for attlevande ektefelle, " +
                                    "partnar eller sambuar på nav.no/gjenlevendeektefelle."
                        },
                        english {
                            +"Cohabitants who have previously been married, " +
                                    "or who have or have had children together, may be entitled to a higher retirement pension if the deceased's earned pension is included in the calculation. " +
                                    "You will find more information and the application form for benefits for surviving spouse, " +
                                    "partner or cohabitant at nav.no/gjenlevendeektefelle."
                        }
                    )
                }
            }

            showIf(
                avdod.sivilstandAvdoed.isOneOf(
                    SivilstandAvdoed.PARTNER,
                    SivilstandAvdoed.GIFT,
                    SivilstandAvdoed.SAMBOER1_5
                )
            ) {
                showIf(alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP2025)) {
                    title2 {
                        text(
                            bokmal { +"Du kan ha rett til høyere pensjon" },
                            nynorsk { +"Du kan ha rett til høgare pensjon" },
                            english { +"You may be entitled to a higher pension" }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du kan få høyere alderspensjon hvis avdødes pensjonsopptjening tas med i beregningen. Da må du som hovedregel:" },
                            nynorsk { +"Du kan få høgare alderspensjon dersom avdøde si pensjonsopptening blir tatt med i utrekninga. Da må du som hovudregel:" },
                            english {
                                +"You may receive a higher retirement pension if the deceased's earned pension is included in the calculation. " +
                                        "To be entitled to survivor's rights in your retirement pension, you must as a rule:"
                            }
                        )
                        list {
                            item {
                                text(
                                    bokmal { +"være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet " },
                                    nynorsk { +"vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet" },
                                    english { +"be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last five years prior to death" },
                                )
                            }
                            item {
                                text(
                                    bokmal { +"ha vært gift med den avdøde i minst fem år, eller " },
                                    nynorsk { +"ha vore gift med den avdøde i minst fem år, eller " },
                                    english { +"have been married to the deceased for at least five years, or " }
                                )
                            }
                            item {
                                text(
                                    bokmal { +"ha vært gift eller vært samboer med den avdøde og har eller ha hatt barn med den avdøde, eller " },
                                    nynorsk { +"ha vore gift eller vore sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller " },
                                    english { +"have been married to or a cohabitant with the deceased, and have/had children together, or " }
                                )
                            }
                            item {
                                text(
                                    bokmal { +"ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år." },
                                    nynorsk { +"ha hatt omsorga for barna til den avdøde på dødsfallstidspunktet. Ekteskapet og omsorga for barnet etter dødsfallet må til saman ha vart minst fem år." },
                                    english { +"have had care of the children of the deceased at the time of the death. The marriage and care of the child after the death must have lasted for at least five years." }
                                )
                            }
                        }
                        text(
                            bokmal { +"Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avdøde. Du kan lese mer om dette på $NAV_URL" },
                            nynorsk { +"Sjølv om du ikkje har rett til ytinga etter hovudreglane, kan du likevel ha rettar etter avdøde. Du kan lese meir om dette på $NAV_URL" },
                            english {
                                +"Even if you are not entitled to benefits in accordance with the general rules, " +
                                        "you may nevertheless have rights as a surviving spouse. You can read more about this at $NAV_URL"
                            }
                        )
                    }
                }

                showIf(alderspensjonVedVirk.uttaksgrad.equalTo(0) and avdod.gjenlevendesAlder.lessThan(67)) {
                    paragraph {
                        text(
                            bokmal { +"ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år." },
                            nynorsk { +"ha hatt omsorga for barna til den avdøde på dødsfallstidspunktet. Ekteskapet og omsorga for barnet etter dødsfallet må til saman ha vart minst fem år." },
                            english { +"have had care of the children of the deceased at the time of the death. The marriage and care of the child after the death must have lasted for at least five years." }
                        )
                    }
                }
                title2 {
                    text(
                        bokmal { +"Rettigheter hvis avdøde har bodd eller arbeidet i utlandet" },
                        nynorsk { +"Rettar når avdøde har budd eller arbeidd i utlandet" },
                        english { +"Rights if the deceased has lived or worked abroad" }
                    )
                }
                showIf(alderspensjonVedVirk.regelverkType.notEqualTo(AlderspensjonRegelverkType.AP2025)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får ubetalt i pensjon. " +
                                        "Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. " +
                                        "Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med."
                            },
                            nynorsk {
                                +"Dersom avdøde har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får ubetalt i pensjon. " +
                                        "Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. " +
                                        "Derfor kan du også ha rett til pensjon frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med."
                            },
                            english {
                                +"If the deceased has lived or worked abroad, this may affect the amount of your pension. " +
                                        "Norway has social security cooperates with a number of countries through the EEA Agreement and other social security agreements. " +
                                        "You may therefore also be entitled to a pension from other countries. " +
                                        "We can help assist you with your application to apply to countries with which Norway has a social security agreement."
                            }
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal {
                                +"Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. " +
                                        "Derfor kan du ha rett til pensjon på grunnlag av avdøde sin opptjening i andre land. " +
                                        "Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med."
                            },
                            nynorsk {
                                +"Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. " +
                                        "Derfor kan du også ha rett til pensjon på grunnlag av avdøde si pensjonsopptening frå andre land. " +
                                        "Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med."
                            },
                            english {
                                +"Norway has social security cooperation with several countries through the EEA Agreement and other agreements. " +
                                        "Therefore, you may be entitled to a pension based on the deceased’s earnings in other countries. " +
                                        "We can assist you with applications to countries with which Norway has social security agreements."
                            }
                        )
                    }
                }
                title2 {
                    text(
                        bokmal { +"Andre pensjonsordninger" },
                        nynorsk { +"Andre pensjonsordningar" },
                        english { +"Other pension schemes" }
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. " +
                                    "Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet."
                        },
                        nynorsk {
                            +"Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. " +
                                    "Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet."
                        },
                        english {
                            +"If the deceased was a member of a private or public pension scheme and you have questions about this, " +
                                    "you can contact the deceased's employer. You can also contact the pension scheme or insurance company."
                        }
                    )
                }
            }

            showIf(barn.harBarnUnder18 or sivilstand.equalTo(BorMedSivilstand.SAMBOER3_2)) {
                title2 {
                    text(
                        bokmal { +"For deg som har barn under 18 år" },
                        nynorsk { +"For deg som har barn under 18 år" },
                        english { +"If you have children under the age of 18" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. Du finner søknadsskjema og mer informasjon om dette på $NAV_URL" },
                        nynorsk { +"Forsørgjer du barn under 18 år, kan du ha rett til utvida barnetrygd. Du finn søknadsskjema og meir informasjon om dette på $NAV_URL" },
                        english { +"If you provide for children under the age of 18, you may be entitled to extended child benefit. " +
                                "You will find the application form and more information about this at $NAV_URL" }
                    )
                }
            }

            showIf(etterBetaling) {
                title2 {
                    text(
                        bokmal { +"Etterbetaling" },
                        nynorsk { +"Etterbetaling" },
                        english { +"Retroactive payment" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får etterbetalt pensjon fra " + virkFom.formatMonthYear() + ". " +
                                "Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. " +
                                "Vi kan trekke fra skatt og ytelser du har fått fra for eksempel $NAV_URL eller tjenestepensjonsordninger. " +
                                "Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. " +
                                "Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV_URL" },
                        nynorsk { +"Du får etterbetalt pensjon frå " + virkFom.formatMonthYear() + " . " +
                                "Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. " +
                                "Vi kan trekkje frå skatt og ytingar du har fått frå for eksempel $NAV_URL eller tenestepensjonsordningar. " +
                                "Etterbetalinga di kan derfor bli forseinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. " +
                                "Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV_URL" },
                        english { +"You will receive retroactive pension payments from " + virkFom.formatMonthYear() + ". " +
                                "The retroactive payments will normally be made in the course of seven working days. " +
                                "We can make deductions for tax and benefits you have received, for example, from $NAV_URL or occupational pension schemes. " +
                                "Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. " +
                                "You can check if there are any deductions from the payment notice at $DITT_NAV_URL" }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser." },
                        nynorsk { +"Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten." },
                        english { +"Retroactive If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates." }
                    )
                }
            }

            title2 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" },
                    english { +"You must notify Nav if anything changes " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om. " },
                    nynorsk { +"Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om. " },
                    english { +"If your circumstances change, you must inform Nav immediately. " +
                            "The appendix specifies which changes you are obligated to notify us of. " }
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. " +
                            "Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav." },
                    nynorsk { +"Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. " +
                            "Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav." },
                    english { +"If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. " +
                            "It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav" }
                )
            }


        }


    }

}