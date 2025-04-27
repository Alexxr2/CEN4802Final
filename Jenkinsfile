pipeline {
  agent any
  tools { maven 'M1' }
  environment {
    IMAGE = "cen4802:${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t ${IMAGE} ."
      }
    }

    stage('Smoke Test') {
      steps {
        // Run your WAR in Tomcat on host port 8082 instead of 8080
        sh """
          docker run -d --name smoke-test -p 8083:8080 ${IMAGE}
          # give the server a few seconds to start up
          sleep 8
          # hit the root URL to verify the app responds
          curl --fail http://localhost:8082/ || exit 1
          # optionally hit the calculator form:
          # curl --fail "http://localhost:8082/?a=3&b=4&op=%2B" || exit 1
        """
      }
      post {
        always {
          // tear it down no matter what
          sh 'docker rm -f smoke-test || true'
        }
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/*.war', fingerprint: true
    }
  }
}
