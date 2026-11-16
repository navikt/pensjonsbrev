package no.nav.pensjon.brev.maler.ufore.utland

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.UT_EKSPORT_AV_UFORETRYGD
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.EksportAvUforetrygdDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.barnetilleggAvtaleland
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.btInnvilgetMenIngenUtbetaling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.ensligOgMinstesats
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.eosNordisk
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.flereBarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.fullEksport
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.gjtAvkortet
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.gjtEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.gjtUendret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.innvilgetBT
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.minstesatsTilBruker
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.muligTilbakekrevingVarsel
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.sumUtOgTillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.selectors.eksportAvUforetrygdDto.pesysData.virkningstidspunkt
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EksportAvUforetrygd : RedigerbarTemplate<EksportAvUforetrygdDto> {
    override val kode = UT_EKSPORT_AV_UFORETRYGD
    override val kategori = Brevkategori.VEDTAK_EKSPORT
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Eksport av uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        val arsak = saksbehandlervalg("arsak", "Velg årsak").enum<Arsak>()
        val manglendeadresse = saksbehandlervalg("manglendeadresse", "Dersom bruker mangler adresse").bool()
        val ungUfor20Ar = saksbehandlervalg("ungUfor20Ar", "20 år ung ufør").bool()
        val flyktningArsak = saksbehandlervalg("flyktningArsak", "Flyktning 20 år, velg årsak").enum<FlyktningArsak>()
        val tyveArNorgeFullEksport = saksbehandlervalg("tyveArNorgeFullEksport", "20 år i Norge, full eksport").bool()
        val tyveArSammenleggingFullEksport = saksbehandlervalg("tyveArSammenleggingFullEksport", "20 år via sammenlegging, full eksport").bool()
        val minstesatsOgFlytterSammen = saksbehandlervalg("minstesatsOgFlytterSammen", "Minstesats og flytter sammen med ektefelle:").bool()
        val vedOpphor = saksbehandlervalg("vedOpphor", "Ved opphør").bool()

        title {
            text(bokmal { +"Du kan ta med deg uføretrygden din til utlandet" })
        }
        outline {
            showIf(arsak.equalTo(Arsak.FLYTTEDATOFRABRUKER)) {
                paragraph {
                    text(bokmal { +"Du har flyttet til " + fritekst("land") + " den " + fritekst("dato") + " og ditt pliktige medlemskap i folketrygden er opphørt fra denne datoen." })
                }
            }.orShowIf(arsak.equalTo(Arsak.MOTTATTOPPLFLYTTING)) {
                paragraph {
                    text(bokmal { +"Vi har mottatt melding fra " + fritekst("kilde") + " den " + fritekst("dato") + " om at du har flyttet til " + fritekst("land") + " den " + fritekst("dato") + ". Ditt pliktige medlemskap i folketrygden er opphørt fra denne datoen." })
                }
            }.orShowIf(arsak.equalTo(Arsak.LENGREOPPHOLDUTLAND)) {
                paragraph {
                    text(bokmal { +"Vi har mottatt opplysninger om at du har oppholdt deg i utlandet i mer enn " + fritekst("12 måneder / seks måneder i to eller flere år på rad") + ". Ved utenlandsopphold som overstiger " + fritekst("12 måneder eller 6 måneder per år i to eller flere år på rad") + ", anses man ikke lenger å være medlem i folketrygden." })
                }
            }
            showIf(pesysData.eosNordisk) {
                paragraph {
                    text(bokmal { +"Vi har vurdert at du har flyttet til " + fritekst("land") + " og at ditt senter for livsinteresser nå er der." })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +"Vi har mottatt melding fra " + fritekst("kilde") + " den " + fritekst("dato") + " om at du har flyttet til " + fritekst("land") + " den " + fritekst("dato") + ". Ditt bosted og senter for livsinteresser er nå der. Ditt pliktige medlemskap i folketrygden er derfor opphørt fra denne datoen." })
                }
            }
            paragraph {
                text(bokmal { +"Du får " + ifElse(pesysData.fullEksport, "fortsatt ", "") + pesysData.sumUtOgTillegg.format() + " i uføretrygd per måned før skatt." })
            }
            showIf(pesysData.muligTilbakekrevingVarsel) {
                paragraph {
                    text(bokmal { +"Vi har endret uføretrygden din fra første mulige utbetaling, som er " + pesysData.virkningstidspunkt.format() + ". Opplysningene vi har tilsier at du kan ha fått for mye utbetalt tilbake i tid. Nav vil vurdere om dette skal betales tilbake, og du vil i så fall motta et eget brev om dette senere." })
                }
            }
            showIf(pesysData.sumUtOgTillegg.greaterThan(0)) {
                paragraph {
                    text(bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket." })
                }
            }
            showIf(manglendeadresse) {
                paragraph {
                    text(bokmal { +"Vi minner om at flytting og ny adresse alltid skal meldes skriftlig både til Folkeregisteret og til Nav. Hvis du har adresse i utlandet i tillegg til adresse i Norge, er det viktig at Nav informeres om begge adressene." })
                }
            }
            title1 {
                text(bokmal { +"Begrunnelse for vedtaket" })
            }
            paragraph {
                text(bokmal { +"For at uføretrygden ikke skal endres ved flytting til utlandet må du som hovedregel ha 20 års trygdetid i Norge etter at du fylte 16 år. Uføretrygden din må også være innvilget uten unntaksbestemmelser som ung ufør og flyktningstatus." })
            }
            showIf(pesysData.fullEksport) {
                showIf(tyveArNorgeFullEksport) {
                    paragraph {
                        text(bokmal { +"Du har bodd i Norge i mer enn 20 år og er innvilget uføretrygd uten rettigheter til ung ufør og flyktning. Du vil beholde uføretrygden din uendret i beløp når du flytter til (land), eller et hvilket som helst annet land i verden." })
                    }
                }
                showIf(tyveArSammenleggingFullEksport) {
                    paragraph {
                        text(bokmal { +"Du er innvilget uføretrygd uten rettigheter til ung ufør og flyktning, men du har ikke mer enn 20 år botid i Norge. Imidlertid har du vært medlem i et land Norge har trygdeavtale med, disse medlemskapsperiodene er likestilte med norske perioder når vi skal ta stilling til om du har 20 års trygdetid." })
                    }
                    paragraph {
                        text(bokmal { +"Du oppfyller vilkåret om 20 års trygdetid via sammenlegging av trygdetidsperioder fra LAND. Dette gjør at du får beholde uføretrygden din uendret når du flytter til (land), eller et hvilket som helst annet land i verden." })
                    }
                }
            }
            showIf(ungUfor20Ar) {
                paragraph {
                    text(bokmal { +"Du har bodd i Norge i mer enn 20 år, men uføretrygden din er innvilget med rettighet som ung ufør. Siden du har flyttet til utlandet har du ikke lenger rett til uføretrygd beregnet som ung ufør, denne beregningen beholdes kun så lenge man er medlem i den norske folketrygden.  Uføretrygden din er derfor endret til " + fritekst("minsteytelse uten beregning som ung ufør // din egenopptjening.") })
                }
                paragraph {
                    text(bokmal { +fritekst("Hvis egenopptjening høyere enn ung ufør") + " Du har bodd i Norge i mer enn 20 år, men uføretrygden din er innvilget med rettighet som ung ufør. Siden du har flyttet til utlandet har du ikke lenger rett til ung ufør-beregning. Denne beregningen gjelder bare så lenge du er medlem i norsk folketrygd, og medlemskapet dekker uføretrygd. Du har en egenopptjening som er høyere enn minsteytelse for ung ufør. Dette medfører at det ikke blir endring i din utbetaling, selv om ung ufør-rettigheten faller bort." })
                }
            }
            showIf(flyktningArsak.equalTo(FlyktningArsak.FLYKTNINGOVER20ARREDUKSJON)) {
                paragraph {
                    text(bokmal { +"Du har mer enn 20 års botid i Norge, men du er innvilget uføretrygd med rettigheter for flyktninger. Dette betyr at uføretrygden din har vært beregnet uten hensyn til reglene om trygdetid. Siden du nå har flyttet til " + fritekst("land") + ", har du kun rett til uføretrygd basert på faktisk trygdetid inkludert framtidig tid, hvilket i ditt tilfelle er " + fritekst("antall") + " år." })
                }
            }.orShowIf(flyktningArsak.equalTo(FlyktningArsak.FLYKTNINGUNDER20ÅREØS)) {
                paragraph {
                    text(bokmal { +"Uføretrygden din er i tillegg innvilget med rettigheter for flyktninger. Dette betyr at uføretrygden din har vært beregnet med full norsk trygdetid på 40 år. Ifølge folketrygdloven beholdes denne rettigheten ikke når når medlemskapet i folketrygden opphører, eksempelvis ved flytting til utlandet. Siden du har flyttet til et EØS-land overstyrer bestemmelsene i EØS-avtalen bestemmelsene i folketrygdloven. Du har i henhold til bestemmelser i EØS-avtalen rett til å få med deg din uføretrygd uten endring i beløp." })
                }
            }.orShowIf(flyktningArsak.equalTo(FlyktningArsak.FLYKTNINGINGENENDRINGAVTALELAND)) {
                paragraph {
                    text(bokmal { +"Du har mer " + fritekst("over/under") + " 20 års botid i Norge, og du er innvilget uføretrygd med rettigheter for flyktninger. Som hovedregel beholder man kun uføretrygd beregnet etter regler for flyktninger så lenge man er medlem i den norske folketrygden. Du ønsker å flytte til " + fritekst("land") + " som Norge har en trygdeavtale med, denne avtalen gir adgang til å beholde uføretrygd uendret med rettighet som flyktning. Følgelig vil din uføretrygd forbli uendret etter flytting til " + fritekst("land") + "." })
                }
            }
            showIf(pesysData.ensligOgMinstesats) {
                paragraph {
                    text(bokmal { +"Hvis du gifter deg eller inngår samboerskap, kan dette få betydning for størrelsen på din uføretrygd. Det er derfor viktig at du melder fra om slike endringer." })
                }
            }
            showIf(minstesatsOgFlytterSammen) {
                paragraph {
                    text(bokmal { +"Du oppgir at du flytter sammen med ektefelle i (land). Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Minsteytelse for deg utgjør " + pesysData.minstesatsTilBruker.format() + " ganger folketrygdens grunnbeløp." })
                }
            }
            showIf(pesysData.barnetilleggAvtaleland and pesysData.innvilgetBT) {
                paragraph {
                    text(bokmal { +"Du har også utbetalt barnetillegg. Dette tillegget kan eksporteres hvis du etter flytting fremdeles bor sammen med eller forsørger barnet/barna du får utbetalt tillegget for. I tillegg må du og " + ifElse(pesysData.flereBarn, "barna", "barnet") + " dine være bosatt i Norge eller et land Norge har trygdeavtale med. Barnet/barna må også oppholde seg i Norge eller et avtaleland, med unntak av maks 90 dager per år." })
                }
                paragraph {
                    text(bokmal { +"Du fyller fremdeles kravet til forsørgelse av " + ifElse(pesysData.flereBarn, "barna", "barnet") + ". Du er bosatt i land og barnet/barna ditt er bosatt og oppholder seg i land. Vi har derfor kommet til at du fremdeles fyller kravene for rett til barnetillegg i din uføretrygd." })
                }
            }.orShowIf(pesysData.innvilgetBT and pesysData.btInnvilgetMenIngenUtbetaling) {
                paragraph {
                    text(bokmal { +"Du er også innvilget barnetillegg i uføretrygden som ikke kommer til utbetaling grunnet for høy inntekt. Dette tillegget kan eksporteres hvis du etter flytting fremdeles bor sammen med eller forsørger barnet/barna du får utbetalt tillegget for. I tillegg må du og barnet/barna dine være bosatt i Norge eller et land Norge har trygdeavtale med. Barnet/barna må også oppholde seg i Norge eller et avtaleland, med unntak av maks 90 dager per år." })
                }
                paragraph {
                    text(bokmal { +"Dersom din inntekt " + fritekst("eller din ektefelles/samboers inntekt") + " endres ved flytting til utlandet kan dette påvirke hvor mye barnetillegg du kan få utbetalt. Dersom din inntekt endres ved flytting til utlandet ber vi om at du også melder ifra om dette til Nav." })
                }
                paragraph {
                    text(bokmal { +"Du fyller fremdeles kravet til forsørgelse av " + ifElse(pesysData.flereBarn, "barna", "barnet") + ". Du er bosatt i " + fritekst("land") + " og " + ifElse(pesysData.flereBarn, "barna", "barnet") + " ditt er bosatt og oppholder seg i " + fritekst("land") + ". Vi har derfor kommet til at du fremdeles fyller kravene for rett til barnetillegg i din uføretrygd." })
                }
            }
            showIf(not(pesysData.barnetilleggAvtaleland)) {
                paragraph {
                    text(bokmal { +"For å ha rett til barnetillegg" })
                    list {
                        item { text(bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" }) }
                        item { text(bokmal { +"må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med." }) }
                    }
                }
                paragraph {
                    text(bokmal { +"Norge har ikke trygdeavtale med " + fritekst("land") + ". På bakgrunn av dette beholder du ikke barnetillegget i uføretrygden etter flytting til dette landet." })
                }
            }
            showIf(pesysData.gjtUendret) {
                paragraph {
                    text(bokmal { +"Flytting til utlandet medfører ingen endring av din uføretrygd og ditt gjenlevendetillegg i uføretrygden forblir også uendret etter at ditt medlemskap i folketrygden er opphørt." })
                }
            }.orShowIf(pesysData.gjtEndret) {
                paragraph {
                    text(bokmal { +"Når uføretrygden endres ved flytting til utlandet skal også gjenlevendetillegget endres forholdsmessig. Dette vil si at gjenlevendetillegget skal reduseres tilsvarende den prosentvise reduksjonen av uføretrygden etter eksport. Gjenlevendetilleggets nye månedlige beløp før skatt er " + pesysData.gjtAvkortet.format() + "." })
                }
            }
            showIf(vedOpphor) {
                paragraph {
                    text(bokmal { +"Hvis du flytter tilbake til Norge får du ikke automatisk tilbake den tidligere ytelsen, men du må søke på nytt." })
                }
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven § 12-3, EØS-forordning 987/2009 Art. 11 og EØS-forordning 883/2004 artikkel 7, [ved ung ufør: art. 70 og vedlegg X til EØS-forordning 883/2004, gitt i vedlegg VI del 1 nr. 1 bokstav k til EØS-avtalen.]." })
            }
            includePhrase(Felles.MeldeFraEndringer)
            includePhrase(Ufoeretrygd.RettTilAKlage)
            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.SkattTilNorge)
            includePhrase(Ufoeretrygd.HarDuSporsmal)
        }
    }
}

enum class Arsak(override val displayText: String) : SaksbehandlerValgEnum {
    FLYTTEDATOFRABRUKER("Mottatt flyttedato fra bruker"),
    MOTTATTOPPLFLYTTING("Mottatt opplysninger om flytting fra andre"),
    LENGREOPPHOLDUTLAND("Mottatt opplysninger om lengre opphold i utlandet"),
}

enum class FlyktningArsak(override val displayText: String) : SaksbehandlerValgEnum {
    IKKEFLYKTNING("Ikke flyktningstatus"),
    FLYKTNINGOVER20ARREDUKSJON("Flyktning- over 20 år - reduksjon"),
    FLYKTNINGUNDER20ÅREØS("Flyktning, under 20 år - EØS"),
    FLYKTNINGINGENENDRINGAVTALELAND("Flyktning – ingen endring - avtaleland"),
}