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

sealed trait ApiError
object ApiError {
  object GameNotFound extends ApiError { val msg = "Game not found" }
  object ParsingError extends ApiError { val msg = "Unable to parse result" }
  object GenericError extends ApiError { val msg = "Unknown error" }
}
