node {
    try {
        checkout scm
        sh "/usr/bin/mvn install"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
        currentBuild.result = "SUCCESS"
        step([$class: 'S3BucketPublisher',
              entries: [[
                 sourceFile: 'target/vpservices.war',
                 bucket: 'api-artifact',
                 selectedRegion: 'us-east-1',
                 noUploadOnFailure: true,
                 managedArtifacts: true,
                 flatten: false,
                 showDirectlyInBrowser: true,
                 keepForever: true
                        ]],
              profileName: 'vp_profile',
              dontWaitForConcurrentBuildCompletion: false,
        ])
    }
    catch(err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}