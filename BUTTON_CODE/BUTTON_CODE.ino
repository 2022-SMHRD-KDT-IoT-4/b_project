#include <WiFi.h> // 와이파이 라이브러리
#include <HTTPClient.h> // HTTP 클라이언트 통신 라이브러리
#include "time.h" // 시간 동기화 라이브러리

const int buttonID = 101; // 버튼 고유 ID 번호
const String cloudServer = "https://api.thingspeak.com/update?api_key=ZHKXMUM9V3XFX6GD";
const String tomcatServer = "http://172.30.1.32:8081/ArduinoTest/getParameter";
const char* ntpServer = "pool.ntp.org";
const char* ssid = "Galaxy A90 5G2242";
const char* password = "SM-A908Nb851";
uint8_t timeZone = 9;
uint8_t summerTime = 0;

const int buttonPin = 19;
const int echo = 25;
const int trig = 26;
const int tempPin = 16;
const int humiPin = 17;
const int mdustPin = 18;
const int LED = 15;
const int solosen = 39;

String qustionInf;
boolean askState = false;
boolean breath = LOW;

boolean buttonState = LOW;
boolean saveButtonState = LOW;

float cycletime = 0;
float distance = 0;
int distanceState = 0;
int saveDistanceState = 0;
int actDat = 0;

float tempDat = 0;
float humiDat = 0;
float mdustDat = 0;
int avgTempDat = 0;
int avgHumiDat = 0;
int avgMdustDat = 0;
int avgTempCount = 1;
int avgHumiCount = 1;
int avgMdustCount = 1;

boolean flag1 = false;
boolean flag2 = false;
boolean flag3 = false;
boolean flag4 = false;
boolean flag5 = false;
boolean flag6 = false;
boolean getFlag = false;

void setup() {

  Serial.begin(115200);
  pinMode(buttonPin, INPUT);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);
  pinMode(LED,OUTPUT);
  pinMode(solosen,INPUT);
  
  delay(4000);
  WiFi.begin(ssid, password);
  Serial.printf("와이파이 연결 중 ...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.print(" 와이파이 연결 성공!\n");
  
  configTime(3600*timeZone, 3600*summerTime, ntpServer);
  Serial.print("시간 동기화 성공!\n");
  
}

void loop() {

  buttonState = digitalRead(buttonPin);

  distanceState = getDistanceValue();
  int act = (int) abs(saveDistanceState - distanceState);
  if(act > 40) {
    actDat += act;
  } else {}

  tempDat += random(0,45); // <- analogRead(tempPin);
  humiDat += random(0,100); // <- analogRead(humiPin);
  mdustDat += random(0,100); // <- analogRead(mdustPin);
  avgTempDat = tempDat/avgTempCount;
  avgHumiDat = humiDat/avgHumiCount;
  avgMdustDat = mdustDat/avgMdustCount;
  avgTempCount++;
  avgHumiCount++;
  avgMdustCount++;

  ledOnOff();

  if((WiFi.status() == WL_CONNECTED)) {

    if(buttonState == HIGH && saveButtonState == LOW) {

      if(!askState) {
        
        Serial.println("긴급 구원 요청을 전송하시겠습니까?"); // <- 음성 출력 코드
        delay(2000);
      
        int wait = 0;
        while(wait < 10) {
          if(digitalRead(buttonPin)) {
            sendData(1, "1");
            break;
          } else {}
          wait++;
          delay(1000);
        }
        
      } else if(askState && distanceState < 40 && breath == LOW) {
        
        breath = HIGH;
        Serial.println("식사하셨어요?"); // <-- 음성 출력 코드
        delay(2000);
        
        int wait = 0;
        while(wait < 10) {
          if(digitalRead(buttonPin)) {
            sendData(6, "1");
            break;
          } else {}
          wait++;
          delay(1000);
        }
        
      } else {}
      
    } else {

      sendData(2, (String) actDat);
      sendData(3, (String) avgTempDat);
      sendData(4, (String) avgHumiDat);
      sendData(5, (String) avgMdustDat);
      sendData(6, (String) buttonState);
      
    }
      
  }
    
  if(getLocalTime('N')%10 == 0) breath = LOW; else {}

  // 버튼 과거 상태 입력
  saveButtonState = buttonState;
  saveDistanceState = distanceState;
  Serial.print((String) getLocalTime('N') + "분 " + (String) getLocalTime('S') + "초 / 활동량 : "+(String)actDat);
  Serial.print(" / 온도 : "+(String) avgTempDat+" / 습도 : "+(String) avgHumiDat+" / 공기오염도 : "+(String) avgMdustDat);
  if(ledOnOff() == 1) {
    Serial.println(" / LED ON");
  } else {
    Serial.println(" / LED OFF");
  }
  
}

int getLocalTime(char c) {
  
  struct tm timeinfo;
  
  if(!getLocalTime(&timeinfo)){
    Serial.println("시간 가져오기 실패");
  } else if(c == 'Y') {
    return (timeinfo.tm_year+1900);
  } else if(c == 'M') {
    return (timeinfo.tm_mon+1);
  } else if(c == 'D') {
    return (timeinfo.tm_mday);
  } else if(c == 'H') {
    return (timeinfo.tm_hour);
  } else if(c == 'N') {
    return (timeinfo.tm_min);
  } else if(c == 'S') {
    return (timeinfo.tm_sec);
  } else {}
  
}

void sendData(int field, String dat) {
    
  if(getLocalTime('H')%2==0 && getLocalTime('N')%5==0) {
    
    HTTPClient http;
    
    if(!flag1&&field==1) {
      http.begin(cloudServer+"&field1="+dat);
      if(http.GET() > 0) {
        Serial.println("위급상황정보 전송 성공");
        flag1 = true; 
        Serial.println("위급상황정보 전송 체크 깃발 올림");
      } else {
        Serial.println("데이터 전송 실패");
      }
      http.end();
      delay(15000);
    } else if(!flag2&&field==2) {
      http.begin(cloudServer+"&field2="+dat);
      if(http.GET() > 0) {
        Serial.println("활동량 데이터 전송 성공");
        flag2 = true; 
        Serial.println("활동량 데이터 전송 체크 깃발 올림");
        actDat = 0;
      } else {
        Serial.println("데이터 전송 실패");
      }
      http.end();
      delay(15000);
    } else if(!flag3&&field==3) {
      http.begin(cloudServer+"&field3="+dat);
      if(http.GET() > 0) {
        Serial.println("온도 데이터 전송 성공");
        flag3 = true;
        Serial.println("온도 데이터 전송 체크 깃발 올림");
        tempDat = 0;
        avgTempCount = 1;
      } else {
        Serial.println("데이터 전송 실패");
      }
      http.end();
      delay(15000);
    } else if(!flag4&&field==4) {
      http.begin(cloudServer+"&field4="+dat);
      if(http.GET() > 0) {
        Serial.println("습도 데이터 전송 성공");
        flag4 = true;
        humiDat = 0;
        avgHumiCount = 1;
        Serial.println("습도 데이터 전송 체크 깃발 올림");
      } else {
        Serial.println("데이터 전송 실패");
      }
      http.end();
      delay(15000);
    } else if(!flag5&&field==5) {
      http.begin(cloudServer+"&field5="+dat);
      if(http.GET() > 0) {
        Serial.println("미세먼지 데이터 전송 성공");
        flag5 = true;
        mdustDat = 0;
        avgMdustCount = 1;
        Serial.println("미세먼지 데이터 전송 체크 깃발 올림");
      } else {
        Serial.println("데이터 전송 실패");
      }
      http.end();
      delay(15000);
    } else if(!flag6&&field==6) {
      http.begin(cloudServer+"&field6="+dat);
      if(http.GET() > 0) {
        Serial.println("질문응답정보 전송 성공");
        flag6 = true;
        Serial.println("질문응답정보 전송 체크 깃발 올림");
      } else {
        Serial.println("데이터 전송 실패");
      }
      http.end();
      delay(15000);
    } else {}
    
  } else if(getLocalTime('H')%2==1) {

    if(flag1) {
      flag1 = false;
      Serial.println("위급상황정보 전송 체크 깃발 내림");
    } else if(flag2) {
      flag2 = false;
      Serial.println("활동량 데이터 전송 체크 깃발 내림");
    } else if(flag3) {
      flag3 = false;
      Serial.println("온도 데이터 전송 체크 깃발 내림");
    } else if(flag4) {
      flag4 = false;
      Serial.println("습도 데이터 전송 체크 깃발 내림");
    } else if(flag5) {
      flag5 = false;
      Serial.println("미세먼지 데이터 전송 체크 깃발 내림");
    } else if(flag6) {
      flag6 = false;
      Serial.println("질문응답정보 전송 체크 깃발 내림");
    } else {}
    
  } else {}
    
}

void getData(int id) {
  
  if(getLocalTime('H')%2==1 && getLocalTime('N')%5==0) {

    HTTPClient http;
    
    if(!getFlag) {
      http.begin(tomcatServer+"?buttonID="+id);
      if(http.GET() > 0) {
        Serial.println("질문정보 업데이트 성공");
        getFlag = true;
        Serial.println("질문정보 업데이트 체크 깃발 올림");
      } else {
        Serial.println("질문정보 업데이트 실패");
      }
      qustionInf = http.getString();
      http.end();
      delay(15000);
    } else {}

  } else {
    
    if (getFlag) {
      getFlag = false;
      Serial.println("질문정보 업데이트 체크 깃발 내림");
    } else {
    }
    
  }
  
}

int getDistanceValue() {
  
  digitalWrite(trig, HIGH);
  delay(10);
  digitalWrite(trig, LOW);
  
  cycletime = pulseIn(echo, HIGH); 
  distance = ((340 * cycletime) / 10000) / 2;
  int result = random(0, 100); // <- distance;
  
  return result;
  
}

int ledOnOff() {

  if(random(0,1023) < 300) { // <- analogRead(solosen)
    digitalWrite(LED,1);
    return 1;
  }else{
    digitalWrite(LED,0);
    return 0;
  }
  
}
