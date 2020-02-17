package rpsls

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import akka.http.scaladsl.model.{ HttpResponse, StatusCodes, ContentType, HttpEntity}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.buildo.enumero.circe._

import rpsls.Request
import rpsls.Response

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // actually never used
  implicit def throwableResponse: ToHttpResponse[Throwable] = null

  val routes = deriveRouter[GameApi](new GameApiImpl)

  val server = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(routes)
  )

}
