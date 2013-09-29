import serial
import math
import os

def surfing(angle):
    cmd = """
        osascript -e 'tell application "System Events" to key code 123'
        """
    os.system(cmd)

def leftArrow():
    cmd = """
        osascript -e 'tell application "System Events" to key code 123'
        """
    os.system(cmd)

def rightArrow():
    cmd = """
        osascript -e 'tell application "System Events" to key code 124'
        """
    os.system(cmd)

ser = serial.Serial('/dev/tty.usbserial-AM01QMOV', 9600)
while True:
    surfing(ser.readline())
