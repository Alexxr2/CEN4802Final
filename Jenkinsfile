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

    stage('Smoke Test & Deploy') {
      steps {
        sh """
          # remove any old instance so 'docker run' won't error
          docker rm -f cen4802-calc || true

          # start your calculator image, keep it running on host:8082→container:8080
          docker run -d --name cen4802-calc -p 8082:8080 ${IMAGE}

          # give Tomcat a bit to boot
          sleep 15

          # verify it’s up (inside the container we hit localhost:8080)
          docker exec cen4802-calc curl --fail http://localhost:8080/ || exit 1
        """
      }
      // NO post { always { docker rm -f cen4802-calc }}  => leaves container running
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/*.war', fingerprint: true
    }
  }
}
