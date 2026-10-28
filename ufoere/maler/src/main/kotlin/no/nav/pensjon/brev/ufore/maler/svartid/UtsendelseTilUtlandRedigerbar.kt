package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object UtsendelseTilUtlandRedigerbar : RedigerbarTemplate<OrienteringOmSaksbehandlingstidDto> {

    override val featureToggle = FeatureToggles.utsendelseTilUtland.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_UTSENDELSE_TIL_UTENLANDSKE_TRYGDEMYND
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om utsendelse til utenlandske trygdemyndigheter",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
        letterDataType = OrienteringOmSaksbehandlingstidDto::class
    ) {
        val kunEOSInnvilgelseNorge = saksbehandlervalg("kunEOSInnvilgelseNorge", "Kun EØS/innvilgelse i Norge").bool()
        val kunEOSAvslagNorge = saksbehandlervalg("kunEOSAvslagNorge", "Kun EØS/avslag i Norge").bool()
        val paragraf12_2SentEOS = saksbehandlervalg("paragraf12_2SentEOS", "§ 12-2 ikke oppfylt etter nasjonale regler – sendt EØS").bool()
        val paragraf12_2SentAvtaleland = saksbehandlervalg("paragraf12_2SentAvtaleland", "§ 12-2 ikke oppfylt etter nasjonale regler – sendt avtaleland med sammenlegging").bool()
        val kunAvtalelandInnvilgelse = saksbehandlervalg("kunAvtalelandInnvilgelse", "Kun avtaleland uten sammenlegging/innvilgelse i Norge").bool()
        val kunAvtalelandAvslag = saksbehandlervalg("kunAvtalelandAvslag", "Kun avtaleland uten sammenlegging/avslag i Norge").bool()

        title {
            text(
                bokmal { + "Informasjon om oversendelse til utenlandske trygdemyndigheter" },
                nynorsk { + "Informasjon om oversending til utanlandske trygdemyndigheiter" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til søknaden din om uføretrygd. " },
                    nynorsk { + "Vi viser til søknaden din om uføretrygd. " },
                )
            }
            showIf(kunEOSInnvilgelseNorge or kunEOSAvslagNorge or paragraf12_2SentEOS) {
                paragraph {
                    text(
                        bokmal { + "Etter reglene i EØS-avtalen er et krav om uføreytelse i Norge også å anse som et krav om uføreytelse i andre EØS-land du kan ha opparbeidet deg trygdetid i. " },
                        nynorsk { + "Etter reglane i EØS-avtalen er eit krav om uføreytelse i Noreg òg å rekne som eit krav om uføreytelse i andre EØS-land du kan ha opparbeidd deg trygdetid i. " },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Fordi du har bodd og/eller arbeidet i et land Norge har trygdeavtale med, videresender vi søknaden din sammen med opplysninger om den norske trygdetiden din til trygdemyndighetene i " + fritekst("land") + ". " },
                    nynorsk { + "Fordi du har budd og/eller arbeidd i eit land Noreg har trygdeavtale med, videresender vi søknaden din saman med opplysningar om den norske trygdetida di til trygdemyndigheitene i " + fritekst("land") + ". " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Søknaden din om uføretrygd er sendt til de utenlandske trygdemyndighetene i dag. Søknadsdatoen din i utlandet vil bli den samme som i Norge. " },
                    nynorsk { + "Søknaden din om uføretrygd er sendt til dei utanlandske trygdemyndigheitene i dag. Søknadsdatoen din i utlandet vil bli den same som i Noreg. " },
                )
            }

            showIf(kunEOSInnvilgelseNorge) {
                paragraph {
                    text(
                        bokmal { + "Du er innvilget norsk uføretrygd med en foreløpig beregning. Uføretrygden din vil bli midlertidig utbetalt, fram til vi mottar vedtak og opplysninger om trygdetiden din fra utenlandske trygdemyndigheter. " },
                        nynorsk { + "Du er innvilga norsk uføretrygd med ei førebels berekning. Uføretrygda di vil bli midlertidig utbetalt, fram til vi mottek vedtak og opplysningar om trygdetida di frå utanlandske trygdemyndigheiter. " },
                    )
                }
                paragraph (uniqueness = "kunEOSInnvilgelseNorge") {
                    text(
                        bokmal { + "Du vil motta vedtak og eventuell utbetaling av utenlandsk ytelse fra utenlandske trygdemyndigheter. Nav vil sende deg et samlet vedtak med en endelig beregning som oppsummerer vedtakene både fra Norge og " + fritekst("land") + ". " },
                        nynorsk { + "Du vil motta vedtak og eventuell utbetaling av utanlandsk ytelse frå utanlandske trygdemyndigheiter. Nav vil sende deg eit samla vedtak med ei endeleg berekning som oppsummerer vedtaka både frå Noreg og " + fritekst("land") + ". " },
                    )
                }
            }

            showIf(kunEOSAvslagNorge) {
                paragraph {
                    text(
                        bokmal { + "Du har fått avslag på søknaden din om norsk uføretrygd. Du kan likevel ha rett på ytelse fra utenlandske trygdemyndigheter. " },
                        nynorsk { + "Du har fått avslag på søknaden din om norsk uføretrygd. Du kan likevel ha rett på ytelse frå utanlandske trygdemyndigheiter. " },
                    )
                }
                paragraph(uniqueness = "kunEOSAvslagNorge") {
                    text(
                        bokmal { + "Du vil motta vedtak og eventuell utbetaling av utenlandsk ytelse fra utenlandske trygdemyndigheter. Nav vil sende deg et samlet vedtak med en endelig beregning som oppsummerer vedtakene både fra Norge og " + fritekst("land") + ". " },
                        nynorsk { + "Du vil motta vedtak og eventuell utbetaling av utanlandsk ytelse frå utanlandske trygdemyndigheiter. Nav vil sende deg eit samla vedtak med ei endeleg berekning som oppsummerer vedtaka både frå Noreg og " + fritekst("land") + ". " },
                    )
                }
            }

            showIf(paragraf12_2SentEOS) {
                paragraph {
                    text(
                        bokmal { + "Nav har i brev " + fritekst("dato") + " funnet at vilkårene i folketrygdlovens § 12-2 ikke er oppfylt etter nasjonale regler. Nav må avvente bekreftelse på den utenlandske trygdetiden din, før vi kan avgjøre om du har rett på norsk uføretrygd etter bestemmelsene i EØS-avtalens forordning 883/2004. Nav arbeid og ytelser skal ta stilling til om vilkårene i § 12-2 kan oppfylles gjennom medregning av medlemstid for tilsvarende perioder i " + fritekst("land") + " så snart Nav mottar bekreftelse på medlemskapsperioder fra dette landet. " },
                        nynorsk { + "Nav har i brev " + fritekst("dato") + " funne at vilkåra i folketrygdlovens § 12-2 ikkje er oppfylte etter nasjonale reglar. Nav må avvente stadfesting av den utanlandske trygdetida di, før vi kan avgjere om du har rett på norsk uføretrygd etter bestemmelsane i EØS-avtalens forordning 883/2004. Nav arbeid og ytingar skal ta stilling til om vilkåra i § 12-2 kan oppfyllast gjennom medrekning av medlemstid for tilsvarande periodar i " + fritekst("land") + " så snart Nav mottar stadfesting av medlemskapsperiodar frå dette landet. " },
                    )
                }
                paragraph (uniqueness = "paragraf12_2SentEOS"){
                    text(
                        bokmal { + "Når alle nødvendige opplysninger, inkludert vedtak, er mottatt fra utlandet, vil Nav arbeid og ytelser fatte endelig vedtak om norsk uføretrygd. Du vil motta eget vedtak og eventuell utbetaling av ytelse fra utenlandske trygdemyndigheter. " },
                        nynorsk { + "Når alle nødvendige opplysningar, inkludert vedtak, er motteke frå utlandet, vil Nav arbeid og ytingar fatte endeleg vedtak om norsk uføretrygd. Du vil motta eige vedtak og eventuell utbetaling av yting frå utanlandske trygdemyndigheiter. " },
                    )
                }
            }

            showIf(paragraf12_2SentAvtaleland) {
                paragraph {
                    text(
                        bokmal { + "Nav har i brev " + fritekst("dato") + " funnet at vilkårene i folketrygdlovens § 12-2 ikke er oppfylt etter nasjonale regler. Nav må avvente bekreftelse på den utenlandske trygdetiden din, før vi kan avgjøre om du har rett på norsk uføretrygd etter bestemmelsene i " + fritekst("fyll inn trygdeavtale") + ". Nav arbeid og ytelser skal ta stilling til om vilkårene i § 12-2 kan oppfylles gjennom medregning av medlemstid for tilsvarende perioder i " + fritekst("land") + " så snart Nav mottar bekreftelse på medlemskapsperioder fra dette landet. " },
                        nynorsk { + "Nav har i brev " + fritekst("dato") + " funne at vilkåra i folketrygdlovens § 12-2 ikkje er oppfylte etter nasjonale reglar. Nav må avvente stadfesting av den utanlandske trygdetida di, før vi kan avgjere om du har rett på norsk uføretrygd etter bestemmelsane i " + fritekst("fyll inn trygdeavtale") + ". Nav arbeid og ytingar skal ta stilling til om vilkåra i § 12-2 kan oppfyllast gjennom medrekning av medlemstid for tilsvarande periodar i " + fritekst("land") + " så snart Nav mottar stadfesting av medlemskapsperiodar frå dette landet. " },
                    )
                }
                paragraph (uniqueness = "paragraf12_2SentAvtaleland"){
                    text(
                        bokmal { + "Når alle nødvendige opplysninger, inkludert vedtak, er mottatt fra utlandet, vil Nav arbeid og ytelser fatte endelig vedtak om norsk uføretrygd. Du vil motta eget vedtak og eventuell utbetaling av ytelse fra utenlandske trygdemyndigheter. " },
                        nynorsk { + "Når alle nødvendige opplysningar, inkludert vedtak, er motteke frå utlandet, vil Nav arbeid og ytingar fatte endeleg vedtak om norsk uføretrygd. Du vil motta eige vedtak og eventuell utbetaling av yting frå utanlandske trygdemyndigheiter. " },
                    )
                }
            }

            showIf(kunAvtalelandInnvilgelse) {
                paragraph {
                    text(
                        bokmal { + "Du er innvilget norsk uføretrygd. Du vil motta vedtak og eventuell utbetaling av utenlandsk ytelse fra utenlandske trygdemyndigheter. Dette vedtaket vil ikke påvirke den norske uføretrygden din. " },
                        nynorsk { + "Du er innvilga norsk uføretrygd. Du vil motta vedtak og eventuell utbetaling av utanlandsk yting frå utanlandske trygdemyndigheiter. Dette vedtaket vil ikkje påverke den norske uføretrygda di. " },
                    )
                }
                paragraph (uniqueness = "kunAvtalelandInnvilgelse") {
                    text(
                        bokmal { + "For spørsmål angående dine eventuelle trygderettigheter i " + fritekst("land") + ", ber vi deg kontakte " + fritekst("lands") + " trygdemyndigheter: " + fritekst("adresse til trygdemyndighet") + ". " },
                        nynorsk { + "For spørsmål angåande dine eventuelle trygderettar i " + fritekst("land") + ", ber vi deg kontakte " + fritekst("lands") + " trygdemyndigheiter: " + fritekst("adresse til trygdemyndigheit") + ". " },
                    )
                }
            }

            showIf(kunAvtalelandAvslag) {
                paragraph {
                    text(
                        bokmal { + "Du har fått avslag på søknaden din om norsk uføretrygd. Du kan likevel ha rett på ytelse fra utenlandske trygdemyndigheter. Du vil motta vedtak om og eventuell utbetaling av utenlandsk ytelse fra utenlandske trygdemyndigheter. Dette vedtaket vil ikke påvirke avslaget på norsk uføretrygd. " },
                        nynorsk { + "Du har fått avslag på søknaden din om norsk uføretrygd. Du kan likevel ha rett på ytelse frå utanlandske trygdemyndigheiter. Du vil motta vedtak om og eventuell utbetaling av utanlandsk yting frå utanlandske trygdemyndigheiter. Dette vedtaket vil ikkje påverke avslaget på norsk uføretrygd. " },
                    )
                }
                paragraph (uniqueness = "kunAvtalelandAvslag") {
                    text(
                        bokmal { + "For spørsmål angående dine eventuelle trygderettigheter i " + fritekst("land") + ", ber vi deg kontakte " + fritekst("lands") + " trygdemyndigheter: " + fritekst("adresse til trygdemyndighet") + ". " },
                        nynorsk { + "For spørsmål angåande dine eventuelle trygderettar i " + fritekst("land") + ", ber vi deg kontakte " + fritekst("lands") + " trygdemyndigheiter: " + fritekst("adresse til trygdemyndigheit") + ". " },
                    )
                }
            }

            title1 {
                text(
                    bokmal { + "Behandlingstid" },
                    nynorsk { + "Behandlingstid" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi kan dessverre ikke si noe om andre lands saksbehandlingstid utover at den er svært varierende, og kan være lang. Saksbehandlingstiden hos utenlandske trygdemyndigheter varierer. " },
                    nynorsk { + "Vi kan dessverre ikkje seie noko om andre lands saksbehandlingstid utover at den er svært varierande, og kan vere lang. Saksbehandlingstida hos utanlandske trygdemyndigheiter varierer. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Utenlandske trygdemyndigheter vurderer søknaden din om uføretrygd etter eget lovverk og egen forvaltningspraksis. Nav kan bare formidle informasjon, vi påvirker ikke avgjørelsene deres. " },
                    nynorsk { + "Utanlandske trygdemyndigheiter vurderer søknaden din om uføretrygd etter eige lovverk og eigen forvaltningspraksis. Nav kan berre formidle informasjon, vi påverkar ikkje avgjerdene deira. " },
                )
            }
            includePhrase(OrienteringOmSaksbehandlingstidFraser.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}