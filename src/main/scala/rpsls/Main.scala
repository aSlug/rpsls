package rpsls

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import akka.http.scaladsl.model.{
  HttpResponse,
  StatusCodes,
  ContentType,
  HttpEntity
}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.buildo.enumero.circe._
import rpsls.model.GameNotFound

import io.circe.generic.auto._
import io.circe.syntax._

import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await

object Main extends App with RouterDerivationModule {

  val db = Database.forConfig("h2mem1")

  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[Throwable] = null

  implicit def notFoundToResponse = new ToHttpResponse[GameNotFound] {
    def response(error: GameNotFound) = HttpResponse(
      status = StatusCodes.NotFound,
      entity = HttpEntity(
        ContentType(MediaTypes.`application/json`),
        error.asJson.noSpaces
      )
    )
  }

  val repo = new GameRepoImpl(db)
  val service = new GameServiceImpl(repo)
  val controller = new GameControllerImpl(service)
  val routes = deriveRouter[GameController](controller)

  val server = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(routes)
  )

}
