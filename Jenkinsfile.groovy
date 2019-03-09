node{
	stage('SCM Checkout') {
		git branch: 'staging', url: 'ssh://git-codecommit.us-east-1.amazonaws.com/v1/repos/sample-project-src'
	}
	stage('Build Angular App') {
	    sh 'rm -rf node_modules/'
		sh 'npm install'
		sh 'npm run build'
		sh 'rm -rf .git/'
	}
	stage('SCP Push') {
		dir('output/') {
		    git branch: 'staging', url: 'ssh://git-codecommit.us-east-1.amazonaws.com/v1/repos/sample-project-client'
		    sh 'echo 71265 > version.txt'
			sh 'git add .'
			sh 'git commit -m "upload"'
			sh 'git push origin staging'
		}
	}
	stage('Deploy EB') {
		dir('output/') {
			sh 'eb deploy client-staging --source codecommit/sample-project-client/staging'
			sh 'rm -rf .git/'
		}
	}

}
