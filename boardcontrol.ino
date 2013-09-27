/*
  Graph
 
 A simple example of communication from the Arduino board to the computer:
 the value of analog input 0 is sent out the serial port.  We call this "serial"
 communication because the connection appears to both the Arduino and the
 computer as a serial port, even though it may actually use
 a USB cable. Bytes are sent one after another (serially) from the Arduino
 to the computer.
 
 You can use the Arduino serial monitor to view the sent data, or it can
 be read by Processing, PD, Max/MSP, or any other program capable of reading 
 data from a serial port.  The Processing code below graphs the data received 
 so you can see the value of the analog input changing over time.
 
 The circuit:
 Any analog input sensor is attached to analog in pin 0.
  
 created 2006
 by David A. Mellis
 modified 9 Apr 2012
 by Tom Igoe and Scott Fitzgerald
 
 This example code is in the public domain.

 http://www.arduino.cc/en/Tutorial/Graph
 */
 //0LEFT
 //1FRONT
 //2 RIGHT
 //3 BACK
 

const int LEFT = 0;
const int FRONT = 1;
const int RIGHT = 2;
const int BACK = 3;
const int NONE = 4;

int position=NONE;
int prevPosition = NONE;

void setup() {
  // initialize the serial communication:
  Serial.begin(9600);
}
//150 is 90 degreese to the right
//72 90 degrees to the left
//97 is normal sitting
boolean isBetween(int x, int minVal, int maxVal){
  return (x>=minVal) && (x<=maxVal);
}

void loop() {
  // send the value of analog input 0:
  readX();
  readY();

  //only print when the value changes
  if (prevPosition != position && position != NONE )Serial.println(position);
  prevPosition = position;
  delay(2);
}

void readX(){
  int x = analogRead(A2);
  if (x<401 ){
    double xScaled = ((double) x) / 400;
    int angleX =  asin(xScaled)*100; 
    if (isBetween(angleX, 120, 151)){      
      position = RIGHT;
      //RIGHT
      //Serial.println(100);
    }
    else if (isBetween(angleX, 85, 110)){
      //only write over X changes
      if (position == RIGHT || position == LEFT)position = NONE;
      //Serial.println(50);
    }
    else if (isBetween(angleX, 60, 85)){
      //LEFT
      position = LEFT;
      //Serial.println(10);
      }
          //Serial.println(angleX);
  }
}

void readY(){
  int y = analogRead(A1);
  if (y<401 ){
    double yScaled = ((double) y) / 400;
    int angleY =  asin(yScaled)*100; 
    //FORWARD
    if (isBetween(angleY, 120, 151)){
      position = FRONT;
      //Serial.println(100);
    }
    //MIDDLE
    if (isBetween(angleY, 85, 110)){
      //only write over Y changes
      if (position == FRONT || position == BACK)position = NONE;
      //Serial.println(50);
    }
    //BACKWARD
    if (isBetween(angleY, 60, 85)){
      position = BACK;
      //Serial.println(10);
      }
          //Serial.println(angleX);
  }
}

