package application;

import java.util.*;

public class Mensaje extends Observable{
	private DataChat msj;
	public DataChat getMsj() {
		return msj;
	}
	public void setMsj(DataChat msj) {
		this.msj = msj;
		this.setChanged();
		this.notifyObservers(this.getMsj());
	}
	
	
}
