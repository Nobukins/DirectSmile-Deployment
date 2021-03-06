//	ALL Upper case indicates Environmental Variable comes from Jenkins as Parameter input

if (binding.variables.containsKey('DEBUG_RUN')) {
	//Inherite value from env var comes Jenkins
	//This part set default Credential ID, to avoid any failure due to missing ID specification.
	//Please replace it with your own Jenkins server's whatever Credenital ID that you can show it as kind of example
	
	if (SQL_CREDENTIAL == ''|IISAPPLICATIONPOOLIDENTITY_CREDENTIAL == ''|IISAPPLICATIONPOOLIDENTITY_CREDENTIAL == '') {

	//Set Default Credential ID
		DEFAULT_SQL_CRDENTIAL_ID = 'Example-SQL-Credential-ID'
		DEFAULT_LOGIN_CRDENTIAL_ID = 'Example-Login-Credential-ID'
		
		SQL_CREDENTIAL = DEFAULT_SQL_CRDENTIAL_ID
		IISAPPLICATIONPOOLIDENTITY_CREDENTIAL = DEFAULT_LOGIN_CRDENTIAL_ID
		LOGINUSERFORBACKEND_CREDENTIAL = DEFAULT_LOGIN_CRDENTIAL_ID
	}
} else {
    DEBUG_RUN = 'true'
	DSMOURL= 'http://' + FQDN + '/dsmo'	
	DSMXURL = 'http://' + FQDN
	DSMI_INSTALLER_FILE_PATH = ''

	WEBSITES = 'C:\\inetpub\\wwwroot'	
	DIRPROPERTY1 = 'C:\\inetpub\\wwwroot\\DSMO'
	CNNAME = FQDN
	SQLINSTANCENAME = '.'
	DSMI_SQLDATABASENAME = 'dsmodb'
	IMGDBNAME = 'dsmoImages'
	SQL_AUTHENTICATION = 'false'

	DSMGPATH = 'C:\\Program Files (x86)\\DirectSmile Generator'
	DSMXPATH = 'C:\\inetpub\\wwwroot'
	DSMUSERS = 'C:\\DSMUsers'
	DSMTEMP = 'C:\\DSMTemp'
	DSMI_INSTALLDIR = 'C:\\Program Files (x86)\\DirectSmile\\DirectSmile Online Backend'
	CONFIGURE_IISAPPLICATIONPOOLIDENTITY_USER = 'false'

	CONFIGURE_LOGINUSERFORBACKEND = 'false'
	SERVICE_DOMAIN = ''

	DSM_BACKUP = 'C:\\DSM_Backup'
	BACKUP_DSMICONFIGURATIONFILES = 'true'
	DATABASE_BACKUP = 'true'
	SHRINK_DATABASE = 'true'
	DB_TIMEOUT = '30'
	DSMIII = 'DSMI01'
	DSMI_REPLICATION_MASTER = 'false'
	DSMI_REPLICATION_SLAVE = 'false'
	AVOID_USER_SESSION = 'false'
	NOASS = '1'
	BACKENDROUTINGARG = 'dsmi'
	SHORTTIMEOUT = '20'
	CROSSDOMAINURI = '*'
	IISAPPNAME = 'DSMO'
	SERVICE_EXE_NAME = 'DSMOnlineBackend.exe'
	LOG_LEVEL = '1'
	ENABLEWF = '1'

	//Set Default Credential ID
		DEFAULT_SQL_CRDENTIAL_ID = 'Example-SQL-Credential-ID'
		DEFAULT_LOGIN_CRDENTIAL_ID = 'Example-Login-Credential-ID'
		
		SQL_CREDENTIAL = DEFAULT_SQL_CRDENTIAL_ID
		IISAPPLICATIONPOOLIDENTITY_CREDENTIAL = DEFAULT_LOGIN_CRDENTIAL_ID
		LOGINUSERFORBACKEND_CREDENTIAL = DEFAULT_LOGIN_CRDENTIAL_ID
}

job('DSMI_Deployment__' + CUSTOMER_NAME) {
    parameters {
        booleanParam('DEBUG_RUN', false, '<p>If DEBUG_RUN=True all commands will be echoed to the screeb only. Target system will not be touched.</p>')
		stringParam('CUSTOMER_NAME', CUSTOMER_NAME,'<h3>Customer Name</h3><p>this value become as post-fix for generated deploy jobs</p>')
		stringParam('FQDN', FQDN ,'<h3>Fully Qualified Domain Name</h3><p>Customer server FQDN which listedn by DSMInstallation Service</p>')

		choiceParam('DEPLOY_VERSION', ['DSMI_LATEST_RELEASE','DSMI_DSF_RELEASE','DSMI_SPECIFIC_VERSION'],'Select version you want to deploy')
		stringParam('DSMI_INSTALLER_FILE_PATH', DSMI_INSTALLER_FILE_PATH, '<h3>Abosolute File Path or URL</h3><p>You can use local directory path as value in here, as well as UNC path is supported</p><ul> <li>Example input: (URL) <a href="http://directsmile.blob.core.windows.net/installer/dsmi.msi">http://directsmile.blob.core.windows.net/installer/dsmi.msi<br /></a></li> <li>Example input: (UNC)&nbsp; <a href="\\\\NetworkAccessStorage\\DirectSmile\\Installer\\dsmi.msi">\\\\NetworkAccessStorage\\DirectSmile\\Installer\\dsmi.msi</a></li> </ul>')	
		
		stringParam('DSMOURL',DSMOURL,'<h3><font color="red">Only Valid &gt; Ver7.2.0.60</font></h3></br> <p>DSMOURL="http://servername/dsmo"</p></br> <p>Please add this parameter to the dsmi installer call to get the base url of DSMI correctly set and to get the url to the dsmimages correctly set.</p></br>')
		stringParam('DSMXURL',DSMXURL,'<h3><font color="red">Only Valid &gt; Ver7.2.0.60</font></h3></br> <p>DSMXURL="http://servername/dsmx"</p><p>(In case of SmartServer deployment)</p></br></br> <p>Please add this parameter to the dsmi installer call to get the base url of DSMX correctly set in the DSMI settings.</p></br>')

		stringParam('WEBSITES', WEBSITES, 'UNC Path for the root website directory')
		stringParam('DIRPROPERTY1', DIRPROPERTY1, 'UNC Path for the DSMI website directory')
		stringParam('CNNAME', CNNAME,'Common Name that your SSL issued to')
		stringParam('SQLINSTANCENAME',SQLINSTANCENAME,'Instance name of your SQL Server<p>.\\SQLEXPRESS</p>')
		stringParam('DSMI_SQLDATABASENAME',DSMI_SQLDATABASENAME,'Name of database for the DSMI-default, "dsmodb"')
		stringParam('IMGDBNAME',IMGDBNAME,'Name of dsmoImages database*New from Ver7.0.0.53')


//Replaced to Credential Plugin with Credential Binding Plugin model
		booleanParam('SQL_AUTHENTICATION',SQL_AUTHENTICATION.toBoolean(),'Does this server use SQL Authentication')
		credentialsParam('SQL_CREDENTIAL') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            defaultValue(SQL_CREDENTIAL)
            description('<h3>SQL Authentication</h3></br><p>In case you use SQL Authentication, please add new credential in here, then specify your created Credential in here</p>')
        }
		
		stringParam('DSMGPATH',DSMGPATH,'<h3><font color="red">Only Valid &gt; Ver7.2.0.133</font></h3></br> <p>DSMGPATH="C:\\Program Files (x86)\\DirectSmile Generator"</p></br></br> <p>if NOT empty, the installer configures the DirectSmileWorkingDirectory in the tblSettings</p></br>')

		stringParam('DSMXPATH',DSMXPATH,'<h3><font color="red">Only Valid &gt; Ver7.2.4</font></h3></br><p>DSMXPATH="C:\\inetpub\\wwwroot"</p></br></br><p>if NOT empty, the installer write input in DSMXRootPath field of dsmodb.tblSettings.</p></br>')

		stringParam('DSMUSERS',DSMUSERS,'<h3><font color="red">Only Valid &gt; Ver7.2.0.60</font></h3> <p>DSMusers directory is getting created and permissions set if DSMUSERS!=null</p>')
		stringParam('DSMTEMP',DSMTEMP,'<h3><font color="red">Only Valid &gt; Ver7.2.0.54</font></h3> <p>DSMTemp can be set in installer now, creates also app pool and sets the permissions, sets the localimagerepository path and url in dsmodb.tblsettings</p>')

		stringParam('DSMI_INSTALLDIR',DSMI_INSTALLDIR,'Install target directory for DSMOnline Backend')

//Replaced to Credential Plugin with Credential Binding Plugin model
		booleanParam('CONFIGURE_IISAPPLICATIONPOOLIDENTITY_USER',CONFIGURE_IISAPPLICATIONPOOLIDENTITY_USER.toBoolean(),'Only available option higher than Ver6.1! Enable when you use specific user for Application Pool Identity')
		credentialsParam('IISAPPLICATIONPOOLIDENTITY_CREDENTIAL') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            defaultValue(IISAPPLICATIONPOOLIDENTITY_CREDENTIAL)
            description('<p>Usually, you need to specify Domain Account which has enough privileges to access relevant directory and service</p>')
        }


//Replaced to Credential Plugin with Credential Binding Plugin model
		booleanParam('CONFIGURE_LOGINUSERFORBACKEND',CONFIGURE_LOGINUSERFORBACKEND.toBoolean(),'Only available option higher than Ver6.1! Enable when you use specific user as login of DSMOnline Backend to run it as Windows Service')
		stringParam('SERVICE_DOMAIN',SERVICE_DOMAIN,'Service user domain that is executed')
		credentialsParam('LOGINUSERFORBACKEND_CREDENTIAL') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            defaultValue(LOGINUSERFORBACKEND_CREDENTIAL)
            description('<h3>Login User for Backend</h3></br><p>In case you run DirectSmile as Service mode, please add new credential in here, then specify your created Credential in here</p>')
        }
		
		
		stringParam('DSM_BACKUP',DSM_BACKUP,'Location of backup files')
		booleanParam('BACKUP_DSMICONFIGURATIONFILES',BACKUP_DSMICONFIGURATIONFILES.toBoolean(),'Take a backup of DSMI configuration files and DSM Components')
		booleanParam('DATABASE_BACKUP',DATABASE_BACKUP.toBoolean(),'Take a backup of DSMI database')
		stringParam('SHRINK_DATABASE',SHRINK_DATABASE,'Whether shrink database when it is taking a backup')
		stringParam('DB_TIMEOUT',DB_TIMEOUT,'Timeout range of database backup')

		stringParam('DSMIII',DSMIII,'DSMI Instance Identifier for systems with multiple DSMI instances.')
		booleanParam('DSMI_REPLICATION_MASTER',DSMI_REPLICATION_MASTER.toBoolean(),'Master Server of the Replication DSMI group')
		booleanParam('DSMI_REPLICATION_SLAVE',DSMI_REPLICATION_SLAVE.toBoolean(),'Slave Server of the Replication DSMI group')
		booleanParam('AVOID_USER_SESSION',AVOID_USER_SESSION.toBoolean(),'If true DSMI Backend is configured to run as native service without user session.')

		booleanParam('NOASS',NOASS.toBoolean(),'<p>Not execute Server Assessment while DSMI Installation</p></br><p>True = Skip Assessment, False = Execute Assessment</p>')
		stringParam('BACKENDROUTINGARG',BACKENDROUTINGARG,'Optional Routing argument added to the DSMI base URL to access the DSMI Backend HTTP handler.')
		stringParam('SHORTTIMEOUT',SHORTTIMEOUT,'Optional Configures the timeout in seconds for image processing requests in the backend.')
		stringParam('CROSSDOMAINURI',CROSSDOMAINURI,'Locks down cross domain access to specific domains')
		stringParam('IISAPPNAME',IISAPPNAME,'Optional Virtual directory name of the DSMI web application')
		stringParam('SERVICE_EXE_NAME',SERVICE_EXE_NAME,'Optional Name of the DSMI Backend service application')

		choiceParam('LOG_LEVEL', ['Information', 'Critical', 'Error', 'Resume', 'Start', 'Stop', 'Suspend', 'Transfer', 'Verbose', 'Warning'],'<h3><font color="red">Only Valid &gt; Ver7.1.0.40</font></h3> <p>LOG_LEVEL install param to configure loglevel (default=Information)</p> <table cellspacing="1" summary="TraceEventType"><tbody><tr><th>TraceEventType</th><th>Description</th></tr><tr><td>Critical</td><td>Critical Error or Application Crash</td></tr><tr><td>Error</td><td>Recoverable Error</td></tr><tr><td>Information</td><td>Information message</td></tr><tr><td>Resume</td><td>Resume processing</td></tr><tr><td>Start</td><td>Start processing</td></tr><tr><td>Stop</td><td>Stop processing</td></tr><tr><td>Suspend</td><td>Suspend processing</td></tr><tr><td>Transfer</td><td>Change relative ID</td></tr><tr><td>Verbose</td><td>Debug tracing</td></tr><tr><td>Warning</td><td>Not important issues</td></tr></tbody> </table>')
		choiceParam('ENABLEWF', ['1','0'],'<h3><font color="red">Only Valid &gt; Ver7.2.0.52</font></h3> <p>ENABLEWF=1|0 enables the Workflow(1) or disables it (0), default is ENABLEWF=1</p>')
		}
	scm {
		git {
			remote {
				url ('https://github.com/Nobukins/DirectSmile-Deployment')
			}
			branch ('*/master')
			extensions {
				cleanAfterCheckout()
			}
		}
	}
	wrappers {
        credentialsBinding {
            usernamePassword('SQL_USERNAME','SQL_PASSWORD', SQL_CREDENTIAL)
            usernamePassword('IISAPPLICATIONPOOLIDENTITY_USERNAME','IISAPPLICATIONPOOLIDENTITY_PASSWORD', IISAPPLICATIONPOOLIDENTITY_CREDENTIAL)
            usernamePassword('LOGINUSERFORBACKEND_USERNAME','LOGINUSERFORBACKEND_PASSWORD', LOGINUSERFORBACKEND_CREDENTIAL)
        }
    }
    steps {
        dsl {
            external('JobDSL_DSMI_Deployment.groovy')
		}
        batchFile('bat_Deployment/DSMI_Deployment.bat')
    }
}