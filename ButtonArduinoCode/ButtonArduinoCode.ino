#include <WiFi.h>
#include <HTTPClient.h>
#include <DHT.h>
#include "time.h"
#include "kp_dfplayer_mini.h"

DHT dht(4, DHT11);
kp_dfplayer_mini mp3(Serial2);
#define VOLUME 30

const int buttonID = 2;
const String cloudServer = "https://api.thingspeak.com/update?api_key=R59W39KN53FQOAYL";
const String tomcatServer = "http://172.30.1.35:8082/button_project_backup/EmergencyMailService.do";
const char* ntpServer = "pool.ntp.org";
const char* ssid = "iptime";
const char* password = "";
uint8_t timeZone = 9;
uint8_t summerTime = 0;

const int buttonPin = 19;
const int echo = 13;
const int trig = 12;
const int LED = 18;
const int solosen = 39;

boolean breath = LOW;
boolean buttonState = LOW;
boolean saveButtonState = LOW;
int con = 0;
int jokeNum = 0;

float cycletime = 0;
float distance = 0;
int distanceState = 0;
int saveDistanceState = 0;

int actDat = 0;
float tempDat = 0;
float humiDat = 0;
int avgTempDat = 20;
int avgHumiDat = 40;
int avgTempCount = 1;
int avgHumiCount = 1;

boolean flag2 = LOW;
boolean flag3 = LOW;
boolean flag4 = LOW;
boolean askFlag = LOW;

void setup() {
  
  Serial.begin(115200);
  Serial2.begin(9600);
  pinMode(buttonPin, INPUT);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);
  pinMode(LED, OUTPUT);
  pinMode(solosen, INPUT);
  mp3.set_volume(30);
  mp3player(4); // 안녕하세요
  delay(1000);
  mp3player(5); // 스마트 버튼 영웅이 입니다.
  delay(3000);
  mp3player(6); // 실행 준비 중입니다 잠시만 기다려 주세요.
  delay(3000);
  mp3player(7); // 준비 대기 음악
  
  WiFi.begin(ssid, password);
  Serial.printf("와이파이 연결 중 ...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.print(" 와이파이 연결 성공\n");
  
  configTime(3600*timeZone, 3600*summerTime, ntpServer);
  Serial.print("시간 동기화 성공\n");

  mp3.stop_mp3();

}

void loop() {
  
  buttonState = digitalRead(buttonPin);
  distanceState = getDistanceValue();
  actDat += getAct();
  getDHT();
  ledOnOff();
  setBreath();
  setFlag();
  
  if((WiFi.status()==WL_CONNECTED)) {

    if(buttonState==HIGH&&saveButtonState==LOW) emerg(); else {}
    if(millis()>30000&&getAct()==100&&breath==LOW) askq(); else {}
    sendData(2, (String) actDat);
    sendData(3, (String) avgTempDat);
    sendData(4, (String) avgHumiDat);
    
  }

  saveButtonState = buttonState;
  saveDistanceState = distanceState;
  getReport();

}

void getReport() {
  
  String report;
  
  report += (String) getLocalTime('H') + "시 ";
  report += (String) getLocalTime('N') + "분 ";
  report += (String) getLocalTime('S') + "초";
  report += " / 활동량 : " + (String) actDat;
  report += " / 거리 : " + (String) distanceState;
  report += " / 온도 : " + (String) avgTempDat;
  report += " / 습도 : " + (String) avgHumiDat;
  if(ledOnOff()==HIGH) report += " / LED ON"; else {}
  if(ledOnOff()==LOW) report += " / LED OFF"; else {}
  
  Serial.println(report);
  delay(1000);
  
}

void askq() {
  
  int w = 0;
  breath = HIGH;
  
  mp3.stop_mp3();
  con = random(0,2);
  askSelect();
  delay(2000);
        
  while(w < 10) {
    
    buttonState = digitalRead(buttonPin);
    
    if(buttonState == HIGH) {

      if(getLocalTime('H')==10||getLocalTime('H')==12||getLocalTime('H')==17
      ||getLocalTime('H')==18||getLocalTime('H')==22||getLocalTime('H')==23) {
        if(con==0) {
          mp3player(random(31,43));
          askFlag = HIGH;
          Serial.println("질문 체크 깃발 올림");
        } else if(con==1) {
          mp3player(random(51,58));
          askFlag = HIGH;
          Serial.println("질문 체크 깃발 올림");
        } else if(con==2) {
          jokeNum = random(61,65);
          mp3player(jokeNum);
          if(jokeNum==61) {
            delay(10000);
          } else if(jokeNum==63) {
            delay(5000);
          } else {
            delay(3000);
          }
          joke();
        } else {}
      } else {
        mp3player(11); // 띵동
        sendData(6, "1");
        mp3player(25); // 응답해주셔서 감사합니다
      }
      break;
      
    } else {}
    
    w++;
    Serial.println(w);
    delay(1000);
    
  }

}

void emerg() {
  
  int w = 0;
  buttonState = LOW;
  
  mp3.stop_mp3();
  mp3player(2); // 응답 신호를 전송하시려면 다시 한 번 눌러주세요.
  delay(2000);
        
  while(w < 10) {
    
    buttonState = digitalRead(buttonPin);
    
    if(buttonState == HIGH) {
      
      mp3player(11); // 띵동
      delay(1000);
      mp3player(3); // 응답 신호 전송중
      sendData(1, "1");
      delay(2000);
      mp3player(1); // 응답 신호가 전송되었습니다
      break;
      
    } else {}
    
    w++;
    Serial.println(w);
    delay(1000);
    
  }
  
}

void joke() {
  
  int w = 0;
  buttonState = LOW;
  
  mp3.stop_mp3();
  mp3player(27); // 정답이 궁금하시면 버튼을 눌러주세요.
  delay(2000);
        
  while(w < 10) {
    
    buttonState = digitalRead(buttonPin);
    
    if(buttonState == HIGH) {

      mp3player(28); // 정답은
      delay(2000);
      mp3player(jokeNum+10);
      delay(2000);
      mp3player(26); // 하하하
      askFlag = HIGH;
      Serial.println("질문 체크 깃발 올림");
      break;
      
    } else {}
    
    w++;
    Serial.println(w);
    delay(1000);
    
  }
  
}

void setBreath() {
  
  if(getLocalTime('N')%10==0&&getLocalTime('S')%10==0) breath = LOW; else {}
  
}

int getLocalTime(char c) {
  
  struct tm timeinfo;
  
  if(!getLocalTime(&timeinfo)){
    
    Serial.println("시간 가져오기 실패");
    
  } else if(c=='Y') {
    
    return (timeinfo.tm_year+1900);
    
  } else if(c=='M') {
    
    return (timeinfo.tm_mon+1);
    
  } else if(c=='D') {
    
    return (timeinfo.tm_mday);
    
  } else if(c=='H') {
    
    return (timeinfo.tm_hour);
    
  } else if(c=='N') {
    
    return (timeinfo.tm_min);
    
  } else if(c=='S') {
    
    return (timeinfo.tm_sec);
    
  } else {}
  
}

void sendData(int field, String dat) {

  HTTPClient http;

  if(field==1) {
    
    http.begin(tomcatServer+"?button_id="+buttonID+"&emergency_check=1");
    
    if(http.GET() > 0) {
      
      Serial.println("위급상황정보 문자&이메일 전송 성공");
      
    } else {
      
      Serial.println("데이터 전송 실패");
      
    }
    
    http.end();
    http.begin(cloudServer+"&field1="+dat);
    
    if(http.GET() > 0) {
      
      Serial.println("위급상황정보 클라우드 서버 전송 성공");
      
    } else {
      
      Serial.println("데이터 전송 실패");
      
    }
    
    http.end();
    delay(5000);
    
  } else if(field==6) {
    
    http.begin(cloudServer+"&field6="+dat);
    
    if(http.GET() > 0) {
      
      Serial.println("질문응답정보 전송 성공");
      askFlag = HIGH;
      Serial.println("질문응답 체크 깃발 올림");
      
    } else {
      
      Serial.println("데이터 전송 실패");
      
    }
    
    http.end();
    delay(5000);
    
  } else {}
    
  if(getLocalTime('H')%2==0&&getLocalTime('N')%5==0) {
    
    if(!flag2&&field==2) {
      
      http.begin(cloudServer+"&field2="+dat);
      
      if(http.GET() > 0) {
        
        Serial.println("활동량 데이터 전송 성공");
        flag2 = HIGH;
        Serial.println("활동량 데이터 전송 체크 깃발 올림");
        actDat = 0;
        
      } else {
        
        Serial.println("데이터 전송 실패");
        
      }
      
      http.end();
      delay(5000);
      
    } else if(!flag3&&field==3) {
      
      http.begin(cloudServer+"&field3="+dat);
      
      if(http.GET() > 0) {
        
        Serial.println("온도 데이터 전송 성공");
        flag3 = HIGH;
        Serial.println("온도 데이터 전송 체크 깃발 올림");
        tempDat = 0;
        avgTempCount = 1;
        
      } else {
        
        Serial.println("데이터 전송 실패");
        
      }
      
      http.end();
      delay(5000);
      
    } else if(!flag4&&field==4) {
      
      http.begin(cloudServer+"&field4="+dat);
      
      if(http.GET() > 0) {
        
        Serial.println("습도 데이터 전송 성공");
        flag4 = HIGH;
        Serial.println("습도 데이터 전송 체크 깃발 올림");
        humiDat = 0;
        avgHumiCount = 1;
        
      } else {
        
        Serial.println("데이터 전송 실패");
        
      }
      
      http.end();
      delay(5000);
      
    } else {}
    
  } else {}

}

void setFlag() {

  if(getLocalTime('H')%2==1&&getLocalTime('N')%5==0&&getLocalTime('S')%10==0) {
    
    if(flag2) {flag2 = LOW; Serial.println("활동량 데이터 전송 체크 깃발 내림");} else {}
    if(flag3) {flag3 = LOW; Serial.println("온도 데이터 전송 체크 깃발 내림");} else {}
    if(flag4) {flag4 = LOW; Serial.println("습도 데이터 전송 체크 깃발 내림");} else {}
    
  } else if(getLocalTime('N')%60==59&&getLocalTime('S')%10==0) {
    
    if(askFlag) {askFlag = LOW; Serial.println("질문 체크 깃발 내림");} else {}
    
  } else {}
  
}

int getDistanceValue() {

  digitalWrite(trig, LOW);
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
  cycletime = pulseIn(echo, HIGH); 
  distance = cycletime * 17 / 1000; 

  if(distance > 100) distance = 100; else {}
    
  int result = distance;
  return result;
  
}

boolean ledOnOff() {

  if(analogRead(solosen) > 4094) { 
    
    digitalWrite(LED,HIGH);
    return HIGH;
    
  } else {
    
    digitalWrite(LED,LOW);
    return LOW;
    
  }
  
}

void getDHT() {

  float temp = dht.readTemperature();
  float humi = dht.readHumidity();

  if(temp > 45 || temp < 0) temp = avgTempDat; else {}
  if(humi > 85 || humi < -15) humi = avgHumiDat; else {}
  
  tempDat += temp;
  humiDat += humi;
  avgTempDat = tempDat/avgTempCount;
  avgHumiDat = humiDat/avgHumiCount;
  avgTempCount++;
  avgHumiCount++;
  
}

int getAct() {

  int act = (int) abs(saveDistanceState - distanceState);
  if(act > 30) return 100; else {}
  return 0;
  
}

void mp3player(int value) {

  // 라이브러리 교체해야
  mp3.set_volume(VOLUME);
  mp3.play_mp3(1, value);
  delay(100);
  mp3.play_mp3(1, value);
  delay(100);
  mp3.play_mp3(1, value);
  delay(100);
  mp3.play_mp3(1, value);
  Serial.println((String)value + "번 재생");
      
}

void askSelect() {

  if(!askFlag&&getLocalTime('H')==7) {
    mp3player(12); // 안녕히 주무셨어요?
  } else if(!askFlag&&getLocalTime('H')==8) {
    mp3player(8); // 식사하셨어요?
  } else if(!askFlag&&getLocalTime('H')==9) {
    mp3player(13); // 아침 약은 드셨나요?
  } else if(!askFlag&&getLocalTime('H')==11) {
    mp3player(14); // 지금 아픈 곳은 없으신가요?
  } else if(!askFlag&&getLocalTime('H')==13) {
    mp3player(15); // 점심식사는 하셨나요?
  } else if(!askFlag&&getLocalTime('H')==14) {
    mp3player(18); // 오늘 운동 하셨나요?
  } else if(!askFlag&&getLocalTime('H')==15) {
    mp3player(17); // 피곤하진 않으세요?
  } else if(!askFlag&&getLocalTime('H')==16) {
    mp3player(16); // 오늘 친구는 만나셨나요?
  } else if(!askFlag&&getLocalTime('H')==19) {
    mp3player(19); // 저녁 식사는 하셨나요?
  } else if(!askFlag&&getLocalTime('H')==20) {
    mp3player(20); // 저녁 약은 드셨나요?
  } else if(!askFlag&&getLocalTime('H')==21) {
    mp3player(21); // 안녕히 주무세요
  } else if(!askFlag&&con==0) {
    mp3player(23); // 노래 한곡 들으시겠습니까?
  } else if(!askFlag&&con==1) {
    mp3player(24); // 놀라운 사실 하나 알려드릴까요?
  } else if(!askFlag&&con==2) {
    mp3player(29); // 농담 하나 들려드릴까요?
  } else {}

}
