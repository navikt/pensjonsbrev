package no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.grunnpensjonSats
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabell
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.avdodFlyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.beregnetEtter_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.grunnpensjon
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

private object GrunnpensjonBold : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Grunnpensjon ",
            Nynorsk to "Grunnpensjon ",
            English to "The basic pension ",
            FontType.BOLD
        )
    }
}

data class MaanedligPensjonFoerSkattGrunnpensjon(
    val beregnetPensjonPerManedGjeldende: Expression<MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed>,
    val epsGjeldende: Expression<MaanedligPensjonFoerSkattDto.EPSgjeldende?>,
    val alderspensjonGjeldende: Expression<MaanedligPensjonFoerSkattDto.AlderspensjonGjeldende>,
    val institusjonsoppholdGjeldende: Expression<MaanedligPensjonFoerSkattDto.InstitusjonsoppholdGjeldende?>,
    val bruker: Expression<MaanedligPensjonFoerSkattDto.Bruker>,
    val beregnetSomEnsligPgaInstopphold: Expression<Boolean>,
): OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val epsBorSammenMedBruker = epsGjeldende.borSammenMedBruker_safe.ifNull(false)
        val epsMottarPensjon = epsGjeldende.mottarPensjon_safe.ifNull(false)
        val epsHarInntektOver2G = epsGjeldende.harInntektOver2G_safe.ifNull(false)

        val beregnetEtterEgen =
            beregnetPensjonPerManedGjeldende.beregnetEtter_safe.ifNull(AlderspensjonBeregnetEtter.AVDOD)
                .isOneOf(AlderspensjonBeregnetEtter.EGEN)
        val beregnetEtterAvdod =
            beregnetPensjonPerManedGjeldende.beregnetEtter_safe.ifNull(AlderspensjonBeregnetEtter.EGEN)
                .isOneOf(AlderspensjonBeregnetEtter.AVDOD)

        val grunnbeloep = beregnetPensjonPerManedGjeldende.grunnbeloep.format()

        val grunnpensjonSats = alderspensjonGjeldende.grunnpensjonSats.format()
        ifNotNull(
            beregnetPensjonPerManedGjeldende.grunnpensjon
        ) { grunnpensjon ->
            showIf(beregnetPensjonPerManedGjeldende.fullTrygdetid and beregnetEtterEgen) {
                showIf(alderspensjonGjeldende.regelverkstype.isOneOf(AP1967, AP2011, AP2016)) {
                    paragraph {
                        text(
                            Bokmal to "Grunnpensjon ",
                            Nynorsk to "Grunnpensjon ",
                            English to "The basic pension ",
                            FontType.BOLD
                        )
                        textExpr(
                            Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep + " kroner.",
                            Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep + " kroner.",
                            English to "is calculated on the basis of the National Insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep + ".",
                        )
                    }
                }


                showIf(
                    bruker.sivilstand.isOneOf(
                        ENSLIG,
                        ENKE,
                        GLAD_EKT,
                        GLAD_PART,
                        SEPARERT,
                        SEPARERT_PARTNER,
                    ) or (bruker.sivilstand.isOneOf(GIFT, PARTNER) and beregnetSomEnsligPgaInstopphold)
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får full grunnpensjon fordi du er enslig pensjonist. Det utgjør ".expr() + grunnpensjonSats + " prosent av grunnbeløpet.",
                            Nynorsk to "Du får full grunnpensjon fordi du er einsleg pensjonist. Det utgjer ".expr() + grunnpensjonSats + " prosent av grunnbeløpet.",
                            English to "As a single pensioner you will receive full basic pension. This is equivalent to ".expr() + grunnpensjonSats + " percent of the National Insurance basic amount.",
                        )
                    }
                }

                showIf(
                    epsMottarPensjon
                            and epsBorSammenMedBruker
                            and not(beregnetSomEnsligPgaInstopphold)
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "Grunnpensjonen er justert til ".expr() +
                                    grunnpensjonSats + " prosent av beløpet fordi " +
                                    bruker.sivilstand.bestemtForm() + " din mottar uføretrygd, pensjon eller omstillingsstønad fra folketrygden eller AFP som det godskrives pensjonspoeng for.",

                            Nynorsk to "Grunnpensjonen er justert til ".expr() +
                                    grunnpensjonSats + " prosent av beløpet fordi " +
                                    bruker.sivilstand.bestemtForm() + " din mottar uføretrygd, pensjon eller omstillingsstønad frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",

                            English to "The basic pension is adjusted to ".expr() +
                                    grunnpensjonSats + " percent of this amount because your " +
                                    bruker.sivilstand.bestemtForm() + " is receiving disability benefit, a national insurance pension or adjustment allowance, or contractual early retirement pension (AFP) which earns pension points.",
                        )
                    }
                }

                showIf(epsBorSammenMedBruker and epsHarInntektOver2G and not(epsMottarPensjon)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Grunnpensjonen er justert til ".expr() +
                                    grunnpensjonSats +
                                    " prosent av beløpet fordi " + bruker.sivilstand.bestemtForm() +
                                    " din har årlig inntekt over to ganger grunnbeløpet.",

                            Nynorsk to "Grunnpensjonen er justert til ".expr() +
                                    grunnpensjonSats +
                                    " prosent av beløpet fordi " + bruker.sivilstand.bestemtForm() +
                                    " din har årleg inntekt over to gonger grunnbeløpet.",

                            English to "The basic pension is adjusted to ".expr() +
                                    grunnpensjonSats +
                                    " percent of this amount because your " + bruker.sivilstand.bestemtForm() +
                                    " has an annual income that exceeds twice the national insurance basic amount.",
                        )
                    }
                }

                showIf(
                    epsBorSammenMedBruker
                            and not(epsHarInntektOver2G)
                            and not(epsMottarPensjon)
                            and not(institusjonsoppholdGjeldende.aldersEllerSykehjem_safe.ifNull(false))
                            and not(institusjonsoppholdGjeldende.helseinstitusjon_safe.ifNull(false))
                            and not(institusjonsoppholdGjeldende.fengsel_safe.ifNull(false))
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får full grunnpensjon fordi ".expr() + bruker.sivilstand.bestemtForm() +
                                    " din har årlig inntekt under to ganger grunnbeløpet. Det utgjør " + grunnpensjonSats +
                                    " prosent av grunnbeløpet.",

                            Nynorsk to "Du får full grunnpensjon fordi ".expr() + bruker.sivilstand.bestemtForm() +
                                    " din har årleg inntekt lågare enn to gonger grunnbeløpet. Det utgjer " + grunnpensjonSats +
                                    " prosent av grunnbeløpet.",

                            English to "You will receive full basic pension because the annual income of your ".expr() + bruker.sivilstand.bestemtForm() +
                                    " is lower than twice the national insurance basic amount. This is equivalent to " + grunnpensjonSats +
                                    " percent of the national insurance basic amount.",
                        )
                    }
                }
            }
        }
        // Text 68 vedleggBelopGPFlyktning_001
        showIf(
            beregnetPensjonPerManedGjeldende.flyktningstatusErBrukt
                    and (alderspensjonGjeldende.regelverkstype.isOneOf(AP2011, AP2016))
                    and beregnetEtterEgen
                    and beregnetPensjonPerManedGjeldende.fullTrygdetid
        ) {
            paragraph {
                text(
                    Bokmal to "Som flyktning får du grunnpensjonen din beregnet som om du har full trygdetid i Norge. Vær oppmerksom på at alderspensjonen din vil bli omregnet etter faktisk trygdetid dersom du flytter til et land utenfor EØS-området.",
                    Nynorsk to "Som flyktning får du grunnpensjonen din rekna ut som om du har full trygdetid i Noreg. Ver merksam på at alderspensjonen din vil bli omrekna etter faktisk trygdetid dersom du flyttar til eit land utanfor EØS-området.",
                    English to "As a refugee, your basic pension is calculated as if you had a full period of national insurance cover in Norway. Please note that your retirement pension will be recalculated according to your actual period of national insurance cover if you move to a country outside the EEA region.",
                )
            }
        }

        showIf(
            beregnetEtterAvdod
                    and beregnetPensjonPerManedGjeldende.fullTrygdetid
                    and (alderspensjonGjeldende.regelverkstype.isOneOf(AP1967, AP2011, AP2016))
        ) {
            showIf(beregnetEtterAvdod and not(bruker.sivilstand.isOneOf(SAMBOER_3_2))) {
                paragraph {
                    includePhrase(GrunnpensjonBold)
                    textExpr(
                        Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg.",

                        Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg.",

                        English to "is calculated on the basis of the National Insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                ". When you have rights as a surviving spouse the basic pension can be calculated on the basis of the pensioner’s own or the deceased’s period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance cover as this is more beneficial for you.",
                    )
                }
            }

            showIf(bruker.sivilstand.isOneOf(SAMBOER_3_2) and epsMottarPensjon and beregnetEtterAvdod) {
                paragraph {
                    includePhrase(GrunnpensjonBold)
                    textExpr(
                        Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er justert til " + grunnpensjonSats +
                                " prosent av beløpet fordi samboeren din mottar uføretrygd, pensjon eller omstillingsstønad fra folketrygden eller AFP som det godskrives pensjonspoeng for.",

                        Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er justert til " + grunnpensjonSats +
                                " prosent av beløpet fordi sambuaren din mottar uføretrygd, pensjon eller omstillingsstønad frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",

                        English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                ". The basic pension for a widowed old age pensioner can be calculated on the basis of the pensioner's own or the deceased's period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance coverage as this is more beneficial for you. The basic pension is adjusted to " + grunnpensjonSats +
                                " percent of this amount because your cohabitant is receiving disability benefit, a national insurance pension or adjustment allowance, or contractual early retirement pension (AFP) which earns pension points.",
                    )
                }
            }

            showIf(
                bruker.sivilstand.isOneOf(SAMBOER_3_2)
                        and epsHarInntektOver2G
                        and beregnetEtterAvdod
                        and not(epsMottarPensjon)
            ) {
                paragraph {
                    includePhrase(GrunnpensjonBold)
                    textExpr(
                        Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. " +
                                "Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er justert til " + grunnpensjonSats +
                                " prosent av beløpet fordi samboeren din har årlig inntekt over to ganger grunnbeløpet.",

                        Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. " +
                                "Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er justert til " + grunnpensjonSats +
                                " prosent av beløpet fordi sambuaren din har årleg inntekt over to gonger grunnbeløpet.",

                        English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                ". The basic pension for a widowed old age pensioner can be calculated on the basis of the pensioner's own or the deceased's period of national insurance coverage. " +
                                "Your basic pension has been calculated on the basis of the deceased's period of national insurance coverage as this is more beneficial for you. The basic pension is adjusted to " + grunnpensjonSats +
                                " percent of this amount because your cohabitant has an annual income that exceeds twice the national insurance basic amount.",
                    )
                }
            }

            showIf(
                bruker.sivilstand.isOneOf(SAMBOER_3_2)
                        and not(epsHarInntektOver2G)
                        and not(epsMottarPensjon)
                        and beregnetEtterAvdod
            ) {
                paragraph {
                    includePhrase(GrunnpensjonBold)
                    textExpr(
                        Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er justert til " + grunnpensjonSats +
                                " prosent av beløpet fordi samboeren din har årlig inntekt under to ganger grunnbeløpet.",
                        Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                " kroner. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er justert til " + grunnpensjonSats +
                                " prosent av beløpet fordi sambuaren din har årleg inntekt lågare enn to gonger grunnbeløpet.",
                        English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                ". When you have rights as a surviving spouse the basic pension can be calculated on the basis of the pensioner’s own or the deceased’s period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance cover as this is more beneficial for you. The basic pension is adjusted to " + grunnpensjonSats +
                                " percent of this amount because the annual income of your cohabitant is lower than twice the national insurance basic amount.",
                    )
                }
            }

        }

        showIf(
            beregnetPensjonPerManedGjeldende.avdodFlyktningstatusErBrukt
                    and beregnetEtterAvdod
                    and beregnetPensjonPerManedGjeldende.fullTrygdetid
                    and alderspensjonGjeldende.regelverkstype.isOneOf(AP1967, AP2011, AP2016)
        ) {
            paragraph {
                text(
                    Bokmal to "Avdødes trygdetid er beregnet etter reglene for flyktninger. Vær oppmerksom på at alderspensjonen din vil bli omregnet etter faktisk trygdetid dersom du flytter til et land utenfor EØS-området.",
                    Nynorsk to "Trygdetida til avdøde er rekna ut etter reglane for flyktning. Ver merksam på at alderspensjonen din vil bli omrekna etter faktisk trygdetid dersom du flyttar til eit land utanfor EØS-området.",
                    English to "The deceased's period of national insurance coverage has been determined on the basis of the regulations for refugees. Please note that your retirement pension will be recalculated according to the actual period of national insurance cover if you move to a country outside the EEA region.",
                )
            }
        }

        showIf(
            not(beregnetPensjonPerManedGjeldende.fullTrygdetid)
                    and (alderspensjonGjeldende.regelverkstype.isOneOf(AP1967)
                    or (
                    alderspensjonGjeldende.regelverkstype.isOneOf(AP2011, AP2016)
                            and beregnetEtterEgen
                            and beregnetPensjonPerManedGjeldende.grunnpensjon.notNull()
                    ))
        ) {
            showIf(alderspensjonGjeldende.erEksportberegnet) {
                paragraph {
                    includePhrase(GrunnpensjonBold)

                    textExpr(
                        Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                " kroner. Fordi du ikke bor i Norge og har mindre enn 20 års medlemstid, er grunnpensjonen beregnet etter antall år med pensjonspoeng.",

                        Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                " kroner. Fordi du ikkje bur i Noreg og har mindre enn 20 års medlemstid, er grunnpensjonen berekna etter talet på år med pensjonspoeng.",

                        English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                ". Because you live outside of Norway and have less than 20 years of national insurance coverage, the basic pension is calculated by using your number of pension point earning years.",
                    )
                }
            }.orShow {
                paragraph {
                    includePhrase(GrunnpensjonBold)
                    textExpr(
                        Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                " kroner. Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Fordi du har mindre enn 40 års trygdetid, vil grunnpensjonen reduseres tilsvarende.",

                        Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                " kroner. Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Fordi du har mindre enn 40 års trygdetid, vil grunnpensjonen bli redusert tilsvarande.",

                        English to "is calculated on the basis of the National Insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                ". Your period of national insurance cover is the years that you lived and/or worked in Norway after the age of 16. Your basic pension has been reduced proportionately because you have less than 40 years of national insurance cover.",
                    )
                }
            }

            showIf(epsMottarPensjon and epsBorSammenMedBruker and not(beregnetSomEnsligPgaInstopphold)) {
                paragraph {
                    textExpr(
                        Bokmal to "Videre er grunnpensjonen justert til ".expr() + grunnpensjonSats +
                                " prosent av beløpet fordi " + bruker.sivilstand.bestemtForm() +
                                " din mottar uføretrygd, pensjon eller omstillingsstønad fra folketrygden eller AFP som det godskrives pensjonspoeng for.",

                        Nynorsk to "Vidare er grunnpensjonen justert til ".expr() + grunnpensjonSats +
                                " prosent av beløpet fordi " + bruker.sivilstand.bestemtForm() +
                                " din mottar uføretrygd, pensjon eller omstillingsstønad frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",

                        English to "The basic pension has also been adjusted to ".expr() + grunnpensjonSats +
                                " percent of the reduced amount because your " + bruker.sivilstand.bestemtForm() +
                                " is receiving disability benefit, a national insurance pension or adjustment allowance, or contractual early retirement pension (AFP) which earns pension points.",
                    )
                }
            }

            showIf(
                epsBorSammenMedBruker
                        and epsHarInntektOver2G
                        and not(epsMottarPensjon)
                        and not(institusjonsoppholdGjeldende.ensligPaInst_safe.ifNull(false))
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Videre er grunnpensjonen justert til ".expr() + grunnpensjonSats +
                                " prosent av beløpet fordi " + bruker.sivilstand.bestemtForm() +
                                " din har årlig inntekt over to ganger grunnbeløpet.",
                        Nynorsk to "Vidare er grunnpensjonen justert til ".expr() + grunnpensjonSats +
                                " prosent av beløpet fordi " + bruker.sivilstand.bestemtForm() +
                                " din har årleg inntekt over to gonger grunnbeløpet.",
                        English to "The basic pension has also been adjusted to ".expr() + grunnpensjonSats +
                                " percent of the reduced amount because your " + bruker.sivilstand.bestemtForm() +
                                " has an annual income that exceeds twice the national insurance basic amount (G).",
                    )
                }
            }

        }

        showIf(
            not(beregnetPensjonPerManedGjeldende.fullTrygdetid)
                    and beregnetEtterAvdod
                    and alderspensjonGjeldende.regelverkstype.isOneOf(AP1967, AP2011, AP2016)
        ) {

            paragraph {
                includePhrase(GrunnpensjonBold)
                textExpr(
                    Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                            " kroner. Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid.",

                    Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                            " kroner. Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdøde si trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid.",

                    English to "is calculated on the basis of the National Insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                            ". Your period of national insurance cover is the years that you lived and/or worked in Norway after the age of 16. When you have rights as a surviving spouse the basic pension can be calculated on the basis of the pensioner’s own or the deceased’s period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance cover as this is more beneficial for you. The deceased's period of national insurance cover was less than 40 years meaning your basic pension will be reduced proportionately.",
                )
            }

            showIf(bruker.sivilstand.isOneOf(SAMBOER_3_2)) {
                showIf(epsMottarPensjon) {
                    paragraph {
                        includePhrase(GrunnpensjonBold)
                        textExpr(
                            Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() +
                                    grunnbeloep +
                                    " kroner. Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid. Videre er grunnpensjonen justert til " +
                                    grunnpensjonSats + " prosent av beløpet fordi samboeren din mottar uføretrygd, pensjon eller omstillingsstønad fra folketrygden eller AFP som det godskrives pensjonspoeng for.",

                            Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() +
                                    grunnbeloep +
                                    " kroner. Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid. Vidare er grunnpensjonen justert til " +
                                    grunnpensjonSats + " prosent av beløpet fordi sambuaren din mottar uføretrygd, pensjon eller omstillingsstønad frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",

                            English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() +
                                    grunnbeloep +
                                    ". Your period of national insurance coverage is the years that you lived and/or worked in Norway after the age of 16. The basic pension for a widowed old age pensioner can be calculated on the basis of the pensioner's own or the deceased's period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance coverage as this is more beneficial for you. The deceased's period of national insurance coverage was less than 40 years meaning your basic pension will be reduced proportionately.  The basic pension has also been adjusted to " +
                                    grunnpensjonSats + " percent of the reduced amount because your cohabitant is receiving disability benefit, a national insurance pension or adjustment allowance, or contractual early retirement pension (AFP) which earns pension points.",
                        )
                    }
                }

                showIf(beregnetEtterAvdod) {
                    paragraph {
                        includePhrase(GrunnpensjonBold)
                        textExpr(
                            Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                    " kroner. Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid. Videre er grunnpensjonen justert til " +
                                    grunnpensjonSats + " prosent av beløpet fordi samboeren din har årlig inntekt over to ganger grunnbeløpet.",

                            Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                    " kroner. Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid. Vidare er grunnpensjonen justert til " +
                                    grunnpensjonSats + " prosent av beløpet fordi sambuaren din har årleg inntekt over to gonger grunnbeløpet.",

                            English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                    ". Your period of national insurance coverage is the years that you lived and/or worked in Norway after the age of 16. The basic pension for a widowed old age pensioner can be calculated on the basis of the pensioner's own or the deceased's period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance coverage as this is more beneficial for you. The deceased's period of national insurance coverage was less than 40 years meaning your basic pension will be reduced proportionately.  The basic pension has also been adjusted to " +
                                    grunnpensjonSats + " percent of the reduced amount because your cohabitant has an annual income that exceeds twice the national insurance basic amount (G).",
                        )
                    }
                }
                showIf(not(epsHarInntektOver2G) and not(epsMottarPensjon)) {
                    paragraph {
                        includePhrase(GrunnpensjonBold)
                        textExpr(
                            Bokmal to "fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er ".expr() + grunnbeloep +
                                    " kroner. Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Grunnpensjonen til en gjenlevende alderspensjonist kan enten gis på grunnlag av egen eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av avdødes trygdetid da dette gir det høyeste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid. Videre er grunnpensjonen justert til " +
                                    grunnpensjonSats + " prosent av beløpet fordi samboeren din har årlig inntekt under to ganger grunnbeløpet.",

                            Nynorsk to "blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er ".expr() + grunnbeloep +
                                    " kroner. Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Grunnpensjonen til ein attlevande alderspensjonist kan bli gitt på grunnlag av anten eiga eller avdødes trygdetid. Grunnpensjonen din er gitt på grunnlag av trygdetida til avdøde då det gir det høgaste beløpet for deg. Grunnpensjonen er redusert fordi avdøde også hadde mindre enn 40 års trygdetid. Vidare er grunnpensjonen justert til " +
                                    grunnpensjonSats + " prosent av beløpet fordi sambuaren din har årleg inntekt lågare enn to gonger grunnbeløpet.",

                            English to "is calculated on the basis of the national insurance basic amount (G), which is currently NOK ".expr() + grunnbeloep +
                                    ". Your period of national insurance cover is the years that you lived and/or worked in Norway after the age of 16. When you have rights as a surviving spouse the basic pension can be calculated on the basis of the pensioner’s own or the deceased’s period of national insurance coverage. Your basic pension has been calculated on the basis of the deceased's period of national insurance cover as this is more beneficial for you. The deceased's period of national insurance cover was less than 40 years meaning your basic pension will be reduced proportionately. The basic pension has also been adjusted to " +
                                    grunnpensjonSats + " percent of this amount because the annual income of your cohabitant is lower than twice the national insurance basic amount.",
                        )
                    }
                }
            }

        }

    }

}