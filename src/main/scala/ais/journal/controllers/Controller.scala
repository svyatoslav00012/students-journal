package ais.journal.controllers

import ais.journal.controllers.impl.StudentController
import cats.data.Kleisli
import cats.effect.IO
import org.http4s.{HttpRoutes, Request, Response}

trait Controller {
  def routes: HttpRoutes[IO]
}
