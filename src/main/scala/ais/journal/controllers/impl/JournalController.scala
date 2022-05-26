package ais.journal.controllers.impl

import ais.journal.controllers.Controller
import ais.journal.controllers.impl.generic.AbstractController
import ais.journal.models.dto.StudentPersonalInfoDTO
import ais.journal.services.{JournalService, StudentService}
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.circe.CirceEntityDecoder._

class JournalController(journalService: JournalService) extends AbstractController with Controller {

  import dsl._

  override def routes: HttpRoutes[IO] = HttpRoutes.of[IO]{

    case GET-> Root => handleError {
      journalService.journalStats().flatMap(Ok(_))
    }

    case GET-> Root / studentId => handleError {
      journalService.studentStats(studentId).flatMap(Ok(_))
    }

  }


}
