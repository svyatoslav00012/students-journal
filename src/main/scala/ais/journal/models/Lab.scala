package ais.journal.models

import ais.journal.models.dto.LabDTO
import reactivemongo.api.bson.{BSONDocumentHandler, Macros}

import java.time.Instant

case class Lab(
                id: String,
                title: String,
                mark: Int,
                possibleMark: Option[Int],
                possibleMarkComment: Option[String],
                comment: Option[String],
                date: Instant
              ) extends HasId {
  def update(labDTO: LabDTO): Lab = copy(
    title = labDTO.title.getOrElse(title),
    mark = labDTO.mark.getOrElse(mark),
    possibleMark = labDTO.possibleMark.orElse(possibleMark),
    possibleMarkComment = labDTO.possibleMarkComment.orElse(possibleMarkComment),
    comment = labDTO.comment.orElse(comment),
    date = labDTO.date.map(Instant.ofEpochMilli).getOrElse(date)
  )
}

object Lab {
  implicit val handler: BSONDocumentHandler[Lab] = Macros.handler[Lab]
}
