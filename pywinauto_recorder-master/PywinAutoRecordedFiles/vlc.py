# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	with Region(u"Desktop||List"):
		double_left_click(u"VLC media player||ListItem")

with Window(u"VLC media player||Window"):
	menu_click(u"", r"Media Alt+M->Open File... Ctrl+O")
	with Region(u"Select one or more files to open||Window"):
		left_click(u"Cancel||Button")

with Window(u""):
	menu_click(u"VLC media player||Window", r"Tools Alt+s")

with Window(u"VLC media player||Window"):
	menu_click(u"", r"Tools Alt+s")
	with Region(u"Adjustments and Effects||Window->||Custom->||Pane->||Custom->||Custom->||Pane->||Custom"):
		left_click(u"Enable||CheckBox")
		left_click(u"Preset Down||ComboBox")
		left_click(u"Preset Down||ComboBox")
	with Region(u"Adjustments and Effects||Window->||Custom->||Pane->||Custom->||Custom"):
		left_click(u"||Pane->||Custom->||Custom->||Slider#[0,7]")
	with Region(u"Adjustments and Effects||Window"):
		left_click(u"||Custom->||Pane->||Custom->||Custom->Equalizer||Tab->Spatializer||TabItem")
	drag_and_drop(u"Adjustments and Effects||Window->||Group->Close Enter||Button", u"Adjustments and Effects||Window->||Group->Close Enter||Button")
	with Region(u"||TitleBar"):
		left_click(u"Close||Button")

with Window(u"Taskbar||Pane"):
	with Region(u"||Pane"):
		left_click(u"Notification Chevron||Button")
		right_click(u"Notification Chevron||Button")

with Window(u"Context||Menu"):
	left_click(u"Stop recording\t\tCTRL+ALT+R||MenuItem")
print("Playback successfully completed")