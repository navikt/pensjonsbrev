package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.BeregningsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.innhold
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.ytelseFoerAvkorting
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregnetTrygdetidAar
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeAnvendt
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeFraGrunnlag
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.mindreEnnFireFemtedelerAvOpptjeningstiden
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.formatBroek
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell

@TemplateModelHelpers
val beregningAvOmstillingsstoenad = createAttachment<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning>(
    title = newText(
        Bokmal to "Beregning av omstillingsstønad",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false
) {
    beregning()
    trygdetid(trygdetid)
    perioderMedRegistrertTrygdetid(trygdetid)
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, OmstillingsstoenadBeregning>.beregning() {
    paragraph {
        textExpr(
            Bokmal to "Full omstillingsstønad beregnes utfra 2,25 ganger folketrygdens ".expr() +
                    "grunnbeløp (G). Dagens verdi av grunnbeløpet er " + grunnbeloep.format() + " kroner. For at du " +
                    "skal få full stønad må avdødes trygdetid være minst 40 år. Er trygdetiden mindre enn 40 år vil " +
                    "stønaden avkortes.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
    paragraph {
        text(
            Bokmal to "Inntekten din avgjør hvor mye du kan få. Stønaden reduseres med 45 prosent av " +
                    "arbeidsinntekt som er over halvparten av grunnbeløpet.",
            Nynorsk to "",
            English to "",
        )
    }

    konverterElementerTilBrevbakerformat(innhold)

    title2 {
        textExpr(
            Bokmal to "".expr() + "Omstillingsstønad fra " + sisteBeregningsperiode.datoFOM.format(),
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }

    includePhrase(Beregningsperiodetabell(trygdetid, sisteBeregningsperiode))

    paragraph {
        text(
            Language.Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales " +
                    "vanligvis i juni hvert år.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Inntekten din",
            Nynorsk to "",
            English to "",
        )
    }

    showIf(sisteBeregningsperiode.inntekt.greaterThan(0)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Vi har lagt til grunn at du har en forventet arbeidsinntekt på " +
                        sisteBeregningsperiode.inntekt.format() + " kroner fra " +
                        sisteBeregningsperiode.datoFOM.format() + ".",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }.orShow {
        paragraph {
            text(
                Bokmal to "Vi har lagt til grunn at du ikke har arbeidsinntekt eller tilsvarende inntekt som " +
                        "omstillingsstønaden skal reduseres etter.",
                Nynorsk to "",
                English to "",
            )
        }
    }
}


private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.trygdetid(
    trygdetid: Expression<Trygdetid>,
) {
    title2 {
        text(
            Bokmal to "Trygdetid",
            Nynorsk to "Trygdetid",
            English to "Period of national insurance coverage",
        )
    }

    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.NASJONAL)) {
        showIf(trygdetid.mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter " +
                            "fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet " +
                            "framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Tabellen under «Perioder med registrert trygdetid» viser full framtidig " +
                            "trygdetid. Siden avdøde har vært medlem i folketrygden i mindre enn 4/5 av tiden " +
                            "mellom fylte 16 år og dødsfallstidspunktet (opptjeningstiden), blir framtidig " +
                            "trygdetid redusert til 40 år minus 4/5 av opptjeningstiden. Dette er mindre enn " +
                            "det tabellen viser.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "For å få full omstillingsstønad må avdødes samlede trygdetid " +
                            "være beregnet til minst 40 år. Avdødes samlede trygdetid er beregnet " +
                            "til " + trygdetid.beregnetTrygdetidAar.format() + " år.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }.orShow {
            paragraph {
                text(
                    Bokmal to "Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden " +
                            "etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis " +
                            "beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "For å få full omstillingsstønad må avdødes trygdetid være beregnet " +
                            "til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede " +
                            "trygdetid er beregnet til " + trygdetid.beregnetTrygdetidAar.format() + " år.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                Bokmal to "For å få full omstillingsstønad må avdødes trygdetid være beregnet til minst 40 år. " +
                        "Trygdetid over 40 år blir ikke tatt med i beregningen.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Omstillingsstønaden din er beregnet etter bestemmelsene i EØS-avtalen " +
                        "fordi vilkårene for rett til omstillingsstønad er oppfylt ved sammenlegging av avdødes " +
                        "opptjeningstid i Norge og andre EØS- eller avtaleland. Trygdetiden er beregnet etter avdødes " +
                        "samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges " +
                        "avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk " +
                        "opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller " +
                        "avtaleland. Avdødes samlede trygdetid er beregnet til " +
                        trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                        trygdetid.prorataBroek.formatBroek() + ".",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }
    showIf(trygdetid.beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.BEST)) {
        paragraph {
            text(
                Bokmal to "For å få full omstillingsstønad må avdødes trygdetid være beregnet til minst 40 år. " +
                        "Trygdetid over 40 år blir ikke tatt med i beregningen. Når grunnlag for omstillingsstønaden " +
                        "er oppfylt etter nasjonale regler, og avdøde også har opptjening av medlemsperioder i land " +
                        "som Norge har trygdeavtale med, skal trygdetid gis etter den beste beregningen av kun " +
                        "nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            text(
                Bokmal to "Ved nasjonal beregning av trygdetid tilsvarer denne det antall år avdøde har vært " +
                        "medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det " +
                        "vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            text(
                Bokmal to "Ved sammenlegging av avdødes opptjeningstid i Norge og andre EØS/avtale-land er " +
                        "trygdetiden beregnet etter avdødes samlede opptjeningstid i disse landene. For å beregne " +
                        "norsk del av denne trygdetiden ganges avdødes samlede opptjeningstid med et forholdstall, " +
                        "som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjeningstid " +
                        "i Norge og andre EØS-land.",
                Nynorsk to "",
                English to "",
            )
        }

        showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Avdødes samlede trygdetid fra avtaleland er beregnet til " +
                            trygdetid.beregnetTrygdetidAar.format() + " år, og forholdstallet til " +
                            trygdetid.prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Avdødes samlede trygdetid er beregnet til " +
                            trygdetid.beregnetTrygdetidAar.format() + " år ved nasjonal opptjening. " +
                            "Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, OmstillingsstoenadBeregning>.perioderMedRegistrertTrygdetid(
    trygdetid: Expression<Trygdetid>
) {
    title2 {
        text(
            Bokmal to "Perioder med registrert trygdetid",
            Nynorsk to "",
            English to "",
        )
    }

    showIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)){
        paragraph {
            text(
                Bokmal to "Tabellen viser perioder avdøde har vært medlem av folketrygden, og registrert " +
                        "fremtidig trygdetid.",
                Nynorsk to "",
                English to "",
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }.orShowIf(trygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                Bokmal to "Tabellen viser perioder avdøde har vært medlem av folketrygden og medlemsperioder " +
                        "avdøde har hatt i land som Norge har trygdeavtale med, som er tatt med i beregningen.",
                Nynorsk to "",
                English to "",
            )
        }
        includePhrase(Trygdetidstabell(trygdetid.trygdetidsperioder))
    }
}
