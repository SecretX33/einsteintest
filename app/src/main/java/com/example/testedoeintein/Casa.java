package com.example.testedoeintein;

public class Casa {
    public int pos;
    public int cor;
    public int nacionalidade;
    public int bebida;
    public int cigarro;
    public int animal;

    public Casa(int pos) {
        this.pos = pos;
        cor=nacionalidade=bebida=cigarro=animal=0;
    }

    public Casa(int pos, int cor, int nacionalidade, int bebida, int cigarro, int animal) {
        this.pos = pos;
        this.cor = cor;
        this.nacionalidade = nacionalidade;
        this.bebida = bebida;
        this.cigarro = cigarro;
        this.animal = animal;
    }
}
