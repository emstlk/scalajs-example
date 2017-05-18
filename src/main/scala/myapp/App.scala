package myapp

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalatags.JsDom.all._

@JSExportTopLevel("App")
object App {

  @JSExport
  def main(): Unit = {
    var repos = Seq.empty[String]

    val myInput = input.render
    val output = ul.render

    val go = a(href := "#", "go").render
    go.onclick = { (e: dom.Event) =>
      val user = myInput.value
      Ajax.get(s"https://api.github.com/users/$user/repos", responseType = "json")
        .foreach { resp =>
          val json = resp.response.asInstanceOf[js.Array[js.Dynamic]]
          repos = json.map { repo =>
            s"${repo.name} - ${repo.stargazers_count}"
          }
          output.innerHTML = ""
          repos.foreach { r =>
            output.appendChild(
              li(r).render
            )
          }
        }
    }

    myInput.onkeyup = { (e: dom.Event) =>
      output.innerHTML = ""
      repos.filter(_.startsWith(myInput.value)).foreach { e =>
        val (h, t) = e.splitAt(myInput.value.length)
        output.appendChild(
          li(
            span(backgroundColor := "yellow", h),
            t
          ).render
        )
      }
    }

    dom.document.body.innerHTML = ""
    dom.document.body.appendChild(
      div(
        h1("My github browser!"),
        p("ScalaJs is awesome"),
        myInput,
        go,
        output
      ).render
    )
  }

}
