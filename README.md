# TT20Forged

## Introduction

TT20Forged is a server-side Minecraft mod designed to improve the gameplay experience on servers with low Ticks Per Second (TPS). When the server is lagging (i.e., TPS is below 20), the mod intelligently accelerates various in-game activities (like block breaking, item usage, fluid flow, etc.) to make them feel as close as possible to the normal speed at 20 TPS, thus reducing the perceived latency and lag for players.

## Features

This mod can accelerate the following game mechanics:

-   Block Entities (e.g., furnaces, chests)
-   Block Breaking
-   Potion Effects
-   Fluid Flow (Water and Lava)
-   Item Pickup Delay
-   Food and Potion Consumption
-   Portal Travel
-   Sleeping
-   Time Progression
-   Bow and Crossbow Drawing
-   Random Ticks (affecting plant growth, etc.)

All features can be toggled individually via the configuration file or in-game commands.

## Commands

All commands require appropriate operator (OP) permissions.

### `/tt20`
-   **Function**: Displays basic information and the enabled status of the mod.
-   **Permission**: None

### `/tt20 tps`
-   **Function**: Shows the server's current TPS, average TPS, and stable TPS.
-   **Permission**: None

### `/tt20 status`
-   **Function**: Dynamically displays the current status of all acceleration features, along with detailed information like TPS and MSPT.
-   **Permission**: None

### `/tt20 set cap <value>`
-   **Function**: Dynamically sets the value of `tick-repeat-cap`, effective immediately.
-   **Example**: `/tt20 set cap 15`
-   **Permission**: OP Level 3

### `/tt20 toggle <config_name>`
-   **Function**: Toggles the state of a specified boolean configuration option.
-   **Example**: `/tt20 toggle block-breaking-acceleration`
-   **Tip**: The `<config_name>` argument supports auto-completion with the `Tab` key.
-   **Permission**: OP Level 3

### `/tt20 mask`
-   **Function**: Manages the blacklist/whitelist for block entities.
-   **Sub-commands**:
    -   `list`: Shows the current content and mode of the mask list.
    -   `type <whitelist|blacklist>`: Switches between whitelist and blacklist mode.
    -   `add <block_id>`: Adds a block to the list.
    -   `remove <block_id>`: Removes a block from the list.
-   **Examples**:
    -   `/tt20 mask list`
    -   `/tt20 mask type blacklist`
    -   `/tt20 mask add minecraft:chest`
    -   `/tt20 mask remove minecraft:furnace`
-   **Permission**: OP Level 3

### `/tt20 reload`
-   **Function**: Reloads the main configuration file and the block entity mask file.
-   **Permission**: OP Level 2

## Configuration

The configuration files are located in the `config/tt20/` folder in your server's root directory. The format has been updated to **TOML**, with detailed comments for each option.

### `config.toml`
The main configuration file. You can manually enable or disable acceleration features here.
-   `tick-repeat-cap`: The tick repeat cap. This is a safety setting that limits the maximum number of times a tick can be repeated in a single game tick to catch up with server lag. The default value is 10. Set to -1 or 0 to disable the cap. It is recommended to keep it as a reasonable positive integer.

### `block_entity_mask.toml`
The mask configuration for block entity acceleration. You can configure a whitelist or a blacklist to precisely control which block entities (e.g., `minecraft:furnace`) should be accelerated.
-   `type`: "whitelist" or "blacklist".
-   `blocks`: A list of block IDs, supporting wildcards (e.g., `minecraft:*` or `*:furnace`).

## Multi-language Support

This mod currently supports **English** and **Chinese**. The mod's display language will automatically switch based on the game client's language setting.
The community can contribute new translations by adding new language files (`assets/tt20forged/lang/<lang_code>.json`) inside the `.jar` file.
