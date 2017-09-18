package Actors

import Core.Models.Models.ScrapResponse

/**
  * Created by OUASMINE Mohammed Amine on 31/08/2017.
  */
object Messages {
  case class scrap(url: String)
  case class parse(response: ScrapResponse)
}
