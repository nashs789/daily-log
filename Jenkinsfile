pipeline {
  agent any
  tools { jdk 'jdk21' }
  stages {
    stage('Verify Java') {
      steps { sh 'echo $JAVA_HOME && java -version && ./gradlew -version' }
    }
  }
  options {
    timestamps()
    ansiColor('xterm')
  }
  environment {
    GRADLE_OPTS = '-Dorg.gradle.jvmargs="-Xmx1g"'
    SPRING_PROFILES_ACTIVE = 'test'
  }
  stages {
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
      steps {
        sh '''
          set -eux
          ./gradlew clean test --no-daemon
          echo "==== JUnit XML files ===="
          find . -type f -name "TEST-*.xml" -maxdepth 8 -print | sed 's/^/  /' || true
        '''
      }
      post {
        always {
          junit 'build/test-results/test/*.xml'
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
      when { branch 'main' }
      steps {
        sh '''
          set -eux
          JAR=$(ls build/libs/*SNAPSHOT*.jar || ls build/libs/*.jar | head -n1)
          echo "Using $JAR"
          cp "$JAR" /opt/myapp/app.jar
          # 안전 재시작
          /opt/myapp/stop.sh || true
          /opt/myapp/run.sh
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
