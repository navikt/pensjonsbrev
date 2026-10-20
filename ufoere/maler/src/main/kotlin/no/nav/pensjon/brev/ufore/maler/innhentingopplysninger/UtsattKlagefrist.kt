package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_UTSATT_KLAGEFRIST
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object UtsattKlagefrist : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_UTSATT_KLAGEFRIST
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Mottatt foreløpig klage",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Mottatt foreløpig klage" },
                nynorsk { + "Mottatt førebels klage" })
        }
        outline {
            paragraph {
                text(bokmal { +"Du har bedt oss om utsatt klagefrist. " },
                    nynorsk { +"Du har bede oss om utsett klagefrist. " })
            }
            paragraph {
                text(bokmal { +"Vi ber deg om å sende oss en utfyllende klage og eventuell tilleggsinformasjon innen to uker. Hvis vi ikke har fått utfyllende klage innen fristen, vil vi behandle klagen ut fra de opplysningene vi har i saken. " },
                    nynorsk { +"Vi ber deg om å sende oss ein utfyllande klage og eventuell tilleggsinformasjon innan to veker. Dersom vi ikkje har fått utfyllande klage innan fristen, vil vi behandle klagen ut frå dei opplysningane vi har i saka. " })
            }
            paragraph {
                text(bokmal { +"Trenger du mer tid, kan du sende oss en søknad og be om ytterligere utsettelse av klagefristen. Du må gi oss en begrunnelse for hvorfor du trenger mer tid. Vi gjør da en vurdering på om vi kan gi deg utsatt frist. " },
                    nynorsk { +"Treng du meir tid, kan du sende oss ein søknad og be om ytterlegare utsetjing av klagefristen. Du må gje oss ei grunngjeving for kvifor du treng meir tid. Vi gjer då ei vurdering på om vi kan gje deg utsett frist. " })
            }
            paragraph {
                text(bokmal { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL}. Dersom du sender opplysningene med posten, må de sendes inn sammen med vedlagt forside til oppgitt adresse på forsidearket snarest mulig. " },
                    nynorsk { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklast og raskast å ettersende digitalt. Du finn skjemaoversikta og rettleiing på nettsidene våre ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL}. Dersom du sender opplysningane med posten, må dei sendast inn saman med vedlagt forside til oppgitt adresse på forsidearket snarast mogleg. " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
