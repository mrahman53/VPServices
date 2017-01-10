node {
    try {
        checkout scm
        sh "/usr/bin/mvn install"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
        currentBuild.result = "SUCCESS"
    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}