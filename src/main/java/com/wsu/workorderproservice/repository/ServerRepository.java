package com.wsu.workorderproservice.repository;

import com.wsu.workorderproservice.model.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//JpaRepository is particularly a JPA specific extension for Repository. It has full API CrudRepository (allows to create, retrieve, update, delete) and PagingAndSortingRepository.
public interface ServerRepository extends JpaRepository<Server, Integer> {

    //@Query annotation is used with Spring Data JPA repositories to define custom queries for retrieving data from a database.
    // It allows you to write JPQL (Java Persistence Query Language) or native SQL queries directly within your repository interfaces
    @Query(nativeQuery = true, value = "SELECT s.server_id AS serverId, s.first_name AS firstName, "
        + "s.last_name AS lastName, s.availability AS availability "
        + "FROM server s "
        + "WHERE :search IS NULL OR (s.server_id LIKE %:search% OR s.first_name LIKE %:search% "
        + "OR s._last_name LIKE %:search%) group by s.server_id")
    Page<Object[]> findBySearch(String search, Pageable pageable);
}
