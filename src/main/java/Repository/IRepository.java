package Repository;

import Domain.Identifier;

import java.util.Collection;

// Identifier <=> Entity
public interface IRepository<T extends Identifier<Tid>, Tid > {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    Collection<T> getAll();

}
