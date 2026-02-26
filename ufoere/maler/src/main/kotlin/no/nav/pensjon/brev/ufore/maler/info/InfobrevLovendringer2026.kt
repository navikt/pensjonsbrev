package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfobrevLovendringer2026Dto
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InfobrevLovendringer2026 : AutobrevTemplate<InfobrevLovendringer2026Dto> {
    override val kode = Ufoerebrevkoder.AutoBrev.UT_INFO_LOVENDRINGER_2026

    override val template = createTemplate(
        letterDataType = InfobrevLovendringer2026Dto::class, languages = languages(Language.Bokmal), letterMetadata = LetterMetadata(
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
            paragraph { text(bokmal { +"Grensen for hvor mye inntekt du kan ha før du får en lavere utbetaling av uføretrygd, vil fra 1. juli 2026 hete bunnfradrag. I folketrygdloven omtales dette før 1. juli 2026 som inntektsgrense." }) }
            paragraph { text(bokmal { +"Frem til nå har alle med 100 prosent uføretrygd hatt et bunnfradrag på 0,4 G (52 064 kroner). De som har gradert uføretrygd, har et individuelt bunnfradrag. 1. oktober øker bunnfradraget til 1 G (130 160 kroner) for deg som har hatt uføretrygd i 2 år eller mer. De som har hatt gradert uføretrygd i 2 år eller mer, vil også få et høyere bunnfradrag, som fortsatt vil være individuelt." }) }
            paragraph { text(bokmal { +"Denne loven er fortsatt under arbeid, og det kan komme endringer." }) }
            paragraph { text(bokmal { +"Grunnbeløpet justeres 1. mai hvert år, og 0,4 G og 1 G vil derfor endre seg fra år til år. Du finner bunnfradraget ditt på Din uføretrygd." }) }
            paragraph {
                text(bokmal { +"To viktige begrep" }, fontType = BOLD)
                list {
                    item {
                        text(bokmal { +"Bunnfradrag" }, fontType = BOLD)
                        text(bokmal { +" - den årlige inntekten du kan ha, før vi reduserer uføretrygden din. " })
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
                        text(bokmal { +"Brev, nav.no, inntektsplanlegger og Din uføretrygd vil vise nytt bunnfradrag fra 1. oktober 2026, eller når du treffes av endringen." })
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

        }

    }
}
