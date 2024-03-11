package ro.mpp2024.Domain;

public abstract class Entity {
    protected int id;

    public Entity(int newID){
        this.id= newID;
    }

    public int getId(){
        return this.id;
    }
}
