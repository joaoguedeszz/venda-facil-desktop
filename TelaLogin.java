import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login - Venda Fácil");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 550);
        frame.setLocationRelativeTo(null);

        JPanel painel = new JPanel();
        painel.setBackground(new Color(236, 240, 241));
        painel.setLayout(null);

        // Carregar a imagem da logo
        ImageIcon logoIcon = new ImageIcon("crie_uma_logo_com_o_nome__VendaFácil_-removebg-preview.png");
        Image img = logoIcon.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);

        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(100, 30, 200, 80);
        painel.add(logoLabel);

        JLabel lblUsuario = new JLabel("Usuário");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setBounds(50, 150, 300, 25);
        painel.add(lblUsuario);

        JTextField campoUsuario = new JTextField();
        campoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoUsuario.setBounds(50, 180, 300, 35);
        campoUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        painel.add(campoUsuario);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSenha.setBounds(50, 230, 300, 25);
        painel.add(lblSenha);

        JPasswordField campoSenha = new JPasswordField();
        campoSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoSenha.setBounds(50, 260, 300, 35);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        painel.add(campoSenha);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(50, 320, 300, 40);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String senha = new String(campoSenha.getPassword());

                if (usuario.equals("admin") && senha.equals("1234")) {
                    JOptionPane.showMessageDialog(frame, "Login realizado com sucesso!");
                    frame.dispose();
                    // Chama o sistema de estoque e vendas
                    AppEstoqueVendas.main(null);
                } else {
                    JOptionPane.showMessageDialog(frame, "Usuário ou senha incorretos.");
                }
            }
        });

        painel.add(btnLogin);

        frame.add(painel);
        frame.setVisible(true);
    }
}
