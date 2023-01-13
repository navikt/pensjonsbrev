package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.OpplysningerOmBarnetillegg
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.OpplysningerOmMinstetillegg
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.TabellUfoereOpplysninger
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr


val vedleggOpplysningerBruktIBeregningUT =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {
        val inntektsgrenseErUnderTak =
            inntektsAvkortingGjeldende.inntektsgrenseAar.lessThan(inntektsAvkortingGjeldende.inntektstak)

        paragraph {
            val virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom.format()
            val grunnbeloep = beregnetUTPerManedGjeldende.grunnbeloep.format()
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom + " Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner",
                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom + " Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner",
                English to "Data we have used in the calculations as of ".expr() + virkDatoFom + " The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + "."
            )
        }

        includePhrase(
            TabellUfoereOpplysninger(
                ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                yrkesskadeGjeldende = yrkesskadeGjeldende,
                inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                inntektEtterUfoereGjeldende_beloepIEU = inntektEtterUfoereGjeldende_beloepIEU,
                ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                barnetilleggGjeldende = barnetilleggGjeldende,
            )
        )
        val harMinsteytelseSats = minsteytelseGjeldende_sats.ifNull(0.0).greaterThan(0.0)
        // TODO send med info fra brev-malen om deler av vedlegget skal vises eller ikke.
        showIf(harMinsteytelseSats){
            includePhrase(OpplysningerOmMinstetillegg(
                minsteytelseGjeldendeSats = minsteytelseGjeldende_sats,
                ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
            ))
        }

        ifNotNull(barnetilleggGjeldende) { barnetillegg ->
            includePhrase(
                OpplysningerOmBarnetillegg(
                    barnetillegg = barnetillegg,
                    sivilstand = sivilstand,
                    anvendtTrygdetid = trygdetidsdetaljerGjeldende.anvendtTT,
                    harYrkesskade = yrkesskadeGjeldende.notNull(),
                    kravAarsak = kravAarsak,
                )
            )
        }
    }

