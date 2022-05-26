package ais.journal.services.impl

import ais.journal.models.Student
import ais.journal.models.dto.{JournalDTO, JournalStatsDTO, StudentDTO, SuccessRateDTO}
import ais.journal.services.StatsCalculationService
import cats.effect.IO
import cats.implicits.catsSyntaxParallelSequence1

class StatsCalculationServiceImpl extends StatsCalculationService {
  override def journalStats(students: List[Student]): IO[JournalDTO] = for {
    studentsDTOs <- students.map(studentStats).parSequence
    allLabs = students.flatMap(_.labs)
    successRateDTO = studentsDTOs.foldLeft(SuccessRateDTO.Zero){
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 0 => sRateDTO.copy(zero = sRateDTO.zero + 1)
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 1 => sRateDTO.copy(one = sRateDTO.one + 1)
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 2 => sRateDTO.copy(two = sRateDTO.two + 1)
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 3 => sRateDTO.copy(three = sRateDTO.three + 1)
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 4 => sRateDTO.copy(four = sRateDTO.four + 1)
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 5 => sRateDTO.copy(five = sRateDTO.five + 1)
      case (sRateDTO, curStudent) if curStudent.mark0_6 == 6 => sRateDTO.copy(over100 = sRateDTO.over100 + 1)
    }
    journalStatsDTO = JournalStatsDTO(
      totalStudents = students.size,
      avgLabsPerStudent = if(students.isEmpty) 0.0 else students.map(_.labs.size).sum.toDouble / students.size ,
      avgMarkPerStudent = if(students.isEmpty) 0.0 else studentsDTOs.map(_.totalMark).sum.toDouble / students.size,
      avgMarkPerLab = if(allLabs.isEmpty) 0.0 else allLabs.map(_.mark).sum.toDouble / allLabs.size,
      labsTotal = allLabs.size,
      labsToImprove = allLabs.count(_.possibleMark.isDefined),
      successRate = successRateDTO,
      studentsWithMinRequiredMark = studentsDTOs.count(_.mark0_6 >= 3),
    )
    journalDTO = JournalDTO(
      stats = journalStatsDTO,
      students = studentsDTOs
    )
  } yield journalDTO

  override def studentStats(student: Student): IO[StudentDTO] = IO {
    val totalMark = student.labs.map(_.mark).sum

    StudentDTO(
      student = student,
      totalMark = totalMark,
      labsToImprove = student.labs.count(_.possibleMark.isDefined),
      avgMarkPerLab = if(student.labs.nonEmpty) totalMark.toDouble / student.labs.size else 0.0,
      mark0_6 = totalMark match {
        case 0 => 0
        case x if x < 25 => 1
        case x if x < 51 => 2
        case x if x < 71 => 3
        case x if x < 88 => 4
        case x if x < 101 => 5
        case _ => 6
      }
    )
  }
}
