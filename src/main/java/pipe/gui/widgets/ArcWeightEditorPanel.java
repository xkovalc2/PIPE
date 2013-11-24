package pipe.gui.widgets;

import net.sourceforge.jeval.EvaluationException;
import parser.ExprEvaluator;
import parser.MarkingDividedByNumberException;
import pipe.controllers.PetriNetController;
import pipe.gui.ApplicationSettings;
import pipe.models.Arc;
import pipe.models.PetriNet;
import pipe.models.Token;
import pipe.models.Transition;
import pipe.views.PetriNetView;
import pipe.views.TokenView;

import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Alex Charalambous
 * @author yufeiwang (minor changes)
 */
public class ArcWeightEditorPanel extends javax.swing.JPanel {

    private final Arc arc;

    private final PetriNet petriNet;
    private JRootPane _rootPane;

    private javax.swing.JButton okButton = new javax.swing.JButton();
    private LinkedList<JTextField> inputtedWeights =
            new LinkedList<JTextField>();
    private LinkedList<String> inputtedTokenClassNames =
            new LinkedList<String>();

    private PetriNetController netController;

    /**
     * Creates new form Arc Weight Editor
     *
     * @param rootPane
     * @param arc
     * @param _pnmlData
     * @param controller
     */
    public ArcWeightEditorPanel(JRootPane rootPane, Arc arc,
                                PetriNetController controller) {
        this.arc = arc;
        this.petriNet = controller.getPetriNet();
        _rootPane = rootPane;
        this.netController = controller;

        initComponents();

        this._rootPane.setDefaultButton(okButton);
    }

    private void initComponents() {
        LinkedList<TokenView> tokenViews =
                ApplicationSettings.getApplicationView()
                        .getCurrentPetriNetView().getTokenViews();
        java.awt.GridBagConstraints gridBagConstraints;

        JPanel arcEditorPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton();

        setLayout(new java.awt.GridBagLayout());

        arcEditorPanel.setBorder(javax.swing.BorderFactory
                .createTitledBorder("Arc Weight Editor"));
        arcEditorPanel.setLayout(new java.awt.GridBagLayout());
        Dimension d = new Dimension();
        d.setSize(300, 340);
        arcEditorPanel.setPreferredSize(d);

        // Now set new dimension used in for loop below
        d = new Dimension();
        d.setSize(50, 19);
        int x = 0;
        int y = 0;

        for (final Token token : petriNet.getTokens()) {
            if (token.isEnabled()) {
                gridBagConstraints = new java.awt.GridBagConstraints();

                JLabel tokenClassName = new JLabel();
                final JTextField tokenClassWeight = new JTextField();
                tokenClassWeight.setEditable(true);
                tokenClassWeight.setName(token.getId());

                tokenClassWeight.setText(arc.getWeightForToken(token));
                inputtedWeights.add(tokenClassWeight);

                tokenClassName.setText(token.getId() + ": ");
                inputtedTokenClassNames.add(token.getId());

                gridBagConstraints.gridx = x;
                gridBagConstraints.gridy = y;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
                arcEditorPanel.add(tokenClassName, gridBagConstraints);

                tokenClassWeight.setPreferredSize(d);

                tokenClassWeight
                        .addFocusListener(new java.awt.event.FocusAdapter() {
                            public void focusGained(
                                    java.awt.event.FocusEvent evt) {
                                nameTextFieldFocusGained(evt);
                            }

                            public void focusLost(
                                    java.awt.event.FocusEvent evt) {
                                nameTextFieldFocusLost(evt);
                            }
                        });
                tokenClassWeight.setEnabled(true);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = x + 3;
                gridBagConstraints.gridy = y;
                gridBagConstraints.gridwidth = 3;
                gridBagConstraints.fill =
                        java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
                arcEditorPanel.add(tokenClassWeight, gridBagConstraints);

                final JButton fweditor =
                        new JButton("Weight expression editor");
                fweditor.setEnabled(true);
                gridBagConstraints.gridx = x + 3;
                gridBagConstraints.gridy = y + 1;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
                arcEditorPanel.add(fweditor, gridBagConstraints);

                fweditor.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent evt) {
                        createEditorWindow(token);
                    }
                });

                y += 2;
            }
        }
        for (final TokenView tc : tokenViews) {
            if (tc.isEnabled()) {

            }
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(arcEditorPanel, gridBagConstraints);
        buttonPanel.setLayout(new java.awt.GridBagLayout());

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonHandler(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        buttonPanel.add(cancelButton, gridBagConstraints);

        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(75, 25));
        okButton.setMinimumSize(new java.awt.Dimension(75, 25));
        okButton.setPreferredSize(new java.awt.Dimension(75, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonHandler(evt);
            }
        });
        okButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okButtonKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        buttonPanel.add(okButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 8, 3);
        add(buttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public void createEditorWindow(Token token) {
        EscapableDialog guiDialog =
                new EscapableDialog(ApplicationSettings.getApplicationView(),
                        "PIPE2", true);
        ArcFunctionEditor feditor =
                new ArcFunctionEditor(this, guiDialog, petriNet, arc, token);
        guiDialog.add(feditor);
        guiDialog.setSize(270, 230);
        guiDialog.setLocationRelativeTo(
                ApplicationSettings.getApplicationView());
        guiDialog.setVisible(true);
        guiDialog.dispose();
    }

    private void nameTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        // focusLost(nameTextField);
    }

    private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        // focusGained(nameTextField);
    }

    private void okButtonKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            okButtonHandler(new java.awt.event.ActionEvent(this, 0, ""));
        }
    }

    CaretListener caretListener = new javax.swing.event.CaretListener() {
        public void caretUpdate(javax.swing.event.CaretEvent evt) {
            JTextField textField = (JTextField) evt.getSource();
            textField.setBackground(new Color(255, 255, 255));
            // textField.removeChangeListener(this);
        }
    };

    private void okButtonHandler(java.awt.event.ActionEvent evt) {
        ExprEvaluator parser = new ExprEvaluator(petriNet);
        for (int i = 0; i < inputtedWeights.size(); i++) {
            String expr = inputtedWeights.get(i).getText();
            try {
                if (expr == null || expr.equals("")) {
                    System.err.println("Error in functional rates expression.");
                    String message =
                            " Expression is invalid. Please check your function.";
                    String title = "Error";
                    JOptionPane.showMessageDialog(null, message, title,
                            JOptionPane.YES_NO_OPTION);
                    return;
                }

                String tokenClassName = inputtedTokenClassNames.get(i);
                Token token = petriNet.getToken(tokenClassName);

                if (parser.parseAndEvalExpr(expr, tokenClassName) != -1) {
                    netController.setWeightForArc(arc, token, expr);
                }
                else {
                    if (parser.parseAndEvalExpr(expr, tokenClassName) == -2) {
                        JOptionPane.showMessageDialog(null,
                                "Please make sure division and floating numbers are " +
                                        "surrounded by ceil() or floor()");
                        return;
                    }
                    else {
                        System.err.println(
                                "Error in functional rates expression.");
                        String message =
                                " Expression is invalid. Please check your function.";
                        String title = "Error";
                        JOptionPane.showMessageDialog(null, message, title,
                                JOptionPane.YES_NO_OPTION);
                        return;
                    }
                }
            } catch (MarkingDividedByNumberException e) {
                JOptionPane.showMessageDialog(null,
                        "Marking-dependent arc weight divided by number not supported.\r\n" +
                                "Since this may cause non-integer arc weight.");
                return;
            } catch (Exception e) {
                System.err.println("Error in functional rates expression.");
                String message =
                        " Expression is invalid. Please check your function.";
                String title = "Error";
                JOptionPane.showMessageDialog(null, message, title,
                        JOptionPane.YES_NO_OPTION);
                return;
            }
        }

        if (arc.hasFunctionalWeight()) {
            Object target = arc.getTarget();
            if (target instanceof Transition) {
                Transition transition = (Transition) target;
                if (transition.isInfiniteServer()) {
                    String message =
                            "This arc points to an infinite server transition. \r\n" +
                                    "Thus this arc could not have functional weights at the moment";
                    String title = "Error";
                    JOptionPane.showMessageDialog(null, message, title,
                            JOptionPane.YES_NO_OPTION);
                    return;
                }
            }
        }


        Map<Token, String> newWeights = new HashMap <Token, String>();
//        int totalMarking = 0;
        for (int i = 0; i < inputtedWeights.size(); i++) {
            String tokenClassName = inputtedTokenClassNames.get(i);
            Token token = petriNet.getToken(tokenClassName);
            String weight = inputtedWeights.get(i).getText();
            newWeights.put(token, weight);
            try {
                int evaluatedWeight = parser.parseAndEvalExpr(weight, tokenClassName);
                if (evaluatedWeight == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Error in weight expression. Please make sure\r\n it is an integer");
                    return;
                }
                if (evaluatedWeight == -2) {
                    JOptionPane.showMessageDialog(null,
                            "Please make sure division and floating numbers are " +
                                    "surrounded by ceil() or floor()");
                    return;
                }
                if (evaluatedWeight < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Weighting cannot be less than 0. Please re-enter");
                    return;
                }

            } catch (EvaluationException e) {
                JOptionPane.showMessageDialog(null,
                        "Error in Arc weight expression");
                return;
            } catch (MarkingDividedByNumberException eee) {
                JOptionPane.showMessageDialog(null,
                        "Marking-dependent arc weight divided by number not supported.\r\n" +
                                "Since this may cause non-integer arc weight.");
                return;
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(null,
                        "Error in Arc weight expression");
                return;
            }

        }
//        if (totalMarking <= 0) {
//            JOptionPane.showMessageDialog(null,
//                    "Total weight of arc must be greater than 0. Please re-enter");
//            return;
//        }

        netController.setWeights(arc, newWeights);
        exit();
    }

    private void exit() {
        _rootPane.getParent().setVisible(false);
    }

    private void cancelButtonHandler(java.awt.event.ActionEvent evt) {
        // Provisional!
        exit();
    }

    public void setWeight(String func, String id) {
        for (int i = 0; i < inputtedTokenClassNames.size(); i++) {
            if (inputtedTokenClassNames.get(i).equals(id)) {
                inputtedWeights.get(i).setText(func);
            }
        }

    }

}
