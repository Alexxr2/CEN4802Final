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
        sh """
          docker run -d --name smoke-test -p 8083:8080 ${IMAGE}
          sleep 15
          docker exec smoke-test curl --fail http://localhost:8080/ || exit 1
        """
      }
      post {
        always {
          sh 'docker rm -f smoke-test || true'
        }
      }
    }
  } // <-- closes stages

  post {
    always {
      archiveArtifacts artifacts: 'target/*.war', fingerprint: true
    }
  }
}
