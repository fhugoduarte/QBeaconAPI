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

import com.tcc.qbeacon.datas.ReservaData;
import com.tcc.qbeacon.datas.SalaData;
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.SalaService;

@RestController
@RequestMapping("/api/sala")
public class SalaResource {
	
	@Autowired
	SalaService salaService;
	
	@GetMapping
	public ResponseEntity<List<SalaData>> listaSalas(){
		List<Sala> salasBanco = salaService.pegarSalas();
		List<SalaData> salas = new ArrayList<SalaData>();
		
		for (Sala sala : salasBanco) {
			if(sala.getBeacon() != null) {
				salas.add(new SalaData(sala.getId(), sala.getNome(), 
					sala.getBloco().getNome(), 
					sala.getBloco().getCampus().getNome(),
					sala.getBloco().getCampus().getInstituicao().getNome(),
					sala.getBeacon().getId(), new ArrayList<ReservaData>()));
			}else {
				salas.add(new SalaData(sala.getId(), sala.getNome(), 
						sala.getBloco().getNome(), 
						sala.getBloco().getCampus().getNome(),
						sala.getBloco().getCampus().getInstituicao().getNome(),
						null, new ArrayList<ReservaData>()));
			}

		}
		
		return new ResponseEntity<List<SalaData>>(salas, HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<SalaData> buscaSala(@PathVariable("id") Integer id){
		Sala salaBanco = salaService.buscarSala(id);		
		
		if(salaBanco.getBeacon() != null) {
			if(salaBanco.getReservas().isEmpty()) {
				SalaData sala = new SalaData(salaBanco.getId(), salaBanco.getNome(), 
					salaBanco.getBloco().getNome(), 
					salaBanco.getBloco().getCampus().getNome(),
					salaBanco.getBloco().getCampus().getInstituicao().getNome(),
					salaBanco.getBeacon().getId(), new ArrayList<ReservaData>());
				
				return new ResponseEntity<SalaData>(sala, HttpStatus.OK);
			}else {
				List<ReservaData> reservas = new ArrayList<ReservaData>();
				for (Reserva reserva : salaBanco.getReservas()) {
					reservas.add(new ReservaData(
							reserva.getId(),
							reserva.getHorario().getDiaSemana() + "/"
									+ reserva.getHorario().getPeriodo(),
							reserva.getSala().getNome() + "-" 
									+ reserva.getSala().getBloco().getNome() + "-" 
									+ reserva.getSala().getBloco().getCampus().getNome() + "-"
									+ reserva.getSala().getBloco().getCampus().getInstituicao().getNome(),
							reserva.getTurma().getDisciplina().getNome() + "-"
									+ reserva.getTurma().getProfessor()
							));
				}
				
				SalaData sala = new SalaData(salaBanco.getId(), salaBanco.getNome(), 
						salaBanco.getBloco().getNome(), 
						salaBanco.getBloco().getCampus().getNome(),
						salaBanco.getBloco().getCampus().getInstituicao().getNome(),
						salaBanco.getBeacon().getId(), reservas);
				
				return new ResponseEntity<SalaData>(sala, HttpStatus.OK);
			}
			
		}else {
			if(salaBanco.getReservas().isEmpty()) {
				SalaData sala = new SalaData(salaBanco.getId(), salaBanco.getNome(), 
					salaBanco.getBloco().getNome(), 
					salaBanco.getBloco().getCampus().getNome(),
					salaBanco.getBloco().getCampus().getInstituicao().getNome(),
					salaBanco.getBeacon().getId(), new ArrayList<ReservaData>());
				
				return new ResponseEntity<SalaData>(sala, HttpStatus.OK);
			}else {
				List<ReservaData> reservas = new ArrayList<ReservaData>();
				for (Reserva reserva : salaBanco.getReservas()) {
					reservas.add(new ReservaData(
							reserva.getId(),
							reserva.getHorario().getDiaSemana() + "/"
									+ reserva.getHorario().getPeriodo(),
							reserva.getSala().getNome() + "-" 
									+ reserva.getSala().getBloco().getNome() + "-" 
									+ reserva.getSala().getBloco().getCampus().getNome() + "-"
									+ reserva.getSala().getBloco().getCampus().getInstituicao().getNome(),
							reserva.getTurma().getDisciplina().getNome() + "-"
									+ reserva.getTurma().getProfessor()
							));
				}
				
				SalaData sala = new SalaData(salaBanco.getId(), salaBanco.getNome(), 
						salaBanco.getBloco().getNome(), 
						salaBanco.getBloco().getCampus().getNome(),
						salaBanco.getBloco().getCampus().getInstituicao().getNome(),
						null, reservas);
				
				return new ResponseEntity<SalaData>(sala, HttpStatus.OK);
			}
		}
	
	
	}

}
