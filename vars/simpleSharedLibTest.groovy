def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    pipeline {
        agent any
        parameters {
            string(name: 'sha1', defaultValue: 'master', description: 'commit to checkout')
        }
        options {
            skipDefaultCheckout true
        }

        environment {
            REPOPATH = "${WORKSPACE}"
            ENV = "${pipelineParams.env}"
            REGION = "${pipelineParams.region}"
            GIT_SSH_COMMAND='ssh -o StrictHostKeyChecking=no'
        }

        stages {
            stage('Checkout') {
                steps {
                    dir ("${env.REPOPATH}") {
                        git branch: "${params.sha1}", changelog: false, credentialsId: 'nitinverma9', poll: false, url: 'git@github.com:nitinverma9/jenkins-shared-lib.git'
                    }
                }
            }
            stage('BuildStage') {
                steps {
                        sh '''
                            #!/bin/bash
                            echo "This is Build Stage for ${ENV}, ${REGION}"
                           '''
                      }
                post {
                    always {
                        sh '''#!/bin/bash
                            set -x
                            echo "This was a shared library test. Bye"
                            '''
                    }
                }
            }
        }
    }
}
