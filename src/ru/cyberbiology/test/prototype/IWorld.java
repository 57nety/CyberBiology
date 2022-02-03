package ru.cyberbiology.test.prototype;

import ru.cyberbiology.test.Bot;

public interface IWorld
{
    public int getHeight();

    public void setSize(int width, int height);

    public void setBot(Bot bot);

    public void paint();
}
