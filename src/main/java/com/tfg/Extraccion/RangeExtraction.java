package com.tfg.Extraccion;

public class RangeExtraction {
	
	private Integer initNumber;
	private Integer finalNumber;
	
	//En caso de tener un unico numero
	public RangeExtraction(Integer initNumber) {
		this.initNumber = initNumber;
		this.finalNumber = initNumber;
	}
	
	//En caso de tener un rango definido
	public RangeExtraction(Integer initNumber, Integer finalNumber) {
		this.initNumber = initNumber;
		this.finalNumber = finalNumber;
	}
	
	public Integer getInitNumber() {
		return initNumber;
	}
	public void setInitNumber(Integer initNumber) {
		this.initNumber = initNumber;
	}
	public Integer getFinalNumber() {
		return finalNumber;
	}
	public void setFinalNumber(Integer finalNumber) {
		this.finalNumber = finalNumber;
	}
	public String toString() {
		if(initNumber == finalNumber)
			return initNumber.toString();
		else
			return initNumber + "-" + finalNumber;
	}
	
	
}
