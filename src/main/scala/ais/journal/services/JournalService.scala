package ais.journal.services

import ais.journal.models.dto.{JournalDTO, StudentDTO}
import cats.effect.IO

trait JournalService {
  def journalStats(): IO[JournalDTO]
  def studentStats(studentId: String): IO[StudentDTO]
}
