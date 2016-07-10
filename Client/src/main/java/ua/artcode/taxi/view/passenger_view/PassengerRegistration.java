package ua.artcode.taxi.view.passenger_view;

import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.ClientAccessToken;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.view.UserLogin;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class PassengerRegistration extends JFrame {

    private final UserService userService;

    private JLabel mainLabel;
    private JLabel nullLabel;
    private JLabel phoneLabel;
    private JTextField phoneText;
    private JLabel nameLabel;
    private JTextField nameText;
    private JLabel passLabel;
    private JTextField passText;
    private JLabel homeAddressLabel;
    private JTextField homeAddressText;
    private JPanel buttonPanel1;
    private JPanel buttonPanel2;

    private JButton okButton;
    private JButton returnButton;

    public PassengerRegistration(UserService userService){

        this.userService = userService;

        setTitle("Main");
        setSize(400, 600);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(6, 1);
        setLayout(gridLayout);

        mainLabel = new JLabel("PASSENGER REGISTRATION");
        nullLabel = new JLabel("");

        phoneLabel = new JLabel("PHONE:");
        phoneText = new JTextField();

        passLabel = new JLabel("PASSWORD:");
        passText = new JTextField();

        nameLabel = new JLabel("NAME:");
        nameText = new JTextField();

        homeAddressLabel = new JLabel("HOME ADDRESS (optional):");
        homeAddressText = new JTextField();

        //for change registration
        if (ClientAccessToken.accessToken != null) {
            User current = userService.getUser(ClientAccessToken.accessToken);
            if (current != null) {
                phoneText.setText(current.getPhone());
                passText.setText(current.getPass());
                nameText.setText(current.getName());

                if (current.getHomeAddress() == null) {
                    homeAddressText.setText("");
                } else if (current.getHomeAddress() != null) {
                    homeAddressText.setText(current.getHomeAddress().toLine());
                }
            }
        }

        buttonPanel1 = new JPanel(new GridLayout(1,1));
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (phoneText.getText().equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "Please enter your phone");
                } else if (passText.getText().equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "Please enter your password");
                } else if (nameText.getText().equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "Please enter your name");

                } else {
                    try {
                        Map<String, String> regDataPassenger = new HashMap<String, String>();
                        regDataPassenger.put("phone", phoneText.getText());
                        regDataPassenger.put("pass", passText.getText());
                        regDataPassenger.put("name", nameText.getText());
                        regDataPassenger.put("homeAddress", homeAddressText.getText());

                        if (ClientAccessToken.accessToken != null) {
                            //for change registration
                            userService.updateUser(regDataPassenger, ClientAccessToken.getAccessToken());

                            dispose();
                            new PassengerMenu(userService);
                            JOptionPane.showMessageDialog(getParent(), "Your registered data was changed");

                        } else if (ClientAccessToken.accessToken == null) {
                            //for new registration
                            User newUser = userService.registerPassenger(regDataPassenger);
                            ClientAccessToken.accessToken = userService.login(newUser.getPhone(), newUser.getPass());

                            dispose();
                            new PassengerMenu(userService);
                            JOptionPane.showMessageDialog(getParent(), "You have been successfully registered");
                        }

                    } catch (LoginException e1) {
                        JOptionPane.showMessageDialog(getParent(), "User not found or incorrect password");
                    } catch (RegisterException e1) {
                        JOptionPane.showMessageDialog(getParent(), "You enter wrong data! Try a different phone number");
                    }
                }
            }
        });

        buttonPanel2 = new JPanel(new GridLayout(1,1));
        returnButton = new JButton("Return to login");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ClientAccessToken.getAccessToken() != null) {
                    JOptionPane.showMessageDialog(getParent(),
                            userService.getUser(ClientAccessToken.getAccessToken()).getName() + " logs out");
                    ClientAccessToken.setAccessToken(null);
                }
                dispose();
                new UserLogin(userService);
            }
        });

        getContentPane().add(phoneLabel);

        buttonPanel1.add(okButton);
        buttonPanel2.add(returnButton);

        getContentPane().add(mainLabel);
        getContentPane().add(nullLabel);

        getContentPane().add(phoneLabel);
        getContentPane().add(phoneText);

        getContentPane().add(passLabel);
        getContentPane().add(passText);

        getContentPane().add(nameLabel);
        getContentPane().add(nameText);

        getContentPane().add(homeAddressLabel);
        getContentPane().add(homeAddressText);

        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);

    }
}
