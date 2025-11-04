package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.BorI.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.garantitilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportBeregnetUtenGarantipensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.harOppfyltVedSammenlegging
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.afpPrivatResultatFellesKontoret
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.avtalelandNavn
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.borI
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.erForstegangsbehandletNorgeUtland
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.faktiskBostedsland
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.norgeBehandlendeLand
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.opplysningerBruktIBeregningenAlderspensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.opplysningerBruktIBeregningenAlderspensjonAP2025
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AP2025TidligUttakHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BilateralAvtaleHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.EOSLandAvtaleHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.GarantitilleggHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.HjemlerInnvilgelseForAP2011AP2016
import no.nav.pensjon.brev.maler.fraser.alderspensjon.HvisFlytetFaktiskBostedsland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPForeloepigBeregning
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPUttakEndr
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ReguleringAvAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilKlageUtland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SkattAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Skatteplikt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SkjermingstilleggHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SoktAFPPrivatInfo
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SupplerendeStoenadAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// MF_000098 / AP_INNV_AUTO, AP_INNVILG_AUTO, AP_INNVILG_UTL_AUTO

@TemplateModelHelpers
object InnvilgelseAvAlderspensjonAuto : AutobrevTemplate<InnvilgelseAvAlderspensjonAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_INNVILGELSE_AUTO

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - innvilgelse av alderspensjon",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = VEDTAKSBREV,
                ),
        ) {

            title {
                text(
                    bokmal { +"Vi har innvilget søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon" },
                    nynorsk { +"Vi har innvilga søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon" },
                    english { +"We have granted your application for ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension" }
                )
            }

            outline {
                includePhrase(Vedtak.Overskrift)

                paragraph {
                    text(
                        bokmal { +"Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra ".expr() + kravVirkDatoFom.format() },
                        nynorsk { +"Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå ".expr() + kravVirkDatoFom.format() },
                        english { +"You will receive ".expr() + alderspensjonVedVirk.totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom.format() }
                    )
                    showIf(alderspensjonVedVirk.uforeKombinertMedAlder and alderspensjonVedVirk.innvilgetFor67) {
                        // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                        text(
                            bokmal { +". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din." },
                            nynorsk { +". Du får alderspensjon frå folketrygda ved sida av uføretrygda di." },
                            english { +". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit." }
                        )
                    }.orShow {
                        // innvilgelseAPInnledn -> Ingen løpende uføretrygd og ingen gjenlevendetillegg
                        text(
                            bokmal { +" i alderspensjon fra folketrygden." },
                            nynorsk { +" i alderspensjon frå folketrygda." },
                            english { +" as retirement pension from the National Insurance Scheme." }
                        )
                    }
                }

                showIf(alderspensjonVedVirk.privatAFPErBrukt) { includePhrase(AfpPrivatErBrukt(alderspensjonVedVirk.uttaksgrad)) }

                showIf(afpPrivatResultatFellesKontoret.ifNull(then = false)) { includePhrase(SoktAFPPrivatInfo) }

                includePhrase(Utbetalingsinformasjon)

                showIf(harFlereBeregningsperioder and alderspensjonVedVirk.totalPensjon.greaterThan(0)) {
                    includePhrase(Felles.FlereBeregningsperioder)
                }

                showIf(alderspensjonVedVirk.uttaksgrad.lessThan(100)) {
                    paragraph {
                        text(
                            bokmal { +"Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                            nynorsk { +"Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden." },
                            english { +"You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application." }
                        )
                    }
                }

                includePhrase(
                    HvisFlytetFaktiskBostedsland(
                        eksportTrygdeavtaleAvtaleland = inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland,
                        eksportTrygdeavtaleEOS = inngangOgEksportVurdering.eksportTrygdeavtaleEOS,
                        faktiskBostedsland = faktiskBostedsland
                    )
                )

                // eksportAPunder20aar (NB! Finnes ikke i det manuelle brevet)
                showIf(
                    (regelverkType.equalTo(AP2011) and alderspensjonVedVirk.erEksportberegnet) or ((regelverkType.equalTo(AP2016)
                            and alderspensjonVedVirk.erEksportberegnet or inngangOgEksportVurdering.eksportBeregnetUtenGarantipensjon))
                ) {
                    paragraph {
                        text(
                            bokmal { +"For å ha rett til full alderspensjon når du bor i ".expr()
                                    + faktiskBostedsland + ", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon." },
                            nynorsk { +"For å ha rett til full alderspensjon når du bur i ".expr()
                                    + faktiskBostedsland + ", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon." },
                            english { +"To be eligible for a full retirement pension while living in ".expr()
                                    + faktiskBostedsland + ", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. " +
                                    "You have been a member for less than 20 years, and are therefore not eligible for a full pension." }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"I vedleggene finner du mer detaljerte opplysninger." },
                            nynorsk { +"I vedlegga finn du meir detaljerte opplysningar." },
                            english { +"There is more detailed information in the attachments." }
                        )
                    }
                }
                includePhrase(
                    HjemlerInnvilgelseForAP2011AP2016(
                        garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget,
                        godkjentYrkesskade = alderspensjonVedVirk.godkjentYrkesskade,
                        innvilgetFor67 = alderspensjonVedVirk.innvilgetFor67,
                        pensjonstilleggInnvilget = alderspensjonVedVirk.pensjonstilleggInnvilget,
                        regelverkType = regelverkType
                    )
                )
                includePhrase(SkjermingstilleggHjemmel(alderspensjonVedVirk.skjermingstilleggInnvilget))
                includePhrase(AP2025TidligUttakHjemmel(alderspensjonVedVirk.innvilgetFor67, regelverkType))
                includePhrase(GarantitilleggHjemmel(alderspensjonVedVirk.garantitilleggInnvilget))
                includePhrase(
                    EOSLandAvtaleHjemmel(
                        borINorge = borI.equalTo(NORGE),
                        eksportTrygdeavtaleEOS = inngangOgEksportVurdering.eksportTrygdeavtaleEOS,
                        erEOSLand = erEOSLand,
                        harOppfyltVedSammenlegging = inngangOgEksportVurdering.harOppfyltVedSammenlegging
                    )
                )
                ifNotNull(avtalelandNavn) { avtalelandNavn ->
                    includePhrase(
                        BilateralAvtaleHjemmel(
                            avtalelandNavn = avtalelandNavn,
                            eksportTrygdeavtaleAvtaleland = inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland,
                            erEOSLand = erEOSLand,
                            harOppfyltVedSammenlegging = inngangOgEksportVurdering.harOppfyltVedSammenlegging
                        )
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du finner informasjon om utbetalingene dine på $DITT_NAV. Her kan du også endre kontonummeret ditt." },
                        nynorsk { +"Du finn informasjon om utbetalingane dine på $DITT_NAV. Her kan du også endre kontonummeret ditt." },
                        english { +"See the more detailed information on what you will receive at $DITT_NAV. Here you can also change your bank account number." }
                    )
                }

                showIf(
                    alderspensjonVedVirk.uttaksgrad.equalTo(100) and borI.equalTo(NORGE)
                            and not(fullTrygdetid) and not(alderspensjonVedVirk.innvilgetFor67)
                ) { includePhrase(SupplerendeStoenadAP) }

                showIf(erForstegangsbehandletNorgeUtland and norgeBehandlendeLand) {
                    includePhrase(InnvilgelseAPForeloepigBeregning)
                }

                includePhrase(SkattAP)

                showIf(borI.notEqualTo(NORGE)) { includePhrase(Skatteplikt) }

                includePhrase(ReguleringAvAlderspensjon)
                includePhrase(InnvilgelseAPUttakEndr(alderspensjonVedVirk.uforeKombinertMedAlder))
                includePhrase(
                    ArbeidsinntektOgAlderspensjon(
                        innvilgetFor67 = alderspensjonVedVirk.innvilgetFor67,
                        uttaksgrad = alderspensjonVedVirk.uttaksgrad,
                        uforeKombinertMedAlder = alderspensjonVedVirk.uforeKombinertMedAlder
                    )
                )
                includePhrase(InfoPensjonFraAndreAP)
                includePhrase(MeldeFraOmEndringer)
                includePhrase(Felles.RettTilAAKlage)

                showIf(borI.equalTo(AVTALELAND)) { includePhrase(RettTilKlageUtland) }

                includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }
            includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, orienteringOmRettigheterOgPlikterDto)
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattDto)
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, maanedligPensjonFoerSkattAP2025Dto)
            includeAttachmentIfNotNull(
                vedleggOpplysningerBruktIBeregningenAlder,
                opplysningerBruktIBeregningenAlderspensjon
            )
            includeAttachmentIfNotNull(
                vedleggOpplysningerBruktIBeregningenAlderAP2025,
                opplysningerBruktIBeregningenAlderspensjonAP2025
            )
        }
}

