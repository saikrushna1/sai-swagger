# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	double_left_click(u"Desktop||List->VLC media player||ListItem")
	left_click(u"Desktop||List")

with Window(u"VLC media player||Window"):
	menu_click(u"", r"Media Alt+M")
	with Region(u"Select one or more files to open||Window"):
		left_click(u"Cancel||Button")
	menu_click(u"", r"Tools Alt+s->Effects and Filters Ctrl+E")
	left_click(u"Adjustments and Effects||Window->||Group->Close Enter||Button")
	with Region(u"||TitleBar"):
		left_click(u"Close||Button")
