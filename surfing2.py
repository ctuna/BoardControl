import serial
import os

LEFT = 0
RIGHT = 1

def surfing(dir):
    if dir == LEFT:
        leftArrow()
    elif dir == RIGHT:
        rightArrow()

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
    try:
        s = int(ser.readline())
        surfing(s)
    except KeyboardInterrupt:
        break
    #except:
    #    print "fd up"
    #    continue
