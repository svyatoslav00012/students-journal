package ais.journal.services

import ais.journal.models.Student
import ais.journal.models.dto.StudentPersonalInfoDTO
import cats.effect.std.UUIDGen
import cats.effect.{Clock, IO}

trait StudentService {
  def getById(id: String): IO[Student]

  def create(studentDTO: StudentPersonalInfoDTO)
            (implicit uuidG: UUIDGen[IO], clock: Clock[IO]): IO[Student]

  def updatePersonalInfo(id: String, studentDTO: StudentPersonalInfoDTO): IO[Unit]

  def delete(id: String): IO[Unit]
}
