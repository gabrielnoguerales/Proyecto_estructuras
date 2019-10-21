package es.uned.lsi.eped.pract2018_2019;

import es.uned.lsi.eped.DataStructures.BTreeIF;
import es.uned.lsi.eped.DataStructures.Stack;

public class StackMachine {

	private Stack<Operand> operands;

	StackMachine() {
		operands = new Stack<>();
	}
	
	Operand execute(SynTree syn) {
		postorder(syn.getSynTree());
		Operand solve = operands.getTop();
		operands.clear();
		return solve;
	}

	private void calculate(Operator operator,Operand operand1,Operand operand2) {
		switch (operator.getOperatorType()){
			case ADD:
				operand2.add(operand1);
				this.operands.push(operand2);
				break;
			case SUB:
				operand2.sub(operand1);
				this.operands.push(operand2);
				break;
			case MULT:
				operand2.mult(operand1);
				this.operands.push(operand2);

		}
	}

	private void postorder(BTreeIF syn) {
		if(syn.getLeftChild() != null) postorder(syn.getLeftChild());
		if(syn.getRightChild() != null) postorder(syn.getRightChild());
		if(syn.getRoot().getClass() == Operator.class){
			Operand operand1 = this.operands.getTop();
			this.operands.pop();
			Operand operand2 = this.operands.getTop();
			this.operands.pop();
			calculate((Operator) syn.getRoot(),operand1,operand2);
		}
		else this.operands.push((Operand) syn.getRoot());
	}

}
