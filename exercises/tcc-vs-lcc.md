# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

```Java
    public class compteur {
        
        private int x;

        public compteur(){
            this.x = 0;
        }

        public int getx(){
            return this.x;
        }

        public void incr(){
            this.x = getx() + 1;
        }

        public void decr(){
            this.x = getx() - 1;
        }

    }
```

- Dans notre exemple ci dessus, nous pouvons remarquer que chaques méthodes utilise l'attribut x, ce qui donne un TCC de 3/3 et un LCC 3/3.

- Vu que LCC prend également en compte les relations transitives entre les méthodes, alors TCC ne peut en aucun cas surpasser la valeur de LCC