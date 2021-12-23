# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	with Region(u"Desktop||List"):
		double_left_click(u"VLC media player||ListItem")

with Window(u"VLC media player||Window"):
	with Region(u"||Custom->||Pane"):
		left_click(u"||Custom")

with Window(u""):
    menu_click(u"VLC media player||Window", r"Media Alt+M")

with Window(u"VLC media player||Window"):
	menu_click(u"", r"Media Alt+M")
	with Region(u"||MenuBar"):
		drag_and_drop(u"Media Alt+M||MenuItem", u"Media Alt+M||MenuItem")
	menu_click(u"", r"Media Alt+M")
	with Region(u"||Custom->||Pane"):
		left_click(u"||Custom")
		left_click(u"||Custom")
		left_click(u"||Custom")
		left_click(u"||Custom")
		right_click(u"||Custom")

with Window(u"Program Manager||Pane"):
	left_click(u"Desktop||List")
