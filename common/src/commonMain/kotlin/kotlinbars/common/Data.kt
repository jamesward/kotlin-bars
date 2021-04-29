package kotlinbars.common

import kotlinx.serialization.Serializable

import org.springframework.data.annotation.Id

@Serializable
data class Bar(@Id val id: Long?, val name: String)