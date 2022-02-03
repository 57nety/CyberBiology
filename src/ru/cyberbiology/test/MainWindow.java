package ru.cyberbiology.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ru.cyberbiology.test.prototype.IWindow;
import ru.cyberbiology.test.prototype.view.IView;
import ru.cyberbiology.test.view.ViewBasic;
import ru.cyberbiology.test.view.ViewMultiCell;

public class MainWindow extends JFrame implements IWindow
{
    JMenuItem runItem;

    public static MainWindow window;

    public static final int BOTW	= 4;
    public static final int BOTH	= 4;

    public static World world;

    public JLabel generationLabel = new JLabel(" Generation: 0 ");
    public JLabel populationLabel = new JLabel(" Population: 0 ");
    public JLabel organicLabel = new JLabel(" Organic: 0 ");

    /** буфер для отрисовки ботов */
    public Image buffer	= null;
    /** актуальный отрисовщик*/
    IView	view;
    /** Перечень возможных отрисовщиков*/
    IView[]  views = new IView[]
            {
                    new ViewBasic(),
                    new ViewMultiCell()
            };

    public JPanel paintPanel = new JPanel()
    {
        public void paint(Graphics g)
        {
            g.drawImage(buffer, 0, 0, null);
        };
    };
    public MainWindow()
    {
        window	= this;

        setTitle("Cyber Biology");
        setSize(new Dimension(1800, 900));
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize(), fSize = getSize();
        if (fSize.height > sSize.height) {
            fSize.height = sSize.height;
        }
        if (fSize.width  > sSize.width)  {
            fSize.width = sSize.width;
        }
        setSize(new Dimension(sSize.width, sSize.height));

        setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);

        Container container = getContentPane();

        container.setLayout(new BorderLayout());// у этого лейаута приятная особенность - центральная часть растягивается автоматически
        container.add(paintPanel, BorderLayout.CENTER);// добавляем нашу карту в центр


        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        container.add(statusPanel, BorderLayout.SOUTH);

        generationLabel.setPreferredSize(new Dimension(140, 18));
        generationLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(generationLabel);

        populationLabel.setPreferredSize(new Dimension(140, 18));
        populationLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(populationLabel);

        organicLabel.setPreferredSize(new Dimension(140, 18));
        organicLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusPanel.add(organicLabel);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Программа");
        runItem = new JMenuItem("Запустить");
        fileMenu.add(runItem);
        runItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // если мир создан, то его можно обновить
                if(world != null) {
                    world=null;
                }
                // если мир не создан, то его можно запустить
                if(world==null) {
                    int width = paintPanel.getWidth()/BOTW;// Ширина доступной части экрана для рисования карты
                    int height = paintPanel.getHeight()/BOTH;// Боты 4 пикселя?
                    world = new World(window,width,height);
                    world.generateAdam();
                    paint();
                    world.start();//Запускаем его
                    runItem.setText("Обновить модель");// изменил название
                }
            }
        });

        JMenuItem exitItem = new JMenuItem("Выйти");
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(fileMenu);

        JMenu ViewMenu = new JMenu("Вид");
        menuBar.add(ViewMenu);

        JMenuItem item;
        for(int i=0; i<views.length; i++) {
            item = new JMenuItem(views[i].getName());
            ViewMenu.add(item);
            item.addActionListener(new ViewMenuActionListener(this, views[i]));
        }

        this.setJMenuBar(menuBar);

        view = new ViewBasic();
        this.pack();
        this.setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
    }

    @Override
    public void setView(IView view) {
        this.view	= view;
    }

    public void paint() {
        buffer = this.view.paint(this.world,this.paintPanel);
        generationLabel.setText(" Generation: " + String.valueOf(world.generation));
        populationLabel.setText(" Population: " + String.valueOf(world.population));
        organicLabel.setText(" Organic: " + String.valueOf(world.organic));
        paintPanel.repaint();
    }

    public static void main(String[] args) {
        MainWindow.window	= new MainWindow();
    }
}
