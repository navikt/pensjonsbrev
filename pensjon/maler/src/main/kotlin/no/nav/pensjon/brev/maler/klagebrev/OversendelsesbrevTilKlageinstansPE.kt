package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.maler.fraser.common.Constants.SAKSBEHANDLINGSTID_URL
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.collections.Set

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansPE : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOversendelsesbrevTilKlageinstansPE.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_KLAGE_OVERSENDELSESBREV_TIL_KLAGEINSTANS
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - Oversendelse til Nav Klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title { text(bokmal { +"Vi har sendt klagen din til $NAV_KLAGEINSTANS" }) }

        outline {

            paragraph {
                text(bokmal {
                    +"Vi har den "
                    + fritekst("dato") + " mottatt klagen din på vedtaket om "
                    +fritekst("ytelse") + " av den "
                    + fritekst("dato") + ". "
                    +"Etter en ny vurdering har vi kommet til at vedtaket ikke skal endres. "
                    +"Saken sendes derfor til $NAV_KLAGEINSTANS, som vil foreta en ny vurdering av klagen din."
                })
            }
            paragraph {
                text(bokmal {
                    +"Klageinstansen vurderer alle sider av saken på selvstendig grunnlag. "
                    +"Resultatet av klagebehandlingen kan bli at vårt vedtak ikke blir endret, eller at det blir endret helt eller delvis. "
                    +"Klageinstansen kan også oppheve vedtaket vårt, og sende saken tilbake til oss for helt eller delvis ny behandling."
                })
            }
            paragraph {
                text(bokmal { +"Saksbehandlingstiden til $NAV_KLAGEINSTANS finner du på $SAKSBEHANDLINGSTID_URL." })
                newline()
                newline()
            }

            title1 { text(bokmal { +"Dette er vurderingen vi har sendt til $NAV_KLAGEINSTANS" }) }

            title2 { text(bokmal { +"Klagers anførsler" }) }
            paragraph { text(bokmal { +fritekst("Gjengi hovedinnholdet i klagers anførsler.") }) }

            title2 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "Det skal gå fram hvorfor underinstansen opprettholder vedtaket. "
                                + "Det skal skrives en begrunnelse for hvorfor klager ikke fyller vilkårene. "
                                + "Klagers anførsler skal kommenteres / imøtegås. Drøftelsen skal avsluttes med en konklusjon."
                    )
                })
            }
        }
    }
}
