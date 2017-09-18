package Actors
import Actors.Messages.{parse, scrap}
import Core.Models.Models.ScrapResponse
import akka.actor.{Actor, ActorLogging}
import org.jsoup.Jsoup

/**
  * Created by Ouasmine on 31/08/2017.
  */
class ScrapperActor extends Actor with ActorLogging {


  val remoteParser = context.actorSelection("akka.tcp://CrawlingSystem@127.0.0.1:5151/user/ParserActor")

  override def preStart(): Unit = log.info("ScrapperActor Starting ...")

  override def receive = {

    case scrap(url) =>
      log.info(s"ScrapperActor recieve msg :'$scrap'")
      log.info("Scrapping the url ["+ url +"]")
      remoteParser ! parse(Scrap(url))
  }

  def Scrap(url: String): ScrapResponse = {
    val response = Jsoup.connect(url)
        .cookie("store", "753")
        .ignoreContentType(true)
        .followRedirects(true)
        .timeout(3000)
        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1").execute()

    val contentType: String = response.contentType

    if (contentType.startsWith("text/html")) {
      log.debug("html content for ["+url+"] = " + response.parse.html.size)
      ScrapResponse(url, response.statusCode(), response.parse().html())
    } else {
      log.debug(s"Ignoring {${url} content type not equal text/html")
      ScrapResponse(url, response.statusCode(), "")
    }
  }
}
