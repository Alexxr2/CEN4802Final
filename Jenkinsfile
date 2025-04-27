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

    stage('Calculator Deploy') {
      steps {
        sh """
          docker rm -f cen4802-calc || true
          docker run -d --name cen4802-calc -p 8082:8080 ${IMAGE}
          sleep 15
          docker exec cen4802-calc curl --fail http://localhost:8080/ || exit 1
        """
      }
    }

    stage('Integration Test') {
      steps {
        sh """
          docker rm -f cen4802-inttest || true
          docker run -d --name cen4802-inttest -p 8084:8080 ${IMAGE}
          sleep 10

          # call the calculator endpoint
          RESP=\$(docker exec cen4802-inttest \\
            curl -s -X POST http://localhost:8080/calculate \\
                 -d "a=7&b=8&op=%2B")

          echo "Integration test response:"
          echo "\$RESP"

          # check for the expected result and print a clear pass/fail
          if echo "\$RESP" | grep -q "Result: 15"; then
            echo "Integration Test: SUCCESS"
          else
            echo "Integration Test: FAILURE"
            exit 1
          fi
        """
      }
      post {
        always {
          sh 'docker rm -f cen4802-inttest || true'
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
