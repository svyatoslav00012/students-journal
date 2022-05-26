package ais.journal

import ais.journal.controllers.impl.{JournalController, LabController, StudentController}
import ais.journal.dao.StudentDAO
import ais.journal.dao.reactiveMongo.StudentDAOImpl
import ais.journal.services.{JournalService, LabService, StatsCalculationService, StudentService}
import ais.journal.services.impl.{JournalServiceImpl, LabServiceImpl, StatsCalculationServiceImpl, StudentServiceImpl}
import cats.effect.unsafe.IORuntime
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.toFunctorOps
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router

import scala.concurrent.ExecutionContext

object Main extends IOApp {

  implicit val IORuntime: IORuntime = cats.effect.unsafe.implicits.global
  implicit val executionContext: ExecutionContext = cats.effect.unsafe.implicits.global.compute


  val studentDAO: StudentDAO = new StudentDAOImpl

  val studentService: StudentService = new StudentServiceImpl(studentDAO)
  val labService: LabService = new LabServiceImpl(studentDAO)
  val statsCalculationService: StatsCalculationService = new StatsCalculationServiceImpl
  val journalService: JournalService = new JournalServiceImpl(studentDAO, statsCalculationService)

  val studentController = new StudentController(studentService)
  val labController = new LabController(labService)
  val journalController = new JournalController(journalService)

  override def run(args: List[String]): IO[ExitCode] = {

    val api = Router("/api/v1" -> Router(
      "/student" -> studentController.routes,
      "/lab" -> labController.routes,
      "/journal" -> journalController.routes
    )).orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(Config.Port)
      .withExecutionContext(executionContext)
      .withHttpApp(api)
      .resource.use(_ => IO.never)
      .as(ExitCode.Success)

  }
}
