package com.github.aldtid.testing.frameworks.comparison

trait Recorder[F[_]] {

  def record(number: Int, sequence: List[Int]): F[Unit] =
    record(Recorder.Entry(number, sequence))

  def record(entry: Recorder.Entry): F[Unit]

}

object Recorder {

  final case class Entry(number: Int, sequence: List[Int])

}
