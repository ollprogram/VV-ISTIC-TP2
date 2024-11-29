# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Après avoir réalisé un check sur un projet de bot pour un chat twitch, certaines erreurs apparaissent :

    /project/src/test/java/FilteringTest.java:6:    NoPackage:      All classes, interfaces, enums and annotations must belong to a named package

Cette erreur est tout simplement le fait que le test ne fait pas partie d'un package. Elle fait partie d'un cas de vrai positif car il est important que chaque classe ait un package par souci d'organisation et pour éviter les problèmes.  

De plus, d'autres erreurs apparaissent telles que :

    /project/src/main/java/fr/ollprogram/twitchdiscordbridge/SettingsFileManager.java:78:   UselessParentheses:     Useless parentheses.

Cette erreur, peut-être considérée comme fausse ou vraie positif. Elle est due à une surutilsation de parenthèses or dans certains cas ces parenthèses sont utilisées pour clarifier le code.

    return (3 + 2);

Dans ce cas précis, il s'agit d'un vrai positif car les parenthèses ne sont pas utiles que ce soit pour la compréhension du code ou pour clarifier l'ordre des opérations.

    return (3 + 2) * 5;

Tandis que dans ce cas précis, c'est un faux positif car les parenthèses clarifient l'ordre des opérations.
