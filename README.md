# Stats2Discord
A plugin to show scoreboard objectives/statistics in Discord.

Made to be used with datapacks like:
 * [Syntro42's Every Scoreboard fork](https://github.com/Syntro42/every-scoreboard)
 * [Every Scoreboard](https://github.com/samipourquoi/every-scoreboard)
 * [RecordEveryStatistic](https://github.com/tylastrog/RecordEveryStatistic)
 * [Vanilla Tweaks Track Raw Statistics](https://vanillatweaks.net/picker/datapacks/)
   
Originaly this plugin is made by Christophe6 for me. I now try to maintain it, and hopefully improve it a bit in the future. I'm pretty clueless about any (Java) development, so the code quality is probably horrible. The plugin works reletive well on my server, but you should use this project at your own risk. If you have a problem you may open a issue or PR.

## Alternatives
I'm pretty sure [samipourquoi's endtech bot](https://github.com/samipourquoi/endbot) has features to show objectives/statistics in Discord and their code quality is probably better, so I recommend you look at that.

## Commands
You can use the following commands with the bot in Discord:

 * `!stats` - Shows a pre configured list of objectives/statistics configured in config.yml, this is the server total so the statistics of all players added together.
 * `!stats <player>` - Shows a pre configured list of objectives/statistics configured in config.yml, this only shows the selected player.
 * `!stats <objective/statistic>` - Shows the top 10 players for the selected objectives/statistic.
 * `!stats <player> <objective/statistic>` - Shows how many the selected player has from the selected objectives/statistic.

## Galery
`!stats` - Shows a pre configured list of objectives/statistics configured in config.yml, this is the server total so the statistics of all players added together.

![image](https://user-images.githubusercontent.com/35953244/183703167-586bca1a-aad3-498b-afd1-957b72d34378.png)

`!stats <player>` - Shows a pre configured list of objectives/statistics configured in config.yml, this only shows the selected player.

![image](https://user-images.githubusercontent.com/35953244/183703272-36b4c426-2f52-4172-9076-3426cd5e9e2f.png)

`!stats <objective/statistic>` - Shows the top 10 players for the selected objectives/statistic.

![image](https://user-images.githubusercontent.com/35953244/183703516-d3796e87-3af7-43ea-8a38-88f170a33d74.png)

`!stats <player> <objective/statistic>` - Shows how many the selected player has from the selected objectives/statistic.

![image](https://user-images.githubusercontent.com/35953244/183703792-4edf19b2-c544-4991-88bf-29ab3821cd40.png)
