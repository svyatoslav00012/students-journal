package ais.journal.controllers.impl.generic

import ais.journal.Config
import ais.journal.exceptions.Exceptions._
import cats.effect.IO
import org.http4s.{Response, Status}
import org.http4s.dsl.Http4sDsl

class AbstractController {

  val dsl: Http4sDsl[IO] = Http4sDsl[IO]
  import dsl._

  object AuthTokenParam extends QueryParamDecoderMatcher[String]("authToken")


  val UnauthorizedResponse: IO[Response[IO]] = IO.pure(Response[IO](status = Status.Unauthorized))

  def authAndHandleError(token: String)(io: IO[Response[IO]]): IO[Response[IO]] =
    if(token == Config.AuthToken)
      handleError(io)
    else UnauthorizedResponse

  def handleError(io: IO[Response[IO]]): IO[Response[IO]] = io.handleErrorWith{
    case WrongJsonException(msg) => BadRequest(msg)
    case NotFoundException(msg) => NotFound(msg)
    case x: LabAlreadyExistsException => Conflict(x.getMessage)
    case e: Exception => InternalServerError(e.getMessage)
  }
}
