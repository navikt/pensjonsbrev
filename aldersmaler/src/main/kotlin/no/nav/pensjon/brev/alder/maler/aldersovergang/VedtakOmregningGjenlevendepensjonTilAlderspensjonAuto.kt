package no.nav.pensjon.brev.alder.maler.aldersovergang

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.alder.maler.felles.MeldeFraOmEndringer
import no.nav.pensjon.brev.alder.maler.felles.RettTilAAKlage
import no.nav.pensjon.brev.alder.maler.felles.RettTilInnsyn
import no.nav.pensjon.brev.alder.maler.felles.SkattAP
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.erEksportBeregnet
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AvdodSelectors.avdodFnr
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.AvdodSelectors.navn
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.BeregnetPensjonPerMaanedSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.BrukerSelectors.borINorge
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.BrukerSelectors.faktiskBostedsland
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportBeregnetUtenGarantipensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArTrygdetid
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.avdod
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.beregnetPensjonPerMaaned
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.bruker
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000143
@TemplateModelHelpers
object VedtakOmregningGjenlevendepensjonTilAlderspensjonAuto :
    AutobrevTemplate<VedtakOmregningGjenlevendepensjonTilAlderspensjonAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.VEDTAK_OMREGNING_GJP_TIL_ALDER_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - omregning av gjenlevendepensjon til alderspensjon",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            val bosattIAnnetAvtaleland = bruker.borINorge.not() and
                    alderspensjonVedVirk.erEksportBeregnet.not() and
                    inngangOgEksportVurdering.minst20ArTrygdetid.isNull() and
                    inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland.isNull() and
                    inngangOgEksportVurdering.eksportTrygdeavtaleEOS.isNull()

            val erEksportberegnet =
                (alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011) and alderspensjonVedVirk.erEksportBeregnet) or
                        (alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016) and alderspensjonVedVirk.erEksportBeregnet) or
                        inngangOgEksportVurdering.eksportBeregnetUtenGarantipensjon.equalTo(true)

            title {
                text(
                    bokmal { +"Vi har regnet om gjenlevendepensjonen din til alderspensjon" },
                    nynorsk { +"Vi har rekna om attlevandepensjonen din til alderspensjon" },
                    english { +"We have converted your survivor`s pension into retirement pension" },
                )
            }

            outline {
                title2 {
                    text(
                        bokmal { +"Vedtak" },
                        nynorsk { +"Vedtak" },
                        english { +"Decision" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Gjenlevendepensjonen din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til " + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() + "." },
                        nynorsk { +"Attlevandepensjonen din opphøyrar frå månaden etter at du fyller 67 år. Vi har derfor rekna den om til " + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon frå " + virkFom.format() + "." },
                        english { +"Your survivor`s pension will be terminated from the month after you turn 67. We have therefore converted it into " + alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension from " + virkFom.format() + "." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du får " + alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra " + virkFom.format() + " i alderspensjon fra folketrygden." },
                        nynorsk { +"Du får " + alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå " + virkFom.format() + " i alderspensjon frå folketrygda." },
                        english { +"You will receive " + alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + virkFom.format() + " as retirement pension from the National Insurance Scheme." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger." },
                        nynorsk { +"Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på nav.no/utbetalinger." },
                        english { +"If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at nav.no/utbetalingsinformasjon." },
                    )
                }

                showIf(
                    beregnetPensjonPerMaaned.antallBeregningsperioderPensjon.greaterThan(1) and alderspensjonVedVirk.totalPensjon.greaterThan(
                        0
                    )
                ) {
                    paragraph {
                        text(
                            bokmal { +"Du kan lese mer om andre beregningsperioder i vedlegget." },
                            nynorsk { +"Du kan lese meir om andre berekningsperiodar i vedlegget." },
                            english { +"There is more information about other calculation periods in the attachment." },
                        )
                    }
                }

                showIf(alderspensjonVedVirk.gjenlevenderettAnvendt and avdod.avdodFnr.notNull()) {
                    paragraph {
                        text(
                            bokmal { +"I beregningen vår har vi tatt utgangspunkt i pensjonsrettigheter du har etter " + avdod.navn + ". Dette gir deg en høyere pensjon enn om vi bare hadde tatt utgangspunkt i din egen opptjening." },
                            nynorsk { +"I berekninga vår har vi teke utgangspunkt i pensjonsrettane du har etter " + avdod.navn + ". Dette gir deg ein høgare pensjon enn om vi berre hadde teke utgangspunkt i di eiga opptening." },
                            english { +"Our calculations are based on the pension rights you are entitled to as the surviving spouse of " + avdod.navn + ". This gives you a higher pension than if we had only used the pension rights you have accumulated yourself." },
                        )
                    }
                }

                showIf(alderspensjonVedVirk.gjenlevenderettAnvendt.not() and avdod.avdodFnr.notNull()) {
                    title2 {
                        text(
                            bokmal { +"Gjenlevenderett i alderspensjon" },
                            nynorsk { +"Attlevenderett i alderspensjon" },
                            english { +"Survivor's rights in retirement pension" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter " + avdod.navn + "." },
                            nynorsk { +"I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter " + avdod.navn + "." },
                            english { +"We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after " + avdod.navn + "." },
                        )
                    }
                }

                showIf(alderspensjonVedVirk.gjenlevenderettAnvendt and avdod.avdodFnr.isNull()) {
                    paragraph {
                        text(
                            bokmal { +"I beregningen vår har vi tatt utgangspunkt i pensjonsrettigheter du har etter avdøde. Dette gir deg en høyere pensjon enn om vi bare hadde tatt utgangspunkt i din egen opptjening." },
                            nynorsk { +"I berekninga vår har vi teke utgangspunkt i pensjonsrettane du har etter den avdøde. Dette gir deg ein høgare pensjon enn om vi berre hadde teke utgangspunkt i di eiga opptening." },
                            english { +"Our calculations are based on pension rights you are entitled to as a surviving spouse. This gives you a higher pension than if we had only used the pension rights you have accumulated yourself." },
                        )
                    }
                }

                showIf(alderspensjonVedVirk.gjenlevenderettAnvendt.not() and avdod.avdodFnr.isNull()) {
                    paragraph {
                        text(
                            bokmal { +"I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter avdøde." },
                            nynorsk { +"I berekninga vår har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter den avdøde." },
                            english { +"Our calculations are based on your own accumulated pension rights. This gives you a higher pension than if we had only used the pension rights you are entitled to as a surviving spouse." },
                        )
                    }
                }

                showIf(bosattIAnnetAvtaleland) {
                    paragraph {
                        text(
                            bokmal { +"Vi forutsetter at du bor i " + bruker.faktiskBostedsland + ". Hvis du skal flytte til et annet land, må du kontakte oss slik at vi kan vurdere vurdere alderspensjonen din på nytt." },
                            nynorsk { +"Vi føreset at du bur i " + bruker.faktiskBostedsland + ". Dersom du skal flytte til eit anna land, må du kontakte oss slik at vi kan vurdere alderspensjonen din på nytt." },
                            english { +"We presume that you live in " + bruker.faktiskBostedsland + ". If you are moving to another country, it is important that you contact [_Value NAV_]. We will then reassess your retirement pension." },
                        )
                    }
                }

                showIf(erEksportberegnet) {
                    paragraph {
                        text(
                            bokmal { +"For å ha rett til full alderspensjon når du bor i " + bruker.faktiskBostedsland + ", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon." },
                            nynorsk { +"For å ha rett til full alderspensjon når du bur i " + bruker.faktiskBostedsland + ", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon." },
                            english { +"To be eligible for a full retirement pension while living in " + bruker.faktiskBostedsland + ", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. You have been a member for less than 20 years, and are therefore not eligible for a full pension." },
                        )
                    }

                    paragraph {
                        text(
                            bokmal { +"I vedleggene finner du mer detaljerte opplysninger." },
                            nynorsk { +"I vedlegga finn du meir detaljerte opplysningar." },
                            english { +"There is more detailed information in the attachments." },
                        )
                    }
                }

                showIf(
                    alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011) and
                            alderspensjonVedVirk.innvilgetFor67.notEqualTo(true) and
                            alderspensjonVedVirk.godkjentYrkesskade.notEqualTo(true)
                ) {
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8," },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8," },
                            english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8," },
                        )
                        showIf(alderspensjonVedVirk.pensjonstilleggInnvilget.equalTo(true)) {
                            text(
                                bokmal { +" 19-9," },
                                nynorsk { +" 19-9," },
                                english { +" 19-9," },
                            )
                        }
                        text(
                            bokmal { +" 19-10 og 22-12." },
                            nynorsk { +" 19-10 og 22-12." },
                            english { +" 19-10, 19-20 and 22-12 of the National Insurance Act." },
                        )
                    }
                }

                showIf(
                    alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011) and
                            alderspensjonVedVirk.innvilgetFor67.notEqualTo(true) and
                            alderspensjonVedVirk.godkjentYrkesskade.equalTo(true)
                ) {
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8," },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8," },
                            english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8," },
                        )
                        showIf(alderspensjonVedVirk.pensjonstilleggInnvilget.equalTo(true)) {
                            text(
                                bokmal { +" 19-9," },
                                nynorsk { +" 19-9," },
                                english { +" 19-9," },
                            )
                        }
                        text(
                            bokmal { +" 19-10, 19-20 og 22-12." },
                            nynorsk { +"  19-10, 19-20 og 22-12." },
                            english { +" 19-10, 19-20 and 22-12 of the National Insurance Act." },
                        )
                    }
                }

                showIf(
                    alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016) and
                            alderspensjonVedVirk.innvilgetFor67.notEqualTo(true) and
                            alderspensjonVedVirk.garantipensjonInnvilget.notEqualTo(true) and
                            alderspensjonVedVirk.godkjentYrkesskade.notEqualTo(true)
                ) {
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8," },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8," },
                            english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8," },
                        )
                        showIf(alderspensjonVedVirk.pensjonstilleggInnvilget.equalTo(true)) {
                            text(
                                bokmal { +" 19-9," },
                                nynorsk { +" 19-9," },
                                english { +" 19-9," },
                            )
                        }
                        text(
                            bokmal { +" 19-10, 19-15, 20-2, 20-3, 20-9, 20-12 til 20-14, 20-19 og 22-12." },
                            nynorsk { +" 19-10, 19-15, 20-2, 20-3, 20-9, 20-12 til 20-14, 20-19 og 22-12." },
                            english { +" 19-10, 19-15, 20-2, 20-3, 20-9, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." },
                        )
                    }
                }

                showIf(
                    alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016) and
                            alderspensjonVedVirk.innvilgetFor67.notEqualTo(true) and
                            alderspensjonVedVirk.garantipensjonInnvilget.notEqualTo(true) and
                            alderspensjonVedVirk.godkjentYrkesskade.equalTo(true)
                ) {
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8," },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8," },
                            english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8," },
                        )
                        showIf(alderspensjonVedVirk.pensjonstilleggInnvilget.equalTo(true)) {
                            text(
                                bokmal { +" 19-9," },
                                nynorsk { +" 19-9," },
                                english { +" 19-9," },
                            )
                        }
                        text(
                            bokmal { +" 19-10, 19-15, 19-20, 20-2, 20-3," },
                            nynorsk { +" 19-10, 19-15, 19-20, 20-2, 20-3," },
                            english { +" 19-10, 19-15, 19-20, 20-2, 20-3," },
                        )
                        showIf(alderspensjonVedVirk.garantipensjonInnvilget.equalTo(true)) {
                            text(
                                bokmal { +" 20-9," },
                                nynorsk { +" 20-9," },
                                english { +" 20-9," },
                            )
                        }
                        text(
                            bokmal { +" 20-12 til 20-14, 20-19 og 22-12." },
                            nynorsk { +" 20-12 til 20-14, 20-19 og 22-12." },
                            english { +" 20-12 to 20-14, 20-12, 20-19 and 22-12 of the National Insurance Act." },
                        )
                    }
                }
                showIf(alderspensjonVedVirk.garantipensjonInnvilget.equalTo(true)) {
                    paragraph {
                        text(
                            bokmal { +"Du er også innvilget garantitillegg for opptjente rettigheter etter folketrygdloven § 20-20." },
                            nynorsk { +"Du er også innvilga garantitillegg for opptente rettar etter folketrygdlova § 20-20." },
                            english { +"You have also been granted the guarantee supplement for accumulated rights pursuant to the provisions of § 20-20 of the National Insurance Act." },
                        )
                    }
                }

                showIf(
                    alderspensjonVedVirk.gjenlevenderettAnvendt and (beregnetPensjonPerManedVedVirk.gjenlevendetillegg.isNull() or beregnetPensjonPerManedVedVirk.gjenlevendetillegg.equalTo(
                        0
                    ))
                ) {
                    paragraph {
                        text(
                            bokmal { +"Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024." },
                            nynorsk { +"Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024." },
                            english { +"The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024." },
                        )
                    }
                }

                ifNotNull(beregnetPensjonPerManedVedVirk.gjenlevendetillegg) { gjenlevendetillegg ->
                    showIf(alderspensjonVedVirk.gjenlevenderettAnvendt and gjenlevendetillegg.greaterThan(0)) {
                        paragraph {
                            text(
                                bokmal { +"Gjenlevenderett er innvilget etter § 19-16 og gjenlevendetillegg etter kapittel 20 i folketrygdloven." },
                                nynorsk { +"Attlevanderett er innvilga etter § 19-16 og attlevandetillegg etter kapittel 20 i folketrygdlova." },
                                english { +"The survivor's rights in your retirement pension and the survivor's supplement have been granted pursuant to the provisions of § 19-16 and Chapter 20 of the National Insurance Act." },
                            )
                        }
                    }
                }

                includePhrase(SkattAP)
                showIf(bruker.borINorge.not()) {
                    paragraph {
                        text(
                            bokmal { +"Spørsmål om skatteplikt til Norge etter flytting til utlandet må rettes til skatteetaten. Du må selv avklare spørsmål om skatteplikt til det landet du bor i med skattemyndighetene der." },
                            nynorsk { +"Spørsmål om skatteplikt til Noreg etter flytting til utlandet på rettast til skatteetaten. Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i med skatteorgana der." },
                            english { +"Questions about tax liability to Norway after moving abroad must be directed to the Tax Administration. You must clarify questions about tax liability to your country of residence with the local tax authorities." },
                        )
                    }
                }

                title2 {
                    text(
                        bokmal { +"Alderspensjonen din reguleres årlig" },
                        nynorsk { +"Alderspensjonen din blir regulert årleg" },
                        english { +"Your retirement pension will be adjusted annually" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldingen din. På " + Constants.NAV_URL + " kan du lese mer om hvordan pensjonene reguleres." },
                        nynorsk { +"Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldinga di. På " + Constants.NAV_URL + " kan du lese meir om korleis pensjonane blir regulerte." },
                        english { +"The pension amount will be adjusted with effect from 1 May, and the actual increase is usually paid retroactively in June. You will be informed about this on your payout notice. You can read more about how pensions are adjusted at " + Constants.NAV_URL + "." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Du kan søke om å endre pensjonen din" },
                        nynorsk { +"Du kan søkje om å endre pensjonen din" },
                        english { +"You can apply to change your pension" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan ha mulighet til å ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon. Etter at du har begynt å ta ut pensjonen din, kan du gjøre endringer med 12 måneders mellomrom. Du kan alltid stanse pensjonen." },
                        nynorsk { +"Du kan ha høve til å ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon. Etter at du har begynt å ta ut pensjonen din, kan du gjere endringar med tolv månaders mellomrom. Du kan alltid stanse pensjonen." },
                        english { +"You are entitled to draw retirement pension at a rate of 20, 40, 50, 60, 80 or 100 percent. Once you have started drawing your pension, you can make changes at 12-monthly intervals. You can stop drawing your pension at any time." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"For å gjøre endringene kan du bruke pensjonskalkulatoren på " + Constants.DIN_PENSJON_URL + "." },
                        nynorsk { +"For å gjere endringar kan du bruke pensjonskalkulatoren på " + Constants.DIN_PENSJON_URL + "." },
                        english { +"To make changes you can use the pension calculator at " + Constants.DIN_PENSJON_URL + "." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Arbeidsinntekt og alderspensjon" },
                        nynorsk { +"Arbeidsinntekt og alderspensjon" },
                        english { +"Earned income and retirement pension" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker." },
                        nynorsk { +"Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar." },
                        english { +"You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig." },
                        nynorsk { +"Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig." },
                        english { +"If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"AAndre pensjonsordninger" },
                        nynorsk { +"Andre pensjonsordningar" },
                        english { +"Other pension schemes" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Mange er tilknyttet en eller flere offentlige eller private pensjonsordninger som de har pensjonsrettigheter fra. Du bør kontakte de du har slike ordninger med for å undersøke hvilke rettigheter du kan ha. Du kan også undersøke med siste arbeidsgiver." },
                        nynorsk { +"Mange er knytte til ei eller fleire offentlege eller private pensjonsordningar som de har pensjonsrettar frå. Du bør kontakte dei du har slike ordningar med for å undersøke kva for rettar du har. Du kan også undersøkje med siste arbeidsgivar." },
                        english { +"Many people are also members of one or more public or private pension schemes where they also have pension rights. You must contact the company/ies you have pension arrangements with, if you have any questions about this. You can also contact your most recent employer." },
                    )
                }

                includePhrase(MeldeFraOmEndringer)
                includePhrase(RettTilAAKlage)
                includePhrase(RettTilInnsyn(vedlegg = vedleggOrienteringOmRettigheterOgPlikter))
                includePhrase(HarDuSpoersmaalAlder)
            }

            includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, orienteringOmRettigheterOgPlikterDto)
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattDto)
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, maanedligPensjonFoerSkattAP2025Dto)
        }
}
