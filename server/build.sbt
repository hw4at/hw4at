import sbt.Package.ManifestAttributes

name := "server"

version := "1.0"

scalaVersion := "2.12.2"
val vertxVersion = "3.4.2"

libraryDependencies += "io.vertx" %  "vertx-codegen"                         % vertxVersion
libraryDependencies += "io.vertx" %% "vertx-lang-scala"                      % vertxVersion
libraryDependencies += "io.vertx" %% "vertx-web-scala"                       % vertxVersion
libraryDependencies += "io.vertx" %% "vertx-sql-common-scala"                % vertxVersion
libraryDependencies += "io.vertx" %% "vertx-jdbc-client-scala"               % vertxVersion
libraryDependencies += "io.vertx" %% "vertx-mysql-postgresql-client-scala"   % vertxVersion

packageOptions += ManifestAttributes(("Main-Verticle", "scala:surl.server.SurlVerticle"))

assemblyJarName in assembly := "surl-server.jar"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.last
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.last
  case PathList("codegen.json") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}