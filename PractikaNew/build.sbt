import play.core.PlayVersion

name := "qweqweqwe"
 
version := "1.0" 
      
lazy val `qweqweqwe` = (project in file(".")).enablePlugins(PlayScala)
lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)


resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

unmanagedJars in Compile += file("play-cxf_play26_2.12-1.3.0.jar" )

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice,
  "org.apache.cxf" % "cxf-rt-frontend-jaxws" % "3.1.2",
  "org.springframework" % "spring-context" % "5.0.1.RELEASE",
  "org.apache.cxf" % "cxf-rt-bindings-soap" % "3.1.2",
  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
  "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1",
  "org.postgresql" % "postgresql" % "9.4.1211")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      