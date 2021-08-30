const int pin11=13;
const int pin12=12;
const int pin21=11;
const int pin22=10;
const int pin31=9;
const int pin32=8;
const int pin41=7;
const int pin42=6;
const int pinLEDIZQUIERDA=4;
const int pinLEDDERECHA=3;
const int pinLEDTRASERA=2;

const int pinOrientacion=A0;    //Izquierda o derecha
const int pinDireccion=A1;      //Adelante o atras
const int pinApagado=A2;        //Apagado
const int pinVelocidad=A3;      //lento o rapido

int orientacion=0; 
int velocidad=0;
int direccion=0;
int detenerse=0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial1.begin(9600);
  //Serial2.begin(9600);
  
  pinMode(pin11,OUTPUT);
  pinMode(pin12,OUTPUT);
  pinMode(pin21,OUTPUT);
  pinMode(pin22,OUTPUT);
  pinMode(pin31,OUTPUT);
  pinMode(pin32,OUTPUT);
  pinMode(pin41,OUTPUT);
  pinMode(pin42,OUTPUT);  
  pinMode(pinLEDIZQUIERDA,OUTPUT);
  pinMode(pinLEDDERECHA,OUTPUT);
  pinMode(pinLEDTRASERA,OUTPUT);

  pinMode(pinOrientacion,INPUT);
  pinMode(pinDireccion,INPUT);
  pinMode(pinApagado,INPUT);
  pinMode(pinVelocidad,INPUT);
}

char dir = 'L';
char ori = 'U';
char vel = 'B';

void loop() {
  // put your main code here, to run repeatedly:
  
  detenerse = digitalRead(pinApagado);
  velocidad = digitalRead(pinVelocidad);
  direccion = digitalRead(pinDireccion);
  orientacion = digitalRead(pinOrientacion);
  
  if (Serial1.available() > 0) {
      char readed = Serial1.read();

      if (readed == 'L' || readed == 'R') {
          dir = readed;
          readed = '1';
      }

      if (readed == 'U' || readed == 'D') {
          ori = readed;
          readed = '1';
      }

      if (readed == 'B' || readed == 'S') {
          vel = readed;
          readed = '1';
      }

      Serial.print(ori);
      Serial.print("-");
      Serial.print(dir);
      Serial.print("-");
      Serial.println(vel);
  
      if (readed == '0') {
        apagado();
        digitalWrite(pinLEDIZQUIERDA,LOW);
        digitalWrite(pinLEDDERECHA,LOW);
        digitalWrite(pinLEDTRASERA,LOW);
      }
      else if (readed == '1') {

        if(vel=='B'){
          velocidad=100;
        }else if(vel == 'S') {
          velocidad=250;
        }
        
         if(dir=='L'){
          izquierda(velocidad, ori);
          digitalWrite(pinLEDIZQUIERDA,HIGH);
          digitalWrite(pinLEDDERECHA,LOW);
          digitalWrite(pinLEDTRASERA,LOW);
        }else if (dir=='R'){
          derecha(velocidad, ori); 
          digitalWrite(pinLEDIZQUIERDA,LOW);
          digitalWrite(pinLEDDERECHA,HIGH);
          digitalWrite(pinLEDTRASERA,LOW);
        }
      }
  }
}

void derecha(int velocidad, char readed){
    if(readed == 'U'){
    digitalWrite(13,LOW);
    digitalWrite(12,LOW);
    
    analogWrite(11,velocidad);
    digitalWrite(10,LOW);

    analogWrite(9,velocidad);
    digitalWrite(8,LOW);
    
    analogWrite(7,velocidad);
    digitalWrite(6,LOW);
  }else if (readed == 'D'){
    digitalWrite(12,LOW);
    digitalWrite(13,LOW);
    
    analogWrite(10,velocidad);
    digitalWrite(11,LOW);
    
    analogWrite(8,velocidad);
    digitalWrite(9,LOW);
    
    analogWrite(6,velocidad);
    digitalWrite(7,LOW);
  } 
}

void izquierda(int velocidad, char readed){
    if(readed=='U'){
      analogWrite(13,velocidad);
      digitalWrite(12,LOW);
      
      digitalWrite(11,LOW);
      digitalWrite(10,LOW);
      
      analogWrite(9,velocidad);
      digitalWrite(8,LOW);
      
      analogWrite(7,velocidad);
      digitalWrite(6,LOW);
    }else if (readed == 'D'){
      analogWrite(12,velocidad);
      digitalWrite(13,LOW);
      
      digitalWrite(10,LOW);
      digitalWrite(11,LOW);
      
      analogWrite(8,velocidad);
      digitalWrite(9,LOW);
      
      analogWrite(6,velocidad);
      digitalWrite(7,LOW);
    }
}
void apagado(){
  digitalWrite(13,LOW);
  digitalWrite(12,LOW);
  digitalWrite(11,LOW);
  digitalWrite(10,LOW);
  digitalWrite(9,LOW);
  digitalWrite(8,LOW);
  digitalWrite(7,LOW);
  digitalWrite(6,LOW);
}
