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

class StatsCalculationServiceImplTest extends AnyWordSpec with MockFactory{

  import MockData._

  val statsCalculationServiceImpl = new StatsCalculationServiceImpl

  "StatsCalcServiceImpl" should {

    "journalStats" when {

      "return stats with one student" in {
        statsCalculationServiceImpl.journalStats(List(student1Mock)).unsafeRunSync() shouldBe journalWithStud1DTOMock
      }

      "return stats with all students" in {
        statsCalculationServiceImpl.journalStats(
          List(student1Mock, student2Mock, student3Mock)
        ).unsafeRunSync() shouldBe totalJournalDTOMock
      }

    }

    "studentStats" should {

      "return stats for first student" in {
        statsCalculationServiceImpl.studentStats(student1Mock).unsafeRunSync() shouldBe student1DTOMock
      }

      "return stats for second student" in {
        statsCalculationServiceImpl.studentStats(student2Mock).unsafeRunSync() shouldBe student2DTOMock
      }

      "return stats for third student" in {
        statsCalculationServiceImpl.studentStats(student3Mock).unsafeRunSync() shouldBe student3DTOMock
      }

    }
  }

}
