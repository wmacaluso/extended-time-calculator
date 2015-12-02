Powershell command to harvest wix file structure

PS C:\Users\Will\Code\TestTimer\Extended Time Calculator Setup> heat dir "..\extended-time-calculator\target\jfx\native\
Extended Time Calculator" -gg -sfrag -template:fragment -out extendedTimeCalculator.wxs -cg TestTimerGroup -dr INSTALLLOCATION -var var.testTimerSourceDir