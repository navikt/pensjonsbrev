package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.gjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.grunnpensjonSats
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.foedselsDato
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.garantipensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.garantitillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.gjenlevendetilleggKap19
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.gjenlevendetilleggKap19_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.inntektspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.minstenivaPensjonistPar
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.pensjonstillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.saertillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.skjermingstillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BeregnetPensjonGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EktefelletilleggGjeldendeSelectors.innvilgetEktefelletillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.SaerskiltSatsGjeldendeSelectors.saerskiltSatsErBrukt_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.erRedusert_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.kombinertMedAvdod_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaUngUforeAvdod_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaUngUfore_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaYrkesskade_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.alderspensjonGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.epsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.beregnetPensjonPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.ektefelletilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.saerskiltSatsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.tilleggspensjonGjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.*
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.Kroner


@TemplateModelHelpers
val maanedligPensjonFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattDto>(
    title = newText(
        Bokmal to "Dette er din månedlige pensjon før skatt",
        Nynorsk to "Dette er den månadlege pensjonen din før skatt",
        English to "This is your monthly pension before tax",
    ),
    includeSakspart = false, // TODO skal den ha saksinfo med?
    outline = {


        val regelverkstype = alderspensjonGjeldende.regelverkstype

        val epsBorSammenMedBruker = epsGjeldende.borSammenMedBruker_safe.ifNull(false)
        val epsMottarPensjon = epsGjeldende.mottarPensjon_safe.ifNull(false)
        val epsHarInntektOver2G = epsGjeldende.harInntektOver2G_safe.ifNull(false)
        showIf(
            regelverkstype.isOneOf(
                AP1967,
                AP2011
            )
        ) {
            includePhrase(TabellMaanedligPensjonKap19(beregnetPensjonPerManedGjeldende))
        }.orShowIf(regelverkstype.isOneOf(AP2016)) {
            includePhrase(
                TabellMaanedligPensjonKap19og20(
                    beregnetPensjon = beregnetPensjonPerManedGjeldende,
                    brukersFoedselsdato = bruker.foedselsDato,
                    alderspensjonGjeldende = alderspensjonGjeldende
                )
            )
        }

        title1 {
            text(
                Bokmal to "Slik er pensjonen din satt sammen",
                Nynorsk to "Slik er pensjonen din sett saman",
                English to "This is how your pension is calculated",
            )
        }

        val aldersEllerSykehjemInstOpphold = institusjonsoppholdGjeldende.aldersEllerSykehjem_safe.ifNull(false)
        val ensligPgaInstitusjonsopphold = institusjonsoppholdGjeldende.ensligPaInst_safe.ifNull(false)
        val epsErpaaInstitusjon = institusjonsoppholdGjeldende.epsPaInstitusjon_safe.ifNull(false)
        val erPaaFengselsInstitusjon = institusjonsoppholdGjeldende.fengsel_safe.ifNull(false)
        val erPaahelseInstitusjon = institusjonsoppholdGjeldende.helseinstitusjon_safe.ifNull(false)
        val beregnetSomEnsligPgaInstopphold = (aldersEllerSykehjemInstOpphold
                or ensligPgaInstitusjonsopphold
                or epsErpaaInstitusjon
                or erPaaFengselsInstitusjon
                or erPaahelseInstitusjon)

        showIf(
            bruker.sivilstand.isOneOf(GLAD_EKT, SEPARERT) or
                    (bruker.sivilstand.isOneOf(GIFT) and beregnetSomEnsligPgaInstopphold)
        ) {
            paragraph {
                text(
                    Bokmal to "Du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                    Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                    English to "You and your spouse are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                )
            }
        }

        showIf(
            bruker.sivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER) or
                    (bruker.sivilstand.isOneOf(PARTNER) and beregnetSomEnsligPgaInstopphold)
        ) {
            paragraph {
                text(
                    Bokmal to "Du og partneren din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                    Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                    English to "You and your partner are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                )
            }
        }

        includePhrase(
            MaanedligPensjonFoerSkattGrunnpensjon(
                beregnetPensjonPerManedGjeldende = beregnetPensjonPerManedGjeldende,
                epsGjeldende = epsGjeldende,
                alderspensjonGjeldende = alderspensjonGjeldende,
                institusjonsoppholdGjeldende = institusjonsoppholdGjeldende,
                bruker = bruker,
                beregnetSomEnsligPgaInstopphold = beregnetSomEnsligPgaInstopphold,
            )
        )

        includePhrase(
            MaanedligPensjonFoerSkattTilleggsPensjon(
                tilleggspensjonGjeldende = tilleggspensjonGjeldende,
                beregnetPensjonPerManedGjeldende = beregnetPensjonPerManedGjeldende,
                regelverkstype = regelverkstype
            )
        )

        includePhrase(
            MaanedligPensjonFoerSkattSaertillegg(
                beregnetPensjonPerManedGjeldende = beregnetPensjonPerManedGjeldende,
                alderspensjonGjeldende = alderspensjonGjeldende,
                epsGjeldende = epsGjeldende,
                institusjonsoppholdGjeldende = institusjonsoppholdGjeldende,
                brukersSivilstand = bruker.sivilstand,
            )
        )

        includePhrase(
            MaanedligPensjonFoerSkattPensjonstillegg(
                beregnetPensjonPerManedGjeldende = beregnetPensjonPerManedGjeldende,
                alderspensjonGjeldende = alderspensjonGjeldende,
                saerskiltSatsGjeldende = saerskiltSatsGjeldende,
                regelverkstype = regelverkstype,
            )
        )

        //vedleggBelopGJTkap19_001
        val harGjenlevendeTilleggKap19 =
            beregnetPensjonPerManedGjeldende.gjenlevendetilleggKap19_safe.ifNull(Kroner(0)).greaterThan(0)
        val gjenlevendetilleggKap19Innvilget = alderspensjonGjeldende.gjenlevendetilleggKap19Innvilget
        val gjenlevendetilleggInnvilget = alderspensjonGjeldende.gjenlevendetilleggInnvilget
        showIf(
            gjenlevendetilleggKap19Innvilget and harGjenlevendeTilleggKap19 and not(gjenlevendetilleggInnvilget)
        ) {
            paragraph {
                text(
                    Bokmal to "Du får ",
                    Nynorsk to "Du får ",
                    English to "You receive ",
                )
                text(
                    Bokmal to "gjenlevendetillegg",
                    Nynorsk to "attlevandetillegg",
                    English to "survivor's supplement",
                    BOLD
                )
                text(
                    Bokmal to " fordi du har pensjonsrettigheter etter avdøde. ",
                    Nynorsk to " fordi du har pensjonsrettar etter avdøde.",
                    English to " because you have pension rights after the deceased.",
                )
            }
        }

        //vedleggBelopInntektspensjon_001
        showIf(beregnetPensjonPerManedGjeldende.inntektspensjon.ifNull(Kroner(0)).greaterThan(0)) {
            paragraph {
                text(
                    Bokmal to "Inntektspensjonen ",
                    Nynorsk to "Inntektspensjonen ",
                    English to "Income-based pension ",
                    BOLD,
                )
                text(
                    Bokmal to "din avhenger av arbeidsinntekt og annen pensjonsopptjening som du har hatt i folketrygden fra og med det året du fylte 17.",
                    Nynorsk to "din er avhengig av arbeidsinntekt og anna pensjonsopptening som du har hatt i folketrygda frå og med året du fylte 17.",
                    English to "is dependent on income from employment and other pensionable earnings in the national insurance scheme from the year you turned 17.",
                )
            }
        }

        //vedleggBelopGarantipensjon_001
        val harGarantipensjon = beregnetPensjonPerManedGjeldende.garantipensjon.ifNull(Kroner(0)).greaterThan(0)
        showIf(harGarantipensjon) {
            paragraph {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "Individuals who have not qualified for income-based pension are entitled to a ",
                )
                text(
                    Bokmal to "Garantipensjon ",
                    Nynorsk to "Garantipensjon ",
                    English to "guaranteed pension. ",
                    BOLD
                )
                text(
                    Bokmal to "får du fordi du har tjent opp lav inntektspensjon. Garantipensjonen er avkortet mot inntektspensjonen din.",
                    Nynorsk to "får du fordi du har tent opp låg inntektspensjon. Garantipensjonen er avkorta mot inntektspensjonen din.",
                    English to "The guaranteed pension is reduced against your income-based pension.",
                )
            }
        }

        //vedleggBelopGarantipensjonFlyktning_001
        val fullTrygdetid = beregnetPensjonPerManedGjeldende.fullTrygdetid
        showIf(
            harGarantipensjon
                    and beregnetPensjonPerManedGjeldende.flyktningstatusErBrukt
                    and fullTrygdetid
        ) {
            paragraph {
                text(
                    Bokmal to "Som flyktning får du garantipensjonen din beregnet som om du har full trygdetid i Norge. Vær oppmerksom på at alderspensjonen din vil bli omregnet etter faktisk trygdetid dersom du flytter til et land utenfor EØS-området.",
                    Nynorsk to "Som flyktning får du garantipensjonen din rekna ut som om du har full trygdetid i Noreg. Ver merksam på at alderspensjonen din vil bli omrekna etter faktisk trygdetid dersom du flyttar til eit land utanfor EØS-området.",
                    English to "As a refugee, your guaranteed pension is calculated as if you had a full period of national insurance coverage in Norway. Please note that your retirement pension will be recalculated according to your actual period of national insurance coverage if you move to a country outside the EEA region.",
                )
            }
        }

        //vedleggBelopGarantipensjonRedusTT_001
        showIf(
            harGarantipensjon and not(fullTrygdetid)
        ) {
            paragraph {
                text(
                    Bokmal to "Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Garantipensjonen din er redusert fordi du har mindre enn 40 års trygdetid.",
                    Nynorsk to "Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Garantipensjonen din er redusert fordi du har mindre enn 40 års trygdetid.",
                    English to "The years you have lived and/or worked in Norway after the age of 16 count as national insurance periods. Your guaranteed pension has been reduced because you have less than 40 years of national insurance coverage.",
                )
            }
        }

        //vedleggBelopGarantitilleggKap20
        val harGarantitillegg = beregnetPensjonPerManedGjeldende.garantitillegg.ifNull(Kroner(0)).greaterThan(0)
        showIf(harGarantitillegg) {
            paragraph {
                text(
                    Bokmal to "Garantitillegg for opptjente rettigheter ",
                    Nynorsk to "Garantitillegg for opptente rettar ",
                    English to "Guarantee supplements for accumulated rights ",
                    BOLD,
                )
                text(
                    Bokmal to "skal sikre at du får en alderspensjon ved 67 år som tilsvarer den pensjonen du hadde tjent opp før pensjonsreformen i 2010.",
                    Nynorsk to "skal sikre at du får ein alderspensjon ved 67 år som tilsvarer den pensjonen du hadde tent opp før pensjonsreforma i 2010.",
                    English to "have been implemented to ensure that your retirement pension at age 67 is equal to the pension you would have been eligible for prior to the pension reform of 2010.",
                )
            }
        }

        //vedleggBelopGjT_001
        val harGjenlevendetillegg = beregnetPensjonPerManedGjeldende.gjenlevendetillegg.ifNull(Kroner(0)).greaterThan(0)
        showIf(harGjenlevendetillegg and gjenlevendetilleggInnvilget and gjenlevendetilleggKap19Innvilget) {
            paragraph {
                text(
                    Bokmal to "Du får et ",
                    Nynorsk to "Du får eit ",
                    English to "You receive a ",
                )
                text(
                    Bokmal to "gjenlevendetillegg",
                    Nynorsk to "attlevandetillegg",
                    English to "survivor's supplement",
                    BOLD
                )
                text(
                    Bokmal to " etter gamle regler (kapittel 19) og et ",
                    Nynorsk to " etter gamle reglar (kapittel 19) og eit ",
                    English to " under the old rules (Chapter19) and a ",
                )

                text(
                    Bokmal to "gjenlevendetillegg ",
                    Nynorsk to "attlevandetillegg ",
                    English to "survivor's supplement ",
                    BOLD
                )
                text(
                    Bokmal to " etter nye regler (kapittel 20) fordi du har rettigheter etter avdøde.",
                    Nynorsk to " etter nye reglar (kapittel 20) fordi du har rettar etter avdøde.",
                    English to " under the new rules (Chapter 20) because you have pension rights after the deceased.",
                )
            }
        }

        //vedleggBelopMNTPP_001
        showIf(beregnetPensjonPerManedGjeldende.minstenivaPensjonistPar.ifNull(Kroner(0)).greaterThan(0)) {
            paragraph {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "You have been granted ",
                )
                text(
                    Bokmal to "Minstenivåtillegg pensjonistpar ",
                    Nynorsk to "Minstenivåtillegg pensjonistpar ",
                    English to "the minimum supplement for pensioner couples ",
                    BOLD
                )
                textExpr(
                    Bokmal to "er gitt fordi den totale pensjonen du og ".expr() + bruker.sivilstand.bestemtForm()
                            + " din mottar er lavere enn et minste samlet pensjonsnivå.",
                    Nynorsk to "er gitt fordi den totale pensjonen du og ".expr() + bruker.sivilstand.bestemtForm()
                            + " din får er lågare enn eit minste samla pensjonsnivå.",
                    English to "because the total pension that you and your ".expr() + bruker.sivilstand.bestemtForm()
                            + " receive is lower than the defined minimum pension level.",
                )
            }
        }

        //vedleggBelopMNT_001
        val harMinstenivaaIndividuell = beregnetPensjonPerManedGjeldende.minstenivaIndividuell.notNull()
        showIf(
            harMinstenivaaIndividuell
                    and regelverkstype.isOneOf(AP2011, AP2016, AP1967)
        ) {
            paragraph {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "You have been granted ",
                )
                text(
                    Bokmal to "Minstenivåtillegg individuelt",
                    Nynorsk to "Minstenivåtillegg individuelt",
                    English to "a minimum pension supplement",
                    BOLD
                )
                text(
                    Bokmal to " er gitt fordi pensjonen ellers ville ha vært lavere enn minste pensjonsnivå.",
                    Nynorsk to " er gitt fordi pensjonen elles ville ha vore lågare enn minste pensjonsnivå.",
                    English to " because your pension otherwise would be lower than the minimum pension level.",
                )
            }
        }

        //vedleggBelopSaerSats_001
        val saerskiltSatsErBrukt = saerskiltSatsGjeldende.saerskiltSatsErBrukt_safe.ifNull(false)
        showIf(
            saerskiltSatsErBrukt
                    and regelverkstype.isOneOf(AP1967, AP2011)
                    and harMinstenivaaIndividuell
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Fordi du forsørger ".expr() + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm() +
                            " din som er over 60 år, er tillegget fastsatt etter en særskilt sats for minste pensjonsnivå.",
                    Nynorsk to "Fordi du forsørgjer ".expr() + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm() +
                            " din som er over 60 år, er tillegget fastsett etter ein særskilt sats for minste pensjonsnivå.",
                    English to "Because you provide for your ".expr() + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm() +
                            " who is over the age of 60, you are granted the minimum pension level according to a special rate.",
                )
            }
        }

        //vedleggBelopSaerSatsMNTAP2016_001
        showIf(harMinstenivaaIndividuell and regelverkstype.isOneOf(AP2016) and saerskiltSatsErBrukt) {
            paragraph {
                textExpr(
                    Bokmal to "Fordi du forsørger ".expr() + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm() +
                            " din som er over 60 år, er tillegget fastsatt etter en særskilt sats for minste pensjonsnivå. Reglene for særskilt sats gjelder den delen av alderspensjonen som er opptjent etter gamle regler.",
                    Nynorsk to "Fordi du forsørgjer ".expr() + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm() +
                            " din som er over 60 år, er tillegget fastsett etter ein særskilt sats for minste pensjonsnivå. Reglane for særskilt sats gjeld den delen av alderspensjonen som er tent opp etter gamle reglar.",
                    English to "Because you provide for your ".expr() + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm() +
                            " who is over the age of 60, you are granted the minimum pension level according to a special rate. The regulations for the special rate applies to the part of your pension calculated on the basis of the old provisions.",
                )
            }
        }

        //vedleggBelopSkjermingstillegg_001
        val harSkjermingstillegg = beregnetPensjonPerManedGjeldende.skjermingstillegg.notNull()
        showIf(harSkjermingstillegg and regelverkstype.isOneOf(AP2011, AP2016)) {
            paragraph {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "As a former recipient of disability benefit, you have been granted ",
                )
                text(
                    Bokmal to "Skjermingstillegg ",
                    Nynorsk to "Skjermingstillegg ",
                    English to "the supplement for the disabled",
                    BOLD
                )
                text(
                    Bokmal to " til uføre er gitt fordi du som tidligere uføretrygdet delvis skal skjermes fra levealdersjustering av alderspensjonen. (Se eget avsnitt om levealdersjustering lenger ned i brevet.) Tillegget gis fordi tidligere uføretrygdede ikke har de samme mulighetene som arbeidsføre til å være i arbeid etter fylte 67 år.",
                    Nynorsk to " til uføre er gitt fordi du som tidlegare uføretrygda delvis skal skjermast frå levealdersjustering av alderspensjonen. (Sjå eiget avsnitt om levealdersjustering lenger ned i brevet.) Tillegget blir gitt fordi tidlegare uføretrygda ikkje har like godt høve som arbeidsføre til å vere i arbeid etter fylte 67 år.",
                    English to " to help compensate for the effects of life expectancy adjustment of the retirement pension. (See the section on life expectancy adjustment below.) The supplement is provided because as a former recipient of disability benefit, you do not have the same opportunity as others to work after the age of 67.",
                )
            }
        }

        //vedleggETAP_001
        showIf(
            beregnetPensjonPerManedGjeldende.virkDatoFom.year.lessThan(2025)
                    and ektefelletilleggGjeldende.innvilgetEktefelletillegg_safe.ifNull(false)
        ) {
            paragraph {
                text(
                    Bokmal to "Ektefelletillegg ",
                    Nynorsk to "Ektefelletillegg ",
                    English to "Spouse supplement ",
                )
            }
        }


    }
)
