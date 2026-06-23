package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSLImpl
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.also

class SaksbehandlerValgBuilder<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> internal constructor(val id: String, val displayText: String, val scope: TemplateRootScope<*, LetterData>) {
    init {
        require(scope.saksbehandlervalg.containsKey(id).not()) { "Saksbehandlervalg med id $id allerede definert" }
    }
    fun bool(default: Boolean = false): Expression<Boolean> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .bool()
        .also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Bool(default, displayText)) }

    fun int(default: Int? = null): Expression<Int?> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .int()
        .also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Integer(default, displayText)) }

    fun text(default: String?): Expression<String?> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .text()
        .also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Text(default, displayText)) }

    inline fun <reified T> enum(default: T? = null): Expression<T?> where T : SaksbehandlerValgEnum, T : Enum<T> = Expression.UnaryInvoke(scope.argument, UnaryOperation.Select(selector(id)))
        .enum<T?>()
        .also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Enum(default, displayText, T::class.java as Class<out Enum<*>?>)) }
}

fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(id: String, displayText: String) = SaksbehandlerValgBuilder(id, displayText, this)

@BrevbakerDSLInternal
fun <T : SaksbehandlervalgVerdi, D : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> selector(id: String) = SaksbehandlervalgSelector<D, T>(
    propertyName = id,
    propertyType = SaksbehandlervalgVerdi::class.qualifiedName!!,
    selector = { (saksbehandlerValg as SaksbehandlervalgIDSLImpl).get(id) } // TODO denne er sårbar
)
internal fun Expression<SaksbehandlervalgVerdi>.bool(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Boolean> =
    invoke("bool", "kotlin.Boolean")

@PublishedApi
@BrevbakerDSLInternal
internal fun <T> Expression<SaksbehandlervalgVerdi>.invoke(propertyName: String, propertyType: String): Expression.UnaryInvoke<SaksbehandlervalgVerdi, T> = Expression.UnaryInvoke(this, UnaryOperation.Select(object : TemplateModelSelector<SaksbehandlervalgVerdi, T> {
    override val className = SaksbehandlervalgVerdi::class.qualifiedName!!
    override val propertyName = propertyName
    override val propertyType = propertyType
    override val selector: SaksbehandlervalgVerdi.() -> T = { this.unwrap() as T }
}))

internal fun Expression<SaksbehandlervalgVerdi>.int(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, Int?> = invoke("int", "Int")

internal fun Expression<SaksbehandlervalgVerdi>.text(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, String?> = invoke("string", "kotlin.String")

@BrevbakerDSLInternal
inline fun <reified T : SaksbehandlerValgEnum?> Expression<SaksbehandlervalgVerdi>.enum(): Expression.UnaryInvoke<SaksbehandlervalgVerdi, T?> = invoke(T::class.simpleName!!, T::class.qualifiedName!!)