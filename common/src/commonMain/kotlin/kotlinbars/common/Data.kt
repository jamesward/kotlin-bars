package kotlinbars.common

import kotlinx.serialization.Serializable

expect annotation class Id()

@Serializable
data class Bar(@Id val id: Long?, val name: String)