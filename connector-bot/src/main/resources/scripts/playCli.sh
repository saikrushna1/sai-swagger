osascript -e 'tell app "Terminal"
do script "/bin/bash '$1'"
end tell' -e 'activate'