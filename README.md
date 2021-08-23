# Badlion Client Timer Manager
BLCTimerManager is a Bukkit plugin that allows you to create and manage cutstom [Timers for Badlion Client](https://www.badlion.net/forum/thread/223003) on your server with commands.

![Plugin Screenshot](https://cdn.discordapp.com/attachments/806219262297243678/879359087639421058/332ESKXLAD_IO478FIKV.png)

![Timer Mod Example](https://i.gyazo.com/76f560b0be9f9585bd0afd737cdfb084.png)

## Installation
1. Download [BLCTimerAPI](https://github.com/BadlionClient/BadlionClientTimerAPI/releases) and [BLCTimerManager](https://github.com/TravinDreek/BLCTimerManager/releases).
2. Place the two downloaded plugin into your `plugins` directory on your server.
3. Turn on the Bukkit server.

## Building
BLCTimerManager uses Maven to handle dependencies & building. **JDK 8** is required for building this plugin.

After all dependencies are ready (see `pom.xml` for more info), run:
```shell
$ mvn install
```