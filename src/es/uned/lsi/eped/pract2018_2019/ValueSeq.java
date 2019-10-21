package es.uned.lsi.eped.pract2018_2019;

import es.uned.lsi.eped.DataStructures.*;

public class ValueSeq extends Value {
	private List<Integer> value;

	/* Constructor: recibe un String con el valor numérico */
	public ValueSeq(String s) {
		s = s.replaceFirst("^0+(?!$)", "");
		this.value = new List<>();
		for (int i = 0; i < s.length(); i++) value.insert(i + 1, (int) s.charAt(i) - 48);
	}

	/* Método que transforma el valor numérico en un String */
	public String toString() {
		StringBuilder num = new StringBuilder();
		for (int i = 1; i <= this.value.size(); i++) {
			String num_char = this.value.get(i).toString();
			num.append(num_char);
		}
		return num.toString();
	}

	/* Método que modifica el valor numérico llamante, sumándole el valor numérico parámetro */
	public void addValue(Value n) {
		ValueSeq vSeq = (ValueSeq) n;
		List<Integer> result = new List<>();
		result = sum(result, vSeq, false);
		this.value = result;
	}

	private List<Integer> sum(List<Integer> result, ValueSeq vSeq, boolean acarreo) {
		int suma = 0;
		if (this.value.isEmpty() && !vSeq.value.isEmpty()) {
			suma = vSeq.value.get(vSeq.value.size());
			vSeq.value.remove(vSeq.value.size());
		} else if (!this.value.isEmpty() && vSeq.value.isEmpty()) {
			suma = this.value.get(this.value.size());
			this.value.remove(this.value.size());
		} else if (!this.value.isEmpty() && !vSeq.value.isEmpty()) {
			suma = this.value.get(this.value.size()) + vSeq.value.get(vSeq.value.size());
			this.value.remove(this.value.size());
			vSeq.value.remove(vSeq.value.size());
		}
		if (acarreo) suma++; //Acarreo de la anterior iteracion?
		boolean acarreoFuturo = suma > 9; //Acarreo de la siguiente iteracion?
		result.insert(1, suma % 10); //Insertamos el valor de suma
		if (!this.value.isEmpty() || !vSeq.value.isEmpty()) { //Hay siguiente ejecucion?
			result = sum(result, vSeq, acarreoFuturo); //Siguiente ejecucion
			acarreoFuturo = false; //acarreoFuturo ya se uso
		}
		if (acarreoFuturo) result.insert(1, 1); //Acarreo final?
		return result;
	}

	/* Método que modifica el valor numérico llamante, restándole el valor numérico parámetro */
	/* Sabemos que el mayor es el valor numérico llamante */
	public void subValue(Value n) {
		ValueSeq vSeq = (ValueSeq) n;
		List<Integer> result = new List<>();
		result = diff(result, vSeq, false);
		while (result.get(1) == 0) result.remove(1);
		this.value = result;
	}

	private List<Integer> diff(List<Integer> result, ValueSeq vSeq, boolean acarreo) {
		int resta = 0;
		boolean acarreoFuturo = false;
		if (!this.value.isEmpty() && vSeq.value.isEmpty()) {
			resta = this.value.get(this.value.size());
			this.value.remove(this.value.size());
		} else if (!this.value.isEmpty() && !vSeq.value.isEmpty()) {
			resta = this.value.get(this.value.size()) - vSeq.value.get(vSeq.value.size());
			this.value.remove(this.value.size());
			vSeq.value.remove(vSeq.value.size());
		}
		if (resta < 0) {
			resta += 10;
			acarreoFuturo = true;
			if (acarreo) resta--;
		} else if (resta == 0 && acarreo) {
			resta += 9;
			acarreoFuturo = true;
		} else {
			if (acarreo) resta--;
		}
		result.insert(1, resta % 10); //Insertamos el valor de suma
		if (!this.value.isEmpty() || !vSeq.value.isEmpty()) { //Hay siguiente ejecucion?
			result = diff(result, vSeq, acarreoFuturo); //Siguiente ejecucion
		}
		return result;

	}

	/* Método que modifica el valor numérico llamante, restándolo del valor numérico parámetro */
	/* Sabemos que el mayor es el valor numérico parámetro */
	public void subFromValue(Value n) {
		ValueSeq vSeq = (ValueSeq) n;
		List<Integer> result = new List<>();
		result = vSeq.diff(result, this, false);
		while (result.get(1) == 0 && result.size() > 1) result.remove(1);
		this.value = result;
	}

	/* Método que modifica el valor numérico llamante, multiplicándolo por el valor numérico parámetro */
	public void multValue(Value n) {
		int thisSize, vSeqSize, resultSize, partialSize, sizeDiff, multiplicador, res, acarreo, suma;
		List<Integer> parcial;
		List<Integer> result = new List<>(this.value);
		vSeqSize = ((ValueSeq) n).value.size();
		for (int i = 1; i <= result.size(); i++) result.set(i, 0);
		for (int i = 0; i < vSeqSize; i++) {
			multiplicador = ((ValueSeq) n).value.get(vSeqSize - i);
			acarreo = 0;
			thisSize = this.value.size();
			parcial = new List<>(this.value);

			while (thisSize > 0) {
				res = this.value.get(thisSize) * multiplicador + acarreo;
				acarreo = 0;
				while (res > 9) { // Max loop 8 times - 9*9+(Acarreo from 9*9)=89
					res -= 10;
					acarreo++;
				}
				parcial.set(thisSize, res);
				thisSize--;
			}
			if (acarreo > 0) parcial.insert(1, acarreo);
			for (int j = 0; j < i; j++) parcial.insert(parcial.size() + 1, 0);
			resultSize = result.size();
			partialSize = parcial.size();
			acarreo = 0;
			for (int j = 1; j <= resultSize; j++) {
				suma = result.get(resultSize - j + 1) + parcial.get(partialSize - j + 1) + acarreo;
				acarreo = suma > 9 ? 1 : 0;
				result.set(resultSize - j + 1, suma%10);
			}
			sizeDiff = partialSize - resultSize;
			while (sizeDiff > 0) {
				suma = parcial.get(sizeDiff) + acarreo;
				if (suma > 9) suma = 0;
				else acarreo = 0;
				result.insert(1, suma);
				sizeDiff--;
			}
			if (acarreo == 1) result.insert(1, 1);
		}
		this.value = result;
	}

	/* Método que indica si el valor numérico llamante es mayor que el valor numérico parámetro */
	public boolean greater(Value n) {
		ValueSeq vSeq = (ValueSeq) n;
		if (this.value.size() > vSeq.value.size()) return true; //Bigger
		else if (vSeq.value.size() > this.value.size()) return false; //Smaller
		else { //Equal at size need to compare digit by digit
			for (int i = 1; i <= this.value.size(); i++) {
				if (vSeq.value.get(i) < this.value.get(i)) return true;
				else if (vSeq.value.get(i) > this.value.get(i)) return false;
			}
			return false;
		}
	}

	/* Método que indica si el valor numérico es cero */
	public boolean isZero() {
		return this.value.get(1) == 0; //Cifra mas significativa 0?
	}
}
