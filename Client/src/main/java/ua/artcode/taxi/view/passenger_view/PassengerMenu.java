package ua.artcode.taxi.view.passenger_view;

import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.UserNotFoundException;
import ua.artcode.taxi.exception.WrongStatusOrderException;
import ua.artcode.taxi.model.ClientAccessToken;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.view.UserLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PassengerMenu extends JFrame {

    private final UserService userService;

    private JLabel mainLabel;

    private JPanel buttonPanel1;
    private JPanel buttonPanel2;
    private JPanel buttonPanel3;
    private JPanel buttonPanel4;
    private JPanel buttonPanel5;
    private JPanel buttonPanel6;

    private JButton findTaxiButton;
    private JButton changeRegButton;
    private JButton getOrderInfoButton;
    private JButton historyButton;
    private JButton returnButton;
    private JButton deleteButton;

    public PassengerMenu(UserService userService){

        this.userService = userService;

        setTitle("Main");
        setSize(400, 600);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(7, 1);
        setLayout(gridLayout);

        mainLabel = new JLabel("PASSENGER MENU");

        buttonPanel1 = new JPanel(new GridLayout(1,1));
        findTaxiButton = new JButton("Find taxi");
        findTaxiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MakeOrder(userService);
            }
        });
        buttonPanel1.add(findTaxiButton);

        buttonPanel2 = new JPanel(new GridLayout(1,1));
        getOrderInfoButton = new JButton("Show last order");
        getOrderInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Order last = userService.getLastOrderInfo(ClientAccessToken.getAccessToken());
                    dispose();
                    new PassengerOrderInfo(userService, last);

                } catch (OrderNotFoundException e1) {
                    JOptionPane.showMessageDialog(getParent(), "You don't have any orders");
                } catch (UserNotFoundException e1) {
                    JOptionPane.showMessageDialog(getParent(), "You must registered for show last order");
                }
            }
        });
        buttonPanel2.add(getOrderInfoButton);

        buttonPanel3 = new JPanel(new GridLayout(1,1));
        historyButton = new JButton("Show my history");
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ClientAccessToken.getAccessToken() != null) {
                    dispose();
                    new PassengerHistory(userService);

                } else {
                    JOptionPane.showMessageDialog(getParent(), "You must registered for show orders history");
                }

            }
        });
        buttonPanel3.add(historyButton);

        buttonPanel4 = new JPanel(new GridLayout(1,1));
        changeRegButton = new JButton("Change registration data");
        changeRegButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PassengerRegistration(userService);
            }
        });
        buttonPanel4.add(changeRegButton);

        buttonPanel5 = new JPanel(new GridLayout(1,1));
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
        buttonPanel5.add(returnButton);

        buttonPanel6 = new JPanel(new GridLayout(1, 1));
        deleteButton = new JButton("Delete user");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ClientAccessToken.getAccessToken() != null) {
                    User currentUser = userService.getUser(ClientAccessToken.getAccessToken());
                    JOptionPane.showMessageDialog(getParent(), currentUser.getName() + " will be deleted");

                    try {
                        User deletedUser = userService.deleteUser(ClientAccessToken.getAccessToken());

                        if (deletedUser.getId() == currentUser.getId()) {
                            ClientAccessToken.setAccessToken(null);
                        }
                        dispose();
                        new UserLogin(userService);

                    } catch (WrongStatusOrderException e1) {
                        JOptionPane.showMessageDialog(getParent(), "User can't has existing orders");
                    }
                }

            }
        });
        buttonPanel6.add(deleteButton);

        getContentPane().add(mainLabel);
        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);
        getContentPane().add(buttonPanel3);
        getContentPane().add(buttonPanel4);
        getContentPane().add(buttonPanel5);
        getContentPane().add(buttonPanel6);
    }
}
