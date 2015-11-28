/*
 * Copyright 2014 Groupon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arpnetworking.sbt.typescript

import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import sbt._
import com.typesafe.sbt.jse.SbtJsTask
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys._
import spray.json.{JsString, JsBoolean, JsObject}

object Import {

  object TypescriptKeys {
    val typescript = TaskKey[Seq[File]]("typescript", "Invoke the typescript compiler.")
    val typescriptGenerateCompiler = TaskKey[File]("generateCompiler", "Generates the typescript compile script.")

    val declaration = SettingKey[Boolean]("--declaration", "Generates corresponding '.d.ts' file.")
    val experimentalAsyncFunctions = SettingKey[Boolean]("--experimentalAsyncFunctions", "Enables experimental support for ES7 async functions.")
    val experimentalDecorators = SettingKey[Boolean]("--experimentalDecorators", "Enables experimental support for ES7 decorators.")
    val init = SettingKey[Boolean]("--init", "Initializes a TypeScript project and creates a tsconfig.json file.")
    val jsx = SettingKey[String]("--jsx KIND", "Specify JSX code generation: 'preserve' or 'react'")
    val mapRoot = SettingKey[String]("--mapRoot LOCATION", "Specifies the location where debugger should locate map files instead of generated locations.")
    val module = SettingKey[String]("--module KIND", "Specify module code generation: 'commonjs', 'amd', 'system' or 'umd'")
    val newLine = SettingKey[String]("--newLine NEWLINE", "Specifies the end of line sequence to be used when emitting files: 'CRLF' (dos) or 'LF' (unix).")
    val noEmit = SettingKey[Boolean]("--noEmit", "Do not emit outputs.")
    val noEmitOnError = SettingKey[Boolean]("--noEmitOnError", "Do not emit outputs if any errors were reported.")
    val noImplicitAny = SettingKey[Boolean]("--noImplicitAny", "Raise error on expressions and declarations with an implied 'any' type.")
    val noLib = SettingKey[Boolean]("--noLib", "Do not include the default library file (lib.d.ts).")
    val outDir = SettingKey[String]("--outDir DIRECTORY", "Redirect output structure to the directory.")
    val outFile = SettingKey[String]("--outFile FILE", "Concatenate and emit output to single file.")
    val preserveConstEnums = SettingKey[Boolean]("--preserveConstEnums", "Do not erase const enum declarations in generated code.")
    val project = SettingKey[String]("--project DIRECTORY", "Compile the project in the given directory.")
    val removeComments = SettingKey[Boolean]("--removeComments", "Do not emit comments to output.")
    val rootDir = SettingKey[String]("--rootDir LOCATION", "Specifies the root directory of input files. Use to control the output directory structure with --outDir.")
    val sourceMap = SettingKey[Boolean]("--sourceMap", "Generates corresponding '.map' file.")
    val sourceRoot = SettingKey[String]("--sourceRoot LOCATION", "Specifies the location where debugger should locate TypeScript files instead of source locations.")
    val suppressImplicitAnyIndexErrors = SettingKey[Boolean]("--suppressImplicitAnyIndexErrors", "Suppress noImplicitAny errors for indexing objects lacking index signatures.")
    val target = SettingKey[String]("--target VERSION", "Specify ECMAScript target version: 'ES3', 'ES5', or 'ES6' (experimental).")
  }
}

object SbtTypescript extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import SbtJsTask.autoImport.JsTaskKeys._
  import autoImport.TypescriptKeys._

  val typescriptUnscopedSettings = Seq(

    includeFilter in typescript := GlobFilter("*.ts") | GlobFilter("*.tsx"),
    excludeFilter in typescript := GlobFilter("*.d.ts"),

    sources in typescript := (sourceDirectories.value ** ((includeFilter in typescript).value -- (excludeFilter in typescript).value)).get,

    jsOptions := JsObject(
      "declaration" -> JsBoolean(declaration.value),
      "experimentalAsyncFunctions" -> JsBoolean(experimentalAsyncFunctions.value),
      "experimentalDecorators" -> JsBoolean(experimentalDecorators.value),
      "init" -> JsBoolean(init.value),
      "jsx" -> JsString(jsx.value),
      "mapRoot" -> JsString(mapRoot.value),
      "module" -> JsString(module.value),
      "newLine" -> JsString(newLine.value),
      "noEmit" -> JsBoolean(noEmit.value),
      "noEmitOnError" -> JsBoolean(noEmitOnError.value),
      "noImplicitAny" -> JsBoolean(noImplicitAny.value),
      "noLib" -> JsBoolean(noLib.value),
      "outDir" -> JsString(outDir.value),
      "outFile" -> JsString(outFile.value),
      "preserveConstEnums" -> JsBoolean(preserveConstEnums.value),
      "project" -> JsString(project.value),
      "removeComments" -> JsBoolean(removeComments.value),
      "rootDir" -> JsString(rootDir.value),
      "sourceMap" -> JsBoolean(sourceMap.value),
      "sourceRoot" -> JsString(sourceRoot.value),
      "suppressImplicitAnyIndexErrors" -> JsBoolean(suppressImplicitAnyIndexErrors.value),
      "target" -> JsString(target.value),

      "logLevel" -> JsString(logLevel.value.toString)

    ).toString()
  )

  override def projectSettings = Seq(
    declaration := false,
    experimentalAsyncFunctions := false,
    experimentalDecorators := false,
    init := false,
    jsx := "preserve",
    mapRoot := "",
    module := "",
    newLine := "crlf",
    noEmit := false,
    noEmitOnError := false,
    noImplicitAny := false,
    noLib := false,
    outDir := ((webTarget in Assets).value / "typescript").absolutePath,
    outFile := "",
    preserveConstEnums := false,
    project := "",
    removeComments := false,
    rootDir := "",
    sourceMap := false,
    sourceRoot := "",
    suppressImplicitAnyIndexErrors := false,
    target := "ES5",
    JsEngineKeys.parallelism := 1,
    logLevel := Level.Info

  ) ++ inTask(typescript)(
    SbtJsTask.jsTaskSpecificUnscopedSettings ++
      inConfig(Assets)(typescriptUnscopedSettings) ++
      inConfig(TestAssets)(typescriptUnscopedSettings) ++
      Seq(
        moduleName := "typescript",
        shellFile := getClass.getClassLoader.getResource("typescriptc.js"),

        taskMessage in Assets := "TypeScript compiling",
        taskMessage in TestAssets := "TypeScript test compiling"
      )
  ) ++ SbtJsTask.addJsSourceFileTasks(typescript) ++ Seq(
    typescript in Assets := (typescript in Assets).dependsOn(webModules in Assets).value,
    typescript in TestAssets := (typescript in TestAssets).dependsOn(webModules in TestAssets).value
  )

}
