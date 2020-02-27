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

case class GameNotFound(msg: String) extends ApiError {}
case class ParsingError(msg: String) extends ApiError {}
case class GenericError(msg: String) extends ApiError {}
