package com.tcc.qbeacon.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Aula;

@Repository
@Transactional
public interface AulaRepository extends JpaRepository<Aula, Integer>{

	@Query(value = "SELECT * FROM AULA a "
			+ "WHERE a.turma_id = ?1 AND a.dia = ?2",
			nativeQuery=true)
	Aula buscarAula(Integer id_turma, String dia);
	
}
