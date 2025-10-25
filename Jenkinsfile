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
              set -Eeuo pipefail

              echo "[deploy] whoami=$(whoami)"
              echo "[deploy] target perms:"
              ls -ld /opt/myapp || true

              # 쓰기 권한 체크 (없으면 상세정보 출력 후 실패)
              if [ ! -w /opt/myapp ]; then
                echo "[deploy] No write permission to /opt/myapp"
                id || true
                exit 1
              fi

              # 안전 정지 및 포트 해제 대기
              bash /opt/myapp/stop.sh || true
              for i in $(seq 1 40); do
                if ! ss -ltn | grep -qE '[:\\.]8081\\b'; then break; fi
                sleep 0.25
              done

              # 최신 JAR 1개 선택
              JAR="$(ls -1t build/libs/*.jar | head -n1)"
              echo "[deploy] JAR=$JAR"
              test -f "$JAR"

              # 원자적 교체(임시파일 -> mv)
              cp -f "$JAR" /opt/myapp/app.jar.new
              mv -f /opt/myapp/app.jar.new /opt/myapp/app.jar

              # 기동
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
