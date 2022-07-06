<br>

# MVC 패턴 기반 주소록 관리 프로그램

> 📌 KH 정보교육원 첫 번째 프로젝트 (개인) <br>
> 2022.05.23 ~ 2022.07.08

<br>

## 1. 프로젝트 개요

- **MVC**(모델-뷰-컨트롤러) 디자인 패턴을 기반으로 비즈니스 로직과 화면을 분리하여 **재사용성**과 **독립성**을 높이고 결합도를 낮출 수 있으며, **단위테스트**를 가능하게 하여 효율적인 업무 처리가 가능하게 한다.
- Java(Eclipse) - Oracle 연동과 **JDBC**를 활용한 주소록 데이터베이스 관리
- 오라클서버와 연계하여 회원 정보 관리를 위한 입력, 수정, 삭제, 상세조회, 전체조회 처리(**CRUD** - Create, Retrieve, Update, Delete)
- 로그인(회원가입) - 메인화면 - 다이얼로그 GUI 구현

<br>

## 2. 요구 사항

- [x] 클래스 관계를 이해하고 적절한 위치에 **인스턴스화** 수행
- [x] **생성자**를 통해 클래스 사이의 의존관계를 코딩
- [x] 메뉴아이템과 버튼 이벤트 처리를 공통된 하나의 메소드로 처리
- [x] **오라클서버와 연계**하여 회원 정보 관리를 위한 입력, 수정, 삭제, 상세조회, 전체 조회를 처리
- [x] 자바에서 제공하는 **JDBC API**를 활용하여 오라클 서버에 필요한 요청을 수행
- [x] UI에 배치된 컴포넌트에 대한 이벤트 처리

<br>

## 3. 설계 과정

### 1) Oracle DB 설계 <br>  ◼ 데이터 모델링과 Oracle 시퀀스 생성(ID 번호 1씩 증가)
![DBERD](https://user-images.githubusercontent.com/99080986/177373122-5f73554e-007b-4702-b541-fcab477ca2d3.JPG)
![DB](https://user-images.githubusercontent.com/99080986/177373396-4ade51b2-b14b-4030-be80-fe75a3157428.JPG)

<br>

### 2) MVC 패턴 기반 클래스 설계
![MVC](https://user-images.githubusercontent.com/99080986/177373370-18cea6a1-4d53-42c4-be95-64c65c17211e.JPG)


  <br>
  
- **모델 계층** <br>: 총 4개의 클래스로서, 실제 방식보다 많이 쪼개놓음. DAO(Data Access Object), 데이터 접근 객체. Dao를 통해 DB를 연결하여 오직 DB연결 데이터를 가져오거나 삭제, 수정 등 CRUD 작업을 할 수 있다. 이렇게 DB연결 객체를 따로 만들어 관리하면 불필요한 작업량이 줄어든다.
- **뷰 계층** <br>: 화면그리기(UI)담당. 
- **컨트롤러** <br>: 업무처리를 위한 중계자 역할. 클라이언트에게 받은 요청을 수행할 로직을 제어하는 객체. 
- **VO(DTO)** <br>: 계층간 데이터 교환을 위한 객체. 프로세스 사이에서 데이터를 운반하는 객체. Dao나 Service처럼 로직을 가지고 있지 않고, data에 접근하기 위해 Getter Setter를 가지고있다. 즉, Dao가 DB에서 얻어온 데이터를 Service나 Controller로 보낼 때 사용하는 객체이다.
<br>

## 4. 주요 구현 기능 목록

| 구현 기능                               | 상세 기능 |
| --------------------------------------- | --------- |
| **로그인&회원가입**<br>(AddressLogin)   | * View와 Dao 계층 함께 구현<br> * 설계 테이블인 'mkaddrtb'의 DB를 활용<br> * 로그인 구현 : 사용자가 입력한 이름(ID)과 생년월일(PW)을 파라미터로 받아서 이름과 생일이 모두 일치하면 id번호 출력, 생일이 맞지 않으면 0반환, 이름이 존재하지 않으면 -1반환 > 반환된 결과를 equals() 메소드를 통해 이벤트 처리 + **AddressBook 호출** <br> * 회원가입 버튼 클릭 시, **ModifyDialof 호출**하여 정보 입력   |
| **메인화면**<br>(AddressBook)           | * 사용자의 선택된 정보를 VO로 넘기며 하위 클래스들과 연결되어 있음 <br> * 프로그램의 메인화면 UI구현(JFrame, DefaultTableModel ...) <br> * **입력, 수정, 삭제, 조회**에 대한 메뉴아이템 & 아이콘 클릭 시 이벤트 처리 <br> * 입력과 수정 작업 시 **ModifyDialog 호출**  |
| **다이얼로그, 팝업**<br>(ModifyDialog)  | * **입력, 수정, 상세조회, 회원가입 시** 호출되는 팝업창 <br> * 사용자가 원하는 메뉴를 선택 시 해당 다이얼로그를 보여줌. 하나의 개체를 가지고 각각의 입력, 수정, 상세조회를 처리할 수 있도록 함 |
| **읽기와 쓰기**<br>(AddressVO)          | * 값을 담을 수 있는 그릇과 같은 클래스 <br> * 변수의 접근제어자를 private으로 두어 외부에서 직접 접근하지 못하도록 하고 데이터를 보호할 수 있음. 인스턴스화를 통한 **직접 접근은 불가능**하며(private은 해당 클래스에서만 사용 가능), **Getter Setter 메소드를 통한 간접 접근**을 허용.  <br> * **Command 변수**를 통해 메소드 호출 |
| **컨트롤러**<br>(AddressCtrl)           | * 사용자가 어떤 요청을 했을 때, 5가지 요청에 대해 감지하는 역할 > 감지한 내용을 AddressVO 안의 command에 담기도록 함 |
| **입력**<br>(RegisterAddrEty)           | * Insert문을 통한 회원 정보 등록 구현 <br> * **등록날짜와 ID는 사용자에게 입력받지 않고 SQL문을 통해 자동 생성**되도록 구성|
| **수정**<br>(ModifyAddrEty)             |  * Update문을 통한 회원 정보 수정 구현 <br> * 수정이 완료되면 **등록 날짜도 현재 날짜와 시간으로 변경**되도록 SQL문을 추가함  |
| **삭제**<br>(DeleteAddrEty)             | * Delete문을 통한 회원 정보 삭제 구현 |
| **전체, 상세조회**<br>(RetrieveAddrEty) | * 회원정보 중 상세보기 구현 <br> * 회원 목록 전체 조회 구현(새로고침 시 재사용) |

![회원등록](https://user-images.githubusercontent.com/99080986/177595995-0ac95fb5-4f41-4405-b490-281b2da6b6d0.JPG) <br>
☝ **RegisterAddrEty 입력 담당 클래스의 회원등록 구현 코드** <br> **등록날짜와 ID(시퀀스)는 사용자에게 입력받지 않고 SQL문을 통해 자동 생성되도록 구성**


<br>

## 5. GUI 화면
![회원가입](https://user-images.githubusercontent.com/99080986/177594463-b8b5c8af-1bd8-4df4-8821-3fbf33fb8137.JPG)
![로그인완료](https://user-images.githubusercontent.com/99080986/177595735-4fb6700f-d65e-4745-90f8-14c4f4cc4ad3.JPG)
![전체조회](https://user-images.githubusercontent.com/99080986/177594652-2f8a6ab0-2a4e-4ba6-bd71-e31af5c1ffb5.JPG)
![상세조회](https://user-images.githubusercontent.com/99080986/177594680-fffadb6f-6229-40f1-9eb2-51ef6daa7ded.JPG)
![입력](https://user-images.githubusercontent.com/99080986/177594716-d7675655-b8b0-446a-99a6-cc9527429912.JPG)
![수정결과](https://user-images.githubusercontent.com/99080986/177595203-62dec250-c520-4d0b-a7fc-a92f1bb52be4.JPG)
![삭제](https://user-images.githubusercontent.com/99080986/177595235-c7a24b10-be01-4e5b-b757-03ff02830d8c.JPG)




<br>

## 6. 사용 기술 및 개발 환경

Back-end   `java` `Eclipse` `JDBC` <br>
Server  `Oracle` `Toad`
