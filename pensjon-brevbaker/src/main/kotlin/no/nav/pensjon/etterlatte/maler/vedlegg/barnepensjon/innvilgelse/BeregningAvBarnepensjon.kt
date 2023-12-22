package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
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
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBP
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.aarTrygdetid
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.beregningsMetodeAnvendt
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.beregningsMetodeFraGrunnlag
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBPSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.formatBroek
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell

@TemplateModelHelpers
val beregningAvBarnepensjonGammeltOgNyttRegelverk = createAttachment(
    title = newText(
        Bokmal to "Beregning av barnepensjon",
        Nynorsk to "Utrekning av barnepensjon",
        English to "Calculation of Children’s Pension",
    ),
    includeSakspart = false
) {
    paragraph {
        text(
            Bokmal to "Barnepensjonen din blir beregnet ut fra grunnbeløpet i folketrygden og avdødes trygdetid.",
            Nynorsk to "",
            English to "",
        )
    }
    grunnbeloepetGammeltOgNyttRegelverk(grunnbeloep)
    trygdetid(aarTrygdetid, prorataBroek, beregningsMetodeFraGrunnlag, beregningsMetodeAnvendt)
    beregnetBarnepensjonGammeltOgNyttRegelverk(aarTrygdetid, prorataBroek, beregningsMetodeAnvendt, beregningsperioder)
    perioderMedRegistrertTrygdetid(trygdetidsperioder, beregningsMetodeAnvendt)
}

@TemplateModelHelpers
val beregningAvBarnepensjonNyttRegelverk = createAttachment(
    title = newText(
        Bokmal to "Beregning av barnepensjon",
        Nynorsk to "Utrekning av barnepensjon",
        English to "Calculation of Children’s Pension",
    ),
    includeSakspart = false
) {
    paragraph {
        text(
            Bokmal to "Barnepensjonen din blir beregnet ut fra grunnbeløpet i folketrygden og avdødes trygdetid.",
            Nynorsk to "",
            English to "",
        )
    }
    grunnbeloepetNyttRegelverk(grunnbeloep)
    trygdetid(aarTrygdetid, prorataBroek, beregningsMetodeFraGrunnlag, beregningsMetodeAnvendt)
    beregnetBarnepensjonNyttRegelverk(aarTrygdetid, prorataBroek, beregningsMetodeAnvendt, beregningsperioder)
    perioderMedRegistrertTrygdetid(trygdetidsperioder, beregningsMetodeAnvendt)
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.grunnbeloepetGammeltOgNyttRegelverk(
    grunnbeloep: Expression<Kroner>,
) {

    title2 {
        text(
            Bokmal to "Grunnbeløpet",
            Nynorsk to "",
            English to "",
        )
    }

    showIf(antallBarn.greaterThan(1)) {
        paragraph {
            text(
                Bokmal to "Før 1. januar 2024",
                Nynorsk to "",
                English to "",
                FontType.BOLD
            )
            newline()
            textExpr(
                Bokmal to "".expr() + "NAV gjør en samlet beregning av pensjon for barn som oppdras sammen. For denne beregningen har vi lagt til grunn at dere er " + antallBarn.format() + " barn som oppdras sammen.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
        paragraph {
            text(
                Bokmal to "Barnepensjon per år utgjør 40 prosent av folketrygdens grunnbeløp (G) for det første barnet i søskenflokken. For hvert av de andre barna legges det til 25 prosent av G. Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            text(
                Bokmal to "Fra 1. januar 2024 (nye regler)",
                Nynorsk to "",
                English to "",
                FontType.BOLD
            )
            newline()
            text(
                Bokmal to "Barnepensjonen per år utgjør en ganger folketrygdens grunnbeløp (G).",
                Nynorsk to "",
                English to "",
            )
        }
    }.orShow {
        paragraph {
            text(
                Bokmal to "Før 1. januar 2024",
                Nynorsk to "",
                English to "",
                FontType.BOLD
            )
            newline()
            text(
                Bokmal to "Barnepensjonen per år utgjør 40 prosent av folketrygdens grunnbeløp (G).",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            text(
                Bokmal to "Fra 1. januar 2024 (nye regler)",
                Nynorsk to "",
                English to "",
                FontType.BOLD
            )
            newline()
            text(
                Bokmal to "Barnepensjonen per år utgjør en ganger folketrygdens grunnbeløp (G).",
                Nynorsk to "",
                English to "",
            )
        }
    }

    paragraph {
        textExpr(
            Bokmal to "".expr() + "Folketrygdens grunnbeløp er per i dag " + grunnbeloep.format() + " kroner. Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år.",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.grunnbeloepetNyttRegelverk(
    grunnbeloep: Expression<Kroner>,
) {

    title2 {
        text(
            Bokmal to "Grunnbeløpet",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        textExpr(
            Bokmal to "".expr() + "Barnepensjonen utgjør en ganger folketrygdens grunnbeløp (G). Folketrygdens grunnbeløp er per i dag " + grunnbeloep.format() + " kroner. Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år. ",
            Nynorsk to "".expr(),
            English to "".expr(),
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.trygdetid(
    aarTrygdetid: Expression<Int>,
    prorataBroek: Expression<IntBroek?>,
    beregningsMetodeFraGrunnlag: Expression<BeregningsMetode>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>,
) {
    title2 {
        text(
            Bokmal to "Trygdetid",
            Nynorsk to "Trygdetid",
            English to "Period of national insurance coverage",
        )
    }

    showIf(beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            text(
                Bokmal to "Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "".expr() + "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede trygdetid er beregnet til " + aarTrygdetid.format() + " år.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }
    showIf(beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Pensjonen din er beregnet etter bestemmelsene i EØS-avtalen fordi vilkårene for rett til pensjon er oppfylt ved sammenlegging av avdødes opptjeningstid i Norge og andre EØS- eller avtaleland. Trygdetiden er beregnet etter avdødes samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller avtaleland. Avdødes samlede trygdetid er beregnet til " + aarTrygdetid.format() + " år, og forholdstallet til " + prorataBroek.formatBroek() + ".",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }
    showIf(beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.BEST)) {
        paragraph {
            text(
                Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Trygdetid over 40 år blir ikke tatt med i beregningen. Når grunnlag for pensjon er oppfylt etter nasjonale regler, og avdøde også har opptjening av medlemsperioder i land som Norge har trygdeavtale med, skal trygdetid gis etter den beste beregningen av kun nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            text(
                Bokmal to "Ved nasjonal beregning av trygdetid tilsvarer denne det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år.",
                Nynorsk to "",
                English to "",
            )
        }
        paragraph {
            text(
                Bokmal to "Ved sammenlegging av avdødes opptjeningstid i Norge og andre EØS/avtale-land er trygdetiden beregnet etter avdødes samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS-land.",
                Nynorsk to "",
                English to "",
            )
        }

        showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Avdødes samlede trygdetid fra avtaleland er beregnet til " + aarTrygdetid.format() + " år, og forholdstallet til " + prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                textExpr(
                    Bokmal to "".expr() + "Avdødes samlede trygdetid er beregnet til " + aarTrygdetid.format() + " år ved nasjonal opptjening. Dette gir den beste beregningen av trygdetid.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }

    konverterElementerTilBrevbakerformat(innhold)
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.beregnetBarnepensjonNyttRegelverk(
    aarTrygdetid: Expression<Int>,
    prorataBroek: Expression<IntBroek?>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>,
    beregningsperioder: Expression<List<Beregningsperiode>>
) {
    title2 {
        text(
            Bokmal to "Beregnet barnepensjon",
            Nynorsk to "",
            English to "",
        )
    }
    showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 1 G ganget med " + aarTrygdetid.format() + "/40 år trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        paragraph {
            textExpr(
                Bokmal to "".expr() + "Barnepensjonen er beregnet til 1 G ganget med " + aarTrygdetid.format() + "/40 år trygdetid. Beløpet fordeles på 12 utbetalinger i året.",
                Nynorsk to "".expr(),
                English to "".expr(),
            )
        }
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.beregnetBarnepensjonGammeltOgNyttRegelverk(
    aarTrygdetid: Expression<Int>,
    prorataBroek: Expression<IntBroek?>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>,
    beregningsperioder: Expression<List<Beregningsperiode>>
) {
    title2 {
        text(
            Bokmal to "Beregnet barnepensjon",
            Nynorsk to "",
            English to "",
        )
    }
    showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        showIf(antallBarn.greaterThan(1)) {
            paragraph {
                text(
                    Bokmal to "Før 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet ut fra at det er " + antallBarn.format() + " søsken som oppdras sammen, med 0,4 G til første barn, og 0,25 G til påfølgende barn. Beløpet fordeles likt på hvert barn, og blir ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 1 G ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }.orShow {
            paragraph {
                text(
                    Bokmal to "Før 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 0,4 G ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 1 G ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        showIf(antallBarn.greaterThan(1)) {
            paragraph {
                text(
                    Bokmal to "Før 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet ut fra at det er " + antallBarn.format() + " søsken som oppdras sammen, med 0,4 G til første barn, og 0,25 G til påfølgende barn. Beløpet fordeles likt på hvert barn, og blir ganget med " + aarTrygdetid.format() + "/40 trygdetid.  Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 1 G ganget med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }.orShow {
            paragraph {
                text(
                    Bokmal to "Før 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 0,4 G ganget med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                    FontType.BOLD
                )
                newline()
                textExpr(
                    Bokmal to "".expr() + "Barnepensjonen per år er beregnet til 1 G ganget med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet fordeles på 12 utbetalinger i året.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BeregningsinfoBP>.perioderMedRegistrertTrygdetid(
    trygdetidsperioder: Expression<List<Trygdetidsperiode>>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>
) {
    title2 {
        text(
            Bokmal to "Perioder med registrert trygdetid",
            Nynorsk to "",
            English to "",
        )
    }

    showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)){
        paragraph {
            text(
                Bokmal to "Tabellen viser perioder avdøde har vært medlem av folketrygden, og registrert fremtidig trygdetid.",
                Nynorsk to "",
                English to "",
            )
        }
        includePhrase(Trygdetidstabell(trygdetidsperioder))
    }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                Bokmal to "Tabellen viser perioder avdøde har vært medlem av folketrygden og medlemsperioder avdøde har hatt i land som Norge har trygdeavtale med, som er tatt med i beregningen.",
                Nynorsk to "",
                English to "",
            )
        }
        includePhrase(Trygdetidstabell(trygdetidsperioder))
    }
}