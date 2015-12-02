::CHANGE JAVA PATH variable IF NEEDED (jre_dir)
::C:\Program Files\Java\jdk1.8.0_25\jre


::::::::::::::::::::::::::Copy trial version config.properties and testtimer.db
copy "%~dp0Extended Time Calculator Setup\AdditionalDependencies\testtimer.db" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\app\testtimer.db" /Y
copy "%~dp0Extended Time Calculator Setup\AdditionalDependencies\TrialVersionConfig\config.properties" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\app\config.properties" /Y

::::::::::::::::::::::::::::Create trial version msi
cd "Extended Time Calculator Setup/Extended Time Calculator Setup Trial"
candle.exe -d"jre_dir=C:\Program Files\Java\jdk1.8.0_25\jre" -d"testTimerSourceDir=..\..\extended-time-calculator\target\jfx\native\Extended Time Calculator" -d"SolutionDir=%~dp0Extended Time Calculator Setup\\" -d"TargetDir=%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Trial\bin\Debug\\" -dTargetExt=.msi -d"TargetFileName=Extended Time Calculator Setup Trial.msi" -out obj\Debug\ -arch x86 -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" "%~dp0Extended Time Calculator Setup\extendedTimeCalculator.wxs" "%~dp0Extended Time Calculator Setup\jre_runtime.wxs" TrialInstall.wxs
Light.exe -out "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Trial\bin\Debug\Extended Time Calculator Setup Trial.msi" -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" -wixprojectfile "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Trial\Extended Time Calculator Setup Trial.wixproj" obj\Debug\extendedTimeCalculator.wixobj obj\Debug\jre_runtime.wixobj obj\Debug\TrialInstall.wixobj

::::::::::::::::::::::::::::Copy full version config
copy "%~dp0Extended Time Calculator Setup\AdditionalDependencies\FullVersionConfig\config.properties" "%~dp0extended-time-calculator\target\jfx\native\Extended Time Calculator\app\config.properties" /Y

::::::::::::::::::::::::::::Create full version msi
cd "../Extended Time Calculator Setup Full"
candle.exe -d"jre_dir=C:\Program Files\Java\jdk1.8.0_25\jre" -d"testTimerSourceDir=..\..\extended-time-calculator\target\jfx\native\Extended Time Calculator" -d"SolutionDir=%~dp0Extended Time Calculator Setup\\" -d"TargetDir=%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Full\bin\Debug\\" -dTargetExt=.msi -d"TargetFileName=Extended Time Calculator Setup.msi" -out obj\Debug\ -arch x86 -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" "%~dp0Extended Time Calculator Setup\extendedTimeCalculator.wxs" "%~dp0Extended Time Calculator Setup\jre_runtime.wxs" FullInstall.wxs
Light.exe -out "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Full\bin\Debug\Extended Time Calculator Setup Full.msi" -ext "WixUIExtension.dll" -ext "WixUtilExtension.dll" -wixprojectfile "%~dp0Extended Time Calculator Setup\Extended Time Calculator Setup Full\Extended Time Calculator Setup Full.wixproj" obj\Debug\extendedTimeCalculator.wixobj obj\Debug\jre_runtime.wixobj obj\Debug\FullInstall.wixobj