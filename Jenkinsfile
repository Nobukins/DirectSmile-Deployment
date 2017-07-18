#!/usr/bin/env groovy
// *******************************************************************************************************
// Start Actual Jenkins JobDSL
// *******************************************************************************************************
node {
	checkout scm{
		//Start to check out Jenkinsfile from Github
		git branch: 'ALL_IN_ONE', url: 'https://github.com/Nobukins/DirectSmile-Deployment'
	}
	parameters {
	//***************************************************************************
	//******* General Arguments ********
        booleanParam('DEBUG_RUN', false, '<p>If DEBUG_RUN=True all commands will be echoed to the screen only. Target system will not be touched. And it does not trigger downstrem jobs</p>')
        booleanParam('DSMG_DEPLOY', false, '<p>If DSMG_DEPLOY=True, DSMG Deployment commands will be executed.</p><br/><p>DSMG_DEPLOY=False, then Target system will not be touched.</p>')
        booleanParam('DSMI_DEPLOY', false, '<p>If DSMI_DEPLOY=True, DSMI Deployment commands will be executed.</p><br/><p>DSMI_DEPLOY=False, then Target system will not be touched.</p>')
        booleanParam('DSMX_DEPLOY', false, '<p>If DSMX_DEPLOY=True, DSMX Deployment commands will be executed.</p><br/><p>DSMX_DEPLOY=False, then Target system will not be touched.</p>')
	}
}