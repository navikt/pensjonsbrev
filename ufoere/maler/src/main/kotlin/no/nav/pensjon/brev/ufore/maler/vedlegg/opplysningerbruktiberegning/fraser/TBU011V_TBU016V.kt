package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.GRUNNBELOEP_URL
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU011V_TBU016V. "Slik beregner vi uføretrygden din" for ny uføretrygd.
 * Inkluderes naar bruker ikke er konvertert (orShow av [Visningsflagg.visBrukerKonvertertUP]).
 * Datostyrte/brevkodestyrte betingelser er erstattet med flagg.
 */
data class TBU011V_TBU016V(
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Slik beregner vi uføretrygden din" },
                nynorsk { + "Slik bereknar vi uføretrygda di" },
            )
        }

        showIf(flagg.visBeregningStandardFemAar) {
            paragraph {
                text(
                    bokmal { + "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                    nynorsk { + "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir teken med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                )
            }
        }

        showIf(flagg.visBeregningLigningIkkeFerdig) {
            paragraph {
                text(
                    bokmal { + "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Fordi ligningen din for året før du ble ufør ikke er ferdig, bruker vi her gjennomsnittsinntekten i de tre beste av de fire siste årene før du ble ufør." },
                    nynorsk { + "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Fordi likninga di for året før du blei ufør, ikkje er ferdig, bruker vi her gjennomsnittsinntekta i dei tre beste av dei fire siste åra før du blei ufør." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                    nynorsk { + "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når ligningen er ferdig, vil uføretrygden din bli beregnet på nytt. Du vil få et nytt vedtaksbrev om dette." },
                    nynorsk { + "Når likninga er ferdig, blir uføretrygda di berekna på nytt. Du får eit nytt vedtaksbrev om dette." },
                )
            }
        }

        showIf(flagg.visBeregningFoerstegangstjeneste) {
            paragraph {
                text(
                    bokmal { + "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har vært i militæret, eller hatt sivil førstegangstjeneste. Inntekt i denne perioden skal utgjøre minst tre ganger gjennomsnittlig G (folketrygdens grunnbeløp). " },
                    nynorsk { + "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har vore i militæret, eller hatt sivil førstegongsteneste. Inntekt i denne perioden skal utgjere minst tre gonger gjennomsnittleg G (grunnbeløpet i folketrygda). " },
                )
                showIf(flagg.harOpptjeningUTMedOpptjeningBruktAaretFoerOgFoerstegangstjeneste) {
                    text(
                        bokmal { + "Du hadde en høyere inntekt i året før du avtjente førstegangstjeneste, og vi bruker derfor denne inntekten i beregningen." },
                        nynorsk { + "Du hadde ei høgare inntekt i året før du avtente førstegongstenesta, og vi bruker derfor denne inntekta i berekninga." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                    nynorsk { + "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                )
            }
        }

        showIf(flagg.visBeregningOmsorgsaar) {
            paragraph {
                text(
                    bokmal { + "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har hatt pensjonsopptjening på grunnlag av omsorgsarbeid i ett eller flere av disse årene. Vi bruker disse årene i beregningen, hvis dette er en fordel for deg." },
                    nynorsk { + "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har hatt pensjonsopptening på grunnlag av omsorgsarbeid i eitt eller fleire av desse åra. Vi bruker desse åra i berekninga dersom dette er ein fordel for deg." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                    nynorsk { + "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                )
            }
        }

        showIf(flagg.visBeregningEOS) {
            paragraph {
                text(
                    bokmal { + "Uføretrygden din blir beregnet ut fra pensjonsgivende inntekt (inntekt som gir opptjening i folketrygden). " },
                    nynorsk { + "Uføretrygda di blir berekna ut frå pensjonsgivande inntekt (inntekt som gir opptjening i folketrygda). " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når du har hatt arbeidsinntekt i ett eller flere EØS-land i løpet av de fem siste årene før uføretidspunktet, fastsetter vi beregningsgrunnlaget ut fra hvor mange av disse fem årene du har hatt arbeidsinntekt i et EØS-land. " },
                    nynorsk { + "Når du har hatt arbeidsinntekt i eitt eller fleire EØS-land i løpet av dei fem siste åra før uføretidspunktet, fastset vi berekningsgrunnlaget ut frå kor mange av desse fem åra du har hatt arbeidsinntekt i eit EØS-land. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom du mangler pensjonsgivende inntekt i Norge i ett eller flere av årene som skal brukes i beregningen, kan inntekten for disse årene settes til et gjennomsnitt av inntekten i årene der du har pensjonsgivende inntekt. Du kan se hvilke år vi har brukt i tabellen «Inntekt lagt til grunn for beregning av uføretrygden din». " },
                    nynorsk { + "Dersom du manglar pensjonsgivande inntekt i Noreg i eitt eller fleire av åra som skal brukast i berekninga, kan inntekta for desse åra setjast til eit gjennomsnitt av inntekta i åra der du har pensjonsgivande inntekt. Du kan sjå kva år vi har brukt i tabellen «Inntekt lagd til grunn for berekning av uføretrygda di». " },
                )
            }
        }
    }
}
