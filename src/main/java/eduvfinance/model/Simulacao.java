package eduvfinance.model;

import eduvfinance.util.Identificavel;

public class Simulacao implements Identificavel {

    private int id;
    private double aporteInicial;
    private double aporteMensal;
    private double taxa;
    private int periodo;
    private TipoInvestimento tipo;

    public Simulacao(double aporteInicial, double aporteMensal, double taxa,
                     int periodo, TipoInvestimento tipo) {
        this.aporteInicial = aporteInicial;
        this.aporteMensal = aporteMensal;
        this.taxa = taxa;
        this.periodo = periodo;
        this.tipo = tipo;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }

    public double getAporteInicial() { return aporteInicial; }
    public void setAporteInicial(double aporteInicial) { this.aporteInicial = aporteInicial; }

    public double getAporteMensal() { return aporteMensal; }
    public void setAporteMensal(double aporteMensal) { this.aporteMensal = aporteMensal; }

    public double getTaxa() { return taxa; }
    public void setTaxa(double taxa) { this.taxa = taxa; }

    public int getPeriodo() { return periodo; }
    public void setPeriodo(int periodo) { this.periodo = periodo; }

    public TipoInvestimento getTipo() { return tipo; }
    public void setTipo(TipoInvestimento tipo) { this.tipo = tipo; }

    public double calcularMontante() {
        double i = taxa / 100.0;
        double montante = aporteInicial;
        for (int mes = 0; mes < periodo; mes++) {
            montante = montante * (1 + i) + aporteMensal;
        }
        return montante;
    }

    public String getMontanteFormatado() { return String.format("%.2f", calcularMontante()); }

    @Override public String toString() {
        return "Simulacao #" + id + " (" + tipo + ", " + periodo + " meses)";
    }
}
