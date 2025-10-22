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
          set -eux
          JAR=$(ls build/libs/*SNAPSHOT*.jar || ls build/libs/*.jar | head -n1)
          echo "Using $JAR"
          cp "$JAR" /opt/myapp/app.jar
          # 안전 재시작
          bash /opt/myapp/stop.sh || true
          bash /opt/myapp/run.sh
          sleep 2
          pgrep -af 'java.*app.jar' || (echo "App not running!" && exit 1)
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
