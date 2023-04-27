package br.com.lufecrx.cm;

import br.com.lufecrx.cm.modelo.Tabuleiro;
import br.com.lufecrx.cm.visao.TabuleiroConsole;

public class Aplicacao {

	public static void main(String[] args) {
		
		Tabuleiro tabuleiro = new Tabuleiro(16, 16, 40);
		new TabuleiroConsole(tabuleiro);
	}
}
