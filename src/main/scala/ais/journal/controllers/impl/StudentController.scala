package ais.journal.controllers.impl

import ais.journal.controllers.Controller
import ais.journal.controllers.impl.generic.AbstractController
import ais.journal.models.dto.StudentPersonalInfoDTO
import ais.journal.services.StudentService
import cats.effect.IO
import cats.implicits.toSemigroupKOps
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.http4s.HttpRoutes


class StudentController(studentService: StudentService) extends AbstractController with Controller {

  import dsl._

  override def routes: HttpRoutes[IO] = HttpRoutes.of[IO]{
//    case GET -> Root / id => authAndHandleError()
//      studentService
//      .getById(id)
//      .map(_.asJson)
//      .flatMap(Ok(_))

    case req@POST -> Root :? AuthTokenParam(authToken) => authAndHandleError(authToken) {
      for {
        student <- req.as[StudentPersonalInfoDTO]
        _ <- studentService.create(student)
        res <- Ok()
      } yield res
    }

    case req@PUT -> Root / id :? AuthTokenParam(authToken) => authAndHandleError(authToken) {
      for {
        student <- req.as[StudentPersonalInfoDTO]
        _ <- studentService.updatePersonalInfo(id, student)
        res <- Ok()
      } yield res
    }


    case DELETE -> Root / id :? AuthTokenParam(authToken) => authAndHandleError(authToken) {
      for {
        _ <- studentService.delete(id)
        res <- Ok()
      } yield res
    }

  }


}
