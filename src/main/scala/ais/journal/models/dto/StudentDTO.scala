package ais.journal.models.dto

import ais.journal.models.Student

case class StudentDTO(
                       student: Student,
                       totalMark: Int,
                       labsToImprove: Int,
                       avgMarkPerLab: Double,
                       mark0_6: Int,
                     )
