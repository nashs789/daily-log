pipeline {
  agent any
  tools { jdk 'jdk21' }
  options {
    timestamps()
    ansiColor('xterm')
  }
  environment {
    GRADLE_OPTS = '-Dorg.gradle.jvmargs="-Xmx1g"'
  }
  stages {
    stage('Verify Java') {
      steps { sh 'echo $JAVA_HOME && java -version && ./gradlew -version' }
    }
    stage('Verify Docker') {
      steps {
        sh '''
          set -eux
          docker version
          docker ps >/dev/null
          id $(whoami) | grep docker
        '''
      }
    }
    stage('Checkout') {
      steps {
        checkout scm
        sh 'git rev-parse --short HEAD'
      }
    }
    stage('Verify Java & Docker') {
      steps {
        sh '''
          set -eux
          java -version
          docker version
          docker ps >/dev/null
        '''
      }
    }
    stage('Test (with Testcontainers)') {
      environment { SPRING_PROFILES_ACTIVE = 'test' }
      steps {
        withCredentials([
          string(credentialsId: 'GOOGLE_CLIENT_ID',     variable: 'GOOGLE_CLIENT_ID'),
          string(credentialsId: 'GOOGLE_CLIENT_SECRET', variable: 'GOOGLE_CLIENT_SECRET')
        ]) {
          sh '''
            set -eux
            ./gradlew clean test --no-daemon
            echo "====== JUnit XML files ======"
            find . -type f -name "TEST-*.xml" -maxdepth 8 -print | sed 's/^/  /' || true
          '''
        }
      }
      post {
        always {
          junit testResults: '**/build/**/test-results/**/*.xml', allowEmptyResults: true
        }
      }
    }
    stage('Build Jar') {
      steps {
        sh '''
          set -eux
          ./gradlew bootJar --no-daemon
          ls -lh build/libs/*.jar
        '''
      }
      post {
        success {
          archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
        }
      }
    }
    stage('Deploy to /opt/myapp') {
      when {
      anyOf {
        expression { env.GIT_BRANCH == 'origin/main' }
      }
    }
    environment {
        SPRING_PROFILES_ACTIVE = 'prod'
    }
      steps {
        sh '''
          set -euo
          echo "[deploy] whoami=$(whoami)"
          ls -ld /opt/myapp || true

          # 안전 정지 및 포트 해제 대기
          bash /opt/myapp/stop.sh || true
          i=0
          while ss -ltn | grep -qE '[:\\.]8081\\b'; do
            i=$((i+1)); [ "$i" -ge 40 ] && break
            sleep 0.25
          done

          echo "[deploy] build/libs 목록:"
          ls -lh build/libs || true

          # 최신 Spring Boot 실행 JAR 선택 ( -plain / sources / javadoc 제외 )
          JAR="$(ls -1t build/libs/*.jar 2>/dev/null | grep -Ev '(-plain|sources|javadoc)\\.jar$' | head -n1 || true)"
          [ -n "$JAR" ] && [ -f "$JAR" ] || { echo "[deploy] Boot JAR을 찾지 못했습니다."; exit 1; }
          echo "[deploy] picked: $JAR"

          # Boot Loader 존재로 실행 JAR 검증 (plain.jar 방지)
          if ! jar tf "$JAR" | grep -q 'org/springframework/boot/loader/'; then
            echo "[deploy] 선택된 파일이 실행 JAR이 아닙니다(아마 -plain.jar). 빌드 설정을 확인하세요."
            exit 1
          fi

          # 원자적 교체
          cp -f "$JAR" /opt/myapp/app.jar.new
          mv -f /opt/myapp/app.jar.new /opt/myapp/app.jar

          # 기동
          sleep 1
          bash /opt/myapp/run.sh

          # 기동 확인
          sleep 2
          pgrep -af 'java.*app.jar'
        '''
      }
    }
  }
  post {
    always {
      sh 'tail -n 200 /opt/myapp/logs/app.out || true'
    }
  }
}
