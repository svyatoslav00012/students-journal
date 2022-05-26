package ais.journal.dao.reactiveMongo.generic

import ais.journal.Config
import ais.journal.dao.generic.DAO
import ais.journal.exceptions.Exceptions.{MongoCodeException, MongoException}
import ais.journal.models.HasId
import cats.effect.IO
import cats.implicits.catsSyntaxParallelSequence1
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.bson.collection.BSONSerializationPack._
import reactivemongo.api.{AsyncDriver, DB, MongoConnection}

import scala.concurrent.{ExecutionContext, Future}

abstract class ReactiveMongoDAOImpl[M <: HasId : Reader : Writer](dbName: String,
                                                                 collectionName: String,
                                                                 connectionF: Future[MongoConnection] = ReactiveMongoDAOImpl.mongoConnection)(implicit ex: ExecutionContext)
  extends DAO[M] {

  def db: Future[DB] = connectionF.flatMap(_.database(dbName))

  def collectionF: Future[BSONCollection] = db.map(_.collection[BSONCollection](collectionName))

  override def getById(id: String): IO[Option[M]] = IO.fromFuture(IO {
    collectionF.flatMap(
      _.find(BSONDocument("_id" -> id)).one[M]
    )
  })

  override def getAll: IO[Seq[M]] = getMany()

  override def create(model: M): IO[String] = IO.fromFuture(IO {
    collectionF.flatMap(_.insert.one(model).map(_.code))
  }).flatMap {
    case None => IO.pure(model.id)
    case Some(errCode) => IO.raiseError(MongoCodeException(errCode))
  }

  override def createMany(models: List[M]): IO[List[String]] = IO.fromFuture(IO{
    collectionF.flatMap(_.insert.many(models).map(_.errmsg))
  }).flatMap {
    case None => IO.pure(models.map(_.id))
    case Some(errMsg) =>
      println(errMsg)
      IO.raiseError(MongoException(errMsg))
  }


  override def update(model: M): IO[Boolean] = update(model, upsert = true)

  override def updateMany(models: List[M]): IO[Unit] = models.map(update).parSequence.void

  override def delete(id: String): IO[Boolean] = IO.fromFuture(IO {
    collectionF.flatMap {
      _.findAndRemove(
        selector = BSONDocument("_id" -> id),
      ).map(_ => true)
    }
  })

  override def deleteMany(ids: Seq[String]): IO[Boolean] = IO.fromFuture(IO {
    collectionF.flatMap {
      _.findAndRemove(
        selector = BSONDocument("_id" -> BSONDocument("$in" -> ids))
      ).map(_ => true)
    }
  })

  override def deleteMany(selector: BSONDocument): IO[Boolean] = IO.fromFuture(IO {
    collectionF.flatMap {c =>
      val deleteBuilder = c.delete(ordered = false)

      Future.sequence(Seq(
        deleteBuilder.element(selector)
      ))
        .flatMap(deleteBuilder.many)
        .map(_ => true)
    }
  })



  def getMany(filter: ElementProducer*): IO[Seq[M]] = getManyFilter(BSONDocument(filter: _*))

  def getManyFilter(filter: BSONDocument): IO[Seq[M]] = IO.fromFuture(IO {
    collectionF.flatMap(
      _.find(filter)
        .cursor[M]()
        .collect[Seq]()
    )
  })

  def update(model: M, upsert: Boolean = false): IO[Boolean] = IO.fromFuture(IO {
    collectionF.flatMap(
      _.update(ordered = false)
        .one(BSONDocument("_id" -> model.id), model, upsert, multi = false)
        .map(_.nModified == 1)
    )
  })

  override def updateSet(id: String, updateBody: ElementProducer*): IO[Boolean] =
    updateBSON(id, BSONDocument("$set" -> BSONDocument(updateBody: _*)))

  override def updateBSON(id: String, modifier: ElementProducer*): IO[Boolean] = IO.fromFuture(IO {
    val updateModifier = BSONDocument(modifier: _*)

    collectionF.flatMap {
      _.findAndUpdate(
        selector = BSONDocument("_id" -> id),
        update = updateModifier,
      ).map(_.result.isDefined)
    }
  })
}

object ReactiveMongoDAOImpl {
  lazy val mongoConnection: Future[MongoConnection] = getFutureConnection(Config.MongoUri)(ExecutionContext.global)

  def getFutureConnection(mongoUri: String)(implicit ex: ExecutionContext): Future[MongoConnection] = for {
    uri <- MongoConnection.fromString(mongoUri)
    driver: AsyncDriver = AsyncDriver()
    connection <- driver.connect(uri)
  } yield connection
}
