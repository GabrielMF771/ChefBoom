package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;

public class InteractiveBlock extends Component {
    //TODO - Todo bloco interativo adicionado precisa ser adicionado aqui
    public enum Type {
        PLATE,
        TRASH,
        GRILL,
        SODAMACHINE,
        FRIESMACHINE,
    }

    public Type type;

    public boolean timerActive = false;
    public float timeLeft = 5f; // Tempo em segundos para gerar o item

}
