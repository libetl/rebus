package org.toilelibre.libe.rebus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.toilelibre.libe.rebus.actions.BuildEquationFromSentence;
import org.toilelibre.libe.rebus.actions.BuildRebusFromSentence;
import org.toilelibre.libe.rebus.actions.LoadData;

/**
 * This is the legacy GUI class made in swing / SWT. It builds an awful frame
 * with two buttons, a text field, a label grid, and a tab with some settings.
 *
 * @author LiBe
 *
 */
public class OldUserInterface {

    /**
     * Called when the user submit the words
     *
     * @author LiBe
     *
     */
    private static class SubmitSentenceAction extends AbstractAction {
        /**
         *
         */
        private static final long serialVersionUID = -2148665021521716600L;

        private int               cost             = 0;

        /**
         * Keeping an eye during the process on how many threads are still
         * converting a word. When 0, the draw () method is called
         */
        private int               nbRemaining      = 0;

        public SubmitSentenceAction () {
            this.putValue (Action.NAME, "I'm loading some data...");
            this.putValue (Action.SHORT_DESCRIPTION, "Push to write it in Rebus");
        }

        // @Override
        /**
         * Method called from Swing when the user click on the button
         */
        @Override
        public void actionPerformed (final ActionEvent e) {
            // Prevent the user from clicking twice
            OldUserInterface.btnPutMyLife.setEnabled (false);
            OldUserInterface.btnPutMyLife.setText ("Computing");

            // Spliting the sentence into words
            final String [] wordsS = BuildEquationFromSentence.preparse (OldUserInterface.textField.getText ());

            // Set remaining as the number of threads to be run.
            this.nbRemaining = wordsS.length;

            // We prepare the labels
            final JLabel [] jlabelsResult = new JLabel [wordsS.length];

            // Clear any previous result
            this.cost = 0;
            OldUserInterface.resultPanels.removeAll ();

            // Here we launch the threads
            for (int i = 0 ; i < jlabelsResult.length ; i++) {
                final int j = i;
                jlabelsResult [i] = new JLabel ("?");
                jlabelsResult [i].setVerticalTextPosition (SwingConstants.BOTTOM);
                final JPanel jpbox = new JPanel ();
                jpbox.setBorder (new LineBorder (new Color (0, 0, 0)));
                jpbox.setLayout (new GridLayout (2, 1, 0, 0));
                jpbox.add (jlabelsResult [i]);
                OldUserInterface.resultPanels.add (jpbox);
                final String word = wordsS [i];
                new Thread () {
                    @Override
                    /**
                     * Thread doing a conversion
                     */
                    public void run () {
                        // Getting the result here
                        final String equation = BuildEquationFromSentence.getEquation (word);
                        // The cost
                        SubmitSentenceAction.this.cost += BuildEquationFromSentence.getCost (equation);
                        // We write it
                        jlabelsResult [j].setText ("(" + equation + ")");
                        OldUserInterface.costLabel.setText ("Cost : " + SubmitSentenceAction.this.cost);
                        // And tell that the thread is over.
                        SubmitSentenceAction.this.join ();
                    }
                }.start ();
            }

        }

        /**
         * Called when a thread is over Decreases the remaining value if 0, it
         * calls frame.draw () and bring back the submit button.
         */
        private void join () {
            this.nbRemaining--;
            if (this.nbRemaining == 0) {
                OldUserInterface.draw ();
                OldUserInterface.btnPutMyLife.setEnabled (true);
                OldUserInterface.btnPutMyLife.setText ("Equation of words");
            }
        }
    }

    private static class SubmitRebusAction extends AbstractAction {

        /**
        *
        */
        private static final long serialVersionUID = -3355814052129919461L;

        public SubmitRebusAction () {
            this.putValue (Action.NAME, "I'm loading some data...");
            this.putValue (Action.SHORT_DESCRIPTION, "Push to write it in Rebus");
        }

        // @Override
        /**
         * Method called from Swing when the user click on the button
         */
        @Override
        public void actionPerformed (final ActionEvent e) {
            // Prevent the user from clicking twice
            OldUserInterface.rebus.setEnabled (false);
            OldUserInterface.rebus.setText ("Computing");

            OldUserInterface.resultPanels.removeAll ();

            // Here we launch the threads
            final JPanel jpbox = new JPanel ();
            jpbox.setBorder (new LineBorder (new Color (0, 0, 0)));
            jpbox.setLayout (new GridLayout (2, 1, 0, 0));
            OldUserInterface.resultPanels.add (jpbox);
            new Thread () {
                @Override
                /**
                 * Thread doing a conversion
                 */
                public void run () {
                    // Getting the result here
                    final String [] words = BuildRebusFromSentence.getRebusFromSentence (OldUserInterface.textField.getText ());
                    // We write it
                    for (final String word : words) {
                        final JLabel jlabelsResult = new JLabel ("?");
                        jlabelsResult.setVerticalTextPosition (SwingConstants.BOTTOM);
                        jlabelsResult.setText ("(" + word + ")");
                        jpbox.add (jlabelsResult);
                    }
                    // And tell that the thread is over.
                    SubmitRebusAction.this.join ();
                }
            }.start ();
        }

        /**
         * Called when a thread is over Decreases the remaining value if 0, it
         * calls frame.draw () and bring back the submit button.
         */
        private void join () {
            OldUserInterface.draw ();
            OldUserInterface.rebus.setEnabled (true);
            OldUserInterface.rebus.setAction (OldUserInterface.rebusAction);
            OldUserInterface.rebus.setText ("Get a rebus");
        }
    }

    /**
     * This method is used when a conversion is finished It displays the Rebus
     * one more time with the pictures
     */
    private static void draw () {
        for (int i = 0 ; i < OldUserInterface.resultPanels.getComponentCount () ; i++) {
            // We add a new Panel to one word box
            final Container box = (Container) OldUserInterface.resultPanels.getComponent (i);
            if (box.getComponentCount () == 0) {
                return;
            }
            final JLabel jl = (JLabel) box.getComponent (0);
            final JPanel jpTextsAndDrawings = new JPanel ();
            box.add (jpTextsAndDrawings);

            // We are looking for any word begining with a colon
            final String text = jl.getText ();
            final Matcher matcher = Pattern.compile ("([a-z0-9]+)").matcher (text);
            int lastPos = 0;
            while (matcher.find ()) {

                // We write the text before the current drawing
                final String subText = text.substring (lastPos, matcher.start ());
                jpTextsAndDrawings.add (new JLabel (subText));
                final String picture = matcher.group (1);

                // We add the drawing
                final String pictureFile = LoadData.getPicture (picture);
                Image pictureImage = null;
                try {
                    pictureImage = pictureFile == null ? null : ImageIO.read (new URL ("https://openclipart.org/image/250px/svg_to_png/" + pictureFile + ".png"));
                } catch (final MalformedURLException e) {
                } catch (final IOException e) {
                }
                if (pictureImage != null) {
                    jpTextsAndDrawings.add (new JLabel (new ImageIcon (pictureImage)));
                } else {
                    jpTextsAndDrawings.add (new JLabel (picture));
                }
                lastPos = matcher.end ();
            }

            // We write the text after the last drawing
            jpTextsAndDrawings.add (new JLabel (text.substring (lastPos)));
        }
    }

    /**
     * Launches the application.
     */
    public static void main (final String [] args) {
        EventQueue.invokeLater (new Runnable () {
            // @Override
            @Override
            public void run () {
                final JFrame frame = OldUserInterface.startGUI ();
                new Thread () {
                    @Override
                    /**
                     * All data is loaded here
                     */
                    public void run () {
                        LoadData.ensureEnglishDataLoaded ();
                        OldUserInterface.btnPutMyLife.setEnabled (true);
                        OldUserInterface.btnPutMyLife.setAction (OldUserInterface.action);
                        OldUserInterface.btnPutMyLife.setText ("Equation of words");
                        OldUserInterface.rebus.setEnabled (true);
                        OldUserInterface.rebus.setAction (OldUserInterface.rebusAction);
                        OldUserInterface.rebus.setText ("Get a rebus");
                    }
                }.start ();
                frame.setVisible (true);
            }
        });
    }

    private static Action     action      = new SubmitSentenceAction ();
    private static Action     rebusAction = new SubmitRebusAction ();

    private static JButton    btnPutMyLife;
    private static JButton    rebus;
    private static JPanel     contentPane;
    private static JLabel     costLabel;

    private static JPanel     resultPanels;

    private static JTextField textField;

    /**
     * Creates the frame. Ugly method generated from a designer toolbox
     */
    public static JFrame startGUI () {
        final JFrame frame = new JFrame ();
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setBounds (100, 100, 549, 539);
        OldUserInterface.contentPane = new JPanel ();
        OldUserInterface.contentPane.setBorder (new EmptyBorder (5, 5, 5, 5));
        frame.setContentPane (OldUserInterface.contentPane);
        OldUserInterface.contentPane.setLayout (new BorderLayout (0, 0));

        final JTabbedPane tabbedPane = new JTabbedPane (SwingConstants.TOP);
        OldUserInterface.contentPane.add (tabbedPane);

        final JPanel panel1 = new JPanel ();
        tabbedPane.addTab ("I want my rebus !", null, panel1, null);
        panel1.setLayout (new GridLayout (3, 1, 0, 0));

        final JPanel panel2 = new JPanel ();
        panel1.add (panel2);
        panel2.setLayout (new GridLayout (2, 1, 0, 0));

        final JLabel lblNewLabel = new JLabel ("What's on your mind ? I will write it in rebus !");
        panel2.add (lblNewLabel);

        final JPanel panel3 = new JPanel ();
        panel1.add (panel3);
        panel3.setLayout (new FlowLayout (FlowLayout.CENTER, 5, 5));

        OldUserInterface.textField = new JTextField ();
        panel3.add (OldUserInterface.textField);
        OldUserInterface.textField.setColumns (35);

        final JLabel label = new JLabel ("");
        panel3.add (label);

        OldUserInterface.btnPutMyLife = new JButton ("I'm loading some data...");
        OldUserInterface.btnPutMyLife.setEnabled (false);
        OldUserInterface.rebus = new JButton ("I'm loading some data...");
        OldUserInterface.rebus.setEnabled (false);
        panel3.add (OldUserInterface.btnPutMyLife);
        panel3.add (OldUserInterface.rebus);

        final JPanel panel4 = new JPanel ();

        final JScrollPane scrollPane = new JScrollPane (panel4, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel1.add (scrollPane);
        panel4.setLayout (new GridLayout (3, 1, 0, 0));

        OldUserInterface.resultPanels = new JPanel ();
        panel4.add (OldUserInterface.resultPanels);

        OldUserInterface.costLabel = new JLabel ("Cost : 0");
        panel4.add (OldUserInterface.costLabel);

        final JPanel panel = new JPanel ();
        tabbedPane.addTab ("Settings", null, panel, null);
        panel.setLayout (new GridLayout (6, 0, 0, 0));

        final JPanel panel6 = new JPanel ();
        panel.add (panel6);

        final JLabel lblNewLabel1 = new JLabel ("Max remaining cost by word (if possible) : ");
        panel6.add (lblNewLabel1);

        final JSpinner spinner = new JSpinner ();
        final SpinnerNumberModel spinnerModel = new SpinnerNumberModel (new Integer (2), null, null, new Integer (1));
        spinnerModel.addChangeListener (new ChangeListener () {
            // @Override
            @Override
            public void stateChanged (final ChangeEvent e) {
                LoadData.setRemainingCost ((Integer) ((SpinnerNumberModel) e.getSource ()).getValue ());
            }
        });
        spinner.setModel (spinnerModel);
        panel6.add (spinner);

        final JPanel panel7 = new JPanel ();
        panel.add (panel7);

        final JLabel lblNewLabel2 = new JLabel ("Complexity of new terms (the higher the longer)");
        panel7.add (lblNewLabel2);

        final JSpinner spinner1 = new JSpinner ();
        final SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel (new Integer (2), null, null, new Integer (1));
        spinnerModel1.addChangeListener (new ChangeListener () {

            @Override
            public void stateChanged (final ChangeEvent e) {
                LoadData.setLettersMissing ((Integer) ((SpinnerNumberModel) e.getSource ()).getValue ());
            }
        });
        spinner1.setModel (spinnerModel1);
        panel7.add (spinner1);

        final JPanel panel5 = new JPanel ();
        panel.add (panel5);

        final JLabel lblNewLabel3 = new JLabel ("Variation of the length of the words");
        panel5.add (lblNewLabel3);

        final JSpinner spinner2 = new JSpinner ();
        final SpinnerNumberModel spinnerModel2 = new SpinnerNumberModel (new Integer (5), null, null, new Integer (1));
        spinnerModel2.addChangeListener (new ChangeListener () {

            @Override
            public void stateChanged (final ChangeEvent e) {
                LoadData.setLengthDelta ((Integer) ((SpinnerNumberModel) e.getSource ()).getValue ());
            }
        });
        spinner2.setModel (spinnerModel2);
        panel5.add (spinner2);
        return frame;
    }
}
