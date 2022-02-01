
import sbt._


object dependencies {

  lazy val version = new {
    val catsEffect: String = "3.3.4"
    val catsEffectTesting: String = "1.4.0"
    val minitest: String = "2.9.6"
    val munit: String = "0.7.29"
    val scalacheckEffect: String = "1.0.3"
    val scalatest: String = "3.2.11"
    val specs2: String = "4.13.2"
    val weaver: String = "0.7.9"
    val utest: String = "0.7.10"
  }

  lazy val cats = new {
    val effect: ModuleID = "org.typelevel" %% "cats-effect" % version.catsEffect
  }

  lazy val test = new  {
    val catsEffect: ModuleID = "org.typelevel" %% "cats-effect-testing-specs2" % version.catsEffectTesting % Test
    val minitest: ModuleID = "io.monix" %% "minitest" % version.minitest % Test
    val munit: ModuleID = "org.scalameta" %% "munit" % version.munit % Test
    val scalacheckEffect: ModuleID = "org.typelevel" %% "scalacheck-effect" % version.scalacheckEffect
    val scalatest: ModuleID = "org.scalatest" %% "scalatest" % version.scalatest % Test
    val specs2: ModuleID = "org.specs2" %% "specs2-core" % version.specs2 % Test
    val utest: ModuleID = "com.lihaoyi" %% "utest" % version.utest % Test
    val weaver: ModuleID = "com.disneystreaming" %% "weaver-cats" % version.weaver % Test
  }
  
}

