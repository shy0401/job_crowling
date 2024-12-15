프로젝트 빌드하기
npm install 실행

job_crowling/src/main/java/com.example.demo/DemoApplication 실행

-> http://localhost:8080/ 에서 postman을 통해 테스트 가능합니다.
-> JCLOUD 서버를 통해 POSTMAN 테스트를 위해서는 아래 설명을 참고해주세요.


<회원가입 : POST> </auth/register>
http://113.198.66.75:10099/auth/register

요청
<body>
{
    "email": "user2@example.com",
    "password": "password123",
    "name": "User Two"
}

응답
->회원가입 완료되었습니다

--------------------------------------
<로그인 : POST> </auth/login>
http://113.198.66.75:10099/auth/login

요청
<body>
{
    "email": "user2@example.com",
    "password": "password123",
    "name": "User Two"
}


응답
->
{
    "message": "로그인이 성공적으로 완료되었습니다.",
    "refreshToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMkBleGFtcGxlLmNvbSIsImlhdCI6MTczNDI1NTYwMCwiZXhwIjoxNzM0ODYwNDAwfQ._xHRahLZM_y0-ZRjsvOEM8TBR_8dOa_jzoWx9xBE5pBQQTdLASdzay7S--fuWzPbg04KzVJGBUlm3fHzVGhrCw",
"accessToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMkBleGFtcGxlLmNvbSIsImlhdCI6MTczNDI1NTYwMCwiZXhwIjoxNzM0MzQyMDAwfQ.xJYNopg48mv1QjihwGVGBviGqR9SKGSPDMgoL6QAFOaE_m4i71tk5d5zbv0b-_oNcrQAyPm70nFTGb8fwWNXYA"
}

--------------------------------------
<토큰 재발급 : POST> </auth/refresh>
http://113.198.66.75:10099/auth/refresh

요청
<Authorization> Auth Type을 Bearer Token으로 하고 로그인 때 발급받은 refrehToken을 넣는다.

<body> 바디에 json 형식으로 refreshToken을 입력 후 Send
{
"refreshToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMkBleGFtcGxlLmNvbSIsImlhdCI6MTczNDI1NzQ1MywiZXhwIjoxNzM0ODYyMjUzfQ.kQc8aMwXzJVHP7f643lnAN34md2xPiEQLqsdD02DJcF3zi5Eyz2QnyeCFsgBuU_5DLZQjMrtX0Iyn3fGMtIyHw"
}

-> 새로운 accessToken 발급
{
"accessToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMkBleGFtcGxlLmNvbSIsImlhdCI6MTczNDI1NzY1MSwiZXhwIjoxNzM0MzQ0MDUxfQ.jOes3p8SMQQ1ykTcPcOYgRiLSr5XTHkSVxZBmu0BAOo5iPxWqaZu_2cIQivHPs0maOBGnx7SFmLhr7fiX8Wpug"
}

--------------------------------------
<정보수정 : PUT> </auth/profile>
요청
헤더에 accessToken을 두고바디에 요청

<body>
{
    "name": "New User Name",
    "password": "UpdatedPassword123"
}

->회원 정보가 수정되었습니다.

--------------------------------------
<지원하기 : POST> /applications
http://113.198.66.75:10099/applications
<accessToken> 그대로


요청
<body>
{
    "jobId": 12,
    "resume": "This is my resume for the job 5."
}

->
{
    "applicationId": 3,
    "message": "지원이 성공적으로 제출되었습니다."
}


--------------------------------------
<지원한거 보기 : GET> /applications
http://113.198.66.75:10099/applications
<accessToken> 그대로

-> 
![image](https://github.com/user-attachments/assets/62d3f408-14d7-425a-b68e-491059a21faf)

--------------------------------------
(GET /jobs)
http://113.198.66.75:10099/jobs

->
{
    "content": [
        {
            "id": 1,
            "company": "Example Company",
            "title": "Software Engineer",
            "location": "Seoul",
            "experience": "신입",
            "education": "학사 이상",
            "employmentType": "정규직",
            "link": null
        }... 등등

