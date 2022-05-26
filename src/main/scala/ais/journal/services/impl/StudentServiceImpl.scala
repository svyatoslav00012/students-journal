package ais.journal.services.impl

import ais.journal.dao.StudentDAO
import ais.journal.exceptions.Exceptions.NotFoundException
import ais.journal.models.Student
import ais.journal.models.dto.StudentPersonalInfoDTO
import ais.journal.services.StudentService
import cats.effect.std.UUIDGen
import cats.effect.{Clock, IO}

class StudentServiceImpl(studentDAO: StudentDAO) extends StudentService {
  override def getById(id: String): IO[Student] = studentDAO.getById(id).map {
    case Some(student) => student
    case _ => throw NotFoundException(s"Student with id $id not found")
  }


  override def create(studentDTO: StudentPersonalInfoDTO)
                     (implicit uuidG: UUIDGen[IO], clock: Clock[IO]): IO[Student] = for {
    id <- uuidG.randomUUID.map(_.toString)
    now <- clock.realTimeInstant
    student = Student(
      id = id,
      firstName = studentDTO.firstName,
      lastName = studentDTO.lastName,
      group = studentDTO.group,
      labs = Nil,
      createdAt = now
    )
    _ <- studentDAO.create(student)
  } yield student


  override def updatePersonalInfo(id: String, studentDTO: StudentPersonalInfoDTO): IO[Unit] =
    studentDAO.updatePersonalInfo(id, studentDTO).flatMap {
      case true => IO.unit
      case false => IO.raiseError(NotFoundException(s"Student with id $id not found"))
    }


  override def delete(id: String): IO[Unit] = studentDAO.delete(id).map {
    case true => ()
    case false => throw NotFoundException(s"Student with id $id not found")
  }
}
