package Akka
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem,Props}

object ActorPingPong extends App {
  object Ping {
    case class CreateChild(name: String)
  }

  class Ping extends Actor with ActorLogging {

    import Ping._

    override def receive: Receive = {
      case CreateChild(name) =>
        println(s"${self.path} creating child")
        val childRef = context.actorOf(Props[Pong], name)
        childRef ! "Ping"
      case "Pong" => log.info("Ping")
    }
  }

  class Pong extends Actor with ActorLogging {
    override def receive: Receive = {
      case "Ping" => log.info("Pong")
        sender ! "Pong"
    }
  }

  import Ping._

  val system = ActorSystem("ParentChild")
  val ping = system.actorOf(Props[Ping], "parent")
  ping ! CreateChild("child")
}
