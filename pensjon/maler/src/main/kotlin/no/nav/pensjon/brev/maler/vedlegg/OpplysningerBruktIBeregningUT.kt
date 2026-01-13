package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.borMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harKravaarsakEndringInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.OpplysningerOmBarnetillegg
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.OpplysningerOmMinstetillegg
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.TabellUfoereOpplysninger
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

fun createVedleggOpplysningerBruktIBeregningUT(skalViseMinsteytelse: Boolean, skalViseBarnetillegg: Boolean) =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                nynorsk { +"Opplysningar om utrekninga" },
                english { +"Information about calculations" }
            )
        },
        includeSakspart = false,
    ) {
        val inntektsgrenseErUnderTak =
            inntektsAvkortingGjeldende.inntektsgrenseAar.lessThan(inntektsAvkortingGjeldende.inntektstak)
        val grunnbeloep = beregnetUTPerManedGjeldende.grunnbeloep.format()

        paragraph {
            val virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom.format()
            text(
                bokmal { + "Opplysninger vi har brukt i beregningen fra " + virkDatoFom + ". Folketrygdens grunnbeløp (G) benyttet i beregningen er " + grunnbeloep + "." },
                nynorsk { + "Opplysningar vi har brukt i utrekninga frå " + virkDatoFom + ". Grunnbeløpet i folketrygda (G) nytta i utrekninga er " + grunnbeloep + "." },
                english { + "Data we have used in the calculations as of " + virkDatoFom + ". The National Insurance basic amount (G) used in the calculation is " + grunnbeloep + "." },
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
                erUngUfoer = ungUfoerGjeldende_erUnder20Aar.notNull(),
                trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                barnetilleggGjeldende = barnetilleggGjeldende,
                harMinsteytelse = minsteytelseGjeldende_sats.notNull(),
                borMedSivilstand = borMedSivilstand,
                brukersSivilstand = sivilstand,
            )
        )
        if(skalViseMinsteytelse) {
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

        if(skalViseBarnetillegg) {
            ifNotNull(barnetilleggGjeldende) { barnetillegg ->
                includePhrase(
                    OpplysningerOmBarnetillegg(
                        barnetillegg = barnetillegg,
                        anvendtTrygdetid = trygdetidsdetaljerGjeldende.anvendtTT,
                        harYrkesskade = yrkesskadeGjeldende.notNull(),
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                        fraOgMedDatoErNesteAar = fraOgMedDatoErNesteAar,
                    )
                )
            }
        }
    }

