pipeline {
    agent any

    environment {
        SONAR_CREDENTIALS = 'real-estate-sonarqube-credentials'  // SonarQube credentials ID
        DOCKER_CREDENTIALS = 'real-estate-dockerhub-credentials' // Docker Hub credentials ID
        DOCKER_IMAGE = 'nabilsabi/real-estate-rental-backend'    // Docker image name with your Docker Hub username
        DOCKER_TAG = "${env.BUILD_NUMBER}"                       // Tag the Docker image with the build number
        SONAR_HOST_URL = 'http://localhost:9000'                 // SonarQube server URL (adjust as needed)
        SONAR_PROJECT_KEY = 'real-estate-app'                    // SonarQube project key
        GIT_REPO = 'https://github.com/nabilsabi01/real-estate-rental-backend.git'  // Your GitHub repository link
    }

    tools {
        maven 'Maven 3.8.4'  // Use Maven for the build
        jdk 'JDK 17'         // Use JDK 17 for the Java project
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from your GitHub repository
                git branch: 'main', url: "${env.GIT_REPO}"
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'  // Build the project and skip tests (use bat for Windows)
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'  // Run unit tests using bat command
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'  // Publish JUnit test reports
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    // Run SonarQube analysis with credentials and project key
                    bat """
                        mvn sonar:sonar ^
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} ^
                        -Dsonar.host.url=${SONAR_HOST_URL} ^
                        -Dsonar.login=${SONAR_CREDENTIALS}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true  // Wait for SonarQube Quality Gate result
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    bat "docker login -u %DOCKER_USER% -p %DOCKER_PASSWORD%" // Docker credentials passed via env variables
                    bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}" // Push the tagged image
                    bat "docker push ${DOCKER_IMAGE}:latest"  // Push the latest tag
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                // Use Docker Compose to deploy to the staging environment (Windows-compatible commands)
                bat """
                    docker-compose -f docker-compose.staging.yml down
                    docker-compose -f docker-compose.staging.yml up -d
                """
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                // Run integration tests on the staging environment
                bat 'mvn verify -Dspring.profiles.active=staging'
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'main'  // Deploy to production only from the main branch
            }
            steps {
                input 'Deploy to production?'  // Manual confirmation before deploying to production
                bat """
                    docker-compose -f docker-compose.prod.yml down
                    docker-compose -f docker-compose.prod.yml up -d
                """
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean up the workspace after the pipeline completes
        }
        success {
            echo 'Pipeline succeeded!'  // Success message
            echo "Check the GitHub repo for the code: ${env.GIT_REPO}"  // Print the GitHub repo link
        }
        failure {
            echo 'Pipeline failed!'  // Failure message
            echo "Check the GitHub repo for the code: ${env.GIT_REPO}"  // Print the GitHub repo link
        }
    }
}
