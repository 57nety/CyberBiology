package ru.cyberbiology.test.prototype;

import ru.cyberbiology.test.prototype.view.IView;

public interface IWindow
{
    public void paint();

    public void setView(IView view);
}
