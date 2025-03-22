# ChatLib

A small library for sending messages to Minecraft players using factories and placeholders.


## Features

- Chat messages
- Action bar messages
- Titles
- Possibility to add custom placeholders and factories

## Usage

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>


<dependency>
    <groupId>com.github.cl3t4p</groupId>
    <artifactId>ChatLib</artifactId>
    <version>1.1.1</version>
</dependency>
```
[![](https://jitpack.io/v/cl3t4p/ChatLib.svg)](https://jitpack.io/#cl3t4p/ChatLib)


## Example usage

```java
    //Setup
    @Override
    public void onEnable(){
        File msg = new File(getDataFolder(),"msg.yml");
        Messenger messenger = new Messenger(msg);
    }



    @EventHandler
    public void fishEvent(PlayerFishEvent event){
        // Send message to player when he catches a fish with the type of fish
        Player player = event.getPlayer();
        if (event.getCaught() != null) {
            Map<String,Object> items = Map.of("fish", event.getCaught().getType().toString());
            messenger.sendMsg("player_fish",player,items);
        }else{
            messenger.sendMsg("player_fish_no_fish",player);
        }
    }
```

```yaml
#msg.yml
#A: action_bar
#C: chat_message
#T: title
#I: item

player_fish: "#T&7You caught a &6%fish%&7!"
player_fish_no_fish: "#C&7You caught nothing!"
````
