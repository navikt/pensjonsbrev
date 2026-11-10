package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object UfoeretrygdForeloepigEksportvurdering : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Ufoerebrevkoder.Redigerbar.UT_FORELOPIG_EKSPORTVURDERING
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val featureToggle = FeatureToggles.ufoeretrygdForeloepigEksportvurdering.toggle

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Uføretrygd - foreløpig eksportvurdering",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        val land = saksbehandlervalg("land", "Land").text()
        val hvisensligogminstesats = saksbehandlervalg("hvisensligogminstesats", "Hvis enslig og minstesats").bool()
        val barnetillegginnenavtaleland = saksbehandlervalg("barnetillegginnenavtaleland", "Barnetillegg innen avtaleland").bool()
        val retttileksportmendetkommeikketilutbetpgainntekt = saksbehandlervalg("retttileksportmendetkommeikketilutbetpgainntekt", "Rett til eksport av barnetillegg men dette kommer ikke til utbetaling").bool()
        val barnetilleggutenforavtaleland = saksbehandlervalg("barnetilleggutenforavtaleland", "Barnetillegg utenfor avtaleland").bool()
        val vedopphoravbarnetillegg = saksbehandlervalg("vedopphoravbarnetillegg", "Dersom opphør av barnetillegg").bool()
        val vedopphoravuforetrygd = saksbehandlervalg("vedopphoravuforetrygd", "Dersom opphør av uføretrygd").bool()
        val tyvearfulleksport = saksbehandlervalg("tyvearfulleksport", "20 år, full eksport").bool()
        val tyvearfulleksportviasammenlegging = saksbehandlervalg("tyvearfulleksportviasammenlegging", "20 år via sammenlegging, full eksport  EØS").bool()
        val mindreenn20areosfulleksport = saksbehandlervalg("mindreenn20areosfulleksport", "Mindre enn 20 år, EØS, full eksport").bool()
        val mindreenn20arikkeavtalelandgenopptjening = saksbehandlervalg("mindreenn20arikkeavtalelandgenopptjening", "Mindre enn 20 år, ikke avtaleland, egenopptjening").bool()
        val fritekstmal = saksbehandlervalg("fritekstmal", "Sett inn rett alternativ fra eksportmal").bool()

        title {
            text(bokmal { +"Uføretrygd - foreløpig eksportvurdering" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har foretatt en foreløpig eksportvurdering av din uføretrygd etter henvendelse fra deg den " + fritekst("dato") + ". Du ønsker å flytte til " + land.ifNull("land") + "." })
            }
            paragraph {
                text(bokmal { +"Generelt kan man ta med seg uføretrygden til utlandet uten hindringer, forutsatt at man har minst 20 års botid i Norge etter fylte 16 år. I tillegg må trygden være innvilget uten unntaksbestemmelser som flyktningstatus, ung ufør eller andre spesielle bestemmelser.  " })
            }
            showIf(fritekstmal) {
                paragraph {
                    text(bokmal { +fritekst("Sett inn rett alternativ fra eksportmal") })
                }
            }
            showIf(mindreenn20arikkeavtalelandgenopptjening) {
                paragraph {
                    text(bokmal { +"Uføretrygden din er innvilget uten unntaksbestemmelser, men du har mindre enn 20 års botid i Norge. Den som ikke har 20 års botid, men som har år med inntekt over ett grunnbeløp før uføretidspunktet, får eksportere egenopptjent uføretrygd basert på trygdetid tilsvarende antall år med slik inntekt, samt eventuell fremtidig trygdetid.  " })
                }
                paragraph {
                    text(bokmal { +"Du har hittil hatt utbetalt " + fritekst("minsteytelse / uføretrygd") + " beregnet med " + fritekst("antall år") + " års trygdetid. Ved flytting til " + land.ifNull("") + " vil du ha rett til egenopptjent uføretrygd beregnet med " + fritekst("antall år") + " års trygdetid. Med dagens grunnbeløp vil du få utbetalt " + fritekst("beløp") + " kroner i måneden før skatt. " })
                }
            }
            showIf(tyvearfulleksport) {
                paragraph {
                    text(bokmal { +"På bakgrunn av opplysninger i din sak har vi lagt til grunn at du har bodd i Norge i mer enn 20 år fra " + fritekst("dato") + " til den " + fritekst("dato") + ". Uføretrygden din er innvilget uten unntaksbestemmelser. Etter gjeldende regelverk kan du dermed ta med deg uføretrygden din til " + land.ifNull("") + " eller hvilket som helst land i verden uten at det medfører endringer i beløp. " })
                }
            }
            showIf(tyvearfulleksportviasammenlegging) {
                paragraph {
                    text(bokmal { +"Du har ikke mer enn 20 års botid i Norge og du er innvilget uføretrygd uten rettigheter til ung ufør og flyktning. Imidlertid har du vært medlem i EØS som Norge har trygdeavtale med, disse medlemskapsperiodene er likestilte med norske perioder når vi skal ta stilling til om du har 20 års trygdetid. Du oppfyller vilkåret om 20 års trygdetid via sammenlegging av trygdetidsperioder fra " + fritekst("land med opptjent trygdetid") + ".  " })
                }
                paragraph {
                    text(bokmal { +"Uføretrygden din er også innvilget uten unntaksbestemmelser. Etter gjeldende regelverk kan du dermed ta med deg uføretrygden din til " + land.ifNull("") + " eller hvilket som helst land i verden uten at det medfører endringer i beløp. " })
                }
            }
            showIf(mindreenn20areosfulleksport) {
                paragraph {
                    text(bokmal { +"Uføretrygden din er innvilget uten unntaksbestemmelser, men du har mindre enn 20 års botid i Norge.  " })
                }
                paragraph {
                    text(bokmal { +"Siden du ønsker å flytte til et EØS-land vil imidlertid bestemmelsene i EØS-avtalen overstyre bestemmelsene i folketrygdloven og det ses derfor bort i fra kravet om 20 års botid. Du kan derfor ta med deg uføretrygden din til " + land.ifNull("") + " uten endring i beløp.   " })
                }
            }
            showIf(hvisensligogminstesats) {
                paragraph {
                    text(bokmal { +"Hvis du gifter deg eller inngår samboerskap etter eventuell flytting, kan dette få betydning for beløpet. Det er derfor viktig at du melder fra om slike endringer.  " })
                }
            }
            showIf(barnetillegginnenavtaleland) {
                paragraph {
                    text(bokmal { +"Du har også utbetalt barnetillegg. Dette tillegget kan eksporteres hvis du etter flytting fremdeles bor sammen med eller forsørger " + fritekst("barnet/barna") + " du får utbetalt tillegget for, og både du og " + fritekst("barnet/barna") + " er bosatt i et land Norge har trygdeavtale med. Vennligst send inn dokumentasjon på dette ved en eventuell flytting." })
                }
            }
            showIf(retttileksportmendetkommeikketilutbetpgainntekt) {
                paragraph {
                    text(bokmal { +"Du er også innvilget barnetillegg i uføretrygden som ikke kommer til utbetaling grunnet for høy inntekt. Retten til dette tillegget kan eksporteres hvis du etter flytting fremdeles bor sammen med eller forsørger " + fritekst("barnet/barna") + " du er innvilget barnetillegg for, og både du og " + fritekst("barnet/barna") + " er bosatt i et land Norge har trygdeavtale med. Dersom din inntekt eller " + fritekst("din ektefelles/samboers inntekt") + " endres ved flytting til utlandet kan dette påvirke hvor mye barnetillegg du kan få utbetalt. Vennligst send inn dokumentasjon på at du har felles bosted som barna eller at du forsørger disse ved eventuell flytting til utlandet. Dersom din inntekt endres ved flytting til utlandet ber vi om at du også melder ifra om dette til Nav. " })
                }
            }
            showIf(barnetilleggutenforavtaleland) {
                paragraph {
                    text(bokmal { +"For å ha rett til barnetillegg" })
                }
                paragraph {
                    list {
                        item { text(bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med " }) }
                        item { text(bokmal { +"må " + fritekst("barnet/barna") + " også være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med. " }) }
                    }
                }
                paragraph {
                    text(bokmal { +"Norge har ikke trygdeavtale med " + land.ifNull("") + ". På bakgrunn av dette får du ikke med deg barnetillegget i uføretrygden til dette landet." })
                }
            }
            showIf(vedopphoravbarnetillegg) {
                paragraph {
                    text(bokmal { +"Dersom du flytter tilbake til Norge etter at barnetillegget er opphørt får du ikke automatisk tilbake tillegget. Du må da søke om barnetillegg på nytt. " })
                }
            }
            showIf(vedopphoravuforetrygd) {
                paragraph {
                    text(bokmal { +"Hvis du flytter tilbake til Norge etter at uføretrygden er opphørt får du ikke automatisk tilbake den tidligere ytelsen, men du må søke på nytt. Alle vilkårene for rett til uføretrygd i folketrygdloven kapittel 12 vil da bli vurdert på nytt.  " })
                }
            }
            paragraph {
                text(bokmal { +"Det er viktig å merke seg at dette kun er en foreløpig vurdering av eksportretten, og at vedtak gjøres når vi har mottatt melding om flytting." }, FontType.BOLD)
            }
            paragraph {
                text(bokmal { +"Vi minner om at flytting og ny adresse alltid skal meldes skriftlig til Folkeregisteret og til Nav." }, FontType.BOLD)
            }
        }
    }
}
