node {
    try {
        stage('Build and Test') {
            checkout scm

            // Build
            sh "/usr/bin/mvn install"

            // archive artifact to Jenkins
            archiveArtifacts artifacts: '**/target/*.war', fingerprint: true

            // Copy artifact to s3
            sh "/usr/local/bin/aws s3 cp target/vpservices.war s3://api-artifact/${BRANCH_NAME}/${BUILD_NUMBER}/vpservices.war"
        }

        stage ('Deploy to staging, run tests, push to prod') {
            if (BRANCH_NAME == 'modeling') {
                sh "ssh -i /home/tomcat/.ssh/id_rsa ubuntu@34.199.138.119 '~/copy-server.sh'"

                // sh 'run tests command'

                // sh 'merge with prod command'
            }
        }

        stage ('Deploy to prod') {
            if (BRANCH_NAME == 'prod') {
                sh "ssh -i /home/tomcat/.ssh/id_rsa ubuntu@54.84.169.143 '~/copy-server.sh'"
            }
        }
    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}