package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTOSelectors.avdoedNavn


data class OmstillingstoenadAvslagRedigerbartUtfallDTO(val avdoedNavn: String) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslagRedigerbartUtfall : EtterlatteTemplate<OmstillingstoenadAvslagRedigerbartUtfallDTO>,
    Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingstoenadAvslagRedigerbartUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - begrunnelse for avslag",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }

        outline {
            paragraph {
                textExpr(
                    Bokmal to "Din søknad om omstillingsstønad etter ".expr() + avdoedNavn + " er avslått.".expr(),
                    Nynorsk to "Søknaden din om omstillingsstønad etter ".expr() + avdoedNavn + " er avslått.".expr(),
                    English to "Your application for adjustment allowance for the deceased ".expr() + avdoedNavn + " has been rejected.".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "(Utfall jamfør tekstbibliotek)",
                    Nynorsk to "(Utfall jamfør tekstbibliotek)",
                    English to "(Utfall jamfør tekstbibliotek)",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § <riktig paragrafhenvisning>.",
                    Nynorsk to "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova § <riktig paragrafhenvisning>.",
                    English to "This decision has been made pursuant to the provisions regarding adjustment allowance in the National Insurance Act – sections § <riktig paragrafhenvisning>.",
                )
            }
        }
    }
}