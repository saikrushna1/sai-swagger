                                                                             
FOR /F "tokens=5" %%T IN ('netstat -a -n -o ^| findstr "9777" ') DO (
SET /A ProcessId=%%T) &GOTO SkipLine                                                   
:SkipLine                                                                              
echo ProcessId to kill = %ProcessId%
if not [%ProcessId%]==[] taskkill /f /pid %ProcessId%

