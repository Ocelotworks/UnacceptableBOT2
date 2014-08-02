UnacceptableBOT2
================

Steve II



HOW SHIT ALL WORKS AND STUFF

- The database:
	The database will be processed by `ConfigHandler.class` and managed by `MySQLConnection.class`.  
	Every variable will be managed by `ConfigHandler.class` for example Disabled commands, banned users etc
	
	`configHandler.getBoolean("cd:fillmein")` would return `true` or `false` depending on whether the command !fillmein is disabled.
	`configHandler.getInteger("facuettimeout")` would return an integer value.
	`configHandler.getUserInfo("UnacceptableUse")` would return a `UBUser` instance containing stats etc about that user
	
	
	
