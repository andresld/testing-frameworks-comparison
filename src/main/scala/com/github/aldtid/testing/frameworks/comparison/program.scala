package com.github.aldtid.testing.frameworks.comparison

import cats.effect.kernel.Sync
import cats.syntax.flatMap._
import cats.implicits.showInterpolator

import scala.annotation.tailrec

object program {

  def isPrime(number: Int): Boolean = {

    @tailrec
    def run(candidates: List[Int]): Boolean = candidates match {
      case Nil => true
      case head :: tail    => if (number % head == 0) false else run(tail)
    }

    if (number < 2) false else run((2 until number).toList)

  }

  def isPrime[F[_] : Sync](number: Int, logger: Logger[F]): F[Boolean] =
    Sync[F]
      .delay(isPrime(number))
      .flatTap(result => logger.log(show"is number $number prime: $result"))

  def fibonacciSequence(number: Int): List[Int] = {

    val numberZeroSequence: List[Int] = List(0)
    val numberOneSequence: List[Int] = numberZeroSequence :+ 1

    @tailrec
    def run(left: Int, acc: List[Int]): List[Int] =
      if (left == 0) acc else run(left - 1, acc :+ acc.takeRight(2).sum)

    if (number < 0) Nil
    else if (number == 0) numberZeroSequence
    else run(number - 1, numberOneSequence)

  }

  def fibonacciSequence[F[_] : Sync](number: Int, logger: Logger[F]): F[List[Int]] =
    Sync[F]
      .delay(fibonacciSequence(number))
      .flatTap(sequence => logger.log(show"fibonacci sequence up to number $number is: ${sequence.mkString(" | ")}"))

  def run[F[_] : Sync](number: Int, logger: Logger[F], recorder: Recorder[F]): F[List[Int]] =
    isPrime(number, logger).flatMap(result =>

      if (result) fibonacciSequence(number, logger).flatTap(recorder.record(number, _))
      else Sync[F].pure(Nil)

    )

}
