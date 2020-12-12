# GroupHelp

A simple and customizable plugin that shows help to players depending on their group.

Advantages:
* Simple installation
* Easy to customize each chapter
* Allows to inherit chapters if permissions plugin is present
* Override only chapters you need to change in other groups

Disadvantages:
* Doesn't allow to work with multiple trees of groups inheritance

Dependencies:
* Any permission plugin

## Usage

Launch plugin for the first time to get sample config file:

```yml
help-header: '===== Help, chapter %s ====='
no-help-provided: 'No help provided'
no-permission: 'You don''t have permission.'
command-only-for-players: 'Only players can execute this command.'

groups:
  default:
    default-weight: 100
    default-message:
      - "Switch between one of the following chapters with §a/help [group name]"
      - "Basics: Basic server commands"
      - "Selection: Selecting area"
    basics:
      - "/help: Show this help"
      - "/rules: Show the server rules"
      - "/ping [text]: Test connection to server"
    selection:
      - "//wand: Get the region selection tool to select region using your mouse"
      - "//hpos1 and //hpos2: Select the corners of the region by the block you are watching at"
      - "//size: Get information about the selection"
  builder:
    default-weight: 200
    default-message:
      - "Since you are pro builder, we assume you know how to play!!"
      - "Have fun!!"
```

`groups` section contains help chapters for each group. For example, group `default` has chapters `default-message`, `basics` and `selection`.

Note that group `builder` has the same chapter name as previous group. In presence of permission plugin, it should inherit all chapters from `default` group and override those who have the same name because `builder` has higher weight.

Assuming that `builder` inherits all permissions of `default`, give the following permissions to groups to make it work:
* `grouphelp.help` to `default`
* `grouphelp.default` to `default`
* `grouphelp.builder` to `builder`
* ... and so on if needed.

Then, as a player, you should simply type `/help [chapter]` to see help from a speficied chapter (if not speficied, `default-message` is shown). If neither chapter is found, `no-help-provided` message is shown.

Additionally, you can customize messages color by using Minecraft color formatting. For example, `"§aHello"` will result in green text.

`help-header` is always ready to show the current chapter. Please, leave `%s` exactly where you want to show it.

`/reloadhelp` command should reload config at runtime if you want to fix something quickly, and it requires `grouphelp.reloadhelp` permission. While it doesn't let normal players to affect something, it should be given to admins/owners only.
