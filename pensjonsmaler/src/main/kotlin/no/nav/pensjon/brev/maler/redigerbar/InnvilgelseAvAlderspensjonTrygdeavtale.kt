package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erMellombehandling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erSluttbehandlingNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtaksresultatUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtaksresultatUtland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.ingenEndringIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.innvilgelseAPellerOektUttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.nyBeregningAvInnvilgetAP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.oekningIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.reduksjonIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.landNavnListe_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

// MF_000097 / AP_INNV_AVT_MAN
@TemplateModelHelpers
object InnvilgelseAvAlderspensjonTrygdeavtale : RedigerbarTemplate<InnvilgelseAvAlderspensjonTrygdeavtaleDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE_TRYGDEAVTALE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonTrygdeavtaleDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon (trygdeavtale)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad
        val erMellombehandling = pesysData.erMellombehandling
        val erSluttbehandlingNorgeUtland = pesysData.erSluttbehandlingNorgeUtland
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val antallLandVikarsprovd = pesysData.vedtaksresultatUtland_safe.ifNull(then = (0))
        //val landNavn = pesysData.vedtaksresultatUtland_safe.landNavnListe_safe.ifNull(then = "landNavn")
        val landNavn = pesysData.vedtaksresultatUtland_safe.landNavnListe_safe
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon


        title {
            showIf(erMellombehandling) {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                )
            }.orShowIf(
                saksbehandlerValg.nyBeregningAvInnvilgetAP and erSluttbehandlingNorgeUtland or (not(
                    erSluttbehandlingNorgeUtland
                ) and not(erMellombehandling))
            ) {
                textExpr(
                    Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + kravVirkDatoFom,
                    Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + kravVirkDatoFom,
                    English to "We have recalculated your retirement pension from ".expr() + kravVirkDatoFom
                )
            }.orShowIf(
                saksbehandlerValg.innvilgelseAPellerOektUttaksgrad and erSluttbehandlingNorgeUtland or (not(
                    erSluttbehandlingNorgeUtland
                ) and not(erMellombehandling))
            ) {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                )
            }
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {
            showIf(antallLandVikarsprovd.equalTo(1)) {
                // mottattInfoFraEttLand
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått opplysninger fra utenlandske trygdemyndigheter om opptjeningen din i ".expr() + landNavn + ".",
                        Nynorsk to "Vi har fått opplysningar frå utanlandske trygdeorgan om oppteninga di i ".expr() + landNavn + ".",
                        English to "We have received information from ".expr() + landNavn + " regarding your accumulated rights abroad",
                    )
                }
            }.orShowIf(antallLandVikarsprovd.greaterThan(1)) {
                // mottattInfoFraFlereLand
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått opplysninger fra utenlandske trygdemyndigheter om opptjeningen din i: ".expr() + landNavn + ".",
                        Nynorsk to "Vi har fått opplysningar frå utanlandske trygdeorgan om oppteninga di i: ".expr() + landNavn + ".",
                        English to "We have received information from foreign national insurance authorities regarding your accumulated rights in: ".expr() + landNavn + ".",
                    )
                }
            }

            showIf(erMellombehandling) {
                // nyBeregningAPInnledn
                paragraph {
                    textExpr(
                        Bokmal to "Dette gjør at du har rett til ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr()
                                + kravVirkDatoFom + ".",
                        Nynorsk to "Dette gjer at du har rett til ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr()
                                + kravVirkDatoFom + ".",
                        English to "This makes you eligible for ".expr() + uttaksgrad.format() + " percent retirement pension from ".expr()
                                + kravVirkDatoFom + ".",
                    )
                }
            }

            showIf(
                erSluttbehandlingNorgeUtland or
                        (not(erSluttbehandlingNorgeUtland) and erMellombehandling)
            ) {
                showIf(saksbehandlerValg.ingenEndringIPensjonen) {
                    // nyBeregningAPIngenEndring
                    paragraph {
                        text(
                            Bokmal to "Dette fører ikke til endringer i alderspensjonen din.",
                            Nynorsk to "Dette fører ikkje til endring av alderspensjonen din.",
                            English to "This does not result in any changes in your retirement pension.",
                        )
                    }
                }.orShowIf(saksbehandlerValg.innvilgelseAPellerOektUttaksgrad) {
                    // nyBeregningAPInnledn
                    paragraph {
                        textExpr(
                            Bokmal to "Dette gjør at du har rett til ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() + kravVirkDatoFom + ".",
                            Nynorsk to "Dette gjer at du har rett til ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() + kravVirkDatoFom + ".",
                            English to "This makes you eligible for ".expr() + uttaksgrad.format() + " percent retirement pension from ".expr() + kravVirkDatoFom + ".",
                        )
                    }
                }.orShowIf(saksbehandlerValg.oekningIPensjonen) {
                    // nyBeregningAPØkning
                    paragraph {
                        text(
                            Bokmal to "Dette fører til at pensjonen din øker.",
                            Nynorsk to "Dette fører til at pensjonen din aukar.",
                            English to "This leads to an increase in your retirement pension."
                        )
                    }
                }.orShowIf(saksbehandlerValg.reduksjonIPensjonen) {
                    // nyBeregningAPReduksjon
                    paragraph {
                        text(
                            Bokmal to "Dette fører til at pensjonen din blir redusert.",
                            Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                            English to "This leads to a reduction in your retirement pension."
                        )
                    }
                }
            }
            // innvilgelseAPogUTInnledn,innvilgelseAPInnledn,
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner hver måned før skatt fra ".expr()
                            + kravVirkDatoFom + "".expr()
                            + ifElse(
                        uforeKombinertMedAlder,
                        ifTrue = ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        ifFalse = " i alderspensjon fra folketrygden."
                    ),
                    Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner kvar månad før skatt frå ".expr()
                            + kravVirkDatoFom + "".expr()
                            + ifElse(
                        uforeKombinertMedAlder,
                        ifTrue = ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        ifFalse = " i alderspensjon frå folketrygda."
                    ),
                    English to "You will receive NOK ".expr() + totalPensjon.format() + " every month before tax from ".expr()
                            + kravVirkDatoFom + "".expr()
                            + ifElse(
                        uforeKombinertMedAlder,
                        ifTrue = ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit.",
                        ifFalse = " as retirement pension from the National Insurance Scheme."
                    )
                )
            }

            showIf()
            // innvilgelseAPogAFPPrivat
        }
    }
}