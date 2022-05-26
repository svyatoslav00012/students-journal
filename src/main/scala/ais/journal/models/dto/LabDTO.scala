package ais.journal.models.dto

import java.time.Instant

case class LabDTO(
                   title: Option[String],
                   mark: Option[Int],
                   possibleMark: Option[Int],
                   possibleMarkComment: Option[String],
                   comment: Option[String],
                   date: Option[Long]
                 )
