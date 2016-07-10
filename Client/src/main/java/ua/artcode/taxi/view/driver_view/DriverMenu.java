package ua.artcode.taxi.view.driver_view;

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

public class DriverMenu extends JFrame {

    private final UserService userService;

    private JLabel mainLabel;
    private JLabel nullLabel;
    private JPanel buttonPanel1;
    private JPanel buttonPanel2;
    private JPanel buttonPanel3;
    private JPanel buttonPanel4;
    private JPanel buttonPanel5;
    private JPanel buttonPanel6;

    private JButton showOrdersButton;
    private JButton changeRegButton;
    private JButton getOrderInfoButton;
    private JButton historyButton;
    private JButton returnButton;
    private JButton deleteButton;

    public DriverMenu(UserService userService){

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

        mainLabel = new JLabel("DRIVER MENU");
        nullLabel = new JLabel("");

        buttonPanel1 = new JPanel(new GridLayout(1,1));
        showOrdersButton = new JButton("Find passenger");
        showOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CurrentOrders(userService);
                dispose();
            }
        });
        buttonPanel1.add(showOrdersButton);

        buttonPanel2 = new JPanel(new GridLayout(1,1));
        getOrderInfoButton = new JButton("Show last order");
        getOrderInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Order lastOrder = userService.getLastOrderInfo(ClientAccessToken.getAccessToken());

                    if (lastOrder != null) {
                        dispose();
                        new DriverOrderInfo(userService, lastOrder);

                    } else {
                        JOptionPane.showMessageDialog(getParent(), "You don't have any orders");
                    }
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
                    new DriverHistory(userService);
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
                new DriverRegistration(userService);
            }
        });
        buttonPanel4.add(changeRegButton);

        buttonPanel5 = new JPanel(new GridLayout(1,1));
        returnButton = new JButton("Return to login menu");
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
