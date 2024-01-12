package cz.mendelu.pef.xchomo.travelsnap.model

import java.io.Serializable

open class UIState<T, E>
    (
    var loading: Boolean = true,
    var data: T? = null,
    var errors: E? = null) : Serializable {
}