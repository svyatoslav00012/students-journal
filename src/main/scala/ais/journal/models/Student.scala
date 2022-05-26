package ais.journal.models

import ais.journal.models.dto.LabDTO
import reactivemongo.api.bson.Macros.Annotations.Key
import reactivemongo.api.bson.{BSONDocumentHandler, Macros}

import java.time.Instant

case class Student(
                    @Key("_id") id: String,
                    firstName: String,
                    lastName: String,
                    group: String,
                    labs: List[Lab],
                    createdAt: Instant
                  ) extends HasId{

  def updateLab(labId: String, labDTO: LabDTO): Student =
    copy(labs = labs.map{
      case l if l.id != labId => l
      case l => l.update(labDTO)
    })

  def removeLab(labId: String): Student = copy(labs = labs.filterNot(_.id == labId))

}

object Student {
  implicit val handler: BSONDocumentHandler[Student] = Macros.handler[Student]
}


