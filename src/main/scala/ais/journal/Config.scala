package ais.journal

object Config {

  def env(name: String): Option[String] = sys.env.get(name)
  def intEnv(name: String): Option[Int] = env(name).map(_.toInt)

  val Port: Int = intEnv("PORT").getOrElse(0)
  val AuthToken: String = env("AUTH_TOKEN").getOrElse("")

  val MongoUri: String = env("MONGO_URI").getOrElse("")
  val MongoDbName: String = env("MONGO_DB_NAME").getOrElse("")

}
