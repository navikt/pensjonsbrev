package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.Tillegg
import no.nav.pensjon.brev.maler.legacy.UTOgTilleggMapper
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object LavereMinstesats {

    data class Brevdata(
        val nettoUforetrygdUtenTillegg: Expression<Kroner>,
        val nettoBarnetillegg: Expression<Kroner?>,
        val nettoGjenlevendetillegg: Expression<Kroner?>,
        val endringNettoUforetrygdUtenTillegg: Expression<Boolean>,
        val endringNettoBarnetillegg: Expression<Boolean>,
        val endringNettoGjenlevendetillegg: Expression<Boolean>,
        val endringReduksjonsprosent: Expression<Boolean>,
        val reduksjonsprosent: Expression<Double>,
        val harMinstesats: Expression<Boolean>,
        val tidligereMinstesats: Expression<Kroner>,
        val nyMinstesats: Expression<Kroner>,
        val tillegg: Expression<Collection<Tillegg>>,
        val egenopptjentUforetrygd: Expression<Kroner>,
        val avkortetPgaRedusertTrygdetid: Expression<Boolean>,
        val harGradertUfoeretrygd: Expression<Boolean>,
        val hjemmeltekst: Expression<String>,
    )

    data class Outline(val data: Brevdata) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            val sumUtOgTillegg = data.nettoUforetrygdUtenTillegg + data.nettoBarnetillegg.ifNull(Kroner(0)) + data.nettoGjenlevendetillegg.ifNull(Kroner(0))

            title1 {
                text(
                    bokmal { +"Dette er dine endringer fra 1. juli 2026" },
                    nynorsk { +"Dette er endringane dine frå 1. juli 2026" },
                )
            }
            paragraph {
                table(header = {
                    column { text(bokmal { +"Endring" }, nynorsk { +"Endring" }) }
                    column(alignment = RIGHT) {}
                }) {
                    showIf(data.endringNettoUforetrygdUtenTillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ny beregnet uføretrygd" },
                                    nynorsk { +"Ny berekna uføretrygd" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.nettoUforetrygdUtenTillegg.format() },
                                    nynorsk { +data.nettoUforetrygdUtenTillegg.format() },
                                )
                            }
                        }
                    }
                    showIf(data.endringNettoBarnetillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Nytt barnetillegg" },
                                    nynorsk { +"Nytt barnetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.nettoBarnetillegg.ifNull(Kroner(0)).format() },
                                    nynorsk { +data.nettoBarnetillegg.ifNull(Kroner(0)).format() },
                                )
                            }
                        }
                    }
                    showIf(data.endringNettoGjenlevendetillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Nytt gjenlevendetillegg" },
                                    nynorsk { +"Nytt gjenlevendetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.nettoGjenlevendetillegg.ifNull(Kroner(0)).format() },
                                    nynorsk { +data.nettoGjenlevendetillegg.ifNull(Kroner(0)).format() },
                                )
                            }
                        }
                    }
                    showIf(data.endringReduksjonsprosent) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ny reduksjonsprosent" },
                                    nynorsk { +"Ny reduksjonsprosent" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.reduksjonsprosent.format() + " prosent" },
                                    nynorsk { +data.reduksjonsprosent.format() + " prosent" },
                                )
                            }
                        }
                    }
                    showIf(data.harMinstesats) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ny minstesats" },
                                    nynorsk { +"Ny minstesats" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +"2,329 G" },
                                    nynorsk { +"2,329 G" },
                                )
                            }
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal {
                        +"Du får " + sumUtOgTillegg.format() + " i " + data.tillegg.format(UTOgTilleggMapper)
                        +" per måned før skatt fra 1. juli 2026."
                    },
                    nynorsk {
                        +"Du får " + sumUtOgTillegg.format() + " i " + data.tillegg.format(UTOgTilleggMapper)
                        +" per månad før skatt frå 1. juli 2026."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned." },
                    nynorsk { +"Uføretrygda blir utbetalt seinast den 20. kvar månad." },
                )
            }
            paragraph {
                text(
                    bokmal { +"I vedlegget " },
                    nynorsk { +"I vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(
                    bokmal { +" kan du se hvordan vi har beregnet uføretrygden din." },
                    nynorsk { +" kan du sjå korleis vi har berekna uføretrygda di." },
                )
            }

            title1 {
                text(
                    bokmal { +"Derfor endrer vi uføretrygden din" },
                    nynorsk { +"Difor endrar vi uføretrygda di" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får endring i uføretrygden din fordi Stortinget har vedtatt at minstesatsen endres 1. juli 2026.  " },
                    nynorsk { +"Du får endring i uføretrygda di fordi Stortinget har vedteke at minstesatsen vert endra 1. juli 2026.  " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fra 1. juli 2026 endres minstesatsen for gifte/samboende fra 2,379 G (" + data.tidligereMinstesats.format() + ") til 2,329 G (" + data.nyMinstesats.format() + ")." },
                    nynorsk { +"Frå 1. juli 2026 vert minstesatsen for gifte/sambuande endra frå 2,379 G (" + data.tidligereMinstesats.format() + ") til 2,329 G (" + data.nyMinstesats.format() + ")." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Lovendringen gjelder for deg som fikk uførepensjonen din omregnet til uføretrygd i 2015. Endringen fører til at alle gifte og samboende nå får lik minstesats." },
                    nynorsk { +"Lovendringa gjeld for deg som fekk uførepensjonen din omrekna til uføretrygd i 2015. Endringa fører til at alle gifte og sambuande no får lik minstesats." },
                )
            }
            paragraph {
                text(
                    bokmal { +"I 2015 ble uførepensjon endret til uføretrygd. For deg som allerede var uføretrygdet, førte det til at ytelsen din ble regnet om etter de nye reglene." },
                    nynorsk { +"I 2015 vart uførepensjon endra til uføretrygd. For deg som allereie var uføretrygda, førte det til at ytinga di vart rekna om etter dei nye reglane." },
                )
            }
            showIf(!data.harMinstesats) {
                paragraph {
                    text(
                        bokmal { +"Siden din egenopptjening er høyere enn minstesatsen (2,329 G), bruker vi din opptjening i beregningen. Dette gir deg en høyere uføretrygd. Din egenopptjening er kroner " + data.egenopptjentUforetrygd.format() + ". " },
                        nynorsk { +"Sidan eigenoppteninga di er høgare enn minstesatsen (2,329 G), brukar vi oppteninga di i berekninga. Dette gir deg ei høgare uføretrygd. Eigenoppteninga di er kroner " + data.egenopptjentUforetrygd.format() + ". " },
                    )
                }
            }
            showIf(data.avkortetPgaRedusertTrygdetid) {
                paragraph {
                    text(
                        bokmal { +"Du har avkortet uføretrygd på grunn av redusert trygdetid. Derfor er minstesatsen din lavere enn 2,329 G." },
                        nynorsk { +"Du har avkorta uføretrygd på grunn av redusert trygdetid. Difor er minstesatsen din lågare enn 2,329 G." },
                    )
                }
            }
            showIf(data.harGradertUfoeretrygd) {
                paragraph {
                    text(
                        bokmal { +"Denne endringen påvirker også deg som har gradert uføretrygd." },
                        nynorsk { +"Denne endringa påverkar òg deg som har gradert uføretrygd." },
                    )
                }
            }
            showIf(data.endringNettoBarnetillegg) {
                title1 {
                    text(
                        bokmal { +"Endring i barnetillegg" },
                        nynorsk { +"Endring i barnetillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Regelverksendringene fører til at du får en endret utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. " +
                                    "Derfor får du en endret utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er " + data.nettoBarnetillegg.ifNull(Kroner(0)).format() + "."
                        },
                        nynorsk {
                            +"Regelverksendringane fører til at du får ei endra utbetaling av uføretrygd. Uføretrygda vert rekna med som inntekt når vi reknar ut barnetillegg. " +
                                    "Difor får du ei endra utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er " + data.nettoBarnetillegg.ifNull(Kroner(0)).format() + "."
                        },
                    )
                }
            }
            showIf(data.endringNettoGjenlevendetillegg) {
                title1 {
                    text(
                        bokmal { +"Endring i gjenlevendetillegg" },
                        nynorsk { +"Endring i gjenlevendetillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Regelverksendringene fører til at gjenlevendetillegg i uføretrygden din endres. Ny beregning av gjenlevendetillegget (før skatt) er " +
                                    data.nettoGjenlevendetillegg.ifNull(Kroner(0)).format() + "."
                        },
                        nynorsk {
                            +"Regelverksendringane fører til at gjenlevendetillegg i uføretrygda di vert endra. Ny berekning av gjenlevendetillegget (før skatt) er " +
                                    data.nettoGjenlevendetillegg.ifNull(Kroner(0)).format() + "."
                        },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket har vi gjort etter " + data.hjemmeltekst + "." },
                    nynorsk { +"Vedtaket har vi gjort etter " + data.hjemmeltekst + "." },
                )
            }

            title1 {
                text(
                    bokmal { +"Dette kan du gjøre nå" },
                    nynorsk { +"Dette kan du gjere no" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du har rett til å klage på vedtaket, selv om endringen i uføretrygden din skyldes endringer i lovverket. " +
                                "Mener du vi har feil opplysninger om saken din, kan du også klage på vedtaket."
                    },
                    nynorsk {
                        +"Du har rett til å klage på vedtaket, sjølv om endringa i uføretrygda di kjem av endringar i lovverket. " +
                                "Meiner du at vi har feil opplysningar om saka di, kan du òg klage på vedtaket."
                    },
                )
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
