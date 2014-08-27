UnacceptableBOT2
===================

	Steve II

	Snapchat
===================
		Ask teknogeek or dvd604 for the pem to package into the JAR so that shit will work.


	Database
====================
	The database will be processed by `ConfigHandler.class` and managed by `MySQLConnection.class`.  
	Every variable will be managed by `ConfigHandler.class` for example Disabled commands, banned users etc
	
	`configHandler.getBoolean("cd:fillmein")` would return `true` or `false` depending on whether the command !fillmein is disabled.
	`configHandler.getInteger("facuettimeout")` would return an integer value.
	`configHandler.getUserInfo("UnacceptableUse")` would return a `UBUser` instance containing stats etc about that user
	
	
	
