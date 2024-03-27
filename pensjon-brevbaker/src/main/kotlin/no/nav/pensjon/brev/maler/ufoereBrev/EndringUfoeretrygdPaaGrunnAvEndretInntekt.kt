package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate


data class EndringUfoeretrygdPaaGrunnAvEndretInntektDto(
    val test: String?,
): BrevbakerBrevdata

@TemplateModelHelpers
object EndringUfoeretrygdPaaGrunnAvEndretInntekt : AutobrevTemplate<EndringUfoeretrygdPaaGrunnAvEndretInntektDto> {
    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_ENDRING_UFOERETRYGD_PGA_INNTEKT
    override val template: LetterTemplate<*, EndringUfoeretrygdPaaGrunnAvEndretInntektDto> = createTemplate(
        name = kode.name,
        letterDataType = EndringUfoeretrygdPaaGrunnAvEndretInntektDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ny beregning av uføretrygd på grunn av endring i opptjening (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        // Felles barn
        val justeringsbeloepPerAarAbsolutt: Expression<Kroner> = Kroner(0).expr() // PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus
        val felles_innvilget: Expression<Boolean> = true.expr()
        val felles_harflerebarn: Expression<Boolean> = true.expr()
        val felles_avkortningsbeloepPerAr: Expression<Kroner> = Kroner(1).expr() // PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr
        val felles_inntektstak: Expression<Kroner> = Kroner(2).expr() // PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak
        val felles_inntektAnnenForelder: Expression<Kroner> = Kroner(3).expr() // PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder
        val felles_brukerInntektTilAvkortning: Expression<Kroner> = Kroner(4).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning
        val felles_inntektBruktIAvkortning: Expression<Kroner> = Kroner(5).expr() // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning
        val felles_fribeloep: Expression<Kroner> = Kroner(6).expr() // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop
        val felles_netto: Expression<Kroner> = Kroner(7).expr() // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto


        // Saerkull
        val saerkull_innvilget: Expression<Boolean> = true.expr()
        val saerkull_harflerebarn: Expression<Boolean> = true.expr()
        val saerkull_justeringsbeloepPerAarAbsolutt: Expression<Kroner> = Kroner(8).expr() // PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus
        //val ifElse(saerkull_harflerebarn, "barna", "barnet"): Expression<Kroner> = Kroner(9).expr() // ifElse(saerkull_harflerebarn, "barna", "barnet")
        val saerkull_avkortningsbeloepPerAar: Expression<Kroner> = Kroner(10).expr() // PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr
        val saerkull_inntektstak: Expression<Kroner> = Kroner(11).expr() // PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak
        val saerkull_inntektBruktIAvkortning: Expression<Kroner> = Kroner(12).expr() // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning
        val saerkull_fribeloep: Expression<Kroner> = Kroner(13).expr() // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop
        val saerkull_netto: Expression<Kroner> = Kroner(14).expr() // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto


        val bormedSivilstand: Expression<BorMedSivilstand> = BorMedSivilstand.EKTEFELLE.expr() // PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT
        val nettoAkkumulertPlussRestAar: Expression<Kroner> = Kroner(17).expr() // PE_UT_NettoAkk_pluss_NettoRestAr
        val virkFomAar: Expression<Year> = Year(2020).expr() // PE_UT_VirkningFomAr
        val virkFomAarMinusEn: Expression<Year> = Year(2019).expr() // PE_UT_VirkningFomArMinusEttAr
        val virkningstidspunktMinus1Aar: Expression<Year> = Year(2019).expr() // PE_UT_VirkningstidpunktArMinus1Ar
        val virkFomDato: Expression<LocalDate> = LocalDate.of(2020,1,1).expr() // PE_VedtaksData_VirkningFOM

        // Ordinær uføretrygd
        val uforetrygdordinaer_avkortningsbeloepPerAar: Expression<Kroner> = Kroner(19).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortingsbelopPerAr
        val uforetrygdordinaer_forventetInntekt: Expression<Kroner> = Kroner(20).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
        val uforetrygdordinaer_inntektsgrense: Expression<Kroner> = Kroner(21).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val uforetrygdordinaer_inntektstak: Expression<Kroner> = Kroner(22).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        val uforetrygdordinaer_netto: Expression<Kroner> = Kroner(23).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto
        val uforetrygdordinaer_nettoAkk: Expression<Kroner> = Kroner(24).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk

        val ufoeretrygdBeregning_totalNetto: Expression<Kroner> = Kroner(25).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
        val ufoeretrygdBeregning_grunnbeloep: Expression<Kroner> = Kroner(26).expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
        val ufoeretrygdBeregning_ufoeregrad: Expression<Int> = 27.expr() // PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad

        val harEktefelletillegg: Expression<Boolean> = true.expr()
        val harGjenlevendeTillegg: Expression<Boolean> = true.expr()
        val borIUtlandet: Expression<Boolean> = true.expr()
        title {
            text(
                //TODO
                Bokmal to "",
                Nynorsk to "",
            )
        }
        outline {

            //TBU2249
            paragraph{
                textExpr(
                    Bokmal to "Vi har mottatt nye opplysninger om inntekten din til deg eller din ".expr() + bormedSivilstand.ubestemtForm() + ". Inntekten til din " + bormedSivilstand.ubestemtForm() + " har kun betydning for størrelsen på barnetillegget for " + ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldreditt. Utbetalingen av uføretrygden din og barnetillegget ditt barnetilleggene dine er derfor endret fra " + virkFomDato.format() + ".",
                    Nynorsk to "Vi har fått nye opplysningar om inntekta di til deg eller ".expr() + bormedSivilstand.bestemtForm() + " din. Inntekta til " + bormedSivilstand.bestemtForm() + " din har berre betydning for storleiken på barnetillegget for " + ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge foreldra sineditt. Utbetalinga av uføretrygda di og barnetillegget ditt barnetillegga dine er derfor endra frå " + virkFomDato.format() + ".",
                )
            }

            //TBU3403
            paragraph{
                textExpr(
                    Bokmal to "Vi vil bruke en inntekt på ".expr() + uforetrygdordinaer_forventetInntekt.format() + " kroner når vi reduserer uføretrygden din for " + virkFomAar.format() + ". Har du ikke meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekten justert opp til dagens verdi.",
                    Nynorsk to "Vi vil bruke ei inntekt på ".expr() + uforetrygdordinaer_forventetInntekt.format() + " kroner når vi reduserer uføretrygda di for " + virkFomAar.format() + ". Har du ikkje meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekta justert opp til dagens verdi.",
                )
            }

            paragraph{
                textExpr(
                    Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + virkFomAarMinusEn.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkFomAar.format() + ".",
                    Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + virkFomAarMinusEn.format() + ", er inntekta også justert opp slik at den gjeld for heile " + virkFomAar.format() + ".",
                )
            }

            paragraph{
                textExpr(
                    Bokmal to "Fordi du fyller 67 år i ".expr() + virkFomAar.format() + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd.",
                    Nynorsk to "Fordi du fyljer 67 år i ".expr() + virkFomAar.format() + ", er inntekta justert ut frå talet på månadar du får uføretrygd.",
                )
            }
            //TBU4016
            paragraph{
                textExpr(
                    Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + saerkull_inntektBruktIAvkortning.format() + " kroner.",
                    Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + saerkull_inntektBruktIAvkortning.format() + " kroner.",
                )
            }
            //TBU4001
            paragraph{
                textExpr(
                    Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + felles_inntektBruktIAvkortning.format() + " kroner for " + ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre. For " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + saerkull_inntektBruktIAvkortning.format() + " kroner.",
                    Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + felles_inntektBruktIAvkortning.format() + " kroner for " + ifElse(felles_harflerebarn, "barna", "barnet") + " som bur med begge sine foreldra. For " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på " + saerkull_inntektBruktIAvkortning.format() + " kroner.",
                )
            }
            //TBU4002
            paragraph{
                textExpr(
                    Bokmal to "Vi vil bruke en inntekt på ".expr() + felles_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt for " + ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre for " + virkFomAar.format() + ". For " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + saerkull_inntektBruktIAvkortning.format() + " kroner. Har du ikke meldt inn nye inntekter for " + virkFomAar.format() + ", er inntektene justert opp til dagens verdi.",
                    Nynorsk to "Vi vil bruke ei inntekt på ".expr() + felles_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt for " + ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge foreldra sine for " + virkFomAar.format() + ". For " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på " + saerkull_inntektBruktIAvkortning.format() + " kroner. Har du ikkje meldt inn ei nye inntekter for " + virkFomAar.format() + ", er inntektainntektene justert opp til dagens verdi.",
                )
            }
            //TBU4017
            paragraph{
                textExpr(
                    Bokmal to "Vi vil bruke en inntekt på ".expr() + saerkull_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikke meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekten justert opp til dagens verdi.",
                    Nynorsk to "Vi vil bruke ei inntekt på ".expr() + saerkull_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikkje meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekta justert opp til dagens verdi.",
                )
            }
            //TBU4013
            paragraph{
                textExpr(
                    Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + virkningstidspunktMinus1Aar.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkFomAar.format() + ".",
                    Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + virkningstidspunktMinus1Aar.format() + ", er inntekta justert opp slik at den gjeld for heile " + virkFomAar.format() + ".",
                )
            }
            //TBU4003
            paragraph{
                textExpr(
                    Bokmal to "Fordi du ikke har barnetillegg for ".expr() + ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre for " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene hele året er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg.",
                    Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge foreldra for " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra heile året er inntektene og fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg.",
                )
            }
            //TBU4004
            paragraph{
                textExpr(
                    Bokmal to "Forventer du og din ".expr() + bormedSivilstand.bestemtForm() + " å tjene noe annet i " + virkFomAar.format() + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på nav.no.",
                    Nynorsk to "Forventar du og ".expr() + bormedSivilstand.bestemtForm() + " din å tene noko anna i " + virkFomAar.format() + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på nav.no.",
                )
            }

            includePhrase(Ufoeretrygd.Beloep(ufoeretrygdBeregning_totalNetto, harEktefelletillegg, true.expr(), harGjenlevendeTillegg, felles_innvilget, saerkull_innvilget))
            paragraph{
                textExpr(
                    Bokmal to "Du får ".expr() + ufoeretrygdBeregning_totalNetto.format() + " kroner i uføretrygd per måned før skatt.",
                    Nynorsk to "Du får ".expr() + ufoeretrygdBeregning_totalNetto.format() + " kroner i uføretrygd per månad før skatt.",
                )
            }




            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(true.expr()))
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)

            //TBU2253
            paragraph{
                text(
                    Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det er bare den delen av inntekten din som overstiger inntektsgrensen som vil gi en reduksjon av uføretrygden.",
                    Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det er berre den delen av inntekta di som kjem over inntektsgrensa som gir ein reduksjon av uføretrygda di.",
                )
            }

            paragraph{
                text(
                    Bokmal to "Uføretrygden din reduseres fordi du tjener over inntektsgrensen din. Selv om du får en reduksjon lønner det seg likevel å jobbe ved siden av uføretrygden. Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert. Utbetalingen av uføretrygden din økes fordi du tjener under inntektsgrensen din. Det betyr at uføretrygden ikke lenger er redusert. Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året.",
                    Nynorsk to "Uføretrygda di blir redusert fordi du tener over inntektsgrensa di. Sjølv om du får ein reduksjon, lønner det seg likevel å jobbe ved sida av uføretrygda. Endringa i inntekta di gjer at uføretrygda ikkje lenger er redusert. Utbetalinga av uføretrygda aukar fordi du tener under inntektsgrensa di. Det betyr at uføretrygda ikkje lenger er redusert. Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året.",
                )
            }
            //TBU3374
            paragraph{
                text(
                    Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                    Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det lønner seg likevel å jobbe fordi inntekt og uføretrygd vil alltid vere høgare enn uføretrygd åleine.",
                )
            }
            //TBU4005
            paragraph{
                textExpr(
                    Bokmal to "Siden du har en inntekt på ".expr() + uforetrygdordinaer_forventetInntekt.format() + " kroner trekker vi " + uforetrygdordinaer_avkortningsbeloepPerAar.format() + " kroner fra uføretrygden i for neste år.",
                    Nynorsk to "Fordi du har ei inntekt på ".expr() + uforetrygdordinaer_forventetInntekt.format() + " kroner trekkjer vi " + uforetrygdordinaer_avkortningsbeloepPerAar.format() + " kroner frå uføretrygda i for neste år.",
                )
            }
            //TBU2258
            paragraph{
                textExpr(
                    Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr()
                            + uforetrygdordinaer_inntektstak.format() + " kroner. Inntekten vi har brukt er "
                            + uforetrygdordinaer_forventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                    Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr()
                            + uforetrygdordinaer_inntektstak.format() + " kroner. Inntekta vi har brukt er "
                            + uforetrygdordinaer_forventetInntekt.format() + " kroner og du vil ikkje få utbetalt uføretrygd resten av året.",
                )
            }
            //TBU3375
            paragraph{
                textExpr(
                    Bokmal to "Det utbetales ikke uføretrygd når inntekten din utgjør mer enn inntektsgrensen, det vil si ".expr()
                            + uforetrygdordinaer_inntektsgrense.format() + " kroner. Inntekten vi har brukt er " + uforetrygdordinaer_forventetInntekt.format()
                            + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                    Nynorsk to "Det blir ikkje utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr()
                            + uforetrygdordinaer_inntektsgrense.format() + " kroner. Inntekta vi har brukt er " + uforetrygdordinaer_forventetInntekt.format()
                            + " kroner og du vil derfor ikkje få utbetalt uføretrygd resten av året.",
                )
            }
            //TBU4006
            paragraph{
                textExpr(
                    Bokmal to "Inntekten din har også betydning for hva du får utbetalt i barnetillegg. FordiFor ".expr()
                            + ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre, bruker vi i tillegg din "
                            + bormedSivilstand.bestemtForm() + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. For "
                            + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din. Uføretrygden din og gjenlevendetillegget ditt regnes med som inntekt.",

                    Nynorsk to "Inntekta di har også betydning for kva du får utbetalt i barnetillegg. FordiFor ".expr()
                            + ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge foreldra sine, bruker vi i tillegg "
                            + bormedSivilstand.bestemtForm() + " din si inntekt når vi fastset storleiken på barnetillegget ditt. For "
                            + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. Uføretrygda di og attlevandetillegget ditt er rekna med som inntekt.",
                )
            }
            //TBU4007
            paragraph{
                textExpr(
                    Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() +
                            felles_brukerInntektTilAvkortning.format() + " kroner og inntekten til din " + bormedSivilstand.bestemtForm() + " på " +
                            felles_inntektAnnenForelder.format() + " kroner. Folketrygdens grunnbeløp på inntil " +
                            ufoeretrygdBeregning_grunnbeloep.format() + " kroner er holdt utenfor din " + bormedSivilstand.bestemtForm()
                            + "s inntekt. Til sammen utgjør disse inntektene " + felles_inntektBruktIAvkortning.format() + " kroner. Dette beløpet er høyerelavere enn fribeløpsgrensen på "
                            + felles_fribeloep.format() + " kroner. Derfor er barnetillegget for " +
                            ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre ikke lenger redusert. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " +
                            ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",

                    Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() +
                            felles_brukerInntektTilAvkortning.format() + " kroner og inntekta til " + bormedSivilstand.bestemtForm() + " din på " +
                            felles_inntektAnnenForelder.format() + " kroner. Grunnbeløpet i folketrygda på inntil " +
                            ufoeretrygdBeregning_grunnbeloep.format() + " kroner er held utanfor inntekta til " + bormedSivilstand.bestemtForm()
                            + " din. Til saman utgjer desse inntektene " + felles_inntektBruktIAvkortning.format() + " kroner. Dette beløpet er høgarelågare enn fribeløpet på "
                            + felles_fribeloep.format() + " kroner. Derfor er barnetillegget for " +
                            ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge sine foreldra ikkje lenger redusert. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " +
                            ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge foreldra sine. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                )
            }
            //TBU4008
            paragraph{
                textExpr(
                    Bokmal to "Barnetillegget for ".expr() + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på " + saerkull_inntektBruktIAvkortning.format() + " kroner. Dette er høyerelavere enn fribeløpsgrensen på " + saerkull_fribeloep.format() + " kroner. Dette barnetillegget er derfor ikke lenger også redusert. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                    Nynorsk to "Barnetillegget for ".expr() + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på " + saerkull_inntektBruktIAvkortning.format() + " kroner. Dette er høgarelågere enn fribeløpet på " + saerkull_fribeloep.format() + " kroner. Derfor er barnetillegget ikkje lenger også redusert. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                )
            }
            //TBU4009
            paragraph{
                textExpr(
                    Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr()
                            + saerkull_inntektBruktIAvkortning.format() + " kroner. Dette er høyerelavere enn fribeløpsgrensen på " +
                            saerkull_fribeloep.format() + " kroner. Barnetillegget er derfor ikke lenger redusert. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",

                    Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr()
                            + saerkull_inntektBruktIAvkortning.format() + " kroner. Dette beløpet er høgarelågare enn fribeløpet på " +
                            saerkull_fribeloep.format() + " kroner. Derfor er barnetillegget ikkje lenger redusert. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                )
            }
            //TBU4010
            paragraph{
                textExpr(
                    Bokmal to "Barnetillegget for ".expr() + ifElse(felles_harflerebarn, "barna", "barnet")
                            + " som bor med begge sine foreldre blir ikke utbetalt fordi den samlede inntekten til deg og din " + bormedSivilstand.ubestemtForm()
                            + " er høyere enn " + felles_inntektstak.format() + " kroner.",

                    Nynorsk to "Barnetillegget for ".expr() + ifElse(felles_harflerebarn, "barna", "barnet")
                            + " som bur saman med begge forelda sine blir ikkje utbetalt fordi den samla inntekta til deg og " + bormedSivilstand.bestemtForm()
                            + " din er høgare enn " + felles_inntektstak.format() + " kroner.",
                )
            }
            //TBU4011
            paragraph{
                textExpr(
                    Bokmal to "Barnetillegget for ".expr() + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene blir ikke utbetalt fordi inntekten din er høyere enn " + saerkull_inntektstak.format() + " kroner.",
                    Nynorsk to "Barnetillegget for ".expr() + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra blir ikkje utbetalt fordi inntekta di er høgare enn " + saerkull_inntektstak.format() + " kroner.",
                )
            }
            //TBU4012
            paragraph{
                textExpr(
                    Bokmal to "Fordi du ikke har barnetillegg for ".expr() + ifElse(felles_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldre for " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene hele året er inntektene og fribeløpet justert slik at det kun gjelder for den perioden du mottar barnetillegg. Fordi sivilstanden din har endret seg er inntektene og fribeløpet justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg.",
                    Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + ifElse(felles_harflerebarn, "barna", "barnet") + " som bur saman med begge foreldra sine for " + ifElse(saerkull_harflerebarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra heile året, er inntektene og fribeløpet justert slik at detdei berre gjeld for den perioden du får barnetillegg. Fordi sivilstanden din har endra seg, er inntektene og fribeløpet justert slik at dei berre gjeld for den framtidige perioden du får barnetillegg.",
                )
            }
            //TBU1133
            paragraph{
                text(
                    Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
                    Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga».",
                )
            }
            //TBU2263
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12.",
                )
            }
            //TBU2264
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygda.",
                )
            }
            //TBU2265
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8 og forskrift om overgangsregler for barnetillegg i uføretrygda.",
                )
            }
            //TBU2266
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
                )
            }
            //TBU2267
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12.",
                )
            }
            //TBU2268
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8.",
                )
            }
            //TBU4014
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
                )
            }
            //TBU4015
            paragraph{
                text(
                    Bokmal to "Hva får du i uføretrygd framover?",
                    Nynorsk to "Kva får du i uføretrygd framover?",
                )
            }
            //TBU4044
            paragraph{
                textExpr(
                    Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() + nettoAkkumulertPlussRestAar.format() + " kroner. Hittil i år har du fått utbetalt " + uforetrygdordinaer_nettoAkk.format() + " kroner. Du har derfor rett til en utbetaling av uføretrygd på " + uforetrygdordinaer_netto.format() + " kroner per måned for resten av året.",
                    Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjere ".expr() + nettoAkkumulertPlussRestAar.format() + " kroner. Hittil i år har du fått utbetalt " + uforetrygdordinaer_nettoAkk.format() + " kroner. Du har derfor rett til ei utbetaling av uføretrygd på " + uforetrygdordinaer_netto.format() + " kroner per månad for resten av året.",
                )
            }
            //TBU4045
            paragraph{
                textExpr(
                    Bokmal to "Selv om uføretrygden din er redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + ufoeretrygdBeregning_ufoeregrad.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
                    Nynorsk to "Sjølv om uføretrygda di er redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + ufoeretrygdBeregning_ufoeregrad.format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.",
                )
            }
            //TBU4046
            paragraph{
                text(
                    Bokmal to "Hva får du i barnetillegg framover?",
                    Nynorsk to "Kva får du i barnetillegg framover?",
                )
            }
            //TBU4047
            paragraph{
                textExpr(
                    Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + bormedSivilstand.ubestemtForm() + " er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er " + felles_avkortningsbeloepPerAr.format() + " kroner. Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor lagt tiltrukket fra " + justeringsbeloepPerAarAbsolutt.format() + " kroner i det vi reduserer barnetillegget med for resten av året. Du har derfor rett til en utbetaling av barnetillegg på " + felles_netto.format() + " kroner per måned for resten av året.",
                    Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + bormedSivilstand.bestemtForm() + " din er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er " + felles_avkortningsbeloepPerAr.format() + " kroner. Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor lagt tiltrekt frå " + justeringsbeloepPerAarAbsolutt.format() + " kroner i det vi har redusert barnetillegget med for resten av året. Du har derfor rett til ei utbetaling av barnetillegg på " + felles_netto.format() + " kroner per månad for resten av året.",
                )
            }
            //TBU4048
            paragraph{
                textExpr(
                    Bokmal to "Ut fra den samlede inntekten din er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er ".expr() + saerkull_avkortningsbeloepPerAar.format() + " kroner. Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor lagt tiltrukket fra " + saerkull_justeringsbeloepPerAarAbsolutt.format() + " kroner i det vi reduserer barnetillegget med for resten av året. Du har derfor rett til en utbetaling av barnetillegg på " + saerkull_netto.format() + " kroner per måned for resten av året.",
                    Nynorsk to "Ut frå den samla inntekta di er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er ".expr() + saerkull_avkortningsbeloepPerAar.format() + " kroner. Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor lagt tiltrekt frå " + saerkull_justeringsbeloepPerAarAbsolutt.format() + " kroner i det vi reduserte barnetillegget med for resten av året. Du har derfor rett til ei utbetaling av barnetillegg på " + saerkull_netto.format() + " kroner per månad for resten av året.",
                )
            }
            //TBU4049
            paragraph{
                textExpr(
                    Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + ifElse(felles_harflerebarn or saerkull_harflerebarn, "barna", "barnet") + " som bor med begge sine foreldreikke bor sammen med begge foreldrene.",
                    Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + ifElse(felles_harflerebarn or saerkull_harflerebarn, "barna", "barnet") + " som bur med begge foreldraikkje bur saman med begge foreldra.",
                )
            }
            //TBU1214
            paragraph{
                text(
                    Bokmal to "For deg som mottar gjenlevendetillegg",
                    Nynorsk to "For deg som mottar tillegg for attlevande ektefelle",
                )
            }
            //TBU2272
            paragraph{
                text(
                    Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med.",
                    Nynorsk to "Du får attlevandetillegg i uføretrygda di. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosenten som vi reduserer uføretrygda di med.",
                )
            }
            //TBU2273
            paragraph{
                text(
                    Bokmal to "Gjenlevendetillegget er redusert ut fra den innmeldte inntekten.",
                    Nynorsk to "Attlevandetillegget er redusert ut frå den innmelde inntekta.",
                )
            }
            //TBU2274
            paragraph{
                text(
                    Bokmal to "Gjenlevendetillegget er økt ut fra den innmeldte inntekten.",
                    Nynorsk to "Attlevandetillegget er auka ut frå den innmelde inntekta.",
                )
            }
            //TBU1133 TODO deduplicate
            paragraph{
                text(
                    Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
                    Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga».",
                )
            }
            //TBU2275
            paragraph{
                text(
                    Bokmal to "For deg som mottar ektefelletillegg",
                    Nynorsk to "For deg som får ektefelletillegg",
                )
            }
            //TBU2276
            paragraph{
                text(
                    Bokmal to "Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av inntektsendringer.",
                    Nynorsk to "Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av inntektsendringar.",
                )
            }
            //TBU4022
            paragraph{
                text(
                    Bokmal to "Du må melde fra om endringer i inntekt",
                    Nynorsk to "Du må melde frå om endringar i inntekt",
                )
            }
            //TBU2278
            paragraph{
                text(
                    Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden og barnetillegget blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL.",
                    Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda og barnetillegget blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på $NAV_URL.",
                )
            }
            //TBU2279
            paragraph{
                textExpr(
                    Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + uforetrygdordinaer_inntektstak.format() + " kroner per år. Inntekten er justert opp til dagens verdi.",
                    Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + uforetrygdordinaer_inntektstak.format() + " kroner per år. Inntekta er justert opp til dagens verdi.",
                )
            }
            //TBU3740
            paragraph{
                textExpr(
                    Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn inntektsgrensen din, det vil si ".expr() + uforetrygdordinaer_inntektsgrense.format() + " kroner per år.",
                    Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + uforetrygdordinaer_inntektsgrense.format() + " kroner per år.",
                )
            }
            //TBU2280
            paragraph{
                text(
                    Bokmal to "Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd og barnetillegg i løpet av året. Dette gjør vi når likningen fra Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd og barnetillegg, vil vi utbetale dette beløpet til deg. Hvis du har fått utbetalt for mye i uføretrygd og barnetillegg, må du betale dette tilbake.",
                    Nynorsk to "Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd og barnetillegg i løpet av året. Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd og barnetillegg, vil vi betale deg dette beløpet. Dersom du har fått utbetalt for mykje i uføretrygd og barnetillegg, må du betale dette tilbake.",
                )
            }
            //TBU2282
            paragraph{
                text(
                    Bokmal to "Inntekter som ikke skal føre til reduksjon av uføretrygden",
                    Nynorsk to "Inntekter som ikkje skal føra til reduksjon av uføretrygda",
                )
            }

            paragraph{
                text(
                    Bokmal to "Det kan gjøres unntak for enkelte inntektstyper som ikke skal føre til reduksjon av uføretrygden. Dette kan gjelde følgende:",
                    Nynorsk to "Det kan gjerast unntak for enkelte inntektstypar som ikkje skal føra til reduksjon av uføretrygda. Dette kan gjelda følgjande:",
                )
            }
            paragraph {
                text(
                    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter",
                    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter",
                )
                list {
                    item {
                        text(
                            Bokmal to "Skadeerstatningsloven § 3-1",
                            Nynorsk to "Skadeerstatningsloven § 3-1",
                        )

                    }
                    item {
                        text(
                            Bokmal to "Yrkesskadeforsikringsloven § 13",
                            Nynorsk to "Yrkesskadeforsikringsloven § 13",
                        )

                    }
                    item {
                        text(
                            Bokmal to "Pasientskadeloven § 4 første ledd",
                            Nynorsk to "Pasientskadeloven § 4 første ledd",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:",
                    Nynorsk to "Inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes:",
                )
                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger for et arbeidsforhold som er avsluttet",
                            Nynorsk to "Utbetalte feriepengar for eit arbeidsforhold som er avslutta",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten",
                            Nynorsk to "Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere",
                            Nynorsk to "Produksjonstillegg og andre overføringar til gardbrukarar",
                        )
                    }

                }
            }
            paragraph{
                text(
                    Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter at du har slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
                    Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar at du har slik inntekt, kan vi gjera ei ny berekning av uføretrygda di.",
                )
            }


            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(borIUtlandet))
            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)
        }
    }
}