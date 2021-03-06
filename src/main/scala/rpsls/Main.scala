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

import io.circe.generic.auto._
import io.circe.syntax._

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import rpsls.model._
import rpsls.model.ApiError._
import cats.effect.IO

object Main extends App with RouterDerivationModule {

  val db = Database.forConfig("postgresDB")

  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit def apiErrorToResponse = new ToHttpResponse[ApiError] {
    def response(error: ApiError) = error match {
      case GameNotFound => HttpResponse(StatusCodes.NotFound)
      case ParsingError => HttpResponse(StatusCodes.InternalServerError)
      case GenericError => HttpResponse(StatusCodes.InternalServerError)
    }
  }

  val repo = new GameRepoImpl[IO](db)
  val service = new GameServiceImpl[IO](repo)
  val controller = new GameControllerImpl(service)
  val routes = deriveRouter[GameController](controller)

  val server = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(routes)
  )

}
