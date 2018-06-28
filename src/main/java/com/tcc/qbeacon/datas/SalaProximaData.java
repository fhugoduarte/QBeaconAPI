package com.tcc.qbeacon.datas;

public class SalaProximaData {

	private SalaData salaProxima;
	private TurmaData turmaAtual;
	private int reserva;

	public SalaProximaData() {
		
	}
	
	public SalaProximaData(SalaData sala, TurmaData turma, int reserva) {
		super();
		this.salaProxima = sala;
		this.turmaAtual = turma;
		this.reserva = reserva;
	}

	public SalaData getSalaProxima() {
		return salaProxima;
	}

	public void setSalaProxima(SalaData salaProxima) {
		this.salaProxima = salaProxima;
	}

	public TurmaData getTurmaAtual() {
		return turmaAtual;
	}

	public void setTurmaAtual(TurmaData turmaAtual) {
		this.turmaAtual = turmaAtual;
	}

	public int getReserva() {
		return reserva;
	}

	public void setReserva(int reserva) {
		this.reserva = reserva;
	}
	
}
