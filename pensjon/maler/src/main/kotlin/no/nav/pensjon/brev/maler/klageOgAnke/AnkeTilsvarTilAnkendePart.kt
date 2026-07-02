package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.AnkeTilsvarTilAnkendePartDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.ankeTilsvarTilAnkendePartDto.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.ankeTilsvarTilAnkendePartDto.pesysData.foedselsnummer
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.ankeTilsvarTilAnkendePartDto.pesysData.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.ankeTilsvarTilAnkendePartDto.pesysData.navnAvsenderEnhet
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori.*
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

// Erstatte PE_IY_03_151

@TemplateModelHelpers
object AnkeTilsvarTilAnkendePart : RedigerbarTemplate<AnkeTilsvarTilAnkendePartDto> {

    override val featureToggle = FeatureToggles.brevmalAnkeTilsvarTilAnkendePart.toggle

    override val kode = PE_ANKE_TILSVAR_TIL_ANKENDE_PART
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Anke - tilsvar til ankende part",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )

    ) {
        title {
            text(
                bokmal { +"Anke " + fritekst("ytelse") },
                english { +"Appeal " + fritekst("ytelse") }
            )
        }

        outline {
            paragraph {
                text(bokmal { +"Den ankende part: " }, english { +"The appellant: " }, BOLD)
                text(bokmal { +pesysData.navn + " " }, english { +pesysData.navn + " " })
                text(bokmal { +pesysData.foedselsnummer.format() }, english { +pesysData.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Ankemotpart: " }, english { +"Other party: " }, BOLD)
                text(bokmal { +"Nav Klageinstans" }, english { +"Nav Klageinstans" })
            }
            paragraph {
                text(
                    bokmal { +"Nav Klageinstans har vurdert vedtaket vårt av " + fritekst("vedtaksdato") + " på nytt, men har ikke funnet grunnlag for å gjøre det om." },
                    english { +"Nav Klageinstans has reviewed our decision of " + fritekst("vedtaksdato") + " again, but has found no grounds for amending it." }
                )
            }
            title1 { text(bokmal { +"Anledning til ytterligere merknader" }, english { +"Opportunity to make further comments" }) }
            paragraph {
                text(
                    bokmal {
                        +"I vedlagte kopi av vårt brev til Trygderetten har vi gjort rede for saksforholdet med nærmere begrunnelse for hvorfor vi opprettholder vedtaket."
                        +" Brevet er foreløpig ikke sendt til Trygderetten."
                    },
                    english { +"In the enclosed copy of our letter to the National Insurance Court,"
                        +" we have explained the circumstances of the case with further reasons why we uphold the decision."
                        +" For the moment, the letter has not been sent to the National Insurance Court." }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du har anledning til å komme med merknader til brevet innen tre uker fra du mottar dette brevet."
                        +" Saken vil bli sendt til Trygderetten til videre behandling, når fristen for å gi merknader er gått ut."
                    },
                    english { +"You have the opportunity to comment on the letter within three weeks from when you receive this letter."
                        +" The case will be passed to the National Insurance Court for further review once the time limit for commenting has expired." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du ønsker å komme med merknader til brevet, må du skrive til oss på følgende adresse:" },
                    english { +"If you would like to comment on the letter, please write to us at the following address:" }
                )
            }
            paragraph {
                text(bokmal { +pesysData.navnAvsenderEnhet }, english { +pesysData.navnAvsenderEnhet })
                newline()
                text(bokmal { +"Postboks 6600 Etterstad" }, english { +"Postboks 6600 Etterstad" })
                newline()
                text(bokmal { +"0607 OSLO" }, english { +"0607 OSLO" })
                newline()
                text(bokmal { +"" }, english { +"NORWAY" })
            }
            paragraph {
                text(
                    bokmal { +"Når saken er avgjort, vil du få melding om resultatet direkte fra Trygderetten." },
                    english { +"When the case has been decided, you will be notified of the result directly by the National Insurance Court." }
                )
            }

        }
    }
}
