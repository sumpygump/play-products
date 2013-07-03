import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "products"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "net.sf.barcode4j" % "barcode4j" % "2.0",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
