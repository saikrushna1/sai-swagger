package com.saviynt.identitybot.constants;

public class Constants {
	
	public static final String CONFIG_PATH= "/application.properties";
	public static enum ScriptType{
		WEB_PLAYBACK,CLI_RECORD,CLI_PLAYBACK,DESKTOP_PLAYBACK
	}
	public static class AGENT_URLS{
		public static final String EXECUTE_REMOTE_WEB="/agent/api/executescript"; 
		public static final String RECORD_REMOTE_CLI="/agent/api/cli/record";
		public static final String EXECUTE_REMOTE_CLI="/agent/api/cli/execute";
		public static final String EXECUTE_REMOTE_DESKTOP="/agent/api/desktop/execute";
		public static final String EXECUTE_REMOTE_DESKTOP_PYWIN="/agent/api/desktop/pywin/execute";
	}
	
	public static class COBOT_STATUS_URLS{
		public static final String RECORD_REMOTE_CLI="/agent/job/cli/recordstatus";
		public static final String PLAY_REMOTE_CLI="/agent/job/cli/playstatus";
		public static final String PLAY_REMOTE_DESKTOP="agent/job/desktop/playstatus";
		public static final String PLAY_REMOTE_DESKTOP_PYWIN="agent/job/desktop/pywin/playstatus";
		public static final String PLAY_REMOTE_CLI_WITHOUT_VIDEO="agent/job/cli/playstatuswithoutvideo";
		public static final String PLAY_REMOTE_DESKTOP_WITHOUT_VIDEO="agent/job/desktop/playstatuswithoutvideo";
		public static final String PLAY_REMOTE_DESKTOP_PYWIN_WITHOUT_VIDEO="agent/job/desktop/pywin/playstatuswithoutvideo";
	}
}
