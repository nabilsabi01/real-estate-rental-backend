pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('real-estate-sonarqube-credentials')
        DOCKER_CREDENTIALS = credentials('real-estate-dockerhub-credentials')
        DOCKER_IMAGE = 'nabilsabi/real-estate-rental-backend'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_PROJECT_KEY = 'real-estate-sonarqube-jenkins'
        SONAR_PROJECT_NAME = 'Real Estate SonarQube Jenkins'
        GITHUB_REPO_URL = 'https://github.com/nabilsabi01/real-estate-rental-backend.git'
    }

    tools {
        maven 'Maven 3.9.9'
        jdk 'JDK17'
    }

    options {
        timeout(time: 2, unit: 'HOURS')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        ansiColor('xterm')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: "${env.GITHUB_REPO_URL}"
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean verify org.jacoco:jacoco-maven-plugin:report'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                    jacoco(
                        execPattern: '**/target/*.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/src/test*'
                    )
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat """
                        mvn sonar:sonar ^
                        -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                        -Dsonar.projectName="%SONAR_PROJECT_NAME%" ^
                        -Dsonar.host.url=%SONAR_HOST_URL% ^
                        -Dsonar.token=%SONAR_TOKEN% ^
                        -Dsonar.java.coveragePlugin=jacoco ^
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml ^
                        -Dsonar.exclusions=**/generated-sources/**,**/model/**,**/config/**
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
            post {
                failure {
                    script {
                        def qg = waitForQualityGate()
                        echo "Quality Gate status: ${qg.status}"
                        qg.conditions.each { condition ->
                            echo "${condition.metricKey} - ${condition.status}: ${condition.actualValue} ${condition.comparator} ${condition.errorThreshold}"
                        }
                    }
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    docker.withRegistry('', 'real-estate-dockerhub-credentials') {
                        def customImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                        customImage.push()
                        customImage.push("latest")
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                bat """
                    docker-compose -f docker-compose.staging.yml down
                    docker-compose -f docker-compose.staging.yml up -d
                """
            }
        }

        stage('Integration Tests') {
            steps {
                bat 'mvn verify -Dspring.profiles.active=staging'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
            bat 'msg %USERNAME% "Build Succeeded: ${env.JOB_NAME} ${env.BUILD_NUMBER}"'
        }
        failure {
            echo 'Pipeline failed!'
            bat 'msg %USERNAME% "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"'
        }
    }
}