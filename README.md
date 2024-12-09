<h1>KPS_AI: 딥러닝을 활용한 스마트 정육 솔루션</h1>
<p><strong>김박신 팀의 살더하기 솔루션 개발 프로젝트</strong><br>
정육 공정의 자동화를 통해 생산성을 높이고, 품질 관리를 개선하기 위한 AI 기반 솔루션입니다.</p>

<hr>

<h2>📚 프로젝트 개요</h2>
<h3>문제 정의</h3>
<ul>
  <li>LA갈비의 지방 손질 과정은 전적으로 수작업으로 진행되어 다음과 같은 문제가 발생합니다:
    <ul>
      <li>인건비 증가</li>
      <li>품질 관리 어려움</li>
      <li>작업자의 안전 문제</li>
    </ul>
  </li>
</ul>
<h3>해결책</h3>
<ul>
  <li>YOLOv5 기반의 이미지 세그먼테이션 모델을 활용하여 <strong>살코기 영역 검출</strong> 및 자동화된 정육 공정을 구현.</li>
  <li><strong>실시간 스트리밍</strong> 환경에서 예측 및 시각화를 지원.</li>
</ul>

<hr>

<h2>🔑 주요 기능</h2>
<ol>
  <li><strong>이미지 세그먼테이션</strong>
    <ul>
      <li>LA갈비 이미지에서 살코기 영역을 정확히 탐지.</li>
      <li>YOLOv11 모델 기반 탐지 정확도 94%.</li>
    </ul>
  </li>
  <li><strong>웹 서버 및 클라우드 연동</strong>
    <ul>
      <li>Spring Boot를 활용한 <strong>관리자 웹 포털</strong> 개발.</li>
      <li>Flask 기반 AI 서버로 실시간 스트리밍 및 분석 제공.</li>
      <li>AWS S3와 MySQL을 통해 데이터 저장 및 관리.</li>
    </ul>
  </li>
  <li><strong>효율적인 데이터 처리</strong>
    <ul>
      <li>폴리곤 어노테이션 방식을 통한 데이터 전처리.</li>
      <li>YOLO 모델 파인튜닝 및 최적화를 통해 탐지 속도 62.5ms 확보.</li>
    </ul>
  </li>
</ol>

<hr>

<h2>🛠️ 사용 기술</h2>
<ul>
  <li><strong>프레임워크</strong>:
    <ul>
      <li>웹 서버: Java, Spring Boot, JPA</li>
      <li>AI 서버: Python, Flask</li>
    </ul>
  </li>
  <li><strong>모델 개발</strong>: YOLOv5, YOLOv11, PyTorch, OpenCV</li>
  <li><strong>데이터베이스</strong>: MySQL</li>
  <li><strong>클라우드 서비스</strong>: AWS (S3, EC2)</li>
  <li><strong>기타 도구</strong>: Labelme, Linux</li>
</ul>

<hr>

<h2>📈 프로젝트 성과</h2>
<ul>
  <li><strong>한국정보처리학회 학술 발표 논문 게재</strong></li>
  <li><strong>한국폴리텍대학 작품 경진대회 수상</strong>:
    <ul>
      <li>아산캠퍼스 1등</li>
      <li>충청권역 4등</li>
    </ul>
  </li>
  <li>YOLOv11 모델 성능:
    <ul>
      <li>mAP: 94.1%</li>
      <li>객체 탐지 속도: 62.5ms</li>
    </ul>
  </li>
</ul>

<hr>

<h2>🚀 실행 방법</h2>
<h3>1. 클론 및 설치</h3>
<pre>
git clone https://github.com/seongraeNaver/KPS_AI.git
cd KPS_AI
</pre>
<h3>2. 백엔드 서버 실행 (Spring Boot)</h3>
<pre>
cd src/main/
mvn spring-boot:run
</pre>
<h3>3. AI 서버 실행 (Flask)</h3>
<pre>
cd AI_Server/
python app.py
</pre>

<hr>

<h2>📌 향후 개선점</h2>
<ul>
  <li>다양한 육류 부위를 포함한 데이터셋 확장.</li>
  <li>실시간 스트리밍 환경에서 지연 시간을 줄이기 위한 IO 최적화.</li>
  <li>불특정 작업 환경에서의 추가 검증 및 성능 개선.</li>
</ul>

<hr>

<h2>👨‍💻 개발자</h2>
<ul>
  <li><strong>팀원 이름</strong>: 팀 김박신(김성래, 박상인)</li>
  <li><strong>문의 사항</strong>: seongraeNaver@example.com</li>
</ul>
