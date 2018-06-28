package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.tcc.qbeacon.datas.SalaProximaData;
import com.tcc.qbeacon.datas.TurmaData;
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.SalaService;

@RestController
@RequestMapping("/api/sala")
public class SalaResource {
	
	@Autowired
	SalaService salaService;
	
	@Autowired
	ReservaService reservaService;
	
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
	
	@GetMapping(path="/proxima/{id}")
	public ResponseEntity<SalaProximaData> buscarSalaProxima(@PathVariable("id") Integer id){
		Sala salaBanco = salaService.buscarSala(id);
		SalaProximaData salaProxima = new SalaProximaData();
		if(salaBanco!= null) {
			if(salaBanco.getBeacon() != null) {
				SalaData sala =  new SalaData(salaBanco.getId(), salaBanco.getNome(), 
						salaBanco.getBloco().getNome(), 
						salaBanco.getBloco().getCampus().getNome(),
						salaBanco.getBloco().getCampus().getInstituicao().getNome(),
						salaBanco.getBeacon().getId(), new ArrayList<ReservaData>());
				salaProxima.setSalaProxima(sala);
			}else if(salaBanco.getBeacon() == null) {
				SalaData sala =  new SalaData(salaBanco.getId(), salaBanco.getNome(), 
						salaBanco.getBloco().getNome(), 
						salaBanco.getBloco().getCampus().getNome(),
						salaBanco.getBloco().getCampus().getInstituicao().getNome(),
						null, new ArrayList<ReservaData>());
				salaProxima.setSalaProxima(sala);
			}
			Calendar calendar = new GregorianCalendar();
			 Date trialTime = new Date();
			 calendar.setTime(trialTime);
			 
			String diaSemana = this.traduzDiaSemana(calendar.get(Calendar.DAY_OF_WEEK));
			String horario = this.traduzHorario(calendar.get(Calendar.HOUR_OF_DAY), Calendar.MINUTE);
			Reserva reservaBanco = reservaService.buscaReserva(salaBanco.getId(), horario, diaSemana);
			if(reservaBanco != null) {
				TurmaData turma = new TurmaData();
				turma.setId(reservaBanco.getTurma().getId());
				turma.setProfessor(reservaBanco.getTurma().getProfessor());
				turma.setDisciplina(reservaBanco.getTurma().getDisciplina().getNome());
				
				salaProxima.setTurmaAtual(turma);
				salaProxima.setReserva(reservaBanco.getId());
			}
		}
		return new ResponseEntity<SalaProximaData>(salaProxima, HttpStatus.OK);		
	}
	
	private String traduzDiaSemana(int diaSemana) {
		switch (diaSemana) {
		case 2:
			return "Segunda-Feira";
		case 3:
			return "Terça-Feira";
		case 4:
			return "Quarta-Feira";
		case 5:
			return "Quinta-Feira";
		case 6:
			return "Sexta-Feira";
		default:
			return "";
		}
	}
	
	private String traduzHorario(int hora, int minutos) {
		if(hora >= 8 && hora < 10) {
			return "08:00 às 10:00";
		}else if(hora >= 10 && hora < 12) {
			return "10:00 às 12:00";
		}else if((hora == 13 && minutos >= 30 ) || hora == 14 || (hora == 15 && minutos < 30)) {
			return "13:30 às 15:30";
		}else if((hora == 15 && minutos >= 30 ) || hora == 16 || (hora == 17 && minutos < 30)) {
			return "15:30 às 17:30";
		}else if(hora >= 18 && hora < 20) {
			return "18:00 às 20:00";
		}else if(hora >= 20 && hora < 22) {
			return "20:00 às 22:00";
		}else {
			return "";
		}
	} 

}
