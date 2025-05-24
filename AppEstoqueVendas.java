import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Produto {
    private String nome;
    private double preco;
    private int quantidade;

    public Produto(String nome, double preco, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }

    public void vender(int quantidadeVendida) {
        this.quantidade -= quantidadeVendida;  // agora também pode repor estoque
    }
}

class Venda {
    private Produto produto;
    private int quantidade;
    private double total;

    public Venda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.total = produto.getPreco() * quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getTotal() { return total; }
}

public class AppEstoqueVendas {
    private static ArrayList<Produto> produtos = new ArrayList<>();
    private static ArrayList<Venda> vendas = new ArrayList<>();
    private static JTable tabelaProdutos;
    private static JTable tabelaVendas;
    private static DefaultTableModel modeloProdutos;
    private static DefaultTableModel modeloVendas;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName()))
                    UIManager.setLookAndFeel(info.getClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("Venda Fácil - desktop");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        Color corFundo = new Color(44, 62, 80);
        Font fonteBotao = new Font("Segoe UI", Font.BOLD, 14);

        JTextField campoBusca = new JTextField(15);
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnBuscar = criarBotao("Buscar", new Color(52, 152, 219), fonteBotao);

        modeloProdutos = new DefaultTableModel(new Object[]{"Nome", "Preço", "Quantidade"}, 0);
        tabelaProdutos = new JTable(modeloProdutos);
        JScrollPane scrollProdutos = new JScrollPane(tabelaProdutos);

        modeloVendas = new DefaultTableModel(new Object[]{"Produto", "Quantidade", "Total"}, 0);
        tabelaVendas = new JTable(modeloVendas);
        JScrollPane scrollVendas = new JScrollPane(tabelaVendas);

        JButton btnAdicionar = criarBotao("Adicionar Produto", new Color(52, 152, 219), fonteBotao);
        JButton btnVender = criarBotao("Vender Produto", new Color(39, 174, 96), fonteBotao);
        JButton btnExcluirVenda = criarBotao("Excluir Venda", new Color(231, 76, 60), fonteBotao);
        JButton btnExcluirProduto = criarBotao("Excluir Produto", new Color(243, 156, 18), fonteBotao);
        JButton btnRelatorio = criarBotao("Relatório de Vendas", new Color(149, 165, 166), fonteBotao);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBusca.setBackground(corFundo);
        painelBusca.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelBusca.add(campoBusca);
        painelBusca.add(btnBuscar);

        // Carregar a imagem da logo
        ImageIcon logoIcon = new ImageIcon("crie_uma_logo_com_o_nome__VendaFácil_-removebg-preview.png");
        Image img = logoIcon.getImage().getScaledInstance(170, 50, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(new EmptyBorder(10, 10, 10, 100));

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(corFundo);
        painelSuperior.add(logoLabel, BorderLayout.WEST);
        painelSuperior.add(painelBusca, BorderLayout.EAST);

        JPanel centro = new JPanel(new GridLayout(2, 1));
        centro.add(scrollProdutos);
        centro.add(scrollVendas);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.X_AXIS));
        painelBotoes.setBackground(corFundo);
        painelBotoes.setBorder(new EmptyBorder(10, 10, 10, 10));

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnVender);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnExcluirVenda);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnExcluirProduto);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnRelatorio);

        frame.add(painelSuperior, BorderLayout.NORTH);
        frame.add(centro, BorderLayout.CENTER);
        frame.add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> {
            try {
                String nome = JOptionPane.showInputDialog(frame, "Nome do Produto:");
                String precoStr = JOptionPane.showInputDialog(frame, "Preço do Produto (apenas número):");
                String qtdStr = JOptionPane.showInputDialog(frame, "Quantidade em Estoque:");
                if (nome == null || precoStr == null || qtdStr == null || nome.trim().isEmpty()) return;
                double preco = Double.parseDouble(precoStr);
                int quantidade = Integer.parseInt(qtdStr);
                produtos.add(new Produto(nome, preco, quantidade));
                atualizarTabelaProdutos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Entrada inválida.");
            }
        });

        btnVender.addActionListener(e -> {
            int linha = tabelaProdutos.getSelectedRow();
            if (linha >= 0) {
                Produto produto = produtos.get(linha);
                String qtdStr = JOptionPane.showInputDialog(frame, "Quantidade a vender:");
                try {
                    int quantidade = Integer.parseInt(qtdStr);
                    if (quantidade <= produto.getQuantidade() && quantidade > 0) {
                        produto.vender(quantidade);
                        vendas.add(new Venda(produto, quantidade));
                        atualizarTabelaProdutos();
                        atualizarTabelaVendas();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Quantidade inválida ou insuficiente.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Quantidade inválida.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um produto para vender.");
            }
        });

        btnExcluirVenda.addActionListener(e -> {
            int linha = tabelaVendas.getSelectedRow();
            if (linha >= 0) {
                Venda venda = vendas.get(linha);
                Produto produto = venda.getProduto();
                int quantidadeVendida = venda.getQuantidade();

                // Repor a quantidade ao estoque
                produto.vender(-quantidadeVendida);

                vendas.remove(linha);
                atualizarTabelaProdutos();
                atualizarTabelaVendas();
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione uma venda para excluir.");
            }
        });

        btnExcluirProduto.addActionListener(e -> {
            int linha = tabelaProdutos.getSelectedRow();
            if (linha >= 0) {
                produtos.remove(linha);
                atualizarTabelaProdutos();
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um produto para excluir.");
            }
        });

        btnRelatorio.addActionListener(e -> {
            double total = vendas.stream().mapToDouble(Venda::getTotal).sum();
            JOptionPane.showMessageDialog(frame,
                    String.format("Total de Vendas: R$ %.2f\nQuantidade de vendas: %d", total, vendas.size()),
                    "Relatório de Vendas", JOptionPane.INFORMATION_MESSAGE);
        });

        btnBuscar.addActionListener(e -> {
            String textoBusca = campoBusca.getText().trim().toLowerCase();
            modeloProdutos.setRowCount(0);
            for (Produto p : produtos) {
                if (p.getNome().toLowerCase().contains(textoBusca)) {
                    modeloProdutos.addRow(new Object[]{p.getNome(), String.format("R$ %.2f", p.getPreco()), p.getQuantidade()});
                }
            }
        });

        frame.setVisible(true);
    }

    private static JButton criarBotao(String texto, Color cor, Font fonte) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(fonte);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setOpaque(false);
        botao.setPreferredSize(new Dimension(150, 40));
        return botao;
    }

    private static void atualizarTabelaProdutos() {
        modeloProdutos.setRowCount(0);
        for (Produto p : produtos) {
            modeloProdutos.addRow(new Object[]{p.getNome(), String.format("R$ %.2f", p.getPreco()), p.getQuantidade()});
        }
    }

    private static void atualizarTabelaVendas() {
        modeloVendas.setRowCount(0);
        for (Venda v : vendas) {
            modeloVendas.addRow(new Object[]{
                    v.getProduto().getNome(),
                    v.getQuantidade(),
                    String.format("R$ %.2f", v.getTotal())
            });
        }
    }
}
