pipeline {
  agent any

  tools {
    maven 'M1'
  }

  environment {
    IMAGE = "cen4802:${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        // run tests and package in one go
        sh 'mvn clean test package'
      }
      post {
        always {
          // publish surefire XML reports so you get test results in Jenkins
          junit 'target/surefire-reports/*.xml'
        }
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
          # remove any old instance
          docker rm -f cen4802-calc || true

          # start your calculator image on host:8082 → container:8080
          docker run -d --name cen4802-calc -p 8082:8080 ${IMAGE}

          # give Tomcat time to boot
          sleep 15

          # verify it’s up
          docker exec cen4802-calc curl --fail http://localhost:8080/ || exit 1
        """
      }
    }

    stage('Integration Test') {
      steps {
        sh """
          # tear down any old integration-test container
          docker rm -f cen4802-inttest || true

          # start a fresh one
          docker run -d --name cen4802-inttest -p 8084:8080 ${IMAGE}
          sleep 10

          # POST to /calculate?a=7&b=8&op=%2B
          RESP=\$(docker exec cen4802-inttest \\
            curl -s -X POST http://localhost:8080/calculate \\
                 -d "a=7&b=8&op=%2B")

          echo "Integration test response:"
          echo "\$RESP"

          # clearly indicate success or failure
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
