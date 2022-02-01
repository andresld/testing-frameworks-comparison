package com.github.aldtid.testing.frameworks.comparison

import cats.Show

trait Logger[F[_]] {

  def log[A: Show](message: A): F[Unit]

}
