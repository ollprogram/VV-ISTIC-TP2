# Code of your exercise

---

Pour cette partie de l'exercice le code est déposé en trois parties :

- La première est une classe Main pour démarrer le programme.
- La deuxième est project analyser qui analyse le directory afin de trouver tous les fichiers .java à analyser
- La dernière sert, quant à elle, de calculateur. En effet, cette dernière analyse les endroits où les attributs de la classe sont utilisés et ensuite calcule à l'aide de la fonction calculateTCC le TTC de la classe.

### Problèmes encourus 

---

Si vous lancez la classe main vous voyez que très souvent le TCC vaut 0. Cela est dû à l'inactivité de la fonction suivante :

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        String fieldName = nameExpr.getNameAsString();
        if (fields.contains(fieldName)) {
            methodFieldAccessMap.get(currentMethodName).add(fieldName);
        }
    }

Notre visiteur ne passe jamais par cette fonction. Malheureusement, nous n'avons pas trouvé la solution à ce problème. Cette fonction est primordiale car elle permet de trouver si des arguments sont utilisés dans le code à visiter.