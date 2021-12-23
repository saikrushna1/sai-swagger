#! /bin/bash
osascript -e 'tell app "Terminal"
do script "pwsh -NoExit -c Start-Transcript -Path '$1'"
end tell'
