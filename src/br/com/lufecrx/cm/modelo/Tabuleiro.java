package br.com.lufecrx.cm.modelo;

import java.util.ArrayList;

import java.util.List;
import java.util.function.Predicate;

import br.com.lufecrx.cm.excecao.ExplosaoException;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private int minas;

	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}

	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.abrir());
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}

	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
				.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
				.ifPresent(c -> c.alternarMarcacao());
	}

	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> camposMinados = c -> c.isMinado();

		do {
			minasArmadas = campos.stream().filter(camposMinados).count();
			int aleatorio = (int) (Math.random() * campos.size());
			if (minasArmadas < this.minas) {
				campos.get(aleatorio).minar();
			}
		} while (minasArmadas < this.minas);
	}

	// Percorrer a lista 2 vezes e tentar associar todos os campos
	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				// O método adicionarVizinho() vai definir quem pode ou não ser vizinho
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void gerarCampos() {
		for (int lin = 0; lin < linhas; lin++) {
			for (int col = 0; col < colunas; col++) {
				campos.add(new Campo(lin, col));
			}
		}
	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());

	}

	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciarJogo());
		sortearMinas();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
	
		sb.append("  ");
	
		for(int col = 0; col < colunas; col++){
			if(col > 9) {
				sb.append("| ");
				sb.append(col);
				sb.append("| ");
			} else {
				sb.append("| ");
				sb.append(col);
				sb.append(" | ");				
			}
		}
		
		sb.append("\n");

		for (int lin = 0; lin < linhas; lin++) {	
			if(lin > 9) {
				sb.append(lin);
			} else {
				sb.append(lin);
				sb.append(" ");				
			}
		
			for (int col = 0; col < colunas; col++) {

				sb.append("| ");
				sb.append(campos.get(i));
				sb.append(" | ");
				i++;
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public List<Campo> getCampos() {
		return campos;
	}
}
