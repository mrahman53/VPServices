node {
    try {
        checkout scm
        sh "/usr/bin/mvn install"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
        currentBuild.result = "SUCCESS"
        sh "/usr/local/bin/aws s3 cp target/vpservices.war s3://api-artifact/${BRANCH_NAME}/${BUILD_NUMBER}/vpservices.war"
    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}