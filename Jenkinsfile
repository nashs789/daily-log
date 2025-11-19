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
          string(credentialsId: 'GOOGLE_CLIENT_SECRET', variable: 'GOOGLE_CLIENT_SECRET'),
          string(credentialsId: 'JWT_SECRET', variable: 'JWT_SECRET')
        ]) {
          sh '''
            set -eux
            ./gradlew clean test --no-daemon
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
    stage('Build WAR') {
      steps {
        sh '''
          set -eux
          ./gradlew clean bootWar --no-daemon
          ls -lh build/libs/*.war
        '''
      }
      post {
        success {
          archiveArtifacts artifacts: 'build/libs/*.war', fingerprint: true
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
        bash /opt/myapp/stop.sh || true
        for i in $(seq 1 20); do ss -ltn | grep -q ":8081 " || break; sleep 0.3; done

        WAR=$(ls build/libs/*.war | head -n1)
        cp -f "$WAR" /opt/myapp/app.war
        bash /opt/myapp/run.sh

        sleep 1
        pgrep -af 'java.*app.war' || (echo "App not running!" && exit 1)
      '''
    }
    }
  }
  post {
    always {
      sh 'tail -n 500 /opt/myapp/logs/app.out || true'
    }
  }
}
