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
        }

        // stage ('Deploy to staging') {
            // Deploy Step
        // }

        stage ('Deploy to Production') {
            sh "/usr/local/bin/aws s3 sync s3://api-artifact/${BRANCH_NAME}/${BUILD_NUMBER} s3://api-artifact/production"
            if (BRANCH_NAME == 'modeling') {
                sh "ssh -i /home/tomcat/.ssh/id_rsa ubuntu@10.20.20.133 '~/copy-server.sh'"
            }
        }

    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}