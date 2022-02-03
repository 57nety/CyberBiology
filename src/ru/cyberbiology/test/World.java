package ru.cyberbiology.test;

import ru.cyberbiology.test.prototype.IWindow;
import ru.cyberbiology.test.prototype.IWorld;

public class World implements IWorld
{
    public World world;
    public IWindow window;

    public static final int BOTW = 4;
    public static final int BOTH = 4;

    public int width;
    public int height;

    public Bot[][] matrix; // Матрица мира
    public int generation;
    public int population;
    public int organic;

    boolean started;
    Worker thread;
    protected World(IWindow win) {
        world = this;
        window = win;
        population = 0;
        generation = 0;
        organic = 0;
    }

    public World(IWindow win, int width, int height) {
        this(win);
        this.setSize(width, height);
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new Bot[width][height];
    }

    @Override
    public void setBot(Bot bot) {
        this.matrix[bot.x][bot.y] = bot;
    }

    public void paint() {
        window.paint();
    }

    class Worker extends Thread
    {
        public void run()
        {
            started = true;// Флаг работы потока, если установить в false поток
            // заканчивает работу
            while (started)
            {
                // обновляем матрицу
                for (int y = 0; y < height; y++)
                {
                    for (int x = 0; x < width; x++)
                    {
                        if (matrix[x][y] != null)
                        {
                            /*if (matrix[x][y].alive == 3)*/
                            {
                                matrix[x][y].step(); // выполняем шаг бота
                            }
                        }
                    }
                }

                generation = generation + 1;
                if (generation % 10 == 0) {
                    // отрисовка на экран через каждые ... шагов
                    paint(); // отображаем текущее состояние симуляции на экран
                }
            }
            paint();// если запаузили рисуем актуальную картинку
            started = false;// Закончили работу
        }
    }

    public void generateAdam()
    {
        // ========== 1 ==============
        // бот номер 1 - это уже реальный бот
        Bot bot = new Bot(this);

        bot.adr = 0;
        bot.x = width / 2; // координаты бота
        bot.y = height / 2;
        bot.health = 990; // энергия
        bot.mineral = 0; // минералы
        bot.alive = 3; // отмечаем, что бот живой
        bot.c_red = 170; // задаем цвет бота
        bot.c_blue = 170;
        bot.c_green = 170;
        bot.direction = 5; // направление
        bot.mprev = null; // бот не входит в многоклеточные цепочки, поэтому
        // ссылки
        bot.mnext = null; // на предыдущего, следующего в многоклеточной цепочке
        // пусты
        for (int i = 0; i < 64; i++)
        { // заполняем геном командой 25 - фотосинтез
            bot.mind[i] = 25;
        }

        matrix[bot.x][bot.y] = bot; // даём ссылку на бота в массиве world[]
    }

    public boolean started() {
        return this.thread != null;
    }

    public void start() {
        if (!this.started())
        {
            this.thread = new Worker();
            this.thread.start();
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
