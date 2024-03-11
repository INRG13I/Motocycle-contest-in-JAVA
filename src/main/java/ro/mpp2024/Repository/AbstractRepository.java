package ro.mpp2024.Repository;

import ro.mpp2024.Domain.IEntity;

import java.util.ArrayList;

public abstract class AbstractRepository<T extends IEntity> implements IRepository{
    protected ArrayList<T> entityList= new ArrayList<>();
}
