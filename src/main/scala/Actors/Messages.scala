package Actors

import Core.Models.WebDocument

/**
  * Created by OUASMINE Mohammed Amine on 31/08/2017.
  */
object Messages {
  case class crawl(url: String, limit: Int)
  case class response(document: WebDocument)
}
