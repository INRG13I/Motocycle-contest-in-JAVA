package Domain;

public class Entity<Tid> implements Identifier<Tid> {
    protected Tid id;

    @Override
    public Tid getId() {return id;}
    @Override
    public void setId(Tid id) {this.id = id;}
}
