# ChefBoom!

*ChefBoom!* é um jogo 2D em Java, criado com LibGDX e arquitetura ECS (Artemis-ODB), onde o jogador assume o papel de um chef que precisa atender rapidamente os pedidos dos clientes antes que percam a paciência. O gameplay mistura ação, estratégia e gerenciamento de tempo, exigindo decisões ágeis para manter todos satisfeitos.

Desenvolvido como parte da disciplina de Sistemas Operacionais, o projeto aplica conceitos de threads e semaforização para sincronizar e coordenar múltiplas entidades e eventos em tempo real no contexto de um jogo.

### Mecânicas

- Prepare diferentes tipos de alimentos:
    - Batatas fritas
    - Hambúrgueres
    - Refrigerantes
    - Donuts

- Gerencie o seu tempo:
    - Atenda os clientes antes que percam a paciência
    - Use as máquinas de forma eficiente
    - Colete os pedidos prontos
    - Entregue os pedidos corretos

## Requisitos

- Java 8 ou superior
- Gradle

## Como rodar

1. Clone o repositório:

```bash
   git clone https://github.com/GabrielMF771/ChefBoom
```

2. Compile o projeto:

```bash
   ./gradlew desktop:shadowJar
```

3. Execute o jogo:

```bash
   java -jar desktop/build/libs/chefboom-1.0.jar
```

## Estrutura do Projeto

- `core/`: Código principal do jogo (lógica, entidades, sistemas, telas).
- `desktop/`: Launcher desktop e configurações específicas.
- `assets/`: Recursos do jogo (imagens, sons, fontes).

## Controles

- **Setas/WASD**: Movimentação
- **Shift**: Dash para frente
- **E**: Interagir/entregar item
- **ESC**: Sair do jogo
- **F1**: Mostrar/ocultar console

**(MODO DEBUG)**

- **CTRL + R**: Reiniciar tela de jogo
- **B**: Alternar janela de debug de entidades
- **C**: Alternar visualização de colisões

## Créditos

Desenvolvido por André Marcos de Souza Batista, Gabriel Martins Fernandes, Gustavo Henrique Sousa de Jesus, Henrique Dantas Faria e João Lucas Cavalcante Borges.

---

Este projeto utiliza LibGDX, Artemis-ODB e outras bibliotecas open source.
