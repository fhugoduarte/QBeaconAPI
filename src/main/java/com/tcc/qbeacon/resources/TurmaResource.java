package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.config.JwtEvaluator;
import com.tcc.qbeacon.datas.ReservaData;
import com.tcc.qbeacon.datas.TurmaData;
import com.tcc.qbeacon.exceptions.TokenException;
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.TurmaService;

@RestController
@RequestMapping("/api/turma")
public class TurmaResource {

	@Autowired
	TurmaService turmaService;
	
	@Autowired
	JwtEvaluator jwtEvaluator;
	
	@GetMapping
	public ResponseEntity<List<TurmaData>> listarTurmas() {
		List<Turma> turmasBanco = turmaService.pegarTurmas();
		List<TurmaData> turmas = new ArrayList<TurmaData>();
		
		for (Turma turma : turmasBanco) {
			turmas.add(new TurmaData(
					turma.getId(), 
					turma.getProfessor(), 
					turma.getDisciplina().getNome(), 
					null, 
					null));
		}
		return new ResponseEntity<List<TurmaData>>(turmas, HttpStatus.OK);
	}
	
	@GetMapping(path="/minhas_turmas")
	public ResponseEntity<List<TurmaData>> listarTurmasUsuario() throws TokenException {
		Usuario usuarioLogado = jwtEvaluator.usuarioToken();
		
		List<Turma>turmasBanco = usuarioLogado.getTurmas();
		List<TurmaData> turmas = new ArrayList<TurmaData>();
		
		if(turmasBanco.isEmpty()) {
			return new ResponseEntity<List<TurmaData>>(turmas, HttpStatus.NOT_FOUND);
		}else {
			for (Turma turma : turmasBanco) {
				turmas.add(new TurmaData(
						turma.getId(), 
						turma.getProfessor(), 
						turma.getDisciplina().getNome(), 
						null, 
						null));
			}
			return new ResponseEntity<List<TurmaData>>(turmas, HttpStatus.OK);
		}
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<TurmaData> buscar(@PathVariable("id") Integer id){
		Turma turmaBanco = turmaService.buscarTurma(id);
		ReservaData reserva1 = new ReservaData();
		ReservaData reserva2 = new ReservaData();
		
		if(turmaBanco.getReserva1() != null) {
			Reserva reserva = turmaBanco.getReserva1();
			reserva1.setId(reserva.getId());
			reserva1.setSala(
					  reserva.getSala().getNome() + "-" 
					+ reserva.getSala().getBloco().getNome() + "-" 
					+ reserva.getSala().getBloco().getCampus().getNome() + "-"
					+ reserva.getSala().getBloco().getCampus().getInstituicao().getNome());
			reserva1.setHorario(
					  reserva.getHorario().getDiaSemana() + "/"
					+ reserva.getHorario().getPeriodo());
			reserva1.setTurma(
					  reserva.getTurma().getDisciplina().getNome() + "-"
					+ reserva.getTurma().getProfessor());
		}
		
		if(turmaBanco.getReserva2() != null) {
			Reserva reserva = turmaBanco.getReserva2();
			reserva2.setId(reserva.getId());
			reserva2.setSala(
					  reserva.getSala().getNome() + "-" 
					+ reserva.getSala().getBloco().getNome() + "-" 
					+ reserva.getSala().getBloco().getCampus().getNome() + "-"
					+ reserva.getSala().getBloco().getCampus().getInstituicao().getNome());
			reserva2.setHorario(
					  reserva.getHorario().getDiaSemana() + "/"
					+ reserva.getHorario().getPeriodo());
			reserva2.setTurma(
					  reserva.getTurma().getDisciplina().getNome() + "-"
					+ reserva.getTurma().getProfessor());
		}
		
		TurmaData turma = new TurmaData(
				turmaBanco.getId(),
				turmaBanco.getProfessor(),
				turmaBanco.getDisciplina().getNome(),
				reserva1,
				reserva2);
		; 
		
		return new ResponseEntity<TurmaData>(turma, HttpStatus.OK);
	}
	
}
