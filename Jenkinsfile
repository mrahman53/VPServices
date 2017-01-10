node {
    try {
        checkout scm
        sh "/usr/bin/mvn install"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
        currentBuild.result = "SUCCESS"
        sh "cp target/vpservices.war ${BRANCH_NAME}-${BUILD_NUMBER}-vpservices.war"
        step([$class: 'S3BucketPublisher', entries: [[
             sourceFile: "${BRANCH_NAME}-${BUILD_NUMBER}-vpservices.war",
             bucket: 'api-artifact',
             selectedRegion: 'us-east-1',
             noUploadOnFailure: true,
             managedArtifacts: true,
             flatten: false,
             showDirectlyInBrowser: true,
             keepForever: true]],
          profileName: "jenkins"
        ])
    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}