package Dao

import Core.Models.WebDocument
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}


/**
  * Created by OUASMINE Mohammed Amine on 31/08/2017.
  */
object MongoFactory {
  private val SERVER = "192.168.0.104"
  private val DATABASE = "Scrapper"
  private val COLLECTION = "Urls"
  val mongoClient: MongoClient = MongoClient("mongodb://" + SERVER)
  val database: MongoDatabase = mongoClient.getDatabase(DATABASE)
  val collection: MongoCollection[WebDocument] = database.getCollection(COLLECTION);
}
