package kotlinbars

import org.springframework.data.annotation.Id

data class Bar(@Id val id: Long?, val name: String)