package com.almightyvats.sensorsafe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Common base repository for all other repositories. Provides basic methods for
 * loading, saving and removing entities.
 *
 * @param <T> The domain type this repository manages.
 * @param <I> The type of the id of the entity this repository manages.
 */
@NoRepositoryBean
public interface AbstractRepository<T, I extends Serializable> extends MongoRepository<T, I> {

    /**
     * Deletes an entity.
     *
     * @param entity The entity to be deleted.
     * @throws IllegalArgumentException If the given entity is (@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes an entity.
     *
     * @param id The id of the entity to be deleted.
     * @throws IllegalArgumentException If the given entity is (@literal null}.
     */
    void deleteById(I id);

    /**
     * Returns all instances of the type.
     *
     * @return All entities.
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its id.
     *
     * @param i must not be {@literal null}.
     * @return The entity with the given id or {@literal null} if none found.
     * @throws IllegalArgumentException If {@code id} is {@literal null}.
     */
    Optional<T> findById(I i);

    /**
     * Retrieves an entity by its name.
     *
     * @param name must not be {@literal null}.
     * @return The entity with the given name or {@literal null} if none found.
     * @throws IllegalArgumentException If {@code name} is {@literal null}.
     */
    Optional<T> findByName(I name);

    /**
     * Checks if an entity with the given name exists.
     *
     * @param name must not be {@literal null}.
     * @return {@literal true} if an entity with the given name exists, {@literal false} otherwise.
     * @throws IllegalArgumentException If {@code name} is {@literal null}.
     */
    Boolean existsByName(I name);

    /**
     * Saves a given entity. Use the returned instance for further operations as
     * the save operation might have changed the entity instance completely.
     *
     * @param <S>    The actual domain type if the entity.
     * @param entity The entity to be saved or updated.
     * @return The saved entity.
     */
    <S extends T> S save(S entity);
}
