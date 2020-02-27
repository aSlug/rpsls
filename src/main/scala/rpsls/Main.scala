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

import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import rpsls.model._

object Main extends App with RouterDerivationModule {

  val db = Database.forConfig("postgresDB")

  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit def _throwableToResponse: ToHttpResponse[Throwable] = null

  implicit def _apiErrorToResponse = new ToHttpResponse[ApiError] {
    def response(error: ApiError) = HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = HttpEntity(
        ContentType(MediaTypes.`application/json`),
        ""
      )
    )
    def response(error: GameNotFound) = HttpResponse(
      status = StatusCodes.NotFound,
      entity = HttpEntity(
        ContentType(MediaTypes.`application/json`),
        error.asJson.noSpaces
      )
    )
    def response(error: ParsingError) = HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = HttpEntity(
        ContentType(MediaTypes.`application/json`),
        error.asJson.noSpaces
      )
    )
    def response(error: GenericError) = HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = HttpEntity(
        ContentType(MediaTypes.`application/json`),
        error.asJson.noSpaces
      )
    )
  }

  // implicit def _GameNotFoundToResponse = new ToHttpResponse[GameNotFound] {
  //   def response(error: GameNotFound) = HttpResponse(
  //     status = StatusCodes.NotFound,
  //     entity = HttpEntity(
  //       ContentType(MediaTypes.`application/json`),
  //       error.asJson.noSpaces
  //     )
  //   )
  // }

  // implicit def _ParsingErrorToResponse = new ToHttpResponse[ParsingError] {
  //   def response(error: ParsingError) = HttpResponse(
  //     status = StatusCodes.InternalServerError,
  //     entity = HttpEntity(
  //       ContentType(MediaTypes.`application/json`),
  //       error.asJson.noSpaces
  //     )
  //   )
  // }

  // implicit def _ToResponse = new ToHttpResponse[GenericError] {
  //   def response(error: GenericError) = HttpResponse(
  //     status = StatusCodes.InternalServerError,
  //     entity = HttpEntity(
  //       ContentType(MediaTypes.`application/json`),
  //       error.asJson.noSpaces
  //     )
  //   )
  // }

  val repo = new GameRepoImpl(db)
  val service = new GameServiceImpl(repo)
  val controller = new GameControllerImpl(service)
  val routes = deriveRouter[GameController](controller)

  val server = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(routes)
  )

}
