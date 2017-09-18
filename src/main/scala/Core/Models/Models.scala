package Core.Models

import java.net.URL

/**
  * Created by OUASMINE Mohammed Amine on 31/08/2017.
  */
object Models {
  case class ScrapResponse(url: String, status: Int, html: String)
  sealed case class Link(title: String, href: String)
  case class WebDocument(url: URL,
                         title: String,
                         body: String,
                         links: Seq[Link],
                         metaDescription: String)
}

