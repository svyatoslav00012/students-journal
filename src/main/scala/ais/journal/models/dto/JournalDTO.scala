package ais.journal.models.dto

case class SuccessRateDTO(
                           zero: Int,
                           one: Int,
                           two: Int,
                           three: Int,
                           four: Int,
                           five: Int,
                           over100: Int
                         )

object SuccessRateDTO {
  val Zero: SuccessRateDTO = SuccessRateDTO(0, 0, 0, 0, 0, 0, 0)
}

case class JournalStatsDTO(
                            totalStudents: Int,
                            avgLabsPerStudent: Double,
                            avgMarkPerStudent: Double,
                            avgMarkPerLab: Double,
                            labsTotal: Int,
                            labsToImprove: Int,
                            successRate: SuccessRateDTO,
                            studentsWithMinRequiredMark: Int
                          )

case class JournalDTO(
                       stats: JournalStatsDTO,
                       students: List[StudentDTO],
                     )
