package ais.journal.dao.generic

import ais.journal.models.HasId
import cats.effect.IO
import reactivemongo.api.bson.{BSONDocument, ElementProducer}

trait DAO[M <: HasId] {
  def getById(id: String): IO[Option[M]]

  def getAll: IO[Seq[M]]

  def create(model: M): IO[String]

  def createMany(models: List[M]): IO[List[String]]

  def updateSet(id: String, updateBody: ElementProducer*): IO[Boolean]

  def updateBSON(id: String, modifier: ElementProducer*): IO[Boolean]

  def update(model: M): IO[Boolean]

  def updateMany(models: List[M]): IO[Unit]

  def delete(id: String): IO[Boolean]

  def deleteMany(ids: Seq[String]): IO[Boolean]

  def deleteMany(selector: BSONDocument): IO[Boolean]
}
