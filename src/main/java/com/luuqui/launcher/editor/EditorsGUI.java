package com.luuqui.launcher.editor;

import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.ImageUtils;
import com.luuqui.launcher.*;
import com.luuqui.launcher.mods.ModListEventHandler;
import com.luuqui.launcher.mods.ModLoader;
import com.luuqui.launcher.mods.data.Mod;
import com.luuqui.util.ColorUtil;
import com.luuqui.util.DesktopUtil;
import com.luuqui.util.ImageUtil;
import javafx.scene.effect.GaussianBlur;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import static com.luuqui.launcher.mods.Log.log;

public class EditorsGUI extends BaseGUI {

  private final LauncherApp app;
  protected static JFrame editorsGUIFrame;
  public static JPanel editorsPanel;
  protected static JPanel editorListPane = new JPanel();
  protected static JScrollPane editorListPaneScroll = new JScrollPane();
  protected static JLabel editorLaunchState;
  protected static JProgressBar editorLaunchFakeProgressBar;

  protected static BufferedImage modelViewerImage = null;
  protected static BufferedImage sceneEditorImage = null;
  protected static BufferedImage interfaceTesterImage = null;
  protected static BufferedImage particleEditorImage = null;

  protected static BufferedImage modelViewerImageUnfocused = null;
  protected static BufferedImage sceneEditorImageUnfocused = null;
  protected static BufferedImage interfaceTesterImageUnfocused = null;
  protected static BufferedImage particleEditorImageUnfocused = null;

  protected static BufferedImage modelViewerImageFocused = null;
  protected static BufferedImage sceneEditorImageFocused = null;
  protected static BufferedImage interfaceTesterImageFocused = null;
  protected static BufferedImage particleEditorImageFocused = null;

  public EditorsGUI(LauncherApp app) {
    super();
    this.app = app;
    setupImages();
    initialize();
  }

  @SuppressWarnings("static-access")
  public void switchVisibility() {
    this.editorsGUIFrame.setVisible(!this.editorsGUIFrame.isVisible());
  }

  private void initialize() {
    editorsGUIFrame = new JFrame();
    editorsGUIFrame.setVisible(false);
    editorsGUIFrame.setTitle("Editors");
    editorsGUIFrame.setBounds(100, 100, 385, 460);
    editorsGUIFrame.setResizable(false);
    editorsGUIFrame.setUndecorated(true);
    editorsGUIFrame.getContentPane().setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    editorsGUIFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    editorsGUIFrame.getContentPane().setLayout(null);
    editorsPanel = (JPanel) editorsGUIFrame.getContentPane();

    editorLaunchState = new JLabel("Loading editor...");
    editorLaunchState.setHorizontalAlignment(SwingConstants.CENTER);
    editorLaunchState.setBounds(192, 190, 385, 25);
    editorLaunchState.setFont(Fonts.fontRegBig);
    editorLaunchState.setVisible(false);
    editorsGUIFrame.getContentPane().add(editorLaunchState);

    editorLaunchFakeProgressBar = new JProgressBar(0, 150);
    editorLaunchFakeProgressBar.setBounds(192, 220, 385, 25);
    editorLaunchFakeProgressBar.setVisible(false);
    editorsGUIFrame.getContentPane().add(editorLaunchFakeProgressBar);

    editorListPane = new JPanel();
    editorListPane.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    GridLayout layout = new GridLayout(4, 1);
    layout.setVgap(10);
    editorListPane.setLayout(layout);
    editorListPane.setPreferredSize(new Dimension(740, 440));

    editorListPaneScroll = new JScrollPane(editorListPane);
    editorListPaneScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    editorListPaneScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    editorListPaneScroll.setBounds(30, 0, 740, 444);
    editorListPaneScroll.setBorder(null);
    editorListPaneScroll.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    editorListPaneScroll.setForeground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    editorListPaneScroll.getVerticalScrollBar().setUnitIncrement(16);
    editorsGUIFrame.getContentPane().add(editorListPaneScroll);

    JPanel modelViewerPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(modelViewerImage, 0, 0, null);
      }
    };
    modelViewerPane.setLayout(null);
    modelViewerPane.setBounds(0, 0, 740, 100);
    modelViewerPane.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);

    JLabel modelViewerLabel = new JLabel();
    modelViewerLabel.setText("Model Viewer");
    modelViewerLabel.setFont(Fonts.fontMedGiant);
    modelViewerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    modelViewerLabel.setVerticalAlignment(SwingConstants.CENTER);
    modelViewerLabel.setBounds(0, 0, 740, 100);
    modelViewerPane.add(modelViewerLabel);

    modelViewerPane.addMouseListener(new MouseListener() {
      @Override public void mouseClicked(MouseEvent e) { EditorsEventHandler.startModelViewer(null); }
      @Override public void mousePressed(MouseEvent e) { EditorsEventHandler.startModelViewer(null); }
      @Override public void mouseReleased(MouseEvent e) { EditorsEventHandler.startModelViewer(null); }
      @Override public void mouseEntered(MouseEvent e) {
        modelViewerPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        modelViewerImage = modelViewerImageFocused;
        modelViewerPane.repaint();
      }
      @Override public void mouseExited(MouseEvent e) {
        modelViewerPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        modelViewerImage = modelViewerImageUnfocused;
        modelViewerPane.repaint();
      }
    });

    JPanel sceneEditorPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(sceneEditorImage, 0, 0, null);
      }
    };
    sceneEditorPane.setLayout(null);
    sceneEditorPane.setBounds(0, 0, 740, 100);
    sceneEditorPane.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);

    JLabel sceneEditorLabel = new JLabel();
    sceneEditorLabel.setText("Scene Editor");
    sceneEditorLabel.setFont(Fonts.fontMedGiant);
    sceneEditorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    sceneEditorLabel.setVerticalAlignment(SwingConstants.CENTER);
    sceneEditorLabel.setBounds(0, 0, 740, 100);
    sceneEditorPane.add(sceneEditorLabel);

    sceneEditorPane.addMouseListener(new MouseListener() {
      @Override public void mouseClicked(MouseEvent e) { EditorsEventHandler.startSceneEditor(null); }
      @Override public void mousePressed(MouseEvent e) { EditorsEventHandler.startSceneEditor(null); }
      @Override public void mouseReleased(MouseEvent e) { EditorsEventHandler.startSceneEditor(null); }
      @Override public void mouseEntered(MouseEvent e) {
        sceneEditorPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sceneEditorImage = sceneEditorImageFocused;
        sceneEditorPane.repaint();
      }
      @Override public void mouseExited(MouseEvent e) {
        sceneEditorPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        sceneEditorImage = sceneEditorImageUnfocused;
        sceneEditorPane.repaint();
      }
    });

    JPanel interfaceTesterPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(interfaceTesterImage, 0, 0, null);
      }
    };
    interfaceTesterPane.setLayout(null);
    interfaceTesterPane.setBounds(0, 0, 740, 100);
    interfaceTesterPane.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);

    JLabel interfaceTesterLabel = new JLabel();
    interfaceTesterLabel.setText("Interface Tester");
    interfaceTesterLabel.setFont(Fonts.fontMedGiant);
    interfaceTesterLabel.setHorizontalAlignment(SwingConstants.CENTER);
    interfaceTesterLabel.setVerticalAlignment(SwingConstants.CENTER);
    interfaceTesterLabel.setBounds(0, 0, 740, 100);
    interfaceTesterPane.add(interfaceTesterLabel);

    interfaceTesterPane.addMouseListener(new MouseListener() {
      @Override public void mouseClicked(MouseEvent e) { EditorsEventHandler.startInterfaceTester(null); }
      @Override public void mousePressed(MouseEvent e) { EditorsEventHandler.startInterfaceTester(null); }
      @Override public void mouseReleased(MouseEvent e) { EditorsEventHandler.startInterfaceTester(null); }
      @Override public void mouseEntered(MouseEvent e) {
        interfaceTesterPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        interfaceTesterImage = interfaceTesterImageFocused;
        interfaceTesterPane.repaint();
      }
      @Override public void mouseExited(MouseEvent e) {
        interfaceTesterPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        interfaceTesterImage = interfaceTesterImageUnfocused;
        interfaceTesterPane.repaint();
      }
    });

    JPanel particleEditorPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(particleEditorImage, 0, 0, null);
      }
    };
    particleEditorPane.setLayout(null);
    particleEditorPane.setBounds(0, 0, 740, 100);
    particleEditorPane.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);

    JLabel particleEditorLabel = new JLabel();
    particleEditorLabel.setText("Particle Editor");
    particleEditorLabel.setFont(Fonts.fontMedGiant);
    particleEditorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    particleEditorLabel.setVerticalAlignment(SwingConstants.CENTER);
    particleEditorLabel.setBounds(0, 0, 740, 100);
    particleEditorPane.add(particleEditorLabel);

    particleEditorPane.addMouseListener(new MouseListener() {
      @Override public void mouseClicked(MouseEvent e) { EditorsEventHandler.startParticleEditor(null); }
      @Override public void mousePressed(MouseEvent e) { EditorsEventHandler.startParticleEditor(null); }
      @Override public void mouseReleased(MouseEvent e) { EditorsEventHandler.startParticleEditor(null); }
      @Override public void mouseEntered(MouseEvent e) {
        particleEditorPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        particleEditorImage = particleEditorImageFocused;
        particleEditorPane.repaint();
      }
      @Override public void mouseExited(MouseEvent e) {
        particleEditorPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        particleEditorImage = particleEditorImageUnfocused;
        particleEditorPane.repaint();
      }
    });

    editorListPane.add(modelViewerPane);
    editorListPane.add(sceneEditorPane);
    editorListPane.add(interfaceTesterPane);
    editorListPane.add(particleEditorPane);

  }

  protected static void startFakeProgress() {
    EditorsGUI.editorLaunchState.setText("Loading editor...");
    for(int i = editorLaunchFakeProgressBar.getMinimum(); i <= editorLaunchFakeProgressBar.getMaximum(); i++) {
      final int percent = i;
      SwingUtilities.invokeLater(() -> editorLaunchFakeProgressBar.setValue(percent));
      rotateFakeLabel(i);
      try {
        Thread.sleep(25);
      } catch (InterruptedException e) {
        log.error(e);
      }
    }
    EditorsEventHandler.finishedBooting();
  }

  protected static void rotateFakeLabel(int pot) {
    switch(pot) {
      case 30:
        EditorsGUI.editorLaunchState.setText("Compiling configs...");
        break;
      case 75:
        EditorsGUI.editorLaunchState.setText("Creating interface...");
        break;
      case 100:
        EditorsGUI.editorLaunchState.setText("Almost there...");
        break;
    }
  }

  protected void setupImages() {
    modelViewerImageFocused = ImageUtil.resizeImage(ImageUtil.loadImageWithinJar("/img/editor-model.png"), 740, 100);
    modelViewerImageFocused = (BufferedImage) ImageUtil.addRoundedCorners(new GaussianFilter(25f).filter(modelViewerImageFocused, null), 25);
    modelViewerImageUnfocused = new GrayscaleFilter().filter(modelViewerImageFocused, null);

    sceneEditorImageFocused = ImageUtil.resizeImage(ImageUtil.loadImageWithinJar("/img/editor-scene.png"), 740, 100);
    sceneEditorImageFocused = (BufferedImage) ImageUtil.addRoundedCorners(new GaussianFilter(25f).filter(sceneEditorImageFocused, null), 25);
    sceneEditorImageUnfocused = new GrayscaleFilter().filter(sceneEditorImageFocused, null);

    interfaceTesterImageFocused = ImageUtil.resizeImage(ImageUtil.loadImageWithinJar("/img/editor-default.png"), 740, 100);
    interfaceTesterImageFocused = (BufferedImage) ImageUtil.addRoundedCorners(new GaussianFilter(25f).filter(interfaceTesterImageFocused, null), 25);
    interfaceTesterImageUnfocused = new GrayscaleFilter().filter(interfaceTesterImageFocused, null);

    particleEditorImageFocused = ImageUtil.resizeImage(ImageUtil.loadImageWithinJar("/img/editor-default.png"), 740, 100);
    particleEditorImageFocused = (BufferedImage) ImageUtil.addRoundedCorners(new GaussianFilter(25f).filter(particleEditorImageFocused, null), 25);
    particleEditorImageUnfocused = new GrayscaleFilter().filter(particleEditorImageFocused, null);

    modelViewerImage = modelViewerImageUnfocused;
    sceneEditorImage = sceneEditorImageUnfocused;
    interfaceTesterImage = interfaceTesterImageUnfocused;
    particleEditorImage = particleEditorImageUnfocused;
  }

}
