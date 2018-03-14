package com.tcc.qbeacon.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.datas.BeaconData;
import com.tcc.qbeacon.model.Beacon;
import com.tcc.qbeacon.service.BeaconService;

@RestController
@RequestMapping("/api/beacon")
public class BeaconResource {

	@Autowired
	BeaconService beaconService;
	
	@GetMapping(path="/{id}")
	public ResponseEntity<BeaconData> buscar(@PathVariable("id") Integer id){
		Beacon beaconBanco = beaconService.buscarBeacon(id);
		
		if(beaconBanco.getSala() != null) {
			BeaconData beacon = new BeaconData(beaconBanco.getId(), 
					beaconBanco.getNome(), 
					beaconBanco.getSala().getId());
			
			return new ResponseEntity<BeaconData>(beacon, HttpStatus.OK);
		}else {
			BeaconData beacon = new BeaconData(beaconBanco.getId(), 
					beaconBanco.getNome(), 
					null);
			
			return new ResponseEntity<BeaconData>(beacon, HttpStatus.OK);
		}
	}
	
}
