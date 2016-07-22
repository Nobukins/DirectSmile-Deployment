//	ALL Upper case indicates Environmental Variable comes from Jenkins as Parameter input	

if (binding.variables.containsKey('DEBUG_RUN')) {
	//Inherite value from env var comes Jenkins
} else {
    DEBUG_RUN = 'true'
	DSMOURL= 'http://' + FQDN + '/dsmo'	
	DSMXURL = 'http://' + FQDN
	
	WEBSITES = 'C:\\inetpub\\wwwroot'
	SQLINSTANCENAME = '.'
	DSMX_SQLDATABASENAME = 'LP3_DSM'
	SQL_AUTHENTICATION = 'false'
	SQLUSERNAME = ''
	SQLPASSWORD = ''
	CONFIGURE_IISAPPLICATIONPOOLIDENTITY_USER = 'false'
	IISAPPIDENTITYUSERNAME = ''
	IISAPPIDENTITYPASSWORD = ''
	CONFIGURE_LOGINUSERFORBACKEND = 'false'
	SERVICE_USERNAME = ''
	SERVICE_PASSWORD = ''
	SERVICE_DOMAIN = ''
	DSM_BACKUP = 'C:\\DSM_Backup'
	BACKUP_DSMXCONFIGURATIONFILES = 'true'
	BACKUP_DSMX_LANDINGPAGEDATA = 'false'
	DATABASE_BACKUP = 'true'
	SHRINK_DATABASE = 'true'
	DB_TIMEOUT = '30'

	EMAILBACKEND = 'C:\\Program Files (x86)\\DirectSmile\\DirectSmile Email Backend'
	TRIGGERBACKEND = 'C:\\Program Files (x86)\\DirectSmile\\DirectSmile Trigger Service'
	LANDINGPAGEDATADIR = 'C:\\inetpub\\wwwroot\\LandingPageData'

	SHAREDSETTINGSFILE = ''
	DSMXSERVERKEY = ''
	DEFAULTREDIRECTURL = ''
	DSMIMASTERURL = ''
	DSMIFRONTENDURL = ''
	FAILOVERENDPOINT = ''

	WEBSITENAME = 'Default Web Site'
	APPPOOLNAME = 'DefaultAppPool'
	STATICCOMPRESSIONOPTION = 'false'

	DSMX_VERSION_NUMBER = '7.2.2.153'

}
job('DSMX_Deployment__' + CUSTOMER_NAME) {
	description('Update DSMX to one of Release, Release Candidate, or Developement version')
    parameters {
        booleanParam('DEBUG_RUN', DEBUG_RUN.toBoolean(), '<p>If DEBUG_RUN=True all commands will be echoed to the screeb only. Target system will not be touched.</p>')
		stringParam('CUSTOMER_NAME', CUSTOMER_NAME,'<h3>Customer Name</h3><p>this value become as post-fix for generated deploy jobs</p>')
		stringParam('FQDN', FQDN ,'<h3>Fully Qualified Domain Name</h3><p>Customer server FQDN which listedn by DSMInstallation Service</p>')
		stringParam('DSMXURL',DSMXURL,'<p>http:// prefix is needed</p>')
		stringParam('DSMOURL',DSMOURL,'<p>http:// prefix and /dsmo as postfix are needed</p>')		
		
		choiceParam('DEPLOY_VERSION', ['DSMX_LATEST_RELEASE','DSMX_DSF_RELEASE','DSMX_SPECIFIC_VERSION'],'Select version you want to deploy')
		stringParam('DSMX_VERSION_NUMBER','DSMX_VERSION_NUMBER', '<p>This field is only relevant when you select "<strong>DSMX_SPECIFIC_VERSION</strong>" in above selection menu.</p> <p>&nbsp;</p> <p>The file name should have<span style="background-color: #ffff00;"> "</span><strong><span style="background-color: #ffff00;">dsmx-</span></strong><span style="background-color: #ffff00;">"</span> as its prefix. You only need to input version number</p> <ul> <li>Example input: <strong>7.2.2.153</strong></li> </ul> <p>While we create download link, we add "dsmx-" + "%DSMX_VERSION_NUMBER%" + ".msi" automatically.</p> <p>&nbsp;</p> <p>In case you set value in <strong>LOCAL_WEB_DIR</strong>, then this value should be exactly file name of .msi you gonna execute.</p> <ul> <li>Example input: <strong>dsmx-7.2.2.153</strong></li> </ul>')	
		stringParam('LOCAL_WEB_DIR', LOCAL_WEB_DIR, '<p>You can use local directory path as value in here.as well as UNC path is supported</p> <p>Please ensure that you provide "<strong>/</strong>"(slash) for URL case in the end, "<strong>\\</strong>"(back slash) in case of UNC path.</p> <ul> <li>Example input: (URL) <a href="http://myserver/DirectSmile/Installer/">http://myserver/DirectSmile/Installer/<br /></a></li> <li>Example input: (UNC)&nbsp; <a href="\\\\NetworkAccessStorage\\DirectSmile\\Installer\\">\\\\NetworkAccessStorage\\DirectSmile\\Installer\\</a></li> </ul>')		
		
		stringParam('WEBSITES', WEBSITES, 'UNC Path for the root website directory')

		stringParam('SQLINSTANCENAME',SQLINSTANCENAME,'Instance name of your SQL Server<p>.\\SQLEXPRESS</p>')
		stringParam('DSMX_SQLDATABASENAME',DSMX_SQLDATABASENAME,'Name of database for the DSMX-default, "LP3_DSM"')
		booleanParam('SQL_AUTHENTICATION',SQL_AUTHENTICATION.toBoolean(),'Does this server use SQL Authentication')
		stringParam('SQLUSERNAME',SQLUSERNAME,'User name for SQL Authentication')

//Need to be changed to mask password
		stringParam('SQLPASSWORD',SQLPASSWORD,'Password for SQL Authentication')

		booleanParam('CONFIGURE_IISAPPLICATIONPOOLIDENTITY_USER',CONFIGURE_IISAPPLICATIONPOOLIDENTITY_USER.toBoolean(),'Only available option higher than Ver6.1! Enable when you use specific user for Application Pool Identity')	
		stringParam('IISAPPIDENTITYUSERNAME',IISAPPIDENTITYUSERNAME,'Usually, you need to specify Domain Account which has enough privileges to access relevant directory and service')

//Need to be changed to mask password
		stringParam('IISAPPIDENTITYPASSWORD',IISAPPIDENTITYPASSWORD,'Usually, you need to specify Domain Account which has enough privileges to access relevant directory and service')

		booleanParam('CONFIGURE_LOGINUSERFORBACKEND',CONFIGURE_LOGINUSERFORBACKEND.toBoolean(),'Only available option higher than Ver6.1! Enable when you use specific user as login of DSMOnline Backend to run it as Windows Service')	
		stringParam('SERVICE_USERNAME',SERVICE_USERNAME,'')

//Need to be changed to mask password
		stringParam('SERVICE_PASSWORD',SERVICE_PASSWORD,'')
		
		stringParam('SERVICE_DOMAIN',SERVICE_DOMAIN,'Service user domain that is executed')

		stringParam('EMAILBACKEND',EMAILBACKEND,'UNC path for the DirectSmile Email Backend')
		stringParam('TRIGGERBACKEND',TRIGGERBACKEND,'UNC path for the DirectSmile Trigger Backend')
		stringParam('LANDINGPAGEDATADIR',LANDINGPAGEDATADIR,'UNC path for LandingPageData Directory')
		
		stringParam('DSM_BACKUP',DSM_BACKUP,'Location of backup files')
		booleanParam('BACKUP_DSMXCONFIGURATIONFILES',BACKUP_DSMXCONFIGURATIONFILES.toBoolean(),'Take a backup of resource files')
		booleanParam('BACKUP_DSMX_LANDINGPAGEDATA',BACKUP_DSMX_LANDINGPAGEDATA.toBoolean(),'Take a backup of the LandigPageData directory(Up to 2GB. It will fail if it exceed more than 2GB because of win 32bit program limitation)')
		booleanParam('DATABASE_BACKUP',DATABASE_BACKUP.toBoolean(),'Take a backup of the LP3_DSM database')
		stringParam('SHRINK_DATABASE',SHRINK_DATABASE,'Whether shrink database when it is taking a backup')
		stringParam('DB_TIMEOUT',DB_TIMEOUT,'Timeout range of database backup')
		
		stringParam('SHAREDSETTINGSFILE',SHAREDSETTINGSFILE,'UNC path of the SharedSettingFile.xml. Important when you store LandingPageData in NAS or FileShare to support cluster DSMX configuration.')
		stringParam('DSMXSERVERKEY',DSMXSERVERKEY,'DSMXServerKey ID. Important when to support cluster/multiple DSMX instance configuration.')
		stringParam('DEFAULTREDIRECTURL',DEFAULTREDIRECTURL,'Default Redirect URL. Important for SaaS configuration')
		stringParam('DSMIMASTERURL',DSMIMASTERURL,'<p>DSMI Master Server URL. Important when to support cluster/multiple DSMI instance configuration.</p></br><p>However, it can be loadbalance DSMI URL from Ver7 DSMI, because of supporting multiple DSMI instance by one SQL DB.</p>')
		stringParam('DSMIFRONTENDURL',DSMIFRONTENDURL,'<p>DSMI Frontend URL. Specify Load Balanced DSMI URL for cluster/multiple DSMI as Crossmedia Backend configuration.</p>')
		stringParam('FAILOVERENDPOINT',FAILOVERENDPOINT,'<p>Redis Failover endpoint URL</p>')
		
		stringParam('WEBSITENAME',WEBSITENAME,'<p>Crossmedia website Name.</p></br><p>As default, it use "Default Web Site". If you use other than default, change this value. It is crutial to control website by appcmd.</p>')
		stringParam('APPPOOLNAME',APPPOOLNAME,'<p>Crossmedia website Application Pool Name.</p></br><p>As default, it use "DefaultAppPool". If you use other than default, change this value. It is crutial to control website by appcmd.</p>')
		booleanParam('STATICCOMPRESSIONOPTION',STATICCOMPRESSIONOPTION.toBoolean(),'<p>Option to make Static HTTP Content Compression on the DSMX Website.</p></br><p> Important to avoid css issue affected to Google Font not appearing correctly.</p>')
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
    steps {
        dsl {
            external('JobDSL_DSMX_Deployment.groovy')
		}
        batchFile('bat_Deployment/DSMX_Deployment.bat')
    }
}