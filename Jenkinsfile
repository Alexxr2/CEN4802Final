pipeline {
  agent any
  tools { maven 'M1' }
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
        sh 'mvn clean package'
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t ${IMAGE} ."
      }
    }

    stage('Smoke Test & Deploy') {
      steps {
        sh """
          # Tear down any old calculator container
          docker rm -f cen4802-calc || true

          # Run your calculator WAR in Tomcat, mapping host 8082 → container 8080
          docker run -d --name cen4802-calc -p 8082:8080 ${IMAGE}

          # Give Tomcat time to start
          sleep 15

          # Verify the root endpoint responds
          docker exec cen4802-calc curl --fail http://localhost:8080/ || exit 1
        """
      }
      // NO post { always { docker rm -f cen4802-calc } }
      // —we leave cen4802-calc running so you can inspect it in Docker Desktop.
    }

    stage('Integration Test') {
      steps {
        sh """
          # Run a separate short-lived container for end-to-end testing
          docker rm -f cen4802-inttest || true
          docker run -d --name cen4802-inttest -p 8084:8080 ${IMAGE}

          # Wait for Tomcat
          sleep 10

          # POST 7+8 and capture the HTML response
          RESP=\$(docker exec cen4802-inttest \
            curl -s -X POST http://localhost:8080/calculate \
              -d 'a=7&b=8&op=%2B')

          echo "Integration test response:"
          echo "\$RESP"

          # Look for "Result: 15" in the HTML
          echo "\$RESP" | grep -q "Result: 15" || exit 1
        """
      }
      post {
        always {
          # Clean up the test container no matter what
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
