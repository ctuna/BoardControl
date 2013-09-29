import serial
import math
import os

center = 10.0

def surfing(angle):
    if math.fabs(angle) < center:
        return
    elif angle > 0:
        rightArrow()
    else:
        leftArrow()

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

def leftDown():
    cmd = """
        osascript -e 'tell application "System Events" to key down key code 123'
        """
    os.system(cmd)

def rightDown():
    cmd = """
        osascript -e 'tell application "System Events" to key down key code 124'
        """
    os.system(cmd)

def leftUp():
    cmd = """
        osascript -e 'tell application "System Events" to key up key code 123'
        """
    os.system(cmd)

def rightUp():
    cmd = """
        osascript -e 'tell application "System Events" to key up key code 124'
        """
    os.system(cmd)

ser = serial.Serial('/dev/tty.usbserial-AM01QMOV', 9600)
while True:
    try:
        surfing(float(ser.readline()))
    except KeyboardInterrupt:
        break
    except:
        print "fd up"
        continue
