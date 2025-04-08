package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.FENGSEL
import no.nav.pensjon.brev.api.model.Institusjon.HELSE
import no.nav.pensjon.brev.api.model.Institusjon.SYKEHJEM
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
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggHjelpFraAndre
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggInnsynSakPensjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggInnsynSakUfoeretrygdPesysNoenDokumenter
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggInnsynSakUnder18
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggKlagePaaVedtaket
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikter
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterAFP
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterAFP1
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterAFP2
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterAFP3
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterAFP4
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT1
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT10
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT11
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT12
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT2
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT3
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT4
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT5
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT8
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggPlikterUT9
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggVeiledning
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.nettside
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.telefonnummer

// V00002 i metaforce
@TemplateModelHelpers
val vedleggOrienteringOmRettigheterOgPlikter =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterOgPlikterDto>(
        title = newText(
            Bokmal to "Dine rettigheter og plikter",
            Nynorsk to "Dine rettar og plikter",
            English to "Your rights and obligations",
        ),
        includeSakspart = false
    ) {
        val erIkkePaaInstitusjon = institusjonsoppholdGjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
        showIf(sakstype.equalTo(Sakstype.ALDER)) {
            includePhrase(VedleggPlikter)
            paragraph {
                list {
                    showIf(erIkkePaaInstitusjon) {
                        showIf(brukerBorINorge) {
                            item {
                                text(
                                    Bokmal to "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land",
                                    Nynorsk to "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land",
                                    English to "you intend to stay in a foreign country for an extended period of time or intend to move to another country"
                                )
                            }
                        }.orShow {
                            item {
                                text(
                                    Bokmal to "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland",
                                    Nynorsk to "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no",
                                    English to "you move to another country, move back to Norway or if you change address in your country of residence"
                                )
                            }
                        }
                        showIf((sivilstand.isNull() or sivilstand.isOneOf(ENKE, ENSLIG))) {
                            item {
                                text(
                                    Bokmal to "du gifter deg eller inngår samboerskap",
                                    Nynorsk to "du giftar deg eller inngår sambuarskap",
                                    English to "you marry or get a cohabiting partner",
                                )
                            }
                        }
                    }
                    showIf(borSammenMedBruker and not(epsPaInstitusjon)) {
                        showIf(sivilstand.equalTo(GIFT)) {
                            item {
                                text(
                                    Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for ektefellen din",
                                    Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til ektefellen din endrar seg",
                                    English to "the employment income, pension, disability benefit or investment income changes for your spouse",
                                )
                            }
                        }
                        showIf(sivilstand.equalTo(PARTNER)) {
                            item {
                                text(
                                    Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for partneren din",
                                    Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til partnaren din endrar seg",
                                    English to "the employment income, pension, disability benefit or investment income changes for your partner",
                                )
                            }
                        }
                        showIf((sivilstand.isOneOf(SAMBOER_1_5, SAMBOER_3_2))) {
                            item {
                                text(
                                    Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for samboeren din",
                                    Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til sambuaren din endrar seg",
                                    English to "the employment income, pension, disability benefit or investment income changes for your cohabiting partner",
                                )
                            }
                        }
                        // vedleggPlikterAP6_002
                        showIf(sivilstand.equalTo(GIFT) and erIkkePaaInstitusjon) {
                            item {
                                text(
                                    Bokmal to "du og ektefellen din flytter fra hverandre",
                                    Nynorsk to "du og ektefellen din flyttar frå kvarandre",
                                    English to "you and your spouse separate",
                                )
                            }
                        }
                        // vedleggPlikterAP14_002
                        showIf(sivilstand.equalTo(PARTNER) and erIkkePaaInstitusjon) {
                            item {
                                text(
                                    Bokmal to "du og partneren din flytter fra hverandre",
                                    Nynorsk to "du og partnaren din flyttar frå kvarandre",
                                    English to "you and your partner separate",
                                )
                            }
                        }
                        showIf(sivilstand.isOneOf(SAMBOER_1_5, SAMBOER_3_2) and erIkkePaaInstitusjon) {
                            item { // vedleggPlikterAP18_001
                                text(
                                    Bokmal to "du og samboeren din flytter fra hverandre",
                                    Nynorsk to "du og sambuaren din flyttar frå kvarandre",
                                    English to "you and your cohabiting partner separate",
                                )
                            }
                            item { // vedleggPlikterAP16_001
                                text(
                                    Bokmal to "du gifter deg",
                                    Nynorsk to "du giftar deg",
                                    English to "you marry",
                                )
                            }
                            item { // vedleggPlikterAP17_001
                                text(
                                    Bokmal to "du får barn med samboeren din",
                                    Nynorsk to "du får barn med sambuaren din",
                                    English to "you and your cohabiting partner have a child together",
                                )
                            }
                            item { // vedleggPlikterAP19_001
                                text(
                                    Bokmal to "samboeren din dør",
                                    Nynorsk to "sambuaren din døyr",
                                    English to "your cohabiting partner dies",
                                )
                            }
                        }
                    }
                    showIf(institusjonsoppholdGjeldende.notEqualTo(SYKEHJEM) and not(epsOppholdSykehjem.ifNull(false))) {
                        showIf(sivilstand.isOneOf(GLAD_EKT, SEPARERT, GIFT) and not(borSammenMedBruker)) { // vedleggPlikterAP8_001
                            item {
                                text(
                                    Bokmal to "du og ektefellen din flytter sammen igjen",
                                    Nynorsk to "du og ektefellen din flyttar saman igjen",
                                    English to "you and your spouse move back together",
                                )
                            }
                        }
                        showIf(sivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER, PARTNER) and not(borSammenMedBruker)) { //  vedleggPlikterAP11_001
                            item {
                                text(
                                    Bokmal to "du og partneren din flytter sammen igjen",
                                    Nynorsk to "du og partnaren din flyttar saman igjen",
                                    English to "you and your partner move back together",
                                )
                            }
                        }
                    }
                    showIf(sivilstand.isOneOf(GLAD_EKT, SEPARERT, GIFT, GLAD_PART, SEPARERT_PARTNER, PARTNER)) { // vedleggPlikterAP9_001
                        item {
                            text(
                                Bokmal to "du blir skilt",
                                Nynorsk to "du blir skild",
                                English to "you divorce",
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(GIFT, GLAD_EKT, SEPARERT)) { // vedleggPlikterAP7_001
                        item {
                            text(
                                Bokmal to "ektefellen din dør",
                                Nynorsk to "ektefellen din døyr",
                                English to "your spouse dies",
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER, PARTNER)) { // vedleggPlikterAP12_001
                        item {
                            text(
                                Bokmal to "partneren din dør",
                                Nynorsk to "partnaren din døyr",
                                English to "your partner dies",
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(GLAD_EKT, SEPARERT, GIFT, GLAD_PART, SEPARERT_PARTNER, PARTNER)
                            and not(borSammenMedBruker) and erIkkePaaInstitusjon and not(epsPaInstitusjon)
                    ) { // vedleggPlikterAP10_001
                        item {
                            text(
                                Bokmal to "du får ny samboer",
                                Nynorsk to "du får ny sambuar",
                                English to "you get a new cohabiting partner",
                            )
                        }
                    }
                    showIf(sivilstand.isNotAnyOf(ENSLIG, ENKE) and sivilstand.notNull() and borSammenMedBruker and erIkkePaaInstitusjon and not(epsPaInstitusjon)) {
                        // vedleggPlikterAP5_001
                        item {
                            textExpr(
                                Bokmal to "en av dere får et varig opphold i institusjon".expr(),
                                Nynorsk to "ein av dykk får et varig opphald i institusjon".expr(),
                                English to "either you or your ".expr() + sivilstand.ubestemtForm() + " get permanent residency in an institution",
                            )
                        }
                    }
                    showIf(sivilstand.isOneOf(ENSLIG, ENKE) or sivilstand.isNull() and erIkkePaaInstitusjon and brukerBorINorge) { // vedleggPlikterAP26_001
                        item {
                            text(
                                Bokmal to "du får et varig opphold i institusjon",
                                Nynorsk to "du blir innlagd på institusjon",
                                English to "you get permanent residency in an institution",
                            )
                        }
                    }
                    showIf(erIkkePaaInstitusjon and brukerBorINorge) { // vedleggPlikterAP27_001
                        item {
                            text(
                                Bokmal to "du sitter i varetekt, soner straff eller er under forvaring",
                                Nynorsk to "du sit i varetekt, sonar straff eller er under forvaring",
                                English to "you are held in detention, incarcerated or in custody",
                            )
                        }
                    }
                }
            }
            paragraph { // vedleggPlikterHvorforMeldeAP_001
                text(
                    Bokmal to "Skjer det endringer, kan det få betydning for hvor mye du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mulig.",
                    Nynorsk to "Skjer det endringar, kan det få betydning for kor mykje du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mogleg.",
                    English to "To make sure you get the right amount of retirement pension, you need to report any changes in your circumstances that can influence the assessment of the supplement you receive. It is important that you notify any change to us as soon as possible.",
                )
            }
        }
        showIf(sakstype.equalTo(Sakstype.UFOREP)) {
            title1 {
                text(
                    Bokmal to "Plikt til å opplyse om endringer - folketrygdloven § 21-3",
                    Nynorsk to "Plikt til å opplyse om endringar - folketrygdlova § 21-3",
                    English to "Duty to inform of changes - Section 21-3 of the National Insurance Act"
                )
            }
            paragraph { // TODO: Denne verkar veldig lik VedleggPlikter, og kan kanskje erstattast med den?
                text(
                    Bokmal to "Du må melde fra til Nav hvis",
                    Nynorsk to "Du må melde frå til Nav om",
                    English to "You must notify Nav if",
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
                    showIf(sivilstand.isOneOf(ENSLIG, ENKE) or sivilstand.isNull()) {
                        item { // vedleggPlikterUT6_001
                            text(
                                Bokmal to "du gifter deg eller inngår samboerskap",
                                Nynorsk to "du giftar deg eller inngår sambuarskap",
                                English to "you get married or get a cohabitant"
                            )
                        }
                    }
                    showIf(harBarnetillegg.ifNull(false)) {
                        item { // vedleggPlikterUT7_001
                            text(
                                Bokmal to "barn du forsørger får en inntekt over folketrygdens grunnbeløp, eller det skjer endringer i omsorgsituasjonen",
                                Nynorsk to "barn du forsørgjer får ei samla inntekt over grunnbeløpet i folketrygda, eller det skjer endringar av omsorgsituasjonen",
                                English to "the child(ren) in your care earn an income exceeding the National Insurance basic amount or there are changes in the care situation",
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
                    showIf(sivilstand.isOneOf(ENSLIG, ENKE) or sivilstand.isNull()) {
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