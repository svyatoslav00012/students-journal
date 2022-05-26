package ais.journal.services.impl

import ais.journal.models.{Lab, Student}
import ais.journal.models.dto.{JournalDTO, JournalStatsDTO, StudentDTO, SuccessRateDTO}

import java.time.Instant

object MockData {

  val student1Mock: Student = Student(
    id = "1",
    firstName = "Taras",
    lastName = "Petrenko",
    group = "KN-333",
    labs = List(
      Lab(
        id = "1",
        title = "lab1",
        mark = 12,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "2",
        title = "lab2",
        mark = 10,
        possibleMark = Some(15),
        possibleMarkComment = Some("Add tests"),
        comment = Some("some comment"),
        date = Instant.EPOCH
      )
    ),
    createdAt = Instant.EPOCH
  )

  val student2Mock: Student = Student(
    id = "2",
    firstName = "Ivan",
    lastName = "Ivanov",
    group = "KN-334",
    labs = List(
      Lab(
        id = "3",
        title = "lab1",
        mark = 30,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "4",
        title = "lab2",
        mark = 50,
        possibleMark = Some(60),
        possibleMarkComment = Some("Add tests"),
        comment = Some("some comment"),
        date = Instant.EPOCH
      ),
      Lab(
        id = "5",
        title = "lab3",
        mark = 40,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      )
    ),
    createdAt = Instant.EPOCH
  )

  val student3Mock: Student = Student(
    id = "3",
    firstName = "Alexiy",
    lastName = "Arestovich",
    group = "KN-333",
    labs = List(
      Lab(
        id = "6",
        title = "lab1",
        mark = 10,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "7",
        title = "lab2",
        mark = 11,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "8",
        title = "lab3",
        mark = 15,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "9",
        title = "lab4",
        mark = 14,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "10",
        title = "lab5",
        mark = 13,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),
      Lab(
        id = "11",
        title = "lab6",
        mark = 12,
        possibleMark = None,
        possibleMarkComment = None,
        comment = None,
        date = Instant.EPOCH
      ),

    ),
    createdAt = Instant.EPOCH
  )

  val student1DTOMock: StudentDTO = StudentDTO(
    student = student1Mock,
    totalMark = 22,
    labsToImprove = 1,
    avgMarkPerLab = 11,
    mark0_6 = 1
  )

  val student2DTOMock: StudentDTO = StudentDTO(
    student = student2Mock,
    totalMark = 120,
    labsToImprove = 1,
    avgMarkPerLab = 40.0,
    mark0_6 = 6
  )

  val student3DTOMock: StudentDTO = StudentDTO(
    student = student3Mock,
    totalMark = 75,
    labsToImprove = 0,
    avgMarkPerLab = 12.5,
    mark0_6 = 4
  )


  val journalWithStud1DTOMock: JournalDTO = JournalDTO(
    stats = JournalStatsDTO(
      totalStudents = 1,
      avgLabsPerStudent = 2,
      avgMarkPerStudent = 22,
      avgMarkPerLab = 11,
      labsTotal = 2,
      labsToImprove = 1,
      successRate = SuccessRateDTO(
        zero = 0,
        one = 1,
        two = 0,
        three = 0,
        four = 0,
        five = 0,
        over100 = 0
      ),
      studentsWithMinRequiredMark = 0
    ),
    students = List(student1DTOMock)
  )

  val totalJournalDTOMock: JournalDTO = JournalDTO(
    stats = JournalStatsDTO(
      totalStudents = 3,
      avgLabsPerStudent = 11.0 / 3,
      avgMarkPerStudent = 217.0 / 3,
      avgMarkPerLab = 217.0 / 11,
      labsTotal = 11,
      labsToImprove = 2,
      successRate = SuccessRateDTO(
        zero = 0,
        one = 1,
        two = 0,
        three = 0,
        four = 1,
        five = 0,
        over100 = 1
      ),
      studentsWithMinRequiredMark = 2
    ),
    students = List(student1DTOMock, student2DTOMock, student3DTOMock)
  )

}
