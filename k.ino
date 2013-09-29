const float alpha = 0.5;

double fXg = 0;
double fYg = 0;
double fZg = 0;
 
void setup()
{
    Serial.begin(9600);
}
 
void loop()
{
    double pitch, roll, Xg, Yg, Zg;
    Xg = analogRead( 0 );
    Xg = acceleration( Xg );
    Yg = analogRead( 1 );
    Yg = acceleration( Yg );
    Zg = analogRead( 2 );
    Zg = acceleration( Zg );
 
    //Low Pass Filter
    fXg = Xg * alpha + (fXg * (1.0 - alpha));
    fYg = Yg * alpha + (fYg * (1.0 - alpha));
    fZg = Zg * alpha + (fZg * (1.0 - alpha));
    
    //Roll & Pitch Equations
    roll  = (atan2(-fYg, fZg)*180.0)/M_PI;
    pitch = (atan2(fXg, sqrt(fYg*fYg + fZg*fZg))*180.0)/M_PI;
    //String pi = "P" + String(pitch);
    //String ro = "R" + String(roll);
    Serial.println(pitch,DEC);
    //Serial.println();
    delay(10);
}

double acceleration( int accelVolt ) {
  return map_double(accelVolt, 0.0, 666.666/*hailsatan*/, -3.0, 3.0);
}

float map_double(double x, double in_min, double in_max, double out_min, double out_max) {
  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}
