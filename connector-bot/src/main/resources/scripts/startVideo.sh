#! /bin/bash
osascript -e 'tell app "Terminal"
do script "pwsh -NoExit -c ffmpeg -f avfoundation -i \"0:\" '$1'"
end tell'