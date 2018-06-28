package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.config.JwtEvaluator;
import com.tcc.qbeacon.datas.ReservaData;
import com.tcc.qbeacon.datas.TurmaData;
import com.tcc.qbeacon.exceptions.TokenException;
import com.tcc.qbeacon.model.Aula;
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.AulaService;
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.TurmaService;
import com.tcc.qbeacon.service.UsuarioService;
import com.tcc.qbeacon.util.Constants;
import com.tcc.qbeacon.util.MensagemRetorno;

@RestController
@RequestMapping("/api/turma")
public class TurmaResource {

	@Autowired
	TurmaService turmaService;
	
	@Autowired
	AulaService aulaService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	UsuarioService usuarioService;
	
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
	
	@PostMapping(path="{id_turma}/presenca/{id_reserva}")
	public MensagemRetorno darPresenca(@RequestBody @Valid Usuario usuario, 
			@PathVariable("id_turma") Integer id_turma, @PathVariable("id_reserva") Integer id_reserva) throws Exception{
		try {
			Turma turmaBanco = turmaService.buscarTurma(id_turma);
			Reserva reservaBanco = reservaService.buscarReserva(id_reserva);
			String data= this.getData();
			Aula aulaBanco = aulaService.buscarAula(turmaBanco.getId(), data);
			if(aulaBanco == null) {
				aulaBanco = this.criaAula(turmaBanco, data, reservaBanco);
			}
			
			this.adicionaAlunoFrequencia(usuario, aulaBanco);
			
			return new MensagemRetorno(Constants.SUCESSO_PRESENCA_USUARIO);
		} catch (Exception e) {
			throw new Exception(Constants.ERRO_PRESENCA_USUARIO);
		}
	}
	
	private void adicionaAlunoFrequencia(Usuario aluno, Aula aula) {
		List<Usuario> alunos = aula.getFrequencia();
		List<Aula> aulas = aluno.getAulas();
		if(!alunos.contains(aluno)) {
			alunos.add(aluno);
			aula.setFrequencia(alunos);
			aulaService.salvarAula(aula);
			
			aulas.add(aula);
			aluno.setAulas(aulas);
			usuarioService.atualizaUsuario(aluno);
		}
	}
	
	private Aula criaAula(Turma turma, String data, Reserva reserva) {
		Aula aula = new Aula();
		aula.setAssunto("");
		aula.setDia(data);
		aula.setTurma(turma);
		aula.setReserva(reserva);
		aulaService.salvarAula(aula);
		
		this.adicionaAulaReserva(aula, reserva);
		this.adicionaAulaTurma(aula, turma);
		return aula;
	}
	
	private void adicionaAulaTurma(Aula aula, Turma turma) {
		List<Aula> aulas = turma.getAulas();
		aulas.add(aula);
		turma.setAulas(aulas);
		turmaService.salvarTurma(turma);
	}
	
	private void adicionaAulaReserva(Aula aula, Reserva reserva) {
		List<Aula> aulas = reserva.getAulas();
		aulas.add(aula);
		reserva.setAulas(aulas);
		reservaService.salvarReserva(reserva);
	}
	
	private String getData() {
		Calendar calendar = new GregorianCalendar();
		 Date trialTime = new Date();
		 calendar.setTime(trialTime);
		 
		 String dia = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));
		 if(dia.length() == 1)
			 dia = "0" + dia;
		 
		 String mes = Integer.toString(calendar.get(Calendar.MONTH) + 1);
		 if(mes.length() == 1)
			 mes = "0" + mes;
		 
		 String ano = Integer.toString(calendar.get(Calendar.YEAR));

		 return dia + "/" + mes + "/" + ano;
	}
	
}
