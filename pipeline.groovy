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
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar') {
                    sh '/opt/maven/bin/mvn sonar:sonar'
                }
                echo 'Test Successfully'
            }
        }
        stage('Deploy') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'linux', path: '', url: 'http://3.1.194.195:8080/')], contextPath: '/', war: '**/*.war'
                echo 'Deploy Successfully'
            }
        }
    }
}
