package ais.journal.services

import ais.journal.models.Student
import ais.journal.models.dto.{JournalDTO, StudentDTO}
import cats.effect.IO

trait StatsCalculationService {
  def journalStats(students: List[Student]): IO[JournalDTO]
  def studentStats(student: Student): IO[StudentDTO]
}
