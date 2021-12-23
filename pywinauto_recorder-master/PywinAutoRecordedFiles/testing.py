# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	drag_and_drop(u"Desktop||List", u"Desktop||List")

with Window(u"VLC media player||Window"):
	with Region(u"||Custom->||Pane"):
		left_click(u"||Custom")

with Window(u""):
    left_click(u"||Menu")

with Window(u"VLC media player||Window"):
	with Region(u"Select one or more files to open||Window->||Pane"):
		left_click(u"||Pane#[0,0]")
	menu_click(u"", r"Tools Alt+s->Effects and Filters Ctrl+E")
	with Region(u"Adjustments and Effects||Window->||Custom->||Pane->||Custom->||Custom->||Pane->||Custom"):
		left_click(u"Enable||CheckBox")
		left_click(u"Enable||CheckBox")
		left_click(u"Enable||CheckBox")
		left_click(u"Enable||CheckBox")
	with Region(u"Adjustments and Effects||Window"):
		left_click(u"||Custom->||Pane->||Custom->||Custom->||Pane->||Custom->Enable||CheckBox")
	left_click(u"Adjustments and Effects||Window->||Group->Close Enter||Button")
	with Region(u"||Custom->||Pane"):
		left_click(u"||Custom")

with Window(u"Program Manager||Pane"):
	left_click(u"Desktop||List")
	right_click(u"Desktop||List")

with Window(u"Context||Menu"):
	left_click(u"Stop recording\t\tCTRL+ALT+R||MenuItem")
