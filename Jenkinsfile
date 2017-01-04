node {
    checkout scm
    sh "/usr/bin/mvn install"
    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
}