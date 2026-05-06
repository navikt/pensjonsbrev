package no.nav.pensjon.brev.ufore.maler.lovendringer2026

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object UforetrygdLovendringer2026Fraser {

    object Introduksjon : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Dette er et varsel om at vi kommer til å endre uføretrygden din. Grunnen til det er lovendringer Stortinget har vedtatt som trer i kraft den 1. juli 2026. Lovendringene gjelder fra 1. januar 2026."
                    },

                    nynorsk {
                        +"Dette er eit varsel om at vi kjem til å endre uføretrygda di. Grunnen til det er lovendringar Stortinget har vedteke som trer i kraft den 1. juli 2026. Lovendringane gjeld frå 1. januar 2026."
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du vil få et vedtaksbrev fra oss med beregninger om hvordan dette vil påvirke utbetalingen av uføretrygden din, og eventuelle tillegg. Vi sender ut vedtaksbrevet så fort vi kan, og du får vedtaket ditt så snart vi vet hvordan dette påvirker uføretrygden din."
                    },

                    nynorsk {
                        +"Du vil få eit vedtaksbrev frå oss med berekningar om korleis dette vil påverke utbetalinga av uføretrygda di, og eventuelle tillegg. Vi sender ut vedtaksbrevet så fort vi kan, og du får vedtaket ditt så snart vi veit korleis dette påverkar uføretrygda di."
                    }
                )
            }
        }
    }

    object LavereReduksjonsprosent : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Varsel om lavere reduksjonsprosent (kompensasjonsgrad)" },
                    nynorsk { +"Varsel om lågare reduksjonsprosent (kompensasjonsgrad)" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt at" },
                    nynorsk { +"Stortinget har vedteke at" }
                )
                list {
                    item {
                        text(
                            bokmal { +"Kompensasjonsgrad endrer navn til reduksjonsprosent." },
                            nynorsk { +"Kompensasjonsgrad endrar namn til reduksjonsprosent." })
                    }
                    item {
                        text(
                            bokmal { +"Vi skal ikke lenger redusere uføretrygden din med mer enn 70 prosent når du har inntekt over inntektsgrensen din." },
                            nynorsk { +"Vi skal ikkje lenger redusere uføretrygda di med meir enn 70 prosent når du har inntekt over inntektsgrensa di." })
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"Slik påvirkes du av endringen" },
                    nynorsk { +"Slik påverkas du av endringa" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får lavere reduksjonsprosent. Din nye reduksjonsprosent blir 70 prosent." },
                    nynorsk { +"Du får lågare reduksjonsprosent. Din nye reduksjonsprosent blir 70 prosent." },
                )
                list {
                    item {
                        text(
                            bokmal { +"Reduksjonsprosent har bare betydning for deg som har inntekt ved siden av uføretrygden." },
                            nynorsk { +"Reduksjonsprosent har berre tyding for deg som har inntekt ved sida av uføretrygda." })
                    }
                    item {
                        text(
                            bokmal { +"Endringen kan føre til at det blir mer lønnsomt å jobbe ved siden av uføretrygden." },
                            nynorsk { +"Endringa kan føre til at det blir meir lønsamt å jobbe ved sida av uføretrygda." })
                    }
                    item {
                        text(
                            bokmal { +"Har du barnetillegg, kan denne endringen påvirke utbetalingen av barnetillegget ditt hvis du har inntekt ved siden av uføretrygden." },
                            nynorsk { +"Har du barnetillegg, kan denne endringa påverke utbetalinga av barnetillegget ditt viss du har inntekt ved sida av uføretrygda." })
                    }
                    item {
                        text(
                            bokmal { +"På nav.no/uforetrygd#jobb kan du lese mer om hvordan inntekt påvirker uføretrygden din." },
                            nynorsk { +"På nav.no/uforetrygd#jobb kan du lese meir om korleis inntekt påverkar uføretrygda di." })
                    }
                }
            }
        }
    }

    object HoyereMinstesatsIFU : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Varsel om høyere minstesats for inntekt før uførhet (IFU)" },
                    nynorsk { +"Varsel om høgare minstesats for inntekt før uførhet (IFU)" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt at" },
                    nynorsk { +"Stortinget har vedteke at" }
                )
                list {
                    item {
                        text(
                            bokmal { +"Fra 1. januar 2026 endres din inntekt før uførhet fra 3,3 ganger grunnbeløpet (G) til 3,5." },
                            nynorsk { +"Frå 1. januar 2026 vert inntekta di før uførhet endra frå 3,3 gonger grunnbeløpet (G) til 3,5." })
                    }
                    item {
                        text(
                            bokmal { +"Minstesats for inntekt før uførhet økes, og kan ikke lenger være lavere enn 3,5 G." },
                            nynorsk { +"Minstesats for inntekt før uførhet aukar, og kan ikkje lenger vere lågare enn 3,5 G." })
                    }
                    item {
                        text(
                            bokmal { +"Endringen fører til at sivilstand ikke lenger påvirker minstesats for IFU." },
                            nynorsk { +"Endringa fører til at sivilstand ikkje lenger påverkar minstesats for IFU." })
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"Derfor endres din IFU" },
                    nynorsk { +"Difor endras IFU-en din" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får en høyere IFU fordi du har eller har hatt ektefelle/samboer og har minstesats for IFU i 2026." },
                    nynorsk { +"Du får ein høgare IFU fordi du har eller har hatt ektefelle/sambuar og har minstesats for IFU i 2026." },
                )
            }
            title2 {
                text(
                    bokmal { +"Slik påvirker IFU uføretrygden din" },
                    nynorsk { +"Slik påverkar IFU uføretrygda di" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Minste IFU er en sats vi setter for å sikre et inntektsgrunnlag for deg som har hatt lite eller ingen inntekt før uførhet." },
                    nynorsk { +"Minste IFU er ein sats vi set for å sikre eit inntektsgrunnlag for deg som har hatt lite eller ingen inntekt før uførhet." },
                )
            }
            paragraph {
                text(
                    bokmal { +"IFU kan ha betydning for:" },
                    nynorsk { +"IFU kan ha tyding for:" },
                )
                list {
                    item {
                        text(
                            bokmal { +"uføregrad" },
                            nynorsk { +"uføregrad" })
                    }
                    item {
                        text(
                            bokmal { +"reduksjonsprosent" },
                            nynorsk { +"reduksjonsprosent" })
                    }
                    item {
                        text(
                            bokmal { +"Inntektstak" },
                            nynorsk { +"Inntektstak" })
                    }
                }
            }
        }
    }

    object DetteKanDuGjoreNa : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Dette kan du gjøre nå" },
                    nynorsk { +"Dette kan du gjere no" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Dette er kun et varsel. Mener du vi har feil opplysninger om saken din, kan du melde fra til oss før vi fatter et vedtak. Fristen for å komme med tilbakemeldinger er 2 uker fra du får dette brevet. Du kan skrive til oss på nav.no/kontakt eller ringe oss på telefon 55 55 33 33." },
                    nynorsk { +"Dette er kun eit varsel. Meiner du vi har feil opplysningar om saka di, kan du melde frå til oss før vi fattar eit vedtak. Fristen for å kome med tilbakemeldingar er 2 veker frå du får dette brevet. Du kan skrive til oss på nav.no/kontakt eller ringe oss på telefon 55 55 33 33." }
                )
            }
        }
    }
}