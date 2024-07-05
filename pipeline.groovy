pipeline {
    agent any
    stages {
        stage('Pull') {
            steps {
                git 'https://github.com/Shantanu20000/studentapp-ui-jenkin-mvn.git'
                echo 'Pull Successfully'
            }
        }
        stage('Build') {
            steps {
                sh '/opt/maven/bin/mvn clean package'
                echo 'Build Successfully'
            }
        }
        stage('Test') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-creds') {
                    sh '/opt/maven/bin/mvn sonar:sonar'
                }
                echo 'Test Successfully'
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo 'Quality Gate Successfully'
            }
        }
        stage('Deploy') {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'linux', path: '', url: 'http://172.31.36.218:8080')], contextPath: '/', war: '**/*.war'
                echo 'Deploy Successfully'
            }
        }
    }
}
