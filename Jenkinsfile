pipeline {
    agent any

    environment {
        // Use descriptive names for credentials and keep them secret
        SONAR_TOKEN = credentials('real-estate-sonarqube-credentials')
        DOCKER_CREDENTIALS = credentials('real-estate-dockerhub-credentials')

        // Project-specific variables
        DOCKER_IMAGE = 'nabilsabi/real-estate-rental-backend'
        DOCKER_TAG = "${env.BUILD_NUMBER}"

        // SonarQube configuration
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_PROJECT_KEY = 'real-estate-sonarqube-jenkins'
        SONAR_PROJECT_NAME = 'Real Estate SonarQube Jenkins'

        // Source control
        GITHUB_REPO_URL = 'https://github.com/nabilsabi01/real-estate-rental-backend.git'
    }

    tools {
        maven 'Maven 3.9.9'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: "${env.GITHUB_REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat """
                        mvn sonar:sonar ^
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} ^
                        -Dsonar.projectName="${SONAR_PROJECT_NAME}" ^
                        -Dsonar.host.url=${SONAR_HOST_URL} ^
                        -Dsonar.token=${SONAR_TOKEN}
                    """
                }
            }
        }

       stage('Quality Gate') {
           steps {
               timeout(time: 5, unit: 'MINUTES') {
                   waitForQualityGate abortPipeline: true
               }
           }
           post {
               failure {
                   script {
                       def qg = waitForQualityGate()
                       echo "Quality Gate status: ${qg.status}"
                       if (qg.status != 'OK') {
                           error "Quality Gate failed: ${qg.status}"
                       }
                   }
               }
           }
       }

        stage('Docker Build') {
            steps {
                script {
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'real-estate-dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
                        bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                        bat "docker push ${DOCKER_IMAGE}:latest"
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
            echo "Check the GitHub repo for the code: ${env.GITHUB_REPO_URL}"
        }
        failure {
            echo 'Pipeline failed!'
            echo "Check the GitHub repo for the code: ${env.GITHUB_REPO_URL}"
        }
    }
}