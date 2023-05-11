# Fassto Fulfillment Platform

## Data Collection Service
외부로부터 들어오는 데이터를 모으고 validation을 진행합니다. 모은 데이터를 카프카에 담습니다.

## 프로젝트 구조

<img width="1095" alt="스크린샷 2023-04-26 오전 10 52 15" src="https://user-images.githubusercontent.com/66578746/234445597-f803f7a2-e652-46c1-b2ca-e2d06bfc0531.png">

## 카프카
일반 큐를 사용해도 되지만, **고가용성**을 위해 카프카를 사용합니다! (게다가 오픈 소스)

#### 고가용성

1.레플리카

파티션의 복제본 <br/>
복제 수(replication factor)만큼 파티션의 복제본이 각 브로커에 생긴다.

2.리더와 팔로워

리더 : 프로듀서와 컨슈머는 리더를 통해서만 메세지를 처리함 <br/>
팔로워 : 리더로부터 복제만 받음 <br/>
리더가 속해있는 브로커가 장애나면, 다른 팔로워 중 하나가 리더가 된다 <br/>

#### Topic

센터이름을 Topic으로 결정 (순차 처리를 위함)

