package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.grunnpensjonSats
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.foedselsDato
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.BrukerSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.EPSgjeldendeSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.avdodFlyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.beregnetEtter_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.GjeldendeBeregnetPensjonSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.ensligPaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.InstitusjonsoppholdGjeldendeSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.erRedusert_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.kombinertMedAvdod_safe
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.alderspensjonGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.epsGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.gjeldendeBeregnetPensjonPerManed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.institusjonsoppholdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.tilleggspensjonGjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.MaanedligPensjonFoerSkattGrunnpensjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19og20
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
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

        val beregnetSomEnsligPgaInstopphold = (institusjonsoppholdGjeldende.aldersEllerSykehjem_safe.ifNull(false)
                or institusjonsoppholdGjeldende.ensligPaInst_safe.ifNull(false)
                or institusjonsoppholdGjeldende.epsPaInstitusjon_safe.ifNull(false)
                or institusjonsoppholdGjeldende.fengsel_safe.ifNull(false)
                or institusjonsoppholdGjeldende.helseinstitusjon_safe.ifNull(false))

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

        val tilleggsPensjonKombinertMedAvdoed = tilleggspensjonGjeldende.kombinertMedAvdod_safe.ifNull(false)
        val tilleggsPenssjonErRedusert = tilleggspensjonGjeldende.erRedusert_safe.ifNull(false)
        val harTilleggspensjon = gjeldendeBeregnetPensjonPerManed.tilleggspensjon.ifNull(Kroner(0)).greaterThan(0)
        //vedleggBelopFullTP_001
        showIf(
            (
                    not(tilleggsPenssjonErRedusert)
                            and not(tilleggsPensjonKombinertMedAvdoed)
                            and regelverkstype.isOneOf(AP2011, AP2016)
                    )
                    or (harTilleggspensjon and regelverkstype.isOneOf(AP1967))
        ) {
            paragraph {
                includePhrase(TilleggspensjonenBold)
                text(
                    Bokmal to "din avhenger av antall år med pensjonspoeng og størrelsen på pensjonspoengene. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp.",
                    Nynorsk to "din er avhengig av kor mange år du har hatt med pensjonspoeng, og storleiken på pensjonspoenga. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda.",
                    English to "depends on the number of years you earned pension points and how many pension points you earned. You receive pension points for years when you had an income greater than the National Insurance basic amount (G).",
                )
            }
        }

        //veldeggBelopRedusTP_001
        showIf(tilleggsPenssjonErRedusert and regelverkstype.isOneOf(AP2011, AP2016)) {
            paragraph {
                includePhrase(TilleggspensjonenBold)
                text(
                    Bokmal to "din avhenger av antall år med pensjonspoeng og størrelsen på pensjonspoengene. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp. Du får ikke full tilleggspensjon fordi du har opptjent pensjonspoeng i mindre enn 40 år.",
                    Nynorsk to "din er avhengig av kor mange år du har hatt med pensjonspoeng, og storleiken på pensjonspoenga. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda. Du får ikkje full tilleggspensjon fordi du har tent opp pensjonspoeng i mindre enn 40 år.",
                    English to "depends on the number of years you earned pension points and how many pension points you earned. You receive pension points for years when you had an income greater than the National Insurance basic amount (G). You do not qualify for a full supplementary pension because you have earned pension points for less than 40 years.",
                )
            }
        }

        //vedleggBelopTPAvdod_001
        showIf(tilleggsPensjonKombinertMedAvdoed and regelverkstype.isOneOf(AP2011, AP2016, AP1967)) {
            paragraph {
                includePhrase(TilleggspensjonenBold)
                text(
                    Bokmal to "til en gjenlevende alderspensjonist kan enten bestå av pensjonistens egen tilleggspensjon eller 55 prosent av summen av pensjonistens egen tilleggspensjon og den avdødes tilleggspensjon. Tilleggspensjonen din er gitt etter det siste alternativet, da dette gir det høyeste beløpet for deg.",
                    Nynorsk to "til ein attlevande alderspensjonist kan anten bestå av pensjonistens eigen tilleggspensjon eller 55 prosent av summen av pensjonistens eigen tilleggspensjon og tilleggspensjonen til den avdøde. Tilleggspensjonen din er gitt etter det siste alternativet då det gir det høgaste beløpet for deg.",
                    English to "for a widowed old age pensioner can be calculated either as the pensioner's own supplementary pension or as 55 percent of the sum of the pensioner's own supplementary pension and the deceased's supplementary pension. Your supplementary pension has been calculated using the latter method, as this is more beneficial for you.",
                )
            }
        }

        


    }
)

private object TilleggspensjonenBold: ParagraphPhrase<LangBokmalNynorskEnglish>(){
    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Tilleggspensjonen ",
            Nynorsk to "Tilleggspensjonen ",
            English to "Your supplementary pension ",
            FontType.BOLD
        )
    }

}
