- JobThread gets Jobs from MySQL database (MySQLConnection)
- JobThread queues Jobs through JobHandler
- JobHandler sorts Jobs by priority and proccess job utils
- JobHandler excecutes Jobs through JobThread? 


-----------------------

| ENUM         | ARGS                           |             EXPLANATION            |
|--------------|--------------------------------|:----------------------------------:|
| UPDATE_CHANS | None                           | Updates the web interface channels |
| RESTART      | None                           | Restarts the bot                   |
| SHUTDOWN     | None                           | Shuts down the bot                 |
| SEND_MESSAGE | channel:channel/line:line:line |                                    |