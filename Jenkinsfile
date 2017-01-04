node {
    checkout scm
    sh "/usr/bin/mvn install"
    ArtifactArchiver artifacts: '**/target/*.jar', fingerprint: true
}