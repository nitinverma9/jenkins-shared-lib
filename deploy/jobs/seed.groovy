pipelineJob("SharedLibraryTest") {
	description()
	keepDependencies(false)
	parameters {
		stringParam("sha1", "master", "commit to checkout")
	}
	definition {
		cpsScm {
			scm {
				git {
					remote {
						github("nitinverma9/jenkins-shared-lib", "ssh")
						credentials("nitinverma9")
					}
					branch("*/master")
				}
			}
			scriptPath("deploy/Jenkinsfile")
		}
	}
	disabled(false)
}
