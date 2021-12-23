# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	with Region(u"Desktop||List"):
		double_left_click(u"Calculator||ListItem")

with Window(u"Calculator||Window"):
	with Region(u"Calculator||Window->||Group"):
		left_click(u"Number pad||Group->Seven||Button")
		left_click(u"Standard operators||Group->Multiply by||Button")
		left_click(u"Number pad||Group->Six||Button")
		left_click(u"Standard operators||Group->Equals||Button")
	with Region(u"Calculator||Window"):
		left_click(u"||Group->Display controls||Group->Clear||Button")
		left_click(u"Close Calculator||Button")

#with Window(u"Program Manager||Pane"):
	#right_click(u"Desktop||List")
	#left_click(u"Desktop||List")
print("Playback successfully completed")