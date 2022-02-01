package com.github.aldtid.testing.frameworks.comparison

import com.github.aldtid.testing.frameworks.comparison.program._
import cats.{Applicative, Show}
import cats.data.WriterT
import cats.effect.{IO, Ref}
import cats.effect.unsafe.implicits.global
import cats.syntax.show._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ScalatestExample extends AnyFlatSpec with Matchers {

  type Context[F[_], A] = WriterT[F, List[String], A]

  def logger[F[_] : Applicative]: Logger[Context[F, *]] =
    new Logger[Context[F, *]] {

      def log[A: Show](message: A): Context[F, Unit] =
        WriterT.tell(List(message.show))

    }

  def recorder[F[_]](acc: Ref[F, Option[Recorder.Entry]]): Recorder[F] =
    entry => acc.set(Some(entry))

  "isPrime" should "be false for any number lower than 2" in {

    isPrime(-1) shouldBe false
    isPrime(0) shouldBe false

  }

  it should "be true for 2" in {

    isPrime(2) shouldBe true

  }

  it should "calculate if the number is prime or not if the number is greater or equal to 3" in {

    isPrime(3) shouldBe true
    isPrime(4) shouldBe false
    isPrime(13) shouldBe true
    isPrime(335) shouldBe false
    isPrime(457) shouldBe true

  }

  it should "perform the calculation of a prime number and log the expected message" in {

    isPrime(3, logger[IO]).run.unsafeRunSync() shouldBe (List("is number 3 prime: true"), true)

  }

  "fibonacciSequence" should "return an empty sequence if fibonacci number is negative" in {

    fibonacciSequence(-3) shouldBe Nil

  }

  it should "return the specific results for fibonacci numbers zero and one" in {

    fibonacciSequence(0) shouldBe List(0)
    fibonacciSequence(1) shouldBe List(0, 1)

  }

  it should "return the expected sequence for other fibonacci numbers" in {

    fibonacciSequence(3) shouldBe List(0, 1, 1, 2)
    fibonacciSequence(5) shouldBe List(0, 1, 1, 2, 3, 5)
    fibonacciSequence(10) shouldBe List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55)

  }

  it should "perform the calculation of a fibonacci sequence for a number and log the expected message" in {

    fibonacciSequence(3, logger[IO]).run.unsafeRunSync() shouldBe
      (List("fibonacci sequence up to number 3 is: 0 | 1 | 1 | 2"), List(0, 1, 1, 2))

  }

  "run" should "not calculate the fibonacci sequence for a number that is not prime" in {

    val expectedLogs: List[String] = List("is number 4 prime: false")
    val expectedSequence: List[Int] = Nil
    val expectedEntry: Option[Recorder.Entry] = None

    (for {

      recorderAcc     <- Ref.of[Context[IO, *], Option[Recorder.Entry]](None)
      sequence        <- program.run(4, logger[IO], recorder(recorderAcc))
      recorderEntries <- recorderAcc.get

    } yield (sequence, recorderEntries))
      .run
      .unsafeRunSync() shouldBe (expectedLogs, (expectedSequence, expectedEntry))

  }

  it should "calculate the fibonacci sequence for a number that is prime and record the expected entry" in {

    val expectedLogs: List[String] =
      List("is number 3 prime: true", "fibonacci sequence up to number 3 is: 0 | 1 | 1 | 2")

    val expectedSequence: List[Int] = List(0, 1, 1, 2)
    val expectedEntry: Option[Recorder.Entry] = Some(Recorder.Entry(3, expectedSequence))

    (for {

      recorderAcc     <- Ref.of[Context[IO, *], Option[Recorder.Entry]](None)
      sequence        <- program.run(3, logger[IO], recorder(recorderAcc))
      recorderEntries <- recorderAcc.get

    } yield (sequence, recorderEntries))
      .run
      .unsafeRunSync() shouldBe (expectedLogs, (expectedSequence, expectedEntry))

  }

}
