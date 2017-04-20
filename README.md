Appetize.io Appium Samples
==========================

Appium test scripts can be written in many languages. We currently provide examples for:
- Java
- Node.js

For further questions, please see our documentation at https://appetize.io/docs.

Java
----
Your system must have a JVM and Java Compiler. Run `javac -version` from a command line to verify.

Our example uses Gradle as a build system and dependency manager. Run these commands in a terminal:

	cd appium-samples/java

	./gradlew assembleDist
	cd build/distributions
	unzip appium-samples.zip

	# run android
	appium-samples/bin/appium-samples android

	# run ios
	appium-samples/bin/appium-samples ios

On Windows, replace `./gradlew` with `gradle.bat`.


Node.js
-------

To install NodeJS, please see https://nodejs.org/en/download/package-manager/. Our preferred method on Mac OSX is to use Homebrew. Install Homebrew by following directions at http://brew.sh/, then you may run `brew install node`.

These test scripts depend on a few packages which are referenced in the file package.json. To install the packages listed in package.json, you may cd to this directory in Terminal and run `npm install`.

You may now run any of the included sample scripts by running `node filename.js`, for example, `node android-basic.js`, `node ios-basic.js`, or `node ios-legacy-basic.js`.
