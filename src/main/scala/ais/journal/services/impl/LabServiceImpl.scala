package ais.journal.services.impl

import ais.journal.dao.StudentDAO
import ais.journal.exceptions.Exceptions.{LabAlreadyExistsException, NotFoundException, WrongJsonException}
import ais.journal.models.{Lab, Student}
import ais.journal.models.dto.{LabDTO, StudentPersonalInfoDTO}
import ais.journal.services.{LabService, StudentService}
import cats.effect.std.UUIDGen
import cats.effect.{Clock, IO}

import java.time.Instant

class LabServiceImpl(studentDAO: StudentDAO) extends LabService {
  override def create(studentId: String, labDTO: LabDTO)(implicit uuidG: UUIDGen[IO], clock: Clock[IO]): IO[Lab] =
    for {
      id <- uuidG.randomUUID.map(_.toString)
      now <- clock.realTimeInstant
      lab <- (labDTO.title, labDTO.mark) match {
        case (Some(title), Some(mark)) => IO.pure(Lab(
          id = id,
          title = title,
          mark = mark,
          possibleMark = labDTO.possibleMark,
          possibleMarkComment = labDTO.possibleMarkComment,
          comment = labDTO.comment,
          date = labDTO.date.map(Instant.ofEpochMilli).getOrElse(now)
        ))
        case _ => IO.raiseError(WrongJsonException("title and mark are required"))
      }
      _ <- getStudent(studentId).flatMap {
        case student if student.labs.exists(_.title == lab.title) => IO.raiseError(LabAlreadyExistsException(lab.title))
        case student => IO.pure(student)
      }
      _ <- studentDAO.addLab(studentId, lab)
    } yield lab


  override def update(studentId: String, labId: String, labDTO: LabDTO): IO[Unit] = for {
      student <- getStudent(studentId)
      _ <- student.labs.find(_.id == labId) match {
        case None => IO.raiseError(NotFoundException(s"Lab with id $labId not found"))
        case Some(lab) => IO.pure(lab)
      }
      _ <- studentDAO.update(student.updateLab(labId, labDTO))
    } yield ()


  override def delete(studentId: String, labId: String): IO[Unit] = for {
    student <- getStudent(studentId)
    _ <- if (student.labs.exists(_.id == labId)) {
      studentDAO.update(student.removeLab(labId))
    } else {
      IO.raiseError(NotFoundException(s"Lab with id $labId not found"))
    }
  } yield ()


  def getStudent(studentId: String): IO[Student] = studentDAO.getById(studentId).map {
    case None => throw NotFoundException(s"Student with id $studentId not found")
    case Some(student) => student
  }
}
