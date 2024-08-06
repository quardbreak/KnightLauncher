package com.luuqui.launcher;

import com.luuqui.dialog.DialogError;
import com.luuqui.dialog.DialogInfo;
import com.luuqui.dialog.DialogWarning;
import com.luuqui.discord.DiscordRPC;
import com.luuqui.launcher.mods.ModListGUI;
import com.luuqui.launcher.settings.SettingsGUI;
import com.luuqui.util.ColorUtil;
import com.luuqui.util.DesktopUtil;
import com.luuqui.util.ImageUtil;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class LauncherGUI extends BaseGUI {

  private final LauncherApp app;
  public static JFrame launcherGUIFrame;
  public static JPanel mainPane;
  public static BufferedImage banner = null;
  public static JLabel bannerTitle;
  public static JLabel bannerSubtitle1;
  public static JLabel bannerSubtitle2;
  public static JButton bannerLinkButton;
  public static JTabbedPane layeredSettingsPane = new JTabbedPane();
  public static JPanel layeredModsPane = new JPanel();
  public static JButton layeredReturnButton;
  public static JButton launchButton;
  public static JButton settingsButton;
  public static JButton modButton;
  public static JButton editorsButton;
  public static JButton updateButton;
  public static JButton playerCountTooltipButton;
  public static JTextPane tweetsContainer;
  public static JLabel launchState;
  public static JProgressBar launchProgressBar;
  public static JLabel imageContainer;
  public static JLabel playerCountLabel;
  public static JComboBox serverList;
  public static JButton serverInfoButton;
  public static JLabel warningLabelIcon;
  public static JLabel warningLabel;
  public static JButton warningNotice;
  public static String currentWarning = "";

  public LauncherGUI(LauncherApp app) {
    super();
    this.app = app;
    initialize();
  }

  @SuppressWarnings("static-access")
  public void switchVisibility() {
    this.launcherGUIFrame.setVisible(!this.launcherGUIFrame.isVisible());
  }

  /** @wbp.parser.entryPoint */
  @SuppressWarnings("static-access")
  private void initialize() {

    launcherGUIFrame = new JFrame();
    launcherGUIFrame.setVisible(false);
    launcherGUIFrame.setTitle(Locale.getValue("t.main", LauncherGlobals.LAUNCHER_VERSION));
    launcherGUIFrame.setResizable(false);
    launcherGUIFrame.setBounds(100, 100, 1050, 550);
    launcherGUIFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    launcherGUIFrame.setUndecorated(true);
    launcherGUIFrame.setIconImage(ImageUtil.loadImageWithinJar("/img/icon-128.png"));
    launcherGUIFrame.getContentPane().setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    launcherGUIFrame.getContentPane().setLayout(null);

    JPanel sidePane = new JPanel();
    sidePane.setBackground(CustomColors.INTERFACE_SIDEPANE_BACKGROUND);
    sidePane.setVisible(true);
    sidePane.setLayout(null);
    sidePane.setBounds(0, 35, 250, 550);
    launcherGUIFrame.getContentPane().add(sidePane);

    banner = generatePlainColorBanner();

    mainPane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(banner, 0, 0, null);
      }
    };
    mainPane.setLayout(null);
    mainPane.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    mainPane.setBounds(250, 35, 800, 550);
    launcherGUIFrame.getContentPane().add(mainPane);

    JLabel launcherLogo = new JLabel();
    BufferedImage launcherLogoImage = ImageUtil.loadImageWithinJar("/img/icon-92.png");
    launcherLogo.setBounds(80, -55, 256, 256);
    launcherLogo.setIcon(new ImageIcon(launcherLogoImage));
    sidePane.add(launcherLogo);

    JLabel launcherName = new JLabel(LauncherGlobals.LAUNCHER_NAME);
    launcherName.setFont(Fonts.fontMedBig);
    launcherName.setHorizontalAlignment(SwingConstants.CENTER);
    launcherName.setVerticalAlignment(SwingConstants.CENTER);
    launcherName.setBounds(0, 100, 250, 80);
    sidePane.add(launcherName);

    JLabel launcherVersion = new JLabel("v" + LauncherGlobals.LAUNCHER_VERSION);
    launcherVersion.setFont(Fonts.fontMedIta);
    launcherVersion.setHorizontalAlignment(SwingConstants.CENTER);
    launcherVersion.setVerticalAlignment(SwingConstants.CENTER);
    launcherVersion.setBounds(0, 150, 250, 25);
    sidePane.add(launcherVersion);

    JLabel serverListLabel = new JLabel("Server");
    serverListLabel.setFont(Fonts.fontMed);
    serverListLabel.setBounds(28, 210, 80, 20);
    sidePane.add(serverListLabel);

    serverList = new JComboBox<String>();
    serverList.setBounds(73, 210, 120, 20);
    serverList.setFont(Fonts.fontReg);
    serverList.setFocusable(false);
    serverList.setRequestFocusEnabled(false);
    sidePane.add(serverList);
    serverList.addActionListener(action -> LauncherEventHandler.selectedServerChanged(action));
    serverList.addItem("Official");

    Icon serverInfoButtonIcon = IconFontSwing.buildIcon(FontAwesome.INFO, 16, Color.WHITE);
    serverInfoButton = new JButton();
    serverInfoButton.setIcon(serverInfoButtonIcon);
    serverInfoButton.setBounds(200, 210, 20, 20);
    serverInfoButton.setEnabled(false);
    serverInfoButton.setVisible(false);
    serverInfoButton.setFocusable(false);
    serverInfoButton.setFocusPainted(false);
    serverInfoButton.setForeground(Color.WHITE);
    serverInfoButton.setToolTipText("Server Information");
    serverInfoButton.addActionListener(l -> LauncherEventHandler.displaySelectedServerInfo());
    sidePane.add(serverInfoButton);

    Icon playerCountIcon = IconFontSwing.buildIcon(FontAwesome.USERS, 14, CustomColors.INTERFACE_DEFAULT);
    playerCountLabel = new JLabel(Locale.getValue("m.player_count_load"));
    playerCountLabel.setFont(Fonts.fontReg);
    playerCountLabel.setIcon(playerCountIcon);
    playerCountLabel.setBounds(28, 235, 200, 18);
    sidePane.add(playerCountLabel);

    String playerCountTooltipTitle = "Where does this player count come from?";
    String playerCountTooltipText = "This player count is an approximation based on the currently online Steam players. The approximation is the Steam value increased by 1.6x to account for Standalone users.";
    Icon playerCountTooltipButtonIcon = IconFontSwing.buildIcon(FontAwesome.QUESTION, 12, Color.WHITE);
    playerCountTooltipButton = new JButton();
    playerCountTooltipButton.setIcon(playerCountTooltipButtonIcon);
    playerCountTooltipButton.setBounds(167, 238, 13, 13);
    playerCountTooltipButton.setEnabled(true);
    playerCountTooltipButton.setFocusable(false);
    playerCountTooltipButton.setFocusPainted(false);
    playerCountTooltipButton.setBorderPainted(false);
    playerCountTooltipButton.setForeground(Color.WHITE);
    playerCountTooltipButton.setToolTipText(playerCountTooltipTitle);
    playerCountTooltipButton.addActionListener(l -> {
      DialogInfo.push(playerCountTooltipText, playerCountTooltipTitle);
    });
    sidePane.add(playerCountTooltipButton);
    playerCountTooltipButton.setVisible(false);

    Icon settingsIcon = IconFontSwing.buildIcon(FontAwesome.COGS, 16, ColorUtil.getForegroundColor());
    settingsButton = new JButton(Locale.getValue("b.settings"));
    settingsButton.setIcon(settingsIcon);
    settingsButton.setBounds(28, 300, 125, 35);
    settingsButton.setHorizontalAlignment(SwingConstants.LEFT);
    settingsButton.setFont(Fonts.fontMed);
    settingsButton.setFocusPainted(false);
    settingsButton.setFocusable(false);
    settingsButton.setBorderPainted(false);
    settingsButton.setBackground(CustomColors.INTERFACE_SIDEPANE_BUTTON);
    settingsButton.setForeground(Color.WHITE);
    settingsButton.setToolTipText(Locale.getValue("b.settings"));
    settingsButton.addActionListener(action -> {
      mainPane.setVisible(false);
      layeredModsPane.setVisible(false);

      layeredSettingsPane = SettingsGUI.tabbedPane;
      layeredSettingsPane.setBounds(250, 75, 800, 550);
      launcherGUIFrame.add(layeredSettingsPane);
      layeredSettingsPane.setVisible(true);

      layeredReturnButton = new JButton(IconFontSwing.buildIcon(FontAwesome.ARROW_LEFT, 12, Color.WHITE));
      layeredReturnButton.setBounds(255, 40, 25, 25);
      layeredReturnButton.setVisible(true);
      layeredReturnButton.setFocusable(false);
      layeredReturnButton.setFocusPainted(false);
      layeredReturnButton.setBorder(null);
      layeredReturnButton.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
      layeredReturnButton.addActionListener(l -> {
        layeredSettingsPane.setVisible(false);
        layeredModsPane.setVisible(false);
        mainPane.setVisible(true);
        layeredReturnButton.setVisible(false);
      });
      launcherGUIFrame.add(layeredReturnButton);
    });
    sidePane.add(settingsButton);

    Icon modsIcon = IconFontSwing.buildIcon(FontAwesome.PUZZLE_PIECE, 16, ColorUtil.getForegroundColor());
    modButton = new JButton(Locale.getValue("b.mods"));
    modButton.setIcon(modsIcon);
    modButton.setBounds(28, 340, 125, 35);
    modButton.setHorizontalAlignment(SwingConstants.LEFT);
    modButton.setFont(Fonts.fontMed);
    modButton.setFocusPainted(false);
    modButton.setFocusable(false);
    modButton.setBorderPainted(false);
    modButton.setEnabled(true);
    modButton.setBackground(CustomColors.INTERFACE_SIDEPANE_BUTTON);
    modButton.setForeground(Color.WHITE);
    modButton.setToolTipText(Locale.getValue("b.mods"));
    modButton.addActionListener(action -> {
      mainPane.setVisible(false);
      layeredSettingsPane.setVisible(false);

      layeredModsPane = ModListGUI.modListPanel;
      layeredModsPane.setBounds(250, 75, 800, 550);
      launcherGUIFrame.add(layeredModsPane);
      layeredModsPane.setVisible(true);

      layeredReturnButton = new JButton(IconFontSwing.buildIcon(FontAwesome.ARROW_LEFT, 12, Color.WHITE));
      layeredReturnButton.setBounds(255, 40, 25, 25);
      layeredReturnButton.setVisible(true);
      layeredReturnButton.setFocusable(false);
      layeredReturnButton.setFocusPainted(false);
      layeredReturnButton.setBorder(null);
      layeredReturnButton.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
      layeredReturnButton.addActionListener(l -> {
        layeredSettingsPane.setVisible(false);
        layeredModsPane.setVisible(false);
        mainPane.setVisible(true);
        layeredReturnButton.setVisible(false);
      });
      launcherGUIFrame.add(layeredReturnButton);
    });
    sidePane.add(modButton);

    Icon editorsIcon = IconFontSwing.buildIcon(FontAwesome.PENCIL, 16, ColorUtil.getForegroundColor());
    editorsButton = new JButton("Editors");
    editorsButton.setIcon(editorsIcon);
    editorsButton.setBounds(28, 380, 125, 35);
    editorsButton.setHorizontalAlignment(SwingConstants.LEFT);
    editorsButton.setFont(Fonts.fontMed);
    editorsButton.setFocusPainted(false);
    editorsButton.setFocusable(false);
    editorsButton.setBorderPainted(false);
    editorsButton.setEnabled(true);
    editorsButton.setBackground(CustomColors.INTERFACE_SIDEPANE_BUTTON);
    editorsButton.setForeground(Color.WHITE);
    editorsButton.setToolTipText("Editors");
    editorsButton.addActionListener(action -> {
      /*
      mainPane.setVisible(false);
      layeredSettingsPane.setVisible(false);

      layeredModsPane = ModListGUI.modListPanel;
      layeredModsPane.setBounds(250, 75, 800, 550);
      launcherGUIFrame.add(layeredModsPane);
      layeredModsPane.setVisible(true);

      layeredReturnButton = new JButton(IconFontSwing.buildIcon(FontAwesome.ARROW_LEFT, 12, Color.WHITE));
      layeredReturnButton.setBounds(255, 40, 25, 25);
      layeredReturnButton.setVisible(true);
      layeredReturnButton.setFocusable(false);
      layeredReturnButton.setFocusPainted(false);
      layeredReturnButton.setBorder(null);
      layeredReturnButton.setBackground(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
      layeredReturnButton.addActionListener(l -> {
        layeredSettingsPane.setVisible(false);
        layeredModsPane.setVisible(false);
        mainPane.setVisible(true);
        layeredReturnButton.setVisible(false);
      });
      launcherGUIFrame.add(layeredReturnButton);
      */
    });
    sidePane.add(editorsButton);
    editorsButton.setVisible(false);

    JButton discordButton = new JButton(ImageUtil.imageStreamToIcon(LauncherGUI.class.getResourceAsStream("/img/icon-discord.png")));
    discordButton.setBounds(66, 465, 35, 35);
    discordButton.setToolTipText("Discord");
    discordButton.setFocusPainted(false);
    discordButton.setFocusable(false);
    discordButton.setBorderPainted(false);
    discordButton.setBackground(CustomColors.INTERFACE_SIDEPANE_BUTTON);
    discordButton.setFont(Fonts.fontMed);
    sidePane.add(discordButton);
    discordButton.addActionListener(e -> DesktopUtil.openWebpage(LauncherGlobals.URL_DISCORD));

    Icon bugIcon = IconFontSwing.buildIcon(FontAwesome.BUG, 16, Color.WHITE);
    JButton bugButton = new JButton(bugIcon);
    bugButton.setBounds(107, 465, 35, 35);
    bugButton.setToolTipText(Locale.getValue("b.bug_report"));
    bugButton.setFocusPainted(false);
    bugButton.setFocusable(false);
    bugButton.setBorderPainted(false);
    bugButton.setBackground(CustomColors.INTERFACE_SIDEPANE_BUTTON);
    bugButton.setFont(Fonts.fontMed);
    sidePane.add(bugButton);
    bugButton.addActionListener(e -> DesktopUtil.openWebpage(LauncherGlobals.URL_BUG_REPORT));

    Icon kofiIcon = IconFontSwing.buildIcon(FontAwesome.PAYPAL, 16, Color.WHITE);
    JButton kofiButton = new JButton(kofiIcon);
    kofiButton.setBounds(148, 465, 35, 35);
    kofiButton.setToolTipText(Locale.getValue("b.kofi"));
    kofiButton.setFocusPainted(false);
    kofiButton.setFocusable(false);
    kofiButton.setBorderPainted(false);
    kofiButton.setBackground(CustomColors.PREMIUM);
    kofiButton.setFont(Fonts.fontMed);
    sidePane.add(kofiButton);
    kofiButton.addActionListener(e -> DesktopUtil.openWebpage(LauncherGlobals.URL_KOFI));

    bannerTitle = new JLabel("Uh, oh");
    bannerTitle.setBounds(35, -60, 700, 340);
    bannerTitle.setFont(Fonts.fontMedGiant);
    bannerTitle.setForeground(Color.WHITE);
    mainPane.add(bannerTitle);

    bannerSubtitle1 = new JLabel("This server is not currently announcing anything.");
    bannerSubtitle1.setBounds(40, -15, 700, 340);
    bannerSubtitle1.setFont(Fonts.fontMedBig);
    bannerSubtitle1.setForeground(Color.WHITE);
    mainPane.add(bannerSubtitle1);

    bannerSubtitle2 = new JLabel("");
    bannerSubtitle2.setBounds(40, 5, 700, 340);
    bannerSubtitle2.setFont(Fonts.fontMedBig);
    bannerSubtitle2.setForeground(Color.WHITE);
    mainPane.add(bannerSubtitle2);

    bannerLinkButton = new JButton("Learn more");
    bannerLinkButton.setBounds(40, 195, 105, 25);
    bannerLinkButton.setFont(Fonts.fontMed);
    bannerLinkButton.setForeground(Color.WHITE);
    bannerLinkButton.setFocusPainted(false);
    bannerLinkButton.setFocusable(false);
    bannerLinkButton.setOpaque(false);
    bannerLinkButton.setBackground(CustomColors.INTERFACE_MAINPANE_BUTTON);
    bannerLinkButton.setBorderPainted(false);
    bannerLinkButton.setVisible(false);
    mainPane.add(bannerLinkButton);

    launchButton = new JButton("Play Now");
    launchButton.setBounds(572, 423, 200, 66);
    launchButton.setFont(Fonts.fontMedBig);
    launchButton.setFocusPainted(false);
    launchButton.setFocusable(false);
    launchButton.setBackground(CustomColors.LAUNCH);
    launchButton.setBorderPainted(false);
    launchButton.setForeground(Color.WHITE);
    launchButton.setToolTipText("Play Now");
    mainPane.add(launchButton);
    launchButton.addActionListener(action -> {
      if (KeyboardController.isShiftPressed() || KeyboardController.isAltPressed()) {
        LauncherEventHandler.launchGameAltEvent();
      } else {
        LauncherEventHandler.launchGameEvent();
      }
    });

    String launchTooltipTitle = "Launching alt accounts";
    String launchTooltipText = "Keep ALT or SHIFT pressed while launching to launch in alt account mode and save computing resources for your main instance.";
    Icon launchTooltipButtonIcon = IconFontSwing.buildIcon(FontAwesome.QUESTION, 16, Color.WHITE);
    JButton launchTooltipButton = new JButton();
    launchTooltipButton.setIcon(launchTooltipButtonIcon);
    launchTooltipButton.setBounds(548, 424, 20, 20);
    launchTooltipButton.setEnabled(true);
    launchTooltipButton.setFocusable(false);
    launchTooltipButton.setFocusPainted(false);
    launchTooltipButton.setBorderPainted(false);
    launchTooltipButton.setBackground(CustomColors.INTERFACE_MAINPANE_BUTTON);
    launchTooltipButton.setForeground(Color.WHITE);
    launchTooltipButton.setToolTipText(launchTooltipTitle);
    launchTooltipButton.addActionListener(l -> {
      DialogInfo.push(launchTooltipText, launchTooltipTitle);
    });
    mainPane.add(launchTooltipButton);

    launchState = new JLabel("");
    launchState.setHorizontalAlignment(SwingConstants.LEFT);
    launchState.setBounds(35, 420, 505, 25);
    launchState.setFont(Fonts.fontRegBig);
    launchState.setVisible(false);
    mainPane.add(launchState);

    launchProgressBar = new JProgressBar();
    launchProgressBar.setBounds(35, 450, 505, 25);
    launchProgressBar.setVisible(false);
    mainPane.add(launchProgressBar);

    Icon warningNoticeIcon = IconFontSwing.buildIcon(FontAwesome.EXCLAMATION_TRIANGLE, 16, Color.WHITE);
    warningNotice = new JButton(warningNoticeIcon);
    warningNotice.setBounds(737, 26, 35, 35);
    warningNotice.setToolTipText("Warning notice");
    warningNotice.setFocusPainted(false);
    warningNotice.setFocusable(false);
    warningNotice.setBorderPainted(false);
    warningNotice.setForeground(Color.WHITE);
    warningNotice.setBackground(CustomColors.MID_RED);
    warningNotice.setFont(Fonts.fontMed);
    warningNotice.setVisible(false);
    warningNotice.addActionListener(l -> {
      DialogError.push(currentWarning, "Warning notice");
    });
    mainPane.add(warningNotice);

    Icon updateIcon = IconFontSwing.buildIcon(FontAwesome.CLOUD_DOWNLOAD, 16, Color.WHITE);
    updateButton = new JButton(updateIcon);
    updateButton.setBounds(737, 26, 35, 35);
    updateButton.setToolTipText(Locale.getValue("b.update_available"));
    updateButton.setFont(Fonts.fontMed);
    updateButton.setFocusPainted(false);
    updateButton.setFocusable(false);
    updateButton.setBorderPainted(false);
    updateButton.setBackground(CustomColors.UPDATE);
    updateButton.setForeground(Color.WHITE);
    updateButton.setVisible(false);
    mainPane.add(updateButton);
    updateButton.addActionListener(l -> LauncherEventHandler.updateLauncher());

    JPanel titleBar = new JPanel();
    titleBar.setBounds(0, 0, launcherGUIFrame.getWidth(), 35);
    titleBar.setBackground(ColorUtil.getTitleBarColor());
    launcherGUIFrame.getContentPane().add(titleBar);


    /*
     * Based on Paul Samsotha's reply @ StackOverflow
     * link: https://stackoverflow.com/questions/24476496/drag-and-resize-undecorated-jframe
     */
    titleBar.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent me) {

        pX = me.getX();
        pY = me.getY();
      }
    });
    titleBar.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me) {

        pX = me.getX();
        pY = me.getY();
      }

      @Override
      public void mouseDragged(MouseEvent me) {

        launcherGUIFrame.setLocation(launcherGUIFrame.getLocation().x + me.getX() - pX,
                launcherGUIFrame.getLocation().y + me.getY() - pY);
      }
    });
    titleBar.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent me) {

        launcherGUIFrame.setLocation(launcherGUIFrame.getLocation().x + me.getX() - pX,
                launcherGUIFrame.getLocation().y + me.getY() - pY);
      }

      @Override
      public void mouseMoved(MouseEvent arg0) {
        // Auto-generated method stub
      }
    });
    titleBar.setLayout(null);

    /*
    JLabel windowTitle = new JLabel(Locale.getValue("t.main", LauncherGlobals.LAUNCHER_VERSION));
    windowTitle.setFont(Fonts.fontMed);
    windowTitle.setBounds(10, 0, launcherGUIFrame.getWidth() - 200, 35);
    titleBar.add(windowTitle);
     */

    Icon closeIcon = IconFontSwing.buildIcon(FontAwesome.TIMES, 20, ColorUtil.getForegroundColor());
    JButton closeButton = new JButton(closeIcon);
    closeButton.setBounds(launcherGUIFrame.getWidth() - 38, 3, 29, 29);
    closeButton.setToolTipText(Locale.getValue("b.close"));
    closeButton.setFocusPainted(false);
    closeButton.setFocusable(false);
    closeButton.setBackground(null);
    closeButton.setBorder(null);
    closeButton.setFont(Fonts.fontMed);
    titleBar.add(closeButton);
    closeButton.addActionListener(e -> {
      DiscordRPC.getInstance().stop();
      System.exit(0);
    });

    Icon minimizeIcon = IconFontSwing.buildIcon(FontAwesome.CHEVRON_DOWN, 20, ColorUtil.getForegroundColor());
    JButton minimizeButton = new JButton(minimizeIcon);
    minimizeButton.setBounds(launcherGUIFrame.getWidth() - 71, 3, 29, 29);
    minimizeButton.setToolTipText(Locale.getValue("b.minimize"));
    minimizeButton.setFocusPainted(false);
    minimizeButton.setFocusable(false);
    minimizeButton.setBackground(null);
    minimizeButton.setBorder(null);
    minimizeButton.setFont(Fonts.fontMed);
    titleBar.add(minimizeButton);
    minimizeButton.addActionListener(e -> launcherGUIFrame.setState(Frame.ICONIFIED));

    launcherGUIFrame.setLocationRelativeTo(null);

    launcherGUIFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent windowEvent) {
        DiscordRPC.getInstance().stop();
      }
    });

  }

  public static void showWarning(String message) {
    // we're also showing an available update, lets move the warning notice
    // slightly to the left, so they don't overlap.
    if(updateButton.isVisible()) {
      warningNotice.setBounds(warningNotice.getX() - 45, 26, 35, 35);
    }

    warningNotice.setVisible(true);
    currentWarning = message;
  }

  public static BufferedImage generatePlainColorBanner() {
    BufferedImage image = new BufferedImage(800, 550, 1);
    Graphics2D g2d = image.createGraphics();
    g2d.setColor(CustomColors.INTERFACE_MAINPANE_BACKGROUND);
    g2d.fillRect(0, 0, 800, 550);
    return image;
  }

  public static BufferedImage processImageForBanner(BufferedImage image, double intensity) {
    image = ImageUtil.resizeImage(image, 800, 550);
    image = ImageUtil.fadeEdges(image, intensity);
    return image;
  }
}
