* 백엔드 시작전에 mysql 로그인하고 해주세요 hibernate이 자동으로 모델에 있는 엔티티들을 테이블로 만들어줍니다. <br>
* 2/13 수정사항 백엔드 실행시 필요한 테이블을 자동으로 변경 및 생성하게 바꿨습니다. 만약 실행이 안된다면 기존 테이블을 전부다 드랍 후 실행 부탁드립니다.
* 위에 사항 때문에 실행할때마다 테이블이 초기화 될 것 입니다, 처음 실행하고 테이블 생성이 확인 됐으면 application/propertice에 들어가셔서 spring.jpa.hibernate.ddl-auto=create 를 spring.jpa.hibernate.ddl-auto=update 로 바꿔주세요.     경로 : src/main/resources/application.properties <br>
* <b>이미 다른분들이 연결에 성공한 API 코드는 수정 요청을 받을수 없습니다.</b><br>
------------------------------------------------------------------------<br>
~2/11 수정사항 <br>
1. 회원탈퇴 버그 수정해서 프론트 정상 동작 확인 <br>
2. 기존 회원정보 수정에서 나이는 업데이트 안되는 버그 수정 <br>
3. 도서등록 및 삭제 특정 도서 조회 API 추가 "이미지" 까지 불러올 수 있어야함 <br>
4. 비밀번호 찾기를 기존 이메일 입력에서 이메일 토큰 인증방식 으로 수정. <- 아마 교수님 요청입니다 
5. 관심분야 등록/삭제 추가 완료
6. 결제정보추가<br>
7. 2/13 14시 : 책등록,관심분야 관련 코드 수정되었습니다.<br>
------------------------------------------------------------------------<br>
~2/14 수정사항
* 회원 관심분야는 현재 풍자,디스토피아,고전,전후소설,동화 이렇게 있고 로직상 텍스트가 일치해야 동작 합니다. 프론트님들 참고 부탁드립니다! <br>
~2/18 수정사항
* 댓글 기능 추가(수정 삭제까지) , 검색 기능 추가(자동완성 등은 미완 서치만 가능함) 보완예정이므로 작업하실때 제외 부탁드립니다.,comments 테이블 추가<br>
~2/19 수정사항
* 도서 구매 및 주문 API 추가, 배송 현황 및 쿠폰 발급과 적용 관련 테이블 및 API 추가(단 이전에 했던 결제방식으로 하는건 프론트에서 부탁드립니다. 프론트에서 하는게 일반적이라고 하네요?? 잘 모름)<br>
* 장바구니 기능은 아직 없습니다. 
