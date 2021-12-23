# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	left_click(u"Desktop||List")

with Window(u"Search||Window"):
	with Region(u"Bing||Pane"):
		double_left_click(u"Bing||Pane")
	with Region(u"Bing||Pane->Bing||Pane->Results||List"):
		left_click(u"Notepad, App, Press right to switch preview||ListItem")

with Window(u"Program Manager||Pane"):
	left_click(u"Desktop||List")

with Window(u"*Untitled - Notepad||Window"):
	left_click(u"Text Editor||Edit")
	with Region(u"Notepad||Window"):
		left_click(u"Don't Save||Button")

with Window(u"Program Manager||Pane"):
	left_click(u"Desktop||List")

with Window(u"Taskbar||Pane"):
	with Region(u"||Pane"):
		double_right_click(u"Notification Chevron||Button")
		left_click(u"Notification Chevron||Button")
