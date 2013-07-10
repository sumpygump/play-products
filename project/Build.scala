import sbt._
import Keys._
import play.Project._

import java.net.URLClassLoader

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
    scalacOptions ++= Seq("-feature"),
    // Add your own project settings here      
    registerTask("seed-products", "tasks.SeedProducts", "Seed data for products table")
  )

  /**
   * Register a task
   *
   * From http://kailuowang.blogspot.com/2013/05/define-arbitrary-tasks-in-play-21.html
   */
  def registerTask(name: String, taskClass: String, description: String) = {
    val sbtTask = (dependencyClasspath in Runtime) map { (deps) =>
      val depURLs = deps.map(_.data.toURI.toURL).toArray
      val classLoader = new URLClassLoader(depURLs, null)
      val task = classLoader.loadClass(taskClass)
        .newInstance().asInstanceOf[Runnable]
      task.run()
    }
    TaskKey[Unit](name, description) <<= sbtTask.dependsOn(compile in Compile)
  }
}
