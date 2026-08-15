@ECHO OFF
SET APP_HOME=%~dp0
SET JAVA_EXE=java
IF NOT "%JAVA_HOME%"=="" SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
IF NOT EXIST "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" (
  ECHO ERROR: Gradle wrapper JAR is missing: %APP_HOME%gradle\wrapper\gradle-wrapper.jar
  EXIT /B 1
)
"%JAVA_EXE%" -classpath "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
