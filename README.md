sbt-typescript  [![Build Status](https://travis-ci.org/ArpNetworking/sbt-typescript.svg?branch=master)](https://travis-ci.org/ArpNetworking/sbt-typescript)
========

Allows TypeScript to be used from within sbt. Leverages the functionality of com.typesafe.sbt:js-engine to run the 
typescript compiler.

To use this plugin use the addSbtPlugin command within your project's plugins.sbt (or as a global setting) i.e.:

```scala
addSbtPlugin("com.arpnetworking" % "sbt-typescript" % "0.1.9")
```

You will also need to enable the SbtWeb plugin in your project.

The options provided mimic the arguments to the tsc command line compiler.

Option              			| Description
--------------------------------|------------
declaration						| Generates corresponding '.d.ts' file.
experimentalAsyncFunctions		| Enables experimental support for ES7 async functions.
experimentalDecorators			| Enables experimental support for ES7 decorators.
init							| Initializes a TypeScript project and creates a tsconfig.json file.
jsx								| Specify JSX code generation: 'preserve' or 'react'
mapRoot							| Specifies the location where debugger should locate map files instead of generated locations.
module							| Specify module code generation: 'commonjs', 'amd', 'system' or 'umd'
newLine							| Specifies the end of line sequence to be used when emitting files: 'CRLF' (dos) or 'LF' (unix).
noEmit							| Do not emit outputs.
noEmitOnError					| Do not emit outputs if any errors were reported.
noImplicitAny					| Raise error on expressions and declarations with an implied 'any' type.
outDir							| Redirect output structure to the directory.
outFile							| Concatenate and emit output to single file.
preserveConstEnums				| Do not erase const enum declarations in generated code.
project							| Compile the project in the given directory.
removeComments					| Do not emit comments to output.
rootDir							| Specifies the root directory of input files. Use to control the output directory structure with --outDir.
sourceMap						| Generates corresponding '.map' file.
sourceRoot						| Specifies the location where debugger should locate TypeScript files instead of source locations.
suppressImplicitAnyIndexErrors	| Suppress noImplicitAny errors for indexing objects lacking index signatures.
target							| Specify ECMAScript target version: 'ES3' (default), 'ES5', or 'ES6' (experimental)

By default, all typescript files (*.ts and *.tsx) are included in the compilation and will generate corresponding javascript
files.  To change this, supply an includeFilter in the TypescriptKeys.typescript task configuration.

For example:

```scala
includeFilter in TypescriptKeys.typescript := "myFile.ts"
```

You can also set an exclude filter in the same way.

A note on typescript compiling speed
------------------------------------

Sometimes the default engine (Trireme) can be quite slow. If you're experiencing long typescript compile times you can always switch to node.js ([remember to install it first](http://nodejs.org/download/)) adding this line to your `build.sbt` file:

```scala
JsEngineKeys.engineType := JsEngineKeys.EngineType.Node
```

&copy; Groupon Inc., 2014
