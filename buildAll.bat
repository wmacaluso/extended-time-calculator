::::::::::Need to add wix, msbuild, java, maven to path

:::::::::::::::Add this to path (C:\Windows\Microsoft.NET\Framework\v4.0.30319)

:::::::::::::::Build java project
cd "%~dp0extended-time-calculator"
::call mvn clean jfx:native

:::::::::::::::::::::::Remove jre, just copy existing x86 jre
::rmdir "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\runtime" /s /q
::xcopy "C:\Program Files\Java\jre1.8.0_25" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\runtime\jre" /E /I /Y /R
::xcopy "C:\Program Files (x86)\Java\jre1.8.0_25" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\runtime\jre" /E /I /Y /R

::::::::::::::Build and copy executable wrapper
msbuild "%~dp0Extended Time Calculator Setup\ExecutableWrapper\ExecutableWrapper.sln" /target:Clean
msbuild "%~dp0Extended Time Calculator Setup\ExecutableWrapper\ExecutableWrapper.sln"

:::::::::::::::::::::::::::THIS WILL FAIL: NEED TO MANUALLY COPY THE NEW ExecutableWrapper::::::::::::::::::
del "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\Extended Time Calculator.exe"
copy "%~dp0Extended Time Calculator Setup\ExecutableWrapper\ExecutableWrapper\bin\Debug\Extended Time Calculator.exe" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\Extended Time Calculator.exe" /Y

:::::::::::::::::::::::::Generate wix fragment for install
::heat dir "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator" -gg -sfrag -template:fragment -out "%~dp0Extended Time Calculator Setup\extendedTimeCalculator.wxs" -cg TestTimerGroup -dr INSTALLLOCATION -var var.testTimerSourceDir

:::::::::::::Harvest JRE
cd %JAVA_HOME%
heat dir "jre" -srd -gg -sfrag -template:fragment -out "%~dp0Extended Time Calculator Setup\jre_runtime.wxs" -cg JRE_Runtime -dr JRE_Runtime -var var.jre_dir
cd "%~dp0"

call buildMSIs

::::::::::::::::::::::::::Copy trial version config.properties and testtimer.db
::copy "%~dp0Extended Time Calculator Setup\AdditionalDependencies\testtimer.db" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\app\testtimer.db" /Y
::copy "%~dp0Extended Time Calculator Setup\AdditionalDependencies\TrialVersionConfig\config.properties" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\app\config.properties" /Y

::::::::::::::::::::::::::::Create trial version msi
::cd "../Extended Time Calculator Setup/Extended Time Calculator Setup Trial"
::candle.exe -d"testTimerSourceDir=..\..\extended-time-calculator\target\jfx\native\Extended Time Calculator" -d"SolutionDir=%~dp0Extended Time Calculator Setup\\" -d"TargetDir=%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Trial\bin\Debug\\" -dTargetExt=.msi -d"TargetFileName=Extended Time Calculator Setup Trial.msi" -out obj\Debug\ -arch x86 -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" "%~dp0Extended Time Calculator Setup\extendedTimeCalculator.wxs" "%~dp0Extended Time Calculator Setup\jre_runtime.wxs" TrialInstall.wxs
::Light.exe -out "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Trial\bin\Debug\Extended Time Calculator Setup Trial.msi" -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" -wixprojectfile "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Trial\Extended Time Calculator Setup Trial.wixproj" obj\Debug\extendedTimeCalculator.wixobj obj\Debug\TrialInstall.wixobj

::::::::::::::::::::::::::::Copy full version config
::copy "%~dp0Extended Time Calculator Setup\AdditionalDependencies\FullVersionConfig\config.properties" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\app\config.properties" /Y

::::::::::::::::::::::::::::Create full version msi
::cd "../Extended Time Calculator Setup Full"
::candle.exe -d"testTimerSourceDir=..\..\extended-time-calculator\target\jfx\native\Extended Time Calculator" -d"SolutionDir=%~dp0Extended Time Calculator Setup\\" -d"TargetDir=%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Full\bin\Debug\\" -dTargetExt=.msi -d"TargetFileName=Extended Time Calculator Setup.msi" -out obj\Debug\ -arch x86 -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" "%~dp0Extended Time Calculator Setup\extendedTimeCalculator.wxs" "%~dp0Extended Time Calculator Setup\jre_runtime.wxs" FullInstall.wxs
::Light.exe -out "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Full\bin\Debug\Extended Time Calculator Setup Full.msi" -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" -wixprojectfile "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Full\Extended Time Calculator Setup Full.wixproj" obj\Debug\extendedTimeCalculator.wixobj obj\Debug\FullInstall.wixobj

cd ..\..