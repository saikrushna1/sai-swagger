echo off
set sendSignalPath=%1
for /f "tokens=2" %%a in ('tasklist^|find /i "ffmpeg.exe"') do set pid=%%a
if "%pid%" == "" (
	echo No Process is running
) else (
	echo ffmpeg is running in the pid "%pid%"
	%sendSignalPath% %pid%
)