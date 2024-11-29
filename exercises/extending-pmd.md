# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

Voici le fichier XML contenant la commande avec une triple condition imbriquée :

    <rule name=""
      language="java"
      message=""
      class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
    <description>

    </description>
    <priority>3</priority>
    <properties>
        <property name="xpath">
            <value>
        <![CDATA[
        //IfStatement//IfStatement//IfStatement
        ]]>
            </value>
        </property>
    </properties>
    </rule>


Un des résultats obtenus est la sélection du troisième et du quatrième if du code ci-dessous :


    if(l.startsWith("Twitch_Token=")){
        twitchAccount = new OAuth2Credential( "twitch", l.replace("Twitch_Token=", ""));
    }
    else if(l.startsWith("Discord_Token=")){
        discordToken = l.replace("Discord_Token=",  "");
    }
    else if(l.startsWith("Discord_Channel_ID=")){
        discordChannelID = l.replace("Discord_Channel_ID=",  "");
    }
    else if(l.startsWith("Twitch_Channel_Name=")){
        twitchChannelName = l.replace("Twitch_Channel_Name=", "");
    }

Nous avons ici une imbrication de 4 conditions, ce qui pourrait rendre complexes les différentes maintenances de l'application mais aussi réduire sa lisibilité. Pour éviter cela, il est possible d'utiliser à la place un switch qui est généralement utilisé pour matcher plusieurs valeurs possibles d'une valeur. 


Un autre résultat trouvé est la troisième condition de ce bout de code ci-dessous :

    if(cmd == null){ //if it's not a referenced command
			String content = event.getMessage().getContentDisplay();
			if(content.startsWith(prefix)
					&& content.equalsIgnoreCase(prefix+"help")){
				helpAction(commands, event);
			}
			else if(bridge != null && bridge.isOpened() &&  event.getChannel().equals(bridge.getDiscordChannel())){
				bridge.sendToTwitch(event.getMessage());
			}
    }

Dans ce cas précis, nous pouvons voir que la troisième condition est incompréhensible et dépend du résultat de trois sous-conditions. On voit donc que cette partie est très enclaine à produire des erreurs, elle mériterait une refonte immédiate en réalisant une analyse des critères MC/DC. 