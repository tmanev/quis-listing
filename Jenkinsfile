pipeline {
    agent any

    stages {
        stage('Build, Test and Package') {
            steps {
                echo 'Building...............'
                sh "mvn clean package"
            }
        }
    }
}