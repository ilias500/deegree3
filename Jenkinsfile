pipeline {
    agent any

    tools {
        maven 'Maven 3.6'
        jdk 'JDK 1.8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
                sh 'mvn -version'
                sh 'java -version'
                sh 'git --version'
                sh 'docker --version'
            }
        }
        stage ('Build') {
            steps {
               echo 'Unit testing'
               sh 'mvn -B -C -fae clean test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage ('Integration Test') {
            steps {
                echo 'Integration testing'
                sh 'mvn -B -C -fae -Dskip.unit.tests=true verify -Pintegration-tests'
            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }
        stage ('Quality Checks') {
            steps {
                echo 'Quality checking'
                sh 'mvn -B -C -fae site -Psite-all-reports'
            }
            post {
                success {
                    cobertura autoUpdateHealth: false, autoUpdateStability: false, coberturaReportFile: '**/target/site/cobertura/coverage.xml', conditionalCoverageTargets: '70, 0, 0', failUnhealthy: false, failUnstable: false, lineCoverageTargets: '80, 0, 0', maxNumberOfBuilds: 0, methodCoverageTargets: '80, 0, 0', onlyStable: false, sourceEncoding: 'ASCII', zoomCoverageChart: false
                }
            }
        }
        stage ('Acceptance Test') {
            steps {
                echo 'Preparing test harness: TEAM Engine'
                echo 'Download and start TEAM Engine'
                echo 'Start SUT deegree webapp with test configuration'
                echo 'Run FAT'
            }
            post {
                success {
                }
            }
        }
        stage ('Release') {
            when {
                // check if branch is master
                branch 'master'
            }
            agent { label 'docker' }
            steps {
                echo 'Prepare release version...'
                echo 'Build docker image...'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
        stage ('Deploy PROD') {
            agent { label 'demo' }
            steps {
                echo 'Deploying to PROD...'
                echo 'Running smoke tests...'
            }
        }
    }
}