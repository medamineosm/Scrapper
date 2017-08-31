package Actors
import java.net.{MalformedURLException, URL}

import Actors.Messages.{crawl, response}
import Core.Models.{Link, WebDocument}
import Dao.MongoFactory
import akka.actor.{Actor, ActorLogging}
import org.jsoup.Jsoup

import scala.collection.JavaConversions._
import scala.text.Document
import scala.util.control.Exception.catching
/**
  * Created by Ouasmine on 31/08/2017.
  */
class ScrapperActor extends Actor with ActorLogging {

  override def receive = {
    case msg: String =>
      log.info("ScrapperActor received message " + msg)
      sender ! "Hello from the ScrapperActor"
    case crawl(url, limit) =>
      log.info("Scrapping the url ["+ url +"] with limit ("+ limit +")")
      var document = Crawler.crawl(url)
      MongoFactory.collection.insertOne(document)
      log.info("Number of Document " + MongoFactory.collection.count().toString)
      //sender ! response(document)
  }


  object Crawler {

    type JDoc = org.jsoup.nodes.Document

    def get(url: String): JDoc = Jsoup.connect(url).get()

    def titleText(doc: JDoc): String = doc.select("title").text

    def bodyText(doc: JDoc): String = doc.select("body").text

    /**
      * Allows for extraction without null pointer exceptions
      *
      */
    def safeMetaExtract(doc: JDoc, meta: String): String = {
      val result = doc.select("meta[name=" ++ meta ++ "]").first
      Option(result) match {
        case Some(v) => v.attr("content")
        case None => ""
      }
    }

    def metaKeywords(doc: JDoc): String = safeMetaExtract(doc, "keywords")

    def metaDescription(doc: JDoc): String = safeMetaExtract(doc, "description")

    /**
      * Extracts links from a document
      *
      */
    def linkSequence(doc: JDoc): Seq[Link] = {
      val links = doc.select("a[href]").iterator.toList
      links.map { l => Link(l.text, l.attr("href")) }
    }

    def extract(doc: JDoc): WebDocument = {
      val title: String = titleText(doc)
      val body: String = bodyText(doc)
      val links: Seq[Link] = linkSequence(doc)
      val desc: String = metaDescription(doc)

      WebDocument(title, body, links, desc)
    }

    def safeURL(url: String): Option[String] = {
      val result = catching(classOf[MalformedURLException]) opt new URL(url)
      result match {
        case Some(v) => Some(v.toString)
        case None => None
      }
    }

    /**
      * Crawl a URL and return a WebDocument
      *
      */
    def crawl(url: String): WebDocument = {
      val f = extract _ compose get
      f(url)
    }
  }
}
