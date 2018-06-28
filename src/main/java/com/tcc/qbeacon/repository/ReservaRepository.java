package com.tcc.qbeacon.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcc.qbeacon.model.Reserva;

@Repository
@Transactional
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	
	@Query(value = "SELECT * FROM RESERVA r "
			+ "WHERE r.sala_id = ?1 AND r.id IN ("
			+ "	SELECT reservas_id FROM HORARIO_RESERVAS hr WHERE hr.horario_id IN ("
			+ "		SELECT id FROM HORARIO h WHERE h.periodo = ?2 AND h.dia_semana = ?3"
			+ "		)"
			+ ")",
			nativeQuery=true)
	Reserva buscaReserva(Integer id_sala, String horario, String diaSemana);
}
