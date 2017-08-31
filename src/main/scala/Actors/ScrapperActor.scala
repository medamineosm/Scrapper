package Actors

import akka.actor.{Actor, ActorLogging}

/**
  * Created by Ouasmine on 31/08/2017.
  */
class ScrapperActor extends Actor with ActorLogging {

  override def receive = {
    case msg: String =>
      log.info("ScrapperActor received message " + msg)
  }
}
