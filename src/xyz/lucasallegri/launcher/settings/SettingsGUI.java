package xyz.lucasallegri.launcher.settings;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import mdlaf.utils.MaterialBorders;
import xyz.lucasallegri.launcher.Fonts;
import xyz.lucasallegri.launcher.Language;
import xyz.lucasallegri.launcher.LauncherGUI;
import xyz.lucasallegri.logging.KnightLog;
import xyz.lucasallegri.util.ColorUtil;
import xyz.lucasallegri.util.SystemUtil;

import javax.swing.SwingConstants;
import javax.swing.JToggleButton;

public class SettingsGUI {

	public static JFrame settingsGUIFrame;
	public static JComboBox<String> choicePlatform;
	public static JComboBox<String> choiceLanguage;
	public static JComboBox<String> choiceStyle;
	public static JComboBox<String> choiceMemory;
	public static JComboBox<String> choiceGC;
	public static JToggleButton switchCleaning;
	public static JToggleButton switchKeepOpen;
	public static JToggleButton switchShortcut;
	public static JButton forceRebuildButton;
	public static JToggleButton switchStringDedup;
	public static JToggleButton switchUseCustomGC;
	public static JToggleButton switchUseIngameRPC;
	public static JEditorPane argumentsPane;
	
	int pY, pX;

	public static void compose() {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					SettingsGUI window = new SettingsGUI();
					window.settingsGUIFrame.setVisible(true);
				} catch (Exception e) {
					KnightLog.logException(e);
				}
			}
		});
	}

	public SettingsGUI() {
		LauncherGUI.launcherGUIFrame.setVisible(false);
		initialize();
	}

	private void initialize() {
		settingsGUIFrame = new JFrame();
		settingsGUIFrame.setTitle(Language.getValue("t.settings"));
		settingsGUIFrame.setBounds(100, 100, 850, 475);
		settingsGUIFrame.setResizable(false);
		settingsGUIFrame.setUndecorated(true);
		settingsGUIFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsGUIFrame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBounds(-2, 20, 852, 455);
		tabbedPane.setFont(Fonts.fontMedBig);
		tabbedPane.addTab(Language.getValue("tab.appearance"), createAppearancePanel());
		tabbedPane.addTab(Language.getValue("tab.behavior"), createBehaviorPanel());
		tabbedPane.addTab(Language.getValue("tab.game"), createGamePanel());
		tabbedPane.addTab(Language.getValue("tab.files"), createFilesPanel());
		tabbedPane.addTab(Language.getValue("tab.extratxt"), createExtraPanel());
		if(SystemUtil.isWindows() && SystemUtil.is64Bit()) tabbedPane.addTab(Language.getValue("tab.ingame_rpc"), createIngameRPCPanel());
		//tabbedPane.addTab(Language.getValue("tab.connection"), createConnectionPanel());
		settingsGUIFrame.getContentPane().add(tabbedPane);
		
		JPanel titleBar = new JPanel();
		titleBar.setBounds(0, 0, settingsGUIFrame.getWidth(), 20);
		titleBar.setBackground(ColorUtil.getTitleBarColor());
		settingsGUIFrame.getContentPane().add(titleBar);
		
		
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
		
		    	settingsGUIFrame.setLocation(settingsGUIFrame.getLocation().x + me.getX() - pX,
		    	settingsGUIFrame.getLocation().y + me.getY() - pY);
		    }
		});
		titleBar.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent me) {
		
				settingsGUIFrame.setLocation(settingsGUIFrame.getLocation().x + me.getX() - pX,
				settingsGUIFrame.getLocation().y + me.getY() - pY);
		    }
		
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// Auto-generated method stub
			}
		});
		titleBar.setLayout(null);
		
		JLabel windowTitle = new JLabel(Language.getValue("t.settings"));
		windowTitle.setFont(Fonts.fontMed);
		windowTitle.setBounds(10, 0, settingsGUIFrame.getWidth() - 100, 20);
		titleBar.add(windowTitle);
		
		Icon closeIcon = IconFontSwing.buildIcon(FontAwesome.TIMES, 14, ColorUtil.getForegroundColor());
		JButton closeButton = new JButton(closeIcon);
		closeButton.setBounds(settingsGUIFrame.getWidth() - 18, 1, 20, 21);
		closeButton.setToolTipText(Language.getValue("b.close"));
		closeButton.setFocusPainted(false);
		closeButton.setFocusable(false);
		closeButton.setBorder(MaterialBorders.roundedLineColorBorder(ColorUtil.getTitleBarColor(), 0));
		closeButton.setFont(Fonts.fontMed);
		titleBar.add(closeButton);
		closeButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		       settingsGUIFrame.dispose();
		    }
		});
		
		Icon minimizeIcon = IconFontSwing.buildIcon(FontAwesome.CHEVRON_DOWN, 14, ColorUtil.getForegroundColor());
		JButton minimizeButton = new JButton(minimizeIcon);
		minimizeButton.setBounds(settingsGUIFrame.getWidth() - 38, 1, 20, 21);
		minimizeButton.setToolTipText(Language.getValue("b.minimize"));
		minimizeButton.setFocusPainted(false);
		minimizeButton.setFocusable(false);
		minimizeButton.setBorder(MaterialBorders.roundedLineColorBorder(ColorUtil.getTitleBarColor(), 0));
		minimizeButton.setFont(Fonts.fontMed);
		titleBar.add(minimizeButton);
		
		settingsGUIFrame.setLocationRelativeTo(null);
		
		settingsGUIFrame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosed(WindowEvent windowEvent) {
		    	LauncherGUI.launcherGUIFrame.setVisible(true);
		        SettingsEventHandler.saveAdditionalArgs();
		    }
		});
		
	}
	
	protected JPanel createAppearancePanel() {
		JPanel appearancePanel = new JPanel();
		appearancePanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.appearance"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		appearancePanel.add(headerLabel);
		
		JLabel labelStyle = new JLabel(Language.getValue("m.launcher_style"));
		labelStyle.setBounds(25, 90, 175, 18);
		labelStyle.setFont(Fonts.fontRegBig);
		appearancePanel.add(labelStyle);
		
		choiceStyle = new JComboBox<String>();
		choiceStyle.setBounds(25, 115, 150, 20);
		choiceStyle.setFocusable(false);
		choiceStyle.setFont(Fonts.fontReg);
		choiceStyle.addItem(Language.getValue("o.dark"));
		choiceStyle.addItem(Language.getValue("o.light"));
		appearancePanel.add(choiceStyle);
		choiceStyle.setSelectedIndex(Settings.launcherStyle.equals("dark") ? 0 : 1);
		choiceStyle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				SettingsEventHandler.styleChangeEvent(event);
			}
		});
		
		JLabel labelLanguage = new JLabel(Language.getValue("m.language"));
		labelLanguage.setBounds(225, 90, 175, 18);
		labelLanguage.setFont(Fonts.fontRegBig);
		appearancePanel.add(labelLanguage);
		
		choiceLanguage = new JComboBox<String>();
		choiceLanguage.setBounds(225, 115, 150, 20);
		choiceLanguage.setFocusable(false);
		choiceLanguage.setFont(Fonts.fontReg);
		for(String lang : Language.AVAILABLE_LANGUAGES) {
			choiceLanguage.addItem(lang);
		}
		appearancePanel.add(choiceLanguage);
		choiceLanguage.setSelectedItem(Language.getLangName(Settings.lang));
		choiceLanguage.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				SettingsEventHandler.languageChangeEvent(event);
			}
		});
		
		return appearancePanel;
	}
	
	protected JPanel createBehaviorPanel() {
		JPanel behaviorPanel = new JPanel();
		behaviorPanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.behavior"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		behaviorPanel.add(headerLabel);
		
		JLabel labelCleaning = new JLabel(Language.getValue("m.rebuilds"));
		labelCleaning.setBounds(25, 90, 350, 18);
		labelCleaning.setFont(Fonts.fontRegBig);
		behaviorPanel.add(labelCleaning);
		
		JLabel labelCleaningExplained = new JLabel(Language.getValue("m.file_cleaning_explained"));
		labelCleaningExplained.setBounds(25, 110, 600, 16);
		labelCleaningExplained.setFont(Fonts.fontReg);
		behaviorPanel.add(labelCleaningExplained);
		
		switchCleaning = new JToggleButton("");
		switchCleaning.setBounds(790, 95, 30, 23);
		switchCleaning.setFocusPainted(false);
		behaviorPanel.add(switchCleaning);
		switchCleaning.setSelected(Settings.doRebuilds);
		switchCleaning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.rebuildsChangeEvent(_action);
			}
		});
		
		JSeparator sep = new JSeparator();
		sep.setBounds(25, 140, 800, 16);
		behaviorPanel.add(sep);
		
		JLabel labelKeepOpen = new JLabel(Language.getValue("m.keep_open"));
		labelKeepOpen.setBounds(25, 155, 350, 18);
		labelKeepOpen.setFont(Fonts.fontRegBig);
		behaviorPanel.add(labelKeepOpen);
		
		JLabel labelKeepOpenExplained = new JLabel(Language.getValue("m.keep_open_explained"));
		labelKeepOpenExplained.setBounds(25, 175, 600, 16);
		labelKeepOpenExplained.setFont(Fonts.fontReg);
		behaviorPanel.add(labelKeepOpenExplained);
		
		switchKeepOpen = new JToggleButton("");
		switchKeepOpen.setBounds(790, 160, 30, 23);
		switchKeepOpen.setFocusPainted(false);
		behaviorPanel.add(switchKeepOpen);
		switchKeepOpen.setSelected(Settings.keepOpen);
		switchKeepOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.keepOpenChangeEvent(_action);
			}
		});
		
		JSeparator sep2 = new JSeparator();
		sep2.setBounds(25, 205, 800, 16);
		behaviorPanel.add(sep2);
		
		JLabel labelShortcut = new JLabel(Language.getValue("m.create_shortcut"));
		labelShortcut.setBounds(25, 220, 225, 18);
		labelShortcut.setFont(Fonts.fontRegBig);
		behaviorPanel.add(labelShortcut);
		
		JLabel labelShortcutExplained = new JLabel(Language.getValue("m.create_shortcut_explained"));
		labelShortcutExplained.setBounds(25, 240, 600, 16);
		labelShortcutExplained.setFont(Fonts.fontReg);
		behaviorPanel.add(labelShortcutExplained);
		
		switchShortcut = new JToggleButton("");
		switchShortcut.setBounds(790, 225, 30, 23);
		switchShortcut.setFocusPainted(false);
		behaviorPanel.add(switchShortcut);
		switchShortcut.setSelected(Settings.createShortcut);
		switchShortcut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.createShortcutChangeEvent(_action);
			}
		});
		
		return behaviorPanel;
	}
	
	protected JPanel createGamePanel() {
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.game"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		gamePanel.add(headerLabel);
		
		JLabel labelStyle = new JLabel(Language.getValue("m.platform"));
		labelStyle.setBounds(25, 70, 125, 18);
		labelStyle.setFont(Fonts.fontRegBig);
		gamePanel.add(labelStyle);
		
		choicePlatform = new JComboBox<String>();
		choicePlatform.setBounds(25, 95, 150, 20);
		choicePlatform.setFont(Fonts.fontReg);
		choicePlatform.setFocusable(false);
		gamePanel.add(choicePlatform);
		choicePlatform.addItem(Language.getValue("o.steam"));
		choicePlatform.addItem(Language.getValue("o.standalone"));
		choicePlatform.setSelectedItem(Settings.gamePlatform);
		choicePlatform.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				SettingsEventHandler.platformChangeEvent(event);
			}
		});
		
		JLabel labelMemory = new JLabel(Language.getValue("m.allocated_memory"));
		labelMemory.setBounds(225, 70, 275, 18);
		labelMemory.setFont(Fonts.fontRegBig);
		gamePanel.add(labelMemory);
		
		choiceMemory = new JComboBox<String>();
		choiceMemory.setBounds(225, 95, 150, 20);
		choiceMemory.setFocusable(false);
		choiceMemory.setFont(Fonts.fontReg);
		gamePanel.add(choiceMemory);
		choiceMemory.addItem(Language.getValue("o.memory_256"));
		choiceMemory.addItem(Language.getValue("o.memory_512"));
		choiceMemory.addItem(Language.getValue("o.memory_768"));
		choiceMemory.addItem(Language.getValue("o.memory_1024"));
		choiceMemory.addItem(Language.getValue("o.memory_1536"));
		choiceMemory.addItem(Language.getValue("o.memory_2048"));
		choiceMemory.addItem(Language.getValue("o.memory_2560"));
		choiceMemory.addItem(Language.getValue("o.memory_3072"));
		if(SystemUtil.is64Bit()) {
			choiceMemory.addItem(Language.getValue("o.memory_4096"));
			choiceMemory.addItem(Language.getValue("o.memory_5120"));
			choiceMemory.addItem(Language.getValue("o.memory_6144"));
			choiceMemory.addItem(Language.getValue("o.memory_8192"));
			choiceMemory.addItem(Language.getValue("o.memory_16384"));
		}
		choiceMemory.setSelectedIndex(parseSelectedMemoryAsIndex());
		choiceMemory.setToolTipText((String)choiceMemory.getSelectedItem());
		choiceMemory.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				SettingsEventHandler.memoryChangeEvent(event);
			}
		});
		
		JSeparator sep = new JSeparator();
		sep.setBounds(25, 140, 800, 16);
		gamePanel.add(sep);
		
		JLabel labelStringDedup = new JLabel(Language.getValue("m.use_string_deduplication"));
		labelStringDedup.setBounds(25, 155, 375, 18);
		labelStringDedup.setFont(Fonts.fontRegBig);
		gamePanel.add(labelStringDedup);
		
		JLabel labelStringDedupExplained = new JLabel(Language.getValue("m.string_deduplication_explained"));
		labelStringDedupExplained.setBounds(25, 175, 600, 16);
		labelStringDedupExplained.setFont(Fonts.fontReg);
		gamePanel.add(labelStringDedupExplained);
		
		switchStringDedup = new JToggleButton("");
		switchStringDedup.setBounds(790, 160, 30, 23);
		switchStringDedup.setFocusPainted(false);
		gamePanel.add(switchStringDedup);
		switchStringDedup.setSelected(Settings.gameUseStringDeduplication);
		switchStringDedup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.useStringDeduplicationChangeEvent(_action);
			}
		});
		
		JSeparator sep2 = new JSeparator();
		sep2.setBounds(25, 205, 800, 16);
		gamePanel.add(sep2);
		
		JLabel labelUseCustomGC = new JLabel("Use a different GC behavior");
		labelUseCustomGC.setBounds(25, 220, 275, 18);
		labelUseCustomGC.setFont(Fonts.fontRegBig);
		gamePanel.add(labelUseCustomGC);
		
		JLabel labelUseCustomGCExplained = new JLabel("Change how Garbage Collection will be done on the game's Java VM, this will disable Explicit GC");
		labelUseCustomGCExplained.setBounds(25, 240, 600, 16);
		labelUseCustomGCExplained.setFont(Fonts.fontReg);
		gamePanel.add(labelUseCustomGCExplained);
		
		switchUseCustomGC = new JToggleButton("");
		switchUseCustomGC.setBounds(790, 225, 30, 23);
		switchUseCustomGC.setFocusPainted(false);
		gamePanel.add(switchUseCustomGC);
		switchUseCustomGC.setSelected(Settings.gameDisableExplicitGC);
		switchUseCustomGC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.customGCChangeEvent(_action);
			}
		});
		switchUseCustomGC.setEnabled(SystemUtil.is64Bit());
		
		choiceGC = new JComboBox<String>();
		choiceGC.setBounds(670, 225, 100, 20);
		choiceGC.setFocusable(false);
		choiceGC.setEnabled(false);
		choiceGC.setFont(Fonts.fontReg);
		gamePanel.add(choiceGC);
		choiceGC.addItem("Serial GC");
		choiceGC.addItem("G1GC");
		choiceGC.setSelectedIndex(0);
//		choiceGC.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent event) {
//				SettingsEventHandler.choiceGCChangeEvent(event);
//			}
//		});
		
		return gamePanel;
	}
	
	protected JPanel createFilesPanel() {
		JPanel filesPanel = new JPanel();
		filesPanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.files"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		filesPanel.add(headerLabel);
		
		JLabel labelFileClean = new JLabel(Language.getValue("b.force_rebuild"));
		labelFileClean.setBounds(25, 90, 275, 18);
		labelFileClean.setFont(Fonts.fontRegBig);
		filesPanel.add(labelFileClean);
		
		JLabel labelFileCleanExplained = new JLabel(Language.getValue("m.clean_files_explained"));
		labelFileCleanExplained.setBounds(25, 110, 600, 16);
		labelFileCleanExplained.setFont(Fonts.fontReg);
		filesPanel.add(labelFileCleanExplained);
		
		Icon startIcon = IconFontSwing.buildIcon(FontAwesome.SHARE, 16, ColorUtil.getForegroundColor());
		JButton forceRebuildButton = new JButton(startIcon);
		forceRebuildButton.setBounds(790, 95, 30, 23);
		forceRebuildButton.setFocusPainted(false);
		forceRebuildButton.setFocusable(false);
		filesPanel.add(forceRebuildButton);
		forceRebuildButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsGUI.settingsGUIFrame.setVisible(false);
				LauncherGUI.launcherGUIFrame.setVisible(true);
				SettingsEventHandler.forceRebuildEvent();
			}
		});
		
		JSeparator sep = new JSeparator();
		sep.setBounds(25, 140, 800, 16);
		filesPanel.add(sep);
		
		JLabel labelJVMPatch = new JLabel(Language.getValue("m.force_jvm_patch"));
		labelJVMPatch.setBounds(25, 155, 350, 18);
		labelJVMPatch.setFont(Fonts.fontRegBig);
		filesPanel.add(labelJVMPatch);
		
		JLabel labelJVMPatchExplained = new JLabel(Language.getValue("m.force_jvm_patch_explained"));
		labelJVMPatchExplained.setBounds(25, 175, 600, 16);
		labelJVMPatchExplained.setFont(Fonts.fontReg);
		filesPanel.add(labelJVMPatchExplained);
		
		JButton jvmPatchButton = new JButton(startIcon);
		jvmPatchButton.setBounds(790, 160, 30, 23);
		jvmPatchButton.setFocusPainted(false);
		jvmPatchButton.setFocusable(false);
		filesPanel.add(jvmPatchButton);
		jvmPatchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.jvmPatchEvent(_action);
			}
		});
		jvmPatchButton.setEnabled(SystemUtil.is64Bit());
		
		return filesPanel;
	}
	
	protected JPanel createExtraPanel() {
		JPanel extraPanel = new JPanel();
		extraPanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.extratxt"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		extraPanel.add(headerLabel);
		
		JLabel labelArguments = new JLabel(Language.getValue("m.extratxt_write_arguments"));
		labelArguments.setBounds(25, 90, 600, 18);
		labelArguments.setFont(Fonts.fontRegBig);
		extraPanel.add(labelArguments);
		
		argumentsPane = new JEditorPane();
		argumentsPane.setFont(Fonts.fontReg);
		argumentsPane.setBounds(25, 120, 255, 85);
		extraPanel.add(argumentsPane);
		argumentsPane.setText(Settings.gameAdditionalArgs);
		
		JScrollPane scrollBar = new JScrollPane(argumentsPane);
		scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar.setBounds(25, 120, 800, 265);
		extraPanel.add(scrollBar);
		
		return extraPanel;
	}
	
	protected JPanel createIngameRPCPanel() {
		JPanel ingameRPCPanel = new JPanel();
		ingameRPCPanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.ingame_rpc"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		ingameRPCPanel.add(headerLabel);
		
		JLabel labelUseIngameRPC = new JLabel(Language.getValue("m.use_ingame_rpc"));
		labelUseIngameRPC.setBounds(25, 90, 350, 18);
		labelUseIngameRPC.setFont(Fonts.fontRegBig);
		ingameRPCPanel.add(labelUseIngameRPC);
		
		JLabel labelUseIngameRPCExplained = new JLabel(Language.getValue("m.use_ingame_rpc_explained"));
		labelUseIngameRPCExplained.setBounds(25, 110, 600, 16);
		labelUseIngameRPCExplained.setFont(Fonts.fontReg);
		ingameRPCPanel.add(labelUseIngameRPCExplained);
		
		switchUseIngameRPC = new JToggleButton("");
		switchUseIngameRPC.setBounds(790, 95, 30, 23);
		switchUseIngameRPC.setFocusPainted(false);
		ingameRPCPanel.add(switchUseIngameRPC);
		switchUseIngameRPC.setSelected(Settings.useIngameRPC);
		switchUseIngameRPC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent _action) {
				SettingsEventHandler.ingameRPCChangeEvent(_action);
			}
		});
		
		return ingameRPCPanel;
	}
	
	protected JPanel createConnectionPanel() {
		JPanel connectionPanel = new JPanel();
		connectionPanel.setLayout(null);
		
		JLabel headerLabel = new JLabel(Language.getValue("tab.connection"));
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setBounds(25, 11, 450, 50);
		headerLabel.setFont(Fonts.fontMedGiant);
		connectionPanel.add(headerLabel);
		
		JLabel soonLabel = new JLabel(Language.getValue("m.coming_soon"));
		soonLabel.setHorizontalAlignment(SwingConstants.LEFT);
		soonLabel.setBounds(25, 90, 450, 50);
		soonLabel.setFont(Fonts.fontRegBig);
		connectionPanel.add(soonLabel);
		
		return connectionPanel;
	}
 	
	public static int parseSelectedMemoryAsInt() {
		switch(choiceMemory.getSelectedIndex()) {
		case 0: return 256;
		case 1: return 512;
		case 2: return 768;
		case 3: return 1024;
		case 4: return 1536;
		case 5: return 2048;
		case 6: return 2560;
		case 7: return 3072;
		case 8: return 4096;
		case 9: return 5120;
		case 10: return 6144;
		case 11: return 8192;
		case 12: return 16384;
		}
		return 512;
	}
	
	public static int parseSelectedMemoryAsIndex() {
		switch(Settings.gameMemory) {
		case 256: return 0;
		case 512: return 1;
		case 768: return 2;
		case 1024: return 3;
		case 1536: return 4;
		case 2048: return 5;
		case 2560: return 6;
		case 3072: return 7;
		case 4096: return 8;
		case 5120: return 9;
		case 6144: return 10;
		case 8192: return 11;
		case 16384: return 12;
		}
		return 0;
	}
	
}
