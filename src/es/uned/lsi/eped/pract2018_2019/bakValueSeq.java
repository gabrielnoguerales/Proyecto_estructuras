package es.uned.lsi.eped.pract2018_2019;

import es.uned.lsi.eped.DataStructures.Stack;

public class ValueSeq extends Value {

	public Stack<Integer> value;

	/* Constructor: recibe un String con el valor numérico */
	public ValueSeq(String s) {
		s = s.replaceFirst("^0+(?!$)", "");
		this.value = new Stack<>();
		for(char num : s.toCharArray()){
			this.value.push(Integer.parseInt(String.valueOf(num)));
		}
	}

	/* Método que transforma el valor numérico en un String */
	public String toString() {
		String number;
		Stack tmp =new Stack(this.value);
		number = convert(tmp);
		return number;
	}

	private String convert(Stack tmp) {
		String number = "";
		if(!tmp.isEmpty()){
			String t1 = tmp.getTop().toString();
			tmp.pop();
			number = convert(tmp);
			number += t1;
		}
		return number;
	}

	/* Método que modifica el valor numérico llamante, sumándole el valor numérico parámetro */
	public void addValue(Value n) {
		ValueSeq nseq = (ValueSeq) n;
		if(nseq.value.isEmpty()) return; // + 0
		if(this.value.isEmpty()) { // Value + 0
			this.value = nseq.value;
			return;
		}
		this.sum(nseq,false);

	}

	private void sum(ValueSeq nseq,Boolean acarreo) {
		Integer sum;
		if(nseq.value.isEmpty()){ //Only this.value contains values
			sum = this.value.getTop();
			this.value.pop();
		}
		else if(this.value.isEmpty()){ //Only nseq.value contains values
			sum = nseq.value.getTop();
			nseq.value.pop();
		}
		else{ //Both contains values
			sum = this.value.getTop() + nseq.value.getTop();
			this.value.pop();
			nseq.value.pop();
		}
		if(acarreo) sum++; //Acarreo de la anterior iteracion?
		acarreo = sum > 9; //Acarreo para la proxima?
		if(!nseq.value.isEmpty() || !this.value.isEmpty()) sum(nseq,acarreo); //Next iteration
		else if (acarreo) this.value.push(1); //Acarreo on the biggest digit
		this.value.push(sum%10); //push to the stack
	}

	/* Método que modifica el valor numérico llamante, restándole el valor numérico parámetro */
	/* Sabemos que el mayor es el valor numérico llamante */
	public void subValue(Value n) {
		ValueSeq nseq = (ValueSeq) n;
		if(nseq.value.isEmpty()) return; // x - 0
		this.diminish(nseq,false);
	}

	private void diminish(ValueSeq nseq, boolean acarreo) {
		Integer diff;
		boolean acarreofuturo;
		if(nseq.value.isEmpty()){
			diff = this.value.getTop();
			this.value.pop();
		}
		else if(!nseq.value.isEmpty() && !this.value.isEmpty()){
			if(nseq.value.getTop() > this.value.getTop() || (
					nseq.value.getTop().equals(this.value.getTop()) && acarreo
			)){
				diff = (this.value.getTop() + 10) - nseq.value.getTop();
				acarreofuturo = true;
				this.value.pop();
				nseq.value.pop();
			}
			else{
				diff = this.value.getTop() - nseq.value.getTop();
				acarreofuturo = false;
				this.value.pop();
				nseq.value.pop();
 			}
			if(acarreo) diff--;
			if(!nseq.value.isEmpty()) diminish(nseq,acarreofuturo); //Next iteration
			else if(acarreofuturo){
				int tmp =this.value.getTop();
				this.value.pop();
				if(tmp != 0){
					this.value.push(tmp-1);
				}else {
					this.value.push(9);
					int contador = 0;
					while(tmp==0){
						 this.value.pop();
						 tmp = this.value.getTop();
						 contador++;
					}
					this.value.pop();
					if(tmp != 1) {
						this.value.push(tmp-1);
					}
					if(contador == 1){
						this.value.push(0);
					}
					while(contador != 0){
						this.value.push(9);
						contador--;
					}
				}
			}
			this.value.push(diff);
		}
	}

	/* Método que modifica el valor numérico llamante, restándolo del valor numérico parámetro */
	/* Sabemos que el mayor es el valor numérico parámetro */
	public void subFromValue(Value n) {
		ValueSeq nseq = (ValueSeq) n;
		if(this.value.isEmpty()) return; // x - 0
		nseq.diminish(this,false);
	}

	/* Método que modifica el valor numérico llamante, multiplicándolo por el valor numérico parámetro */
	public void multValue(Value n) {

	}

	/* Método que indica si el valor numérico llamante es mayor que el valor numérico parámetro */
	public boolean greater(Value n) {
		ValueSeq nseq = (ValueSeq) n;
		if(this.toString().length() > nseq.toString().length()){
			return true;
		}
		else if(nseq.toString().length() > this.toString().length()){
			return false;
		}
		else{
			String nseq_str = nseq.toString();
			String this_str = this.toString();
			for(int i = 0;this.value.size() > i;i++){
				if(Integer.parseInt(String.valueOf(nseq_str.charAt(i))) > Integer.parseInt(String.valueOf(this_str.charAt(i)))) return false;
			}
			return true;
		}
	}

	/* Método que indica si el valor numérico es cero */
	public boolean isZero() {
		Stack<Integer>  aux = new Stack<>(this.value);
		int contador = 0;
		while(!aux.isEmpty() && contador == 0){
			contador += aux.getTop();
			aux.pop();
		}
		return (contador == 0);
	}

}
