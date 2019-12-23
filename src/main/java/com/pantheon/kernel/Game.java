package com.pantheon.kernel;

public class Game {
    protected CoreEngine engine;

    public Game() {
        engine = new CoreEngine();
    }

    public void init() {
        engine.init();
    }
    public void launch() { engine.start(); }

    public CoreEngine getEngine() { return engine; }
    public void setEngine(CoreEngine engine) { this.engine = engine; }
}
