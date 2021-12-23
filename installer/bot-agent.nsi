;NSIS Modern User Interface
;Bot Agent Installer
;Written by Srinivasa Challa

;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"
  ;!include "LogicLib.nsh"

;--------------------------------
;General

  ;Name and file
  Name "Bot Agent"
  OutFile "Bot-Agent-Setup.exe"
  Unicode True

  
  ;Default installation folder
  InstallDir "$PROGRAMFILES64\Bot Agent"

  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\BotAgent" ""

  ;Request application privileges for Windows Vista
  RequestExecutionLevel admin

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "C:\Users\hp\Desktop\nsis1\Licence.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  Page custom nsDialogsPage nsDialogsPageLeave

  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH

;--------------------------------
;Languages

  !insertmacro MUI_LANGUAGE "English"

;--------------------------------


Var Dialog
Var Label
Var Text
Var Text_State
var Client_State
var Secret_State
var Client
var Secret
var oauthServer_state
var oauthServer
var agentName_state
var agentName

Function nsDialogsPage

	nsDialogs::Create 1018
	!insertmacro MUI_HEADER_TEXT "Agent Custom Configuration" "Provide necessary custom configurations for Agent"
	Pop $Dialog

	${If} $Dialog == error
		Abort
	${EndIf}

	${NSD_CreateLabel} 0 0 30% 12u "Cobot Server URL* :"
	Pop $Label

	${NSD_CreateText} 35% 0 50% 12u $Text_State
	Pop $Text
	
	;${NSD_CreateLabel} 0 20u 30% 12u "Oauth Client :"
	;Pop $Label

	;${NSD_CreateText} 35% 20u 50% 12u $Client_State
	;Pop $Client
	
	;${NSD_CreateLabel} 0 40u 30% 12u "Oauth Secret :"
	;Pop $Label

	;${NSD_CreatePassword} 35% 40u 50% 12u $Secret_State
	;Pop $Secret
	
	;${NSD_CreateLabel} 0 60u 30% 12u "Oauth Server :"
	;Pop $Label

	;${NSD_CreateText} 35% 60u 50% 12u $oauthServer_state
	;Pop $oauthServer
	
	${NSD_CreateLabel} 0 20u 30% 12u "Agent ID* :"
	Pop $Label

	${NSD_CreateText} 35% 20u 50% 12u $agentName_state
	Pop $agentName
	
	nsDialogs::Show

FunctionEnd


Function nsDialogsPageLeave

	${NSD_GetText} $Text $Text_State
	;${NSD_GetText} $Client $Client_State
	;${NSD_GetText} $Secret $Secret_State
	;${NSD_GetText} $oauthServer $oauthServer_state
	${NSD_GetText} $agentName $agentName_state
		
FunctionEnd

;Installer Sections

Section "Bot Agent" SecDummy

  SetOutPath "$INSTDIR"
  
  File "C:\Users\hp\Desktop\nsis1\agent-1.0.0.jar"
  File "C:\Users\hp\Desktop\nsis1\StopAgent.bat"
  File "C:\Users\hp\Desktop\nsis1\icon_menu64.png"
  
  SetOutPath "$INSTDIR\libs"
  File "C:\Users\hp\Desktop\nsis1\libs\*"
  
  ;Store installation folder
  WriteRegStr HKCU "Software\Bot Agent" "" $INSTDIR
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BotAgent" "DisplayName" "Bot Agent"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BotAgent" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BotAgent" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BotAgent" "NoRepair" 1
  
  Var /GLOBAL tokenIntrospect
  StrCpy $tokenIntrospect "/v1/introspect"
  Var /GLOBAL tokenStr
  StrCpy $tokenStr "/v1/token"
  
  
  FileOpen $0 "$INSTDIR\application.properties" w
  FileWrite $0 "cobot.server.url: $Text_State"
  ;FileWrite $0 "$\r$\n" 
  ;FileWrite $0 "security.oauth2.client.clientId: $Client_State"
  ;FileWrite $0 "$\r$\n" 
  ;FileWrite $0 "security.oauth2.client.clientSecret: $Secret_State"
  ;FileWrite $0 "$\r$\n"
  ;FileWrite $0 "security.oauth2.resource.tokenInfoUri: $oauthServer_state$tokenIntrospect"
  ;FileWrite $0 "$\r$\n"
  ;FileWrite $0 "$\r$\n"
  ;FileWrite $0 "secclient.oauth2.client.grantType: client_credentials"
  ;FileWrite $0 "$\r$\n"  
  ;FileWrite $0 "secclient.oauth2.client.clientId: $Client_State"
  ;FileWrite $0 "$\r$\n"  
  ;FileWrite $0 "secclient.oauth2.client.clientSecret: $Secret_State"
  ;FileWrite $0 "$\r$\n"  
  ;FileWrite $0 "secclient.oauth2.client.accessTokenUri: $oauthServer_state$tokenStr"
  ;FileWrite $0 "$\r$\n"  
  ;FileWrite $0 "secclient.oauth2.client.scope: agent"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "server.port: 9777"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "scripts.dir: C:/cobot/scripts/agent"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "cobot.agent.id: $agentName"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "cobot.server.heartbeat.interval: 180000"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "logging.level.org.springframework: INFO"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "logging.level.com.peddle.digitals.cobot.agent: DEBUG"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "bot.agent.status: 1"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "scripts.store: local"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "s3.access_key: "
  FileWrite $0 "$\r$\n"
  FileWrite $0 "s3.secret_key: "
  FileWrite $0 "$\r$\n"
  FileWrite $0 "cli.scriptType: CLI"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "cobot.status.callback.url: "
  FileWrite $0 "$\r$\n"
  FileWrite $0 "cli.errors: ACCESS_DENIED:Access denied for user;NOT_RECOGNIZED_COMMAND:is not recognized as an internal or external command;UNKNOWN_DATABASE:Unknown database"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "video.record: off"
  FileWrite $0 "$\r$\n"
  FileClose $0
  
  FileOpen $0 "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp\agent_start.bat" w
  FileWrite $0 "start javaw -jar -Dinstalldir=$\"$INSTDIR$\"  -Dspring.config.location=$\"$INSTDIR\application.properties$\" $\"$INSTDIR\agent-1.0.0.jar$\"  $\"-Djava.awt.headless=false$\""
  FileClose $0
  
  ExecShellWait "" "$INSTDIR\StopAgent.bat"  "" SW_HIDE
  Sleep 1000
  ExecShell "" "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp\agent_start.bat"  "" SW_HIDE 

SectionEnd

Section "Video Recorder" SecRecorder

  SetOutPath "$INSTDIR"
  File "C:\Users\hp\Desktop\nsis1\PathEd.exe"
  

  SetOutPath "$INSTDIR\ffmpeg-win64"
  File /nonfatal /a /r "C:\Users\hp\Desktop\nsis1\ffmpeg-win64\"
  ;nsExec::Exec '$INSTDIR\PathEd.exe add "$INSTDIR\ffmpeg-win64\bin"'
  
 

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "Bot Agent Components that communitcate with connector Bot Server"
  LangString DESC_SecRecorder ${LANG_ENGLISH} "Component for Recording Video of Record and play-back of scenarios"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
	!insertmacro MUI_DESCRIPTION_TEXT ${SecRecorder} $(DESC_SecRecorder)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END
  
 
;--------------------------------
;Uninstaller Section

Section "Uninstall"


  
  ExecShellWait "" "$INSTDIR\StopAgent.bat"  "" SW_HIDE

  ;nsExec::Exec '$INSTDIR\PathEd.exe remove "$INSTDIR\ffmpeg-win64\bin"'
  
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BotAgent"
  
  Delete "$INSTDIR\agent-1.0.0.jar"
  Delete "$INSTDIR\StopAgent.bat"
  Delete "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp\agent_start.bat"

  Delete "$INSTDIR\application.properties"
  Delete "$INSTDIR\libs\*.*"
  Delete "$INSTDIR\libs\byte-buddy-1.8.15.jar"
  Delete "$INSTDIR\libs\client-combined-3.141.59.jar"
  Delete "$INSTDIR\libs\commons-exec-1.3.jar"
  Delete "$INSTDIR\libs\commons-lang3-3.8.1.jar"
  Delete "$INSTDIR\libs\guava-25.0-jre.jar"
  Delete "$INSTDIR\libs\javax.json-1.1.4.jar"
  Delete "$INSTDIR\libs\json-20190722.jar"
  Delete "$INSTDIR\libs\okhttp-3.11.0.jar"
  Delete "$INSTDIR\libs\okio-1.14.0.jar"
  Delete "$INSTDIR\PathEd.exe"
  Delete "$INSTDIR\icon_menu64.png"
  
  
  Delete "$INSTDIR\Uninstall.exe"
  RMDIR /r "$INSTDIR\ffmpeg-win64"
  RMDIR /r "$INSTDIR\libs"
  RMDIR /r "$INSTDIR"
  
  DeleteRegKey /ifempty HKCU "Software\BotAgent"

SectionEnd
