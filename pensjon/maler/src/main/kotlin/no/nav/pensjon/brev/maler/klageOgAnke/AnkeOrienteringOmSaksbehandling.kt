package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer

@TemplateModelHelpers
object AnkeOrienteringOmSaksbehandling : RedigerbarTemplate<EmptyRedigerbarBrevdataMedSaksbehandlerValg> {

    override val featureToggle = FeatureToggles.brevmalAnkeOrienteringOmSaksbehandling.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_ANKE_ORIENTERING_OM_SAKSBEHANDLING
    override val kategori = no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = no.nav.pensjon.brev.api.model.Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Anke - orientering om saksbehandling ved Nav klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Anke - " + fritekst("ytelse") + " - Orientering om saksbehandling" },
                english { +"Appeal - " + fritekst("ytelse") + " - Briefing on case proceedings" })
        }

        outline {
            paragraph {
                text(bokmal { +"Ankende part: " }, english { +"Appellant: " }, BOLD)
                text(bokmal { +felles.bruker.fulltNavn() + " " }, english { +felles.bruker.fulltNavn() + " " })
                text(
                    bokmal { +felles.bruker.foedselsnummer.format() },
                    english { +felles.bruker.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Ankemotpart: " }, english { +"Other party: " }, BOLD)
                text(bokmal { +NAV_KLAGEINSTANS }, english { +NAV_KLAGEINSTANS })
            }
            paragraph {
                text(
                    bokmal { +"Vi har mottatt anken din av " + fritekst("ankedato") + " over vedtaket av " + fritekst("dato på klagevedtaket") + "." },
                    english {
                        +"We have received your appeal of " + fritekst("ankedato") + " against the decision of "
                        +fritekst("dato på klagevedtaket") + "."
                    }
                )
            }
            title1 { text(bokmal { +"Ny prøving" }, english { +"New examination" }) }
            paragraph {
                text(
                    bokmal {
                        +"$NAV_KLAGEINSTANS skal prøve vedtaket som er påanket på nytt."
                        +" Hvis dette ikke fører til at vedtaket blir omgjort, blir saken sendt til Trygderetten til avgjørelse."
                        +" Dette følger av lov om anke til Trygderetten paragraf 13."
                    },
                    english { +"$NAV_KLAGEINSTANS will reconsider the decision that is being appealed."
                            + " If this does not result in the decision being amended, the case will be passed to the National Insurance Court for decision."
                            + " This is pursuant to Section 13 of the Act governing appeals to the National Insurance Court." }
                )
            }
            title1 { text(bokmal { +"Anledning til nye merknader" }, english { +"Opportunity for further comment" }) }
            paragraph {
                text(
                    bokmal {
                        +"I forbindelse med at vi sender saken til Trygderetten,"
                        +" vil det bli utarbeidet et oversendelsesbrev der det blir redegjort for saksforholdet og begrunnelsen for vedtaket."
                        +" Du vil få kopi av oversendelsesbrevet før saken sendes til Trygderetten."
                        +" Du får da anledning til å komme med ytterligere merknader."
                    },
                    english {
                        +"In the event that the case is referred to the National Insurance Court,"
                        +" a covering letter will be drawn up, which will explain the case circumstances and reasons for the decision."
                        +" You will receive a copy of the covering letter before the covering letter is sent to the National Insurance Court."
                        +" You will then have an opportunity to make further comments."
                    }
                )
            }
            title1 { text(bokmal { +"Behandlingstid" }, english { +"Processing time" }) }
            paragraph {
                text(
                    bokmal {
                        +"Saksbehandlingstiden er vanligvis " + fritekst("antall dager/uker/måneder") + "."
                        +" Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed."
                    },
                    english {
                        +"The processing time is normally " + fritekst("antall dager/uker/måneder") + "."
                        +"If the processing of your case has not been completed within that time, you will be notified."
                    }
                )
            }
            title1 { text(bokmal { +"Meld fra om endringer" }, english { +"Please notify us of changes" }) }
            paragraph {
                text(
                    bokmal {
                        +"Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av saken din."
                        +" Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende."
                    },
                    english {
                        +"Please keep us informed about circumstances that can affect the decision on your case."
                        +" These might be changes in circumstances relating to health, work, income, civil status and similar."
                    }
                )
            }
        }
    }
}