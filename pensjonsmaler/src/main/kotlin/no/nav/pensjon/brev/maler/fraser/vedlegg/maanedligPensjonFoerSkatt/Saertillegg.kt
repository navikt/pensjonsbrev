package no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.grunnpensjonSats
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.saertillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonPerManedSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
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
import no.nav.pensjon.brevbaker.api.model.Kroner

private object SaerTilleggetBold : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Særtillegget ",
            Nynorsk to "Særtillegget ",
            English to "Special supplement ",
            FontType.BOLD
        )
    }

}

data class MaanedligPensjonFoerSkattSaertillegg(
    val beregnetPensjonPerManedGjeldende: Expression<MaanedligPensjonFoerSkattDto.AlderspensjonPerManed>,
    val alderspensjonGjeldende: Expression<MaanedligPensjonFoerSkattDto.AlderspensjonGjeldende>,
    val epsGjeldende: Expression<MaanedligPensjonFoerSkattDto.EPSgjeldende?>,
    val institusjonsoppholdGjeldende: Expression<MaanedligPensjonFoerSkattDto.InstitusjonsoppholdGjeldende?>,
    val brukersSivilstand: Expression<MetaforceSivilstand>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        val harTilleggspensjon: Expression<Boolean> = beregnetPensjonPerManedGjeldende.tilleggspensjon.ifNull(Kroner(0)).greaterThan(0)
        val regelverkstype = alderspensjonGjeldende.regelverkstype
        val grunnpensjonSats = alderspensjonGjeldende.grunnpensjonSats.format()
        val epsBorSammenMedBruker = epsGjeldende.borSammenMedBruker_safe.ifNull(false)
        val harFullTrygdetid = beregnetPensjonPerManedGjeldende.fullTrygdetid
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

        val harSaertillegg = beregnetPensjonPerManedGjeldende.saertillegg_safe.ifNull(Kroner(0)).greaterThan(0)

        //vedleggBelopST_002
        showIf(
            brukersSivilstand.isOneOf(ENSLIG, ENKE, GLAD_EKT, GLAD_PART, SEPARERT, SEPARERT_PARTNER, GIFT, PARTNER)
                    and (
                    aldersEllerSykehjemInstOpphold
                            or erPaahelseInstitusjon
                            or erPaaFengselsInstitusjon
                            or epsErpaaInstitusjon
                    )
        ) {
            paragraph {
                includePhrase(SaerTilleggetBold)
                textExpr(
                    Bokmal to "skal sikre et visst minstenivå på pensjonen. Du får særtillegg etter ordinær sats som utgjør ".expr() +
                            grunnpensjonSats + " prosent av grunnbeløpet. Denne gis til enslige pensjonister som ikke har rett til tilleggspensjon eller som har en tilleggspensjon som er mindre enn særtillegget.",

                    Nynorsk to "skal sikre eit visst minstenivå på pensjonen. Du får særtillegg etter ordinær sats som utgjer ".expr() +
                            grunnpensjonSats + " prosent av grunnbeløpet. Denne gis til einslege pensjonistar som ikkje har rett til tilleggspensjon eller som har ein tilleggspensjon som er lågare enn særtillegget.",

                    English to "is granted to ensure a certain minimum level of pension. You will receive special supplement calculated to ".expr() +
                            grunnpensjonSats + " percent of the National Insurance basic amount (G). This is given to single pensioners who are not entitled to supplementary pension, or receive supplementary pension that is lower than the special supplement.",
                )
            }
        }

        // vedleggBelopSTEktefeller_001
        showIf(
            harSaertillegg
                    and brukersSivilstand.isOneOf(GIFT)
                    and epsBorSammenMedBruker
                    and not(beregnetSomEnsligPgaInstopphold)
        ) {
            paragraph {
                includePhrase(SaerTilleggetBold)
                text(
                    Bokmal to "skal sikre et visst minstenivå på pensjonen. Størrelsen på særtillegget varierer ut fra om ektefellen du bor sammen med har tilleggspensjon eller ikke.",
                    Nynorsk to "skal sikre eit visst minstenivå på pensjonen. Storleiken på særtillegget varierer ut frå om ektefellen du bur saman med har tilleggspensjon eller ikkje.",
                    English to "is granted to ensure a certain minimum level of pension. The amount of the special supplement depends on whether your spouse, with whom you live, is receiving supplementary pension or not.",
                )
            }
        }

        //vedleggBelopSTPartner_001
        showIf(
            harSaertillegg
                    and brukersSivilstand.isOneOf(PARTNER)
                    and epsBorSammenMedBruker
                    and not(beregnetSomEnsligPgaInstopphold)
        ) {
            paragraph {
                includePhrase(SaerTilleggetBold)
                text(
                    Bokmal to "skal sikre et visst minstenivå på pensjonen. Størrelsen på særtillegget varierer ut fra om partner du bor sammen med har tilleggspensjon eller ikke. ",
                    Nynorsk to "skal sikre eit visst minstenivå på pensjonen. Storleiken på særtillegget varierer ut frå om partnaren du bur saman med har tilleggspensjon eller ikkje.",
                    English to "is granted to ensure a certain minimum level of pension. The amount of the special supplement depends on whether your partner, with whom you live, is receiving supplementary pension or not.",
                )
            }
        }

        //vedleggBelopSTSamboer1-5_001
        showIf(brukersSivilstand.isOneOf(SAMBOER_1_5) and harSaertillegg) {
            paragraph {
                includePhrase(SaerTilleggetBold)
                text(
                    Bokmal to "skal sikre et visst minstenivå på pensjonen. Størrelsen på særtillegget varierer ut fra om samboeren din har tilleggspensjon eller ikke.",
                    Nynorsk to "skal sikre eit visst minstenivå på pensjonen. Storleiken på særtillegget varierer ut frå om sambuaren din har tilleggspensjon eller ikkje.",
                    English to "is granted to ensure a certain minimum level of pension. The amount of the special supplement depends on whether your cohabitant is receiving supplementary pension or not.",
                )
            }
        }

        //vedleggBelopSTSamboer3-2
        showIf(brukersSivilstand.isOneOf(SAMBOER_3_2) and harSaertillegg) {
            paragraph {
                includePhrase(SaerTilleggetBold)
                textExpr(
                    Bokmal to "skal sikre et visst minstenivå på pensjonen. Du får særtillegg etter ordinær sats som utgjør ".expr() +
                            grunnpensjonSats + " prosent av grunnbeløpet.",

                    Nynorsk to "skal sikre eit visst minstenivå på pensjonen. Du får særtillegg etter ordinær sats som utgjer ".expr() +
                            grunnpensjonSats + " prosent av grunnbeløpet.",

                    English to "is granted to ensure a certain minimum level of pension. You will receive special supplement calculated to ".expr() +
                            grunnpensjonSats + " percent of the National Insurance basic amount (G).",
                )
            }
        }

        //vedleggBelopSTAvkortetTP
        showIf(harSaertillegg and harTilleggspensjon and regelverkstype.isOneOf(AP1967)) {
            paragraph {
                text(
                    Bokmal to "Vi har avkortet særtillegget ditt mot tilleggspensjonen, slik at du får utbetalt differansen.",
                    Nynorsk to "Vi har avkorta særtillegget mot din tilleggspensjon, slik at du får utbetalt differansen.",
                    English to "The supplementary pension will be deducted from the special supplement so that you will be paid the difference.",
                )
            }
        }

        //vedleggBelopSTAvkortetTPogRedusertTT_001

        showIf(
            harSaertillegg
                    and harTilleggspensjon
                    and regelverkstype.isOneOf(AP1967)
                    and not(harFullTrygdetid)
        ) {
            paragraph {
                text(
                    Bokmal to "Vi har avkortet særtillegget ditt mot tilleggspensjonen, slik at du får utbetalt differansen. Det avkortes også mot trygdetid på samme måte som for grunnpensjonen.",
                    Nynorsk to "Vi har avkorta særtillegget mot din tilleggspensjon, slik at du får utbetalt differansen. Det blir også avkorta mot trygdetid på same måte som for grunnpensjonen.",
                    English to "The supplementary pension will be deducted from the special supplement so that you will be paid the difference. It is also calculated on the same period of national insurance cover as the basic pension.",
                )
            }
        }

        //vedleggBelopSTAvkortetRedusertTT_001
        showIf(
            harSaertillegg and regelverkstype.isOneOf(AP1967) and not(harFullTrygdetid)
        ) {
            paragraph {
                text(
                    Bokmal to "Vi har avkortet særtillegget ditt mot trygdetid på samme måte som for grunnpensjonen.",
                    Nynorsk to "Vi har avkorta særtillegget ditt mot trygdetid på same måte som for grunnpensjonen.",
                    English to "Special supplement is calculated on the same period of national insurance cover as the basic pension.",
                )
            }
        }
    }

}