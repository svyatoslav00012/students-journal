package ais.journal.dao

import ais.journal.dao.generic.DAO
import ais.journal.models.{Lab, Student}
import ais.journal.models.dto.StudentPersonalInfoDTO
import cats.effect.IO

trait StudentDAO extends DAO[Student] {

  def updatePersonalInfo(id: String, studentDTO: StudentPersonalInfoDTO): IO[Boolean]

  def addLab(studentId: String, lab: Lab): IO[Unit]
}
