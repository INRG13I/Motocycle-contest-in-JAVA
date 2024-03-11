package ro.mpp2024.Repository;

import java.util.ArrayList;

public interface IRepository<T> {
    public void add(T newEntity);
    public void delete(Integer id);
    public void update(Integer id, T newEntity);
    public Integer findById(Integer id);
    public ArrayList<T> getAll();
}
