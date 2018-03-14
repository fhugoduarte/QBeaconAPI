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

import com.tcc.qbeacon.datas.DisciplinaData;
import com.tcc.qbeacon.datas.TurmaData;
import com.tcc.qbeacon.model.Disciplina;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.service.DisciplinaService;

@RestController
@RequestMapping("/api/disciplina")
public class DisciplinaResource {

	@Autowired
	DisciplinaService disciplinaService;
	
	@GetMapping
	public ResponseEntity<List<DisciplinaData>> listaDisciplinas(){
		List<Disciplina> disciplinasBanco = disciplinaService.pegarDisciplinas();
		List<DisciplinaData> disciplinas = new ArrayList<DisciplinaData>();
		
		for (Disciplina disciplina : disciplinasBanco) {
			disciplinas.add(new DisciplinaData(disciplina.getId(), disciplina.getNome(), null));
		}
		
		return new ResponseEntity<List<DisciplinaData>>(disciplinas, HttpStatus.OK);
	}
	
	@GetMapping(path="{id}")
	public ResponseEntity<DisciplinaData> buscarDisciplina(@PathVariable("id") Integer id){
		Disciplina disciplinaBanco = disciplinaService.buscarDisciplina(id);
		List<TurmaData> turmas = new ArrayList<TurmaData>();
		
		if(!disciplinaBanco.getTurmas().isEmpty()) {
			for (Turma turma : disciplinaBanco.getTurmas()) {
				turmas.add(new TurmaData(
						turma.getId(),
						turma.getProfessor(),
						turma.getDisciplina().getNome(),
						null,
						null
						));
			}
		}
		
		return new ResponseEntity<DisciplinaData>(
				new DisciplinaData(disciplinaBanco.getId(),
						disciplinaBanco.getNome(),
						turmas), HttpStatus.OK);
	}
}
