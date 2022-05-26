package ais.journal.dao.reactiveMongo

import ais.journal.Config
import ais.journal.Main.executionContext
import ais.journal.dao.StudentDAO
import ais.journal.dao.reactiveMongo.generic.ReactiveMongoDAOImpl
import ais.journal.models.{Lab, Student}
import ais.journal.models.dto.StudentPersonalInfoDTO
import cats.effect.IO
import reactivemongo.api.bson.BSONDocument

class StudentDAOImpl extends ReactiveMongoDAOImpl[Student](Config.MongoDbName, "student")
  with StudentDAO {

  override def updatePersonalInfo(id: String, studentDTO: StudentPersonalInfoDTO): IO[Boolean] =
    updateSet(id,
      "firstName" -> studentDTO.firstName,
      "lastName" -> studentDTO.lastName,
      "group" -> studentDTO.group
    )

  override def addLab(studentId: String, lab: Lab): IO[Unit] = updateBSON(
    studentId,
    "$push" -> BSONDocument("labs" -> lab)
  ).void
}
