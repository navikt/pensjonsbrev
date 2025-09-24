package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO

data class TilbakekrevingRedigerbartBrevDTO(val utenVariabler: Boolean = true) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object TilbakekrevingInnhold: EtterlatteTemplate<TilbakekrevingRedigerbartBrevDTO>, Delmal {
	override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_INNHOLD

	override val template = createTemplate(
		languages = languages(Bokmal, Nynorsk, English),
		letterMetadata = LetterMetadata(
			displayTitle = "Vedtak - Tilbakekreving",
			isSensitiv = true,
			distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
			brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
		),
	) {
		title {
			text(
				bokmal { +"" },
				nynorsk { +"" },
				english { +"" },
			)
		}
		outline {
			title2 {
				text(
					bokmal { +"Begrunnelse for vedtaket" },
					nynorsk { +"Grunngiving for vedtaket" },
					english { +"Grounds for the decision" },
				)
			}
			paragraph {
				text(
					bokmal { +"FRITEKSTFELT HENTET FRA MALER PÅ NAVET" },
					nynorsk { +"FRITEKSTFELT HENTET FRA MALER PÅ NAVET" },
					english { +"FRITEKSTFELT HENTET FRA MALER PÅ NAVET" },
				)
			}

		}
	}
}
