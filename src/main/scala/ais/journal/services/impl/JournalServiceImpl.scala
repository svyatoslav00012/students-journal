package ais.journal.services.impl

import ais.journal.dao.StudentDAO
import ais.journal.exceptions.Exceptions.NotFoundException
import ais.journal.models.dto.{JournalDTO, StudentDTO}
import ais.journal.services.{JournalService, StatsCalculationService}
import cats.effect.IO

class JournalServiceImpl(studentDAO: StudentDAO,
                         statsCalculationService: StatsCalculationService) extends JournalService {
  override def journalStats(): IO[JournalDTO] = for {
    students <- studentDAO.getAll
    stats <- statsCalculationService.journalStats(students.toList)
  } yield stats

  override def studentStats(studentId: String): IO[StudentDTO] = for {
    student <- studentDAO.getById(studentId) map {
      case Some(s) => s
      case _ => throw NotFoundException(s"Student with id $studentId not found")
    }
    studentDTO <- statsCalculationService.studentStats(student)
  } yield studentDTO
}
