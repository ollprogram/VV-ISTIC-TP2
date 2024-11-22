# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

## Example

#### Tight Class Cohesion 

```java
class Player{
    private String firstname;
    private String lastname;

    public Player(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    } 

    public getFirstName(){
        return firstname;
    }


    public getLastName(){
        return lastname;
    }
}
```


#### Loose Class Cohesion

```java
class Player{
    private String firstname;
    private String lastname;

    public Player(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    } 

    public String getFirstName(){
        return firstname;
    }


    public String getLastName(){
        return lastname;
    }

    public String getFullName(){
        return firstname+" "+fullname;
    } 
}
```

//TODO