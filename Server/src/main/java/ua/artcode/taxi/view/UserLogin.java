package ua.artcode.taxi.view;

import ua.artcode.taxi.model.ClientAccessToken;
import ua.artcode.taxi.model.UserIdentifier;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.view.driver_view.DriverMenu;
import ua.artcode.taxi.view.passenger_view.MakeOrderAnonymous;
import ua.artcode.taxi.view.passenger_view.PassengerMenu;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLogin extends JFrame {

    private final UserService userService;

    private JLabel helloLabel;
    private JLabel nullLabel;
    private JLabel phoneLabel;
    private JTextField phoneText;
    private JLabel passLabel;
    private JTextField passText;
    private JPanel buttonPanel1;
    private JPanel buttonPanel2;

    private JButton loginButton;
    private JButton registerButton;
    private JButton gonowButton;

    public UserLogin(UserService userService){

        this.userService = userService;

        setTitle("Main");
        setSize(600, 400);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(4, 1);
        setLayout(gridLayout);

        helloLabel = new JLabel("LOGIN MENU");
        nullLabel = new JLabel("");

        phoneLabel = new JLabel("LOGIN (phone):");
        phoneText = new JTextField();

        passLabel = new JLabel("PASSWORD:");
        passText = new JTextField();

        buttonPanel1 = new JPanel(new GridLayout(1,1));

        loginButton = new JButton("LOGIN");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (phoneText.getText().equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "Please enter your phone");
                } else if (passText.getText().equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "Please enter your password");
                } else if (!phoneText.getText().equals("") && !passText.getText().equals("")) {
                    try {
                        String accessToken = userService.login(phoneText.getText(), passText.getText());
                        ClientAccessToken.accessToken = accessToken;
                        dispose();

                        if (userService.getUser(accessToken).getIdentifier().equals(UserIdentifier.P)) {
                            new PassengerMenu(userService);

                        } else if (userService.getUser(accessToken).getIdentifier().equals(UserIdentifier.D)) {
                            new DriverMenu(userService);
                        }

                    } catch (LoginException e1) {
                        JOptionPane.showMessageDialog(getParent(), "Login or password is wrong!");
                        dispose();
                        new UserLogin(userService);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        buttonPanel2 = new JPanel(new GridLayout(1,1));
        registerButton = new JButton("REGISTER");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Identifier(userService);
            }
        });

        gonowButton = new JButton("GO NOW!");
        gonowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MakeOrderAnonymous(userService);
                JOptionPane.showMessageDialog(getParent(), "You logged in without registration");
            }
        });

        buttonPanel1.add(loginButton);
        buttonPanel2.add(registerButton);
        buttonPanel2.add(gonowButton);

        getContentPane().add(helloLabel);
        getContentPane().add(nullLabel);

        getContentPane().add(phoneLabel);
        getContentPane().add(phoneText);

        getContentPane().add(passLabel);
        getContentPane().add(passText);

        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);
    }
}
