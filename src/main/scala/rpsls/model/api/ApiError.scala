package rpsls.model

import wiro.server.akkaHttp.ToHttpResponse
import wiro.server.akkaHttp.FailSupport._
import akka.http.scaladsl.model.{
  HttpResponse,
  StatusCodes,
  ContentType,
  HttpEntity
}
import akka.http.scaladsl.model.MediaTypes
import io.circe.generic.auto._
import io.circe.syntax._

trait ApiError extends Throwable {}
trait Error404 extends ApiError {}
trait Error500 extends ApiError {}

case class GameNotFound() extends Error404 { val msg = "Game not found" }
case class ParsingError() extends Error500 {
  val msg = "Unable to parse result"
}
case class GenericError() extends Error500 { val msg = "Unknown error" }
