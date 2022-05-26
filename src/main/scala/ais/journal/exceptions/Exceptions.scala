package ais.journal.exceptions

object Exceptions {

  class AppException(message: String) extends RuntimeException(message)

  case class WrongJsonException(message: String) extends AppException(message)

  case class NotFoundException(message: String) extends AppException(message)

  case class LabAlreadyExistsException(title: String) extends AppException(s"Lab with title '$title' already exists")

  case class MongoCodeException(errCode: Int) extends AppException("Mongo exception. code: " + errCode)

  case class MongoException(message: String) extends AppException(message)

}
