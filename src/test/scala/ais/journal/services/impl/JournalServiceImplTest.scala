package ais.journal.services.impl

import ais.journal.Main.IORuntime
import ais.journal.dao.StudentDAO
import ais.journal.exceptions.Exceptions.NotFoundException
import ais.journal.models.dto.{JournalDTO, JournalStatsDTO, StudentDTO, SuccessRateDTO}
import ais.journal.models.{Lab, Student}
import ais.journal.services.StatsCalculationService
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

import java.time.Instant

class JournalServiceImplTest extends AnyWordSpec with MockFactory{

  val studentDAO: StudentDAO = mock[StudentDAO]
  val statsCalculationService: StatsCalculationService = mock[StatsCalculationService]


  val journalServiceImpl = new JournalServiceImpl(studentDAO, statsCalculationService)

  import MockData._

  "JournalServiceImpl" should {

    "journalStats" when {

      "return stats" in {
        (studentDAO.getAll _).expects().returns(IO.pure(List(student1Mock)))
        (statsCalculationService.journalStats _).expects(List(student1Mock)).returns(IO.pure(journalWithStud1DTOMock))

        journalServiceImpl.journalStats().unsafeRunSync() shouldBe journalWithStud1DTOMock
      }

    }

    "studentStats" should {

      "return stats" in {
        (studentDAO.getById _).expects(student1Mock.id).returns(IO.pure(Some(student1Mock)))
        (statsCalculationService.studentStats _).expects(student1Mock).returns(IO.pure(student1DTOMock))

        journalServiceImpl.studentStats(student1Mock.id).unsafeRunSync() shouldBe student1DTOMock
      }

      "fail if student does not exist" in {
        (studentDAO.getById _).expects(student1Mock.id).returns(IO.pure(None))

        an[NotFoundException] shouldBe thrownBy (journalServiceImpl.studentStats(student1Mock.id).unsafeRunSync())
      }

    }
  }

}
