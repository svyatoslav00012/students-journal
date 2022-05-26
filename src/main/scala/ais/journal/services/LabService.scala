package ais.journal.services

import ais.journal.models.{Lab, Student}
import ais.journal.models.dto.{LabDTO, StudentPersonalInfoDTO}
import cats.effect.std.UUIDGen
import cats.effect.{Clock, IO}

trait LabService {
  def create(studentId: String, labDTO: LabDTO)
            (implicit uuidG: UUIDGen[IO], clock: Clock[IO]): IO[Lab]

  def update(studentId: String, labId: String, labDTO: LabDTO): IO[Unit]

  def delete(studentId: String, labId: String): IO[Unit]
}
