package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.andelKap19
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.andelKap20
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.gjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BarnetilleggGjeldendeSelectors.innvilgetBarnetilleggFB_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BarnetilleggGjeldendeSelectors.innvilgetBarnetilleggSB_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.barnetilleggFBbrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.barnetilleggSBbrutto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.foedselsDato
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.ektefelletillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.fribelopET
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.fribelopFB
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.fribelopSB
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.garantipensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.garantitillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.gjenlevendetilleggKap19_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.inntektBruktIavkortningET
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.inntektBruktIavkortningFB
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.inntektBruktIavkortningSB
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.inntektspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.minstenivaPensjonistPar
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.skjermingstillegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EktefelletilleggGjeldendeSelectors.innvilgetEktefelletillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.SaerskiltSatsGjeldendeSelectors.saerskiltSatsErBrukt_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.alderspensjonGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.alderspensjonPerManed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.epsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.beregnetPensjonPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.ektefelletilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.krav
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.saerskiltSatsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.tilleggspensjonGjeldende
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.*
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.Kroner


@TemplateModelHelpers
val vedleggMaanedligPensjonFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattDto>(
    title = newText(
        Bokmal to "Dette er din månedlige pensjon før skatt",
        Nynorsk to "Dette er den månadlege pensjonen din før skatt",
        English to "This is your monthly pension before tax",
    ),
    includeSakspart = false,
    outline = {


        val regelverkstype = alderspensjonGjeldende.regelverkstype

        showIf(regelverkstype.isOneOf(AP1967, AP2011)) {
            includePhrase(TabellMaanedligPensjonKap19(beregnetPensjonPerManedGjeldende))
        }.orShowIf(regelverkstype.isOneOf(AP2016)) {

            val foedselsaar = bruker.foedselsDato.year.toYear().format()
            val andelKap19 = alderspensjonGjeldende.andelKap19.format()
            val andelKap20 = alderspensjonGjeldende.andelKap20.format()
            paragraph {
                textExpr(
                    Bokmal to "De som er født i perioden 1954–1962 får en kombinasjon av alderspensjon etter gamle og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i ".expr() +
                            foedselsaar + " får du beregnet "
                            + andelKap19 + "/10 av pensjonen etter gamle regler, og " +
                            andelKap20 + "/10 etter nye regler.",

                    Nynorsk to "Dei som er fødde i perioden 1954–1962 får ein kombinasjon av alderspensjon etter gamle og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i ".expr() +
                            foedselsaar + ", får du rekna ut "
                            + andelKap19 + "/10 av pensjonen etter gamle reglar, og " +
                            andelKap20 + "/10 etter nye reglar.",

                    English to "Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in ".expr() +
                            foedselsaar + ", "
                            + andelKap19 + "/10 of your pension is calculated on the basis of the old provisions, and " +
                            andelKap20 + "/10 is calculated on the basis of new provisions.",
                )
            }

            includePhrase(TabellMaanedligPensjonKap19og20(beregnetPensjon = beregnetPensjonPerManedGjeldende))
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
        val beregningVirkningsaar = beregnetPensjonPerManedGjeldende.virkDatoFom.year
        showIf(
            beregningVirkningsaar.lessThan(2025)
                    and ektefelletilleggGjeldende.innvilgetEktefelletillegg_safe.ifNull(false)
        ) {
            paragraph {
                text(
                    Bokmal to "Ektefelletillegg ",
                    Nynorsk to "Ektefelletillegg ",
                    English to "Spouse supplement ",
                    BOLD
                )

                text(
                    Bokmal to "er et inntektsprøvd tillegg til alderspensjon. Fullt ektefelletillegg fastsettes som en prosent av høy sats for minste pensjonsnivå. Til og med 31. desember 2022 tilsvarte dette 25 prosent. ",
                    Nynorsk to "er eit inntektsprøvd tillegg til alderspensjon. Fullt ektefelletillegg blir fastsett som ein prosent av høg sats for minste pensjonsnivå. Til og med 31. desember 2022 svarte dette til 25 prosent. ",
                    English to "is a means-test supplement to the retirement pension.  er eit inntektsprøvd tillegg til alderspensjon. Fullt ektefelletillegg blir fastsett som ein prosent av høg sats for minste pensjonsnivå. Til og med 31. desember 2022 svarte dette til 25 prosent. ",
                )

                showIf(beregnetPensjonPerManedGjeldende.virkDatoFom.year.lessThan(2024)) {
                    text(
                        Bokmal to "Fra 1. januar 2023 reduseres fullt ektefelletillegg til 67 prosent av dette beløpet.",
                        Nynorsk to "Frå 1. januar 2023 blir fullt ektefelletillegg redusert til 67 prosent av dette beløpet.",
                        English to "From 1 January 2023, the full spouse supplement will be reduced to 67 percent of this amount.",
                    )
                }.orShow {
                    text(
                        Bokmal to "Fra 1. januar 2024 reduseres fullt ektefelletillegg til 33 prosent av dette beløpet.",
                        Nynorsk to "Frå 1. januar 2024 blir fullt ektefelletillegg redusert til 33 prosent av dette beløpet.",
                        English to "From 1 January 2024, the full spouse supplement will be reduced to 33 percent of this amount.",
                    )
                }
            }

            ifNotNull(beregnetPensjonPerManedGjeldende.ektefelletillegg) {
                paragraph {
                    textExpr(
                        Bokmal to "Før eventuell justering på grunn av inntekt, utgjør tillegget ditt ".expr() +
                                it.format() + " kroner hver måned. Beløpet er beregnet med samme trygdetid som for grunnpensjonen.",

                        Nynorsk to "Før eventuell justering på grunn av inntekt, utgjer tillegget ditt ".expr() +
                                it.format() + " kroner kvar månad. Beløpet er berekna med same trygdetid som for grunnpensjonen.",

                        English to "Before any adjustment according to your income, your supplement is calculated to NOK ".expr() +
                                it.format() + " each month. The amount is calculated on the same period of national insurance cover as the basic pension.",
                    )
                }
            }

            ifNotNull(beregnetPensjonPerManedGjeldende.inntektBruktIavkortningET) {
                paragraph {
                    textExpr(
                        Bokmal to "Ektefelletillegget ditt er deretter beregnet på grunnlag av din samlede inntekt på ".expr() + it.format() + " kroner.",
                        Nynorsk to "Ektefelletillegget ditt er deretter berekna på grunnlag av din samla inntekt på ".expr() + it.format() + " kroner.",
                        English to "The spouse supplement is then adjusted according to your total income of NOK ".expr() + it.format() + ".",
                    )
                }
            }
            ifNotNull(beregnetPensjonPerManedGjeldende.fribelopET) {
                paragraph {
                    text(
                        Bokmal to "Fribeløp",
                        Nynorsk to "Fribeløp",
                        English to "The exemption amount",
                        BOLD
                    )
                    textExpr(
                        Bokmal to " er et fastsatt beløp som benyttes ved inntektsprøving av ektefelletillegg. Fribeløpet er ".expr() +
                                it.format() + " kroner. Tillegget ditt reduseres med 50 prosent av den samlede inntekten som overstiger fribeløpet.",
                        Nynorsk to " er eit fastsett beløp vi bruker ved inntektsprøving av ektefelletillegg. Fribeløpet er for tida ".expr() +
                                it.format() + " kroner. Tillegget ditt blir redusert med 50 prosent av den samla inntekta som overstig fribeløpet.",
                        English to " is a fixed figure that is used in income testing in connection with calculating spouse supplement. The exemption amount is NOK ".expr() +
                                it.format() + ". The supplement will be reduced by 50 percent of your total income in excess of the exemption amount.",
                    )
                }
            }
        }

        val innvilgetBarnetilleggFellesBarn = barnetilleggGjeldende.innvilgetBarnetilleggFB_safe.ifNull(false)
        val innvilgetBarnetilleggSaerkullesbarn = barnetilleggGjeldende.innvilgetBarnetilleggSB_safe.ifNull(false)
        val innvilgetBarnetillegg = innvilgetBarnetilleggFellesBarn or innvilgetBarnetilleggSaerkullesbarn

        //vedleggBTAP_001
        showIf(
            beregningVirkningsaar.lessThan(2025)
                    and innvilgetBarnetillegg
        ) {
            paragraph {
                text(
                    Bokmal to "Barnetillegg ",
                    Nynorsk to "Barnetillegg ",
                    English to "Child supplement ",
                    BOLD
                )
                text(
                    Bokmal to " gis som et inntektsprøvd tillegg til alderspensjonen. Fullt barnetillegg for hvert barn fastsettes som en prosent av høy sats for minste pensjonsnivå. Til og med 31. desember 2022 tilsvarte dette 20 prosent.",
                    Nynorsk to " blir gitt som eit inntektsprøvd tillegg til alderspensjonen. Fullt barnetillegg for kvart barn blir fastsett som ein prosent av høg sats for minste pensjonsnivå. Til og med 31. desember 2022 svarte dette til 20 prosent.",
                    English to " is paid as a means-test supplement to the retirement pension. Up to and including 31 December 2022, the full child supplement corresponded to 25 percent of the minimum pension level at the high rate.",
                )

                showIf(beregningVirkningsaar.lessThan(2024)) {
                    text(
                        Bokmal to "Fra 1. januar 2023 reduseres fullt barnetillegg til 67 prosent av dette beløpet.",
                        Nynorsk to "Frå 1. januar 2023 blir fullt barnetillegg redusert til 67 prosent av dette beløpet.",
                        English to "From 1 January 2023, the full child supplement will be reduced to 67 percent of this amount.",
                    )
                }.orShow {
                    text(
                        Bokmal to "Fra 1. januar 2024 reduseres fullt barnetillegg til 33 prosent av dette beløpet.",
                        Nynorsk to "Frå 1. januar 2024 blir fullt barnetillegg redusert til 33 prosent av dette beløpet.",
                        English to "From 1 January 2024, the full child supplement will be reduced to 33 percent of this amount.",
                    )
                }

            }
        }

        //vedleggBTFBAP_001
        showIf(innvilgetBarnetilleggFellesBarn and beregningVirkningsaar.lessThan(2025)) {
            ifNotNull(beregnetPensjonPerManedGjeldende.barnetilleggFBbrutto) {
                paragraph {
                    textExpr(
                        Bokmal to "Før eventuell justering på grunn av inntekt, utgjør barnetillegget ditt for fellesbarn ".expr() +
                                it.format() + " kroner hver måned. Beløpet er avkortet mot trygdetid på samme måte som for grunnpensjonen.",

                        Nynorsk to "Før eventuell justering på grunn av inntekt, utgjer barnetillegget ditt for fellesbarn ".expr() +
                                it.format() + " kroner kvar månad. Beløpet er avkorta mot trygdetid på same måte som for grunnpensjonen.",

                        English to "Before any adjustment according to your income, your child supplement is calculated to NOK ".expr() +
                                it.format() + " each month. The amount is calculated on the same period of national insurance cover as the basic pension.",
                    )
                }
            }
            ifNotNull(beregnetPensjonPerManedGjeldende.inntektBruktIavkortningFB) {
                paragraph {
                    textExpr(
                        Bokmal to "For barn som bor sammen med begge foreldrene (fellesbarn) tar vi hensyn til begge foreldrenes inntekter når vi fastsetter samlet inntekt. Barnetillegget ditt er beregnet på grunnlag av en samlet inntekt på ".expr() +
                                it.format() + " kroner.",
                        Nynorsk to "For barn som bur samen med begge foreldra (fellesbarn) tek vi omsyn til begge foreldras inntekter når vi fastset samla inntekt. Barnetillegget ditt er berekna på grunnlag av ei samla inntekt på ".expr() +
                                it.format() + " kroner.",
                        English to "For children who live with both their parents, we take the sum total of both parents' income into account. The supplement is therefore adjusted according to your total income of NOK ".expr() +
                                it.format() + ".",
                    )
                }
            }
            ifNotNull(beregnetPensjonPerManedGjeldende.fribelopFB) {
                paragraph {
                    text(
                        Bokmal to "Fribeløp ",
                        Nynorsk to "Fribeløp ",
                        English to "The exemption amount ",
                        BOLD
                    )
                    textExpr(
                        Bokmal to "er et fastsatt beløp som benyttes ved inntektsprøving av barnetillegg. Fribeløpet er ".expr() +
                                it.format() + " kroner. Tillegget ditt reduseres med 50 prosent av den samlede inntekten som overstiger fribeløpet.",

                        Nynorsk to "Fribeløp er eit fastsett beløp vi bruker ved inntektsprøving av barnetillegg. Fribeløpet er ".expr() +
                                it.format() + " kroner. Tillegget ditt blir redusert med 50 prosent av den samla inntekta som overstig fribeløpet.",

                        English to "is a fixed figure that is used in income testing in connection with calculating child supplement. The exemption amount is NOK ".expr() +
                                it.format() + ". The supplement will be reduced by 50 percent of your total income in excess of the exemption amount.",
                    )
                }
            }
        }

        showIf(innvilgetBarnetilleggSaerkullesbarn and beregningVirkningsaar.lessThan(2025)) {
            ifNotNull(beregnetPensjonPerManedGjeldende.barnetilleggSBbrutto) {
                paragraph {
                    textExpr(
                        Bokmal to "Før eventuell justering på grunn av inntekt, utgjør barnetillegget ditt for særkullsbarn ".expr() +
                                it.format() + " kroner hver måned. Beløpet er avkortet mot trygdetid på samme måte som for grunnpensjonen.",

                        Nynorsk to "Før eventuell justering på grunn av inntekt, utgjer barnetillegget ditt for særkullsbarn ".expr() +
                                it.format() + " kroner kvar månad. Beløpet er avkorta mot trygdetid på same måte som for grunnpensjonen.",

                        English to "Before any adjustment according to your income, your child supplement for a child/children from a previous relationship, is calculated to NOK ".expr() +
                                it.format() + " each month. The amount is calculated on the same period of national insurance cover as the basic pension.",
                    )
                }
            }

            ifNotNull(beregnetPensjonPerManedGjeldende.inntektBruktIavkortningSB) {
                paragraph {
                    textExpr(
                        Bokmal to "Barnetillegget ditt er deretter beregnet på grunnlag av din samlede inntekt på ".expr() + it.format() + " kroner.",
                        Nynorsk to "Barnetillegget ditt er deretter berekna på grunnlag av di samla inntekt på ".expr() + it.format() + " kroner.",
                        English to "The child supplement is then adjusted according to your total income of NOK ".expr() + it.format() + ".",
                    )
                }
            }
            ifNotNull(beregnetPensjonPerManedGjeldende.fribelopSB) {
                paragraph {
                    text(
                        Bokmal to "Fribeløp ",
                        Nynorsk to "Fribeløp ",
                        English to "The exemption amount ",
                        BOLD
                    )
                    textExpr(
                        Bokmal to " er et fastsatt beløp som benyttes ved inntektsprøving av barnetillegg. Fribeløpet er ".expr() +
                                it.format() + " kroner. Tillegget reduseres med 50 prosent av den samlede inntekten som overstiger fribeløpet.",

                        Nynorsk to " er eit fastsett beløp vi bruker ved inntektsprøving av barnetillegg. Fribeløpet er ".expr() +
                                it.format() + " kroner. Tillegget ditt blir redusert med 50 prosent av den samla inntekta som overstig fribeløpet.",

                        English to " is a fixed figure that is used in income testing in connection with calculating child supplement. The exemption amount is NOK ".expr() +
                                it.format() + ". The supplement will be reduced by 50 percent of your total income in excess of the exemption amount.",
                    )
                }
            }
        }

        //vedleggBelopLonnsvekstRegOverskrift
        showIf(regelverkstype.isOneOf(AP1967)) {
            title1 {
                text(
                    Bokmal to "Regulering av alderspensjon under utbetaling",
                    Nynorsk to "Regulering av alderspensjon under utbetaling",
                    English to "Regulation of retirement pension during payment",
                )
            }

            //vedleggBelopLonnsvekstReg
            paragraph {
                text(
                    Bokmal to "Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldingen din. På $NAV_URL kan du lese mer om hvordan pensjonene blir regulert.",
                    Nynorsk to "Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldinga di. På $NAV_URL kan du lese meir om korleis pensjonane blir regulerte.",
                    English to "The pension amount will be adjusted with effect from 1 May, and the actual increase is usually paid retroactively in June. You will be informed about this on your payment notice. You can read more about how pensions are adjusted at $NAV_URL",
                )
            }
        }

        showIf(regelverkstype.isOneOf(AP2011, AP2016)) {
            title1 {
                text(
                    Bokmal to "Alderspensjonen din er levealdersjustert",
                    Nynorsk to "Alderspensjonen din er levealdersjustert",
                    English to "Your retirement pension has been adjusted for life expectancy",
                )
            }
            paragraph {
                text(
                    Bokmal to "Pensjonen er justert i forhold til forventet levealder for ditt årskull. Levealdersjustering er en mekanisme som tar høyde for økt levealder i befolkningen og er innført for å sikre at pensjonssystemet forblir bærekraftig. Hver enkelt del av pensjonen din har blitt levealdersjustert.",
                    Nynorsk to "Pensjonen er justert i forhold til forventa levealder for årskullet ditt. Levealdersjustering er ein mekanisme som tek høgde for auka levealder i befolkninga og er innført for å sikre at pensjonssystemet blir verande berekraftig. Kvar enkelt del av pensjonen din har blitt levealdersjustert.",
                    English to "Your pension has been adjusted in accordance with the life expectancy for people born in the same year as you. Life expectancy adjustments take into account increased life expectancy in the population and have been implemented to ensure that our pension scheme remains sustainable. Each individual component of your pension has been adjusted for life expectancy.",
                )
            }
        }


        // vedlegg del 2

        showIf(alderspensjonPerManed.size().greaterThan(1)) {
            //vedleggBelopFlerePerioderTittel_001
            title1 {
                textExpr(
                    Bokmal to "Oversikt over pensjonen fra ".expr() + krav.virkDatoFom.format(),
                    Nynorsk to "Oversikt over pensjonen frå ".expr() + krav.virkDatoFom.format(),
                    English to "Pension specifications as of ".expr() + krav.virkDatoFom.format(),
                )
            }

            // veldeggBelopFlerePerioder_001
            paragraph {
                text(
                    Bokmal to "Hvis det har vært endringer i noen av opplysningene som ligger til grunn for beregningen eller pensjonen har vært regulert, kan dette føre til en endring i hvor mye du får utbetalt. Nedenfor følger en oversikt over de månedlige pensjonsbeløpene dine.",
                    Nynorsk to "Dersom det har vore endringar i nokre av opplysningane som ligg til grunn for utrekninga eller pensjonen har vore regulert, kan det føre til ei endring i kor mykje du får utbetalt. Nedanfor fylgjer ei oversikt over dei månadlege pensjonsbeløpa dine.",
                    English to "If there have been changes affecting how your pension is calculated in the period or amendments in the National Insurance basic amount, your pension may be adjusted accordingly. Below is a list of your monthly pension payments.",
                )
            }

            forEach(alderspensjonPerManed) { alderspensjon ->
                showIf(regelverkstype.isOneOf(AP1967, AP2011)) {
                    includePhrase(TabellMaanedligPensjonKap19(alderspensjon))
                }.orShowIf(regelverkstype.isOneOf(AP2016)) {
                    includePhrase(TabellMaanedligPensjonKap19og20(alderspensjon))
                }
            }
        }
    }
)
