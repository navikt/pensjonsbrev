package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.borSammenMedBruker
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.brukerUnder18Aar
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.epsOppholdSykehjem
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.epsPaInstitusjon
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.harBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.sakstype
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDtoSelectors.sivilstand
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.nettside
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.telefonnummer

// V00002 i metaforce
// Hvis sivilstand mangler, map det til UKJENT
@TemplateModelHelpers
val vedleggOrienteringOmRettigheterOgPlikter =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterOgPlikterDto>(
        title = {
            text(
                bokmal { +"Dine rettigheter og plikter" },
                nynorsk { +"Dine rettar og plikter" },
                english { +"Your rights and obligations" },
            )
        },
        includeSakspart = false
    ) {
        val erIkkePaaInstitusjon = institusjonsoppholdGjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
        val borSammenMedBrukerAndNotEpsPaInstitusjon = borSammenMedBruker and not(epsPaInstitusjon)
        val samboer = sivilstand.isOneOf(SAMBOER_1_5, SAMBOER_3_2)
        val erEllerHarVaertEktefelleEllerPartner = sivilstand.isOneOf(GLAD_EKT, SEPARERT, GIFT, GLAD_PART, SEPARERT_PARTNER, PARTNER)

        showIf(sakstype.equalTo(Sakstype.ALDER)) {
            includePhrase(VedleggPlikterTittel)
            // Denne showIf-en skal dekke alle scenarie der det kommer minst ett kulepunkt i lista under
            showIf(
                erIkkePaaInstitusjon or
                        (borSammenMedBrukerAndNotEpsPaInstitusjon and samboer) or
                        erEllerHarVaertEktefelleEllerPartner
            ) {
                includePhrase(VedleggPlikter)
            }

            paragraph {
                list {
                    showIf(erIkkePaaInstitusjon) {
                        showIf(brukerBorINorge) {
                            item {
                                text(
                                    bokmal { + "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land" },
                                    nynorsk { + "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land" },
                                    english { + "you intend to stay in a foreign country for an extended period of time or intend to move to another country" }
                                )
                            }
                        }.orShow {
                            item {
                                text(
                                    bokmal { + "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland" },
                                    nynorsk { + "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no" },
                                    english { + "you move to another country, move back to Norway or if you change address in your country of residence" }
                                )
                            }
                        }
                        showIf(sivilstand.isOneOf(ENKE, ENSLIG, UKJENT)) {
                            item {
                                text(
                                    bokmal { + "du gifter deg eller inngår samboerskap" },
                                    nynorsk { + "du giftar deg eller inngår sambuarskap" },
                                    english { + "you marry or get a cohabiting partner" },
                                )
                            }
                        }
                    }
                    showIf(borSammenMedBrukerAndNotEpsPaInstitusjon) {
                        showIf(sivilstand.equalTo(GIFT)) {
                            item {
                                text(
                                    bokmal { + "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for ektefellen din" },
                                    nynorsk { + "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til ektefellen din endrar seg" },
                                    english { + "the employment income, pension, disability benefit or investment income changes for your spouse" },
                                )
                            }
                        }
                        showIf(sivilstand.equalTo(PARTNER)) {
                            item {
                                text(
                                    bokmal { + "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for partneren din" },
                                    nynorsk { + "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til partnaren din endrar seg" },
                                    english { + "the employment income, pension, disability benefit or investment income changes for your partner" },
                                )
                            }
                        }
                        showIf(samboer) {
                            item {
                                text(
                                    bokmal { + "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for samboeren din" },
                                    nynorsk { + "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til sambuaren din endrar seg" },
                                    english { + "the employment income, pension, disability benefit or investment income changes for your cohabiting partner" },
                                )
                            }
                        }
                        // vedleggPlikterAP6_002
                        showIf(sivilstand.equalTo(GIFT) and erIkkePaaInstitusjon) {
                            item {
                                text(
                                    bokmal { + "du og ektefellen din flytter fra hverandre" },
                                    nynorsk { + "du og ektefellen din flyttar frå kvarandre" },
                                    english { + "you and your spouse separate" },
                                )
                            }
                        }
                        // vedleggPlikterAP14_002
                        showIf(sivilstand.equalTo(PARTNER) and erIkkePaaInstitusjon) {
                            item {
                                text(
                                    bokmal { + "du og partneren din flytter fra hverandre" },
                                    nynorsk { + "du og partnaren din flyttar frå kvarandre" },
                                    english { + "you and your partner separate" },
                                )
                            }
                        }
                        showIf(samboer and erIkkePaaInstitusjon) {
                            item { // vedleggPlikterAP18_001
                                text(
                                    bokmal { + "du og samboeren din flytter fra hverandre" },
                                    nynorsk { + "du og sambuaren din flyttar frå kvarandre" },
                                    english { + "you and your cohabiting partner separate" },
                                )
                            }
                            item { // vedleggPlikterAP16_001
                                text(
                                    bokmal { + "du gifter deg" },
                                    nynorsk { + "du giftar deg" },
                                    english { + "you marry" },
                                )
                            }
                            item { // vedleggPlikterAP17_001
                                text(
                                    bokmal { + "du får barn med samboeren din" },
                                    nynorsk { + "du får barn med sambuaren din" },
                                    english { + "you and your cohabiting partner have a child together" },
                                )
                            }
                            item { // vedleggPlikterAP19_001
                                text(
                                    bokmal { + "samboeren din dør" },
                                    nynorsk { + "sambuaren din døyr" },
                                    english { + "your cohabiting partner dies" },
                                )
                            }
                        }
                    }
                    showIf(institusjonsoppholdGjeldende.notEqualTo(SYKEHJEM) and not(epsOppholdSykehjem.ifNull(false))) {
                        showIf(sivilstand.isOneOf(GLAD_EKT, SEPARERT, GIFT) and not(borSammenMedBruker)) { // vedleggPlikterAP8_001
                            item {
                                text(
                                    bokmal { + "du og ektefellen din flytter sammen igjen" },
                                    nynorsk { + "du og ektefellen din flyttar saman igjen" },
                                    english { + "you and your spouse move back together" },
                                )
                            }
                        }
                        showIf(sivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER, PARTNER) and not(borSammenMedBruker)) { //  vedleggPlikterAP11_001
                            item {
                                text(
                                    bokmal { + "du og partneren din flytter sammen igjen" },
                                    nynorsk { + "du og partnaren din flyttar saman igjen" },
                                    english { + "you and your partner move back together" },
                                )
                            }
                        }
                    }
                    showIf(erEllerHarVaertEktefelleEllerPartner) { // vedleggPlikterAP9_001
                        item {
                            text(
                                bokmal { + "du blir skilt" },
                                nynorsk { + "du blir skild" },
                                english { + "you divorce" },
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(GIFT, GLAD_EKT, SEPARERT)) { // vedleggPlikterAP7_001
                        item {
                            text(
                                bokmal { + "ektefellen din dør" },
                                nynorsk { + "ektefellen din døyr" },
                                english { + "your spouse dies" },
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER, PARTNER)) { // vedleggPlikterAP12_001
                        item {
                            text(
                                bokmal { + "partneren din dør" },
                                nynorsk { + "partnaren din døyr" },
                                english { + "your partner dies" },
                            )
                        }
                    }
                    showIf(
                        erEllerHarVaertEktefelleEllerPartner
                            and not(borSammenMedBruker) and erIkkePaaInstitusjon and not(epsPaInstitusjon)
                    ) { // vedleggPlikterAP10_001
                        item {
                            text(
                                bokmal { + "du får ny samboer" },
                                nynorsk { + "du får ny sambuar" },
                                english { + "you get a new cohabiting partner" },
                            )
                        }
                    }
                    showIf(sivilstand.isNotAnyOf(ENSLIG, ENKE, UKJENT) and borSammenMedBruker and erIkkePaaInstitusjon and not(epsPaInstitusjon)) {
                        // vedleggPlikterAP5_001
                        item {
                            text(
                                bokmal { + "en av dere får et varig opphold i institusjon" },
                                nynorsk { + "ein av dykk får et varig opphald i institusjon" },
                                english { + "either you or your " + sivilstand.ubestemtForm() + " get permanent residency in an institution" },
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(ENSLIG, ENKE, UKJENT) and erIkkePaaInstitusjon and brukerBorINorge) { // vedleggPlikterAP26_001
                        item {
                            text(
                                bokmal { + "du får et varig opphold i institusjon" },
                                nynorsk { + "du blir innlagd på institusjon" },
                                english { + "you get permanent residency in an institution" },
                            )
                        }
                    }
                    showIf(erIkkePaaInstitusjon and brukerBorINorge) { // vedleggPlikterAP27_001
                        item {
                            text(
                                bokmal { + "du sitter i varetekt, soner straff eller er under forvaring" },
                                nynorsk { + "du sit i varetekt, sonar straff eller er under forvaring" },
                                english { + "you are held in detention, incarcerated or in custody" },
                            )
                        }
                    }
                }
            }
            paragraph { // vedleggPlikterHvorforMeldeAP_001
                text(
                    bokmal { + "Skjer det endringer, kan det få betydning for hvor mye du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mulig." },
                    nynorsk { + "Skjer det endringar, kan det få betydning for kor mykje du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mogleg." },
                    english { + "To make sure you get the right amount of retirement pension, you need to report any changes in your circumstances that can influence the assessment of the supplement you receive. It is important that you notify any change to us as soon as possible." },
                )
            }
        }
        showIf(sakstype.equalTo(Sakstype.UFOREP)) {
            title1 {
                text(
                    bokmal { + "Plikt til å opplyse om endringer - folketrygdloven § 21-3" },
                    nynorsk { + "Plikt til å opplyse om endringar - folketrygdlova § 21-3" },
                    english { + "Duty to inform of changes - Section 21-3 of the National Insurance Act" }
                )
            }
            paragraph { // TODO: Denne verkar veldig lik VedleggPlikter, og kan kanskje erstattast med den?
                text(
                    bokmal { +"Du må melde fra til Nav hvis" },
                    nynorsk { +"Du må melde frå til Nav om" },
                    english { +"You must notify Nav if" },
                )
            }
            paragraph {
                list {
                    item {
                        includePhrase(VedleggPlikterUT1)
                    }
                    item {
                        includePhrase(VedleggPlikterUT2)
                    }
                    showIf(brukerBorINorge and erIkkePaaInstitusjon) {
                        item {
                            includePhrase(VedleggPlikterUT3)
                        }
                        item {
                            includePhrase(VedleggPlikterUT4)
                        }
                    }
                    item {
                        includePhrase(VedleggPlikterUT5)
                    }
                    showIf(sivilstand.isOneOf(ENSLIG, ENKE, UKJENT)) {
                        item { // vedleggPlikterUT6_001
                            text(
                                bokmal { + "du gifter deg eller inngår samboerskap" },
                                nynorsk { + "du giftar deg eller inngår sambuarskap" },
                                english { + "you get married or get a cohabitant" }
                            )
                        }
                    }
                    showIf(harBarnetillegg.notNull()) {
                        item { // vedleggPlikterUT7_001
                            text(
                                bokmal { + "barn du forsørger får en inntekt over folketrygdens grunnbeløp, eller det skjer endringer i omsorgsituasjonen" },
                                nynorsk { + "barn du forsørgjer får ei samla inntekt over grunnbeløpet i folketrygda, eller det skjer endringar av omsorgsituasjonen" },
                                english { + "the child(ren) in your care earn an income exceeding the National Insurance basic amount or there are changes in the care situation" },
                            )
                        }
                    }
                    item {
                        includePhrase(VedleggPlikterUT8)
                    }
                    item {
                        includePhrase(VedleggPlikterUT9)
                    }
                    item {
                        includePhrase(VedleggPlikterUT10)
                    }
                    item {
                        includePhrase(VedleggPlikterUT11)
                    }
                    item {
                        includePhrase(VedleggPlikterUT12)
                    }
                }
            }
        }
        showIf(sakstype.equalTo(Sakstype.AFP)) {
            includePhrase(VedleggPlikterAFP)
            paragraph {
                list {
                    item { includePhrase(VedleggPlikterAFP1) }
                    showIf(sivilstand.isOneOf(ENSLIG, ENKE, UKJENT)) {
                        item { includePhrase(VedleggPlikterAFP2) }
                    }
                    showIf(brukerBorINorge and erIkkePaaInstitusjon) {
                        item {
                            includePhrase(VedleggPlikterAFP3)
                        }
                    }
                    showIf(not(brukerBorINorge) and erIkkePaaInstitusjon) {
                        item {
                            includePhrase(VedleggPlikterAFP4)
                        }
                    }
                }
            }
        }
        includePhrase(VedleggVeiledning)
        showIf(sakstype.notEqualTo(Sakstype.UFOREP)) {
            includePhrase(VedleggInnsynSakPensjon(felles.avsenderEnhet.telefonnummer, felles.avsenderEnhet.nettside))
        }
        showIf(sakstype.equalTo(Sakstype.BARNEP) and brukerUnder18Aar.ifNull(false)) {
            includePhrase(VedleggInnsynSakUnder18)
        }
        showIf(sakstype.equalTo(Sakstype.UFOREP)) {
            includePhrase(VedleggInnsynSakUfoeretrygdPesysNoenDokumenter)
        }
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }