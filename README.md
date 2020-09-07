# KitPvP Plugin Minecraft

This project is made as an assignment for testing my skills programming a Minecraft plugin. As assignment I programmed a KitPvP plugin where players can fight each other, use different kits and become the number one player on the server.

## Getting Started

Before you are able to use/test this plugin there are a couple of steps you need to take in order to set up the project.

### Minecraft Server

The project is written in Java and uses Maven to import Paper-Spigot version 1.13.2, this had to be used for the assignment. If you want to use/test this plugin build this project to a .jar file, run a Paper-Spigot server and add the .jar file.

### Database
To use the plugin you have to set up a local database using the standard credentials "root", "root". If you want a public database or change these settings you can find the setup in the *Database.java* class.

### Command Usage

The plugin contains a couple commands that have to be setup before you can use it. Here I will list all the commands and there purpose.

##### Add Spawn Locations

The *"setlobbyspawn"* and *"setgamespawn"* commands are used to add new lobby/game spawn location to the database, it takes the location and direction of the player that used it.

'''
/setlobbyspawn
'''

##### Remove Spawn Locations

The *"removelobbyspawn"* and *"removegamespawn"* commands are used to remove lobby/game spawn location from the database, it takes table id of a location.

'''
/removelobbyspawn 12
'''

##### List Spawn Locations

The *"listlobbyspawn"* and *"listgamespawn"* commands are used display a list of all the spawn locations with there id in the chat.

'''
/listlobbyspawn
'''

##### Create a Kit

The *"createkit"* command is used to create a new kit, it takes all the item from the player that used the command and saves it in the config file. As parameters it need a name for the kit, a material to display the kit and the display slot with a min/max of (0, 8).

'''
/createkit mykit diamond_axe 4
'''

##### Get a Kit

The *"getkit"* command is used to give the player that used it the selected kit, it takes the name of the kit.

'''
/getkit mykit
'''

### Sing Usage

For the players to have a more smooth experience you can use Minecraft sign's to teleport them to the lobby or game and give them kits. Here I will list the process to set up thees sign's.

There are three type of sign's you can create. One for letting players join the game, another for letting them leave the game and the last is for letting them select there kit.

The join sign is used for joining the lobby and the game, it checks if a player has already joined the lobby and then puts him in the game. If he's not in the lobby yet he joins the lobby.

To create a sing like this add this text to the first line:

'''
[KITPVP]

'''
Then you can choose between *join/kit* on the second line:
'''
join

'''
Leave the last two lines blank and create the sign, now you should see the sign turn read and this means you did it correct. By clicking the sing its runs the used command on the player that clicked it.

## Documentation
All the code in this project is documented using JavaDocs. To generate the documentation create a new folder called *"./docs"* and run this command:
'''
javadoc -d docs @jdoc.lst
'''
After this command you will find an index.html file inside the *"./docs"* folder.
## Built With

* [Paper-Spigot](https://papermc.io/) - Lightweight Minecraft server
* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is open source and free to use for education purpose.
