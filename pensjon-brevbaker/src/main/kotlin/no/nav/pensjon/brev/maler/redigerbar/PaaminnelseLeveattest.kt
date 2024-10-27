package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.redigerbar.PaaminnelseLeveattestDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.PaaminnelseLeveattestDtoSelectors.PesysdataSelectors.fristdato
import no.nav.pensjon.brev.api.model.maler.redigerbar.PaaminnelseLeveattestDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object PaaminnelseLeveattest : RedigerbarTemplate<PaaminnelseLeveattestDto> {

    override val kode = Brevkode.Redigerbar.PE_PAAMINNELSE_LEVEATTEST
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV

    override val template = createTemplate(
        name = kode.name,
        letterDataType = PaaminnelseLeveattestDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Påminnelse - leveattest",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                //[PE_IY_03_178_overskrift]
                Bokmal to "PÅMINNELSE - LEVEATTEST"
            )
        }
        outline {

            //[PE_IY_05_411_tekst1]
            paragraph {
                list {
                    item {
                        Bokmal to "REMINDER  -  LIFE CERTIFICAT"
                    }
                    item {
                        Bokmal to "MAHNBRIEF - LEBENSBESCHEINIGUNG (DEUTSCH siehe die Rückseite)"
                    }
                    item {
                        Bokmal to "RAPPEL - CERTIFICAT DE VIE (FRANÇAIS - voir au verso)"
                    }
                    item {
                        Bokmal to "RECORDATORIO - CERTIFICADO DE FE DE VIDA (ESPAÑOL - ver al dorso)"
                    }
                }
            }

            //[PE_IY_03_178_tekst]

            title2 {
                text(Bokmal to "NORSK:")
            }
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere tilsendt leveattest. Fristen for innsendelse av denne er utløpt, og vi sender nå en påminnelse til de vi ikke har mottatt attest fra.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                textExpr(
                    Bokmal to "Vedlagt følger en ny leveattest. Vennligst les veiledningen på baksiden før du fyller ut attesten. Attesten må sendes innen ".expr() + pesysData.fristdato
                        .format() + ".",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Dersom vi ikke mottar attesten innen denne datoen vil utbetalingen bli stanset. Du vil i så fall motta vedtak om stans av ytelse.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Skulle du ha sendt inn leveattesten i løpet av dagene etter den opprinnelige fristen, kan du se bort fra denne henvendelsen.",
                )
            }
            //[PE_IY_03_178_tekst]

            title2 {
                text(Bokmal to "ENGLISH: ")
            }
            paragraph {
                text(
                    Bokmal to "We have previously sent you a life certificate. The return deadline has expired, and we are now sending a reminder to those we have not received the certificate from.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                textExpr(
                    Bokmal to "You will find a new life certificate enclosed. Please read the guidelines on the reverse side before filling out the certificate. The certificate must be returned within ".expr() + pesysData.fristdato
                        .format() + ".",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "If we have not received your certificate within this date, your pension payments will cease and you will receive an administrative decision stopping your pension payments.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "If you mailed your certificate during the first few days following deadline, please disregard this reminder.",
                )
            }
            //[PE_IY_03_178_tekst]

            title2 {
                text(Bokmal to "DEUTSCH:")
            }
            paragraph {
                text(
                    Bokmal to "Wir verweisen auf die Lebensbescheinigung, die Sie früher erhalten haben. Die Einsendefrist dieser Bescheinigung ist abgelaufen. Wir schicken jetzt ein Mahnschreiben an alle Personen, von denen wir keine Bescheinigung erhalten haben.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                textExpr(
                    Bokmal to "Beigelegt finden Sie ein neues Lebensbescheinigungsformular. Bitte beachten Sie die Hinweise auf der Rückseite, bevor Sie die Bescheinigung ausfüllen. Die Bescheinigung muß bis spätestens ".expr() + pesysData.fristdato
                        .format() + ". zurückgeschickt werden.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Sollten wir die Bescheinigung nicht innerhalb des besagten Datums erhalten, wird die Auszahlung Ihrer Rente eingestellt. In diesem Fall werden Sie einen Bescheid über den Beschluß der eingestellten Rentenzahlung erhalten.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Falls Sie die Lebensbescheinigung im Laufe der ersten Tage nach der ursprünglichen Frist abgeschickt haben, sehen Sie bitte von diesem Mahnschreiben ab.",
                )
            }
            //[PE_IY_03_178_tekst]

            title2 {
                text(
                    Bokmal to "FRANÇAIS:"
                )
            }
            paragraph {
                text(
                    Bokmal to "Veuillez noter que la date de limite de renvoi du certificat de vie a expiré. Nous envoyons maintenant un rappel à ceux dont nous n'avons pas reçu de certificat.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Nous vous joignons un nouveau formulaire. Veuillez lire les instructions portées au verso avant de remplir ce formulaire.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                textExpr(
                    Bokmal to "Le certificat de vie devra être adressé avant ".expr() + pesysData.fristdato
                        .format() + ".",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Si nous ne recevons pas votre certificat avant cette date, nous cesserons les versements de votre pension. Dans ce cas vous recevrez une décision de cessation de versement de pension.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Au cas où vous auriez envoyé votre certificat de vie dans les jours suivant la date limite fixée à l'origine, nous vous prions de ne pas tenir compte du présent rappel.",
                )
            }
            //[PE_IY_03_178_tekst]

            title2 {
                text(Bokmal to "ESPAÑOL:")
            }
            paragraph {
                text(
                    Bokmal to "Hacemos referencia  al certificado de fe de vida que se le ha enviado previamente. El plazo para entregar el certificado ha vencido, y así mandamos un recordatorio a los que no han remitido el certificado.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Le mandamos adjunto un nuevo certificado de fe de vida. Le rogamos que lea atentamente las instrucciones al dorso del certificado antes de rellenarlo.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                textExpr(
                    Bokmal to "Debe remitir el certificado antes del ".expr() + pesysData.fristdato
                        .format() + ".",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Si no recibimos el certificado dentro del mencionado plazo, se suspenderá el pago de la pensión. En este caso, recibirá una resolución de suspensión de pago de la pensión.",
                )
            }
            //[PE_IY_03_178_tekst]

            paragraph {
                text(
                    Bokmal to "Si Vd. hubiera remitido el certificado de fe de vida pocos días después del plazo original, puede descartar este recordatorio.",
                )
            }
            //[PE_IY_03_178_tekst]
        }
    }
}