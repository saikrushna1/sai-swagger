# encoding: utf-8

from pywinauto_recorder.player import *


with Window(u"Program Manager||Pane"):
	double_left_click(u"Desktop||List")
	with Region(u"Desktop||List"):
		left_click(u"puttytel||ListItem")
		send_keys("localhost")

with Window(u"PuTTYtel Configuration||Window"):
	left_click(u"Host Name (or IP address)||Edit")
	with Region(u"||ComboBox"):
		left_click(u"||List")
	left_click(u"||ComboBox->||List")
	with Region(u"About PuTTYtel||Window"):
		left_click(u"Close||Button")

with Window(u"Program Manager||Pane"):
	left_click(u"Desktop||List")
	left_click(u"Desktop||List")
	drag_and_drop(u"Desktop||List", u"Desktop||List")
	left_click(u"Desktop||List")
