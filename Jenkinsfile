node {
    try {
        stage ('Build and Test') {
            checkout scm

            // Build
            sh "/usr/bin/mvn install"

            // archive artifact to Jenkins
            archiveArtifacts artifacts: '**/target/*.war', fingerprint: true

            // Copy artifact to s3
            sh "/usr/local/bin/aws s3 cp target/vpservices.war s3://api-artifact/${BRANCH_NAME}/${BUILD_NUMBER}/vpservices.war"

            if ($ { BRANCH_NAME } == 'modeling') {
                sh "ssh -i path-to-key.pem ec2-user@<api-server-ip> '/path/to/server/script'"
            }

            currentBuild.result = "SUCCESS"
        }

        stage ('Deploy to staging') {
            // Deploy Step

            // Run integration Tests
        }

        stage ('Deploy to Production') {
            if ($ { BRANCH_NAME } == 'modeling') {
                sh "ssh -i path-to-key.pem ec2-user@<api-server-ip> '/path/to/server/script'"
            }
        }

    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}