package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.grunnpensjonSats
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.foedselsDato
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.saertillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.erRedusert_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.kombinertMedAvdod_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaUngUforeAvdod_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaUngUfore_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaYrkesskade_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.alderspensjonGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.epsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.gjeldendeBeregnetPensjonPerManed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.tilleggspensjonGjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.MaanedligPensjonFoerSkattGrunnpensjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.MaanedligPensjonFoerSkattTilleggsPensjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19og20
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
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
            includePhrase(TabellMaanedligPensjonKap19(gjeldendeBeregnetPensjonPerManed))
        }.orShowIf(regelverkstype.isOneOf(AP2016)) {
            includePhrase(
                TabellMaanedligPensjonKap19og20(
                    beregnetPensjon = gjeldendeBeregnetPensjonPerManed,
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

        val grunnpensjonSats = alderspensjonGjeldende.grunnpensjonSats.format()
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
                gjeldendeBeregnetPensjonPerManed = gjeldendeBeregnetPensjonPerManed,
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
                gjeldendeBeregnetPensjonPerManed = gjeldendeBeregnetPensjonPerManed,
                regelverkstype = regelverkstype
            )
        )

        val harTilleggspensjon = gjeldendeBeregnetPensjonPerManed.tilleggspensjon.ifNull(Kroner(0)).greaterThan(0)

        // TODO erstatt alle values
        //vedleggBelopST_002
        showIf(
            bruker.sivilstand.isOneOf(ENSLIG, ENKE, GLAD_EKT, GLAD_PART, SEPARERT, SEPARERT_PARTNER, GIFT, PARTNER)
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

        val harSaertillegg = gjeldendeBeregnetPensjonPerManed.saertillegg_safe.ifNull(Kroner(0)).greaterThan(0)
        // vedleggBelopSTEktefeller_001
        showIf(
            harSaertillegg
                    and bruker.sivilstand.isOneOf(GIFT)
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
                    and bruker.sivilstand.isOneOf(PARTNER)
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
        showIf(bruker.sivilstand.isOneOf(SAMBOER_1_5) and harSaertillegg) {
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
        showIf(bruker.sivilstand.isOneOf(SAMBOER_3_2) and harSaertillegg) {
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
                    and not(gjeldendeBeregnetPensjonPerManed.fullTrygdetid)
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
            harSaertillegg and regelverkstype.isOneOf(AP1967) and not(gjeldendeBeregnetPensjonPerManed.fullTrygdetid)
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
)


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
