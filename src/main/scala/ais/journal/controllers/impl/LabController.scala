package ais.journal.controllers.impl

import ais.journal.controllers.Controller
import ais.journal.controllers.impl.generic.AbstractController
import ais.journal.models.dto.LabDTO
import ais.journal.services.LabService
import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityDecoder._

class LabController(labService: LabService) extends AbstractController with Controller {

  import dsl._

  override def routes: HttpRoutes[IO] = HttpRoutes.of[IO]{

    case req@POST -> Root / studentId :? AuthTokenParam(authToken) => authAndHandleError(authToken) {
      for {
        labDTO <- req.as[LabDTO]
        _ <- labService.create(studentId, labDTO)
        res <- Ok()
      } yield res
    }

    case req@PUT -> Root / studentId / labId :? AuthTokenParam(authToken) => authAndHandleError(authToken) {
      for {
        labDTO <- req.as[LabDTO]
        _ <- labService.update(studentId, labId, labDTO)
        res <- Ok()
      } yield res
    }

    case DELETE -> Root / studentId / labId :? AuthTokenParam(authToken) => authAndHandleError(authToken) {
      for {
        _ <- labService.delete(studentId, labId)
        res <- Ok()
      } yield res
    }
  }

}
