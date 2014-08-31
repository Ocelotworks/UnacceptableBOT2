UnacceptableBOT2
===================

	Stevie, Java IRC bot full of useful features.

===================
	Snapchat
	
		Snapchat no longer works, and probably will not again.

====================
	Database
	
	The database will be processed by `ConfigHandler.class` and managed by `MySQLConnection.class`.  
	Every variable will be managed by `ConfigHandler.class` for example Disabled commands, banned users etc
	
	`configHandler.getBoolean("cd:fillmein")` would return `true` or `false` depending on whether the command !fillmein is disabled.
	`configHandler.getInteger("facuettimeout")` would return an integer value.
	`configHandler.getUserInfo("UnacceptableUse")` would return a `UBUser` instance containing stats etc about that user
	
	
	
