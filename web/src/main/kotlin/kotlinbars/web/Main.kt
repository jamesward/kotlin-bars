package kotlinbars.web

/*
import kotlinbars.common.Bar
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.dom.clear
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLUListElement
import org.w3c.fetch.RequestInit
import kotlin.js.json
import kotlinext.js.require

suspend fun refreshBars() {
    val bars = document.getElementById("bars") as HTMLUListElement
    bars.clear()

    window.fetch("/api/bars").await().json().await().unsafeCast<Array<Bar>>().forEach { bar ->
        bars.append {
            li {
                +bar.name
            }
        }
    }
}

suspend fun main() {

    require("bootstrap/dist/css/bootstrap.css")

    window.addEventListener("load", {

        GlobalScope.launch {
            val form = document.getElementById("form") as HTMLFormElement
            form.addEventListener("submit", { event ->
                GlobalScope.launch {
                    event.preventDefault()
                    val name = document.getElementById("name") as HTMLInputElement
                    val bar = Bar(null, name.value)
                    val req = RequestInit(
                        method = "POST",
                        headers = json("Content-type" to "application/json"),
                        body = JSON.stringify(bar),
                    )

                    window.fetch("/api/bars", req).await()
                    name.value = ""

                    refreshBars()
                }
            })

            refreshBars()
        }
    })

}
 */