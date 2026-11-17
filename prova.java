import java.util.Locale;
import java.util.Scanner;

class Conta {
    int id;
    String cliente;
    double saldo;
    double limite;
}

class Operacao {
    int id;
    int idConta;
    char tipo; // 'D', 'S', 'T'
    double valor;
}

public class Banco {
    public static final int TAM_CONTAS = 50;
    public static final int TAM_OP = 1000;
    static Scanner sc = new Scanner(System.in);

    public static int buscaId(Conta[] v, int tam, int x) {
        for (int i = 0; i < tam; i++) {
            if (v[i].id == x) {
                return i;
            }
        }
        return -1;
    }

    // Questão 1
    public static int cadastrarConta(Conta[] v, int tam) {
        if (tam >= TAM_CONTAS) {
            System.out.println("Vetor de contas cheio!");
            return tam;
        }

        Conta c = new Conta();
        System.out.print("ID: ");
        c.id = sc.nextInt();

        if (buscaId(v, tam, c.id) != -1) {
            System.out.println("ID duplicado!");
            return tam;
        }

        sc.nextLine();
        System.out.print("Cliente: ");
        c.cliente = sc.nextLine();

        System.out.print("Saldo: ");
        c.saldo = sc.nextDouble();

        System.out.print("Limite: ");
        c.limite = sc.nextDouble();

        if (c.saldo < 0 || c.limite < 0) {
            System.out.println("Saldo e limite não podem ser negativos!");
            return tam;
        }

        v[tam] = c;
        return tam + 1;
    }

    // Questão 2
    public static int buscaBinariaCliente(Conta[] v, int tam, String x) {
        int ini = 0;
        int fim = tam - 1;

        while (ini <= fim) {
            int meio = (ini + fim) / 2;
            int cmp = v[meio].cliente.compareTo(x);

            if (cmp == 0) {
                return meio;
            } else if (cmp < 0) {
                ini = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return -1;
    }

    // Questão 3
    public static int insertionSortCliente(Conta[] v, int tam) {
        for (int i = 1; i < tam; i++) {
            Conta chave = v[i];
            int j = i - 1;

            while (j >= 0 && v[j].cliente.compareTo(chave.cliente) > 0) {
                v[j + 1] = v[j];
                j--;
            }

            v[j + 1] = chave;
        }

        return tam;
    }

    // Questão 4
    public static int filtraOperacoes(Operacao[] vOp, int tamOp,
                                      Operacao[] vOpFiltrado, int x) {
        int tamFiltrado = 0;

        for (int i = 0; i < tamOp; i++) {
            if (vOp[i].idConta == x) {
                vOpFiltrado[tamFiltrado] = vOp[i];
                tamFiltrado++;
            }
        }

        return tamFiltrado;
    }

    // Questão 5
    public static void extrato(Conta[] vContas, int tamConta,
                               Operacao[] vOp, int tamOp) {
        System.out.print("ID da conta: ");
        int id = sc.nextInt();

        int pos = buscaId(vContas, tamConta, id);

        if (pos == -1) {
            System.out.println("Conta não encontrada!");
            return;
        }

        Operacao[] f = new Operacao[TAM_OP];
        int tamF = filtraOperacoes(vOp, tamOp, f, id);

        int inicio = Math.max(0, tamF - 5);
        double saldo = 0;

        for (int i = 0; i < inicio; i++) {
            saldo += f[i].valor;
        }

        char cdSaldo = (saldo >= 0) ? 'C' : 'D';

        System.out.printf("CONTA N.%03d%n", id);
        System.out.println("CLIENTE: " + vContas[pos].cliente);
        System.out.println("ID   | OPERAÇÃO       | VALOR    | SALDO");
        System.out.printf(Locale.US, "     | Saldo Anterior |          | %.2f%c%n",
                Math.abs(saldo), cdSaldo);

        for (int i = inicio; i < tamF; i++) {
            saldo += f[i].valor;

            char cdValor = (f[i].valor >= 0) ? 'C' : 'D';
            cdSaldo = (saldo >= 0) ? 'C' : 'D';

            String nome;
            if (f[i].tipo == 'D') {
                nome = "Depósito";
            } else if (f[i].tipo == 'S') {
                nome = "Saque";
            } else {
                nome = "Transferência";
            }

            System.out.printf(Locale.US, "%04d | %-14s | %.2f%c | %.2f%c%n",
                    f[i].id, nome,
                    Math.abs(f[i].valor), cdValor,
                    Math.abs(saldo), cdSaldo);
        }

        cdSaldo = (saldo >= 0) ? 'C' : 'D';
        System.out.printf(Locale.US, "Saldo Atual: %.2f%c%n", Math.abs(saldo), cdSaldo);
    }
}
