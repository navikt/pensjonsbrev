package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harKravaarsakEndringInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harKravaarsakSoeknadBT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.norskTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningAvdoedUfoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningUfoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.utenlandskTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.fastsattTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.har40AarFastsattTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harFramtidigTrygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harFramtidigTrygdetidNorsk
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harLikUfoeregradOgYrkesskadegrad
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harTrygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harYrkesskadeOppfylt
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

fun createVedleggOpplysningerBruktIBeregningUT(skalViseMinsteytelse: Boolean, skalViseBarnetillegg: Boolean) =
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
        val grunnbeloep = beregnetUTPerManedGjeldende.grunnbeloep.format()

        paragraph {
            val virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom.format()
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom + ". Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner.",
                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom + ". Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner.",
                English to "Data we have used in the calculations as of ".expr() + virkDatoFom + ". The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + ".",
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
                inntektEtterUfoereGjeldendeBeloep = inntektEtterUfoereGjeldende_beloepIEU,
                ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                barnetilleggGjeldende = barnetilleggGjeldende,
            )
        )
        if (skalViseMinsteytelse) {
            val harMinsteytelseSats = minsteytelseGjeldende_sats.ifNull(0.0).greaterThan(0.0)
            showIf(harMinsteytelseSats) {
                includePhrase(
                    OpplysningerOmMinstetillegg(
                        minsteytelseGjeldendeSats = minsteytelseGjeldende_sats,
                        ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                        ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                        inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                        inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                    )
                )
            }
        }

        if (skalViseBarnetillegg) {
            ifNotNull(barnetilleggGjeldende) { barnetillegg ->
                includePhrase(
                    OpplysningerOmBarnetillegg(
                        barnetillegg = barnetillegg,
                        sivilstand = sivilstand,
                        anvendtTrygdetid = trygdetidsdetaljerGjeldende.anvendtTT,
                        harYrkesskade = yrkesskadeGjeldende.notNull(),
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                        fraOgMedDatoErNesteAar = fraOgMedDatoErNesteAar,
                    )
                )
            }
        }
        showIf(not(harKravaarsakSoeknadBT)) {
            ifNotNull(opptjeningUfoeretrygd) { opptjening ->
                includePhrase(
                    TabellInntekteneBruktIBeregningen(
                        beregningGjeldendeFraOgMed = beregnetUTPerManedGjeldende.virkDatoFom,
                        harAvdoed = false.expr(),
                        opptjeningUfoeretrygd = opptjening,
                    )
                )
            }

            ifNotNull(opptjeningAvdoedUfoeretrygd) { opptjening ->
                includePhrase(
                    TabellInntekteneBruktIBeregningen(
                        beregningGjeldendeFraOgMed = beregnetUTPerManedGjeldende.virkDatoFom,
                        harAvdoed = true.expr(),
                        opptjeningUfoeretrygd = opptjening,
                    )
                )
            }
            includePhrase(
                TrygdetidenDin(
                    beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                    fastsattTrygdetid = trygdetidGjeldende.fastsattTrygdetid,
                    har40AarFastsattTrygdetid = trygdetidGjeldende.har40AarFastsattTrygdetid,
                    harFramtidigTrygdetidEOS = trygdetidGjeldende.harFramtidigTrygdetidEOS,
                    harFramtidigTrygdetidNorsk = trygdetidGjeldende.harFramtidigTrygdetidNorsk,
                    harLikUfoeregradOgYrkesskadegrad = trygdetidGjeldende.harLikUfoeregradOgYrkesskadegrad,
                    harTrygdetidsgrunnlag = trygdetidGjeldende.harTrygdetidsgrunnlag,
                    harYrkesskadeOppfylt = trygdetidGjeldende.harYrkesskadeOppfylt,
                )
            )
            includePhrase(
                TabellTrygdetiden(
                    beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                    trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                    trygdetidGjeldende = trygdetidGjeldende,
                    norskTrygdetid = norskTrygdetid,
                    utenlandskTrygdetid = utenlandskTrygdetid,
                )
            )

        }
    }








