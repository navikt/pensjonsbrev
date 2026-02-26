package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InfobrevLovendringer2026 : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Ufoerebrevkoder.AutoBrev.UT_INFO_LOVENDRINGER_2026

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Infobrev for lovendringer uføretrygd 2026",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Lovendringer uføretrygd 2026" })
        }

        outline {
            paragraph { text(bokmal { +"Du får dette brevet fordi det kommer flere lovendringer i regelverket for uføretrygd i 2026. Hvis endringene påvirker din uføretrygd, vil du få brev fra oss med mer informasjon." }) }
            paragraph { text(bokmal { +"Du trenger ikke å gjøre noe, lovendringene skjer automatisk." }) }
            paragraph {
                text(bokmal { +"I dette brevet finner du informasjon om følgende lovendringer:" })
                list {
                    item { text(bokmal { +"Økt bunnfradrag (inntektsgrense) etter 2 år med uføretrygd" }) }
                    item { text(bokmal { +"Nye regler for reduksjonsprosent (kompensasjonsgrad)" }) }
                    item { text(bokmal { +"Lik minste inntekt før uførhet (IFU) for alle" }) }
                    item { text(bokmal { +"Ny minstesats for de som gikk fra uførepensjon til uføretrygd 1. januar 2015" }) }
                }
            }

            title1 { text(bokmal { +"Økt bunnfradrag (inntektsgrense) etter 2 år med uføretrygd" }) }
            paragraph {
                text(bokmal { +"Grensen for hvor mye inntekt du kan ha før du får en lavere utbetaling av uføretrygd, vil fra 1. juli 2026 hete " })
                text(bokmal { +"bunnfradrag" }, fontType = BOLD)
                text(bokmal { +". I folketrygdloven omtales dette før 1. juli 2026 som inntektsgrense." })
            }
            paragraph { text(bokmal { +"Frem til nå har alle med 100 prosent uføretrygd hatt et bunnfradrag på 0,4 G (52 064 kroner). De som har gradert uføretrygd, har et individuelt bunnfradrag. 1. oktober øker bunnfradraget til 1 G (130 160 kroner) for deg som har hatt uføretrygd i 2 år eller mer. De som har hatt gradert uføretrygd i 2 år eller mer, vil også få et høyere bunnfradrag, som fortsatt vil være individuelt." }) }
            paragraph { text(bokmal { +"Denne loven er fortsatt under arbeid, og det kan komme endringer." }) }
            paragraph { text(bokmal { +"Grunnbeløpet justeres 1. mai hvert år, og 0,4 G og 1 G vil derfor endre seg fra år til år. Du finner bunnfradraget ditt på Din uføretrygd." }) }
            paragraph {
                text(bokmal { +"To viktige begrep" }, fontType = BOLD)
                list {
                    item {
                        text(bokmal { +"Bunnfradrag" }, fontType = BOLD)
                        text(bokmal { +" - den årlige inntekten du kan ha, før vi reduserer uføretrygden din. " })
                    }
                    item {
                        text(bokmal { +"Inntektstak " }, fontType = BOLD)
                        text(bokmal { +"- den årlige inntekten du kan ha, før du ikke lenger får utbetalt uføretrygd det aktuelle året. Inntektstaket er 80 prosent av inntekten du hadde før uførhet, oppjustert til dagens verdi." })
                    }
                }
            }

            title2 { text(bokmal { +"Du trenger ikke å gjøre noe" }) }
            paragraph {
                list {
                    item {
                        text(bokmal { +"Endringen skjer automatisk når du har hatt uføretrygd i 24 måneder." })
                    }
                    item {
                        text(bokmal { +"Brev, nav.no, inntektsplanlegger og Din uføretrygd vil vise nytt bunnfradrag fra 1. oktober 2026, eller når du treffes av endringen." })
                    }
                    item {
                        text(bokmal { +"Har du varig tilrettelagt arbeid, påvirkes du ikke av denne regelendringen." })
                    }
                }
            }

            title2 { text(bokmal { +"Hvis du har hatt uføretrygd i mindre enn 2 år" }) }
            paragraph { text(bokmal { +"Du som enda ikke har hatt uføretrygd i 2 år, vil få et brev med hvordan vi vil beregne bunnfradrag ditt, året bunnfradraget øker." }) }
            paragraph { text(bokmal { +"Eksempel: " }, fontType = ITALIC) }
            paragraph { text(bokmal { +"Kim fikk innvilget uføretrygd med virkning fra april 2025. Før april 2027, vil Kim få et brev fra oss med informasjon om bunnfradraget i 2027. " }, fontType = ITALIC) }

            title2 { text(bokmal { +"Hvis du har hatt uføretrygd i 2 år eller mer" }) }
            paragraph { text(bokmal { +"Endringen i bunnfradraget påvirker deg som har inntekt ved siden av uføretrygden, og har hatt uføretrygd i 2 år eller mer.  " }) }
            paragraph { text(bokmal { +"Har du barnetillegg, kan endringen påvirke barnetillegget ditt også. " }) }
            paragraph { text(bokmal { +"Alle som fikk innvilget uføretrygd før 1. januar 2024, får et bunnfradrag på 1G (130 160 kroner) i hele 2026.  " }) }
            paragraph { text(bokmal { +"Loven trer i kraft 1. oktober 2026, men har virkning fra 1. januar 2026. Det betyr at uføretrygden blir redusert etter bunnfradrag på 0,4 G fram til 1. oktober 2026.  " }) }
            paragraph { text(bokmal { +"Eksempel: " }, fontType = ITALIC) }
            paragraph { text(bokmal { +"Kim fikk innvilget uføretrygd i 2023. Kim har 100 prosent uføretrygd. Fram til 1. oktober i år, vil vi redusere uføretrygden til Kim når Kim har inntekt over 0,4 G. I etteroppgjøret for 2026, vil Kim få tilbake det vi eventuelt har trukket for mye." }, fontType = ITALIC) }

            title1 { text(bokmal { +"Nye regler for reduksjonsprosent (kompensasjonsgrad) " }) }
            paragraph {
                text(bokmal { +"Prosenten vi bruker når vi reduserer uføretrygd hvis du har inntekt over bunnfradraget, vil fra 1. juli 2026 hete " })
                text(bokmal { +"reduksjonsprosent" }, fontType = BOLD)
                text(bokmal { +". I folketrygdloven omtales dette før 1. juli 2026 som kompensasjonsgrad." })
            }
            paragraph { text(bokmal { +"Fra 1. juli 2026 kan du ikke lenger ha høyere reduksjonsprosent enn 70 prosent. Det betyr at vi ikke kan redusere utbetalingen av uføretrygden din med mer enn 70 prosent av hver krone du tjener over bunnfradraget ditt." }) }

            title2 { text(bokmal { +"Hva er reduksjonsprosent? " }) }
            paragraph { text(bokmal { +"Uføretrygden din blir redusert med en prosentandel når du har inntekt over bunnfradraget ditt. Denne prosentandelen kalles reduksjonsprosent." }) }
            paragraph {
                text(bokmal { +"Eksempel på reduksjonsprosent" }, fontType = BOLD)
                list {
                    item {
                        text(bokmal { +"Hvis reduksjonsprosenten din er 70 prosent, reduseres uføretrygden med 70 kroner for hver 100 kroner du tjener over bunnfradraget. " })
                    }
                    item {
                        text(bokmal { +"Hvis du tjener 10 000 kroner over bunnfradraget, trekkes 7 000 kroner fra uføretrygden. Du beholder altså inntekten din, i tillegg til 3 000 kroner i uføretrygd. " })
                    }
                }
            }

            title2 { text(bokmal { +"Slik påvirkes du av endringen" }) }
            paragraph { text(bokmal { +"Dette påvirker bare deg som har en høyere reduksjonsprosent enn 70 prosent i dag, og i tillegg har inntekt ved siden av uføretrygden. Reduksjonsprosenten er individuell, og kan være lavere enn 70 prosent. Fra 1. juli 2026 kan den ikke lenger være høyere enn 70 prosent. " }) }
            paragraph { text(bokmal { +"Hvis du får en lavere reduksjonsprosent, vil vi trekke mindre av uføretrygden din når du har inntekt over bunnfradraget. Det kan føre til at det blir mer lønnsomt å jobbe ved siden av uføretrygden.  " }) }
            paragraph { text(bokmal { +"Har du barnetillegg, kan denne endringen påvirke utbetalingen av barnetillegget ditt. Uføretrygd regnes med som inntekt når vi beregner størrelsen på barnetillegget ditt. Derfor kan en høyere utbetaling av uføretrygd, føre til en reduksjon i barnetillegget. " }) }
            paragraph { text(bokmal { +"Eksempel: " }, fontType = ITALIC) }
            paragraph { text(bokmal { +"Kim har uføretrygd og en reduksjonsprosent på 80 prosent. Fram til 1. juli i år, vil vi redusere uføretrygden til Kim med 80 prosent av hver krone Kim tjener over bunnfradraget sitt. Fra 1. juli reduserer vi uføretrygden til Kim med 70 prosent av hver krone Kim tjener over bunnfradraget. I etteroppgjøret for 2026, vil Kim få tilbakebetalt hvis vi har trukket for mye." }, fontType = ITALIC) }

            title2 { text(bokmal { +"Du trenger ikke å gjøre noe " }) }
            paragraph { text(bokmal { +"Endringen skjer automatisk. Du får varsel- og vedtaksbrev fra oss hvis du får en lavere reduksjonsprosent.  " }) }
            paragraph { text(bokmal { +"Du finner din personlige reduksjonsprosent i vedtaket ditt eller ved å logge inn på Din uføretrygd." }) }

            title1 { text(bokmal { +"Lik minste inntekt før uførhet (IFU) for alle" }) }
            paragraph { text(bokmal { +"Fra 1. juli 2026 skal enslige, samboere og ektefeller ha lik IFU. Din sivilstand vil ikke lenger ha betydning. Etter lovendringen skal IFU aldri fastsettes lavere enn 3,5 ganger grunnbeløpet. " }) }

            title2 { text(bokmal { +"Hva er inntekt før uførhet IFU?" }) }
            paragraph { text(bokmal { +"Inntekt før uførhet (IFU) fastsettes til den inntekten man normalt ville hatt i full stilling før uføretidspunktet." }) }
            paragraph {
                text(bokmal { +"IFU kan ha betydning for:" })
                list {
                    item { text(bokmal { +"uføregrad" }) }
                    item { text(bokmal { +"reduksjonsprosent" }) }
                    item { text(bokmal { +"inntektstak " }) }
                }
            }

            title2 { text(bokmal { +"Slik påvirkes du av endringen" }) }
            paragraph { text(bokmal { +"Hvis du har samboer eller ektefelle og har minstesats i dag, skal vi endre din inntekt før uførhet fra 3,3 ganger grunnbeløpet til 3,5 ganger grunnbeløpet. " }) }
            paragraph { text(bokmal { +"De som får beregnet inntekt før uførhet etter regler for enslige eller unge uføre, påvirkes ikke av denne lovendringen." }) }
            paragraph { text(bokmal { +"I noen tilfeller vil økt minste IFU føre til at uføregraden øker. Du vil få varsel- og vedtaksbrev hvis dette gjelder deg. Der vil du få informasjon om hvordan det påvirker uføretrygden din. " }) }

            title2 { text(bokmal { +"Du trenger ikke å gjøre noe " }) }
            paragraph { text(bokmal { +"Endringen skjer automatisk. Du får et vedtaksbrev fra oss hvis denne endringen påvirker uføretrygden din. " }) }
            paragraph { text(bokmal { +"Du finner din nåværende IFU i det siste vedtaksbrevet ditt. " }) }

            title1 { text(bokmal { +"Ny minstesats for de som gikk fra uførepensjon til uføretrygd 1. januar 2015 " }) }
            paragraph { text(bokmal { +"Minstesatsen for uføretrygd for gifte/samboende som gikk fra uførepensjon til uføretrygd i 2015 endres fra 1. juli 2026. Den endres fra 2,379 G (309 651 kroner) til 2,329 G (303 143).  Det fører til at minstesatsen for uføretrygd nå blir lik for alle." }) }
            paragraph { text(bokmal { +"Grunnbeløpet justeres 1. mai hvert år, og minstesatsen vil derfor endre seg fra år til år." }) }

            title2 { text(bokmal { +"Slik påvirkes du av endringen" }) }
            paragraph { text(bokmal { +"Endringen påvirker bare deg som gikk fra uførepensjon til uføretrygd 1. januar 2015, er gift eller samboende og får utbetalt minstesatsen for uføretrygd. Du som påvirkes av denne lovendringen, vil få en lavere utbetaling av uføretrygd." }) }
            paragraph { text(bokmal { +"Har du barnetillegg, kan denne endringen påvirke utbetalingen av barnetillegget ditt. Uføretrygd regnes med som inntekt når vi beregner størrelsen på barnetillegget ditt. Derfor kan en lavere utbetaling av uføretrygd, føre til en høyere utbetaling av barnetillegget. " }) }

            title2 { text(bokmal { +"Du trenger ikke å gjøre noe " }) }
            paragraph { text(bokmal { +"Endringen skjer automatisk. Du får et vedtaksbrev fra oss hvis du får en lavere utbetaling av uføretrygd. Har du spørsmål om hvordan endringen påvirker deg, ta kontakt med oss." }) }

            title1 { text( bokmal { +"Dette sier loven " }) }

            title2 { text(bokmal { +"Økt bunnfradrag (inntektsgrense) etter 2 år med uføretrygd  " }) }
            paragraph { text(bokmal { +"§ 12-14 første ledd første punktum skal lyde:" }) }
            paragraph { text(bokmal { +"\"Når uføregraden fastsettes etter § 12-10, skal det fastsettes et bunnfradrag, som skal svare til inntekt etter uførhet (se § 12-9 tredje ledd) tillagt 40 prosent av grunnbeløpet per kalenderår.\"" }) }

            title2 { text(bokmal { +" Nye regler for reduksjonsprosent (kompensasjonsgrad) § 12-14 første ledd andre punktum blir opphevet." }) }
            paragraph { text(bokmal { +"§ 12-14 andre ledd skal lyde:" }) }
            paragraph { text(bokmal { +"\"Uføretrygd skal reduseres med en andel av inntekt som overstiger bunnfradraget. Andelen (reduksjonsprosenten) skal svare til mottakerens uføretrygd ved 100 prosent uføregrad delt på mottakerens inntekt før uførhet (se § 12-9 første og andre ledd). " +
                    "Reduksjonsprosenten skal likevel ikke overstige 70 prosent. Som inntekt regnes pensjonsgivende inntekt og inntekt av samme artinntekt før uførhet skal ikke settes lavere enn fra utlandet.\"" }) }

            title2 { text(bokmal { +"Lik minste inntekt før uførhet (IFU) for alle " }) }
            paragraph { text(bokmal { +"§ 12-9 andre ledd skal lyde: " }) }
            paragraph { text(bokmal { +"\"Inntekt før uførhet skal ikke settes lavere enn 3,5 ganger grunnbeløpet. For medlemmer som fyller vilkårene for rett til minsteytelse som ung ufør, skal inntekt før uførhet ikke settes lavere enn 4,5 ganger grunnbeløpet.\"" }) }

            title2 { text(bokmal { +"Ny minstesats de som gikk fra uførepensjon til uføretrygd 1. januar 2015" }) }
            paragraph { text(bokmal { +"§ 12-13 andre ledd andre punktum blir oppheva." }) }
            paragraph { text(bokmal { +"Noverande tredje punktum blir andre punktum." }) }
            paragraph { text(bokmal { +"§ 12-2 tredje ledd bokstav a skal lyde: " }) }
            paragraph { text(bokmal { +"\"a. beregnet av grunnlaget etter § 12-11 første ledd minst vil svare til halvparten av høy sats etter § 12-13 andre ledd andre punktum, eller\" " }) }
        }

    }
}
